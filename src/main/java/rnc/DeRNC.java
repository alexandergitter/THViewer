/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2008, Alexander Gitter
 
 All rights reserved.
*/

package rnc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DeRNC {

    public static void main(String args[]) throws IOException {
        main_unpack(new File(args[0]), new File(args[1]));
    }

    public static void main_unpack(File inFile, File outFile)
            throws FileNotFoundException, IOException {
        FileInputStream ir = new FileInputStream(inFile);

        if (ir.read() != 'R' || ir.read() != 'N' || ir.read() != 'C') {
            ir.close();
            return;
        }

        ir.getChannel().position(0);

        byte packed[] = new byte[(int) inFile.length()];
        ir.read(packed);
        ir.close();

        CharPointer pp = new CharPointer(packed);
        int ulen = rnc_ulen(pp);

        byte unpacked[] = new byte[ulen];

        rnc_unpack(pp, new CharPointer(unpacked));

        FileOutputStream or = new FileOutputStream(outFile);
        or.write(unpacked);
        or.close();
    }

    // XXX
    /*
     * private boolean check_signature(byte[] packed) { return (packed[0] == 'R' &&
     * packed[1] == 'N' && packed[2] == 'C' && packed[3] == 0x01); }
     */

    /*
     * Return the uncompressed length of a packed data block, or a negative
     * error code.
     */
    protected static int rnc_ulen(CharPointer p)
    throws IllegalArgumentException {
        return blong(new CharPointer(p, 4));
    }

    protected static void rnc_unpack(CharPointer input, CharPointer output)
    throws IOException {
        input.inc(18);
        
        BitStream bis = new BitStream(input);
        
        
        /*
         * CRC-Check should be done here
         */
        
        bis.bit_advance(2, input);
        
        while(output.pos() < output.len()) {
            Huftable raw = new Huftable();
            raw.read(bis, input);
            Huftable dist = new Huftable();
            dist.read(bis, input);
            Huftable len = new Huftable();
            len.read(bis, input);
            
            int ch_count = bis.bit_read (0xFFFF, 16, input);
            
            while (true) {
                int length, posn;

                length = raw.huf_read(bis, input);
                
                if (length < 0)
                    throw new IOException();
                
                if (length > 0) {
                    while( length-- > 0) {
                        output.set(input.get());
                        output.inc();
                        input.inc();
                    }
                    
                    bis.bitread_fix(input);
                }
                
                if (--ch_count <= 0)
                    break;
                
                posn = dist.huf_read (bis, input);
                
                if (posn == -1)
                    throw new IOException();
                
                length = len.huf_read (bis, input);
                
                if (length == -1)
                    throw new IOException();
                
                posn += 1;
                length += 2;
                
                while (length-- > 0) {
                    output.set(output.get(-posn));
                    output.inc();
                }
            }
        }
        
        if (output.pos() > output.len())
            throw new IOException();
        
        /*
         * CRC unpacked data
         */

    }

    /*
     * Return the big-endian longword at p.
     */
    protected static int blong(CharPointer p) {
        int n;
        n = p.get(0);
        n = (n << 8) | p.get(1);
        n = (n << 8) | p.get(2);
        n = (n << 8) | p.get(3);
        return n;
    }
    
    /*
     * Return the little-endian word at p.
     */
    protected static int lword(CharPointer p) {
        int n;
        n = p.get(1);
        n = (n << 8) | p.get(0);
        return n;
    }

}

class BitStream {
    private int buffer;
    private int count;
    
    public BitStream(CharPointer p) {
        buffer = DeRNC.lword(p);
        count = 16;
    }
    
    /*
     * Returns some bits.
     */
    int bit_peek (int mask) {
        return buffer & mask;
    }
    
    /*
     * Advances the bit stream.
     */
    void bit_advance (int n, CharPointer p) {
        buffer >>>= n;
        count -= n;
        if(count < 16) {
            p.inc(2);
            buffer |= (DeRNC.lword(p) << count);
            count += 16;
        }
    }
    
    /*
     * Reads some bits in one go (ie the above two routines combined).
     */
    int bit_read (int mask, int n, CharPointer p) {
        int result = bit_peek(mask);
        bit_advance (n, p);
        return result;
    }
    
    /*
     * Fixes up a bit stream after literals have been read out of the
     * data stream.
     */
    void bitread_fix (CharPointer p) {
        count -= 16;
        buffer &= (1 << count) - 1; /* remove the top 16 bits */
        buffer |= (DeRNC.lword(p) << count);/* replace with what's at *p */
        count += 16;
    }
}

class Huftable {
    int num;
    long code[] = new long[32];
    int codelen[] = new int[32];
    int value[] = new int[32];
    
    void read(BitStream bis, CharPointer p) {
        int i, j, k, num;
        int leaflen[] = new int[32];
        int leafmax;
        long codeb;
        
        num = bis.bit_read(0x1f, 5, p);
        
        if(num < 1)
            return;
        
        leafmax = 1;
        
        for (i = 0; i < num; ++i) {
            leaflen[i] = bis.bit_read(0x0F, 4, p);
            
            if (leafmax < leaflen[i])
                leafmax = leaflen[i];
        }
        
        codeb = 0L;
        k = 0;
        
        for (i=1; i<=leafmax; i++) {
            for (j=0; j<num; j++) {
                if (leaflen[j] == i) {
                    code[k] = mirror (codeb, i);
                    codelen[k] = i;
                    value[k] = j;
                    codeb++;
                    k++;
                }
            }
            
            codeb <<= 1;
        }
        
        this.num = k;
    }
    
    /*
     * Read a value out of the bit stream using the given Huffman table.
     */
    int huf_read (BitStream bs, CharPointer p) {
        int i;
        int val;

        for (i = 0; i < this.num; i++) {
            int mask = (1 << this.codelen[i]) - 1;
            if (bs.bit_peek(mask) == this.code[i])
            break;
        }
        
        if (i == this.num)
            return -1;
        
        bs.bit_advance (this.codelen[i], p);

        val = this.value[i];

        if (val >= 2) {
            val = 1 << (val-1);
            val |= bs.bit_read (val-1, this.value[i] - 1, p);
        }
        return val;
    }
    
    long mirror(long x, int n) {
        long top = 1 << (n-1), bottom = 1;
        while (top > bottom) {
        long mask = top | bottom;
        long masked = x & mask;
        if (masked != 0 && masked != mask)
            x ^= mask;
        top >>>= 1;
        bottom <<= 1;
        }
        return x;
    }
}

class CharPointer {
    private byte[] data;
    private int pos;
    
    CharPointer(byte[] data) {
        this.data = data;
        this.pos = 0;
    }
    
    CharPointer(byte[] data, int pos) {
        this.data = data;
        this.pos = pos;
    }
    
    CharPointer(CharPointer c) {
        this.data = c.data;
        this.pos = c.pos;
    }
    
    CharPointer(CharPointer c, int offset) {
        this.data = c.data;
        this.pos = c.pos + offset;
    }
    
    void set(int b) {
        data[pos] = (byte)(0xff & b);
    }
    
    void set(int b, int offset) {
        data[pos + offset] = (byte)(0xff & b);
    }
    
    int get() {
        return (data[pos] & 0xff);
    }
    
    int get(int offset) {
        // Hack!!
        if(pos + offset >= data.length)
            return 0;
        else
            return (data[pos + offset] & 0xff);
    }
    
    void inc() {
        pos++;
    }
    
    void inc(int offset) {
        pos += offset;
    }
    
    int pos() {
        return pos;
    }
    
    int len() {
        return data.length;
    }
}

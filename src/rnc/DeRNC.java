/*
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the author nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package rnc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeRNC {
    
    /*public static void main( String args ) {
        
    }
    
    public static void main_unpack( File inFile, File outFile ) throws FileNotFoundException, IOException {
        FileInputStream ir = new FileInputStream( inFile );
        
        if(ir.read() != 'R' || ir.read() != 'N' || ir.read() != 'C') {
            ir.close();
            return;
        }
            
        ir.getChannel().position(0);
        
        byte packed[] = new byte[ (int)inFile.length() ];
        ir.read( packed );
        ir.close();
        
        
    }
    
    private boolean check_signature(byte[] packed) {
        return (packed[0] == 'R' && packed[1] == 'N' && packed[2] == 'C' && packed[3] == 0x01);
    }
    
    public static int rnc_ulen( byte[] packed ) throws IllegalArgumentException {
        
    }*/
}

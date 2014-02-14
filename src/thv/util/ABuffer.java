package thv.util;

import java.io.IOException;
import java.io.InputStream;

public abstract class ABuffer {
	abstract public int getSize() throws IOException;
	abstract public int get(int position) throws IOException;
	abstract public InputStream createInputStream() throws IOException;
	
    public int getSwappedInteger(int position)
    throws IOException {
        int n;
        
        n = get(position);
        n |= get(position + 1) << 8;
        n |= get(position + 2) << 16;
        n |= get(position + 3) << 24;
        
        return n;
    }
    
    public short getSwappedShort(int position)
    throws IOException {
        short n;
        
        n = (byte)get(position);
        n |= get(position + 1) << 8;
        
        return n;
    }
}

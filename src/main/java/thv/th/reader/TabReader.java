/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.reader;

import java.io.IOException;
import java.util.ArrayList;

import thv.th.data.TabEntry;
import thv.util.ABuffer;

public class TabReader {
    public static TabEntry readByPosition(ABuffer buffer, int position, int consecNumber)
    throws IOException {
        //buffer.seek(position);
        
        TabEntry e = new TabEntry(position, 0, 0, 0, consecNumber);
            
        e.setChunksPos( buffer.getSwappedInteger(position) );
        e.setWidth( buffer.get(position+4) );
        e.setHeight( buffer.get(position+5) );
        
        return e;
    }
    
    public static ArrayList<TabEntry> readAll(ABuffer is) throws IOException {
        ArrayList<TabEntry> res = new ArrayList<TabEntry>();
        int pos = 0;
        
        int consecNumber = 0;
        
        while(pos < is.getSize()) {
            TabEntry t = TabReader.readByPosition(is, pos, consecNumber);
            pos += 6;
            if(t.getWidth() == 0 || t.getHeight() == 0) {
                continue;
            } else {
                res.add(t);
                ++consecNumber;
            }
        }
        
        return res;
    }
}

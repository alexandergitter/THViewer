/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.reader;

import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.EndianUtils;

import thv.th.data.TabEntry;
import thv.util.ABuffer;

public class TabReader {
    public static TabEntry readByPosition(ABuffer is, int position, int consecNumber) throws IOException {
        is.seek(position);
        
        TabEntry e = new TabEntry(position, 0, 0, 0, consecNumber);
            
        e.setChunksPos( EndianUtils.readSwappedInteger(is) );
        e.setWidth( is.read() );
        e.setHeight( is.read() );
        
        return e;
    }
    
    public static Vector<TabEntry> readAll(ABuffer is) throws IOException {
        Vector<TabEntry> res = new Vector<TabEntry>();
        int pos = 0;
        
        int consecNumber = 0;
        
        while(is.available() > 0) {
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

/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.EndianUtils;

import thv.th.data.SpriteElement;

public class SpriteElementReader {
    public static SpriteElement readByIndex(FileInputStream is, int index) throws IOException {
        is.getChannel().position(index * 6);
        
        int tabPos = EndianUtils.readSwappedShort(is);
        int offsetx = is.read();
        int offsety = is.read();
        int layerClass = is.read();
        byte flags = (byte) (0xf & layerClass);
        layerClass = (layerClass & 0xf0) >>> 4;
        int id = is.read();
        
        return new SpriteElement(index * 6, tabPos, offsetx, offsety, flags, id, layerClass);
    }
    
    public static Vector<SpriteElement> readAll(FileInputStream is) throws IOException {
        Vector<SpriteElement> res = new Vector<SpriteElement>();
        
        int index = 0;
        
        while(is.available() > 0) {
            res.add(readByIndex(is, index));
        }
        
        return res;
    }
    
}

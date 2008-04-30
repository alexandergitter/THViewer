/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package th.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;
import org.apache.commons.io.EndianUtils;
import th.data.SpriteElement;
import th.data.THFrame;
import th.data.THPalette;

public class FramesReader {

    public static THFrame readByIndex(int frameIndex, FileInputStream frameStream, FileInputStream listStream, FileInputStream elementStream, File tabFile, File chunksFile, THPalette palette) throws IOException {
        frameStream.getChannel().position(frameIndex * 10);
        
        int listIndex = EndianUtils.readSwappedInteger(frameStream) * 2;
        int width = frameStream.read();
        int height = frameStream.read();
        
        if(width == 0  || height == 0)
            return null;
        
        frameStream.skip(1);
        int flags = frameStream.read();
        int nextFrame = EndianUtils.readSwappedShort(frameStream);
        
        THFrame res = new THFrame(frameIndex, width, height, flags, nextFrame, chunksFile, tabFile, palette);
        
        Vector<Integer> elementList = new Vector<Integer>();
        
        listStream.getChannel().position(listIndex);
        
        while(true) {
            int elementIndex = EndianUtils.readSwappedShort(listStream);
            
            if(elementIndex == -1)
                break;
            
            SpriteElement element = SpriteElementReader.readByIndex(elementStream, elementIndex);
            res.addElement(element);
        }
        
        return res;
    }
    
}

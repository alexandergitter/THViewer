/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.EndianUtils;

import thv.th.data.SpriteElement;
import thv.th.data.THFrame;
import thv.th.data.THPalette;

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
        
        ArrayList<Integer> elementList = new ArrayList<Integer>();
        
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

/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.reader;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import thv.th.data.THPalette;

public class RawReader {
    
    /** Creates a new instance of RawReader */
    public static Image read(FileInputStream rawStream, THPalette palette, int width, int height) throws IOException {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        rawStream.getChannel().position(0);
                  
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                int a = rawStream.read() & 0xff;
                bi.setRGB(x, y, palette.get(a).getRGB());
            }
        }
        
        return bi;
    }
}

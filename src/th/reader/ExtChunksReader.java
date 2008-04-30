/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package th.reader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import th.data.THPalette;
import th.data.TabEntry;

public class ExtChunksReader {
    public static Image readByEntry(FileInputStream is, TabEntry en, THPalette palette, Color background) throws IOException {
        BufferedImage bi = new BufferedImage(en.getWidth(), en.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.setColor(background);
        g.fillRect(0, 0, en.getWidth(), en.getHeight());

        is.getChannel().position(en.getChunksPos());

        for(int y = 0; y < en.getHeight(); ++y) {
            int x = 0;

            while(true) {
                int val = is.read();

                if(val == 0) {  // end of row
                    break;
                } else if(val <= 63) {
                    for(int i = 0; i < val; ++i) {
                        int palindex = is.read();
                        bi.setRGB(x, y, palette.elementAt(palindex).getRGB());
                        ++x;
                    }
                } else if(val <= 127) {
                    int palindex = is.read();

                    for(int i = 0; i < (val-60); ++i) {
                        bi.setRGB(x, y, palette.elementAt(palindex).getRGB());
                        ++x;
                    }
                } else if(val <= 191) {
                    for(int i = 0; i < (val - 128); ++i) {
                        ++x;
                    }
                } else if(val <= 254) {
                    int palindex = is.read();

                    for(int i = 0; i < (val-124); ++i) {
                        bi.setRGB(x, y, palette.elementAt(palindex).getRGB());
                        ++x;
                    }
                } else {
                    int count = is.read();
                    int palindex = is.read();

                    for(int i = 0; i < count; ++i) {
                        bi.setRGB(x, y, palette.elementAt(palindex).getRGB());
                        ++x;
                    }
                }
            }
        }
       
        return bi;
    }
    
}

/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.reader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import thv.th.data.SpriteElement;
import thv.th.data.THPalette;
import thv.th.data.TabEntry;
import thv.util.ABuffer;

public class ChunksReader {
    private static HashMap<Integer, BufferedImage> cache = new HashMap<Integer, BufferedImage>();
    
    public static ArrayList<BufferedImage> readAll(FileInputStream chunkStream, ABuffer tabStream, THPalette palette, Color background) throws IOException {
        ArrayList<TabEntry> entries = TabReader.readAll(tabStream);
        
        ArrayList<BufferedImage> res = new ArrayList<BufferedImage>();
        
        for(TabEntry en: entries) {
            res.add(readByEntry(chunkStream, en, palette, background, (byte)0));
        }
        
        return res;
    }
    
    public static Image readByEntry(FileInputStream is, TabEntry en, THPalette palette, Color background) throws IOException {
        return readByEntry(is, en, palette, background, (byte)0);
    }
    
    public static BufferedImage readByEntry(FileInputStream is, TabEntry en, THPalette palette, Color background, byte flags) throws IOException {
        if(en.getHeight() <= 0 || en.getWidth() <= 0)
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        else if(cache.containsKey(en.getChunksPos() ^ flags))
            return cache.get(en.getChunksPos() ^ flags);
        
        BufferedImage bi = new BufferedImage(en.getWidth(), en.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.setColor(background);
        g.fillRect(0, 0, en.getWidth(), en.getHeight());

        is.getChannel().position(en.getChunksPos());

        WritableRaster raster = bi.getAlphaRaster();
        
        for(int y = 0; y < en.getHeight(); ++y) {
            int x = 0;

            while(true) { // loop until end of row
                byte val = (byte)is.read();

                if(val == 0) {  // end of row
                    break;
                } else if(val > 0) {
                    for(int i = 0; i < val; ++i) {
                        int palindex = is.read();
                        Color color = palette.get(palindex);
                        try {
                            int mx = x;
                            int my = y;
                            
                            if((flags & SpriteElement.FLIP_VERTICAL) != 0) {
                                mx = en.getWidth() - x - 1;
                            }
                            
                            if((flags & SpriteElement.FLIP_HORIZONTAL) != 0) {
                                my = en.getHeight() - y - 1;
                            }
 
                            bi.setRGB(mx, my, palette.get(palindex).getRGB());
                            if((flags & SpriteElement.ALPHA_50) != 0) {
                                raster.setPixel(mx, my, new int[]{128});
                            } else if((flags & SpriteElement.ALPHA_75) != 0) {
                                raster.setPixel(mx, my, new int[]{191});
                            }
                        } catch(ArrayIndexOutOfBoundsException e) {
                            System.err.println(e);
                            System.err.println("TabPos: " + Integer.toHexString(en.getTabPos()) + ", ChunksPos: " + Integer.toHexString(en.getChunksPos()));
                            break;
                        }
                        ++x;
                    }
                } else if(val < 0) {
                    for(int i = 0; i < -val; ++i) {
                        ++x;
                    }
                }
            }
        }

        cache.put(en.getChunksPos() ^ flags, bi);
        
        return bi;
    }
}
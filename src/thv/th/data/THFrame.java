/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.data;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import thv.th.reader.ChunksReader;
import thv.th.reader.TabReader;
import thv.util.ABuffer;
import thv.util.FileBuffer;

public class THFrame {
    
    private Vector<SpriteElement> elements;
    private int index;
    private int width;
    private int height;
    private int next;
    private int flags;
    
    private File chunksFile;
    private File tabFile;
    private THPalette palette;
    
    /** Creates a new instance of Frame */
    public THFrame(int index, int width, int height, int flags, int next, File chunksFile, File tabFile, THPalette palette) {
        this.index = index;
        this.width = width;
        this.height = height;
        this.flags = flags;
        this.next = next;
        this.chunksFile = chunksFile;
        this.tabFile = tabFile;
        this.palette = palette;
        elements = new Vector<SpriteElement>();
    }
    
    public int getIndex() {
        return index;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getFlags() {
        return flags;
    }
    
    public int getYOverrun(int height, int minYOffset, ABuffer tabStream) throws IOException {
        int maxHeight = 0;
        
        for(SpriteElement element: elements) {
            int ypos = element.getOffsety() - minYOffset;
            TabEntry en = TabReader.readByPosition(tabStream, element.getTabPos(), 0);
            
            if((ypos + en.getHeight()) > maxHeight) {
                maxHeight = ypos + en.getHeight();
            }
        }
        
        return maxHeight - height;
    }
    
    public int getYUnderrun(int height, int minYOffset, ABuffer tabStream) throws IOException {
        int underrun = 0;
        
        for(SpriteElement element: elements) {
            int ypos = element.getOffsety() - minYOffset;
            TabEntry en = TabReader.readByPosition(tabStream, element.getTabPos(), 0);
            
            if(ypos < underrun) {
                underrun = ypos;
            }
        }
        
        return -underrun;
    }
    
    public int getXOverrun(int width, int maxXOffset, ABuffer tabStream) throws IOException {
        int maxWidth = 0;
        
        for(SpriteElement element: elements) {
            int xpos = width - maxXOffset + element.getOffsetx();
            TabEntry en = TabReader.readByPosition(tabStream, element.getTabPos(), 0);
            
            if((xpos + en.getWidth()) > maxWidth) {
                maxWidth = xpos + en.getWidth();
            }
        }
        
        return maxWidth - width;
    }
    
    public int getXUnderrun(int width, int maxXOffset, ABuffer tabStream) throws IOException {
        int underrun = 0;
        
        for(SpriteElement element: elements) {
            int xpos = width - maxXOffset + element.getOffsetx();
            TabEntry en = TabReader.readByPosition(tabStream, element.getTabPos(), 0);
            
            if(xpos < underrun) {
                underrun = xpos;
            }
        }
        
        return -underrun;
    }
    
    public int getNext() {
        return next;
    }
    
    public Vector<SpriteElement> getElements() {
        return elements;
    }
    
    public void addElement(SpriteElement element) {
        elements.add(element);
    }
    
    public int getMaxXOffset(ABuffer tabStream) throws IOException {
        int maxXOffset = 0;
        
        for(SpriteElement element: elements) {
            TabEntry en = TabReader.readByPosition(tabStream, element.getTabPos(), 0);
            
            if((element.getOffsetx() + en.getWidth()) > maxXOffset) {
                maxXOffset = element.getOffsetx() + en.getWidth();
            }
        }
        
        return maxXOffset;
    }
    
    public int getMinYOffset(ABuffer tabStream) throws IOException {
        int minYOffset = 256;
        
        for(SpriteElement element: elements) {
            TabEntry en = TabReader.readByPosition(tabStream, element.getTabPos(), 0);
            
            if(element.getOffsety() < minYOffset)
                minYOffset = element.getOffsety();
        }
        
        return minYOffset;
    }
    
    public Image render(ABuffer tabStream, FileInputStream chunksStream, THPalette palette, int id) throws IOException {
        int maxXOffset = getMaxXOffset(tabStream);
        int minYOffset = getMinYOffset(tabStream);
                
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();        
        
        for(SpriteElement element: elements) {
            if(element.getId() != 0 && element.getId() != id)
                continue;
            
            TabEntry en = TabReader.readByPosition(tabStream, element.getTabPos(), 0);
            Image img = ChunksReader.readByEntry(chunksStream, en, palette, new Color(0, 0, 0, 0), element.getFlags());
            
            int x = width - maxXOffset + element.getOffsetx();
            
            int y = element.getOffsety() - minYOffset;
            
            g.drawImage(img, x, y, null);
        }
        
        return bi;
    }
    
    public void renderInMap(Tile tile, Graphics g, int x, int y, boolean mouseOver) throws IOException {
        ObjectInfo oi = tile.getObjectInfo();
        ABuffer tabStream = new FileBuffer(tabFile);
        FileInputStream chunksStream = new FileInputStream(chunksFile);
        
        for(SpriteElement element: elements) {
            if(element.getId() > 1 && oi.getLayerId(element.getLayerClass()) != element.getId())
                continue;
            
            TabEntry en = TabReader.readByPosition(tabStream, element.getTabPos(), 0);
            Image img = ChunksReader.readByEntry(chunksStream, en, palette, new Color(0, 0, 0, 0), element.getFlags());

            if(mouseOver)
                g.drawImage(RedFilter.createRedImage(img), x + element.getOffsetx(), y + element.getOffsety(), null);
            else
                g.drawImage(img, x + element.getOffsetx(), y + element.getOffsety(), null);
        }
        
        tabStream.close();
        chunksStream.close();
    }
    
    public Image renderInAnim(ABuffer tabStream, FileInputStream chunksStream, THPalette palette, int id, int width, int height, int maxXOffset, int minYOffset, int xunderrun, int yunderrun) throws IOException {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();        
        
        for(SpriteElement element: elements) {
            if(element.getId() > 1 && element.getId() != id)
                continue;
            
            TabEntry en = TabReader.readByPosition(tabStream, element.getTabPos(), 0);
            Image img = ChunksReader.readByEntry(chunksStream, en, palette, new Color(0, 0, 0, 0), element.getFlags());
            
            int x = width - maxXOffset + element.getOffsetx();
            
            int y = element.getOffsety() - minYOffset + yunderrun;
            
            g.drawImage(img, x, y, null);
        }
        
        return bi;
    }
}

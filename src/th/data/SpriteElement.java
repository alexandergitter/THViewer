/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package th.data;

import java.io.IOException;
import java.io.RandomAccessFile;
import javax.swing.JOptionPane;
import th.Main;

public class SpriteElement {
    private int elementPos;
    private int tabPos;
    private int offsetx;
    private int offsety;
    private byte flags;
    private int id;
    private int layerClass;
    
    public static final byte FLIP_VERTICAL = 0x1;
    public static final byte FLIP_HORIZONTAL = 0x2;
    public static final byte ALPHA_50 = 0x4;
    public static final byte ALPHA_75 = 0x8;
    
    public SpriteElement(int elementPos, int tabPos, int offsetx, int offsety, byte flags, int id, int layerClass) {
        this.elementPos = elementPos;
        this.tabPos = tabPos;
        this.offsetx = offsetx;
        this.offsety = offsety;
        this.flags = flags;
        this.id = id;
        this.layerClass = layerClass;
    }

    public void modify(RandomAccessFile f) throws IOException {
        try {
        String s = JOptionPane.showInputDialog("Spritetab position", tabPos);
        short newTabPos = (short)Main.parseInt(s);
        
        s = JOptionPane.showInputDialog("offset x", offsetx);
        byte newOffsetX = (byte)Main.parseInt(s);
        
        s = JOptionPane.showInputDialog("offset y", offsety);
        byte newOffsetY = (byte)Main.parseInt(s);
        
        s = JOptionPane.showInputDialog("layerclass/flags", (flags | (layerClass << 4)));
        byte newFlags = (byte)Main.parseInt(s);
        
        s = JOptionPane.showInputDialog("layer id", id);
        byte newId = (byte)Main.parseInt(s);
        
        f.seek(elementPos);
        f.write(newTabPos & 0xff);
        f.write((newTabPos >>> 8) & 0xff);
        f.write(newOffsetX);
        f.write(newOffsetY);
        f.write(newFlags);
        f.write(newId);
        
        } catch(NumberFormatException e) {
            System.out.println(e);
        }
    }
    
    public int getLayerClass() {
        return this.layerClass;
    }
    
    public int getElementPos() {
        return elementPos;
    }
        
    public int getTabPos() {
        return tabPos;
    }

    public int getOffsetx() {
        return offsetx;
    }

    public int getOffsety() {
        return offsety;
    }

    public byte getFlags() {
        return flags;
    }

    public int getId() {
        return id;
    }
}
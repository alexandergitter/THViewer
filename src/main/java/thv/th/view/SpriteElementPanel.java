/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import thv.th.data.SpriteElement;
import thv.th.data.TabEntry;
import thv.th.reader.ChunksReader;
import thv.th.reader.SpriteElementReader;
import thv.th.reader.TabReader;
import thv.util.ABuffer;
import thv.util.FileBuffer;

public final class SpriteElementPanel
extends CommonImagePanel {

    protected File spriteElementFile;
    protected ArrayList<SpriteElement> elements;    
    
    public SpriteElementPanel(File paletteFile, File chunksFile, File tabFile, File spriteElementFile) {
        super(paletteFile, chunksFile, tabFile);
        this.spriteElementFile = spriteElementFile;
        
        init();
    }
    
    protected void loadElements(int start) throws IOException {
        if(elements == null)
            elements = new ArrayList<SpriteElement>();
        
        elements.clear();
        
        int elementCount = (int)(spriteElementFile.length() / 6);
        
        FileInputStream is = new FileInputStream(spriteElementFile);
        
        for(int i = 0; i < perPage; ++i) {
            if((i + start) >= elementCount)
                break;
            
            elements.add(SpriteElementReader.readByIndex(is, i+start));
        }
        
        is.close();
        
        selectPanel.setSelectionCount((elementCount / perPage) + 1);
    }
    
    protected void show(int start) throws IOException {
        loadPalette();
        loadElements(start);
        panel.removeAll();
        
        ABuffer tabStream = new FileBuffer(tabFile);
        FileInputStream is = new FileInputStream(chunksFile);
        
        for(int i = 0; i < perPage; ++i) {
            if(i >= elements.size())
                break;
            
            SpriteElement el = elements.get(i);
            TabEntry en = TabReader.readByPosition(tabStream, el.getTabPos(), 0);
            Image img = ChunksReader.readByEntry(is, en, palette, new Color(0, 0, 0, 0), el.getFlags());
            JLabel l = new MyLabel(el, img, spriteElementFile);
            l.setBorder(BorderFactory.createLineBorder(Color.RED));
            panel.add(l);
            
            StringBuilder sb = new StringBuilder();
            sb.append("<html>position: " + el.getElementPos());
            sb.append("<br>offsetx: " + el.getOffsetx());
            sb.append("<br>offsety: " + el.getOffsety());
            sb.append("<br>layerid: " + el.getId());
            sb.append("<br>Layerclass/Flags: 0x" + Integer.toHexString(el.getFlags() | (el.getLayerClass() << 4)));
            l.setToolTipText(sb.toString());
        }

        is.close();
        panel.revalidate();
        panel.repaint();
    }
}

class MyLabel extends JLabel implements MouseListener {
    private SpriteElement el;
    private File file;
    
    public MyLabel(SpriteElement el, Image img, File file) {
        super(new ImageIcon(img));
        this.el = el;
        this.file = file;
        this.addMouseListener(this);
    }
    
    public void mouseClicked (MouseEvent me) {
        try {
        if(me.getButton() == MouseEvent.BUTTON3) {
            RandomAccessFile f = new RandomAccessFile(file, "rw");
            el.modify(f);
            f.close();
        }
        } catch(Exception e) {}
    }
    
    public void mouseEntered (MouseEvent me) {}
    public void mousePressed (MouseEvent me) {}
    public void mouseReleased (MouseEvent me) {} 
    public void mouseExited (MouseEvent me) {}  
}
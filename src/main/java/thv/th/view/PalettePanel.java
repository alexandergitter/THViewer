/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import thv.th.data.THPalette;
import thv.th.reader.PaletteReader;

public class PalettePanel
extends DividePanel {
    
    protected File paletteFile;
    
    public PalettePanel(File paletteFile) {
        this.paletteFile = paletteFile;
        perPage = 300;

        init();
        selectPanel.setSelectionCount(1);
    }
    
    protected void show(int start) throws IOException {
        FileInputStream paletteStream = new FileInputStream(paletteFile);
        THPalette pal = PaletteReader.readAll(paletteStream);
        paletteStream.close();
        
        panel.removeAll();
        
        int size = 20;
        
        for(int i = 0; i < perPage; ++i) {
            if(start + i >= pal.size())
                break;
            
            BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics g = img.getGraphics();
            g.setColor(pal.get(i));
            g.fillRect(0, 0, size, size);
            
            
            JLabel l = new JLabel(new ImageIcon(img));
            l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            l.setToolTipText("<html><b>index:</b> " + i + " (0x" + Integer.toHexString(i) + ")");
            panel.add(l);
        }
        
        panel.revalidate();
        panel.repaint();
    }
}

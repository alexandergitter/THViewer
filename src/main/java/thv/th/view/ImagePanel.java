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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import thv.th.data.TabEntry;
import thv.th.reader.ChunksReader;
import thv.th.reader.ExtChunksReader;

@SuppressWarnings("serial")
public final class ImagePanel
extends CommonImagePanel {
    boolean extendedChunks = false;
    
    /** Creates a new instance of ImagePanel */
    public ImagePanel(File paletteFile, File chunksFile, File tabFile) {
        super(paletteFile, chunksFile, tabFile);

        init();
    }
    
    public ImagePanel(File paletteFile, File chunksFile, File tabFile, boolean extendedChunks) {
        super(paletteFile, chunksFile, tabFile);

        this.extendedChunks = extendedChunks;
        
        init();
    }
    
    protected void show(int start) throws IOException {
        loadEntries();
        loadPalette();
        
        panel.removeAll();
        
        FileInputStream is = new FileInputStream(chunksFile);
        
        for(int i = 0; i < perPage; ++i) {
            if(start + i >= entries.size())
                break;
            
            TabEntry en = entries.elementAt(i+start);
            Image img;
            
            if(extendedChunks)
                img = ExtChunksReader.readByEntry(is, en, palette, new Color(0, 0, 0, 0));
            else
                img = ChunksReader.readByEntry(is, en, palette, new Color(0, 0, 0, 0));
            
            JLabel l = new JLabel(new ImageIcon(img));
            l.setToolTipText("<html><b>index:</b> " + en.getTabIndex() + " (hex: " + Integer.toHexString(en.getTabIndex()) + ")<br><b>tabPos:</b> " + en.getTabPos() + "<br><b>chunkPos:</b> " + en.getChunksPos() + "<br><b>width:</b> " + en.getWidth() + "<br><b>height:</b> " + en.getHeight());
            panel.add(l);
        }
        
        is.close();
        panel.revalidate();
        panel.repaint();
    }
}

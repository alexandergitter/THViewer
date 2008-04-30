/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package th.view;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import th.data.THPalette;
import th.data.TabEntry;
import th.reader.ChunksReader;
import th.reader.PaletteReader;
import th.reader.TabReader;

public abstract class CommonImagePanel extends DividePanel {
    protected File paletteFile;
    protected File chunksFile;
    protected File tabFile;
    
    protected Vector<TabEntry> entries = null;
    protected THPalette palette = null;
    
    /** Creates a new instance of ImagePanel */
    public CommonImagePanel(File paletteFile, File chunksFile, File tabFile) {
        this.paletteFile = paletteFile;
        this.chunksFile = chunksFile;
        this.tabFile = tabFile;
    }
    
    protected void loadEntries() throws IOException {
        if(entries != null)
            return;
        
        FileInputStream is = new FileInputStream(tabFile);
        entries = TabReader.readAll(is);
        is.close();
        
        selectPanel.setSelectionCount((entries.size() / perPage) + 1);
    }
    
    protected void loadPalette() throws IOException {
        if(palette != null)
            return;
        
        FileInputStream is = new FileInputStream(paletteFile);
        palette = PaletteReader.readAll(is);
        is.close();
    }
    
    protected abstract void show(int start) throws IOException;
}

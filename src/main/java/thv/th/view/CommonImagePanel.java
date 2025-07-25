/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import thv.th.data.THPalette;
import thv.th.data.TabEntry;
import thv.th.reader.PaletteReader;
import thv.th.reader.TabReader;
import thv.util.ABuffer;
import thv.util.FileBuffer;

public abstract class CommonImagePanel
extends DividePanel {
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
        
        ABuffer is = new FileBuffer(tabFile);
        entries = TabReader.readAll(is);
        //is.close();
        
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

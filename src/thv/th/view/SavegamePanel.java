/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import thv.th.data.THMap;
import thv.th.data.THPalette;
import thv.th.reader.PaletteReader;
import thv.th.reader.SavegameReader;
import thv.util.ABuffer;
import thv.util.FileBuffer;

public class SavegamePanel extends JPanel {
    private JScrollPane scrollPane;
    private THMap mapComponent;
    
    /** Creates a new instance of MapPanel */
    public SavegamePanel(File frameFile, File frameListFile, File frameElementFile, File frameTabFile, File frameChunksFile, File framePaletteFile, File saveFile, File blockTabFile, File blockChunksFile, File blockPaletteFile) {
        this.setLayout(new BorderLayout());
        
        try {
            FileInputStream paletteStream = new FileInputStream(blockPaletteFile);
            FileInputStream framePaletteStream = new FileInputStream(framePaletteFile);
        
            THPalette blockPalette = PaletteReader.readAll(paletteStream);
            THPalette framePalette = PaletteReader.readAll(framePaletteStream);
        
            paletteStream.close();
            framePaletteStream.close();
            
            FileInputStream frameElementStream = new FileInputStream(frameElementFile);
            FileInputStream frameStream = new FileInputStream(frameFile);
            FileInputStream frameListStream = new FileInputStream(frameListFile);
            FileInputStream saveStream = new FileInputStream(saveFile);
            ABuffer blockTabStream = new FileBuffer(blockTabFile);
            FileInputStream blockChunksStream = new FileInputStream(blockChunksFile);
        
            mapComponent = SavegameReader.read(frameStream, frameListStream, frameElementStream, frameTabFile, frameChunksFile, framePalette, saveStream, blockTabStream, blockChunksStream, blockPalette, new Color(0, 0, 0, 0));
        
            frameStream.close();
            frameListStream.close();
            frameElementStream.close();
            saveStream.close();
            blockTabStream.close();
            blockChunksStream.close();
        
            scrollPane = new JScrollPane(mapComponent, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            this.add(scrollPane, BorderLayout.CENTER);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
}

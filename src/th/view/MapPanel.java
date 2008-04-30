/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package th.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import th.data.THPalette;
import th.data.THMap;
import th.reader.LevelReader;
import th.reader.PaletteReader;

public class MapPanel extends JPanel {
    private JScrollPane scrollPane;
    private THMap mapComponent;
    
    /** Creates a new instance of MapPanel */
    public MapPanel(File mapFile, File tabFile, File chunksFile, File paletteFile) {
        this.setLayout(new BorderLayout());
        
        try {
            FileInputStream mapStream = new FileInputStream(mapFile);
            FileInputStream tabStream = new FileInputStream(tabFile);
            FileInputStream chunksStream = new FileInputStream(chunksFile);
            FileInputStream paletteStream = new FileInputStream(paletteFile);
        
            THPalette palette = PaletteReader.readAll(paletteStream);
        
            paletteStream.close();
        
            mapComponent = LevelReader.read(mapStream, tabStream, chunksStream, palette, new Color(0, 0, 0, 0));
        
            mapStream.close();
            tabStream.close();
            chunksStream.close();
        
            scrollPane = new JScrollPane(mapComponent, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            this.add(scrollPane, BorderLayout.CENTER);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
}

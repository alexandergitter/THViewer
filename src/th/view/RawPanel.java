/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package th.view;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import th.data.THPalette;
import th.reader.PaletteReader;
import th.reader.RawReader;

public class RawPanel extends JPanel {
    
    private JScrollPane scrollPane;
    
    /** Creates a new instance of RawPanel */
    public RawPanel(File paletteFile, int width, int height, File rawFile) {
        this.setLayout(new BorderLayout());
        
        try {
            FileInputStream paletteStream = new FileInputStream(paletteFile);
            FileInputStream rawStream = new FileInputStream(rawFile);
        
            THPalette palette = PaletteReader.readAll(paletteStream);
        
            paletteStream.close();
        
            Image img = RawReader.read(rawStream, palette, width, height);
        
            rawStream.close();
        
            scrollPane = new JScrollPane(new JLabel(new ImageIcon(img)));
            this.add(scrollPane, BorderLayout.CENTER);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
}

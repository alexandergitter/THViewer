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
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import th.FlowScrollLayout;
import th.data.THPalette;
import th.data.TabEntry;
import th.reader.ChunksReader;
import th.reader.PaletteReader;
import th.reader.TabReader;

public abstract class DividePanel extends JPanel implements SwitchListener{
    protected SelectPanel selectPanel;
    private JScrollPane scrollPane;
    protected JPanel panel;
    protected int perPage = 50;
    
    protected void init() {
        this.setLayout(new BorderLayout());
        selectPanel = new SelectPanel();
        selectPanel.setSelectionCount(70);
        selectPanel.setCurrentSelection(1);
        selectPanel.addSwitchListener(this);
        this.add(selectPanel, BorderLayout.NORTH);
        
        panel = new JPanel();
        scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        FlowLayout fl = new FlowScrollLayout(scrollPane);
        fl.setAlignment(FlowLayout.LEFT);
        panel.setLayout(fl);
        
        this.add(scrollPane, BorderLayout.CENTER);
        try {
        show(0);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    protected abstract void show(int start) throws IOException;

    public void userSwitch( int action, int from ) {
        try {
        if(action == SwitchListener.NEXT) {
            show(perPage * from);
            selectPanel.setCurrentSelection(from+1);
        } else if(action == SwitchListener.PREVIOUS) {
            show(perPage * (from - 2));
            selectPanel.setCurrentSelection(from-1);
        }
        } catch(Exception e) {
            System.err.println(e);
        }
    }    
}

/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import thv.th.data.THMap;

@SuppressWarnings("serial")
public class MapPanel
extends JPanel {
    private JScrollPane scrollPane;
    private THMap mapComponent;
    
    /** Creates a new instance of MapPanel */
    public MapPanel(THMap mapComponent) {
        this.setLayout(new BorderLayout());
        this.mapComponent = mapComponent;
        
        scrollPane = new JScrollPane(mapComponent, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane, BorderLayout.CENTER);
    }
    
}

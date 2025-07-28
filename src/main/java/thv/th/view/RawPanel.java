/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class RawPanel
extends JPanel {
    
    private JScrollPane scrollPane;
    
    public RawPanel(Image img) {
        this.setLayout(new BorderLayout());
        
        scrollPane = new JScrollPane(new JLabel(new ImageIcon(img)));
        this.add(scrollPane, BorderLayout.CENTER);
    }
}

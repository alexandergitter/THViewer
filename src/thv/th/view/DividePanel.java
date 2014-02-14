/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import thv.th.FlowScrollLayout;

public abstract class DividePanel
extends JPanel
implements ISwitchListener {
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
        if(action == ISwitchListener.NEXT) {
            show(perPage * from);
            selectPanel.setCurrentSelection(from+1);
        } else if(action == ISwitchListener.PREVIOUS) {
            show(perPage * (from - 2));
            selectPanel.setCurrentSelection(from-1);
        }
        } catch(Exception e) {
            System.err.println(e);
        }
    }    
}

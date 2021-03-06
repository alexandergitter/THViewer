/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class StringsPanel  extends JPanel {
    
    private JScrollPane scrollPane;
    
    /** Creates a new instance of StringsPanel */
    //public StringsPanel(File stringsFile) {
    public StringsPanel(Vector<Vector<String>> sections) {
        this.setLayout(new BorderLayout());
            
        StringBuilder sb = new StringBuilder();
            
        int k = 0;
        for(Vector<String> v: sections) {
            sb.append("======= Section " + k + " =======\n");
            int i = 0;
            for(String s: v) {
                sb.append(String.format("%d  %s%n", i, s));
                ++i;
            }
            ++k;
        }
    
        JTextArea tf = new JTextArea();
        tf.setText(sb.toString());
        
        scrollPane = new JScrollPane(tf);
        this.add(scrollPane, BorderLayout.CENTER);
    }
    
}

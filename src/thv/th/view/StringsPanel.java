/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import thv.th.reader.LangReader;

public class StringsPanel  extends JPanel {
    
    private JScrollPane scrollPane;
    
    /** Creates a new instance of StringsPanel */
    public StringsPanel(File stringsFile) {
        this.setLayout(new BorderLayout());
        
        try {
            FileInputStream stringsStream = new FileInputStream(stringsFile);
            Vector<Vector<String>> sections = LangReader.read(stringsStream);
            stringsStream.close();
            
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
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
}

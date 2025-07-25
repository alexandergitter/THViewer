/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th;

import javax.swing.JFrame;

public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        MainWindow frame;
    
        if(args.length == 0) {
            frame = new MainWindow(null);
        } else {
            frame = new MainWindow(args[0]);
        }
            
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
    
    public static int parseInt(String s) {
        if(s.startsWith("0x")) {
            return Integer.parseInt(s.substring(2), 16);
        } else {
            return Integer.parseInt(s);
        }
    }    
}

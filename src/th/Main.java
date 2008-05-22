/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package th;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import th.reader.SmartReader;

public class Main extends JFrame {
    
    private static Main instance;
    
    JMenuBar menuBar;
    JMenu fileMenu;
    Color imageBackground = Color.WHITE;
    JPanel panel;
    StatusBar statusBar;
    SmartReader smartReader;
    
    public static Main getInstance() {
        return getInstance(null);
    }
    
    public static Main getInstance(String thDir) {
        if(instance == null)
            instance = new Main(thDir);
        
        return instance;
    }
    
    /** Creates a new instance of Main */
    private Main(String thDir) {
        super("TH Viewer");
        
        this.getContentPane().setLayout(new BorderLayout());
        panel = new JPanel();
        
        statusBar = new StatusBar();
        getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
        
        panel.setLayout(new BorderLayout());
        this.getContentPane().add(panel, BorderLayout.CENTER);
        
        menuBar = new JMenuBar();
        fileMenu = new JMenu("Viewer");
        this.setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        
        if(thDir == null || thDir.length() == 0)
            smartReader = new SmartReader();
        else {
            File dir = new File(thDir);
            smartReader = new SmartReader(dir);
        }
        
        fileMenu.add(new AbstractAction("Set Colors...") {
            public void actionPerformed(ActionEvent arg0) {
                int vr = Integer.parseInt(JOptionPane.showInputDialog("viewer background red"));
                int vg = Integer.parseInt(JOptionPane.showInputDialog("viewer background green"));
                int vb = Integer.parseInt(JOptionPane.showInputDialog("viewer background blue"));
                int ir = Integer.parseInt(JOptionPane.showInputDialog("image background red"));
                int ig = Integer.parseInt(JOptionPane.showInputDialog("image background green"));
                int ib = Integer.parseInt(JOptionPane.showInputDialog("image background blue"));
                
                panel.setBackground(new Color(vr, vg, vb));
                imageBackground = new Color(ir, ig, ib);
            }
        });
        
        fileMenu.add(new AbstractAction("Open File...") {
            public void actionPerformed(ActionEvent arg0) {
                try {
                panel.removeAll();
                JComponent c = smartReader.open();
                if(c != null) {
                    panel.add(c, BorderLayout.CENTER);
                    c.revalidate();
                }
                } catch(Exception e) {
                    System.err.println(e);
                }
            }
        });
    }
    
    public void setStatus(String str) {
        statusBar.setMessage(str);
    }
    
    private static JFileChooser fc = new JFileChooser();
    
    public static int parseInt(String s) {
        if(s.startsWith("0x")) {
            return Integer.parseInt(s.substring(2), 16);
        } else {
            return Integer.parseInt(s);
        }
    }
    
    public static File openFile(String title) {
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle(title);
        fc.showOpenDialog(null);
        
        return fc.getSelectedFile();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Main frame;
    
        if(args.length == 0) {
            frame = Main.getInstance();
        } else {
            frame = Main.getInstance(args[0]);
        }
            
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }
    
}

class StatusBar extends JLabel {
    
    /** Creates a new instance of StatusBar */
    public StatusBar() {
        super();
        super.setPreferredSize(new Dimension(100, 16));
        setMessage("Ready");
    }
    
    public void setMessage(String message) {
        setText(" "+message);        
    }        
}

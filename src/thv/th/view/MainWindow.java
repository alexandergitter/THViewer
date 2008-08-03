package thv.th.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import thv.th.reader.SmartReader;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
    JMenuBar menuBar;
    JMenu fileMenu;
    Color imageBackground = Color.WHITE;
    JPanel panel;
    SmartReader smartReader;
    private JFileChooser fc = new JFileChooser();
    
    /** Creates a new instance of Main */
    public MainWindow(String thDir) throws Exception {
        super("TH Viewer");

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panel, BorderLayout.CENTER);
        
        menuBar = new JMenuBar();
        fileMenu = new JMenu("Viewer");
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
        
        File dir;
       
        if(thDir == null || thDir.length() == 0)
            dir = getTHDir();
        else
            dir = new File(thDir);

        smartReader = new SmartReader(dir, this);
        
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
        
        JMenuItem openFile = new JMenuItem("Open File...");
        openFile.addActionListener(smartReader);
        fileMenu.add(openFile);
        /*fileMenu.add(new AbstractAction("Open File...") {
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
        });*/
    }
    
    private File getTHDir() throws Exception {
        fc.setDialogTitle("Select your Theme Hospital Directory");
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int t = fc.showOpenDialog(null);

        if (t == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        } else {
        	throw new Exception("You have to choose a Theme Hospital directory.");
        }
    }
    
    public File queryOpen(String title, File thDir) {
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle(title);
        fc.setCurrentDirectory(thDir);
        fc.showOpenDialog(this);

        return fc.getSelectedFile();
    }
    
    public void setContent(JComponent c) {
    	panel.removeAll();
    	panel.add(c, BorderLayout.CENTER);
    	c.revalidate();
    }
    
    /*private static JFileChooser fc = new JFileChooser();
    
    public static File openFile(String title) {
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogTitle(title);
        fc.showOpenDialog(null);
        
        return fc.getSelectedFile();
    }*/
}

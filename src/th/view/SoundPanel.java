/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 */

package th.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import th.data.Sample;
import th.data.THSound;
import th.reader.SoundReader;

public class SoundPanel extends JPanel implements ActionListener {
    private THSound sound;
    private JList listWidget;
    
    /** Creates a new instance of SoundPanel */
    public SoundPanel(File soundFile) {
        this.setLayout(new FlowLayout());
        try {
        this.sound = SoundReader.readAll(soundFile);
        } catch(Exception e) { e.printStackTrace(); }
        listWidget = new JList(sound.getSamples());
        listWidget.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(listWidget));
        JButton play = new JButton("play");
        play.addActionListener(this);
        add(play);
    }

    public void actionPerformed(ActionEvent e) {
        sound.play((Sample)listWidget.getSelectedValue());
    }
}

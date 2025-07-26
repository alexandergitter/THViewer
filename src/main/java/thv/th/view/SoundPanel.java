/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 */

package thv.th.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import thv.th.data.Sample;
import thv.th.data.THSound;

@SuppressWarnings("serial")
public class SoundPanel extends JPanel implements ActionListener {
    private THSound sound;
    private JList<Sample> listWidget;
    
    /** Creates a new instance of SoundPanel */
    public SoundPanel(THSound sound) {
        this.setLayout(new FlowLayout());
        this.sound = sound;

        listWidget = new JList<Sample>(sound.getSamples().toArray(new Sample[0]));
        listWidget.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(listWidget));
        JButton play = new JButton("play");
        play.addActionListener(this);
        add(play);
    }

    public void actionPerformed(ActionEvent e) {
        sound.play(listWidget.getSelectedValue());
    }
}

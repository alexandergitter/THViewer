/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.data;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JLabel;

public class THAnimation extends JLabel implements ActionListener {
    Vector<Image> frames;
    Vector<THFrame> fdata;
    int frameIndex = 0;
    int animIndex = 0;

    /** Creates a new instance of THAnimation */
    public THAnimation(int width, int height, int animIndex) {
        frames = new Vector<Image>();
        fdata = new Vector<THFrame>();
        this.animIndex = animIndex;
        
        this.setPreferredSize(new Dimension(width+2, height+2));
    }
    
    public Vector<THFrame> getFrames() {
        return fdata;
    }
    
    public int getAnimIndex() {
        return animIndex;
    } 
    
    public int getFrameWidth() {
        return frames.elementAt(0).getWidth(null);
    }
    
    public int getFrameHeight() {
        return frames.elementAt(0).getHeight(null);
    }
    
    public void addFrame(Image frame, THFrame data) {
        frames.add(frame);
        fdata.add(data);
    }
    
    public void paint(Graphics g) {
        Image frame = frames.elementAt(frameIndex);
        g.drawImage(frame, 0, 0, null);
    }
    
    public void actionPerformed(ActionEvent evt) {
        ++frameIndex;
        this.repaint();
        
        if(frameIndex >= frames.size())
            frameIndex = 0;
    }
}

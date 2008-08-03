/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import thv.th.data.SpriteElement;
import thv.th.data.THFrame;
import thv.th.reader.FramesReader;
import thv.util.ABuffer;
import thv.util.FileBuffer;

public final class FramePanel extends CommonImagePanel {

    protected File framesFile;
    protected File listFile;
    protected File spriteElementFile;
    
    protected Vector<THFrame> frames;
    
    /** Creates a new instance of SpriteElementPanel */
    public FramePanel(File paletteFile, File chunksFile, File tabFile, File spriteElementFile, File framesFile, File listFile) {
        super(paletteFile, chunksFile, tabFile);
        this.framesFile = framesFile;
        this.listFile = listFile;
        this.spriteElementFile = spriteElementFile;
        
        perPage = 40;
        init();
    }
    
    protected void loadFrames(int start) throws IOException {
        if(frames == null)
            frames = new Vector<THFrame>();
        
        frames.removeAllElements();
        
        int framesCount = (int)(framesFile.length() / 10);
        
        FileInputStream frameStream = new FileInputStream(framesFile);
        FileInputStream listStream = new FileInputStream(listFile);
        FileInputStream elementStream = new FileInputStream(spriteElementFile);
        
        for(int i = 0; i < perPage; ++i) {
            if((i + start) >= framesCount)
                break;
            
            THFrame t = FramesReader.readByIndex(i + start, frameStream, listStream, elementStream, tabFile, chunksFile, palette);

            if(t != null)
                frames.add(t);
        }
        
        frameStream.close();
        listStream.close();
        elementStream.close();
        
        selectPanel.setSelectionCount((framesCount / perPage) + 1);
    }
    
    protected void show(int start) throws IOException {
        loadPalette();
        loadFrames(start);
        panel.removeAll();
        
        ABuffer tabStream = new FileBuffer(tabFile);
        FileInputStream chunksStream = new FileInputStream(chunksFile);
        
        int layerid = 0;
        
        for(THFrame frame: frames) {
            Image img = frame.render(tabStream, chunksStream, palette, layerid);
            JLabel l = new JLabel(new ImageIcon(img));
            l.setBorder(BorderFactory.createLineBorder(Color.RED));
            l.setToolTipText(generateToolTip(frame, layerid));
            panel.add(l);
        }

        chunksStream.close();
        //tabStream.close();
        panel.revalidate();
        panel.repaint();
    }
    
    private String generateToolTip(THFrame frame, int layerid) {
        StringBuilder sb =  new StringBuilder("<html>");
        sb.append("<b>Index:</b>" + frame.getIndex());
        sb.append("<br><b>Position:</b> " + frame.getIndex() * 10);
        sb.append("<br><b>Width:</b> " + frame.getWidth());
        sb.append("<br><b>Height:</b> " + frame.getHeight());
        sb.append("<br><b>Flags:</b> " + frame.getFlags());
        sb.append("<br><u>Spriteelements</u>");
        
        Vector<SpriteElement> vec = frame.getElements();
        
        for(SpriteElement el: vec) {
            if(el.getId() < 2 || el.getId() == layerid) {
                sb.append("<br>" + el.getId());
                sb.append("<br><b>OffsetX:</b> " + el.getOffsetx());
                sb.append("<br><b>OffsetY:</b> " + el.getOffsety());
                sb.append("<br><b>Tabposition:</b> " + el.getTabPos());
            }
        }
        
        return sb.toString();
    }
}
/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Timer;

import org.apache.commons.io.EndianUtils;

import thv.th.data.THAnimation;
import thv.th.data.THFrame;
import thv.th.reader.FramesReader;
import thv.util.ABuffer;
import thv.util.FileBuffer;

public class AnimationPanel extends CommonImagePanel {
    
    protected File framesFile;
    protected File listFile;
    protected File spriteElementFile;
    protected File startFile;
    
    protected Timer timer;
    
    /** Creates a new instance of AnimationPanel */
    public AnimationPanel(File paletteFile, File chunksFile, File tabFile, File spriteElementFile, File framesFile, File listFile, File startFile) {
        super(paletteFile, chunksFile, tabFile);
        this.framesFile = framesFile;
        this.listFile = listFile;
        this.spriteElementFile = spriteElementFile;
        this.startFile = startFile;
        
        timer = new Timer(200, null);
        
        perPage = 20;
        init();
    }
    
    private Vector<THFrame> loadFrameChain(int index, FileInputStream frameStream, FileInputStream listStream, FileInputStream elementStream) throws IOException {
        Vector<THFrame> res = new Vector<THFrame>();
        
        THFrame first = FramesReader.readByIndex(index, frameStream, listStream, elementStream, tabFile, chunksFile, palette);
        
        if(first != null) {
            res.add(first);
            
            int curIndex = first.getNext();
            int count = 0;

            while(curIndex != first.getIndex() && count++ < 20) {
                THFrame next = FramesReader.readByIndex(curIndex, frameStream, listStream, elementStream, tabFile, chunksFile, palette);
                res.add(next);
                curIndex = next.getNext();
            }
        }
        
        return res;
    }
    
    protected Vector<THAnimation> loadAnimations(int start) throws IOException {
        Vector<THAnimation> res = new Vector<THAnimation>();
        
        int animCount = (int)(startFile.length() / 4);
        
        FileInputStream startStream = new FileInputStream(startFile);
        ABuffer tabStream = new FileBuffer(tabFile);
        FileInputStream chunkStream = new FileInputStream(chunksFile);
        FileInputStream frameStream = new FileInputStream(framesFile);
        FileInputStream listStream = new FileInputStream(listFile);
        FileInputStream elementStream = new FileInputStream(spriteElementFile);
        
        for(int i = 0; i < perPage; ++i) {
            if((i + start) >= animCount)
                break;
            
            startStream.getChannel().position((i+start)*4);
            int index = EndianUtils.readSwappedShort(startStream);
            
            int maxFrameWidth = 0;
            int maxFrameHeight = 0;
            int maxXOffset = 0;
            int minYOffset = 256;
            
            Vector<THFrame> frames = loadFrameChain(index, frameStream, listStream, elementStream);
            
            if(frames.size() == 0)
                continue;
            
            for(THFrame frame: frames) {
                if(frame.getWidth() > maxFrameWidth) {
                    maxFrameWidth = frame.getWidth();
                    //maxXOffset = frame.getMaxXOffset(tabStream);
                }
                
                maxXOffset = Math.max(maxXOffset, frame.getMaxXOffset(tabStream));
                minYOffset = Math.min(minYOffset, frame.getMinYOffset(tabStream));
            }
            
            int xover = 0;
            int xunder = 0;
            int yover = 0;
            int yunder = 0;
            
            for(THFrame frame: frames) {
                xover = Math.max(xover, frame.getXOverrun(maxFrameWidth, maxXOffset, tabStream));
                xunder = Math.max(xunder, frame.getXUnderrun(maxFrameWidth, maxXOffset, tabStream));
                yover = Math.max(yover, frame.getYOverrun(0, minYOffset, tabStream));
                yunder = Math.max(yunder, frame.getYUnderrun(0, minYOffset, tabStream));
            }
            
            maxFrameWidth += xover + xunder;
            maxFrameHeight += yover + yunder;
            
            THAnimation an = new THAnimation(maxFrameWidth, maxFrameHeight, i+start);
            
            for(THFrame frame: frames) {
                an.addFrame(frame.renderInAnim(tabStream, chunkStream, palette, 4, maxFrameWidth, maxFrameHeight, maxXOffset, minYOffset, xunder, yunder), frame);
            }
            
            res.add(an);
        }
        
        tabStream.close();
        chunkStream.close();
        frameStream.close();
        listStream.close();
        elementStream.close();
        
        selectPanel.setSelectionCount((animCount / perPage) + 1);
        
        return res;
    }
    
    protected void show(int start) throws IOException {
        panel.removeAll();
        
        loadPalette();
        Vector<THAnimation> anims = loadAnimations(start);
        
        timer.stop();
        
        for(ActionListener al: timer.getActionListeners())
            timer.removeActionListener(al);
        
        for(THAnimation anim: anims) {
            timer.addActionListener(anim);
            panel.add(anim);
            anim.setBorder(BorderFactory.createLineBorder(Color.RED));
            
            StringBuilder sb = new StringBuilder();
            sb.append("<html>index: ");
            sb.append(anim.getAnimIndex());
            sb.append("<br>width: ");
            sb.append(anim.getFrameWidth());
            sb.append("<br>height: ");
            sb.append(anim.getFrameHeight());
            sb.append("<br>Frames");
            
            for(THFrame f: anim.getFrames()) {
                sb.append("<br>" + f.getIndex());
            }
            
            anim.setToolTipText(sb.toString());
        }
        
        timer.start();

        panel.revalidate();
        panel.repaint();
    }
    
}

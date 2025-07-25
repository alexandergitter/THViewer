/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.data;

import java.awt.image.BufferedImage;

public class Tile {
    private int anim;
    private BufferedImage layer0 = null;
    private BufferedImage layer1 = null;
    private BufferedImage layer2 = null;
    
    private BufferedImage shadow0 = null;
    private BufferedImage shadow1 = null;
    
    private THFrame litter;
    
    private ObjectInfo objectInfo = null;
    
    private int parcel = 0;
    private int filePosition = 0;
    
    /** Creates a new instance of Tile */
    public Tile(int anim, BufferedImage layer0, BufferedImage layer1, BufferedImage layer2, int filePosition) {
        this.setAnim(anim);
        this.setLayer0(layer0);
        this.setLayer1(layer1);
        this.setLayer2(layer2);
        this.filePosition = filePosition;
    }
    
    public Tile(int anim, BufferedImage layer0, BufferedImage layer1, int fp) {
        this(anim, layer0, layer1, null, fp);
    }
    
    public Tile(int anim, BufferedImage layer0, int fp) {
        this(anim, layer0, null, null, fp);
    }
    
    public int getWidth() {
        int w0 = layer0.getWidth(null);
        int w1 = 0;
        int w2 = 0;
        
        if(layer1 != null)
            w1 = layer1.getWidth(null);
        
        if(layer2 != null)
            w2 = layer2.getWidth(null);
        
        return Math.max(Math.max(w0, w1), w2);
    }
    
    public int getHeight() {
        int w0 = layer0.getHeight(null);
        int w1 = 0;
        int w2 = 0;
        
        if(layer1 != null)
            w1 = layer1.getHeight(null);
        
        if(layer2 != null)
            w2 = layer2.getHeight(null);
        
        return Math.max(Math.max(w0, w1), w2);
    }

    public BufferedImage getLayer0() {
        return layer0;
    }

    public void setLayer0(BufferedImage layer0) {
        this.layer0 = layer0;
    }

    public BufferedImage getLayer1() {
        return layer1;
    }

    public void setLayer1(BufferedImage layer1) {
        this.layer1 = layer1;
    }

    public BufferedImage getLayer2() {
        return layer2;
    }

    public void setLayer2(BufferedImage layer2) {
        this.layer2 = layer2;
    }

    public int getParcel() {
        return parcel;
    }

    public void setParcel(int parcel) {
        this.parcel = parcel;
    }
    
    public int getFilePosition() {
        return filePosition;
    }

    public ObjectInfo getObjectInfo() {
        return objectInfo;
    }

    public void setObjectInfo(ObjectInfo objectInfo) {
        this.objectInfo = objectInfo;
    }

    public int getAnim() {
        return anim;
    }

    public void setAnim(int anim) {
        this.anim = anim;
    }
    
    public void setShadow0(BufferedImage i) {
        this.shadow0 = i;
    }
    
    public void setShadow1(BufferedImage i) {
        this.shadow1 = i;
    }
    
    public void setLitter(THFrame f) {
        this.litter = f;
    }
    
    public BufferedImage getShadow0() {
        return this.shadow0;
    }
    
    public BufferedImage getShadow1() {
        return this.shadow1;
    }
    
    public THFrame getLitter() {
        return this.litter;
    }
}
/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 */

package th.data;


public class ObjectInfo {
    private int position;
    private THFrame frame;
    private int byte16;
    private int[] layerIds;
    
    public ObjectInfo(int position) {
        this.position = position;
        layerIds = new int[13];
    }
    
    public int getPosition() {
        return position;
    }
    
    public THFrame getFrame() {
        return frame;
    }
    
    public void setFrame(THFrame frame) {
        this.frame = frame;
    }
    
    public int getByte16() {
        return byte16;
    }
    
    public void setByte16(int byte16) {
        this.byte16 = byte16;
    }
    
    public void setLayerId(int layerid, int layerClass) {
        this.layerIds[layerClass] = layerid;
    }
    
    public int getLayerId(int layerClass) {
        return this.layerIds[layerClass] * 2;
    }
}

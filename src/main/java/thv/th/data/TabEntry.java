/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.data;

public class TabEntry {

    private int tabPos;
    private int chunksPos;
    private int width;
    private int height;
    private int consecNumber;
    
    public TabEntry(int tabPos, int chunksPos, int width, int height, int consecNumber) {
        this.setTabPos(tabPos);
        this.setChunksPos(chunksPos);
        this.setHeight(height);
        this.setWidth(width);
        this.setConsecNumber(consecNumber);
    }

    public int getChunksPos() {
        return chunksPos;
    }

    public void setChunksPos(int pos) {
        this.chunksPos = pos;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTabPos() {
        return tabPos;
    }
    
    public int getTabIndex() {
        return (tabPos / 6);
    }

    public void setTabPos(int tabPos) {
        this.tabPos = tabPos;
    }
    
    public int getConsecNumber() {
        return consecNumber;
    }

    public void setConsecNumber(int consecNumber) {
        this.consecNumber = consecNumber;
    }
}

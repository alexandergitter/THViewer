/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 */

package thv.th.data;

public class Sample {
    private String filename;
    private int position;
    private int length;
    
    public Sample(String filename, int position, int length) {
        this.filename = filename;
        this.position = position;
        this.length = length;
    }
    
    public String toString() {
        return this.filename;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public int getLength() {
        return this.length;
    }
}

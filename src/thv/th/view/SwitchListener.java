/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.view;

public interface SwitchListener {
    public final static int PREVIOUS = 1;
    public final static int NEXT = 2;
    public final static int NEW = 3;
    public final static int DELETE = 4;

    public void userSwitch( int action, int from );
}

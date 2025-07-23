/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.reader;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;

import thv.th.data.THPalette;

public class PaletteReader {
    public static THPalette readAll(FileInputStream is) throws IOException {
        THPalette res = new THPalette();
        
        while(is.available() > 0) {
            int r  = is.read()*4;
            int g = is.read()*4;
            int b = is.read()*4;
            
            res.add(new Color(r, g, b));
        }
        
        return res;
    }
    
}

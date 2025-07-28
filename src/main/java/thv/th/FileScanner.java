/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2008, Alexander Gitter
 
 All rights reserved.
*/

package thv.th;

import java.io.File;
import java.util.HashMap;
import java.util.Stack;

public class FileScanner {
    public static HashMap<String, File>
    scanDirectory(File dir) {
        HashMap<String, File> result = new HashMap<String, File>();

        Stack<File> stack = new Stack<File>();
        File curDir;
        
        stack.push(dir);
        
        while(!stack.isEmpty()) {
            curDir = stack.pop();
            
            for(File f: curDir.listFiles()) {
                if(f.isDirectory() && !f.getName().equalsIgnoreCase("datam")) {
                    stack.push(f);
                } else {
                    result.put(f.getName().toLowerCase(), f);
                }
            }
        }
        
        return result;
    }
}

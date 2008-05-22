/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2008, Alexander Gitter
 
 All rights reserved.
*/

package th.reader;

import java.io.File;
import java.util.Hashtable;
import java.util.Stack;

public class FileScanner {
    public static Hashtable<String, File>
    scanDirectory(File dir) {
        Hashtable<String, File> result = new Hashtable<String, File>();
        
        Stack<File> stack = new Stack<File>();
        File curDir;
        
        stack.push(dir);
        
        while(!stack.isEmpty()) {
            curDir = stack.pop();
            
            for(File f: curDir.listFiles()) {
                if(f.isDirectory()) {
                    stack.push(f);
                } else {
                    result.put(f.getName().toLowerCase(), f);
                }
            }
        }
        
        return result;
    }
}

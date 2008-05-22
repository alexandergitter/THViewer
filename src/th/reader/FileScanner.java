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

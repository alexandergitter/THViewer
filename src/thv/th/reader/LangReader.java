/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
*/

package thv.th.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Vector;
import org.apache.commons.io.EndianUtils;

public class LangReader {
	/**
	 * Reads in all the strings of a language-file.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
    public static Vector<Vector<String>> read(FileInputStream is)
    throws IOException {
        int sections = EndianUtils.readSwappedShort(is);
        
        Vector<Integer> counts = new Vector<Integer>();
        
        for(int i = 0; i < sections; ++i) {
            counts.add((int)EndianUtils.readSwappedShort(is));
        }
        
        Vector<Vector<String>> res = new Vector<Vector<String>>();
        Reader r = new InputStreamReader(is, "Cp437");
        
        for(int i= 0; i < sections; ++i) {
            int n = counts.elementAt(i);
            Vector<String> temp = new Vector<String>();
            
            for(int j = 0; j < n; ++j) {
                temp.add(readNullTerminatedString(r));
            }
            
            res.add(temp);
        }

        return res;
    }
    
    public static String readNullTerminatedString(Reader is) throws IOException {
        StringBuilder b = new StringBuilder();
        
        while(true) {
            char c = (char)is.read();
            if( c == 0 )
                break;
            else
                b.append(c);
        } 
        
        return b.toString();
    }
}

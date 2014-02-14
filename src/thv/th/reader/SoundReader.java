/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 */

package thv.th.reader;

import java.io.IOException;
import java.util.Vector;

import thv.th.data.Sample;
import thv.util.ABuffer;

public class SoundReader {
    public static Vector<Sample> readAll(ABuffer buffer)
    throws IOException {
        Vector<Sample> result = new Vector<Sample>();
        
        
        
//        FileInputStream fis = new FileInputStream(soundFile);
//        fis.getChannel().position(soundFile.length() - 4);
//        int infoPosition = EndianUtils.readSwappedInteger(fis);
        int infoPosition = buffer.getSwappedInteger(buffer.getSize() - 4);

//        fis.getChannel().position(infoPosition + 50);
//        int indexPosition = EndianUtils.readSwappedInteger(fis);
//        fis.skip(4);
//        int indexLength = EndianUtils.readSwappedInteger(fis);
//        int indexCount = (indexLength / 32) - 1;
//        
//        fis.getChannel().position(indexPosition);
//        fis.skip(32);
//        
//        for(int i = 0; i < indexCount; ++i) {
//            StringBuilder sb = new StringBuilder();
//            
//            for(int j = 0; j < 18; ++j) {
//                char c = (char)fis.read();
//                if(c != 0)
//                    sb.append(c);
//            }
//            
//            int samplePosition = EndianUtils.readSwappedInteger(fis);
//            fis.skip(4);
//            int sampleLength = EndianUtils.readSwappedInteger(fis);
//            fis.skip(2);
//            
//            result.addSample(new Sample(sb.toString(), samplePosition, sampleLength));
//        }
//        
//        fis.close();
        
        
        return result;
    }
    
    
}

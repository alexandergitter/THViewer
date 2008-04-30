/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 */

package th.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.io.EndianUtils;
import th.data.Sample;
import th.data.THSound;

public class SoundReader {
    public static THSound readAll(File soundFile) throws FileNotFoundException, IOException {
        THSound result = new THSound(soundFile);
        
        FileInputStream fis = new FileInputStream(soundFile);
        fis.getChannel().position(soundFile.length() - 4);
        
        int infoPosition = EndianUtils.readSwappedInteger(fis);
        fis.getChannel().position(infoPosition + 50);
        int indexPosition = EndianUtils.readSwappedInteger(fis);
        fis.skip(4);
        int indexLength = EndianUtils.readSwappedInteger(fis);
        int indexCount = (indexLength / 32) - 1;
        
        fis.getChannel().position(indexPosition);
        fis.skip(32);
        
        for(int i = 0; i < indexCount; ++i) {
            StringBuilder sb = new StringBuilder();
            
            for(int j = 0; j < 18; ++j) {
                char c = (char)fis.read();
                if(c != 0)
                    sb.append(c);
            }
            
            int samplePosition = EndianUtils.readSwappedInteger(fis);
            fis.skip(4);
            int sampleLength = EndianUtils.readSwappedInteger(fis);
            fis.skip(2);
            
            result.addSample(new Sample(sb.toString(), samplePosition, sampleLength));
        }
        
        fis.close();
        return result;
    }       
}

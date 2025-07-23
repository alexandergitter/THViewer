/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 Copyright (c) 2025, Stephen E. Baker
 
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
        
        int infoPosition = buffer.getSwappedInteger(buffer.getSize() - 4);
        int indexPosition = buffer.getSwappedInteger(infoPosition + 50);
        int indexLength = buffer.getSwappedInteger(infoPosition + 58);
        int indexCount = (indexLength / 32) - 1;

        for (int i = 1; i <= indexCount; ++i) {
            StringBuilder sb = new StringBuilder();
            
            for (int j = 0; j < 18; ++j) {
                char c = (char) buffer.get(indexPosition + (i * 32) + j);
                if (c != 0)
                    sb.append(c);
            }

            int samplePosition = buffer.getSwappedInteger(indexPosition + (i * 32) + 18);
            int sampleLength = buffer.getSwappedInteger(indexPosition + (i * 32) + 26);

            result.add(new Sample(sb.toString(), samplePosition, sampleLength));
        }
        
        return result;
    }
}

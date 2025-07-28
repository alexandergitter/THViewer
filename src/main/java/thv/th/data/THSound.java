/*
 This project is made available under the terms of the BSD license, more
 information can be found at
 http://www.opensource.org/licenses/bsd-license.html
 
 Copyright (c) 2007, Alexander Gitter
 
 All rights reserved.
 */

package thv.th.data;

import java.io.InputStream;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import thv.util.ABuffer;

public class THSound {
    
    private ABuffer soundBuffer;
    private ArrayList<Sample> samples;
    
    /** Creates a new instance of THSound */
    public THSound(ABuffer soundBuffer) {
        this.soundBuffer = soundBuffer;
        this.samples = new ArrayList<Sample>();
    }
    
    public void addSample(Sample s) {
        samples.add(s);
    }
    
    public ArrayList<Sample> getSamples() {
        return this.samples;
    }
    
    public void play(Sample s) {
        try {
            InputStream fis = soundBuffer.createInputStream();
            fis.skip(s.getPosition());
            
            // From file
            AudioInputStream stream = AudioSystem.getAudioInputStream(fis);
        
            // At present, ALAW and ULAW encodings must be converted
            // to PCM_SIGNED before it can be played
            AudioFormat format = stream.getFormat();

            // Create the clip
            DataLine.Info info = new DataLine.Info(
                Clip.class, stream.getFormat(), ((int)stream.getFrameLength()*format.getFrameSize()));
            Clip clip = (Clip) AudioSystem.getLine(info);
        
            // This method does not return until the audio file is completely loaded
            clip.open(stream);
        
            // Start playing
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
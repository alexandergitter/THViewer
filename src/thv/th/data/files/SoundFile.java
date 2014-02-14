package thv.th.data.files;

import java.util.Vector;

import thv.th.data.Sample;
import thv.th.reader.SoundReader;
import thv.util.ABuffer;

public class SoundFile {
	protected ABuffer rawData;
	
	protected Vector<Sample> samples;
	
	public SoundFile(ABuffer rawData) {
		this.rawData = rawData;
		this.samples = SoundReader.readAll(rawData);
	}
}

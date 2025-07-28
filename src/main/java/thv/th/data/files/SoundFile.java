package thv.th.data.files;

import java.io.IOException;
import java.util.ArrayList;

import thv.th.data.Sample;
import thv.th.reader.SoundReader;
import thv.util.ABuffer;

public class SoundFile {
	protected ABuffer rawData;
	
	protected ArrayList<Sample> samples;
	
	public SoundFile(ABuffer rawData) throws IOException {
		this.rawData = rawData;
		this.samples = SoundReader.readAll(rawData);
	}
}

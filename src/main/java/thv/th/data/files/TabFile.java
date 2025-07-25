package thv.th.data.files;

import java.io.IOException;

import thv.th.data.TabEntry;
import thv.th.reader.TabReader;
import thv.util.ABuffer;

public class TabFile {
	protected ABuffer rawData;
	
	public TabFile(ABuffer rawData) {
		this.rawData = rawData;
	}
	
	public TabEntry getByIndex(int index)
	throws IOException {
		return TabReader.readByPosition(rawData, index*6, 0);
	}
}

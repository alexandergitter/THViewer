package thv.th.data.files;

import java.io.IOException;
import java.util.ArrayList;

import thv.th.reader.LangReader;
import thv.util.ABuffer;

public class StringsFile {
	protected ABuffer rawData;
	
	protected ArrayList<ArrayList<String>> strings;
	
	public StringsFile(ABuffer rawData)
	throws IOException {
		this.rawData = rawData;
		this.strings = LangReader.read(rawData);
	}
	
	public int numberSections() {
		return strings.size();
	}
	
	public int numberStrings(int section) {
		return strings.get(section).size();
	}
	
	public String getString(int section, int index) {
		return strings.get(section).get(index);
	}
}

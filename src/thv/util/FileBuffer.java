package thv.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileBuffer extends ABuffer {

	File file;
	
	public FileBuffer(File file)
	throws FileNotFoundException {
		this.file = file;
	}
	
	@Override
	public int getSize() throws IOException {
		return (int)file.length();
	}

	@Override
	public int get(int pos) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		fis.getChannel().position(pos);
		
		int result = fis.read();
		fis.close();
		
		return result;
	}
}

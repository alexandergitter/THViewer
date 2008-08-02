package thv.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileBuffer extends ABuffer {

	FileInputStream fis;
	
	public FileBuffer(File file)
	throws FileNotFoundException {
		this.fis = new FileInputStream(file);
	}
	
	@Override
	public int available() throws IOException {
		return fis.available();
	}

	@Override
	public void seek(long pos) throws IOException {
		fis.getChannel().position(pos);
	}

	@Override
	public int read() throws IOException {
		return fis.read();
	}

	@Override
	public void close() throws IOException {
		fis.close();
	}

}

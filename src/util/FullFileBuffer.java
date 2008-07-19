package util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FullFileBuffer
extends ABuffer {
	ByteArrayInputStream buffer = null;
	
	public FullFileBuffer(File backEndFile)
	throws FileNotFoundException, IOException {
		byte[] temp = new byte[(int)backEndFile.length()];
		
		FileInputStream fis = new FileInputStream(backEndFile);
		fis.read(temp);
		fis.close();
		
		buffer = new ByteArrayInputStream(temp);
	}
	
	@Override
	public int available() {
		return buffer.available();
	}

	@Override
	public void seek(long pos) throws IOException {
		buffer.reset();
		buffer.skip(pos);
	}

	@Override
	public int read() throws IOException {
		return buffer.read();
	}

	@Override
	public void close() throws IOException {
		buffer.close();
	}

}

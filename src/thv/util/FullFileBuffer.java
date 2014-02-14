package thv.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FullFileBuffer
extends ABuffer {
	byte[] buffer = null;
	
	public FullFileBuffer(File backEndFile)
	throws FileNotFoundException, IOException {
		buffer = new byte[(int)backEndFile.length()];
		
		FileInputStream fis = new FileInputStream(backEndFile);
		fis.read(buffer);
		fis.close();
	}
	
	@Override
	public int getSize() {
		return buffer.length;
	}

	@Override
	public int get(int position) throws IOException {
		return buffer[position] & 0xff;
	}

	@Override
	public InputStream createInputStream() throws IOException {
		return new ByteArrayInputStream(this.buffer);
	}
}

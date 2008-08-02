package thv.util;

import java.io.IOException;
import java.io.InputStream;

public abstract class ABuffer
extends InputStream {
	abstract public void seek(long pos) throws IOException;
	abstract public int available() throws IOException;
	abstract public void close() throws IOException;
}

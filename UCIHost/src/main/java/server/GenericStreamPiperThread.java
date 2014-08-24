package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GenericStreamPiperThread implements Runnable {

	public static final int DEFAULT_BUFFER_SIZE = 1024*1000;
	
	private InputStream is;
	private OutputStream os;
	
	public GenericStreamPiperThread(InputStream is, OutputStream os) {
		this.is = is;
		this.os = os;
	}
	
	public void run() {
		try {
			streamPiper(is, os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void streamPiper(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int readLen = 0;
		while((readLen = is.read(buffer, 0, buffer.length)) != -1) {
			os.write(buffer, 0, readLen);
			os.flush();
		}
	}

}

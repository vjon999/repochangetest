package unused;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import server.ChessLogger;

public class SimpleEngineToGUIWriter implements Runnable {
	
	public static final int DEFAULT_BUFFER_SIZE = 1024*10;
	
	private ChessLogger LOG;
	private InputStream engineInputStream;
	private OutputStream guiOutputStream;
	private boolean stop = false;
	
	public SimpleEngineToGUIWriter(InputStream engineInputStream, OutputStream outputStream, ChessLogger LOG) throws IOException {
		this.engineInputStream = engineInputStream;
		this.guiOutputStream = outputStream;
		this.LOG = LOG;
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	public void run() {
		
		try {
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int readLen = 0;
			while(!stop && (readLen = engineInputStream.read(buffer, 0, buffer.length)) != -1) {
				guiOutputStream.write(buffer, 0, readLen);
				guiOutputStream.flush();
				LOG.log(new String(buffer, 0, readLen));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			stop = true;
		}		
		finally {
			if(null != guiOutputStream) {
				try {
					guiOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		LOG.log("Ending EngineToGUIWriterThread");
	}
}

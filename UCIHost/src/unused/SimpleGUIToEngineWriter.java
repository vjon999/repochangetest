package unused;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import server.ChessLogger;

public class SimpleGUIToEngineWriter implements Runnable {
	
	private OutputStream outputStream;
	private InputStream guiInputStream;
	private volatile boolean stop = false;
	private ChessLogger LOG;
	public static final int DEFAULT_BUFFER_SIZE = 1024;
	
	public SimpleGUIToEngineWriter(InputStream guiInputStream, OutputStream outputStream, ChessLogger LOG) throws IOException {
		this.guiInputStream =  guiInputStream;		
		this.outputStream = outputStream;
		this.LOG = LOG;
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	@Override
	public void run() {
		int readLen = 0;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		try {
			while(!stop && (readLen = guiInputStream.read(buffer, 0, buffer.length)) != -1) {
				outputStream.write(buffer, 0, readLen);
				//outputStream.write("\n".getBytes());
				outputStream.flush();
				if("quit".equalsIgnoreCase(new String(buffer, 0, readLen))) {
					stop = true;
					LOG.log("client is quitting");
				}
				LOG.log("gui >> "+new String(buffer, 0, readLen)+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (null != outputStream) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != guiInputStream) {
				try {
					guiInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		LOG.log("Ending GUIToEngineWriterThread");
	}
}

package unused;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import server.ChessLogger;

public class EngineToGUIWriter implements Runnable {
	
	private ChessLogger LOG;
	private BufferedReader engineReader;
	private OutputStream guiOutputStream;
	private boolean stop = false;
	public EngineToGUIWriter(InputStream engineInputStream, OutputStream outputStream, ChessLogger LOG) throws IOException {
		engineReader = new BufferedReader(new InputStreamReader(engineInputStream));
		guiOutputStream = outputStream;
		this.LOG = LOG;
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	public void run() {
		StringBuilder line = new StringBuilder();
		String text = null;
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(guiOutputStream);
			oos.writeObject("Rybka started");
			while(!stop) {				
				if(engineReader.ready()) {
					while(null != (text = engineReader.readLine())) {
						line.append(text+"\r\n");
						if(!engineReader.ready()) {
							break;
						}
					}
					if(null != line) {
						oos.writeObject(line.toString());
						//guiOutputStream.flush();
						LOG.log("ENGINE-TO-GUI: "+line);						
					}					
					line = new StringBuilder();
				}
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
			if(null != engineReader) {
				try {
					engineReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		LOG.log("Ending EngineToGUIWriterThread");
	}
}

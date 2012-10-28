package unused;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import server.ChessLogger;

public class GUIToEngineWriter implements Runnable {
	
	private OutputStream outputStream;
	private InputStream guiInputStream;
	private boolean stop = false;
	private ChessLogger LOG;
	private EngineToGUIWriter engineToGUIWriter;
	
	public GUIToEngineWriter(InputStream guiInputStream, OutputStream outputStream, ChessLogger LOG) throws IOException {
		this.guiInputStream =  guiInputStream;		
		this.outputStream = new BufferedOutputStream(outputStream);
		this.LOG = LOG;
	}
	
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(guiInputStream);
			String message = null;			
			while (!stop && null != ois) {
				message = (String) ois.readObject();
				if(null != message) {
					 if(!message.contains("\n"))
							 message += "\n";
					outputStream.write(message.getBytes());
					outputStream.flush();
					LOG.log("GUI-TO-ENGINE: " + message);
					if (message.contains("quit")) {
						stop = true;
						engineToGUIWriter.setStop(true);
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			stop = true;
		} catch (ClassNotFoundException e) {
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

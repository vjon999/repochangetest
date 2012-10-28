package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ChessLogger implements Runnable {
	
	private BufferedWriter LOG;
	
	public ChessLogger(File logFile) throws IOException {
		if(!logFile.exists()) {
			logFile.createNewFile();
		}	
		/*Thread t = new Thread(this);
		t.start();*/
		LOG = new BufferedWriter(new FileWriter(logFile, true));
	}
	
	public void log(String line) {
		/*try {
			if(!line.contains("\n")) {
				line+="\n";
			}
			LOG.write(new Date(System.currentTimeMillis())+" -> "+line);
			//LOG.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	public void log(byte[] buffer, int readLen) {
		try {
			LOG.write(/*new Date(System.currentTimeMillis())+" -> "+*/new String(buffer, 0, readLen));
			//LOG.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void flush() {
		try {
			LOG.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				flush();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
}

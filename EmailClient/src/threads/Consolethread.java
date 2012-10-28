package threads;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JTextArea;

public class Consolethread implements Runnable {
	
	private JTextArea console;
	private File logFile = null; 
	
	public Consolethread(JTextArea console,String logFileName) {
		this.console = console;
		this.logFile = new File(logFileName);
		if(logFile.exists()) {
			new Thread(this).start();
		}
	}
	
	@Override
	public void run() {		
		try {
			BufferedReader br = new BufferedReader(new FileReader(logFile));
			while(true) {
				console.setText("");
				Thread.sleep(1000);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}
}

package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;

public class EngineClient implements Runnable {

	private Properties config;
	private File logFile;
	private ChessLogger LOG;
	private boolean clearLog = false;
	private OutputStream socketOutputStream;
	private InputStream socketInputStream;
	private Socket clientSocket;
	private String enginePath;
	private boolean authenticated = false;
	
	public EngineClient(InputStream inputStream, OutputStream outputStream) throws IOException {
		
	}
	
	public EngineClient(InputStream inputStream, OutputStream outputStream, String engineName) throws IOException {
		this(inputStream, outputStream);
		this.enginePath = engineName;
	}
	
	public EngineClient(Socket clientSocket, String engineName) throws IOException {
		this.clientSocket = clientSocket;
		this.enginePath = engineName;
		this.socketOutputStream = clientSocket.getOutputStream();
		this.socketInputStream = clientSocket.getInputStream();
		try {
			System.out.println("user directory: "+System.getProperty("user.dir"));
			System.out.println(System.getProperty("user.dir")+"/config.ini");
			config = new Properties();
			config.load(new FileInputStream(System.getProperty("user.dir")+"/config.ini"));
			if(null != config.getProperty("clear_log")) {
				try {
					clearLog = Boolean.parseBoolean(config.getProperty("clear_log"));						
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			logFile = new File(config.getProperty("log_path"));
			if(clearLog && logFile.exists()) {
				logFile.delete();
				logFile.createNewFile();
			}
			System.out.println(logFile);
			LOG = new ChessLogger(logFile);			
		} catch (FileNotFoundException e) {
			config = null;
			e.printStackTrace();
		} catch (IOException e) {
			config = null;
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		/*Thread t = new Thread(new EngineClient(System.in, System.out));
		t.start();*/
		new EngineClient(System.in, System.out).startEngine();		
	}
	
	public void startEngine() throws InterruptedException {		
		if(config != null) {
			Process p = null;
			try {
				System.out.println("preferred enginePath "+enginePath);
				String path = (enginePath!=null)?enginePath:config.getProperty("engine_path");
				System.out.println("Engine Path : "+path);
				LOG.log("\n----------------------------------------------------------------------------------------------------------------------------------------------\nengine_path = "+path);
				p = Runtime.getRuntime().exec(path);
				LOG.log("Engine started");
				
				//Thread guiToEngineWriterThread = new Thread(new SimpleEngineToGUIWriter(socketInputStream, p.getOutputStream(), LOG));
				//Thread engineToGUIWriterThread = new Thread(new SimpleGUIToEngineWriter(p.getInputStream(), socketOutputStream, LOG));
				Thread guiToEngineWriterThread = new Thread(new GenericStreamPiperThread(p.getInputStream(), socketOutputStream));
				Thread engineToGUIWriterThread = new Thread(new GenericStreamPiperThread(socketInputStream, p.getOutputStream()));
				
				guiToEngineWriterThread.start();
				LOG.log("guiToEngineWriterThread started");
				engineToGUIWriterThread.start();
				LOG.log("engineToGUIWriterThread started");
				
				if(guiToEngineWriterThread.isAlive())
					guiToEngineWriterThread.join();
				if(engineToGUIWriterThread.isAlive())
					engineToGUIWriterThread.join();
				
				if(!guiToEngineWriterThread.isAlive()) {
					LOG.log("guiToEngineWriterThread Closed");
					engineToGUIWriterThread.stop();
				}
				if(!engineToGUIWriterThread.isAlive()) {
					LOG.log("engineToGUIWriterThread Closed");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					p.destroy();
					clientSocket.close();
					LOG.log("Client Socket Closed");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			LOG.log("Cannot find the Configuration file config.ini, Exitting");
		}
		LOG.log("Closed all connections and processes\n");
		LOG.log("Exiting engineClient");
		LOG.flush();
	}
	
	@Override
	public void run() {
		try {
			startEngine();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
}

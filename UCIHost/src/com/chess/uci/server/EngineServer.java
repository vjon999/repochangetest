package com.chess.uci.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

import com.chess.uci.share.AsyncReader;
import com.chess.uci.share.AsyncWriter;
import com.chess.uci.share.NetworkRW;
import com.chess.uci.share.TCPNetworkRW;

public class EngineServer implements Runnable {

	private static Logger LOG = Logger.getLogger(EngineServer.class.getName());
	
	private NetworkRW networkRW;
	private ServerSocket serverSocket;
	public boolean exit = false;
	private String enginePath;
	public EngineServer(int port, String enginePath) throws FileNotFoundException, IOException {
		this.enginePath = enginePath;
		serverSocket = new ServerSocket(port);
		LOG.info("TCP server port: "+port);
	}
	
	public void listen() throws IOException {
		LOG.info("waiting for connection on Engine port: "+serverSocket.getLocalPort());
		LOG.info("Engine: "+enginePath);
		networkRW = new TCPNetworkRW(serverSocket.accept());
		LOG.info("\n--------------------------------------Start Server ----------------------------------\n");
		Process p = null;
		if(!networkRW.isClosed() && networkRW.isConnected()) {
			try {
				p = startEngine(enginePath);
				Thread guiToEngineWriterThread = new Thread(new AsyncReader(p.getOutputStream(), networkRW, exit));
				Thread engineToGUIWriterThread = new Thread(new AsyncWriter(p.getInputStream(), networkRW, exit));
				// writer.setDaemon(true);
				guiToEngineWriterThread.start();
				engineToGUIWriterThread.start();

				if(guiToEngineWriterThread.isAlive())
					guiToEngineWriterThread.join();
				if(engineToGUIWriterThread.isAlive())
					engineToGUIWriterThread.join();

				LOG.info("Closing Engine on port "+networkRW.getPort());
				networkRW.writeToNetwork("exit");
				networkRW.close();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				networkRW.close();
			} finally {
				if(null != p)
					p.destroy();
			}
		}
	}
	
	public Process startEngine(String enginePath) throws IOException {
		Process p = Runtime.getRuntime().exec(enginePath);
		return p;
	}

	@Override
	public void run() {
		try {
			while(!exit)
				listen();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}

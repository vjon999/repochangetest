package com.chess.uci.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

import com.chess.uci.share.AsyncReader;
import com.chess.uci.share.AsyncWriter;
import com.chess.uci.share.NetworkRW;
import com.chess.uci.share.TCPNetworkRW;

public class EngineServer extends Server implements Runnable {

	private static Logger LOG = Logger.getLogger(EngineServer.class.getName());
	
	private NetworkRW networkRW;
	public boolean exit = false;
	private ServerSocket serverSocket;
	
	public EngineServer(Server server) throws IOException {
		this.id = server.id;
		this.name = server.name;
		this.path = server.path;
		this.command = server.command;
		this.port = server.port;
		this.running = server.running;
		File file = new File(path);
		if(!file.exists()) {
			throw new FileNotFoundException("Invalid Engine Path"+path);
		}
		serverSocket = new ServerSocket(port);
	}
	
	public void listen() throws IOException {		
		LOG.info(name+" -> TCP server port: "+port);
		LOG.info("waiting for connection on Engine port: "+serverSocket.getLocalPort());
		LOG.info("Engine: "+path);
		networkRW = new TCPNetworkRW(serverSocket.accept());
		LOG.fine("\n--------------------------------------Start Server ----------------------------------\n");
		Process p = null;
		if(!networkRW.isClosed() && networkRW.isConnected()) {
			try {
				p = startEngine(path);
				Thread guiToEngineWriterThread = new Thread(new AsyncReader(p.getOutputStream(), networkRW, exit));
				Thread engineToGUIWriterThread = new Thread(new AsyncWriter(p.getInputStream(), networkRW, exit));
				// writer.setDaemon(true);
				guiToEngineWriterThread.start();
				engineToGUIWriterThread.start();

				if(guiToEngineWriterThread.isAlive())
					guiToEngineWriterThread.join();
				if(engineToGUIWriterThread.isAlive())
					engineToGUIWriterThread.join();

				LOG.fine("Closing Engine on port "+networkRW.getPort());
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

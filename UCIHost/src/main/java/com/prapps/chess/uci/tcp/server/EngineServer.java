package com.prapps.chess.server.uci.tcp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

import com.prapps.chess.uci.share.AsyncReader;
import com.prapps.chess.uci.share.AsyncWriter;
import com.prapps.chess.uci.share.NetworkRW;
import com.prapps.chess.uci.share.TCPNetworkRW;

public class EngineServer extends Server implements Runnable {

	private static Logger LOG = Logger.getLogger(EngineServer.class.getName());
	
	private NetworkRW networkRW;
	public volatile boolean stateClosing = false;
	private ServerSocket serverSocket;
	private Thread guiToEngineWriterThread;
	private Thread engineToGUIWriterThread;
	
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
				p = startEngine(path, command);
				guiToEngineWriterThread = new Thread(new AsyncReader(p.getOutputStream(), networkRW, stateClosing));
				engineToGUIWriterThread = new Thread(new AsyncWriter(p.getInputStream(), networkRW, stateClosing));
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
			} catch (IOException e) {
				e.printStackTrace();
				networkRW.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if(null != p)
					p.destroy();
			}
		}
	}
	
	public Process startEngine(String enginePath, String command) throws IOException {
		Process p = null;
		if(null == command || "".equals(command)) {
			 p = Runtime.getRuntime().exec(enginePath);
		}
		else {
			p = new ProcessBuilder(command, enginePath).start();
		}
		return p;
	}

	public void run() {
		try {
			while(!stateClosing)
				listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
		running = false;
		LOG.info(name + "Engine Server closed");
	}
	
	public void close() {
		if(null  != guiToEngineWriterThread)
			guiToEngineWriterThread.interrupt();
		if(null != engineToGUIWriterThread)
			engineToGUIWriterThread.interrupt();
		if(null != guiToEngineWriterThread && null != engineToGUIWriterThread) {
			if(!engineToGUIWriterThread.isAlive() && !guiToEngineWriterThread.isAlive()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

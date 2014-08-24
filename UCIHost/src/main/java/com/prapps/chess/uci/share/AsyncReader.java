package com.prapps.chess.uci.share;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;


public class AsyncReader implements Runnable {

	private static Logger LOG = Logger.getLogger(AsyncReader.class.getName());
	
	private NetworkRW networkRW;
	private volatile boolean stop = false;
	private OutputStream os;
	
	public AsyncReader(OutputStream os, NetworkRW networkRW, Boolean exit) {
		this.os = os;
		this.networkRW = networkRW;
	}
	
	public void run() {
		StringBuffer message = new StringBuffer();
		String msg = null;
		try {
			while (!stop && (msg = networkRW.readFromNetwork()) != null) {
				message.append(msg);
				if(message.lastIndexOf("\n") != message.length()-1) {
					message.append("\n");
				}
				LOG.finer("client: " + message+"\tLength: "+message);
				os.write(message.toString().getBytes());
				os.flush();
				message = new StringBuffer();
			}
		} catch (IOException e) {
			e.printStackTrace();
			stop = true;
			try {
				os.write("quit\n".getBytes());
				os.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}	
		}
		LOG.info("Closing "+getClass().getName());
	}
	
	public void stop() {
		stop = true;
	}
}

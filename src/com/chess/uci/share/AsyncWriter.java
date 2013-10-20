package com.chess.uci.share;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;

import com.util.ProtocolConstants;

public class AsyncWriter implements Runnable {

	private static Logger LOG = Logger.getLogger(AsyncWriter.class.getName());
	
	private InputStream is;
	private NetworkRW networkRW;

	public AsyncWriter(InputStream is, NetworkRW networkRW, Boolean exit) {
		this.networkRW = networkRW;
		this.is = is;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[ProtocolConstants.BUFFER_SIZE/2];
		try {
			if(null != is) {
				int readLen = buffer.length;
				while((readLen = is.read(buffer, 0, buffer.length)) != -1) {
					LOG.finer("Server: "+new String(buffer, 0, readLen));
					networkRW.writeToNetwork(Arrays.copyOfRange(buffer, 0, readLen));
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		LOG.info("Engine process closed");
		try {
			networkRW.writeToNetwork("exit");
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOG.info("closing writer");
	}
	
	public void stop() {
	}
}

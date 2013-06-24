package udp;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

import server.Consts;

import com.util.UCIUtil;

public class AsyncDatagramWriter implements Runnable {

	private static Logger LOG = Logger.getLogger(AsyncDatagramWriter.class.getName());
	
	private InputStream is;
	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	private byte[] password;

	public AsyncDatagramWriter(InputStream is, DatagramSocket socket, InetAddress address, int port, Boolean exit) {
		this.socket = socket;
		this.is = is;
		this.address = address;
		this.port = port;
	}
	
	public AsyncDatagramWriter(InputStream is, DatagramSocket socket, InetAddress address, int port, Boolean exit, byte[] password) {
		this.socket = socket;
		this.is = is;
		this.address = address;
		this.port = port;
		this.password=password;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[Consts.BUFFER_SIZE/2];
		try {
			if(null != is) {
				int readLen = buffer.length;
				while((readLen = is.read(buffer, 0, buffer.length)) != -1) {
						UCIUtil.sendPacket(buffer, 0, readLen, password, socket, address, port);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		LOG.info("closing writer");
	}
	
	public void stop() {
	}
}

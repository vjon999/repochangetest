package udp;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

import server.Consts;

public class AsyncDatagramWriter implements Runnable {

	private static Logger LOG = Logger.getLogger(AsyncDatagramWriter.class.getName());
	
	private InputStream is;
	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	private volatile boolean stop = false;
	private Boolean exit;

	public AsyncDatagramWriter(InputStream is, DatagramSocket socket, InetAddress address, int port, Boolean exit) {
		this.socket = socket;
		this.is = is;
		this.address = address;
		this.port = port;
		this.exit = exit;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[Consts.BUFFER_SIZE];
		DatagramPacket packet;
		try {
			if(null != is) {
				int readLen = buffer.length;
				/*while (!stop && (readLen = is.read(buffer, 0, buffer.length)) > 0) {
					LOG.debug(getClass().getName()+":"+new String(buffer, 0, readLen));
					packet = new DatagramPacket(buffer, readLen, address, port);
					socket.send(packet);
				}*/
				//int totalReadLen = 0;
				while((readLen = is.read(buffer, 0, buffer.length)) != -1) {
					//totalReadLen += readLen;
					/*if(exit && totalReadLen > 10) {
						LOG.info("infinite case");
						packet = new DatagramPacket(buffer, readLen, address, port);
						socket.send(packet);
						totalReadLen = 0;
					}
					else {*/
						packet = new DatagramPacket(buffer, readLen, address, port);
						socket.send(packet);
					//}
					//LOG.debug(getClass().getName()+":"+new String(buffer, 0, readLen));
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		LOG.info("closing writer");
	}
	
	public void stop() {
		stop = false;
	}
}

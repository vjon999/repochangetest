package udp;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server.Consts;

public class AsyncDatagramWriter implements Runnable {

	private InputStream is;
	private DatagramSocket socket;
	private InetAddress address;
	private int port;
	private volatile boolean stop = false;

	public AsyncDatagramWriter(InputStream is, DatagramSocket socket, InetAddress address, int port) {
		this.socket = socket;
		this.is = is;
		this.address = address;
		this.port = port;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[Consts.BUFFER_SIZE];
		DatagramPacket packet;
		try {
			if(null != is) {
				int readLen = buffer.length;
				while (!stop && (readLen = is.read(buffer, 0, buffer.length)) != -1) {
					packet = new DatagramPacket(buffer, readLen, address, port);
					socket.send(packet);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println("closing writer");
	}
	
	public void stop() {
		stop = false;
	}
}

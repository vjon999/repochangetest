package udp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.apache.log4j.Logger;

import server.Consts;

public class AsyncDatagramReader implements Runnable {

	private static Logger LOG = Logger.getLogger(AsyncDatagramReader.class);
	
	private DatagramSocket socket;
	private volatile boolean stop = false;
	private OutputStream os;
	private DatagramPacket firstPacket;
	
	public AsyncDatagramReader(OutputStream os, DatagramSocket socket,DatagramPacket packet) {
		this.os = os;
		this.socket = socket;
		this.firstPacket = packet;
	}
	
	@Override
	public void run() {
		byte[] buf = new byte[Consts.BUFFER_SIZE];
		String message = null;
		try {
			os.flush();
			message = new String(firstPacket.getData(), 0, firstPacket.getLength());
			LOG.debug("message: "+message);
			os.write(firstPacket.getData(), 0, firstPacket.getLength());
			os.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		 while(!stop) {
			 DatagramPacket packet = new DatagramPacket(buf, buf.length);
	         try {
				socket.receive(packet);
				message = new String(firstPacket.getData(), 0, firstPacket.getLength());
				LOG.debug("message: "+message);
				os.write(packet.getData(), 0, packet.getLength());
				os.flush();
				if("quit\n".contains(new String(packet.getData(), 0, packet.getLength()))) {
					stop = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		 }
		 LOG.info("Closing "+getClass().getName());
	}
	
	public void stop() {
		stop = true;
	}
}

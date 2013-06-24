package udp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Logger;

import server.Consts;

public class AsyncDatagramReader implements Runnable {

	private static Logger LOG = Logger.getLogger(AsyncDatagramReader.class.getName());
	
	private DatagramSocket socket;
	private volatile boolean stop = false;
	private OutputStream os;
	private DatagramPacket firstPacket;
	private Boolean exit;
	
	public AsyncDatagramReader(OutputStream os, DatagramSocket socket,DatagramPacket packet, Boolean exit) {
		this.os = os;
		this.socket = socket;
		this.firstPacket = packet;
		this.exit = exit;
	}
	
	@Override
	public void run() {
		byte[] buf = new byte[Consts.BUFFER_SIZE];
		StringBuffer message = new StringBuffer();
		/*try {
			os.flush();
			message = new String(firstPacket.getData(), 0, firstPacket.getLength());
			LOG.debug("Chessbase: "+message);
			os.write(message.getBytes(), 0, message.length());
			os.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		while (!stop) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
				message.append(new String(packet.getData(), 0, packet.getLength()));
				/*if(message.indexOf("go infinite") != -1) {
					infinite = true;
				}
				else {
					infinite = false;
				}*/
				LOG.info("Chessbase: " + message);
				if(message.lastIndexOf("\n") != message.length()-1) {
					message.append("\n");
				}
				os.write(message.toString().getBytes(), 0, message.length());
				os.flush();
				if (message.indexOf("quit") != -1) {
					stop = true;
					exit = true;
				}
				message = new StringBuffer();
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

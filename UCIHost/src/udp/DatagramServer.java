package udp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;

import server.Consts;

public class DatagramServer {

	public static final int DEFAULT_PORT = 8888;
	private DatagramSocket datagramSocket = null;
	private Properties config;
	private boolean exit = false;
	
	public DatagramServer() throws FileNotFoundException, IOException {
		config = new Properties();
		config.load(new FileInputStream("config.ini"));
		datagramSocket = new DatagramSocket(DEFAULT_PORT);	
		System.out.println("Port number: "+DEFAULT_PORT+" "+datagramSocket.getLocalSocketAddress());
	}

	public void start() {
		byte[] buffer = new byte[Consts.BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		Process p = null;
		while(!exit) {
			try {
				String path = "H:/languages/Chess Projects/UCIServer/ChessEngines/Houdini_20_x64.exe";			
				datagramSocket.receive(packet);
				
				p = Runtime.getRuntime().exec(path);
				
				System.out.println("Connection received from "+ packet.getAddress().getHostName());
				Thread reader = new Thread(new AsyncDatagramReader(p.getOutputStream(), datagramSocket, packet));
				Thread writer = new Thread(new AsyncDatagramWriter(p.getInputStream(), datagramSocket, packet.getAddress(), packet.getPort()));
				writer.setDaemon(true);
				reader.start();
				writer.start();
				
				if(reader.isAlive()) reader.join();
				//if(writer.isAlive()) writer.join();
				
				System.out.println("Closing");
				DatagramPacket newPacket = new DatagramPacket("exit".getBytes(), 4, packet.getAddress(), packet.getPort());
				datagramSocket.send(newPacket);
				System.out.println("Closed");
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				p.destroy();
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		DatagramServer server = new DatagramServer();
		server.start();
	}
	
	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}
}

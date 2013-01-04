package udp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;

import org.apache.log4j.Logger;

import server.Consts;

public class DatagramServer {

	private static Logger LOG = Logger.getLogger(DatagramServer.class);
	
	public static final int DEFAULT_PORT = 11000;
	private DatagramSocket datagramSocket = null;
	private Properties config;
	private boolean exit = false;
	private String enginePath;
	private int port;
	
	public DatagramServer(String enginePath, int port) throws FileNotFoundException, IOException {
		config = new Properties();
		this.port = port;
		config.load(new FileInputStream("config.ini"));
		datagramSocket = new DatagramSocket(port);
		this.enginePath = enginePath;
		System.out.println("Port number: "+DEFAULT_PORT+" "+datagramSocket.getLocalSocketAddress());
	}

	public void start() {
		byte[] buffer = new byte[Consts.BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		Process p = null;
		while(!exit) {
			try {
				//String path = "H:/languages/Chess Projects/UCIServer/ChessEngines/Houdini_20_x64.exe";			
				datagramSocket.receive(packet);				
				
				p = Runtime.getRuntime().exec(enginePath);
				
				LOG.debug("Connection received from "+ packet.getAddress().getHostName());
				Thread reader = new Thread(new AsyncDatagramReader(p.getOutputStream(), datagramSocket, packet));
				Thread writer = new Thread(new AsyncDatagramWriter(p.getInputStream(), datagramSocket, packet.getAddress(), packet.getPort()));
				writer.setDaemon(true);
				reader.start();
				writer.start();
				
				if(reader.isAlive()) reader.join();
				if(writer.isAlive()) writer.join();
				
				LOG.info("Closing Engine");
				DatagramPacket newPacket = new DatagramPacket("exit".getBytes(), 4, packet.getAddress(), packet.getPort());
				datagramSocket.send(newPacket);
				LOG.info("Closed Engine");
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			finally {
				p.destroy();
			}
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		//System.getProperties().put("-Dlog4j.configuration", "H:/languages/Java/Java Projects/Eclipse Projects/UCIHost/src/log4j.properties");
		LOG.info("server started");
		DatagramServer server = new DatagramServer("H:/languages/Chess Projects/UCIServer/ChessEngines/Houdini_20_x64.exe", DEFAULT_PORT);
		server.start();
		
	}
	
	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}
}

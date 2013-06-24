package udp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.MessagingException;

import server.Consts;

public class DatagramServer {

	private static Logger LOG = Logger.getLogger(DatagramServer.class.getName());
	
	public static final int DEFAULT_PORT = 11000;
	private DatagramSocket datagramSocket = null;
	private Properties config;
	public Boolean exit = false;
	private String enginePath;
	private int port;
	private boolean started = false;
	
	private Boolean infinite = false;
	private InetAddress clientAddress;
	
	public DatagramServer(String enginePath, int port) throws FileNotFoundException, IOException, MessagingException {
		config = new Properties();
		config.load(new FileInputStream("config.ini"));
		//clientAddress = InetAddress.getByName(IPGetter.getIPFromMail());
		clientAddress = InetAddress.getByName(config.getProperty("client_ip"));//117.194.208.151//localhost
		//System.out.println("clientAddress: " + clientAddress.getHostAddress());
		this.port = port;
		datagramSocket = new DatagramSocket(11000);
		//datagramSocket.setReuseAddress(true);
		//datagramSocket.bind(new InetSocketAddress("192.168.1.2", 11000));
		System.out.println(datagramSocket.getLocalPort());
		this.enginePath = enginePath;
		//LOG.info("Port number: "+DEFAULT_PORT+" "+datagramSocket.getLocalSocketAddress());
	}

	public void start() {
		byte[] buffer = new byte[Consts.BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		Process p = null;
		DatagramPacket newPacket = null;
		boolean init = false;
		String sentMsg = null;
		String receivedText = null;

		try {
			while (!exit) {
				//String path = "wine /media/pratik/Education/languages/ChessProjects/Rybka_4_2/Houdini_15a_x64_1.exe";
				
				/*newPacket = new DatagramPacket("helloclient".getBytes(), "helloclient".length(), clientAddress, 12000);
				System.out.println("sending to "+clientAddress+":"+newPacket.getPort());
				datagramSocket.send(newPacket);*/
				
				if(init) {
					String path = "H:/languages/ChessProjects/Rybka_4_2/Houdini_15a_x64_1.exe";
					if(!started) {
						//datagramSocket.receive(packet);
						p = Runtime.getRuntime().exec(path);
						started = true;
						LOG.fine("Connection received from " + packet.getAddress().getHostName());
					}
					
					Thread reader = new Thread(new AsyncDatagramReader(p.getOutputStream(), datagramSocket, packet,
							infinite));
					Thread writer = new Thread(new AsyncDatagramWriter(p.getInputStream(), datagramSocket,
							packet.getAddress(), packet.getPort(), infinite));
					// writer.setDaemon(true);
					reader.start();
					writer.start();

					if (reader.isAlive())
						reader.join();
					if (writer.isAlive())
						writer.join();

					LOG.info("Closing Engine");
					newPacket = new DatagramPacket("exit".getBytes(), 4, packet.getAddress(), packet.getPort());
					datagramSocket.send(newPacket);
					LOG.info("Closed Engine");
					init = false;
					p.destroy();
					started=false;
				}
				else {
					packet = new DatagramPacket(buffer, buffer.length);
					LOG.info("waiting for packet... on port "+datagramSocket.getLocalPort());
					datagramSocket.receive(packet);
					receivedText = new String(packet.getData(), 0, packet.getLength());
					LOG.info("packet recvd "+receivedText+" from "+packet.getSocketAddress());
					if(receivedText.equalsIgnoreCase("helloserver")) {
						sentMsg = "connected";
						init = true;
					}
					else if("exit".equals(receivedText)) {
						exit = true;
					}
					else if("close_engine".equals(receivedText)) {
						p.destroy();
						sentMsg = "engine closed";
					}
					else {
						sentMsg = "unknown command";
					}
					newPacket = new DatagramPacket(sentMsg.getBytes(), sentMsg.length(), packet.getAddress(),packet.getPort());
					datagramSocket.send(newPacket);
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			p.destroy();
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, MessagingException {
		//System.getProperties().put("-Dlog4j.configuration", "H:/languages/Java/Java Projects/Eclipse Projects/UCIHost/src/log4j.properties");
		LOG.info("server started");
		DatagramServer server = new DatagramServer("C:/Users/Pratik/Desktop/Debug/Rybka_4_2/Houdini_20_x64", DEFAULT_PORT);
		server.start();
		
	}
	
	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}
}

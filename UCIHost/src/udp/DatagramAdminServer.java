package udp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

import server.Consts;
import server.ProtocolConsts;
import util.UCIUtil;

public class DatagramAdminServer implements ProtocolConsts {

	private DatagramSocket datagramSocket = null;
	private Properties config;
	private boolean exit = false;
	
	public DatagramAdminServer() throws IOException {
		config = new Properties();
		config.load(new FileInputStream("config.ini"));
		datagramSocket = new DatagramSocket(DEFAULT_ADMIN_PORT, InetAddress.getByName("127.0.0.1"));	
		System.out.println("Port number: "+DEFAULT_ADMIN_PORT+" "+datagramSocket.getLocalSocketAddress());
	}

	public void start() {		
		byte[] buffer = new byte[Consts.BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		String response = null;
		String selectedEngine = null;
		String command = null;
		String[] params = null;
		while(!exit) {
			try {				
				datagramSocket.receive(packet);
				System.out.println("Connection received from "+ packet.getAddress().getHostName());				
				response = UCIUtil.readPacket(packet);
				params = UCIUtil.getMessageParams(response);
				command = params[0];
				System.out.println(response);
				if(GET_AVAILABLE_ENGINES.equals(command)) {
					UCIUtil.sendPacket(config.getProperty(AVAILABLE_ENGINES), datagramSocket, packet);
				}
				else if(SELECTED_ENGINE.equals(command)) {
					selectedEngine = params[1];
					UCIUtil.sendPacket(SUCCESSFUL_ENGINE_SELECTION_MESSAGE+DELIMITER+config.getProperty(selectedEngine), datagramSocket, packet);
				}
				else {
					UCIUtil.sendPacket(UNKNOWN_COMMAND, datagramSocket, packet);
					System.out.println("Closed");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendHelloPacket() {
		byte[] buffer = new byte[Consts.BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
		packet.setPort(DEFAULT_ADMIN_PORT);
		packet.setAddress(datagramSocket.getInetAddress());
		try {
			System.out.println(datagramSocket.getLocalAddress());
			UCIUtil.sendPacket(HELLO_PACKET, datagramSocket, datagramSocket.getLocalAddress(), DEFAULT_ADMIN_PORT);
			packet = new DatagramPacket(buffer, buffer.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		DatagramAdminServer server = new DatagramAdminServer();
		//server.start();
		server.sendHelloPacket();
	}
	
	public DatagramSocket getDatagramSocket() {
		return datagramSocket;
	}

}

package com.udp.cb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;
import java.util.logging.Logger;

import server.Consts;

public class ChessbaseAdapter {

	private static Logger LOG = Logger.getLogger(ChessbaseAdapter.class.getName());
	public static final int MAX_CONN_ATTEMPT = 5;
	public static final String START_MSG = "helloserver";
	public static final String 	CONN_SUCCESS_MSG = "connected";
	
	private DatagramSocket datagramSocket = null;
	private Properties config;
	private boolean exit = false;
	private int targetPort;
	private Thread cbReader;
	private Thread cbWriter;
	private InputStream consoleInputStream = System.in;
	private InetAddress targetAddress;
	private byte[] pass;
	
	private void listen() throws FileNotFoundException, IOException, InterruptedException {
		config = new Properties();
		config.load(new FileInputStream("clientConfig.ini"));
		targetAddress = InetAddress.getByName(config.getProperty("target_ip"));
		targetPort = Integer.parseInt(config.getProperty("target_port"));
		pass = config.getProperty("secretKey").getBytes();
		datagramSocket = new DatagramSocket();
		
		if(!connect()) {
			System.out.print("Cannot connect to server "+targetAddress+":"+targetPort);
		}
		
		cbWriter = new Thread(new Runnable() {
			
			@Override
			public void run() {
				byte[] buf = new byte[Consts.BUFFER_SIZE];
				OutputStream os = System.out;
				DatagramPacket inputPacket;
				while (!exit) {
					inputPacket = new DatagramPacket(buf, buf.length);
					try {
						datagramSocket.receive(inputPacket);
						os.write(inputPacket.getData(), 0, inputPacket.getLength());
						os.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				LOG.info("Closing CB Writer");
				try {
					consoleInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		cbReader = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					handle();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			public void handle() throws InterruptedException {
				byte[] buffer = new byte[Consts.BUFFER_SIZE];
				DatagramPacket outputPacket;
				try {
					int readLen = buffer.length;
					while(!exit && (readLen = consoleInputStream.read(buffer, 0, buffer.length)) != -1) {
						//LOG.info(new String(buffer, 0, readLen));
							outputPacket = new DatagramPacket(buffer, 0, readLen, targetAddress, targetPort);
							datagramSocket.send(outputPacket);
						if(new String(buffer, 0, readLen).contains("quit"))
							exit=true;
					}
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
				LOG.info("closing cbReader");
			}
		});
		
		cbReader.start();
		cbWriter.start();
		
		
		if (cbReader.isAlive())
			cbReader.join();
		if (cbWriter.isAlive())
			cbWriter.join();
		
		LOG.finest("Closing CB Adapter");
	}
	
	public String udpToString(DatagramPacket p) {
		return new String(p.getData(), 0, p.getLength());
	}
	
	public boolean connect() throws IOException {
		DatagramPacket connPacket = null;
		String connResp = null;
		int connAttepmt = 0;
		do {
			connPacket = new DatagramPacket(START_MSG.getBytes(), 0, START_MSG.length(), targetAddress, targetPort);
			datagramSocket.send(connPacket);
			datagramSocket.receive(connPacket);
			connAttepmt++;
			LOG.fine("sent connection initator packet :"+START_MSG+" to "+targetAddress+":"+targetPort);
			connResp = udpToString(connPacket);
			LOG.fine("Server :"+connResp);
			if(connResp.equalsIgnoreCase(CONN_SUCCESS_MSG))
				return true;
		}while(connAttepmt < MAX_CONN_ATTEMPT);
		
		return false;
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		ChessbaseAdapter adapter = new ChessbaseAdapter();
		adapter.listen();
	}

}

package com.prapps.client.tcp.cb;

import static com.prapps.chess.common.engines.ProtocolConstants.CLOSE_MSG;
import static com.prapps.chess.common.engines.ProtocolConstants.CONN_SUCCESS_MSG;
import static com.prapps.chess.common.engines.ProtocolConstants.QUIT_MSG;
import static com.prapps.chess.common.engines.ProtocolConstants.SET_PROTOCOL_TCP;
import static com.prapps.chess.common.engines.ProtocolConstants.START_MSG;
import static com.prapps.chess.common.engines.ProtocolConstants.SUCCESS_SET_PROTOCOL_TCP;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

import com.prapps.chess.common.engines.ProtocolConstants;
import com.prapps.chess.uci.share.NetworkRW;
import com.prapps.chess.uci.share.TCPNetworkRW;

public class ChessbaseClientAdapter {

	private static Logger LOG = Logger.getLogger(ChessbaseClientAdapter.class.getName());
	public static final int MAX_CONN_ATTEMPT = 5;
	

	private NetworkRW networkRW;
	private Properties config;
	private boolean exit = false;
	private int adminPort;
	private int targetPort;
	private Thread cbReader;
	private Thread cbWriter;
	private InputStream consoleInputStream = System.in;
	private InetAddress targetAddress;
	private byte[] password;

	private void listen() throws FileNotFoundException, IOException, InterruptedException {
		
		config = new Properties();
		config.load(new FileInputStream("src/test/resources/clientConfig.ini"));
		adminPort = Integer.parseInt(config.getProperty("admin_port"));
		/*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					System.out.println("isConnected "+networkRW.isConnected());
					System.out.println("isClosed "+networkRW.isClosed());
					if(networkRW.isConnected() && !networkRW.isClosed())
						close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));*/
		/*if(null != config.getProperty("auto_config") && "true".equalsIgnoreCase(config.getProperty("auto_config"))) {
			try {
				String params[] = UCIUtil.getIPFromMail(config.getProperty("clientMail"), config.getProperty("clientMailPass")).split(":");
				targetAddress = InetAddress.getByName(UCIUtil.getParam(params, 0));				
				targetPort = Integer.parseInt(UCIUtil.getParam(params, 1))+Integer.parseInt(config.getProperty("target_port"));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			targetAddress = InetAddress.getByName(config.getProperty("target_ip"));
		}*/
		targetAddress = InetAddress.getByName(config.getProperty("target_ip"));
		targetPort = adminPort + Integer.parseInt(config.getProperty("target_port"));
		
		LOG.info("server: "+targetAddress.getHostName()+"\tport: "+targetPort);	
		//password = config.getProperty("secretKey").getBytes();
		password = "test123".getBytes();
		

		/*if (!connect()) {
			System.out.print("Cannot connect to server " + targetAddress + ":" + targetPort);
		}
		else {
			LOG.finest("connected");
		}*/

		networkRW = new TCPNetworkRW(new Socket(targetAddress, targetPort));
		cbWriter = new Thread(new Runnable() {

			public void run() {
				String serverMsg = null;
				while (!exit) {
					try {
						serverMsg = networkRW.readFromNetwork();
						LOG.finest("Server: "+serverMsg);
						if("exit".equalsIgnoreCase(serverMsg)) {
							networkRW.close();
							exit = true;
						}
						else {
							System.out.write(serverMsg.getBytes());
							System.out.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				LOG.finest("Closing CB Writer");
				try {
					consoleInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		cbReader = new Thread(new Runnable() {

			public void run() {
				byte[] buffer = new byte[ProtocolConstants.BUFFER_SIZE];
				try {
					int readLen = buffer.length;
					/*String line = null;
					BufferedReader reader = new BufferedReader(new InputStreamReader(consoleInputStream));
					while(!exit && (line = reader.readLine()) != null) {
						LOG.finest("Chessbase: "+line);
						networkRW.writeToNetwork(line);
					}*/
					while (!exit && (readLen = consoleInputStream.read(buffer, 0, buffer.length)) != -1) {
						LOG.finest("Chessbase: "+new String(buffer, 0, readLen));
						networkRW.writeToNetwork(Arrays.copyOfRange(buffer, 0, readLen));
						if (new String(buffer, 0, readLen).contains("quit")) {
							exit = true;
						}
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				LOG.finest("closing cbReader");
			}

		});

		cbReader.start();
		cbWriter.start();

		/*if (cbReader.isAlive())
			cbReader.join();*/
		if (cbWriter.isAlive())
			cbWriter.join();

		cbWriter.stop();
		LOG.finest("Closing CB Adapter");
	}

	public String udpToString(DatagramPacket p) {
		return new String(p.getData(), 0, p.getLength());
	}

	public boolean connect() throws IOException {
		String connResp = null;
		 NetworkRW adminNetworkRW = new TCPNetworkRW(new Socket(targetAddress, targetPort));
		int connAttepmt = 0;
		do {
			connAttepmt++;
			adminNetworkRW.writeToNetwork(START_MSG);
			LOG.fine("sent connection initator packet :" + START_MSG + " to " + targetAddress + ":" + targetPort);
			connResp = adminNetworkRW.readFromNetwork();
			LOG.fine("Server :" + connResp);
			if (connResp.equalsIgnoreCase(CONN_SUCCESS_MSG)) {
				adminNetworkRW.writeToNetwork(SET_PROTOCOL_TCP);
				connResp = adminNetworkRW.readFromNetwork();
				if(SUCCESS_SET_PROTOCOL_TCP.equalsIgnoreCase(connResp)) {
					adminNetworkRW.writeToNetwork("exit");
					adminNetworkRW.close();
					return true;
				}
			}
		} while (connAttepmt < MAX_CONN_ATTEMPT);

		return false;
	}
	
	public void close() throws IOException {
		LOG.fine("closing client packet :" + QUIT_MSG + " to " + targetAddress + ":" + targetPort);
		networkRW.writeToNetwork(QUIT_MSG);
		LOG.fine("closing client packet :" + CLOSE_MSG + " to " + targetAddress + ":" + targetPort);
		networkRW.writeToNetwork(CLOSE_MSG);
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		/*System.out.println(System.getProperty("user.dir"));
		System.getProperties().put("Djava.util.logging.config.file", System.getProperty("user.dir")+"/logging.properties");*/
		LOG.finest("JAVA_HOME: "+System.getProperty("java.home"));
		LOG.finest("Logger initialized");
		LOG.finest("\n-------------------------------Starting UCIClient ---------------------------------------\n");
		ChessbaseClientAdapter adapter = new ChessbaseClientAdapter();
		adapter.listen();
	}

}

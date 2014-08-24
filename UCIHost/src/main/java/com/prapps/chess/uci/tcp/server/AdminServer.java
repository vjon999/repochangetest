package com.prapps.chess.uci.tcp.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import com.prapps.chess.uci.share.NetworkRW;
import com.prapps.chess.uci.share.TCPNetworkRW;

public class AdminServer implements Runnable {

	private static Logger LOG = Logger.getLogger(AdminServer.class.getName());
	
	private static ServerConfig serverConfig;
	protected Server[] server;
	//protected static Properties config;
	protected int cores = -1;
	protected static int adminPort;
	public boolean exit;
	protected boolean engineStarted = false;
	protected boolean connected = false;
	protected Process p;
	protected ServerSocket adminServerSocket;
	protected NetworkRW adminNetworkRW;

	protected String enginePath;
	protected char[] password;
	protected static List<EngineServer> servers = new ArrayList<EngineServer>();
	
	protected Thread guiToEngineWriterThread;
	protected Thread engineToGUIWriterThread;
	
	public AdminServer() {
		// TODO Auto-generated constructor stub
	}

	public AdminServer(int port) throws FileNotFoundException, IOException {
		cores = Runtime.getRuntime().availableProcessors();
		LOG.info("available cores: " + cores);
		if (cores > 1)
			cores = cores - 1;
		LOG.info("usable cores: " + cores);
		//enginePath = config.getProperty(String.valueOf(port));
	}

	public void staticInitServer() throws IOException {
		//AdminServer.config = config;
		LOG.info("server starting");
		adminPort = serverConfig.getAdminPort();
		//protocol = config.getProperty("protocol");
		LOG.info("protocol type: "+serverConfig.getProtocol());
		LOG.info("admin port: "+adminPort);
		adminServerSocket = new ServerSocket(adminPort);
		
		/*try {
			LOG.info(UCIUtil.getExternalIP());
			UCIUtil.mailExternalIP(UCIUtil.getExternalIP() + ":admin_port=" + adminPort, serverConfig.getFromMail(),serverConfig.getMailPass(), serverConfig.getToMail());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		new Thread(new Runnable() {

			public void run() {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						LOG.fine("Admin command: "+line);
						if (line.startsWith(":q")) {
							exit = true;
							for (EngineServer s : servers) {
								s.exit = true;
							}
							
							break;
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		LOG.info("Admin server started");
		/*while(!exit)
			handshake();*/
		initEngines();
		LOG.fine("Admin server closed");
	}
	
	public Process startEngine() throws IOException {
		if (!engineStarted) {
			Process p = Runtime.getRuntime().exec(enginePath);
			//p.getOutputStream().write(("setoption name Max CPUs value "+cores+"\nsetoption name CPU Usage value 100\n").getBytes());
			p.getOutputStream().write(("setoption name Threads value "+cores+"\nsetoption name CPU Usage value 100\n").getBytes());
			engineStarted = true;
			return p;
		}
		return null;
	}
	
	public void close() {
		LOG.info("Closed Engine");
		connected = false;
		p.destroy();
		engineStarted = false;
	}
	
	public void handshake() throws IOException {
		adminNetworkRW = new TCPNetworkRW(adminServerSocket.accept());
		LOG.info("waiting for connection...");
		LOG.info("Connection received from " + adminNetworkRW.getAddress().getHostName());
		String request = null;
		String protocol = serverConfig.getProtocol();
		do {
			request = adminNetworkRW.readFromNetwork();
			LOG.fine("Client: " + request);
			String sentMsg = null;
			if (request.equalsIgnoreCase("helloserver")) {
				sentMsg = "connected";
				connected = true;
			} 
			else if (request.startsWith("protocol=")) {
				protocol = request.split("=")[1];
				if ("tcp".equalsIgnoreCase(protocol)) {
					if(!engineStarted)
						initEngines();
					sentMsg = "tcp protocol set";
				} else if ("udp".equalsIgnoreCase(protocol)) {
					sentMsg = "udp protocol set";
				} else {
					sentMsg = "unknown protocol set";
				}
			} else if ("exit".equals(request)) {
				adminNetworkRW.close();
				//exit = true;
			} else if ("close_engine".equals(request)) {
				p.destroy();
				sentMsg = "engine closed";
			} else {
				sentMsg = "unknown command";
			}
			if(null != sentMsg) {
				LOG.info("Server: " + sentMsg);
				adminNetworkRW.writeToNetwork(sentMsg);
			}
		} while (null != request && !"exit".equals(request));
	}
	
	public static void main(String[] args) throws IOException, JAXBException {
		System.getProperties().put("-Djava.util.logging.config.file", "h:/logging.properties");
		System.out.println(System.getProperty("java.home"));
		LOG.info("Admin Server : Log initialized");
		InputStream is = AdminServer.class.getClassLoader().getResourceAsStream("serverConfig.xml");		
		ServerConfigUtil serverConfigUtil = new ServerConfigUtil(is);
		serverConfig = serverConfigUtil.getServerConfig();
		
		/*Properties config = new Properties();
		config.load(new FileInputStream("config.ini"));*/
		AdminServer server = new AdminServer();
		server.staticInitServer();
		LOG.info("Admin Server closed");
	}
	
	private void initEngines() throws IOException {
		/*int count = 0;
		for (int port = 0; port <= 10; port++) {
			if (config.containsKey(String.valueOf(port))) {
				if("tcp".equalsIgnoreCase(protocol)) {
					LOG.info("Starting Engine Server on port: "+(adminPort+port));
					servers.add(new EngineServer((adminPort+port), config.getProperty(port+"")));
				}
				else {
					//servers.add(new DatagramServer(port));
				}				
				new Thread(servers.get(count)).start();
				count++;
			}
		}*/
		for(Server server : serverConfig.getServers()) {
			EngineServer engineServer = new EngineServer(server);
			servers.add(engineServer);
			new Thread(engineServer).start();
			LOG.info("server listening: "+server);
		}
		engineStarted = true;
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}
}

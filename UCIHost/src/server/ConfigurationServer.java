package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import personalize.User;
import engines.Engine;

public class ConfigurationServer extends AbstractSocketServer {
	
	private Map<String, Engine> engines = new HashMap<String, Engine>(5);
	private Engine selectedEngine;
	private boolean loop = true;
	private User user;
	private boolean authenticated;
	
	public ConfigurationServer() throws IOException {
		config = new Properties();
		config.load(new FileInputStream("config.ini"));
		for(String engine:((String)config.getProperty(Consts.AVAILABLE_ENGINES_KEY)).split(",")) {
			engine = engine.trim();
			if(null != config.getProperty(engine)) {
				engines.put(engine, new Engine(engine, config.getProperty(engine)));
			}
		}
	}
	
	public static void main(String[] arg) throws IOException {
		ConfigurationServer configurationServer = new ConfigurationServer();		
		configurationServer.start(configurationServer);
	}
	
	@Override
	public void run() {
		Socket clientSocket = null;
		try {
			serverSocket = new ServerSocket(DEFAULT_CONFIGURATION_PORT);
			while (connectionCount < maxConnectionsServable) {
				System.out.println("Configuration Server started, waiting for connection..."+selectedEngine);
				clientSocket = serverSocket.accept();
				connectionCount++;
				System.out.println("Connection received from " + clientSocket.getInetAddress().getHostName());
				try {
					handleConversation(clientSocket);
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					try {
						clientSocket.close();
						System.out.println("Client socket closed");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}	
				System.out.println("Closing client socket");
				clientSocket.close();
				connectionCount--;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	private void handleConversation(Socket clientSocket) throws IOException {
		String command = null;
		OutputStream out = clientSocket.getOutputStream();
		InputStream in = clientSocket.getInputStream();
		while((command = UCIUtil.readStream(in)) != null) {
			System.out.println(command);
			if(command.equals(ProtocolConsts.GET_AVAILABLE_ENGINES)) {
				StringBuilder sb = new StringBuilder();
				for(String key : engines.keySet()) {
					sb.append(key+",");
				}
				UCIUtil.writeString(sb.toString(), out);
			}
			else if(command.indexOf(ProtocolConsts.AUTHENTICATE) == 0) {
				user = new User();
				if(UCIUtil.authenticate(command, user))
					UCIUtil.writeString(ProtocolConsts.AUTHENTICATION_SUCCESSFUL, out);
				else
					UCIUtil.writeString(ProtocolConsts.AUTHENTICATION_FAIL, out);
			}
			else if(command.equals(ProtocolConsts.GET_PORT)) {
				UCIUtil.writeString(config.getProperty(ProtocolConsts.PORT), out);
			}
			else if(command.indexOf(ProtocolConsts.SAVE_CONFIGURATION) == 0) {
				String arr[] = command.split("\\|")[1].split("=");
				System.out.println(arr[0]+"@"+arr[1]);
				File file = new File(System.getProperty("user.dir")+"/"+user.getUserName()+".ini");
				if(!file.exists())
					file.createNewFile();
				Properties properties = new Properties();
				properties.load(new FileInputStream(file));
				properties.put(ProtocolConsts.SELECTED_ENGINE, arr[1]);
				properties.store(new FileOutputStream(file), "auto save");
				UCIUtil.writeString(ProtocolConsts.SAVE_SUCCESSFUL, out);
			}
			else if(command.equals(ProtocolConsts.SELECTED_ENGINE)) {
				UCIUtil.writeString(ProtocolConsts.ENGINE_NAME, out);
				String engineName = null;
				engineName = UCIUtil.readStream(in);;
				System.out.println(engines);
				if(engineName.contains("=")) {
					selectedEngine = engines.get(engineName.split("=")[1].trim());
					System.out.println("Engine name selected is: "+selectedEngine.getPath());
					config.put(clientSocket.getInetAddress().getHostName(), selectedEngine.getName());
					config.store(new FileOutputStream("config.ini"), "");
					UCIUtil.writeString(ProtocolConsts.SUCCESSFUL_ENGINE_SELECTION_MESSAGE, out);
				}
				else {
					UCIUtil.writeString("illegal engine name", out);
				}
			}
			else if(command.equals(ProtocolConsts.CLOSE_CONNECTION)) {
				break;
			}
			else {
				UCIUtil.writeString(ProtocolConsts.UNKNOWN_COMMAND, out);
			}					
		}
		
	}
}

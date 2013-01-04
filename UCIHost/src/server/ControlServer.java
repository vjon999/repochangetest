package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import personalize.User;
import util.IPGetter;
import util.UCIUtil;

public class ControlServer implements Runnable {
	public static final int DEFAULT_PORT = 8888;
	private int connectionCount = 0;
	private int maxConnectionsServable = 50;
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private static List<Thread> threads = new ArrayList<Thread>();
	private Properties config;
	
	public ControlServer() throws FileNotFoundException, IOException {
		config = new Properties();
		config.load(new FileInputStream("config.ini"));		
	}

	public void start() {
		String response;
		try {
			serverSocket = new ServerSocket(Integer.parseInt(String.valueOf(config.get("port"))), DEFAULT_PORT);	
			System.out.println("Port number: "+config.get("port")+" "+serverSocket.getLocalSocketAddress());
			try {
				IPGetter.mailExternalIP("vickysengupta006@gmail.com");
		    } 
		    catch (Exception e)
		    {
		    	e.printStackTrace();
		    }
			Thread t;
			while (connectionCount < maxConnectionsServable) {
				System.out.println("waiting for connection...");
				clientSocket = serverSocket.accept();
				System.out.println("Connection received from " + clientSocket.getInetAddress().getHostName());
				System.out.println(clientSocket.getInetAddress().getHostName());
				System.out.println(config.getProperty(clientSocket.getInetAddress().getHostName()));
				response = UCIUtil.readStream(clientSocket.getInputStream());
				System.out.println("Client is: "+clientSocket.getRemoteSocketAddress()+":"+clientSocket.getPort());
				System.out.println("response: "+response);
				if(response.indexOf(ProtocolConsts.AUTHENTICATE) == 0) {
					User user = new User();
					if(UCIUtil.authenticate(response, user)) {
						UCIUtil.writeString(ProtocolConsts.AUTHENTICATION_SUCCESSFUL, clientSocket.getOutputStream());
						System.out.println("authenticated");
						Properties prop = new Properties();
						prop.load(new FileInputStream(System.getProperty("user.dir")+"/"+user.getUserName()+".ini"));
						String engineName = (String) prop.get(ProtocolConsts.SELECTED_ENGINE);
						System.out.println(config.get(engineName));
						t = new Thread(new EngineClient(clientSocket, (String) config.get(engineName)));
						t.start();
						threads.add(t);
						connectionCount++;
						System.out.println("connection count = "+connectionCount);
					}
					else {
						UCIUtil.writeString(ProtocolConsts.AUTHENTICATION_FAIL, clientSocket.getOutputStream());
					}
				}												
			}
			serverSocket.close();
		} catch (IOException e) {
			try {
				System.out.println(e.getMessage());
				clientSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		ControlServer server;
		try {
			server = new ControlServer();
			server.start();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String args[]) throws FileNotFoundException, IOException {
		ControlServer server = new ControlServer();
		Thread currentThread = new Thread(server);
		currentThread.start();
		
		/*ConfigurationServer configurationServer = new ConfigurationServer();
		Thread configThread = new Thread(configurationServer);
		configThread.start();*/
		
		addThread(currentThread);
		while(true) {
			try {
				Thread.sleep(1000);
				InputStream is = System.in;
				if((char)is.read() == 'x') {
					for(Thread t: threads) {
						System.out.println("closing "+t.getName());
						t.stop();
					}
					System.out.println("Closing server...");
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
	
	public static void addThread(Thread thread) {
		threads.add(thread);
	}
}

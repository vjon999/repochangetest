package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class AbstractSocketServer implements Runnable {
	public static final int DEFAULT_PORT = 10;
	public static final int DEFAULT_CONFIGURATION_PORT = 11;
	protected ServerSocket serverSocket = null;
	protected int connectionCount = 0;
	protected int maxConnectionsServable = 50;
	protected static List<Thread> threads = new ArrayList<Thread>();
	protected Properties config;
	
	public static void addThread(Thread thread) {
		threads.add(thread);
	}
	
	public void start(AbstractSocketServer abstractSocketServer) {
		Thread currentThread = new Thread(abstractSocketServer);
		currentThread.start();
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
}

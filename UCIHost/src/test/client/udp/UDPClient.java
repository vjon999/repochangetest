package test.client.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server.Consts;

public class UDPClient implements Runnable {

	private DatagramSocket socket;
	private boolean exit = false;
	
	public void init() throws IOException, InterruptedException {
		 socket = new DatagramSocket();
	        //socket.setSoTimeout(5000);
	        
	        InetAddress address = InetAddress.getByName("localhost");
	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	        Thread t = new Thread(this);
	        t.start();
	        String line = null;
	        do {
	        	line = reader.readLine() + "\n";
	        	DatagramPacket packet = new DatagramPacket(line.getBytes(), line.getBytes().length, address, 8888);
	        	socket.send(packet);
	    	}
	        while(!"quit\n".equalsIgnoreCase(line));
	        
	        while(t.isAlive())
	        	Thread.sleep(100);
	        
	        socket.close();
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		UDPClient client = new UDPClient();
		client.init();
	}
	
	@Override
	public void run() {
    	try {
    		while(!exit) {
    			byte[] buf = new byte[Consts.BUFFER_SIZE];
    			DatagramPacket packet = new DatagramPacket(buf, buf.length);
    			socket.receive(packet);
    	        String received = new String(packet.getData(), 0, packet.getLength());
    	        if("exit".equals(received))
    	        	exit = true;
    	        System.out.print(received);
    		}
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
        
	}

}

package test.client.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server.Consts;
import server.ProtocolConsts;
import util.UCIUtil;

public class UDPClient implements Runnable,ProtocolConsts {

	private DatagramSocket socket;
	private boolean exit = false;
	
	public void init() throws IOException, InterruptedException {
		 socket = new DatagramSocket();
	        //socket.setSoTimeout(5000);
	        
	        InetAddress address = InetAddress.getByName("127.0.0.1");
	        /**configure engine */
	        String response = null;
	        DatagramPacket packet = getPacket();
	        UCIUtil.sendPacket(GET_AVAILABLE_ENGINES, socket, address, DEFAULT_ADMIN_PORT);
	        socket.receive(packet);
	        response = UCIUtil.readPacket(packet);
	        System.out.println(response);
	        UCIUtil.sendPacket(SELECTED_ENGINE+DELIMITER+response.split(DELIMITER)[0], socket, address, DEFAULT_ADMIN_PORT);
	        socket.receive(packet);
	        response = UCIUtil.readPacket(packet);
	        System.out.println("recvd"+response);
	        /**configure engine done */
	        
	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	        Thread t = new Thread(this);
	        t.start();
	        String line = null;
	        do {
	        	line = reader.readLine() + "\n";
	        	UCIUtil.sendPacket(line, socket, address, DEFAULT_ADMIN_PORT);
	    	}
	        while(!"quit\n".equalsIgnoreCase(line) || !exit);
	        
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
    	        if(CLOSE_CONNECTION.equals(received)) {
    	        	System.out.print(received);
    	        	exit = true;
    	        }
    	        System.out.print(received);
    		}
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
        
	}
	
	private DatagramPacket getPacket() {
		byte[] buf = new byte[Consts.BUFFER_SIZE];
		return new DatagramPacket(buf, buf.length);
	}

}

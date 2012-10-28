package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import server.Consts;
import engines.Engine;

public class DatagramEngineServer implements Runnable {

	private Engine engine;
	private AsyncDatagramReader asyncDatagramReader;
	private AsyncDatagramWriter asyncDatagramWriter;
	DatagramSocket datagramSocket = null;
	
	public DatagramEngineServer(Engine engine) {
		this.engine = engine;
	}
	
	public void run() {
		byte[] buffer = new byte[Consts.BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);		
		
		try {
			datagramSocket = new DatagramSocket(engine.getPort());
			datagramSocket.setReuseAddress(true);
			
			while(!datagramSocket.isClosed()) {
				datagramSocket.receive(packet);
				System.out.println("Connection received from "+ packet.getAddress().getHostName());
				engine.start();
				
				asyncDatagramReader = new AsyncDatagramReader(engine.getOutputStream(), datagramSocket, packet);
				asyncDatagramWriter = new AsyncDatagramWriter(engine.getInputStream(), datagramSocket, packet.getAddress(), packet.getPort());
				Thread reader = new Thread(asyncDatagramReader);
				Thread writer = new Thread(asyncDatagramWriter);
				
				writer.setDaemon(true);
				reader.start();
				writer.start();
				
				if(reader.isAlive()) reader.join();
				//if(writer.isAlive()) writer.join();
				
				System.out.println("Closing");
				DatagramPacket newPacket = new DatagramPacket("exit".getBytes(), 4, packet.getAddress(), packet.getPort());
				datagramSocket.send(newPacket);
				System.out.println("Closed");
				engine.stop();
			}
		}
		catch(java.net.SocketException ex1) {
			//suppress
			ex1.printStackTrace();
		}
		catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			try {
				engine.stop();
				if(null != datagramSocket) {
					datagramSocket.disconnect();
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void stop() {
		try {
			engine.stop();
			if(null != datagramSocket && !datagramSocket.isClosed()) {
				System.out.println("closing port");
				datagramSocket.close();
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(null != asyncDatagramReader) {
			asyncDatagramReader.stop();
		}
		if(null != asyncDatagramWriter) {
			asyncDatagramWriter.stop();
		}
	}
}

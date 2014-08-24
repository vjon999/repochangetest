package com.prapps.chess.uci.share;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;



public class TCPNetworkRW implements NetworkRW {

	private Socket socket;
	
	public TCPNetworkRW(java.net.Socket socket) {
		this.socket = new Socket(socket);
	}
	
	public TCPNetworkRW(DatagramSocket datagramSocket, InetAddress address, int port) {
		this.socket = new Socket(datagramSocket, address, port);
	}
	
	public void writeToNetwork(String content) throws IOException {
		socket.write(content);
	}
	
	public void writeToNetwork(byte[] content) throws IOException {
		socket.write(content);
	}

	public String readFromNetwork() throws IOException {
		return socket.read();
	}
	
	public InetAddress getAddress() {
		return socket.getAddress();
	}
	
	public int getPort() {
		return socket.getPort();
	}
	
	public void close() throws IOException {
		if(null != socket) {
			socket.close();
		}
	}
	
	public boolean isConnected() {
		 return socket.isConnected();
	}
	
	public boolean isClosed() {
		 return socket.isClosed();
	}
}

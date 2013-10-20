package com.chess.uci.share;

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
	
	@Override
	public void writeToNetwork(String content) throws IOException {
		socket.write(content);
	}
	
	@Override
	public void writeToNetwork(byte[] content) throws IOException {
		socket.write(content);
	}

	@Override
	public String readFromNetwork() throws IOException {
		return socket.read();
	}
	
	@Override
	public InetAddress getAddress() {
		return socket.getAddress();
	}
	
	@Override
	public int getPort() {
		return socket.getPort();
	}
	
	@Override
	public void close() throws IOException {
		if(null != socket) {
			socket.close();
		}
	}
	
	@Override
	public boolean isConnected() {
		 return socket.isConnected();
	}
	
	@Override
	public boolean isClosed() {
		 return socket.isClosed();
	}
}

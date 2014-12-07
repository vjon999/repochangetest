package com.prapps.chess.client.tcp.cb;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

import static com.prapps.chess.common.engines.ProtocolConstants.*;

import com.prapps.chess.common.engines.ProtocolConstants;
import com.prapps.chess.uci.share.TCPNetworkRW;

public class ServerConnector {

	private static Logger LOG = Logger.getLogger(ServerConnector.class.getName());
	
	/*public ServerDetails connect(String ip, int port) throws IOException {
		InetAddress targetAddress = null;
		targetAddress = InetAddress.getByName(ip);		
		LOG.info("server: "+targetAddress.getHostName()+"\tport: "+port);	

		final TCPNetworkRW networkRW = new TCPNetworkRW(new Socket(targetAddress, port));
		networkRW.writeToNetwork(START_MSG);
		String connected = networkRW.readFromNetwork();
		LOG.finest("Server: "+connected);
		networkRW.writeToNetwork(GET_AVAILABLE_ENGINES);
		String servers = networkRW.readFromNetwork();
		LOG.finest("Server: "+servers);
		return new ServerDetails(servers);
	}*/
	
	public TCPNetworkRW connect(String ip, int port) throws IOException {
		InetAddress targetAddress = null;
		targetAddress = InetAddress.getByName(ip);		
		LOG.info("server: "+targetAddress.getHostName()+"\tport: "+port);
		final TCPNetworkRW networkRW = new TCPNetworkRW(new Socket(targetAddress, port));
		networkRW.writeToNetwork(ProtocolConstants.START_MSG);
		String connected = networkRW.readFromNetwork();
		LOG.finest("Server: "+connected);
		return networkRW;
	}
	
	public static void main(String[] args) throws IOException {
		ServerConnector connector = new ServerConnector();
		//ServerDetails serverDetails = connector.connect("localhost",8080);
		//System.err.println(serverDetails);
	}
}

package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import personalize.User;
import server.ProtocolConsts;

public class UCIUtil {

	public static final int DEFAULT_BUFFER_SIZE = 1024*1000;
	
	public static String readStream(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int readLen = buffer.length;
		while(readLen == buffer.length && (readLen = is.read(buffer, 0, buffer.length)) != 0) {
			sb.append(new String(buffer, 0, readLen));
		}
		return sb.toString();
	}
	
	public static void writeString(String line, OutputStream os) throws IOException {
		os.write(line.getBytes());
		os.flush();
	}
	
	public static String calculateMD5Hash(String input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] mdbytes = md.digest();
		
		//convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        return sb.toString();
	}
	
	public static boolean authenticate(String command, User user) {
		try {
			String arr[] = command.split("\\|");
			if(arr[1].trim().equals("deep") && UCIUtil.calculateMD5Hash(arr[2].trim()).equals("d41d8cd98f00b204e9800998ecf8427e")) {
				user.setUserName(arr[1]);
				return true;
			}
			else {
				return false;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void streamPiper(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int readLen = 0;
		while((readLen = is.read(buffer, 0, buffer.length)) != -1) {
			os.write(buffer, 0, readLen);
			os.flush();
		}
	}
	
	public static String readPacket(DatagramPacket packet) throws IOException {
		return new String(packet.getData(), 0, packet.getLength());
	}
	
	public static void sendPacket(String message, DatagramSocket datagramSocket, DatagramPacket oldPacket) throws IOException {
		System.out.println("sending: "+message+"\tsize: "+message.getBytes().length);
		DatagramPacket newPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, oldPacket.getAddress(), oldPacket.getPort());
		datagramSocket.send(newPacket);
	}
	
	public static void sendPacket(String message, DatagramSocket datagramSocket, InetAddress address, int port) throws IOException {
		DatagramPacket newPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, address, port);
		System.out.println(address+":"+port);
		datagramSocket.send(newPacket);
	}
	
	public static String[] getMessageParams(String message) {
		return message.split(ProtocolConsts.DELIMITER);
	}
}

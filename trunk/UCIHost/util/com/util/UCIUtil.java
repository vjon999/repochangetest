package com.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import personalize.User;
import server.ProtocolConsts;

public class UCIUtil {

	public static final int DEFAULT_BUFFER_SIZE = 1024 * 1000;
	private static Logger LOG = Logger.getLogger(UCIUtil.class.getName());

	public static String readStream(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int readLen = buffer.length;
		while (readLen == buffer.length && (readLen = is.read(buffer, 0, buffer.length)) != 0) {
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

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}

	public static boolean authenticate(String command, User user) {
		try {
			String arr[] = command.split("\\|");
			if (arr[1].trim().equals("deep") && UCIUtil.calculateMD5Hash(arr[2].trim()).equals("d41d8cd98f00b204e9800998ecf8427e")) {
				user.setUserName(arr[1]);
				return true;
			} else {
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
		while ((readLen = is.read(buffer, 0, buffer.length)) != -1) {
			os.write(buffer, 0, readLen);
			os.flush();
		}
	}

	public static String readPacket(DatagramPacket packet, byte[] password) throws IOException {
		return new String(decrypt(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()), password));
	}
	
	public static byte[] readBytePacket(DatagramPacket packet, byte[] password) throws IOException {
		return decrypt(Arrays.copyOfRange(packet.getData(), 0, packet.getLength()),password);
	}

	/*public static void sendPacket(String message, DatagramSocket datagramSocket, DatagramPacket oldPacket) throws IOException {
		LOG.finest("sending: " + message + "\tsize: " + message.getBytes().length);		
		DatagramPacket newPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, oldPacket.getAddress(), oldPacket.getPort());
		datagramSocket.send(newPacket);
	}

	public static void sendPacket(String message, DatagramSocket datagramSocket, InetAddress address, int port) throws IOException {
		LOG.finest(address + ":" + port);
		DatagramPacket newPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, address, port);
		datagramSocket.send(newPacket);
	}*/
	
	public static void sendPacket(byte[] message,int start, int len, byte[] password, DatagramSocket datagramSocket, InetAddress address, int port) throws IOException {
		LOG.finest(address + ":" + port);
		final byte[] msg = encrypt(message, start, len, password);
		DatagramPacket newPacket = new DatagramPacket(msg, msg.length, address, port);
		datagramSocket.send(newPacket);
	}
	
	public static void sendPacket(String message, byte[] password, DatagramSocket datagramSocket, InetAddress address, int port) throws IOException {
		LOG.finest(address + ":" + port);
		final String msg = encrypt(message, password);
		DatagramPacket newPacket = new DatagramPacket(msg.getBytes(), 0,msg.getBytes().length, address, port);
		datagramSocket.send(newPacket);
	}

	public static String[] getMessageParams(String message) {
		return message.split(ProtocolConsts.DELIMITER);
	}

	public static String encrypt(String strToEncrypt, byte[] password) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			final SecretKeySpec secretKey = new SecretKeySpec(password, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
			return encryptedString;
		} catch (Exception e) {
			LOG.warning("Error while encrypting" + e);
		}
		return null;

	}
	
	public static byte[] encrypt(byte[] buffer, int start, int len, byte[] password) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			final SecretKeySpec secretKey = new SecretKeySpec(password, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.encodeBase64(cipher.doFinal(buffer, start, len));
		} catch (Exception e) {
			LOG.warning("Error while encrypting" + e);
		}
		return null;

	}

	public static String decrypt(String strToDecrypt, byte[] password) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			final SecretKeySpec secretKey = new SecretKeySpec(password, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
			return decryptedString;
		} catch (Exception e) {
			LOG.warning("Error while decrypting" + e);
			e.printStackTrace();

		}
		return null;
	}

	public static byte[] decrypt(byte[] buffer, byte[] password) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			final SecretKeySpec secretKey = new SecretKeySpec(password, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(Base64.decodeBase64(buffer));
		} catch (Exception e) {
			LOG.warning("Error while decrypting" + e);
			e.printStackTrace();
		}
		return null;
	}

}

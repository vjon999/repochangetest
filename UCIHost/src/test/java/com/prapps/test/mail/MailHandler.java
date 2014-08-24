package com.prapps.test.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHandler implements Runnable {

	private boolean sent = false;
	
	public boolean isSent() {
		return sent;
	}

	public void run() {
		try {
			send("pratik006@gmail.com", "vickysengupta006@gmail.com", "Server_IP="+getPublicIP(), "");
			sent = true;
		} catch (IOException e) {
			e.printStackTrace();
			sent = false;
		}
	}
	
	private static String getPublicIP() throws IOException {
		URL whatismyip = new URL("http://automation.whatismyip.com/n09230945.asp");
	    URLConnection connection = whatismyip.openConnection();
	    connection.addRequestProperty("Protocol", "Http/1.1");
	    connection.addRequestProperty("Connection", "keep-alive");
	    connection.addRequestProperty("Keep-Alive", "1000");
	    connection.addRequestProperty("User-Agent", "Web-Agent");

	    BufferedReader in = 
	        new BufferedReader(new InputStreamReader(connection.getInputStream()));

	    String ip = in.readLine(); //you get the IP as a String
	    return ip;
	}

	private static void send(String from, String to, String subject, String content){
   	 
		final String username = "vickysengupta006@gmail.com";
		final String password = "chinat0wn";
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.timeout","30000");
		
		Session mailSession = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
 
		Message simpleMessage = new MimeMessage(mailSession);
 
		InternetAddress fromAddress = null;
		InternetAddress toAddress = null;
		try {
			fromAddress = new InternetAddress(from);
			toAddress = new InternetAddress(to);
		} catch (AddressException e) {
			e.printStackTrace();
		}
 
		try {
			simpleMessage.setFrom(fromAddress);
			simpleMessage.setRecipient(RecipientType.TO, toAddress);
			simpleMessage.setSubject(subject);
			simpleMessage.setText(content);
 
			Transport.send(simpleMessage);
			System.out.println("sent");
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}

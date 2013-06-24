package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class IPGetter {
	
	public static String IPADDRESS_PATTERN =  "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
	
	public static void main(String[] arg) {
		try {
			//mailExternalIP(getExternalIP(), "vickysengupta006@gmail.com");
			System.out.println(getExternalIP());
	    } 
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	public static String getExternalIP() throws IOException {
		URL whatismyip = new URL("http://agentgatech.appspot.com/");
		URLConnection connection = whatismyip.openConnection();
		connection.addRequestProperty("Protocol", "Http/1.1");
	    connection.addRequestProperty("Connection", "keep-alive");
	    connection.addRequestProperty("Keep-Alive", "1000");
	    connection.addRequestProperty("User-Agent", "Web-Agent");
	    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String ip = in.readLine(); //you get the IP as a String
	    return ip;
	}
	
	public static void mailExternalIP(String ip, String mailAddress) {
		final String username = "vickysengupta006@gmail.com";
		final String password = "chinat0wn";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(mailAddress));
			message.setSubject("External_IP="+ip);
			message.setText("External_IP="+ip+";"+new Date().getTime()); 
			Transport.send(message); 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getIPFromMail() throws MessagingException {

		Folder inbox;
		String email = "vickysengupta006@gmail.com";
		String pass = "chinat0wn";
		String ip = null;

		/* Set the mail properties */
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
			/* Create the session and get the store for read the mail. */
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", email, pass);

			/* Mention the folder name which you want to read. */
			inbox = store.getFolder("Inbox");
			//System.out.println("No of Unread Messages : " + inbox.getUnreadMessageCount());

			/* Open the inbox using store. */
			inbox.open(Folder.READ_ONLY);

			/* Get the messages which is unread in the Inbox */
			/*Message messages[] = inbox.search(new FlagTerm(
					new Flags(Flag.SEEN), false));*/
			
			Message messages[] = inbox.getMessages(new int[]{inbox.getMessageCount()});
			String subject = messages[0].getSubject();
			ip = subject.substring(subject.indexOf("=") + 1, subject.length());
			//System.out.println(ip + " , " +messages[0].getReceivedDate());
		return ip;
	}
	
	public static void mailExternalIP(String mailAddress) throws IOException {
		mailExternalIP(getExternalIP(), mailAddress);
	}
}

package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class IPGetter {
	public static void main(String[] arg) {
		try {
			mailExternalIP(getExternalIP(), "vickysengupta006@gmail.com");
	    } 
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	public static String getExternalIP() throws IOException {
		URL whatismyip = new URL("http://automation.whatismyip.com/n09230945.asp");
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
	
	public static void mailExternalIP(String mailAddress) throws IOException {
		mailExternalIP(getExternalIP(), mailAddress);
	}
}

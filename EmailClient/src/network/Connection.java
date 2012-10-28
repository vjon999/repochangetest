package network;

import java.net.ConnectException;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import listener.InternetConnectionListener;
import threads.StatusThread;
import account.AccountInformation;
import account.MailAccount;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.*;

import constants.Consts;


public class Connection implements Consts {
	private String host=null;
	private Store store=null;
	private Session session=null;
	private Folder folder=null;
	
	public static Store connect(MailAccount mailAccount) throws MessagingException {
		Store store=null;
		String host=null;
		String userName=null;
		String password=null;
		Properties prop=null;
		Session session=null;
		AccountInformation accountInformation = mailAccount.getAccountInformation();
		host=accountInformation.getIncomingServerAddress();
		userName=accountInformation.getEmail();
		password =new String(accountInformation.getPassword());
		prop=System.getProperties();
		session=Session.getInstance(prop);	
		try {			
			store=session.getStore(accountInformation.getIncomingMailServerType());
			//store=session.getStore("pop3s");
		}catch(MessagingException e) {
			JOptionPane.showMessageDialog(mailAccount.getMailHandler().getMainFrame(), e.getMessage());
			throw e;
		}
		store.addConnectionListener(new InternetConnectionListener(mailAccount));
		mailAccount.setConnectionStatus(CONNECTION_STATUSES[STATUS_CONNECTING]);
		new StatusThread(mailAccount);
		store.connect(host,userName,password);
		return store;
	}

	public static Store connect(AccountInformation accountInformation) throws MessagingException {
		Store store=null;
		String host=null;
		String userName=null;
		String password=null;
		Properties prop=null;
		Session session=null;
		host=accountInformation.getIncomingServerAddress();
		userName=accountInformation.getEmail();
		password =new String(accountInformation.getPassword());
		prop=new Properties();
		session=Session.getDefaultInstance(prop,null);
		store=session.getStore(accountInformation.getIncomingMailServerType());
		store.connect(host,userName,password);
		return store;
	}
	
	public static POP3Folder getPOP3Folder(Store store, String folderName) throws ConnectException,MessagingException {
		POP3Folder folder = (POP3Folder)store.getFolder(folderName.toUpperCase());
		folder.open(Folder.READ_WRITE);
	    return folder;	   
	}
	
	public static IMAPFolder getIMAPFolder(Store store, String folderName) throws ConnectException,MessagingException {
		IMAPFolder folder = (IMAPFolder)store.getFolder(folderName.toUpperCase());
		folder.open(Folder.READ_WRITE);
	    return folder;	   
	}

	
	public Session createSMTPSession() {
		Properties prop=System.getProperties();
		prop.put("smtp.gmail.com",host);
		Session sess=Session.getDefaultInstance(prop,null);
		return sess;
	}
	
	public static Session createSMTPSession(AccountInformation  accountInformation) {
		Properties prop=System.getProperties();
		prop.put("mail.smtp.host",accountInformation.getOutgoingServerAddress());
		Session sess=Session.getDefaultInstance(prop,null);
		return sess;
	}
	
	public void closeConnection() throws MessagingException {
		store.close();
	}
	
	public Session getSession() {
		return session;
	}

	
	public Folder getFolder() {
		return folder;
	}


	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public static void sendMail(AccountInformation accountInformation,MimeMessage message,Session session) throws MessagingException {
		if(null == session) {
			session=createSMTPSession(accountInformation);
		}
		Transport t = session.getTransport(accountInformation.getOutgoingMailServerType());
		try {
			t.connect(accountInformation.getOutgoingServerAddress(), accountInformation.getUserName(), new String(accountInformation.getPassword()));
			t.sendMessage(message, message.getAllRecipients());
		} finally {
			t.close();
		}
		JOptionPane.showMessageDialog(new JFrame(),SUCCESSFULLY_SENT_MESSAGE);
		System.out.println("Message sent successfully...");
	}	
	
	public static void sendMail(AccountInformation accountInformation,MimeMessage message) throws MessagingException {
		Session	session=createSMTPSession(accountInformation);
		Transport t = session.getTransport(accountInformation.getOutgoingMailServerType());
		try {
			t.connect(accountInformation.getOutgoingServerAddress(), accountInformation.getUserName(), new String(accountInformation.getPassword()));
			t.sendMessage(message, message.getAllRecipients());
		} finally {
			t.close();
		}
		JOptionPane.showMessageDialog(new JFrame(),SUCCESSFULLY_SENT_MESSAGE);
		System.out.println("Message sent successfully...");
	}	
}

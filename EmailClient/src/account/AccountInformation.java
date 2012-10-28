package account;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import constants.Consts;

import network.Connection;

import email.EmailMessage;
import email.MessageIndex;
import exception.BaseException;
import filesystem.DAO;

import util.Encrypt;
import util.Encryptor;
import util.MessageUtil;

public class AccountInformation implements Serializable,Consts {
	/**
	 * 
	 */
	public transient static final String[] MAIL_TYPE_KEYS={"POP3","IMAP","SMTP"};
	public transient static final String[] MAIL_TYPES={"pop3s","imaps","smtps"};
	public transient static final int POP3_TYPE=0;
	public transient static final int IMAP_TYPE=1;
	public transient static final int SMTP_TYPE=2;
	public transient static Map<String,String> mailTypeMap=null;
	private static final long serialVersionUID = 1L;
	private String accountName=null;
	private String userName=null;
	private String email=null;
	private char[] password=null;
	private String incomingServerAddress=null;
	private String outgoingServerAddress=null;
	private String incomingMailServerType=null;
	private String outgoingMailServerType=null;
	private boolean folderStructureCreated=false;
	private List<String> folderStructure=new ArrayList<String>();
	
	public AccountInformation() {
		mailTypeMap=new HashMap<String, String>();
		for(int i=0;i<MAIL_TYPES.length;i++) {
			mailTypeMap.put(MAIL_TYPE_KEYS[i],MAIL_TYPES[i]);
		}
	}
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public char[] getPassword() {
		return Encrypt.decrypt(password);
	}
	public void setPassword(char[] password) {	
		this.password = Encrypt.encrypt(password);
	}

	public String getIncomingServerAddress() {
		return incomingServerAddress;
	}
	public void setIncomingServerAddress(String serverAddress) {
		this.incomingServerAddress = serverAddress;
	}
	
	/**
	 * @return the outgoingServerAddress
	 */
	public String getOutgoingServerAddress() {
		return outgoingServerAddress;
	}

	/**
	 * @param outgoingServerAddress the outgoingServerAddress to set
	 */
	public void setOutgoingServerAddress(String outgoingServerAddress) {
		this.outgoingServerAddress = outgoingServerAddress;
	}

	public String getIncomingMailServerType() {
		return incomingMailServerType;
	}
	public void setIncomingMailServerType(String mailType) {
		this.incomingMailServerType = getValueFromMap(mailType);
	}
	
	public String getOutgoingMailServerType() {
		return outgoingMailServerType;
	}

	public void setOutgoingMailServerType(String outgoingMailServerType) {
		this.outgoingMailServerType = getValueFromMap(outgoingMailServerType);
	}

	public boolean isFolderStructureCreated() {
		return folderStructureCreated;
	}

	public void setFolderStructureCreated(boolean folderStructureCreated) {
		this.folderStructureCreated = folderStructureCreated;
	}

	public List<String> getFolderStructure() {
		return folderStructure;
	}

	public void setFolderStructure(List<String> folderStructure) {
		this.folderStructure = folderStructure;
	}

	public static String getValueFromMap(String key) {
		return mailTypeMap.get(key);
	}
	
	public String getFrom() {
		return userName+"<"+email+">";
	}
	
	public EmailMessage readMessage(MessageIndex messageIndex,String folderName)  {
		return null;
	}
	
	public void readString(StringBuilder sbBuilder) {
		int index = sbBuilder.indexOf(SEPARATOR);
		accountName = sbBuilder.substring(0,index);
		sbBuilder.delete(0,index+SEPARATOR.length());
		index = sbBuilder.indexOf(SEPARATOR);
		userName = sbBuilder.substring(0,index);
		sbBuilder.delete(0,index+SEPARATOR.length());
		index = sbBuilder.indexOf(SEPARATOR);
		email = sbBuilder.substring(0,index);
		sbBuilder.delete(0,index+SEPARATOR.length());
		index = sbBuilder.indexOf(SEPARATOR);
		password = sbBuilder.substring(0,index).toCharArray();
		sbBuilder.delete(0,index+SEPARATOR.length());
		index = sbBuilder.indexOf(SEPARATOR);
		incomingMailServerType = sbBuilder.substring(0,index);
		sbBuilder.delete(0,index+SEPARATOR.length());
		index = sbBuilder.indexOf(SEPARATOR);
		incomingServerAddress = sbBuilder.substring(0,index);
		sbBuilder.delete(0,index+SEPARATOR.length());
		index = sbBuilder.indexOf(SEPARATOR);
		outgoingMailServerType = sbBuilder.substring(0,index);
		sbBuilder.delete(0,index+SEPARATOR.length());
		index = sbBuilder.indexOf(SEPARATOR);
		outgoingServerAddress = sbBuilder.substring(0,index);
		sbBuilder.delete(0,index+SEPARATOR.length());
		index = sbBuilder.indexOf(SEPARATOR);
	}
	
	public String toWrite() {
		StringBuilder sbBuilder = new StringBuilder();
		sbBuilder.append(accountName+SEPARATOR);
		sbBuilder.append(userName+SEPARATOR);
		sbBuilder.append(email+SEPARATOR);
		for(char ch:password) {
			sbBuilder.append(ch);
		}
		sbBuilder.append(SEPARATOR);
		sbBuilder.append(incomingMailServerType+SEPARATOR);
		sbBuilder.append(incomingServerAddress+SEPARATOR);
		sbBuilder.append(outgoingMailServerType+SEPARATOR);
		sbBuilder.append(outgoingServerAddress+SEPARATOR);
		return sbBuilder.toString();
	}
}

package email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import util.MessageUtil;

public class MessageIndex implements Serializable,Comparable<MessageIndex> {
	
	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;
	protected String fileName = null;
	protected String uid;
	protected String messageID = null;
	protected String messageSubject=null;
	protected Date receivedDate=null;
	protected Date sentDate=null;
	protected InternetAddress[] to=null;
	protected InternetAddress[] cc=null; 
	protected InternetAddress[] bcc=null; 
	protected InternetAddress[] from=null;
	protected int messageNumber=-1;
	protected int messageSize=0;
	protected boolean saved;
	protected boolean read;
	
	public MessageIndex() {
		
	}
	
	public MessageIndex(EmailMessage emailMessage) {
		//setUID(emailMessage.getUID());
		messageID = emailMessage.getMessageID();
		setMessageSubject(emailMessage.getMessageSubject());
		setReceivedDate(emailMessage.getReceivedDate());
		setSentDate(emailMessage.getSentDate());
		setFrom(emailMessage.getFrom());
		setTo(emailMessage.getTo());
		setCc(emailMessage.getCc());
		setBcc(emailMessage.getBcc());
		setMessageSize(emailMessage.getMessageSize());	
	}
	
	public MessageIndex(MimeMessage message,String fileName) throws MessagingException {
		//uid=message.getMessageID();
		messageID = message.getMessageID();
		messageSubject=message.getSubject();
		receivedDate=message.getReceivedDate();
		sentDate=message.getSentDate();
		this.fileName=fileName;
		try {
			to=(InternetAddress[])message.getRecipients(RecipientType.TO);
		}catch (AddressException e) {
			try {
				to=new InternetAddress[message.getRecipients(RecipientType.TO).length];
				for(int i=0;i<message.getRecipients(RecipientType.TO).length;i++) {
					to[i]=(InternetAddress) message.getRecipients(RecipientType.TO)[i];
				}
			}catch (AddressException e2) {
				if(null == to || to.length == 0) {
					to=new InternetAddress[1];
					to[0]=new InternetAddress("<error@address.com>");
				}
			}				
		}
		//to=(InternetAddress[]) message.getRecipients(RecipientType.TO);
		cc=(InternetAddress[]) message.getRecipients(RecipientType.CC);
		bcc=(InternetAddress[]) message.getRecipients(RecipientType.BCC);
		try {
			from=(InternetAddress[]) message.getFrom();
		}catch (AddressException e) {
			try {
				from=new InternetAddress[message.getFrom().length];
				for(int i=0;i<message.getFrom().length;i++) {
					from[i]=(InternetAddress) message.getFrom()[i];
				}
			}catch (AddressException e2) {
				if(null == from || from.length == 0) {
					from=new InternetAddress[1];
					from[0]=new InternetAddress("<error@address.com>");
				}
			}				
		}
		messageNumber=message.getMessageNumber();
		messageSize=message.getSize();
	}
	
	public String getUID() {
		return uid;
	}

	public void setUID(String uid) {
		this.uid = uid;
	}

	public String getMessageSubject() {
		return messageSubject;
	}
	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}
	public Date getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public InternetAddress[] getTo() {
		return to;
	}

	public void setTo(InternetAddress[] to) {
		this.to = to;
	}

	public InternetAddress[] getFrom() {
		return from;
	}

	public void setFrom(InternetAddress[] from) {
		this.from = from;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InternetAddress[] getCc() {
		return cc;
	}

	public void setCc(InternetAddress[] cc) {
		this.cc = cc;
	}

	public InternetAddress[] getBcc() {
		return bcc;
	}

	public void setBcc(InternetAddress[] bcc) {
		this.bcc = bcc;
	}

	public int getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}

	public int getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}
	
	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
	
	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public void writeTo(Message message,ObjectOutputStream os) {
		try {			
			os.writeObject(uid);
			os.writeObject(message.getSubject());
			os.writeObject(message.getReceivedDate());
			os.writeObject(message.getSentDate());
			os.writeObject(message.getFrom());
			os.writeObject(message.getRecipients(RecipientType.TO));
			os.writeObject(message.getRecipients(RecipientType.CC));
			os.writeObject(message.getRecipients(RecipientType.BCC));
			os.writeObject(message.getSize());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static MessageIndex createEmailMessage(File file) throws IOException {
		MessageIndex messageIndex=new MessageIndex();		
		try {		
			ObjectInputStream is=new ObjectInputStream(new FileInputStream(file));
			messageIndex.setUID((String) is.readObject());
			messageIndex.setMessageSubject((String) is.readObject());
			messageIndex.setReceivedDate((Date) is.readObject());
			messageIndex.setSentDate((Date) is.readObject());
			messageIndex.setFrom((InternetAddress[]) is.readObject());
			messageIndex.setTo((InternetAddress[]) is.readObject());
			messageIndex.setCc((InternetAddress[]) is.readObject());
			messageIndex.setBcc((InternetAddress[]) is.readObject());
			messageIndex.setMessageSize((Integer) is.readObject());	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return messageIndex;
	}
	
	public void readFrom(ObjectInputStream is) {
		try {
			setMessageNumber(is.readInt());
			setMessageSize(is.readInt());
			setMessageSubject(is.readUTF());
			setReceivedDate((Date) is.readObject());
			setSentDate((Date) is.readObject());
			setTo((InternetAddress[]) is.readObject());
			setCc((InternetAddress[]) is.readObject());
			setBcc((InternetAddress[]) is.readObject());
			setFrom((InternetAddress[]) is.readObject());
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private boolean equalsSubject(Message message) throws MessagingException {
		if(null == message.getSubject()) {
			if(null == messageSubject) {
				return true;
			}			
		}
		else {
			if(null == messageSubject) {
				return true;
			}
			else {
				if(messageSubject.equals(message.getSubject())) {
					return true;
				}				
			}
		}
		return false;
	}
	
	private boolean equalsFrom(Message message) throws MessagingException {
		if(null == message.getFrom()) {
			if(null == from) {
				return true;
			}
		}
		else if(null == from) {
			if(null == message.getFrom()) {
				return true;
			}
		}
		else {
			if(0 >= message.getFrom().length) {
				if(0 >= from.length) {
					return true;
				}
			}
			else if(0 >= from.length){
				if(0 >= message.getFrom().length) {
					return true;
				}
			}
			else {
				if(from[0].equals(message.getFrom()[0])) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean equalsSentDate(Message message) throws MessagingException {
		if(null == message.getSentDate()) {
			if(null == sentDate) {
				return true;
			}
		}
		else if(null == sentDate) {
			if(null == message.getSentDate()) {
				return true;
			}
		}
		else {
			if(sentDate.equals(message.getSentDate())) {
				return true;
			}
		}
		return false;
	}

	public boolean isEquals(Message message) throws MessagingException {
		if(equalsSubject(message)) {
			if(equalsSentDate(message)) {
				if(equalsFrom(message)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int compareTo(MessageIndex anotherMessageIndex) {
		return (messageSize > anotherMessageIndex.getMessageSize()) ? 1:0;
	}
	
	public static String createFileName(String uid,String subject) {
		String fileName = uid+"_"+MessageUtil.dropSpecialChars(subject)+".dmp";
		return fileName;
	}
}

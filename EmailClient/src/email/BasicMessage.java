package email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;


import com.sun.mail.util.BASE64DecoderStream;

import util.MessageUtil;

import constants.Consts;

public class BasicMessage extends MessageIndex implements Consts {

	String content;
	List<String> attachmentList = new ArrayList<String>();
	List<String> contentList = new ArrayList<String>();
	List<String> contentTypeList = new ArrayList<String>();

	public String getContent() {
		String textContent="";
		String htmlContent="";
		for(int i=0;i<contentTypeList.size();i++) {
			if("text/plain".equalsIgnoreCase(contentTypeList.get(i))) {
				textContent+=contentList.get(i);
			}
			else if("text/html".equalsIgnoreCase(contentTypeList.get(i))) {
				htmlContent+=contentList.get(i);
			}
		}
		if(htmlContent.length()>0) {
			return content;
		}
		return textContent;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void write(Message message,String uid,File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
			writer.write(uid+SEPARATOR);
			writer.write(message.getSubject()+SEPARATOR);

			if(null != message.getReceivedDate()) {
				writer.write(message.getReceivedDate().toString()+SEPARATOR);
			}
			else {
				writer.write(NULL+SEPARATOR);
			}
			
			if(null != message.getSentDate()) {
				writer.write(message.getSentDate().toString()+SEPARATOR);
			}
			else {
				writer.write(NULL+SEPARATOR);
			}
			if(null != message.getRecipients(RecipientType.TO)) {
				writer.write(MessageUtil.getStringFromAddress((InternetAddress[]) message.getRecipients(RecipientType.TO))+SEPARATOR);
			}
			else {
				writer.write(NULL+SEPARATOR);
			}
			if(null != message.getRecipients(RecipientType.CC)) {
				writer.write(MessageUtil.getStringFromAddress((InternetAddress[]) message.getRecipients(RecipientType.CC))+SEPARATOR);
			}
			else {
				writer.write(NULL+SEPARATOR);
			}
			if(null != message.getRecipients(RecipientType.BCC)) {
				writer.write(MessageUtil.getStringFromAddress((InternetAddress[]) message.getRecipients(RecipientType.BCC))+SEPARATOR);
			}
			else {
				writer.write(NULL+SEPARATOR);
			}
			if(null != message.getFrom()) {
				writer.write(MessageUtil.getStringFromAddress((InternetAddress[]) message.getFrom())+SEPARATOR);
			}
			else {
				writer.write(NULL+SEPARATOR);
			}
			writer.write(message.getMessageNumber()+SEPARATOR);
			writer.write(message.getSize()+SEPARATOR);			
			writer.write(getContent());
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} 
	}

	public void read(File file) {
		String buffer=null;
		List<BasicMessage> basicMessageList=new ArrayList<BasicMessage>();
		try {
			BufferedReader reader=new BufferedReader(new FileReader(file));
			buffer = reader.readLine();
			while(null != buffer) {
				basicMessageList.add(createBasicMessage(new StringBuilder(reader.readLine())));
			}                        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BasicMessage createBasicMessage(StringBuilder buffer) {
		BasicMessage basicMessage=new BasicMessage();
		String tmp;
		basicMessage.setUID(readNext(buffer));
		basicMessage.setMessageSubject(readNext(buffer));
		tmp=readNext(buffer);
		if(null == tmp) {
			basicMessage.setReceivedDate(null);
		}
		else {
			basicMessage.setReceivedDate(new Date(tmp));
		}
		tmp=readNext(buffer);
		if(null == tmp) {
			basicMessage.setSentDate(null);
		}
		else {
			basicMessage.setSentDate(new Date(tmp));
		}
		basicMessage.setTo((InternetAddress[]) MessageUtil.getAddressFromString(readNext(buffer)));
		basicMessage.setCc((InternetAddress[]) MessageUtil.getAddressFromString(readNext(buffer)));
		basicMessage.setBcc((InternetAddress[]) MessageUtil.getAddressFromString(readNext(buffer)));
		basicMessage.setFrom((InternetAddress[]) MessageUtil.getAddressFromString(readNext(buffer)));
		basicMessage.setMessageNumber(Integer.parseInt(readNext(buffer)));
		basicMessage.setMessageSize(Integer.parseInt(readNext(buffer)));
		basicMessage.setContent(readNext(buffer));
		return basicMessage;
	}

	private String readNext(StringBuilder buffer) {
		int index=0;
		String data=null;
		index=buffer.indexOf(SEPARATOR);
		if(-1 != index) {
			data = buffer.substring(0,index);
			buffer.delete(0,index+SEPARATOR.length());
		}

		return data;
	}


	public void writePart(Part p) throws IOException, MessagingException {
		String filename = p.getFileName();
		if (filename != null)
			attachmentList.add(filename);
		if (p.isMimeType("text/plain")) {
			contentTypeList.add((String) p.getContentType());
			contentList.add((String) p.getContent());
		}
		if (p.isMimeType("text/html")) {
			contentTypeList.add((String) p.getContentType());
			contentList.add((String) p.getContent());
		}else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart)p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				writePart(mp.getBodyPart(i));
		} else if (p.isMimeType("message/rfc822")) {
			writePart((Part)p.getContent());
		} else {
			if (!false && !false) {
				/*
				 * If we actually want to see the data, and it's not a
				 * MIME type we know, fetch it and check its Java type.
				 */
				Object o = p.getContent();
				if (o instanceof String) {
					if(!MessageUtil.isInList(contentList, p.getContent())) {
						contentTypeList.add(p.getContentType());
						contentList.add(o.toString());
					}
				} else if (o instanceof BASE64DecoderStream) {
					attachmentList.add(p.getFileName());
				}

			}
		}
	}
	
	public String getContent(Message message) throws MessagingException, IOException {
		String textContent = "";
		String htmlContent = "";
		if (message.isMimeType("text/plain")) {
			textContent = (String) message.getContent();
		}
		if (message.isMimeType("text/html")) {
			htmlContent = (String) message.getContent();
		}else if (message.isMimeType("multipart/*")) {
			Multipart mp = (Multipart)message.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				htmlContent = getContent(message);
		}
		if(htmlContent.length() > 0) {
			return htmlContent;
		}
		return textContent;
	}

	public static void main(String[] args) {

	}

}

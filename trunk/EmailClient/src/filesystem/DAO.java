package filesystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.mail.util.BASE64DecoderStream;

import constants.Consts;
import email.EmailAttachment;
import email.EmailMessage;
import email.MessageIndex;
import exception.NoAccountException;
import gui.MainFrame;
import network.MailHandler;
import threads.AbstractThread;
import threads.HeaderThread;
import util.MessageUtil;
import account.AccountInformation;
import account.MailAccount;

public class DAO implements Consts {
	protected static MyLogger LOG = new MyLogger(DAO.class ,MainFrame.console);
	private boolean isWriting = false;

	public boolean isWriting() {
		return isWriting;
	}

	public void setWriting(boolean isWriting) {
		this.isWriting = isWriting;
	}
	
	public static void writeMessageIndex(MessageIndex messageIndex,File file) throws IOException {
		ObjectOutputStream os = new AppendableOutputStream(new FileOutputStream(file));
		os.writeObject(messageIndex);
		os.close();
	}
	
	public static void writeMessageIndexes(List<MessageIndex> messageIndexes,File file) throws IOException {
		ObjectOutputStream os = new AppendableOutputStream(new FileOutputStream(file));
		for(MessageIndex messageIndex:messageIndexes) {			
			os.writeObject(messageIndex);			
		}
		os.close();
	}
	
	public static void syncHeader(Map<String,MessageIndex> headerMap,File file) throws FileNotFoundException, IOException {
		LOG.info("filepath => "+file.getAbsolutePath());
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
		os.writeObject(headerMap);
		os.close();
		LOG.info("output stream closed");
	}
	
	public static void saveMail(Message message,String path) throws FileNotFoundException, IOException, MessagingException {
		File file = new File(path);
		try {
			message.writeTo(new FileOutputStream(file));
		}catch (MessagingException e) {
			LOG.info("Exception: "+e.getMessage()+" occurred, deleting file "+file.getName());
			if(!file.delete()) {
				file.deleteOnExit();
			}
			throw e;
		}
		
	}

	public void writeMessage(Message message,AccountInformation accountInformation,String folderName,String uid) {
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.writeTo(message,accountInformation,folderName,uid);
	}
	
	public EmailMessage writeBasicMessage(Message message,MailAccount mailAccount,String folderName,String uid) throws MessagingException {
		try {
			File folder = new File(MailHandler.getBaseFolderName()+"\\"+mailAccount.getAccountInformation().getAccountName()+"_"+mailAccount.getAccountInformation().getUserName()+"\\"+folderName);
			if(!folder.exists()) {
				if(!folder.mkdir()) {					
					throw new IOException();
				}
				mailAccount.addFolder(folder.getName());
			}						

			
			StringBuilder messageString=new StringBuilder();
			StringBuilder attachmentNames=new StringBuilder();
			//String messageString="";
			messageString.append(uid+SEPARATOR+message.getSubject()+SEPARATOR);
			//writer.write(uid+SEPARATOR);
			//writer.write(message.getSubject()+SEPARATOR);

			if(null != message.getReceivedDate()) {
				//writer.write(message.getReceivedDate().getTime()+SEPARATOR);
				messageString.append(message.getReceivedDate().getTime()+SEPARATOR);
			}
			else {
				//writer.write(NULL+SEPARATOR);
				messageString.append(NULL+SEPARATOR);
			}
			
			if(null != message.getSentDate()) {
				//writer.write(message.getSentDate().getTime()+SEPARATOR);
				messageString.append(message.getSentDate().getTime()+SEPARATOR);
			}
			else {
				//writer.write(NULL+SEPARATOR);
				messageString.append(NULL+SEPARATOR);
			}
			if(null != message.getRecipients(RecipientType.TO)) {
				//writer.write(MessageUtil.getStringFromAddress((InternetAddress[]) message.getRecipients(RecipientType.TO))+SEPARATOR);
				messageString.append(MessageUtil.getStringFromAddress((InternetAddress[]) message.getRecipients(RecipientType.TO))+SEPARATOR);
			}
			else {
				//writer.write(NULL+SEPARATOR);
				messageString.append(NULL+SEPARATOR);
			}
			if(null != message.getRecipients(RecipientType.CC)) {
				//writer.write(MessageUtil.getStringFromAddress((InternetAddress[]) message.getRecipients(RecipientType.CC))+SEPARATOR);
				messageString.append(MessageUtil.getStringFromAddress((InternetAddress[]) message.getRecipients(RecipientType.CC))+SEPARATOR);
			}
			else {
				//writer.write(NULL+SEPARATOR);
				messageString.append(NULL+SEPARATOR);
			}
			if(null != message.getRecipients(RecipientType.BCC)) {
				//writer.write(MessageUtil.getStringFromAddress((InternetAddress[]) message.getRecipients(RecipientType.BCC))+SEPARATOR);
				messageString.append(MessageUtil.getStringFromAddress((InternetAddress[]) message.getRecipients(RecipientType.BCC))+SEPARATOR);
			}
			else {
				//writer.write(NULL+SEPARATOR);
				messageString.append(NULL+SEPARATOR);
			}
			if(null != message.getFrom()) {
				//writer.write(MessageUtil.getStringFromAddress((InternetAddress[]) message.getFrom())+SEPARATOR);
				messageString.append(MessageUtil.getStringFromAddress((InternetAddress[]) message.getFrom())+SEPARATOR);
			}
			else {
				//writer.write(NULL+SEPARATOR);
				messageString.append(NULL+SEPARATOR);
			}
			//writer.write(message.getMessageNumber()+SEPARATOR);			
			//writer.write(message.getSize()+SEPARATOR);
			messageString.append(message.getMessageNumber()+SEPARATOR+message.getSize()+SEPARATOR);
			/*String s=getText(message,attachmentNames);
			s=s.replaceAll("\r\n", "");
			s=s.replaceAll("\n", "");
			if(s.length() <4) {
				System.out.println(message.getSubject());
			}*/
			//writePart(message, messageString, attachmentNames);
			messageString.append(getText(message));
			//messageString.append(s+SEPARATOR);	
			//writer.write(s+SEPARATOR);\
			File file=new File(MailHandler.getBaseFolderName()+"\\"+mailAccount.getAccountInformation().getAccountName()+"_"+mailAccount.getAccountInformation().getUserName()+"\\"+folderName+"\\"+uid+".dmp");
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
			isWriting = true;
			writer.write(messageString.toString());
			writer.newLine();			
			writer.close();
			isWriting = false;
			return createBasicMessage(messageString);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}	

	private List<String> getText(Part p) throws MessagingException, IOException {
		List<String> list=new ArrayList<String>();
		StringBuilder builder=new StringBuilder();
		writePart(p, list);
		builder.append(list.size()+SEPARATOR);
		/*for(String s:list) {
			builder.append(s+SEPARATOR);
		}*/
		return list;
	}
	
	public void writePart(Part p,List<String> list) throws IOException, MessagingException {
		if (p.isMimeType("text/plain")) {
			list.add(TEXT_CONTENT);
			list.add((String) p.getContent());
		}
		if (p.isMimeType("text/html")) {
			list.add(HTML_CONTENT);
			list.add((String) p.getContent());
		}else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart)p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				writePart(mp.getBodyPart(i),list);
		} else if (p.isMimeType("message/rfc822")) {
			writePart((Part) p.getContent(),list);
		} else {

			/*
			 * If we actually want to see the data, and it's not a
			 * MIME type we know, fetch it and check its Java type.
			 */
			Object o = p.getContent();
			if (o instanceof String) {
				list.add(OTHER_CONTENT);
				list.add((String) p.getContent());		   
			}else if (o instanceof InputStream) {
				list.add(ATTACHMENTS);
				list.add(p.getFileName()+SEPARATOR2+p.getSize());
			} else {
				list.add(OTHER_CONTENT);
				list.add((String) p.getContent());
			}

		}	
	}	
	
	public List<EmailMessage> read(File file) {
		String buffer=null;
		StringBuilder content=null; 
		List<EmailMessage> basicMessageList=new ArrayList<EmailMessage>();		
		try {
			if(!file.isDirectory()) {
				throw new IOException(); 
			}
			for(File f:file.listFiles()) {
				if(f.getName().endsWith(".dmp")) {
					content=new StringBuilder(); 
					/*BufferedReader reader=new BufferedReader(new FileReader(f));
					buffer = reader.readLine();
					while(null != buffer) {	
						content.append("\n"+buffer);
						buffer = reader.readLine();
						if(null == buffer) {
							break;
						}					
					}*/  
					//basicMessageList.add();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return basicMessageList;
	}
	
	private EmailMessage createBasicMessage(StringBuilder buffer) {		
		EmailMessage emailMessage=new EmailMessage();
		String tmp;
		emailMessage.setUID(readNext(buffer));
		emailMessage.setMessageSubject(readNext(buffer));
		tmp=readNext(buffer);
		if(null == tmp || "null".equals(tmp)) {
			emailMessage.setReceivedDate(null);
		}
		else {
			emailMessage.setReceivedDate(new Date(Long.parseLong(tmp)));
		}
		tmp=readNext(buffer);
		if(null == tmp) {
			emailMessage.setSentDate(null);
		}
		else {
			emailMessage.setSentDate(new Date(Long.parseLong(tmp)));
		}
		emailMessage.setTo((InternetAddress[]) MessageUtil.getAddressFromString(readNext(buffer)));
		emailMessage.setCc((InternetAddress[]) MessageUtil.getAddressFromString(readNext(buffer)));
		emailMessage.setBcc((InternetAddress[]) MessageUtil.getAddressFromString(readNext(buffer)));
		emailMessage.setFrom((InternetAddress[]) MessageUtil.getAddressFromString(readNext(buffer)));
		tmp=readNext(buffer);
		emailMessage.setMessageNumber(Integer.parseInt(tmp));		
		emailMessage.setMessageSize(Integer.parseInt(readNext(buffer)));
		int contentCount=Integer.parseInt(readNext(buffer));
		for(int i=0;i<contentCount/2;i++) {
			tmp = readNext(buffer);
			if(TEXT_CONTENT.equals(tmp)) {
				emailMessage.getContentTypeList().add(tmp);
				emailMessage.getContentList().add(readNext(buffer));
			}
			else if(HTML_CONTENT.equals(tmp)) {
				emailMessage.getContentTypeList().add(tmp);
				emailMessage.getContentList().add(readNext(buffer));
			}
			else if(ATTACHMENTS.equals(tmp)) {
				EmailAttachment emailAttachment=new EmailAttachment();
				String str = readNext(buffer);
				int index = str.indexOf(SEPARATOR2);
				emailAttachment.setAttachmentName(str.substring(0, index));
				emailAttachment.setAttachmentSize(Integer.parseInt(str.substring(index+SEPARATOR2.length(),str.length())));
				emailMessage.getAttachmentList().add(emailAttachment);
			}
			
		}		
		/*tmp=readNext(buffer);
		while(null != tmp) {
			emailMessage.getAttachmentPaths().add(tmp);
			tmp = readNext(buffer);
			//size
			tmp = readNext(buffer);			
		}*/
		return emailMessage;
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
	
	public List<EmailMessage> readMessages(AccountInformation accountInformation,String folderName) throws MessagingException, IOException {
		List<EmailMessage> emailList = new ArrayList<EmailMessage>();
		File mailDirectory =new File(MailHandler.getBaseFolderName()+"\\"+accountInformation.getAccountName()+"\\"+folderName); 
		for(String fileName:mailDirectory.list()) {
			emailList.add(readMessage(accountInformation, folderName, fileName));
		}
		return emailList;
	}

	public EmailMessage readMessage(AccountInformation accountInformation,String folderName,String fileName) throws MessagingException, IOException {		
		EmailMessage message = null;
		String filePath=MailHandler.getBaseFolderName()+"\\"+accountInformation.getAccountName()+"\\"+folderName+"\\"+fileName;
		File file = new File(filePath);
		Message mimeMessage=new MimeMessage(MessageUtil.createDummySession(),new FileInputStream(file));
		message = getEmailMessage(mimeMessage, file.getName());
		return message;
	}
	
	public static MimeMessage getMessage(AccountInformation accountInformation,String folderName, String fileName) throws FileNotFoundException, MessagingException {
		File file = new File(MailHandler.getBaseFolderName()+"/"+accountInformation.getAccountName()+"_"+accountInformation.getUserName()+"/"+folderName+"/"+fileName);
		return new MimeMessage(MessageUtil.getTempSession(),new FileInputStream(file));
	}
	
	public EmailMessage getEmailMessage(Message message,String uid) throws MessagingException, IOException {
		EmailMessage emailMessage = new EmailMessage();		
		emailMessage.setUID(uid);
		emailMessage.setMessageSubject(message.getSubject());
		emailMessage.setReceivedDate(message.getReceivedDate());
		emailMessage.setSentDate(message.getSentDate());
		emailMessage.setTo((InternetAddress[]) message.getRecipients(RecipientType.TO));
		emailMessage.setCc((InternetAddress[]) message.getRecipients(RecipientType.CC));
		emailMessage.setBcc((InternetAddress[]) message.getRecipients(RecipientType.BCC));
		emailMessage.setFrom((InternetAddress[]) message.getFrom());
		emailMessage.setMessageNumber(message.getMessageNumber());
		emailMessage.setMessageSize(message.getSize());
		emailMessage.setContentList(getText(message));
		return emailMessage;
	}

	public static EmailMessage search(String uid,AccountInformation accountInformation,String folderName) throws EOFException {		
		String filePath=MailHandler.getBaseFolderName()+"\\"+accountInformation.getAccountName()+"\\"+folderName;								
		File directory = new File(filePath);
		if(directory.isDirectory()) {
			for(File file:directory.listFiles()) {
				if(file.getName().startsWith(uid)) {
					return EmailMessage.createEmailMessage(file);
				}
			}
		}
		return null;
	}
	
	public static Map<String, MessageIndex> readHeaders(AccountInformation accountInformation,String folderName) throws FileNotFoundException, IOException, ClassNotFoundException {		
		//List<MessageIndex> messageIndexes = new ArrayList<MessageIndex>();
		Map<String,MessageIndex> headerMap = new HashMap<String, MessageIndex>();
		File accountFolder = new File(MailHandler.getBaseFolderName()+"/"+accountInformation.getAccountName()+"_"+accountInformation.getUserName()+"/"+folderName);
		LOG.info("DAO.readHeaders: accountFolderPath => "+accountFolder.getAbsolutePath());
		File headerFile = null;
		ObjectInputStream is = null;
		LOG.info("accountFolder.exists => "+accountFolder.exists());
		LOG.info("accountFolder.isDirectory => "+accountFolder.isDirectory());
		if(accountFolder.exists() && accountFolder.isDirectory()) {			
			for(File headerFolder:accountFolder.listFiles()) {
				if(Consts.HEADER_FOLDER_NAME.equals(headerFolder.getName())) {
					for(File f2:headerFolder.listFiles()) {
						if(Consts.HEADER_FILE_NAME.equals(f2.getName())) {
							headerFile = f2;
							break;
						}
					}					
					LOG.info("header file found");
					break;
				}
			}
			if(null != headerFile && headerFile.exists() && headerFile.length() > 0) {
				LOG.info("DAO.readHeaders: headerFilePath => "+headerFile.getAbsolutePath());
				is = new ObjectInputStream(new FileInputStream(headerFile));
				headerMap = (Map<String, MessageIndex>) is.readObject();				
			}
		}
		LOG.info("headerMap length => "+headerMap.size());
		return headerMap;
	}

	public List<MessageIndex> readMessage(String accountName,String folderName) {
		MessageIndex messageIndex = null;
		ObjectInputStream is = null;
		List<MessageIndex> messageIndexList=new ArrayList<MessageIndex>();
		File fileName = new File(MailHandler.getBaseFolderName()+"\\"+accountName+"\\"+folderName);
		if(fileName.exists()) {
			try {								
				for(File file:fileName.listFiles()) {
					if(file.getAbsolutePath().endsWith(".dmp")) {
						is=new ObjectInputStream(new FileInputStream(file));
						try {
							EmailMessage.createEmailMessage(file);
							messageIndex = MessageIndex.createEmailMessage(file);
						}
						catch (EOFException e) {
							messageIndex = null;
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						if(null!= messageIndex) {
							messageIndexList.add(messageIndex);
						}					
					}
				}			
			}catch (FileNotFoundException e) {
				//e.printStackTrace();
				return messageIndexList;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					if(null != is) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}
		return messageIndexList;
	}

	public void saveAccountInf(AccountInformation accountInformation) {
		File accountDir=null;		
		File accFile=new File(MailHandler.getBaseFolderName());
		//ObjectOutputStream oos=null;
		if(accFile.isDirectory()) {
			for(File f:accFile.listFiles()) {
				if(f.isDirectory()) {
					if(f.getName().equals(accountInformation.getAccountName())) {
						return;
					}
				}
			}
			File f=null;
			try {
				String newAccountName=accountInformation.getAccountName()+"_"+accountInformation.getUserName();
				accountDir = new File(MailHandler.getBaseFolderName()+"/"+newAccountName);
				accountDir.mkdir();
				File attachmentFolder = new File(accountDir.getAbsolutePath()+"/"+Consts.ATTACHMENTS_FOLDER_NAME);
				attachmentFolder.mkdir();
				f=new File(accountDir.getAbsolutePath()+"/"+newAccountName+".acc");
				if(!f.exists()) {
					f.createNewFile();
				}
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(f));
				writer.write(accountInformation.toWrite());
				writer.close();
				//oos=new ObjectOutputStream(new FileOutputStream(f));
				//oos.writeObject(accountInformation);
			}catch(FileNotFoundException e1) {
				e1.printStackTrace();
			}
			catch(IOException e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public void updateAccountInf(AccountInformation accountInformation) {
		File accFile=new File(MailHandler.getBaseFolderName()+"\\"+accountInformation.getAccountName()+"\\"+accountInformation.getAccountName());
		if(accFile.exists()) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(accFile));
				writer.write(accountInformation.toWrite());
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}									
		}
	}
	
	public void updateAccountInf(AccountInformation oldAccountInformation,AccountInformation newAccountInformation) {
		File accDir=new File(MailHandler.getBaseFolderName()+"\\"+newAccountInformation.getAccountName()+"_"+newAccountInformation.getUserName());
		if(accDir.exists()) {
			try {
				File accFile=new File(MailHandler.getBaseFolderName()+"\\"+newAccountInformation.getAccountName()+"_"+newAccountInformation.getUserName()+"\\"+newAccountInformation.getAccountName()+"_"+newAccountInformation.getUserName()+".acc");
				BufferedWriter writer = new BufferedWriter(new FileWriter(accFile));
				writer.write(newAccountInformation.toWrite());
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}									
		}
		else {	
			accDir=new File(MailHandler.getBaseFolderName()+"\\"+oldAccountInformation.getAccountName()+"_"+oldAccountInformation.getUserName());
			if(accDir.exists() && accDir.isDirectory()) {				
				try {					
					File accFile = new File(accDir.getAbsolutePath()+"\\"+oldAccountInformation.getAccountName()+"_"+oldAccountInformation.getUserName()+".acc");
					File newFile = new File(accDir.getAbsolutePath()+"\\"+newAccountInformation.getAccountName()+"_"+newAccountInformation.getUserName()+".acc");
					newFile.createNewFile();
					accFile.delete();
					BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
					writer.write(newAccountInformation.toWrite());
					writer.close();				
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}		
			}
			accDir.renameTo(new File(MailHandler.getBaseFolderName()+"\\"+newAccountInformation.getAccountName()+"_"+newAccountInformation.getUserName()));			
		}
	}
	
	private static void deleteFolder(File file) {
		if(file.isDirectory()) {
			for(File f:file.listFiles()) {
				deleteFolder(f);
			}
			file.delete();
		}
		else {
			file.delete();
		}
	}

	public List<AccountInformation> readAccountInf() throws NoAccountException {
		File accDir=new File(MailHandler.getBaseFolderName());
		ObjectInputStream ois=null;
		AccountInformation accountInformation = new AccountInformation();
		List<AccountInformation> accList=new ArrayList<AccountInformation>();
		if(accDir.isDirectory()) {
			for(File f:accDir.listFiles()) {
				if(f.isDirectory()) {
					try {
						for(File file:f.listFiles()) {
							if(file.isFile() && file.getName().endsWith(".acc")) {
								accountInformation = readOneAccount(file);
								accList.add(accountInformation);
								break;
							}
						}							
					}catch(FileNotFoundException e1) {
						e1.printStackTrace();
					}
					catch(IOException e2) {
						e2.printStackTrace();
					} 
					finally {
						try {
							if(null != ois) {
								ois.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}			
		}
		try {	
			if(null != ois) {
				ois.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(0 == accList.size()) {
			throw new NoAccountException(NO_ACCOUNT_FOUND_MESSAGE);
		}
		else {
			return accList;
		}
	}
	
	private AccountInformation readOneAccount(File file) throws IOException {
		AccountInformation accountInformation = new AccountInformation();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String tmpStr;
		StringBuilder sb=new StringBuilder();						
		tmpStr = reader.readLine();
		while(null != tmpStr) {
			sb.append(tmpStr);
			tmpStr = reader.readLine();
		}
		reader.close();
		accountInformation.readString(sb);
		return accountInformation;
	}
	
	public List<String> readFolderStructure(File file,List<String> list) throws IOException {		
		if(file.exists()) {
			if(file.isDirectory()) {
				for(File f:file.listFiles()) {
					readFolderStructure(f, list);
				}
			}			
			list.add(file.getAbsolutePath());
		}		
		return list;
	}

	public void createFolder(String accountName,String folderName) throws IOException {
		File mailDir=new File(MailHandler.getBaseFolderName()+"\\"+accountName+"\\"+folderName);
		if(!mailDir.exists()) {
			if(!mailDir.mkdir()) {
				throw new IOException();
			}
		}		
	}
	
	public static StringBuilder loadConfig() throws IOException {
		File configFile=new File("config.dat");
		System.out.println(configFile.getAbsolutePath());
		Properties props = new Properties();
		props.load(new FileInputStream(configFile));
		System.out.println(props.get("BASE_FOLDER_LOCAION"));
		StringBuilder strBuilder=null;
		if(configFile.exists()) {
			strBuilder=new StringBuilder();
			String line=null;
			try {
				BufferedReader br=new BufferedReader(new FileReader(configFile));
				line=br.readLine();
				while(line != null) {
					strBuilder.append(line);
					line=br.readLine();
				}			
			} catch (FileNotFoundException e) {
				e.printStackTrace();			
			} catch (IOException e) {
				e.printStackTrace();			
			}		
		}
		else {
			throw new FileNotFoundException("configuration file not found");
		}
		return strBuilder;
	}
}

package email;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.swing.JOptionPane;

import network.MailHandler;

import com.sun.mail.util.BASE64DecoderStream;

import util.MessageUtil;

import account.AccountInformation;

import constants.Consts;

public class EmailMessage extends MessageIndex implements Consts {
	private static String ATTACHMENTS_FOLDER_NAME="ATTACHMENTS";
	private List<String> contentTypeList = new ArrayList<String>();
	private List<String> contentList = new ArrayList<String>();
	private List<EmailAttachment> attachmentList=new ArrayList<EmailAttachment>();
	private List<String> attachmentPaths = new ArrayList<String>();
	private Address[] replyTo;
	private AccountInformation accountInformation;
	
	public EmailMessage() {
		
	}
	
	public EmailMessage(Message message,AccountInformation accountInformation) {
		try {
			this.accountInformation = accountInformation;
			setUID(uid);
			setMessageSubject(message.getSubject());
			setReceivedDate(message.getReceivedDate());
			setSentDate(message.getSentDate());
			setFrom((InternetAddress[]) message.getFrom());
			setTo((InternetAddress[]) message.getRecipients(RecipientType.TO));
			setCc((InternetAddress[]) message.getRecipients(RecipientType.CC));
			setBcc((InternetAddress[]) message.getRecipients(RecipientType.BCC));
			setMessageSize(message.getSize());
			setReplyTo(message.getReplyTo());	
			writePart(message);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public List<EmailAttachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<EmailAttachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public List<String> getContentTypeList() {
		return contentTypeList;
	}

	public void setContentTypeList(List<String> contentTypeList) {
		this.contentTypeList = contentTypeList;
	}

	public List<String> getContentList() {
		return contentList;
	}

	public void setContentList(List<String> contentList) {
		this.contentList = contentList;
	}

	public List<String> getAttachmentPaths() {
		return attachmentPaths;
	}

	public void setAttachmentPaths(List<String> attachmentPaths) {
		this.attachmentPaths = attachmentPaths;
	}

	public Address[] getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(Address[] replyTo) {
		this.replyTo = replyTo;
	}

	public void writeTo(Message message,AccountInformation accountInformation,String folderName,String uid) {
		File file = null;
		this.accountInformation=accountInformation;
		try {	
			String fileName = createFileName(uid,message.getSubject());
			file = new File(MailHandler.getBaseFolderName()+"\\test.txt");
			if(!file.exists()) {
				file.createNewFile();
			}
			ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream(file));
			os.writeObject(uid);
			os.writeObject(message.getSubject());
			os.writeObject(message.getReceivedDate());
			os.writeObject(message.getSentDate());
			os.writeObject(message.getFrom());
			os.writeObject(message.getRecipients(RecipientType.TO));
			os.writeObject(message.getRecipients(RecipientType.CC));
			os.writeObject(message.getRecipients(RecipientType.BCC));
			os.writeObject(message.getSize());
			os.writeObject(message.getReplyTo());			
			writePart(message);
			os.writeObject(contentTypeList);
			os.writeObject(contentList);
			os.writeObject(attachmentPaths);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
			file.deleteOnExit();
		}
	}
	
	public static EmailMessage createEmailMessage(File file) throws EOFException {
		EmailMessage emailMessage=new EmailMessage();		
		ObjectInputStream is = null;
		try {		
			is=new ObjectInputStream(new FileInputStream(file));
			emailMessage.setUID((String) is.readObject());
			emailMessage.setMessageSubject((String) is.readObject());
			emailMessage.setReceivedDate((Date) is.readObject());
			emailMessage.setSentDate((Date) is.readObject());
			emailMessage.setFrom((InternetAddress[]) is.readObject());
			emailMessage.setTo((InternetAddress[]) is.readObject());
			emailMessage.setCc((InternetAddress[]) is.readObject());
			emailMessage.setBcc((InternetAddress[]) is.readObject());
			emailMessage.setMessageSize((Integer) is.readObject());	
			emailMessage.setReplyTo((Address[]) is.readObject());
			emailMessage.setContentTypeList((List<String>) is.readObject());
			emailMessage.setContentList((List<String>) is.readObject());
			emailMessage.setAttachmentPaths((List<String>) is.readObject());			
		}
		catch (EOFException e) {	
			try {
				if(null != is) {
					is.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
			throw new EOFException();
		}
		catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return emailMessage;
	}
	
	public void writePart(Part p) throws IOException, MessagingException {
		int level=0;
		int attm=0;
		System.out.println(p.getContentType());
		String filename = p.getFileName();
		if (filename != null)
		    pr("FILENAME: " + filename);
		if (p.isMimeType("text/plain")) {
		    pr("This is plain text");
		    pr("---------------------------");
			contentTypeList.add((String) p.getContentType());
			contentList.add((String) p.getContent());
			//os.writeObject(p.getContent());
		}
		if (p.isMimeType("text/html")) {
		    pr("This is html text");
		    pr("---------------------------");
			System.out.println((String)p.getContent());	
			contentTypeList.add((String) p.getContentType());
			contentList.add((String) p.getContent());
			//os.writeObject(p.getContent());
		}else if (p.isMimeType("multipart/*")) {
		    pr("This is a Multipart");
		    pr("---------------------------");
		    Multipart mp = (Multipart)p.getContent();
		    level++;
		    int count = mp.getCount();
		    for (int i = 0; i < count; i++)
		    	writePart(mp.getBodyPart(i));
		    level--;
		} else if (p.isMimeType("message/rfc822")) {
		    pr("This is a Nested Message");
		    pr("---------------------------");
		    level++;
		    writePart((Part)p.getContent());
		    level--;
		} else {
		    if (!false && !false) {
			/*
			 * If we actually want to see the data, and it's not a
			 * MIME type we know, fetch it and check its Java type.
			 */
			Object o = p.getContent();
			if (o instanceof String) {
			    pr("This is a string");
			    pr("---------------------------");
			    //System.out.println((String)o);
			    if(!MessageUtil.isInList(contentList, p.getContent())) {
			    	contentTypeList.add(p.getContentType());
					contentList.add(o.toString());
			    }
			} else if (o instanceof BASE64DecoderStream) {
			    pr("This is just an BASE64DecoderStream ");
			    pr("---------------------------");
			    //InputStream is = (BASE64DecoderStream)o;
			    //saveAttachment(is, filename);
			    try {
			    	File attachmentFolder = new File(MailHandler.getBaseFolderName()+"/"+accountInformation.getAccountName()+"_"+accountInformation.getUserName()+"/"+ATTACHMENTS_FOLDER_NAME);
			    	if(!attachmentFolder.exists()) {
			    		attachmentFolder.mkdir();
			    	}
					File f = new File(MailHandler.getBaseFolderName()+"/"+accountInformation.getAccountName()+"_"+accountInformation.getUserName()+"/"+ATTACHMENTS_FOLDER_NAME+"/"+filename);
					f=getUniqueFile(f);
					System.setProperty("mail.mime.base64.ignoreerrors", "true");
					saveAttachment((MimeBodyPart)p, filename);
					//((MimeBodyPart)p).saveFile(f);
				} catch (IOException ex) {
					pr("Failed to save attachment: " + ex);
				}
			}
			else if (o instanceof InputStream) {
			    pr("This is just an input stream");
			    pr("---------------------------");
			    InputStream is = (InputStream)o;
			    saveAttachment(is, filename);
			} else {
			    pr("This is an unknown type");
			    pr("---------------------------");
			    pr(o.toString());
			}
		    } else {
			// just a separator
			pr("---------------------------");
		    }
		}
		
		if (level != 0 && !p.isMimeType("multipart/*")) {
			String disp = p.getDisposition();
			// many mailers don't include a Content-Disposition
			if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
				if (filename == null)
					filename = "Attachment" + attm++;
				pr("Saving attachment to file " + filename);
				try {
					File f = new File(MailHandler.getBaseFolderName()+"\\"+accountInformation.getAccountName()+"\\"+ATTACHMENTS_FOLDER_NAME+"\\"+"_"+filename);
					f=getUniqueFile(f);
					//((MimeBodyPart)p).saveFile(f);
					saveAttachment((MimeBodyPart) p, filename);
				} catch (IOException ex) {
					pr("Failed to save attachment: " + ex);
				}
				pr("---------------------------");
			}
		}

	}
	
	private void saveAttachment(MimeBodyPart mp,String fileName) throws IOException, MessagingException {
		mp.saveFile(fileName);
		EmailAttachment emailAttachment = new EmailAttachment();
		emailAttachment.setAttachmentName(fileName);
		emailAttachment.setAttachmentSize(mp.getSize());
		emailAttachment.setAttachmentPath(fileName);
		attachmentList.add(emailAttachment);
	}
	
	public void saveAttachment(InputStream is,String fileName) {
		File f = null;
		int fileSuffix=0;
		boolean fileExists=false;		
		String extn;
		
		if(null != fileName && 0<fileName.length()) {
			while(true) {
				if(fileExists) {
					extn=getExtension(fileName);
					fileName = fileName.substring(0,fileName.lastIndexOf('.'));
					System.out.println("fileName = "+fileName+" extn : "+extn);
					f = new File(MailHandler.getBaseFolderName()+"/"+accountInformation.getAccountName()+"_"+accountInformation.getUserName()+"\\"+ATTACHMENTS_FOLDER_NAME+"\\"+fileName+"_"+fileSuffix+"."+extn);
				}
				else {
					f = new File(MailHandler.getBaseFolderName()+"/"+accountInformation.getAccountName()+"/"+"/"+accountInformation.getAccountName()+"_"+accountInformation.getUserName()+ATTACHMENTS_FOLDER_NAME+"/"+fileName);
				}			
				if (f.exists()) {
					fileExists=true;
					fileSuffix++;
				}
				else {
					break;
				}
			}

		}
				
	    System.out.println("saving attachment at"+f.getAbsolutePath());
	    attachmentPaths.add(f.getAbsolutePath());
	    OutputStream os = null;
	    BASE64DecoderStream in;	   
	    try {
	    	if(is instanceof BASE64DecoderStream) {
	    		System.out.println("BASE64DecoderStream");
	    		is= MimeUtility.decode(is, "binary");	    		
	    	}
	    	os = new FileOutputStream(f);
	    	//int data=is.read();
	    	byte buffer[]=new byte[100]; 
	    	while(-1 != is.read(buffer)) {			    	
	    		os.write(buffer);
	    		//os.write(data);
	    		//data=is.read();
	    		//if(-1 == data) {
	    		//break;
	    		//}

	    	}
	    }catch (EOFException e) {
			
		} 
	    catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			f.deleteOnExit();
		}catch (Exception e) {
			System.out.println("exception occurred...");
			f.deleteOnExit();
		}	 
		finally {
			if(null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
	}
	
	public void pr(String s){
		System.out.println(s);
	}
	
	public static File getUniqueFile(File file) {
		String fileName=file.getAbsolutePath();
		String extn;
		int fileSuffix=1;
		while(file.exists()) {
			extn=getExtension(fileName);
			if(fileName.lastIndexOf('.') >= 0)
				fileName = fileName.substring(0,fileName.lastIndexOf('.'));
			file = new File(fileName+"_"+fileSuffix+"."+extn);
			fileSuffix++;		
		}
		return file;
	}
	
	public static String getExtension(String s) {
        String ext = null;
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}

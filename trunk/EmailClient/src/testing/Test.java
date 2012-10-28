package testing;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;

import util.MessageUtil;
import account.AccountInformation;

import email.EmailMessage;
import filesystem.AppendableOutputStream;
import filesystem.DAO;

public class Test {
	public static final String FILE_PATH="D:\\My Projects\\EclipseProject\\MyMail\\src\\filesystem\\MessageDumps.dmp";
	/**
	 * @param args
	 */
	static int attnum = 1;
	public static final String ACC_DIR_PATH="D:\\Java\\Java Projects\\EclipseProjects\\EmailClient\\bin\\account\\accountfiles\\Gmail\\INBOX\\504_mailinstaller.dmp";
	/**
	 * @param args
	 * @throws EOFException 
	 */
	public static void main(String[] args) throws EOFException {
		File file=new File(ACC_DIR_PATH);
		EmailMessage emailMessage = EmailMessage.createEmailMessage(file);
		System.out.println(emailMessage.getMessageSubject());
		for(String content : emailMessage.getContentList()) {
			System.out.println(content);
		}
	}
	
	public static EmailMessage read(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream os=new ObjectInputStream(new FileInputStream(file));
		EmailMessage emailMessage=new EmailMessage();
		emailMessage.setMessageSubject((String) os.readObject());
		emailMessage.setReceivedDate((Date) os.readObject());
		emailMessage.setSentDate((Date) os.readObject());
		emailMessage.setFrom((InternetAddress[]) os.readObject());
		emailMessage.setTo((InternetAddress[]) os.readObject());
		emailMessage.setCc((InternetAddress[]) os.readObject());
		emailMessage.setBcc((InternetAddress[]) os.readObject());
		emailMessage.setMessageSize((Integer) os.readObject());	
		emailMessage.setReplyTo((Address[]) os.readObject());
		emailMessage.setContentTypeList((List<String>) os.readObject());
		emailMessage.setContentList((List<String>) os.readObject());
		emailMessage.setAttachmentPaths((List<String>) os.readObject());
		return emailMessage;
	}
	
}

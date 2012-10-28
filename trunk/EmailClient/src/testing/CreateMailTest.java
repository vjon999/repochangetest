package testing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import util.MessageUtil;

public class CreateMailTest {
	public static void main(String[] arg) {
		try {
		String filename="C:/Documents and Settings/Pratik/My Documents/My Pictures/11";
		MimeMessage mimeMessage=new MimeMessage(MessageUtil.getTempSession());
		mimeMessage.setFrom(new InternetAddress("abc@yahoo.com"));
		mimeMessage.addRecipient(RecipientType.TO,new InternetAddress("pratik006@gmail.com"));
		mimeMessage.setSubject("Photo attached");
		BodyPart bodyPart=new MimeBodyPart();
		bodyPart.setText("Take a look at this.");
		Multipart multipart=new MimeMultipart();
		multipart.addBodyPart(bodyPart);
		bodyPart=new MimeBodyPart();
		DataSource dataSource=new FileDataSource(filename);
		bodyPart.setDataHandler(new DataHandler(dataSource));
		bodyPart.setFileName(filename);
		bodyPart.setText("attachment");
		multipart.addBodyPart(bodyPart);
		mimeMessage.setContent(multipart);
		
		File file=new File("d:/mailtest.dmp");
		file.createNewFile();
		ObjectOutputStream fos=new ObjectOutputStream(new FileOutputStream(file));
		mimeMessage.writeTo(fos);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
}

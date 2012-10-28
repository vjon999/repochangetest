package testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import util.MessageUtil;

public class FileRenaming {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File folder=new File("D:\\Java\\Java Projects\\EclipseProjects\\EmailClient\\bin\\account\\accountfiles\\Gmail\\SENT");
		int count =0 ;
		System.out.println(folder.exists());
		for(File f:folder.listFiles()) {
			try {
				ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f));
				MimeMessage message = new MimeMessage(MessageUtil.getTempSession(),ois);
				String fileName = message.getMessageID();
				if(null == message.getMessageID() || fileName.length() == 0) {
					System.out.println(message.getSubject()+": "+f.getName());
					count++;
				}
				ois.close();
				//f.renameTo(new File(folder+"\\"+fileName));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(count);

	}

}

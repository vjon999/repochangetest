package testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

public class DumpMessage {

	/**
	 * @param args
	 */
	
	public static Map<String, Boolean> map = new HashMap<String, Boolean>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
dump();
	}
	
	public static void dump() {
		String userName = "pratik006@gmail.com";
		String password = "kitarpeed";
		String host = "imap.gmail.com";
		String path="d:/mails";
		File file = null;
		boolean complete=false;
		String maxUID = "537";

		Store store=null;
		Properties prop=null;
		Session session=null;
		Folder folder=null;


		prop=System.getProperties();
		session=Session.getInstance(prop);
		//store=session.getStore("imaps");


		try {
			loadMap(path);
			store=session.getStore("imaps");
			System.out.println("before ");
			store.connect(host,userName,password);
			System.out.println("connected..");
			folder=store.getFolder("INBOX");
			while(!complete) {
				try {
					folder.open(Folder.READ_ONLY);
					System.out.println("folder opened");
					Message messages[]=folder.getMessages();
					FetchProfile profile = new FetchProfile();
					//profile.add(FetchProfile.Item.ENVELOPE);
					profile.add(FetchProfile.Item.FLAGS);
					//profile.add("X-Mailer");
					profile.add(UIDFolder.FetchProfileItem.UID);
					String uid = null;
					int i=Integer.parseInt(maxUID);
					for(;i<messages.length;i++) {
						uid = String.valueOf(((IMAPFolder)folder).getUID(messages[i]));
						file = new File(path+"/"+uid+".dmp");
						if(isNew(uid)) {
							messages[i].writeTo(new FileOutputStream(file));
							System.out.println("subject: "+messages[i].getSubject());
							System.out.println("date: "+messages[i].getSentDate());
							maxUID = uid;
						}
						else {
							System.out.println("skipping: "+uid);
						}
					}
					complete = true;
				}catch (FolderClosedException e) {
					System.out.println("folder closed, file delete: "+file.delete());
				}
				catch (MessagingException e) {
					e.printStackTrace();
					System.out.println("file delete: "+file.delete());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (NoSuchProviderException e2) {
			e2.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadMap(String dir) {
		File file = new File(dir);
		for(String str:file.list()) {
			map.put(str, true);
		}
	}
	
	public static boolean isNew(String uid) {
		if(null == map.get(uid)) {
			return true;
		}
		return false;
	}

}

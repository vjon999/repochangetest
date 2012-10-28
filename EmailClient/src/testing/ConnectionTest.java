package testing;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;



	public class ConnectionTest   {
		
	
		
		public static void main(String[] arg) {
			/*String host="plus.pop.mail.yahoo.com";
			String userName="pratik_juprint@yahoo.com";
			String password="";*/
			
			String host="pop3.live.com";
			String userName="deepsengupta306@hotmail.com";
			String password="kunalshyna";
			Store store=null;
			Properties prop=null;
			Session session=null;
			Folder folder=null;
			
			
			prop=System.getProperties();
			session=Session.getInstance(prop);
			//store=session.getStore("imaps");
			
			try {
				store=session.getStore("pop3s");
				System.out.println("before ");
				store.connect(host,userName,password);
			System.out.println("connected..");
			folder=store.getFolder("INBOX");		
			folder.open(Folder.READ_ONLY);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	
	}


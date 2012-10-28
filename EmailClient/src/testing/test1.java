package testing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;


import account.AccountInformation;

import email.EmailMessage;
import exception.NoAccountException;
import filesystem.AppendableOutputStream;
import filesystem.DAO;

public class test1 {
	public static final String ACC_DIR_PATH="D:\\Java\\Java Projects\\EclipseProjects\\EmailClient\\bin\\account\\accountfiles\\Gmail_Pratik Sengupta\\INBOX.dmp";
	public static void main(String[] arg) {
		DAO dao=new DAO();
		List<EmailMessage> list = dao.read(new File(ACC_DIR_PATH));
		for(EmailMessage m:list) {
			//if(m.getContentList().get(0).length()<1000) {
				System.out.println(m.getUID()+" \t"+m.getMessageSubject());
				System.out.println("\t content = "+m.getContentList().get(0));
			//}
			
		}
		
	}
}

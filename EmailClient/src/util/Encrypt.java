package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import account.AccountInformation;

public class Encrypt {
	
	public static char[] encrypt(char[] ch) {		
		for(int i=0;i<ch.length;i++) {
			ch[i]=(char)((int)ch[i]+100+i);
		}
		return ch;
	}
	
	public static char[] decrypt(char[] ch) {
		char decryptedPassword[]=new char[ch.length];
		for(int i=0;i<ch.length;i++) {
			decryptedPassword[i]=(char)((int)ch[i]-100-i);
		}
		return decryptedPassword;
	}
public static void main(String arg[]) {
	String pass="pleasehelpme";
	AccountInformation accountInformation;
	File f=new File("D:\\Java\\Java Projects\\EclipseProjects\\EmailClient\\bin\\account\\accountfiles\\Hotmail\\Hotmail.acc");
	try {
		ObjectInputStream is=new ObjectInputStream(new FileInputStream(f));
		accountInformation = (AccountInformation) is.readObject();
		System.out.println(accountInformation.getPassword());
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
/*	pass = new String(Encrypt.encrypt(pass.toCharArray()));
	System.out.println(pass);
	pass = new String(Encrypt.decrypt(pass.toCharArray()));
	System.out.println(pass);*/
	
}
}

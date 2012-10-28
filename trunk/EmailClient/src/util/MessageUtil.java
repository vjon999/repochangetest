package util;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.swing.JFileChooser;


import com.sun.istack.internal.Nullable;

import constants.Consts;

import email.EmailMessage;
import email.MessageIndex;
import exception.BaseException;

public class MessageUtil {

	public static String getUniqueId(Message message) throws MessagingException {
		String uniqueId="";
		try {
			uniqueId+=message.getSentDate().getDate();
			uniqueId+=message.getSentDate().getMonth();
			uniqueId+=message.getSentDate().getYear();
		}
		catch(NullPointerException ne) {
			try {
				uniqueId+=message.getReceivedDate().getDate();
				uniqueId+=message.getReceivedDate().getMonth();
				uniqueId+=message.getReceivedDate().getYear();
			}
			catch(NullPointerException ne2) {
				uniqueId+="null_date";
			}
		}
		uniqueId=uniqueId+"#"+message.getSize();
		return uniqueId;
	}

	public static String getUniqueId(MessageIndex message) throws MessagingException {
		String uniqueId="";
		try {
			uniqueId+=message.getSentDate().getDate();
			uniqueId+=message.getSentDate().getMonth();
			uniqueId+=message.getSentDate().getYear();
		}
		catch(NullPointerException ne) {
			try {
				uniqueId+=message.getReceivedDate().getDate();
				uniqueId+=message.getReceivedDate().getMonth();
				uniqueId+=message.getReceivedDate().getYear();
			}
			catch(NullPointerException ne2) {
				uniqueId+="null_date";
			}
		}
		uniqueId=uniqueId+"#"+message.getMessageSize();
		return uniqueId;
	}

	public static Session getTempSession() {
		Properties prop=new Properties();
		Session sess=Session.getDefaultInstance(prop);
		return sess;
	}

	public static String getStringFromAddress(InternetAddress address[]) {
		String strAddress="";
		if(null != address) {
			for(int i=0;i<address.length;i++) {
				if(null != address[i]) {
					if(null != address[i].getPersonal()) {
						strAddress+=address[i].getPersonal();
					}
					if(null != address[i].getAddress()) {
						strAddress+="<"+address[i].getAddress()+">";
					}                        
					strAddress+=",";
				}
			}
		}

		return strAddress;
	}
	
	public static Address[] getAddressFromString(String str) {
		Address[] address = null;
		int i=0;
		List<String> list=new ArrayList<String>();
		if(null == str) {
			return null;
		}
		Scanner scanner=new Scanner(str);
		scanner.useDelimiter(",");
		while(scanner.hasNext()) {
			list.add(scanner.next());
		}
		address = new InternetAddress[list.size()];
		
		for(String s:list) {
			try {
				address[i]=new InternetAddress(s);
				i++;
			} catch (AddressException e) {
				//e.printStackTrace();
			}			
		}
		return address;
	}

	public static String getShortDate(Date date) {
		String shortDate;
		if(null != date) {
			shortDate=checkTwoDigit(date.getDate())+"/"+checkTwoDigit((date.getMonth()+1))+"/"+checkTwoDigit((1900+date.getYear()));
			return shortDate;
		}                
		return "";
	}

	public static String getTime(Date date) {
		String time;
		if(null != date) {
			time=checkTwoDigit(date.getHours())+":"+checkTwoDigit(date.getMinutes())+":"+checkTwoDigit(date.getSeconds());
			return time;
		}                
		return "";
	}        

	public static String dropQuotes(String content) {
		String str="";
		if((null != content) && (content.length() > 0)) {
			for(int i=0;i<content.length();i++) {
				if(content.charAt(i) == '"') {
					str+='\'';
				}
				else {
					str+=content.charAt(i);
				}
			}
		}        
		return str;
	}

	public static String checkTwoDigit(int number) {
		if(number < 10) {
			return "0"+number;
		}
		return number+"";
	}

	public static boolean equalsAddress(InternetAddress address1,InternetAddress address2) {
		if(address1.getAddress().equals(address2.getAddress())) {
			return true;
		}
		return false;
	}

	public static List<MessageIndex> reverseList(List<MessageIndex> messageIndexList) {
		List<MessageIndex> reversedList=new ArrayList<MessageIndex>();
		for(int i=messageIndexList.size()-1;i>=0;i--) {
			reversedList.add(messageIndexList.get(i));
		}                
		return reversedList;
	}

	public static byte[] toByteArray(char[] value) {
		byte[] byteArray=new byte[value.length];
		for(int i=0;i<value.length;i++) {
			byteArray[i]=(byte)value[i];
		}
		return byteArray;
	}

	public static File searchDirectory(File file,String folderName) {
		if(null != file && file.isDirectory()) {
			for(File f:file.listFiles()) {                        
				if(f.isDirectory() && f.getName().equalsIgnoreCase(folderName)) {
					return f;        
				}
				else {
					file=searchDirectory(f,folderName);
				}
			}
		}
		return file;
	}

	public static List<MessageIndex> sortByIndex(List<MessageIndex> list) {
		int max,index;
		MessageIndex tmp;
		for(int i=0;i<list.size();i++) {
			max=list.get(i).getMessageNumber();
			index=i;
			for(int j=i+1;j<list.size();j++) {
				if(max < list.get(j).getMessageNumber()) {
					max=list.get(j).getMessageNumber();
					index=j;
				}
			}
			tmp=list.get(i);
			list.set(i,list.get(index));
			list.set(index,tmp);
		}
		return list;
	}
	
	public static List<EmailMessage> sortByDate(List<EmailMessage> list) {
		int index;		
		long maxDate = 0,varDate = 0;
		EmailMessage tmp;
		List<EmailMessage> sortedList=new ArrayList<EmailMessage>();
		for(EmailMessage msgIndex:list) {
			sortedList.add(msgIndex);
		}
		for(int i=0;i<sortedList.size();i++) {
			if(null != sortedList.get(i).getReceivedDate() && Consts.NULL.equals(sortedList.get(i).getReceivedDate())) {
				varDate=sortedList.get(i).getReceivedDate().getTime();
			}
			else if(null != sortedList.get(i).getSentDate() && Consts.NULL.equals(sortedList.get(i).getReceivedDate())) {
				varDate=sortedList.get(i).getReceivedDate().getTime();
			}
			maxDate = varDate;
			index=i;
			for(int j=i+1;j<sortedList.size();j++) {
				if(null != sortedList.get(j).getReceivedDate()  &&  Consts.NULL.equals(sortedList.get(i).getReceivedDate())) {
					if(maxDate < sortedList.get(j).getReceivedDate().getTime()) {
						maxDate=sortedList.get(j).getReceivedDate().getTime();
						index=j;
					}
				}
				else {
					if(null != sortedList.get(j).getSentDate()  &&  Consts.NULL.equals(sortedList.get(i).getSentDate())) {
						if(maxDate < sortedList.get(j).getSentDate().getTime()) {
							maxDate=sortedList.get(j).getSentDate().getTime();
							index=j;
						}
					}
				}				
			}
			tmp=sortedList.get(i);
			sortedList.set(i,list.get(index));
			sortedList.set(index,tmp);
		}
		return sortedList;
	}

	public static List<MessageIndex> sortBySize(List<MessageIndex> list) {
		int max,index;
		MessageIndex tmp;
		List<MessageIndex> sortedList=new ArrayList<MessageIndex>();
		for(MessageIndex msgIndex:list) {
			sortedList.add(msgIndex);
		}
		for(int i=0;i<sortedList.size();i++) {
			max=sortedList.get(i).getMessageSize();
			index=i;
			for(int j=i+1;j<sortedList.size();j++) {
				if(max > sortedList.get(j).getMessageSize()) {
					max=sortedList.get(j).getMessageSize();
					index=j;
				}
			}
			tmp=sortedList.get(i);
			sortedList.set(i,list.get(index));
			sortedList.set(index,tmp);
		}
		return sortedList;
	}

	public static byte[] encrypt(byte[] input) throws BaseException {
		try {
			Encryptor encryptor = new Encryptor();
			return encryptor.encrypt(input);
		} catch (BaseException e) {
			e.printStackTrace();
			throw new BaseException(e.getMessage());    			
		}    	
	}

	public static byte[] decrypt(byte[] input) throws BaseException {
		try {
			Encryptor encryptor = new Encryptor();
			return encryptor.decrypt(input);
		} catch (BaseException e) {
			e.printStackTrace();
			throw new BaseException(e.getMessage());    			
		}    	
	}

	private static boolean equalsSubject(Message message1,Message message2) throws MessagingException {
		if(null == message1.getSubject()) {
			if(null == message2.getSubject()) {
				return true;
			}			
		}
		else {
			if(null == message2.getSubject()) {
				return true;
			}
			else {
				if(message2.getSubject().equals(message1.getSubject())) {
					return true;
				}				
			}
		}
		return false;
	}

	private static boolean equalsFrom(Message message1,Message message2) throws MessagingException {
		if(null == message1.getFrom()) {
			if(null == message2) {
				return true;
			}
		}
		else if(null == message2.getFrom()) {
			if(null == message1.getFrom()) {
				return true;
			}
		}
		else {
			if(0 >= message1.getFrom().length) {
				if(0 >= message2.getFrom().length) {
					return true;
				}
			}
			else if(0 >= message2.getFrom().length){
				if(0 >= message1.getFrom().length) {
					return true;
				}
			}
			else {
				if(message2.getFrom().equals(message1.getFrom())) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean equalsSentDate(Message message1,Message message2) throws MessagingException {
		if(null == message1.getSentDate()) {
			if(null == message2.getSentDate()) {
				return true;
			}
		}
		else if(null == message2.getSentDate()) {
			if(null == message1.getSentDate()) {
				return true;
			}
		}
		else {
			if(message2.getSentDate().equals(message1.getSentDate())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEquals(Message message1,Message message2) throws MessagingException {
		if(equalsSubject(message1,message2)) {
			if(equalsSentDate(message1,message2)) {
				if(equalsFrom(message1,message2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean equalsSubject(MessageIndex message1,Message message2) throws MessagingException {
		if(null == message1.getMessageSubject()) {
			if(null == message2.getSubject()) {
				return true;
			}			
		}
		else {
			if(null == message2.getSubject()) {
				return true;
			}
			else {
				if(message2.getSubject().equals(message1.getMessageSubject())) {
					return true;
				}				
			}
		}
		return false;
	}

	private static boolean equalsFrom(MessageIndex message1,Message message2) throws MessagingException {
		if(null == message1.getFrom()) {
			if(null == message2) {
				return true;
			}
		}
		else if(null == message2.getFrom()) {
			if(null == message1.getFrom()) {
				return true;
			}
		}
		else {
			if(0 >= message1.getFrom().length) {
				if(0 >= message2.getFrom().length) {
					return true;
				}
			}
			else if(0 >= message2.getFrom().length){
				if(0 >= message1.getFrom().length) {
					return true;
				}
			}
			else {
				if(message2.getFrom().equals(message1.getFrom())) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean equalsSentDate(MessageIndex message1,Message message2) throws MessagingException {
		if(null == message1.getSentDate()) {
			if(null == message2.getSentDate()) {
				return true;
			}
		}
		else if(null == message2.getSentDate()) {
			if(null == message1.getSentDate()) {
				return true;
			}
		}
		else {
			if(message2.getSentDate().equals(message1.getSentDate())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isEquals(MessageIndex message1,Message message2) throws MessagingException {
		if(equalsSubject(message1,message2)) {
			if(equalsSentDate(message1,message2)) {
				if(equalsFrom(message1,message2)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isMessageInList(Message message,List<Message> messageList) throws MessagingException {
		for(Message msg:messageList) {
			if(isEquals(message, msg)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMessagePresent(MessageIndex message,List<Message> messageList) throws MessagingException {
		for(Message msgIndex:messageList) {	
			if(message.getMessageSubject().equals(msgIndex.getSubject())) {
				if(message.getSentDate().equals(msgIndex.getSentDate())) {
					if(MessageUtil.equalsAddress(message.getFrom()[0], (InternetAddress) msgIndex.getFrom()[0])) {
						//if(message.getMessageSize()+44 == msgIndex.getSize()) {
							System.out.println(message.getMessageSize()+" , "+ msgIndex.getSize());
							return true;
							//}
					}
				}
			}
		}
		return false;
	}

	public static boolean isMessageSaved(int messageNumber,List<MessageIndex> messageIndexList) throws MessagingException {
		for(MessageIndex msgIndex:messageIndexList) {	
			if(msgIndex.getMessageNumber() == messageNumber) {
				if(msgIndex.isSaved()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static String dropSpecialChars(String str) {
		String result="";
		int asciiVal=0;
		if(null != str) {
			for(int i=0;i<str.length();i++) {
				asciiVal = (int)str.charAt(i); 
				if(asciiVal > 47 && asciiVal < 126) {
					result+=str.charAt(i);
				}
			}
		}
		return result;
	}
	
	public static boolean isInList(List list,Object object) {
		for(int i=0;i<list.size();i++) {
			if(object.equals(list.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	public static List<MessageIndex> getListFromMap(Map<String,MessageIndex> map) {
		List<MessageIndex> list = new ArrayList<MessageIndex>();
		for(String key:map.keySet()) {
			list.add(map.get(key));
		}
		return list;
	}
	
	public static Session createDummySession() {
		Properties prop=null;
		Session session=null;
		prop=System.getProperties();
		session=Session.getInstance(prop);
		return session;
	}
} 
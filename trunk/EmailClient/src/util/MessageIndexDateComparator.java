package util;

import java.util.Comparator;

import javax.mail.Message;
import javax.mail.MessagingException;

import email.MessageIndex;

public class MessageIndexDateComparator implements Comparator<MessageIndex>{
	@Override
	public int compare(MessageIndex arg0, MessageIndex arg1) {
		if(null == arg0.getReceivedDate() && null != arg1.getReceivedDate()) {
			if(arg0.getReceivedDate().getTime() > arg1.getReceivedDate().getTime()) {
				return 1;
			}
			else if(arg0.getReceivedDate().getTime() == arg1.getReceivedDate().getTime()) {
				return 0;
			}
			else if(arg0.getReceivedDate().getTime() < arg1.getReceivedDate().getTime()) {
				return -1;
			}
		}
		else {
			if(arg0.getSentDate().getTime() > arg1.getSentDate().getTime()) {
				return 1;
			}
			else if(arg0.getSentDate().getTime() == arg1.getSentDate().getTime()) {
				return 0;
			}
			else if(arg0.getSentDate().getTime() < arg1.getSentDate().getTime()) {
				return -1;
			}
		}
		return -2;
	}
	
	public int compare(Message arg0, Message arg1) throws MessagingException {
		if(null == arg0.getReceivedDate() && null != arg1.getReceivedDate()) {
			if(arg0.getReceivedDate().getTime() > arg1.getReceivedDate().getTime()) {
				return 1;
			}
			else if(arg0.getReceivedDate().getTime() == arg1.getReceivedDate().getTime()) {
				return 0;
			}
			else if(arg0.getReceivedDate().getTime() < arg1.getReceivedDate().getTime()) {
				return -1;
			}
		}
		else {
			if(arg0.getSentDate().getTime() > arg1.getSentDate().getTime()) {
				return 1;
			}
			else if(arg0.getSentDate().getTime() == arg1.getSentDate().getTime()) {
				return 0;
			}
			else if(arg0.getSentDate().getTime() < arg1.getSentDate().getTime()) {
				return -1;
			}
		}
		return -2;
	}
}

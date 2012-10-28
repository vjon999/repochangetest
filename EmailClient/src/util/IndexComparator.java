package util;

import java.util.Comparator;

import email.MessageIndex;


public class IndexComparator implements Comparator<MessageIndex>{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
	
	@Override
	public int compare(MessageIndex arg0, MessageIndex arg1) {
		return arg0.getMessageNumber()>arg1.getMessageNumber() ?1:-1;
	}
}

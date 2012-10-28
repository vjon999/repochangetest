package network;

import java.util.Calendar;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

public class Search {
	
	public static final int SEARCH_SUBJECT=1;
	public static final int SEARCH_FROM=2; 
	Message messages[];
	Folder folder;
	
	public Search(Folder folder) {
		this.folder=folder;
	}
	
	private SearchTerm searchSubject(String subject) throws MessagingException {
		SearchTerm term = null;
		if(null != subject) {
			term = new SubjectTerm(subject);			
		}
		return term;
	}
	
	private SearchTerm searchDate(Calendar startDate,Calendar endDate) throws MessagingException {
		SearchTerm term = null;
		if(null != startDate && null != endDate) {
			ReceivedDateTerm startDateTerm = new ReceivedDateTerm(ComparisonTerm.GE,startDate.getTime());
			ReceivedDateTerm endDateTerm = new ReceivedDateTerm(ComparisonTerm.GE,endDate.getTime());
			term = new AndTerm(startDateTerm,endDateTerm);			
		}
		return term;
	}
	
	private SearchTerm searchFrom(String from) throws MessagingException {
		SearchTerm term = null;
		if(null != from) {
			term = new FromStringTerm(from);
			messages = folder.search(term);
		}
		return term;
	}
	
	public SearchTerm search(String str,int searchType) throws MessagingException {
		if(SEARCH_SUBJECT == searchType) {
			return searchSubject(str);
		}
		return searchFrom(str);		
	}
	
	public SearchTerm search(String subject,Calendar startDate,Calendar endDate) throws MessagingException {
		return new AndTerm(searchSubject(subject),searchDate(startDate, endDate));
	}
	
	public SearchTerm search(String subject,String from) throws MessagingException {
		return new AndTerm(searchSubject(subject),searchFrom(from));
	}
	
	public SearchTerm search(String subject,String from,Calendar startDate,Calendar endDate) throws MessagingException {
		return new AndTerm(search(subject, from),searchDate(startDate, endDate));
	}
}

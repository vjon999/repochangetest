package database;


import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;

public class MailDAO {
	Connection conn=null;
	Statement stmt=null;
	String dbPath="";
	InternetAddress address[];
	String from,to[],subject,content;
	Date date;
	
	public MailDAO() throws SQLException {
		conn=new JDBCConnection("").createConnection();
		stmt=conn.createStatement();
	}
	
	public void saveMessage(Message message) throws SQLException {
		/*date=(java.sql.Date) message.getSentDate();
		address=(InternetAddress[]) message.getFrom();
		from="";
		for(InternetAddress addr:address) {
			from+=addr.getAddress()+" ,";
		}
		subject=message.getSubject();
		if((!message.getContentType().equals(Message.ATTACHMENT)) || (!message.getContentType().equals(Message.INLINE))) {
			content=message.getContent().toString();
		}
		String query="INSERT INTO MAIL('Sent_Date','From','To','Subject','Content') ";
		stmt.executeUpdate(query);*/
	}
}

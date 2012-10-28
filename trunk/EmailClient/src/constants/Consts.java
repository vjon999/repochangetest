package constants;

import java.awt.Color;

public interface Consts {
	public static final String SEPARATOR=" :_!";
	public static final String SEPARATOR2="=-";
	public static final String NULL="null";
	public static final String ACCOUNT_NAME_KEY="accountName";
	public static final String TITLE_KEY="title";
	public static final String MESSAGE_INDEX_KEY="index";
	public static final String MESSAGE_KEY="messages";
	public static final String TABLE_KEY="table";
	public static final String TABLE_RENDERER_KEY="tableRenderer";
	public static final Color UNSAVED_ROW_COLOR=Color.BLACK;
	public static final Color SAVED_ROW_COLOR=Color.BLUE;
	public static final String INBOX_KEY="Inbox";
	public static final String NO_ACCOUNT_SELECTED_MSG="Please Select one account";
	public static final String[] COLUMN_HEADERS = { "Saved","No","Size (KB)","Received Date", "From","Subject" };
	//public static final String[] ACCOUNT_LEAFS={"Inbox","Sent"};
	public static final String[] CONNECTION_STATUSES={"Connected","Disconnected","Opened","Connecting"};
	public static final int STATUS_CONNECTED=0;
	public static final int STATUS_DICONNECTED=1;
	public static final int STATUS_OPENED=2;
	public static final int STATUS_CONNECTING=3;
	public static final String STATUS_CONNECTING_STAGES[]={"Conecting.","Conecting..","Conecting..."};
	
	public static final String DOWNLOAD_STATUSES[]={"Downloading: ","Idle"};
	public static int STATUS_DOWNLOADING=1;
	public static int STATUS_IDLE=2;
	public static final String DEFAULT_PASSWORD_TEXT = "DEFAULT PASSWORD";
	
	//public static final String[] FOLDER_TYPES={"Inbox","Sent"};
	//public static final int INBOX=0;
	//public static final int SENT=1;	
	public static final String DEFAULT_CONNECTION_lABEL_TEXT="Connection Status: ";
	public static final String DEFAULT_NUMBER_OF_MAILS_lABEL_TEXT="Total Mails: ";
	public static final String DEFAULT_UNREAD_MAILS_lABEL_TEXT="Unread Mails: ";
	public static final String DEFAULT_COPIED_MAILS_lABEL_TEXT="Copied Mails: ";
	public static final String DEFAULT_NEW_MAILS_lABEL_TEXT="New Mails: ";
	public static final String NO_ACCOUNT_FOUND_MESSAGE="No Account Found";
	public static final String MAIL_SAVER_RUNNING_MESSAGE="One thread is already saving mails";
	public static final String INDEX_READER_RUNNING_MESSAGE="One thread is already reading indexes";
	
	//Form Validation messages
	public static final String SUCCESSFULLY_SENT_MESSAGE="Message Successfully Sent";
	public static final String MANDATORY_FIELD_MESSAGE="Mandatory Field";
	public static final String CONNECTION_FAILED_MESSAGE="Connection to the mail server cannot be made.";
	public static final String FOLDER_OPEN_FAILED_MESSAGE="Cannot open folder";	
	public static final String ATTACHMENTS_FOLDER_NAME="ATTACHMENTS";
	
	public static final String GMAIL="GMAIL";
	public static final String HOTMAIL="HOTMAIL";
	public static final String YAHOO="YAHOO";
	public static final String OTHER="OTHER";
	
	public static final String GMAIL_INCOMING_SERVER="imap.gmail.com";
	public static final String HOTMAIL_INCOMING_SERVER="pop3.live.com";
	public static final String YAHOO_INCOMING_SERVER="plus.pop.mail.yahoo.com";
	
	public static final String GMAIL_OUTGOING_SERVER="smtp.gmail.com";
	public static final String HOTMAIL_OUTGOING_SERVER="smtp.live.com";
	public static final String YAHOO_OUTGOING_SERVER="plus.smtp.mail.yahoo.com";
	
	public static final String IMAP="IMAP";
	public static final String POP3="POP3";
	public static final String DEFAULT_FOLDER_NAME="INBOX";
	
	// configuration keys
	public static final String BASE_FOLDER_LOCATION = "BASE_FOLDER_LOCATION";
	public static final String HEADER_FOLDER_NAME = "Header";
	public static final String HEADER_FILE_NAME = "Header.dmp";
	public static final String DEAULT_BASE_FOLDER_LOCATION = "C:/EmailClient";
	
	public static final String TEXT_CONTENT="text/plain";
	public static final String HTML_CONTENT="text/html";
	public static final String OTHER_CONTENT="text/*";
	public static final String ATTACHMENTS="attachments";
	
	public static final String EMAIL_CLIENT_DOWNLOAD_MODE = "downloadmode";
	public static final String EMAIL_CLIENT_DOWNLOAD_MODES[] = {"fullmode","basicmode"};
	public static final int EMAIL_CLIENT_DOWNLOAD_FULL_MODE = 0;
	public static final int EMAIL_CLIENT_DOWNLOAD_BASIC_MODE = 1;
}

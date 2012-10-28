package account;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import threads.BasicMailSaverThread;
import util.MessageUtil;
import constants.Consts;
import email.EmailMessage;
import email.MessageIndex;
import filesystem.DAO;
import network.Connection;
import network.MailHandler;
import network.UIDStore;

public class MailAccount implements Consts {

	private MailHandler mailHandler;
	private AccountInformation accountInformation;
	private int savedMessageCount=0;
	private int messageCount=-1;
	private int newMessageCount=0;
	private Map<String,List<EmailMessage>> indexListMap=new HashMap<String, List<EmailMessage>>();
	private Map<String,Map<String,MessageIndex>> headerListMap = new HashMap<String, Map<String,MessageIndex>>();
	private UIDStore uidStore;
	private boolean mailSaverRunning=false;	
	private String connectionStatus=CONNECTION_STATUSES[STATUS_DICONNECTED];	
	List<Runnable> threadList=new ArrayList<Runnable>();
	private List<String> folders=new ArrayList<String>();

	public MailAccount(MailHandler mailHandler,AccountInformation accountInformation) {
		this.mailHandler=mailHandler;
		this.accountInformation=accountInformation;
		try {
			uidStore = new UIDStore(accountInformation);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file=new File(MailHandler.getBaseFolderName()+"\\"+accountInformation.getAccountName()+"_"+accountInformation.getUserName());
		if(file.exists()) {
			for(File f:file.listFiles()) {
				if(f.isDirectory()) {				
					folders.add(f.getName());
					DAO dao=new DAO();
					List<EmailMessage> list = dao.read(f);
					list=MessageUtil.sortByDate(list);
					savedMessageCount = list.size();
					indexListMap.put(f.getName(), list);
				}
			}		
		}
	}
	
	public void addFolder(String folderName) {
		int index=folderName.lastIndexOf(".");
		if(-1 != index) {
			folderName = folderName.substring(0,index);
		}		
		folders.add(folderName);
		indexListMap.put(folderName, new ArrayList<EmailMessage>());
		if(null != mailHandler.getMainFrame()) {
			mailHandler.getMainFrame().refreshAccount(accountInformation);
		}
		
	}
	
	public List<String> getFolders() {
		return folders;
	}

	public MailHandler getMailHandler() {
		return mailHandler;
	}

	public void setMailHandler(MailHandler mailHandler) {
		this.mailHandler = mailHandler;
	}

	public AccountInformation getAccountInformation() {
		return accountInformation;
	}

	public void setAccountInformation(AccountInformation accountInformation) {
		this.accountInformation = accountInformation;
	}

	public int getSavedMessageCount() {
		return savedMessageCount;
	}

	public void setSavedMessageCount(int messageCount) {
		this.savedMessageCount = messageCount;
	}

	public int getMessageCount() {
		if(-1 == messageCount) {
			return savedMessageCount;
		}
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;		
		mailHandler.getMainFrame().setMailCount(messageCount);
	}

	public int getNewMessageCount() {
		return newMessageCount;
	}

	public void setNewMessageCount(int newMessageCount) {
		this.newMessageCount = newMessageCount;
	}
	
	public Map<String,MessageIndex> getHeaderMap(String folderName) {
		if(null == headerListMap.get(folderName) || headerListMap.get(folderName).size() == 0) {
			try {
				headerListMap.put(folderName, DAO.readHeaders(accountInformation, folderName));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return headerListMap.get(folderName);
	}
	
	public void addHeader(MessageIndex messageIndex,String folderName) {
		if(null == headerListMap.get(folderName)) {
			headerListMap.put(folderName, new HashMap<String,MessageIndex>());
		}
		headerListMap.get(folderName).put(messageIndex.getUID(),messageIndex);
	}
	
	public List<EmailMessage> getIndexList(String folderName) {
		return indexListMap.get(folderName);
	}	

	public void addMessageIndexList(EmailMessage emailMessage,String folderName) {
		if(null == indexListMap.get(folderName)) {
			indexListMap.put(folderName, new ArrayList<EmailMessage>());
		}
		indexListMap.get(folderName).add(0,emailMessage);
		savedMessageCount++;
	}

	public boolean isMailSaverRunning() {
		return mailSaverRunning;
	}

	public void setMailSaverRunning(boolean mailSaverRunning) {
		this.mailSaverRunning = mailSaverRunning;
	}

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
		mailHandler.getMainFrame().setStatus(this);
	}

	public UIDStore getUidStore() {
		return uidStore;
	}

	public void setUidStore(UIDStore uidStore) {
		this.uidStore = uidStore;
	}

	public void sendMail(MimeMessage message) throws MessagingException {
		Connection.sendMail(accountInformation,message,null);
	}	 

	public EmailMessage readMessage(MessageIndex messageIndex,String folderName)  {
		DAO dao=new DAO();	
		EmailMessage emailMessage = null;
		try {
			emailMessage = (dao.readMessage(accountInformation, folderName,MessageIndex.createFileName(messageIndex.getUID(), messageIndex.getMessageSubject())));
		} catch (EOFException e) {
			System.out.println("EOF exception encountered, removing uid");
			uidStore.remove(messageIndex.getUID());
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return emailMessage;
	}

	public List<MessageIndex> loadIndex(String accountName,String folderName) {
		DAO dao=new DAO();
		return dao.readMessage(accountName,folderName);
	}

	public List<Runnable> getThreadList() {
		return threadList;
	}

	public void setThreadList(List<Runnable> threadList) {
		this.threadList = threadList;
	}
	public void closeConection() {
		for(Runnable runnable: threadList) {
			if(runnable instanceof BasicMailSaverThread) {
				((BasicMailSaverThread)runnable).close();
			}
		}
	}
}

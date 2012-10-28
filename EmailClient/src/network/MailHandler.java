package network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import email.MessageIndex;
import exception.NoAccountException;
import filesystem.DAO;
import gui.AccountFrame;
import gui.MainFrame;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import threads.BasicMailSaverThread;
import threads.HeaderThread;
import threads.MailSaverThread;
import constants.Consts;
import account.AccountInformation;
import account.MailAccount;

public class MailHandler implements Consts {
	private static final Log LOG = LogFactory.getLog(MailHandler.class);
	private Map<String,MailAccount> accountMap=new HashMap<String,MailAccount>();
	private static Map<String,String> configMap = new HashMap<String,String>();
	private List<MessageIndex> curMessageIndexList = new ArrayList<MessageIndex>();
	private String currentFolderName = null;
	private MailAccount selectedAccount=null;
	private String connectionStatus=null;
	private Boolean indexClosed=true;
	private Boolean mailClosed=false;
	private Boolean exitSystem=false;
	private boolean accountExists=false;
	private boolean indexReaderRunning=false;
	private boolean mailSaverRunning=false;	
	private MainFrame mainFrame=null;
	private static String baseFolderName;
	
	public MailHandler() {		
		loadConfigFile();
		baseFolderName=configMap.get(Consts.BASE_FOLDER_LOCATION);
		while(!accountExists) {
			try {
				for(AccountInformation ac:(new DAO()).readAccountInf()) {
					accountMap.put(ac.getAccountName(),new MailAccount(this,ac));
				}
				accountExists=true;
			}catch (NoAccountException e) {
				new AccountFrame(this);
				try {
					synchronized(this) {						
						wait();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		/*for(AccountInformation ac:accountList) {
			loadAccount(ac, indexListMap, messageListMap);
		}*/
		mainFrame=new MainFrame(this);
		//updateTable();
		//markCopiedMails();		
		mainFrame.setVisible(true);
		connectionStatus=CONNECTION_STATUSES[STATUS_DICONNECTED];
	}
	
	public static void main(String args[]) {
		MailHandler mailHandler =   new MailHandler();
	}
	
	public void addAccount(AccountInformation accountInformation) {
		accountMap.put(accountInformation.getAccountName(),new MailAccount(this,accountInformation));
		if(null != mainFrame) {
			mainFrame.addAccount(accountInformation);
		}		
	}
	
	public List<MessageIndex> getCurMessageIndexList() {
		return curMessageIndexList;
	}

	public void setCurMessageIndexList(List<MessageIndex> curMessageIndexList) {
		this.curMessageIndexList = curMessageIndexList;
	}

	public String getCurrentFolderName() {
		return currentFolderName;
	}

	public void setCurrentFolderName(String currentFolderName) {
		this.currentFolderName = currentFolderName;
	}

	public static String getBaseFolderName() {
		return baseFolderName;
	}

	public boolean isAccountExists() {
		return accountExists;
	}

	public void setAccountExists(boolean accountExists) {
		this.accountExists = accountExists;
	}
	
	/**
	 * @return the indexReaderRunning
	 */
	public synchronized boolean isIndexReaderRunning() {
		return indexReaderRunning;
	}

	/**
	 * @param indexReaderRunning the indexReaderRunning to set
	 */
	public synchronized void setIndexReaderRunning(boolean indexReaderRunning) {
		this.indexReaderRunning = indexReaderRunning;
	}

	/**
	 * @return the mailSaverRunning
	 */
	public synchronized boolean isMailSaverRunning() {
		return mailSaverRunning;
	}

	/**
	 * @param mailSaverRunning the mailSaverRunning to set
	 */
	public synchronized void setMailSaverRunning(boolean mailSaverRunning) {
		this.mailSaverRunning = mailSaverRunning;
	}

	public void sendMail(AccountInformation accountInformation,MimeMessage message) throws MessagingException {
		Connection.sendMail(accountInformation,message);
	}		
		

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setMessageCount(int messageCount) {
		mainFrame.setMailCount(messageCount);
	}

	public MailAccount getSelectedAccount() {
		return selectedAccount;
	}

	public void setSelectedAccount(String accountName) {
		if(null == accountName) {
			selectedAccount=null;
		}
		else {
			selectedAccount = accountMap.get(accountName);
		}
	}
	
	public void addRow(MessageIndex messageIndex,String accountName) {
		mainFrame.addRow(messageIndex,accountName);
	}
	
	public List<String> getAccountNames() {
		List<String> accountNamesList=new ArrayList<String>();
		for(String key:accountMap.keySet()) {
			accountNamesList.add(key);
		}
		return accountNamesList;
	}
	public MailAccount getAccount(String key) {
		return accountMap.get(key);
	}

	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
		mainFrame.setConnectionStatus(connectionStatus);
	}		
	
	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	public Boolean isIndexClosed() {
		return indexClosed;
	}

	public synchronized void setIndexClosed(Boolean indexClosed) {
		this.indexClosed = indexClosed;
	}

	public Boolean isMailClosed() {
		return mailClosed;
	}

	public synchronized void setMailClosed(Boolean mailClosed) {
		this.mailClosed = mailClosed;
	}

	public Boolean isExitSystem() {
		return exitSystem;
	}

	public synchronized void setExitSystem(Boolean exitSystem) {
		this.exitSystem = exitSystem;
	}
	
	public synchronized void notifyToCloseSystem() {
		indexClosed=true;
		notify();
	}
	
	public synchronized void closeSystem() {
		MailAccount mailAccount = null;
		for(String key:accountMap.keySet()) {
			mailAccount = accountMap.get(key);
			for(Runnable runnable:mailAccount.getThreadList()) {
				if(runnable instanceof HeaderThread) {
					HeaderThread headerThread = (HeaderThread) runnable;
					while(!headerThread.isClosed()) {
						headerThread.close();
						synchronized (runnable) {
							try {
								LOG.info("waiting for Headerthread to close..");
								headerThread.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					LOG.info("Headerthread closed");
				}
				if(runnable instanceof MailSaverThread) {
					MailSaverThread mailSaverThread = (MailSaverThread) runnable;
					mailSaverThread.close();
					LOG.info("MailSaverThread closed");
				}
			}
		}
		
		
		exitSystem=true;
		synchronized (this) {
			while(!indexClosed) {
				try {
					wait();					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	

		}
		MailAccount ma;
		boolean safeToClose=false;
		while(safeToClose) {
			safeToClose=true;
		for(String key:accountMap.keySet()) {
			ma=accountMap.get(key);
			for(Runnable r:ma.getThreadList()) {
				if(r instanceof BasicMailSaverThread) {
					if(((BasicMailSaverThread) r).getDao().isWriting()) {
						safeToClose=false;
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						break;
					}					
				}	
				else if(r instanceof HeaderThread) {
					((HeaderThread) r).close();
				}
			}
			if(!safeToClose) {
				break;
			}
		}
		}
		System.exit(0);
	}
	
	public void updateCopiedMessages(int updateIndex) {
		mainFrame.markAsCopied(updateIndex);
	}
	
/*	public void markCopiedMails() {
		List<Message> msgList= messageListMap.get(selectedAccount.getAccountName());
		List<MessageIndex> msgIndexList= indexListMap.get(selectedAccount.getAccountName());
		if(null != msgList && null != msgIndexList) {
			for(Message message:msgList) {
				for(int i=msgIndexList.size()-1;i>=0;i--) {
					try {
						if(msgIndexList.get(i).isEquals(message)) {
							if(i >=0) {
								updateCopiedMessages(i);
							}
							break;
						}
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}*/
	
	/*public void loadAccount(AccountInformation accountInformation,Map<String,List<MessageIndex>> indexListMap,Map<String,List<Message>> messageListMap) {
		String folderName=FOLDER_TYPES[INBOX];
		List<Message> messageList=new ArrayList<Message>();
		List<MessageIndex> messageIndexList=new ArrayList<MessageIndex>();
		DAO dao=new DAO();
		messageIndexList=dao.readMessage(accountInformation.getAccountName(),folderName);
		//messageList=dao.readMessages(accountInformation);
		//markSavedMessages(messageIndexList,messageList);
		//messageIndexList=MessageUtil.sortByIndex(messageIndexList);			
		indexListMap.put(accountInformation.getAccountName(),messageIndexList);
		messageListMap.put(accountInformation.getAccountName(), messageList);		
	}*/
	
	public void markSavedMessages(List<MessageIndex> indexList,List<Message> messageList) {
		try {int c=0;
			for(Message message:messageList) {
				for(int i=0;i<indexList.size();i++) {				
					if(indexList.get(i).isEquals(message)) {
						indexList.get(i).setSaved(true);
						c++;
					}
				}
			}
		}catch(MessagingException e) {

		}
	}
	
	
	
	public void loadConfigFile() {
		File configFile = null;
		try {
			configFile=new File("config.dat");
			if(!configFile.exists()) {				
				configFile.createNewFile();
				FileWriter writer = new FileWriter(configFile);
				writer.write(Consts.BASE_FOLDER_LOCATION+" = "+DEAULT_BASE_FOLDER_LOCATION);
				writer.close();
			}
			Properties properties = new Properties();
			properties.load(new FileInputStream(configFile));
			File baseFolder = new File(properties.getProperty(Consts.BASE_FOLDER_LOCATION));
			if(!baseFolder.exists()) {
				JOptionPane.showMessageDialog(new javax.swing.JFrame(), "Cannot load config file, exiting");
				System.exit(0);
			}
			else {
				configMap.put(Consts.BASE_FOLDER_LOCATION, properties.getProperty(Consts.BASE_FOLDER_LOCATION));
				configMap.put(Consts.EMAIL_CLIENT_DOWNLOAD_MODE, properties.getProperty(Consts.EMAIL_CLIENT_DOWNLOAD_MODE));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getConfigurationProperty(String key) {
		return configMap.get(key);
	}
}

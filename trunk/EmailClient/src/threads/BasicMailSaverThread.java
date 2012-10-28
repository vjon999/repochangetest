package threads;

import java.io.IOException;
import java.net.ConnectException;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.swing.JOptionPane;
import network.Connection;
import network.MailHandler;
import network.UIDStore;
import account.AccountInformation;
import account.MailAccount;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

import email.EmailMessage;
import email.MessageIndex;
import filesystem.DAO;

public class BasicMailSaverThread implements Runnable {

	MailAccount mailAccount;
	Thread t=null;
	Folder folder=null;
	Store store=null;
	boolean closeFlag=false;
	DAO dao=new DAO();
	
	public BasicMailSaverThread(MailAccount mailAccount) {
		this.mailAccount=mailAccount;
		t=new Thread(this,"basicmailsaver");
		t.start();
	}

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	@Override
	public void run() {
		int connectattempt=5;
		AccountInformation accountInformation=mailAccount.getAccountInformation();
		UIDStore uids = mailAccount.getUidStore();
		try {
			while(connectattempt>0 && store==null) {
				try {
					mailAccount.setMailSaverRunning(true);
					//JOptionPane.showMessageDialog(mailAccount.getMailHandler().getMainFrame(), "here");
					store=Connection.connect(mailAccount);					
					connectattempt=0;
					break;
				}catch (MessagingException e) {
					mailAccount.setMailSaverRunning(false);
					connectattempt--;
				}				
			}
			if(null != store) {				
				Folder defaultFolder = store.getDefaultFolder();
				for(Folder f:defaultFolder.list()){
					if(closeFlag) {
						break;
					}
					FetchProfile profile = new FetchProfile();
					//profile.add(FetchProfile.Item.ENVELOPE);
					profile.add(FetchProfile.Item.FLAGS);
					//profile.add("X-Mailer");
					profile.add(UIDFolder.FetchProfileItem.UID);
					if(accountInformation.getIncomingMailServerType().equals(AccountInformation.MAIL_TYPES[AccountInformation.POP3_TYPE])) {
						//POP3Folder folder=Connection.getPOP3Folder(store, folderName.toUpperCase());
						JOptionPane.showMessageDialog(mailAccount.getMailHandler().getMainFrame(), "here");
						folder=(POP3Folder)f;
						folder.open(Folder.READ_ONLY);
						Message[] messages = folder.getMessages();
						folder.fetch(messages,profile);
						for(int i = 0;i < messages.length;i++) {
							String uid = ((POP3Folder)folder).getUID(messages[i]);			         
							if(uids.isNew(uid)) {
								EmailMessage emailMessage = downloadBasicMessage(folder.getMessage(i+1),f.getFullName(),uid);
								uids.store(uid);
								if(null != mailAccount.getIndexList(folder.getFullName()) && null!=emailMessage) {
									mailAccount.addMessageIndexList(emailMessage,folder.getFullName());
								}
								mailAccount.getMailHandler().getMainFrame().refreshTable(mailAccount, folder.getFullName());
							}
						}
					}
					else if(accountInformation.getIncomingMailServerType().equals(AccountInformation.MAIL_TYPES[AccountInformation.IMAP_TYPE])) {
						//IMAPFolder folder=Connection.getIMAPFolder(store, folderName);
						folder=(IMAPFolder)f;
						folder.open(Folder.READ_ONLY);
						Message[] messages = folder.getMessages();	
						mailAccount.setMessageCount(folder.getMessageCount());
						folder.fetch(messages,profile);
						for(int i = 0;i < messages.length;i++) {
							if(closeFlag) {
								if(folder.isOpen()) {
									folder.close(true);
								}
								break;
							}
							String uid = String.valueOf(((IMAPFolder)folder).getUID(messages[i]));							
							if(uids.isNew(uid)) {
								System.out.println("uid = "+uid);
								String savePath=MailHandler.getBaseFolderName()+"\\"+mailAccount.getAccountInformation().getAccountName()+"_"+mailAccount.getAccountInformation().getUserName()+"\\"+folder.getFullName();
								EmailMessage emailMessage = downloadBasicMessage(messages[i],f.getFullName(),uid);
								DAO.saveMail(messages[i], savePath);
								uids.store(uid);								
								if(null!=emailMessage) {									
									mailAccount.addMessageIndexList(emailMessage,folder.getFullName());
									mailAccount.getMailHandler().getMainFrame().addNewMessage(mailAccount, folder.getFullName(), emailMessage);
								}								
							}

						}
					}
				}
			}	
			else {
				JOptionPane.showMessageDialog(mailAccount.getMailHandler().getMainFrame(), "Unable to connect to "+accountInformation.getIncomingServerAddress());
			}
			if(null != store) {
				store.close();
			}
		}catch(MessagingException messagingException) {
			messagingException.printStackTrace();
			if(null != store && store.isConnected()) {
				try {
					store.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				mailAccount.setMailSaverRunning(false);
			}
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(null != store) {
				try {
					store.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void downloadMessage(Message message,Folder folder,String uid) {		
		dao.writeMessage(message,mailAccount.getAccountInformation(),folder.getFullName(),uid);
	}

	public EmailMessage downloadBasicMessage(Message message,String folderName,String uid) throws MessagingException {
		DAO dao=new DAO();		
		return dao.writeBasicMessage(message, mailAccount,folderName,uid);
	}
	
	public void createFolderStructure(Store store,Folder folder) throws MessagingException, IOException {
		DAO dao=new DAO(); 
		if(null == folder) {
			folder = store.getDefaultFolder();			
			for(Folder f: folder.list()) {
				createFolderStructure(store, f);
			}
		}
		else {
			System.out.println("full name  = "+folder.getFullName());
			dao.createFolder(mailAccount.getAccountInformation().getAccountName(), folder.getFullName());
			for(Folder f: folder.list()) {
				createFolderStructure(store, f);
			}
		}
	}
	public void close() {closeFlag=true;
		/*if(folder != null) {
			try {
				folder.close(true);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		if(store != null) {
			try {
				store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}*/
	}
}

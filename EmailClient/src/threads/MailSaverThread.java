package threads;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import network.MailHandler;
import account.AccountInformation;
import account.MailAccount;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

import email.EmailMessage;
import filesystem.DAO;

public class MailSaverThread extends AbstractThread {
	private Folder folder = null;
	public MailSaverThread(MailAccount mailAccount) {
		super(mailAccount, "MailSaverThread");
	}

	@Override
	protected void handleFolder(IMAPFolder folder, FetchProfile profile)
			throws MessagingException, IOException {
		this.folder = folder;
		saveMails(folder, profile);
	}

	@Override
	protected void handleFolder(POP3Folder folder, FetchProfile profile)
			throws MessagingException, IOException {
		this.folder = folder;
		saveMails(folder, profile);
	}

	private void saveMails(Folder folder, FetchProfile profile) throws MessagingException {
		Map<String, String> uidStore = null;
		Message[] messages = folder.getMessages();
		mailAccount.setMessageCount(folder.getMessageCount());
		folder.fetch(messages, profile);
		uidStore = loadSavedMailUIDs(getFolderPath(folder));
		for (int i = 0; i < messages.length; i++) {
			if (closeFlag) {
				if (folder.isOpen()) {
					folder.close(true);
				}
				break;
			}
			String uid = String.valueOf(((IMAPFolder) folder).getUID(messages[i]));
			if(!uidStore.containsKey(uid)) {
				LOG.info("uid = " + uid + "\tsubject => "+ messages[i].getSubject());
				try {
					DAO.saveMail(messages[i], getFolderPath(folder)+"/"+uid+".dmp");
					LOG.info("Message saved successfully...");
				}catch (IOException e) {
					LOG.info(e.getStackTrace());
				}
			}
			else {
				LOG.info("uid = "+uid+" => Message already present");
			}			
			//mailAccount.addMessageIndexList(messages[i], folder.getFullName());
			//mailAccount.getMailHandler().getMainFrame().addNewMessage(mailAccount, folder.getFullName(), messages[i]);
		}

	}
	
	private Map<String,String> loadSavedMailUIDs(String folderPath) {
		Map<String, String> uidStore = new HashMap<String, String>();
		
		return uidStore;
	}
	
	private String getFolderPath(Folder folder) {
		String folderPath = MailHandler.getBaseFolderName() + "/"
		+ mailAccount.getAccountInformation().getAccountName()
		+ "_" + mailAccount.getAccountInformation().getUserName()
		+ "/" + folder.getFullName();
		return folderPath;
	}
	
	public void close() {
		try {
			if(folder.isOpen())
				folder.close(true);
			if(folder.getStore().isConnected())
				folder.getStore().close();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}

package threads;

import java.io.File;
import java.io.IOException;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.swing.JOptionPane;
import network.Connection;
import network.MailHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.security.ssl.DefaultSSLContextImpl;
import account.AccountInformation;
import account.MailAccount;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPSSLStore;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3SSLStore;
import com.sun.mail.pop3.POP3Store;

import constants.Consts;

import filesystem.MyLogger;
import gui.MainFrame;

public abstract class AbstractThread implements Runnable {
	protected static MyLogger LOG = new MyLogger(AbstractThread.class ,MainFrame.console);
	public static final int BUFFER_SIZE = 20;
	protected MailAccount mailAccount;
	protected Thread t = null;
	protected Store store = null;
	protected boolean closeFlag = false;
	protected boolean closed = false;
	protected String relativeBasePath = null;

	public AbstractThread(MailAccount mailAccount,String threadName) {		
		LOG.info("logging info...");
		this.mailAccount = mailAccount;
		relativeBasePath = MailHandler.getBaseFolderName() + "\\"
				+ mailAccount.getAccountInformation().getAccountName() + "_"
				+ mailAccount.getAccountInformation().getUserName();
		t = new Thread(this, threadName);
		t.start();
	}

	public boolean isClosed() {
		return closed;
	}

	public synchronized void setClosed(boolean closed) {
		this.closed = closed;
		mailAccount.setMailSaverRunning(false);
		this.notifyAll();
		LOG.info("notifying");
	}
	
	private void initStore() throws MessagingException {
		int connectattempt = 5;
		while (connectattempt > 0 && store == null) {
			try {
				mailAccount.setMailSaverRunning(true);
				store = Connection.connect(mailAccount);
				connectattempt = 0;
				break;
			} catch (MessagingException e) {
				mailAccount.setMailSaverRunning(false);
				connectattempt--;
			}
		}
		if (null == store || !store.isConnected()) {
			LOG.info("Unable to connect to " + mailAccount.getAccountInformation().getIncomingServerAddress()+", store is null or not connected");
			JOptionPane.showMessageDialog(mailAccount.getMailHandler()
					.getMainFrame(), "Unable to connect to "
					+ mailAccount.getAccountInformation().getIncomingServerAddress());
			throw new MessagingException("Connection failed");
		}
	}

	@Override
	public void run() {
		FetchProfile profile = createProfile();		
		try {
			initStore();
			if(store instanceof POP3SSLStore || store instanceof POP3Store) {
				openFolder((POP3Store)store, profile);
			}
			else if(store instanceof IMAPSSLStore || store instanceof IMAPStore) {
				openFolder((IMAPStore)store, profile);
			}		
			if (null != store && store.isConnected()) {
				store.close();
			}
		} catch (MessagingException messagingException) {
			LOG.info(messagingException.getMessage());
			if (null != store && store.isConnected()) {
				try {
					store.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				mailAccount.setMailSaverRunning(false);
			}
		} catch (IOException e) {
			LOG.info("IOException occurred, "+e.getMessage());
		} finally {
			if (null != store && store.isConnected()) {
				try {
					store.close();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
			LOG.info("Closing HeaderThread");
			setClosed(true);
		}
	}
	
	protected void openFolder(POP3Store store,FetchProfile profile) throws MessagingException, IOException {
		POP3Folder folder = (POP3Folder) store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		switch (folder.getType()) {
		case Folder.HOLDS_MESSAGES:
			handleFolder(folder, profile);
			break;
		default:
			LOG.info("Folder doesnot holds messages");
		}
		handleFolder(folder, profile);
	}
	
	protected void openFolder(IMAPStore store, FetchProfile profile) throws MessagingException, IOException {
		//IMAPFolder folder = (IMAPFolder) store.getDefaultFolder();
		for(Folder folder:store.getDefaultFolder().list()) {
			switch (folder.getType()) {
			case Folder.HOLDS_FOLDERS | Folder.HOLDS_MESSAGES:
			case Folder.HOLDS_MESSAGES:
				folder.open(Folder.READ_ONLY);
				LOG.info("Folder " + folder.getName() + " holds messages");
				handleFolder((IMAPFolder) folder, profile);
				break;
			case Folder.HOLDS_FOLDERS:
				FolderParser(folder);
				LOG.info("Folder " + folder.getName() + " doesnot hold sub-folders");
				break;
			default:
				LOG.info("Folder " + folder.getName() + " unknown type");
			}
		}
	}
	
	protected abstract void handleFolder(IMAPFolder folder,FetchProfile profile) throws MessagingException, IOException;
	protected abstract void handleFolder(POP3Folder folder,FetchProfile profile) throws MessagingException, IOException;

	private void FolderParser(Folder folder) throws MessagingException {
		File curFolder = new File(relativeBasePath + "/" + folder.getFullName());
		if (!curFolder.exists()) {
			curFolder.mkdir();
			LOG.info(curFolder.getAbsoluteFile() + " -> folder created");
		}
		for (Folder f : folder.list()) {
			FolderParser(f);
		}
	}
	
	private FetchProfile createProfile() {
		FetchProfile profile = new FetchProfile();
		// profile.add(FetchProfile.Item.ENVELOPE);
		profile.add(FetchProfile.Item.FLAGS);
		// profile.add("X-Mailer");
		profile.add(UIDFolder.FetchProfileItem.UID);
		return profile;
	}

	public void close() {
		closeFlag = true;
	}
}

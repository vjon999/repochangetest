package threads;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import network.MailHandler;

import account.MailAccount;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;

import constants.Consts;

import email.MessageIndex;
import filesystem.DAO;

public class HeaderThread extends AbstractThread {
	
	public HeaderThread(MailAccount mailAccount) {
		super(mailAccount,"Header Thread");
	}
	
	@Override
	protected void handleFolder(IMAPFolder folder, FetchProfile profile) throws MessagingException, IOException {
		readHeaders(folder, profile);
	}
	@Override
	protected void handleFolder(POP3Folder folder, FetchProfile profile) throws MessagingException, IOException {
		readHeaders(folder, profile);
	}
	
	private Map<String, MessageIndex> readHeaders(POP3Folder folder,FetchProfile profile) throws MessagingException, IOException {
		Map<String, MessageIndex> headerMap = mailAccount.getHeaderMap(folder.getFullName());
		File headerFile = getHeaderFile(folder);
		Message[] messages = folder.getMessages();
		folder.fetch(messages, profile);		
		if (messages.length > 0) {
			mailAccount.setMessageCount(folder.getMessageCount());		
			for (int i = 0; i < messages.length; i++) {
				if (closeFlag) {
					DAO.syncHeader(headerMap, headerFile);
					if (folder.isOpen()) {
						folder.close(true);
					}
					break;
				}
				String uid = folder.getUID(messages[i]);
				if (!mailAccount.getHeaderMap(folder.getFullName()).containsKey(uid)) {
					MessageIndex messageIndex = readHeader(messages[i],uid,headerFile);
					headerMap.put(String.valueOf(messageIndex.getUID()), messageIndex);
				}
			}
		}
		return headerMap;
	}
	
	private Map<String, MessageIndex> readHeaders(IMAPFolder folder,FetchProfile profile) throws MessagingException, IOException {
		Map<String, MessageIndex> headerMap = mailAccount.getHeaderMap(folder.getFullName());
		File headerFile = getHeaderFile(folder);
		Message[] messages = folder.getMessages();
		folder.fetch(messages, profile);		
		if (messages.length > 0) {
			mailAccount.setMessageCount(folder.getMessageCount());		
			for (int i = 0; i < messages.length; i++) {
				if (closeFlag) {
					DAO.syncHeader(headerMap, headerFile);
					if (folder.isOpen()) {
						folder.close(true);
					}
					break;
				}
				long uid = folder.getUID(messages[i]);
				if (!mailAccount.getHeaderMap(folder.getFullName()).containsKey(uid)) {
					MessageIndex messageIndex = readHeader(messages[i],String.valueOf(uid),headerFile);
					headerMap.put(String.valueOf(messageIndex.getUID()), messageIndex);
				}
				else {
					LOG.info("uid "+uid+" already present in map => "+mailAccount.getHeaderMap(folder.getFullName()).get(uid));
				}
			}
		}
		return headerMap;
	}

	private MessageIndex readHeader(Message message,String uid, File headerFile) throws MessagingException {
		MessageIndex messageIndex = new MessageIndex((MimeMessage) message, headerFile.getAbsolutePath());
			messageIndex.setUID(uid);
			LOG.info("uid = " + messageIndex.getUID() + "\tsubject => "
					+ messageIndex.getMessageSubject());
		
		return messageIndex;
	}
	
	private File getHeaderFile(Folder folder) throws IOException {
		File headerFile = new File(MailHandler.getBaseFolderName() + "/"
				+ mailAccount.getAccountInformation().getAccountName() + "_"
				+ mailAccount.getAccountInformation().getUserName() + "/"
				+ folder.getFullName());
		if (headerFile.exists() && headerFile.isDirectory()) {
			headerFile = new File(headerFile.getAbsolutePath() + "/"
					+ Consts.HEADER_FOLDER_NAME);
			if (!headerFile.exists())
				headerFile.mkdir();
			headerFile = new File(headerFile.getAbsolutePath() + "/"
					+ Consts.HEADER_FILE_NAME);
			if (!headerFile.exists())
				headerFile.createNewFile();
			return headerFile;
		}
		return null;
	}
}

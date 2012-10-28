package threads;

import email.EmailMessage;
import gui.AttachmentDownloadFrame;

import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.sun.mail.imap.IMAPFolder;

import network.Connection;
import account.AccountInformation;

public class AttachmentDownloaderThread implements Runnable {
	
	private AttachmentDownloadFrame frame;
	private Thread t;	
	private Store store = null;
	private Folder folder = null;
	private boolean stop=false;
	
	public AttachmentDownloaderThread(AttachmentDownloadFrame attachmentDownloadFrame) {
		frame = attachmentDownloadFrame;		
		t=new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		
		try {
			FetchProfile profile = new FetchProfile();			
			profile.add(UIDFolder.FetchProfileItem.UID);
			if(stop) {
				return;
			}
			store = Connection.connect(frame.getMailAccount().getAccountInformation());
			frame.setConnectionStatus(3);	
			if(stop) {
				return;
			}
			folder = store.getFolder(frame.getFolderName());			
			folder.open(Folder.READ_ONLY);
			frame.setConnectionStatus(4);
			if(stop) {
				return;
			}
			Message message = folder.getMessage(frame.getEmailMessage().getMessageNumber());
			frame.setConnectionStatus(5);
			if(stop) {
				return;
			}
			saveAttachment(message, frame.getEmailAttachment().getAttachmentName());
			frame.setDownloadComplete(true);

		} catch (MessagingException e) {
			if(e.getNextException() instanceof java.net.UnknownHostException) {
				int ans = JOptionPane.showConfirmDialog(frame,"Error: "+e.getNextException().getMessage()+"\nDo you want to Retry ?","Error",JOptionPane.YES_NO_OPTION );
				if(JOptionPane.YES_OPTION == ans) {
					frame.restart();
				}
				else {
					frame.setDownloadComplete(true);
				}
			}
			else {
				int ans = JOptionPane.showConfirmDialog(frame,"Error: "+e.getMessage()+"\nDo you want to Retry ?","Error",JOptionPane.YES_NO_OPTION );
				if(JOptionPane.YES_OPTION == ans) {
					frame.restart();
				}
				else {
					frame.setDownloadComplete(true);
				}
			}
			e.printStackTrace();
		} catch (IOException e) {
			int ans = JOptionPane.showConfirmDialog(frame,"Error: "+e.getMessage()+"\nDo you want to Retry ?","Error",JOptionPane.YES_NO_OPTION );
			if(JOptionPane.YES_OPTION == ans) {
				frame.restart();
			}
			else {
				frame.setDownloadComplete(true);
			}
			e.printStackTrace();
		}		
		finally {
			if(folder != null) {
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
			}
		}
		System.out.println("store closed");
	}
	
	public void saveAttachment(Part p,String fileName) throws IOException, MessagingException {
		
		if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart)p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				saveAttachment(mp.getBodyPart(i),fileName);
		} else if (p.isMimeType("message/rfc822")) {
			saveAttachment((Part) p.getContent(),fileName);
		} else {

			/*
			 * If we actually want to see the data, and it's not a
			 * MIME type we know, fetch it and check its Java type.
			 */
			Object o = p.getContent();
			if (o instanceof InputStream) {
				InputStream is=(InputStream) o;
				if(p.getFileName().equals(fileName)) {
					File file=new File("d:/"+fileName);
					JFileChooser fc=new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fc.setSelectedFile(new File(fileName));
					fc.showOpenDialog(frame);
					file=fc.getSelectedFile();
					BufferedOutputStream os=new BufferedOutputStream(new FileOutputStream(file));
					int i=((InputStream) o).read();
					int count=0,size=p.getSize();
					while(-1 != i) {
						os.write(i);
						count++;
						if(count%10 == 0) {
							frame.setComplete(count);
							System.out.println(count*100/size+"%");
						}
						i=((InputStream) o).read();
					}				
					os.close();
				}	
				else {
					System.out.println("not matched..."+p.getFileName());
				}
			}

		}	
	}
	public void stopThread() {
		stop = true;
		if(null != folder && folder.isOpen()) {
			try {
				folder.close(true);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		if(null != store) {
			try {
				store.close();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
		frame.setDownloadComplete(true);
	}
	

}

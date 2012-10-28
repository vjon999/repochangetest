package threads;

import javax.mail.MessagingException;
import javax.mail.Store;

import network.MailHandler;

import account.MailAccount;

public class KillerThread implements Runnable {

	MailAccount mailAccount;
	MailHandler mailHandler;
	Store store;
	Thread t;
	
	public KillerThread(MailHandler mailHandler,Thread t,MailAccount mailAccount,Store store) {
		this.mailAccount=mailAccount;
		this.mailHandler=mailHandler;
		this.store=store;
		this.t=t;
		t=new Thread(this,"killer thread");
		t.start();
	}
	
	
	@Override
	public void run() {	
		while(true) {
			if(mailHandler.isExitSystem()) {
				t.stop();
				try {
					store.close();
					mailHandler.setIndexClosed(true);
					System.out.println("closing...");
					mailHandler.notifyToCloseSystem();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

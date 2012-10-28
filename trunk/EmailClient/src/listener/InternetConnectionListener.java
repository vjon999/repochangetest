package listener;

import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import account.MailAccount;
import constants.Consts;

public class InternetConnectionListener implements ConnectionListener,Consts {
	
	MailAccount mailAccount=null;	
	
	public InternetConnectionListener(MailAccount mailAccount) {
		this.mailAccount=mailAccount;
	}
	
	@Override
	public void closed(ConnectionEvent arg0) {
		System.out.println("Connection Closed");
		mailAccount.setConnectionStatus("Connection Closed");
	}

	@Override
	public void disconnected(ConnectionEvent arg0) {
		System.out.println("disconnected");
		mailAccount.setConnectionStatus("disconnected");		
	}

	@Override
	public void opened(ConnectionEvent arg0) {
		System.out.println("Connected");
		mailAccount.setConnectionStatus(CONNECTION_STATUSES[STATUS_CONNECTED]);
	}

}

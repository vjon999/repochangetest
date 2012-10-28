package threads;

import account.MailAccount;
import constants.Consts;
import network.MailHandler;

public class StatusThread implements Runnable,Consts {
	Thread t;
	MailAccount mailAccount;
	
	public StatusThread(MailAccount mailAccount) {
		this.mailAccount=mailAccount;
		t=new Thread(this,"status thread");
		t.start();
	}
	
	@Override
	public void run() {
		int count = 0;
		while(mailAccount.isMailSaverRunning()) {
			if(mailAccount.getConnectionStatus().equalsIgnoreCase(CONNECTION_STATUSES[STATUS_CONNECTED])) {
				break;
			}
			mailAccount.setConnectionStatus(STATUS_CONNECTING_STAGES[count%STATUS_CONNECTING_STAGES.length]);
			//System.out.println(STATUS_CONNECTING_STAGES[count%STATUS_CONNECTING_STAGES.length]);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count++;
		}
	}
}

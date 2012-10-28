package listener;

import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

public class MyMessageCountListener implements MessageCountListener {

	@Override
	public void messagesAdded(MessageCountEvent arg0) {
		System.out.println("message added...");
	}

	@Override
	public void messagesRemoved(MessageCountEvent arg0) {
		System.out.println("message removed...");
	}

}

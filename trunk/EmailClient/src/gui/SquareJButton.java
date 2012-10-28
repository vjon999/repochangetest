package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import network.Connection;
import account.AccountInformation;
import threads.HeaderThread;
import constants.Consts;

public class SquareJButton extends JButton implements ActionListener {

	private static final long serialVersionUID = 2378583415596492718L;
	private MainFrame mainFrame = null;
	private NewMailFrame newMail = null;

	public SquareJButton(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		/*setPreferredSize(GUIConsts.DEFAULT_BUTTON_DIMENSION);
		setSize(GUIConsts.DEFAULT_BUTTON_DIMENSION);
		setMaximumSize(GUIConsts.DEFAULT_BUTTON_DIMENSION);
		setMinimumSize(GUIConsts.DEFAULT_BUTTON_DIMENSION);*/
		setFont(GUIConsts.DEFAULT_FONT);
	    setIconTextGap(0);
	    setMargin(new java.awt.Insets(2, 0, 2, 0));
		addActionListener(this);
	}
	
	public SquareJButton(NewMailFrame createMail) {
		this.newMail = createMail;
		setFont(GUIConsts.DEFAULT_FONT);
	    setIconTextGap(0);
	    setMargin(new java.awt.Insets(2, 0, 2, 0));
		addActionListener(this);
	}

	public SquareJButton(MainFrame mainFrame, String title) {
		this(mainFrame);
		setText(title);
		setToolTipText(title);
	}
	
	public SquareJButton(NewMailFrame createMail, String title) {
		this(createMail);
		setText(title);
		setToolTipText(title);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		String actionCommand = evt.getActionCommand();
		if (actionCommand.equals(GUIConsts.SEND_RECEIVE_BUTTON_TEXT)) {
			if (null == mainFrame.getMailHandler().getSelectedAccount()) {
				JOptionPane.showMessageDialog(getParent(),
						Consts.NO_ACCOUNT_SELECTED_MSG);
			} else {
				mainFrame.sendReceive();
			}
		}else if (actionCommand.equals(GUIConsts.RECEIVE_BUTTON_TEXT)) {
			if(null != mainFrame.getMailHandler().getSelectedAccount()) {
				mainFrame.getMailHandler().getSelectedAccount().getThreadList().add(new HeaderThread(mainFrame.getMailHandler().getSelectedAccount()));        		
			}
			else {
				JOptionPane.showMessageDialog(mainFrame,Consts.NO_ACCOUNT_SELECTED_MSG);
			}
		}
		else if (actionCommand.equals(GUIConsts.NEW_MAIL_BUTTON_TEXT)) {
			if(null == mainFrame.getMailHandler().getSelectedAccount()) {
				JOptionPane.showMessageDialog(mainFrame,Consts.NO_ACCOUNT_SELECTED_MSG);
			}
			else {
				NewMailFrame newMail = new NewMailFrame(mainFrame);
				newMail.setVisible(true);   
			}
		}
		else if (actionCommand.equals(GUIConsts.REPLY_BUTTON_TEXT)) {
			if(null == mainFrame.getMailHandler().getSelectedAccount()) {
				JOptionPane.showMessageDialog(mainFrame,Consts.NO_ACCOUNT_SELECTED_MSG);
			}
			else {
				NewMailFrame newMail = new NewMailFrame(mainFrame,true);				   
			}
		}
		else if (actionCommand.equals(GUIConsts.DISCONNECT_BUTTON_TEXT)) {
			mainFrame.getMailHandler().getSelectedAccount().closeConection();
		}
		else if (actionCommand.equals(GUIConsts.SEND_BUTTON_TEXT)) {
			if(null != newMail) {
    			try {
    	    		AccountInformation accountInformation = newMail.getMainFrame().getMailHandler().getSelectedAccount().getAccountInformation();
    	    		Connection.sendMail(accountInformation, newMail.createMail(Connection.createSMTPSession(accountInformation)));
    	    		setVisible(false);
    	    		
    	    	}catch (MessagingException e) {
    	    		System.out.println("messaging exception encountered in CreateMail:cmdSendActionPerformed ");
    	    		JOptionPane.showMessageDialog(this, "Your message could not be sent: "+e.getMessage());
    	    		e.printStackTrace();
    	    	}
    	    	newMail.dispose();
    		}
		}
		else if (actionCommand.equals(GUIConsts.ATTACH_BUTTON_TEXT)) {
			
		}
	}
	
	public void updateUI() {
		adjustSize();
		super.updateUI();
	}
	
	public void paint(Graphics g) {
		adjustSize();
		super.paint(g);
	}
	
	private void adjustSize() {
		if(null != getParent()) {
			Dimension d = new Dimension(getParent().getHeight(),getParent().getHeight()-2);
			setPreferredSize(d);
			setSize(d);
			setMaximumSize(d);
			setMinimumSize(d);
		}
	}

}

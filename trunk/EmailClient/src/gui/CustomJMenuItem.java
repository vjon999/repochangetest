package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import constants.Consts;

public class CustomJMenuItem extends JMenuItem implements ActionListener {
	
	private static final long serialVersionUID = 1608130084515671133L;
	private MainFrame mainFrame;
	
	public CustomJMenuItem(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		addActionListener(this);
	}
	
	public CustomJMenuItem(MainFrame mainFrame,String text) {
		this(mainFrame);
		setText(text);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		String actionCommand = evt.getActionCommand();
		if(actionCommand.equals(GUIConsts.EXIT_MENU_DEFAULT_TEXT)) {
			System.exit(0);
		}
		else if(actionCommand.equals(GUIConsts.NEW_MAIL_MENU_DEFAULT_TEXT)) {
			if(null == mainFrame.getMailHandler().getSelectedAccount()) {
				JOptionPane.showMessageDialog(mainFrame,Consts.NO_ACCOUNT_SELECTED_MSG);
			}
			else {
				NewMailFrame newMail = new NewMailFrame(mainFrame);
				newMail.setVisible(true);   
			}
		}		
	}

}

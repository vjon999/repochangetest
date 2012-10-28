	package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Message;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import util.MessageIndexDateComparator;
import util.MessageUtil;
import account.MailAccount;
import constants.Consts;
import email.EmailMessage;
import email.MessageIndex;

public class MyScrollPane extends JPanel implements Consts {
	private String title;
	private Map<String,Object> map=new HashMap<String, Object>();
	private MailAccount mailAccount;
	private static JTable table;
	private DefaultTableModel tableModel;
	private TextRenderer textRenderer;
	private MainFrame mainFrame;
	private JScrollPane scrollPane;
	private JPanel statusPanel;
	private JLabel lblStatus;
	private GroupLayout layout;
	
	public MyScrollPane(MainFrame mainFrame,MailAccount mailAccount,String folderType) {
		this.mailAccount=mailAccount;
		this.mainFrame=mainFrame;
		this.title=folderType;	
		initComponents();
	}
	public MyScrollPane() {
		initComponents();
	}
	private void initComponents() {
		scrollPane = new JScrollPane();
		statusPanel = new JPanel();
		lblStatus = new JLabel();
		
		lblStatus.setText("Status:");
		
		layout = new GroupLayout(this);
		setLayout(layout);
		initJTable();
		scrollPane.setViewportView(table);
		statusPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		
        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
        	statusPanelLayout.createSequentialGroup()
            .addComponent(lblStatus)
        );
        statusPanelLayout.setVerticalGroup(
        		statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblStatus)
        );
		layout.setAutoCreateContainerGaps(false);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(scrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
				.addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
						.addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
		);       
	}
	
	private void initJTable() {
		table = new JTable();
		tableModel = new DefaultTableModel();		
		table.setModel(tableModel);
		textRenderer = new TextRenderer();
		for(int i=0;i<MainFrame.COLUMN_HEADERS.length;i++) {
			tableModel.addColumn(MainFrame.COLUMN_HEADERS[i]);
		}
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				tableMouseClicked(arg0);				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		//table.setPreferredSize(new Dimension(800,400));
		TableColumn tableCol = null;
		tableCol = table.getColumnModel().getColumn(5);
		tableCol.setPreferredWidth((int) (table.getPreferredSize().getWidth() * 0.4));
		tableCol.setWidth((int) (table.getPreferredSize().getWidth() * 0.4));
		
		tableCol = table.getColumnModel().getColumn(4);
		tableCol.setPreferredWidth((int) (table.getPreferredSize().getWidth() * 0.3));
		tableCol.setWidth((int) (table.getPreferredSize().getWidth() * 0.3));
		
		tableCol = table.getColumnModel().getColumn(3);
		tableCol.setMaxWidth(100);
		tableCol.setMinWidth(100);
		
		tableCol = table.getColumnModel().getColumn(2);
		tableCol.setMaxWidth(60);
		tableCol.setMinWidth(60);
		
		tableCol = table.getColumnModel().getColumn(1);
		tableCol.setMaxWidth(50);
		tableCol.setMinWidth(50);
				
		tableCol=(TableColumn) table.getColumnModel().getColumn(0);
		tableCol.setCellRenderer(table.getDefaultRenderer(Boolean.class));
		tableCol.setCellEditor(table.getDefaultEditor(Boolean.class));
		tableCol.setMaxWidth(40);
		tableCol.setMinWidth(40);
		
		for(int i=1;i<tableModel.getColumnCount();i++) {
			table.getColumn(tableModel.getColumnName(i)).setCellRenderer(textRenderer);
		}
	}
	
	public void tableMouseClicked(MouseEvent evt) {
		if(evt.getClickCount()==1) {
			mainFrame.openTab(table.getSelectedRow(),mailAccount,title);
		}
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public MailAccount getMailAccount() {
		return mailAccount;
	}

	public void setMailAccount(MailAccount mailAccount) {
		this.mailAccount = mailAccount;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public TextRenderer getTextRenderer() {
		return textRenderer;
	}

	public void setTextRenderer(TextRenderer textRenderer) {
		this.textRenderer = textRenderer;
	}
	
	public void addNewMessage(EmailMessage emailMessage) {
		tableModel.insertRow(0, addRow(emailMessage));
	}
	
	public void refreshTable(List<MessageIndex> list) {
		if(tableModel.getRowCount() > 0) {
			for(int i=0;i<tableModel.getRowCount();i++) {
				tableModel.removeRow(i);
			}
		}				
		for(MessageIndex messageIndex:list) {
			tableModel.addRow(addRow(messageIndex));
		}		
	}
	
	public Object[] addRow(EmailMessage emailMessage) {
		Object object[]=new Object[COLUMN_HEADERS.length];    	
		object[0]=new Object();
		object[1]=new Object();
		object[2]=new Object();
		object[3]=new Object();
		object[4]=new Object();
		object[5]=new Object();
		try {
			object[0]=emailMessage.isSaved();
			object[1]=emailMessage.getMessageNumber();
			object[2]=emailMessage.getMessageSize()/1024;
			object[3]=MessageUtil.getShortDate(emailMessage.getSentDate());   
			object[5]=emailMessage.getMessageSubject();
			try {
				if(null != emailMessage.getFrom()[0]) {
					object[4]=emailMessage.getFrom()[0].getPersonal()+"<"+emailMessage.getFrom()[0].getAddress()+">";
				} 		
				if(null == emailMessage.getMessageSubject()) {
					emailMessage.setMessageSubject("");
				}	
			}
			catch(ArrayIndexOutOfBoundsException e) {
				object[4]="";
			}
		}catch(ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return object;
	}
	
	public Object[] addRow(MessageIndex messageIndex) {
		Object object[]=new Object[COLUMN_HEADERS.length];    	
		object[0]=new Object();
		object[1]=new Object();
		object[2]=new Object();
		object[3]=new Object();
		object[4]=new Object();
		object[5]=new Object();
		try {
			object[0]=false;
			object[1]=messageIndex.getMessageNumber();
			object[2]=messageIndex.getMessageSize()/1024;
			object[3]=MessageUtil.getShortDate(messageIndex.getSentDate());   
			object[5]=messageIndex.getMessageSubject();
			try {
				if(null != messageIndex.getFrom()[0]) {
					object[4]=messageIndex.getFrom()[0].getPersonal()+"<"+messageIndex.getFrom()[0].getAddress()+">";
				} 		
				if(null == messageIndex.getMessageSubject()) {
					messageIndex.setMessageSubject("");
				}	
			}
			catch(ArrayIndexOutOfBoundsException e) {
				object[4]="";
			}
		}catch(ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return object;
	}
}

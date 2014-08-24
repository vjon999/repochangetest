/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NATFrame.java
 *
 * Created on 16 May, 2013, 9:55:24 AM
 */
package com.nat.puncher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;

/**
 *
 * @author root
 */
public class NATFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5979527502707576941L;

	private static final Logger LOG = Logger.getLogger(NATFrame.class.getName()); 
	
    private int natServerPort;
    private int natCBClientPort;
    private int cbClientPort;
    private DatagramSocket datagramSocket = null;
    private DatagramSocket cbClientSocket = null;
    private byte[] buffer = new byte[1024];
    private boolean stop = false;
    private boolean connected = false;
    private final StringBuffer viewText = new StringBuffer();
    private final StringBuffer sendText = new StringBuffer();
    private Properties config;
    
    private InetAddress serverAddress;
    private int serverPort;
    private String clientIP;
    
    private Thread serverPortListener;
    private Thread localPortListener;
    
    /** Creates new form NATFrame 
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws MessagingException */
    public NATFrame() throws FileNotFoundException, IOException, MessagingException {
        try {
        	config = new Properties();
    		config.load(new FileInputStream("clientConfig.ini"));
        	//clientIP = IPGetter.getExternalIP();
        	clientIP = config.getProperty("client_ip");//"192.168.1.9";//"localhost";//117.194.208.151
        	//IPGetter.mailExternalIP(clientIP, "vickysengupta006@gmail.com");
        	//System.out.println(IPGetter.getIPFromMail());
            initComponents();
            
            
    		//serverPort = Integer.parseInt(config.getProperty("server_port"));
    		natServerPort = Integer.parseInt(config.getProperty("nat_server_port"));
    		natCBClientPort = Integer.parseInt(config.getProperty("nat_cb_port"));
    		
    		datagramSocket = new DatagramSocket(natServerPort);
            cbClientSocket = new DatagramSocket(natCBClientPort);
            
            Thread serverDataSender = new Thread(new Runnable() {

                public void run() {
                    while(true) {
                        synchronized(sendText) {
                            try {
                                if(sendText.length() > 0) {
                                    DatagramPacket newPacket = new DatagramPacket(sendText.toString().getBytes(), 0, sendText.toString().length(), serverAddress, serverPort);
                                    datagramSocket.send(newPacket);
                                    updateConsole("\nSent to "+serverAddress+":"+serverPort+" --> "+sendText.toString());
                                    sendText.delete(0, sendText.length());
                                }
                                sendText.wait();
                            } catch (IOException ex) {
                                LOG.log(Level.SEVERE, null, ex);
                            } catch (InterruptedException ex) {
                                LOG.log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            });
            
            Thread uiUpdater = new Thread(new Runnable() {

                public void run() {
                    while(true) {
                        synchronized(viewText) {
                            try {
                                viewText.wait();
                                txtConsole.setText(viewText.toString());
                            } catch (InterruptedException ex) {
                                LOG.log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            });
            
            serverPortListener = new Thread(new Runnable() {

                public void run() {
                    try {            
                        //ip = txtIP.getText();
                        //serverPort = Integer.parseInt(txtPort.getText());
                    	DatagramPacket fromServerPacket = new DatagramPacket(buffer, buffer.length);
                        String recvdText = null;
                        updateConsole("\nListening... to port : "+datagramSocket.getLocalPort());
                        while(!stop) {
                        	LOG.info("waiting for server");
                            datagramSocket.receive(fromServerPacket);
                            recvdText = new String(fromServerPacket.getData(), 0, fromServerPacket.getLength());
                            LOG.info("got from serevr"+recvdText+" == port == "+fromServerPacket.getPort());
                            
                            //if(fromServerPacket.getPort() == serverPort) {
                            	//server sent this
                                 updateConsole("\nServer: "+recvdText);
                                 LOG.info("connected"+connected);
                                 if(connected) {
                                	 LOG.info("Sending to port: "+natCBClientPort);
                                	 DatagramPacket cbPacket = new DatagramPacket(recvdText.getBytes(), 0, recvdText.length(), InetAddress.getLocalHost(), cbClientPort);
                                     cbClientSocket.send(cbPacket); 
                                 }
                                 
                                 if(recvdText.equalsIgnoreCase("helloclient")) {
                                	 serverAddress = fromServerPacket.getAddress();
                                	 serverPort = fromServerPacket.getPort();
                                     send("helloserver");
                                 }
                                 else if(recvdText.indexOf("connected") != -1) {
                                 	connected = true;
                                 }
                            //}
                           /* else if(packet.getPort() == 14000) {
                            	//cb sent this
                            	try {            
                                    //ip = txtIP.getText();
                                    String cbText = null;
                                    updateConsole("Listening... to local cb client port : "+cbClientSocket.getPort());
                                    while(!stop) {
                                    	cbClientSocket.receive(packet);                
                                    	cbText = new String(packet.getData(), 0, packet.getLength());
                                        updateConsole("\nCB: "+cbText);
                                        //address = packet.getAddress();
                                        //serverPort = packet.getPort();
                                        send(cbText);
                                    }
                                    cbClientSocket.disconnect();
                                } catch (IOException ex) {
                                    LOG.log(Level.SEVERE, null, ex);
                                }
                            }*/
                           
                        }
                        datagramSocket.disconnect();
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            localPortListener = new Thread(new Runnable() {

                public void run() {
                    try {            
                        //ip = txtIP.getText();
                        String cbText = null;
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        updateConsole("Listening... to local cb client port : "+cbClientSocket.getLocalPort());
                        while(!stop) {
                        	LOG.info("waiting for CB");
                        	cbClientSocket.receive(packet);
                            cbText = new String(packet.getData(), 0, packet.getLength());
                            LOG.info("received from CB"+cbText+" == port =="+packet.getPort());
                            updateConsole("\nCB: "+cbText);
                            //address = packet.getAddress();
                            //serverPort = packet.getPort();
                            cbClientPort = packet.getPort();
                            send(cbText);
                        }
                        cbClientSocket.disconnect();
                    } catch (IOException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            uiUpdater.start();
            serverDataSender.start();
            localPortListener.start();
            serverPortListener.start();
        } catch (SocketException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtIP = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        cmdListen = new javax.swing.JButton();
        cmdAuto = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtConsole = new javax.swing.JTextArea();
        cmdStop = new javax.swing.JButton();
        cmdClearConsole = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtSend = new javax.swing.JTextField();
        cmdSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("NAT Client");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtIP.setText("localhost");

        jLabel1.setText("IP: ");

        jLabel2.setText("Port: ");

        txtPort.setText("12000");
        txtPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPortActionPerformed(evt);
            }
        });

        cmdListen.setText("Listen");
        cmdListen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdListenActionPerformed(evt);
            }
        });

        cmdAuto.setText("auto");
        cmdAuto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAutoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cmdListen, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIP, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel2)
                        .addGap(1, 1, 1)
                        .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdAuto)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdAuto, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdListen)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtConsole.setColumns(20);
        txtConsole.setRows(5);
        jScrollPane1.setViewportView(txtConsole);

        cmdStop.setText("Stop");
        cmdStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdStopActionPerformed(evt);
            }
        });

        cmdClearConsole.setText("Clear Console");
        cmdClearConsole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdClearConsoleActionPerformed(evt);
            }
        });

        jButton1.setText("Manual Entry");

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtSend.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSendKeyPressed(evt);
            }
        });

        cmdSend.setText("Send");
        cmdSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(txtSend, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdSend))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtSend, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cmdSend, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(cmdStop, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                        .addGap(11, 11, 11)
                        .addComponent(cmdClearConsole))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdStop)
                    .addComponent(cmdClearConsole))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdAutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdAutoActionPerformed

    private void cmdListenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdListenActionPerformed
        stop = false;
        cmdListen.setEnabled(false);
        serverPortListener.start();
        
    }//GEN-LAST:event_cmdListenActionPerformed

    private void cmdStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdStopActionPerformed
        stop = true;
        cmdListen.setEnabled(true);
    }//GEN-LAST:event_cmdStopActionPerformed

    private void txtPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPortActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPortActionPerformed

    private void cmdClearConsoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdClearConsoleActionPerformed
        if(null != viewText && viewText.length() > 0) 
            viewText.delete(0, viewText.length());
        synchronized(viewText) {
            viewText.notify();
        }
    }//GEN-LAST:event_cmdClearConsoleActionPerformed

    private void cmdSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSendActionPerformed
        send(txtSend.getText());
        txtSend.setText("");
    }//GEN-LAST:event_cmdSendActionPerformed

    private void txtSendKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSendKeyPressed
       if(evt.getKeyCode() == 10) {
           send(txtSend.getText());
            txtSend.setText("");
       }
    }//GEN-LAST:event_txtSendKeyPressed
    
    public void send(String message) {
        if(null != message && message.length() > 0) {
          sendText.append(message);
          synchronized(sendText) {
            sendText.notify();
          }  
        }
    }
    
    private void updateConsole(String text) {
        viewText.append(text);
        synchronized(viewText) {
            viewText.notify();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
					new NATFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAuto;
    private javax.swing.JButton cmdClearConsole;
    private javax.swing.JButton cmdListen;
    private javax.swing.JButton cmdSend;
    private javax.swing.JButton cmdStop;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtConsole;
    private javax.swing.JTextField txtIP;
    private javax.swing.JTextField txtPort;
    private javax.swing.JTextField txtSend;
    // End of variables declaration//GEN-END:variables
}

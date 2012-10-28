package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class InstallerFrame extends JFrame {
       
        public static final int BUFFER_SIZE=1024;
       
        private JLabel lblSelectDirectory;
        private JTextField txtSelectDirectory;
        private JButton cmdBrowse;
        private JButton cmdOK;
        private JButton cmdCancel;
        private File installLocation;
       
        public InstallerFrame() {
                initcomponents();
        }
       
        private void initcomponents() {
                setSize(800,200);
                setTitle("Installation");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
                lblSelectDirectory=new JLabel();
                txtSelectDirectory=new JTextField();
                cmdBrowse=new JButton();
                cmdOK=new JButton();
                cmdCancel=new JButton();
               
                lblSelectDirectory.setText("Select Location for installation: ");
                cmdBrowse.setText("Browse");
                cmdOK.setText("OK");
                cmdCancel.setText("Cancel");
               
                cmdBrowse.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                                cmdBrowseActionPerformed(evt);
                        }
                });
               
                cmdOK.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                                cmdOKActionPerformed();
                        }
                });
               
                cmdCancel.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                                cmdCancelActionPerformed();
                        }
                });
               
                txtSelectDirectory.setPreferredSize(new Dimension(500,25));
                cmdOK.setPreferredSize(new Dimension(100,25));
                cmdCancel.setPreferredSize(new Dimension(100,25));
               
                setLayout(new FlowLayout(FlowLayout.CENTER,0,10));
                add(lblSelectDirectory);
                add(txtSelectDirectory);
                add(cmdBrowse);
                add(cmdOK);
                add(cmdCancel);
               
                setVisible(true);
        }
       
        private void cmdBrowseActionPerformed(java.awt.event.ActionEvent evt) {
                JFileChooser fc=new JFileChooser();
                fc.setMultiSelectionEnabled(false);
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int i=fc.showDialog(this,"Select Directory");
                if(0 == i) {
                        installLocation=fc.getSelectedFile();
                        txtSelectDirectory.setText(installLocation.getAbsolutePath());
                }        
        }
       
        private void cmdOKActionPerformed() {
                if(new File("Test").exists()) {
                        copyFile(new File("Test"),new File(installLocation.getAbsoluteFile()+"\\Test"));
                }
                else {
                        txtSelectDirectory.setText(new File("Test").getAbsolutePath());
                }
               
        }
       
        private void cmdCancelActionPerformed() {
                System.exit(0);
        }
       
        private void copyFile(File inputFile,File outputFile) {
                try {
                        FileInputStream fis=new FileInputStream(inputFile);
                        FileOutputStream fos=new FileOutputStream(outputFile);
                        int i=-1;
                        int count=0;
                        byte[] buffer=new byte[BUFFER_SIZE];
                        while(-1 != (i=fis.read())) {
                                buffer[count]=(byte)i;
                                count++;
                                if(BUFFER_SIZE <= count) {
                                        fos.write(buffer);
                                        count=0;
                                }                                
                        }
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
       
        public static void main(String[] arg) {
                java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InstallerFrame();
            }
        });
        }
} 
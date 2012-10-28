package network;

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import filesystem.AppendableOutputStream;

import account.AccountInformation;

public class UIDStore {
	
	private Map uids = new HashMap();
	private File uidFile;
   
   public UIDStore(AccountInformation accountInformation) throws IOException {
	   String fileName=accountInformation.getAccountName()+"_"+accountInformation.getUserName();
	   uidFile=new File(MailHandler.getBaseFolderName()+"\\"+fileName+"\\uid.txt");
	   load();
   }
   
   public void load() throws IOException {
	   if(uidFile != null && uidFile.exists() && uidFile.length() > 0) {
		   ObjectInputStream is = new ObjectInputStream(new FileInputStream(uidFile));
		   // BufferedReader reader = new BufferedReader(new FileReader(file));
		   try {
			   String uid = (String) is.readObject();
			   while(uid != null) {
				   uids.put(uid,Boolean.FALSE);
				   uid = (String) is.readObject();
			   }

		   }catch (EOFException e) {
			   
		   } catch (ClassNotFoundException e) {
			   e.printStackTrace();
		   }
		   finally {
			   is.close();
		   }
	   }
	   else {
		   uidFile.createNewFile();
	   }
   }

   public synchronized void store(String value) throws IOException {
	   ObjectOutputStream os = null;
	   if(isNew(value)) {
		   try {    	 
			   if(uidFile.exists() && uidFile.length() > 0) {
				   os = new AppendableOutputStream(new FileOutputStream(uidFile,true)); 
			   }
			   else {
				   os = new ObjectOutputStream(new FileOutputStream(uidFile));
			   }
			   os.writeObject(value);
		   }
		   finally {
			   os.close();
		   }
		   uids.put(value, Boolean.FALSE);
	   }	  
   }
   
   public void remove(String key) {
	   uids.remove(key);
	   updateUIDs();
   }
   
   public void updateUIDs() {
	   ObjectOutputStream os = null;
	   try {    	 
		   os = new ObjectOutputStream(new FileOutputStream(uidFile)); 
		   for(Object k:uids.keySet()) {
			   os.writeObject(k);
		   }		   
	   } catch (IOException e) {
		   e.printStackTrace();
	   }
	   finally {
		   try {
			   if(null != os) {
				   os.close();
			   }
		   } catch (IOException e) {
			   e.printStackTrace();
		   }
	   }
   }
   
   // if we've seen this UID on the server, store it in the list
   // if it was not already in the list, return true to indicate
   // that the mail is new
   public boolean isNew(String uid) {
      if(null == uids.get(uid)) {
    	  return true;
      }
      else {
    	  return false;
      }
   }
}
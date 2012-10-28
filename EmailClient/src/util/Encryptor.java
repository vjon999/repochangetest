package util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

import exception.BaseException;

public class Encryptor {
        private Key key;
        SealedObject sealedObject;
       
        public Encryptor() throws BaseException  {
                try {
					generateKey();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
	        		throw new BaseException(e.getMessage());
				}
        }
       
        private void generateKey() throws NoSuchAlgorithmException {
                KeyGenerator generator;
                generator = KeyGenerator.getInstance("DES");
                generator.init(new SecureRandom());
                key = generator.generateKey();
        }        

        public byte[] encrypt(byte[] message) throws BaseException {
        	try {
        		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        		cipher.init(Cipher.ENCRYPT_MODE, key);
        		// encrypt using the cipher                
        		return cipher.doFinal(message);
        	} catch (IllegalBlockSizeException e) {        		
        		e.printStackTrace();
        		throw new BaseException(e.getMessage());
        	} catch (BadPaddingException e) {
        		e.printStackTrace();
        		throw new BaseException(e.getMessage());
        	} catch (InvalidKeyException e) {
        		e.printStackTrace();
        		throw new BaseException(e.getMessage());
        	} catch (NoSuchAlgorithmException e) {
        		e.printStackTrace();
        		throw new BaseException(e.getMessage());
        	} catch (NoSuchPaddingException e) {
        		e.printStackTrace();
        		throw new BaseException(e.getMessage());
        	}	

        }

        public byte[] decrypt(byte[] encrypted) throws BaseException {
        	try {
        		// Get a cipher object.
        		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        		cipher.init(Cipher.DECRYPT_MODE, key);
        		//decode the message
        		byte[] decryptedBytes = cipher.doFinal(encrypted);
        		//converts the decoded message to a String
        		/*try {
					String decryptedString = new String(decryptedBytes, "UTF8");
					System.out.println(decryptedString);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
        		
        		return decryptedBytes;
        	} catch (IllegalBlockSizeException e) {        		
        		e.printStackTrace();
        		throw new BaseException(e.getMessage());
        	} catch (BadPaddingException e) {
        		e.printStackTrace();
        		throw new BaseException(e.getMessage());
        	} catch (InvalidKeyException e) {
        		e.printStackTrace();
        		throw new BaseException(e.getMessage());
        	} catch (NoSuchAlgorithmException e) {
        		e.printStackTrace();
        		throw new BaseException(e.getMessage());
        	} catch (NoSuchPaddingException e) {
        		e.printStackTrace();
        		throw new BaseException(e.getMessage());
        	}	
        }

        public String update(byte[] encrypted) throws InvalidKeyException,
        NoSuchAlgorithmException, NoSuchPaddingException,IllegalBlockSizeException, BadPaddingException, IOException {
                // Get a cipher object.
                Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key);

                //decode the message
                byte[] decryptedBytes = cipher.update(encrypted);

                //converts the decoded message to a String
                String decryptedString = new String(decryptedBytes, "UTF8");
                return decryptedString;
        }
} 
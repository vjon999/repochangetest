package server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class test {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub

		String test = "asd|asdaa|aaaa";
		System.out.println(test.split("\\|")[0]);
		System.out.println(System.getProperty("user.dir"));
		
        System.out.println("Digest(in hex format):: " + UCIUtil.calculateMD5Hash("deep"));
        
	}

}

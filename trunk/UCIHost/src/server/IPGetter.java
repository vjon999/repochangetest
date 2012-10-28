package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class IPGetter {
	public static void main(String[] arg) {
		try {
			Socket sock = new Socket("www.edpsciences.org", 80);
			BufferedReader is = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintWriter os = new PrintWriter(sock.getOutputStream(), true);

                           // the actual URL is www.edpsciences.org/htbin/ipaddress (a ruby page)
			os.println("<strong class=\"highlight\">GET</strong> /htbin/ipaddress HTTP/1.0");
			os.println(); // two new lines are required to send a command
                     	
                           // the page has x number of lines with unnecessary information before it says "Your <strong class="highlight">IP</strong> <strong class="highlight">address</strong>"....
			String line = "";
			while(line.indexOf("Your <strong class=\"highlight\">IP</strong> <strong class=\"highlight\">address</strong>") == -1)
			{
				line = is.readLine();
			}
                                                // do a little substringing, and we'll have our answer (the <strong class="highlight">ip</strong> <strong class="highlight">address</strong> is inbetween "<B> " and " </B>"
			String ipAddr = line.substring(line.indexOf("<B> ")+4, line.indexOf("</B>")-1);
	    } 
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
}

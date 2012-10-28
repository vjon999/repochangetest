package testing;

import java.awt.GridLayout;
import java.net.MalformedURLException;

import gui.MyScrollPane;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;

public class TestFrame extends JFrame {
	
	
	public static void main(String[] args) throws MalformedURLException {
		 JFrame window = new JFrame();
		    HtmlPanel panel = new HtmlPanel();
		    SimpleHtmlRendererContext context = new SimpleHtmlRendererContext(panel, new SimpleUserAgentContext());
		    panel.setHtml("aasd","",context); 
		    window.getContentPane().add(panel);
		    window.setSize(600, 400);
		    window.setVisible(true);
			
	}

}

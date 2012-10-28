package filesystem;

import javax.swing.JTextArea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyLogger {
	
	private JTextArea console;
	private Log LOG = null;
	
	public MyLogger(Class className,JTextArea console) {
		this.console = console;
		LOG = LogFactory.getLog(className);
	}

	public void info(Object object) {
		LOG.info(object);
		if(console != null) {
			console.setText(console.getText().concat("\n"+(String) object));
		}
	}
	
	public void error(Object object) {
		LOG.error(object);
		if(console != null) {
			console.setText(console.getText().concat("\n"+(String) object));
		}
	}
	
	public void debug(Object object) {
		LOG.error(object);
		if(console != null) {
			console.setText(console.getText().concat((String) object));
		}
	}
}

package email;

import java.text.NumberFormat;

public class EmailAttachment {
	public static final String UNITS[]={"byte","kb","mb","gb"};
	private int attachmentSize;
	private int attachmentLevel;
	private int unitLevel=0;
	private String attachmentName;	
	private String attachmentPath;
	
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	public int getAttachmentSize() {
		return attachmentSize;
	}
	public float getFormattedSize() {
		float size=attachmentSize;
		if(1024 > size) {    
			unitLevel=0;
    		//do nothing
    	}
    	else if(1024 < size && 1024*1024 > size) {
    		unitLevel=1;
    		size=size/1024;    		
    	}
    	else if(1024*1024 < size && 1024*1024*1024 > size) {
    		unitLevel=2;
    		size=size/(1024*1024);		
    	}
    	else if(1024*1024*1024 < size && 1024*1024*1024*1024 > size) {
    		unitLevel=3;
    		size=size/(1024*1024*1024);    		
    	}
    	NumberFormat numberFormat=NumberFormat.getInstance();
    	numberFormat.setMaximumFractionDigits(2);  
    	return Float.parseFloat(numberFormat.format(size));		
	}
	public void setAttachmentSize(int size) {
    	this.attachmentSize = size;
	}
	public int getAttachmentLevel() {
		return attachmentLevel;
	}
	public void setAttachmentLevel(int attachmentLevel) {
		this.attachmentLevel = attachmentLevel;
	}
	public String getAttachmentName() {
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	public String getAttachmentUnit() {
		return UNITS[unitLevel];
	}	
}

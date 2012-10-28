package com;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

public class Config {
	
	public static final String BASE_LOCATION_KEY="BASE_LOCATION";
	
	private String key;
	private String value;
	private Map<String, String> map=new HashMap<String, String>();
	
	public String getValue(String key) {
		return map.get(key);
	}
	
	public void putValue(String key,String value) {
		map.put(key,value);
	}
	
	public void read(File file) {
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(new FileInputStream(file));
			while(true) {
				key = (String) is.readObject();
				value = (String) is.readObject();
				if(null ==key || null == value) {
					break;
				}
				else {
					map.put(key, value);
				}				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void read() throws FileNotFoundException {
		ObjectInputStream is = null;
		File file = new File("config.dat");
		if(file.exists()) {								
			try {
				is = new ObjectInputStream(new FileInputStream(file));
				while(true) {
					key = (String) is.readObject();
					value = (String) is.readObject();
					if(null ==key || null == value) {
						break;
					}
					else {
						map.put(key, value);
					}				
				}
			}catch (EOFException e) {
				
			}
			catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			finally {
				if(null != is) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		else {
			throw new FileNotFoundException();
		}
	}
	
	public void write(File file) {
		ObjectOutputStream os = null;
		 try {
			os = new ObjectOutputStream(new FileOutputStream(file));
			for(String key:map.keySet()) {
				os.writeObject(key);
				os.writeObject(map.get(key));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void write() {
		ObjectOutputStream os = null;
		File file=null; 
		 try {
			 file = new File("config.dat");System.out.println(file.getAbsolutePath());
			 if(file.exists()) {
				 
			 }
			 else {
				file.createNewFile(); 
			 }
			os = new ObjectOutputStream(new FileOutputStream(file));
			for(String key:map.keySet()) {
				os.writeObject(key);
				os.writeObject(map.get(key));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

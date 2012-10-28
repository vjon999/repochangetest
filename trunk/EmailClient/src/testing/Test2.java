package testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import filesystem.DAO;

import network.UIDStore;

public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DAO d=new DAO();
		String baseloc="D:\\Java\\Java Projects\\EclipseProjects\\EmailClient\\bin\\account\\accountfiles\\Gmail2";
		File f=new File(baseloc);
		deleteFolder(f);
		System.out.println(); 
	}
	
	private static void deleteFolder(File file) {
		if(file.isDirectory()) {
			for(File f:file.listFiles()) {
				deleteFolder(f);
			}
			System.out.println(file.delete());
		}
		else {
			System.out.println(file.delete());
		}
	}

}

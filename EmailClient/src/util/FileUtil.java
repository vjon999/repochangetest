package util;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

public class FileUtil {
	public static void copyFile(File sourceFile,File destFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(sourceFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(destFile));
		String line = br.readLine();
		while(null != line) {
			bw.write(line);
			line = br.readLine();
		}
		bw.close();
		br.close();
	}
	
	public static void copyFile(String sourceFile,String destFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(sourceFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(destFile));
		String line = br.readLine();
		while(null != line) {
			bw.write(line);
			line = br.readLine();
		}
		bw.close();
		br.close();
	}
	
	public static File chooseFolder(Component component) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.showOpenDialog(component);     
		File dir = fileChooser.getSelectedFile();
		return dir;
	}
}

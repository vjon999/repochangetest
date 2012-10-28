package util;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GUIUtil {
	
	public static final Integer[] FONT_SIZES = {8,10,12,14,16,24,32,48,64,72};
	public static final Font DEFAULT_FONT = new Font("Times New Roman",Font.PLAIN,12);
	
	public static String[] availableFontFamilyNames() {		
		Font[] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();		
        Set<String> availableFontNames = new HashSet<String>();
        for(int i=0;i<availableFonts.length;i++) {
        	availableFontNames.add(availableFonts[i].getFamily());
        }
        String familyNames[] = new String[availableFontNames.size()];
        availableFontNames.toArray(familyNames);
        Arrays.sort(familyNames);
        return familyNames;
	}
	
}

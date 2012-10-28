package gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import constants.Consts;

/**
 *
 * @author Pratik
 */
public class TextRenderer extends JLabel implements TableCellRenderer,Consts{
	Color foregroundColor=Color.BLUE;
	public TextRenderer() {	}
	
	public TextRenderer(Color color) {
		foregroundColor=color;
	}
	
    public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		
		Integer columnValue=(Integer)table.getValueAt(row,table.getColumnModel().getColumnIndex(COLUMN_HEADERS[1]));		
        //setForegroundColor(foregroundColor);
        if(null != value) { 
        	if(columnValue == 458) {
        		setForegroundColor(Color.BLUE);
        		setBackground(Color.RED);	
        		//System.out.println(COLUMN_HEADERS[1]);
        	}
        	else {
        		setForegroundColor(Color.BLUE);
        		setBackground(Color.RED);
        	}
        	setText(value.toString());
        }
        setForegroundColor(Color.BLUE);
		setBackground(Color.RED);
        return this;
    }
    
}


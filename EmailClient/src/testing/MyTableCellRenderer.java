package testing;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MyTableCellRenderer implements TableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int col) {

		Component component=(Component) table.getCellRenderer(1,1);

		return null;
	}
}

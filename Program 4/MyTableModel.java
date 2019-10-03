//package swingExamples;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel{
	// Table data 
	String [] columnNames = {"SEG NUM","LENGTH","SPEEDLIM"};
	String[][] data = { {"", "", "" }, {"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" },{"", "", "" }
	};

	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	
	@Override
	public boolean isCellEditable (int row, int col) {
		return (true);
	}
	
	@Override 
	public void setValueAt (Object value, int row, int col) {
		data[row][col] = (String) value;
		fireTableCellUpdated (row, col);
		
	}
	
	

	
	

		

}

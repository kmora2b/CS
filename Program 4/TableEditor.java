/*******************************************************
*@author: Kimberly Morales
Files Requirements:
	+XMLFileData.java
	+MyTableModel.java
	+(Optional) input.txt

Purpose
-To create a GUI to edit information on the course data XML files. This will be done by using
prior code and modifying table models. JTable and Swing are the main components.

History
-9/25/19: 
	+Initial Version
-9/26/19: 
	+Created insertDataToTable()
	+Edited MyTableModel
	+Edited XMLFileData
	+Created fileChooser
-9/27/19
	+Created saveAsFile
	+Made methods in XMLFileData dynamic
	+Used DefaultTableModel
	+Implemented JOptions
	+Updated writeFile in XMLFileData
-9/28/19
	+Went back to MyTableModel
	+Changed this class file to TableEditor.java
	+Running into some errors with JDK
-9/30/19
	+Fixed JDK 
	+Fixed JTable not updating issue
-10/02/19
	+Final version

***********************************************************/
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;

//File chooser
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.SwingUtilities;
import java.awt.Dimension;
import javax.swing.JOptionPane;

public class TableEditor implements TableModelListener, ActionListener, ItemListener{
	private JFileChooser jfc= new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	private XMLFileData dataXML = new XMLFileData();
	private JFrame frame;
	private DefaultTableModel model;
	private MyTableModel m;
	private JTable table;
	private JScrollPane scrollPane;
	private String filename;
	private FileWriter fw;
	

	/*Main table class, creates Swing components and action listners*/
	public TableEditor () {
		frame = new JFrame ("Segment XML Editor");
		m = new MyTableModel();
		frame.setPreferredSize(new Dimension(800,1080));

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu ("File");
		JMenuItem menuItemOpen = new JMenuItem ("Open");
		JMenuItem menuItemSave = new JMenuItem ("Save");
		JMenuItem menuItemSaveAs = new JMenuItem ("Save As");
		JMenuItem menuItemExit = new JMenuItem ("Exit");

		menuItemOpen.addActionListener(this);
		menuItemSave.addActionListener(this);
		menuItemSaveAs.addActionListener(this);
		menuItemExit.addActionListener(this);


		menu.add(menuItemOpen);
		menu.add(menuItemSave);
		menu.add(menuItemSaveAs);
		menu.add(menuItemExit);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// put window in center of screen
		frame.setLocationRelativeTo(null);		

		// Show frame
		frame.pack();
		frame.setVisible(true);
	}


	/*Once a change has been made to the table, the row and col number are used to update the doc file */
	@Override
	public void tableChanged(TableModelEvent e){
		// TODO Auto-generated method stub
		int row = e.getFirstRow();
		int col = e.getColumn();
		TableModel mod = (TableModel)e.getSource();
		String colName = mod.getColumnName(col);
		Object data = mod.getValueAt(row,  col);
		System.out.println("new data is > " + data.toString());
		try {
			dataXML.changeContents(getCoordinatingColNum(col),row,data.toString());
		}
		catch (Exception except){
			System.out.println("Error in opening file");
		}
	}
	
	/*Helper function to tableChanged, finds the corresponding data to change*/
	public String getCoordinatingColNum(int n){
		if(n == 0) {
			return "SEGMENT_NUMBER";
		}
		
		else if(n == 1) {
			return "LENGTH";
		}
		
		else if(n == 1) {
			return "SPEED_LIMIT";
		}
		
		return "";
	}
	
	/*Used for the JFileChooser GUI and filters out for XML and TXT files, returns the directory and filename*/
	public String fileChooser() {
		jfc.setDialogTitle("Select a File");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filterXML = new FileNameExtensionFilter("XML", "xml");
		FileNameExtensionFilter filterTXT = new FileNameExtensionFilter("TXT", "txt");
		jfc.addChoosableFileFilter(filterXML);
		jfc.addChoosableFileFilter(filterTXT);
		jfc.showOpenDialog(null);
		File file = jfc.getSelectedFile();
		filename = file.getAbsolutePath();
		System.out.println(filename);
		
		return filename;
	}
	
	/*Inserts data to the table model so to show on JTable*/
	public void insertDataToTable() {
		try {
			m = new MyTableModel();
			dataXML.readFile(fileChooser());
			String getData[][] = dataXML.courseInfo();

			System.out.println(Arrays.deepToString(getData));

			for(int i = 0; i < getData.length;i++){
				
				for(int j = 0; j < getData[i].length; j++){
						m.setValueAt(getData[i][j], i, j);
						
				}
			}			
			 
			/*Create JTable here to show new values*/
			table = new JTable (m);
			table.getModel().addTableModelListener(this);
			scrollPane = new JScrollPane (table);
			table.setFillsViewportHeight(true);
			frame.add(scrollPane);
			frame.setLocationRelativeTo(null);		

			// Show frame
			frame.pack();
			frame.setVisible(true);
			JOptionPane.showMessageDialog(frame, "Upload is successful!");
		}
		
		catch (ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(frame, "Invalid filetype");
		}
	}

	/*Saves info from the JTable as a new filename*/
	public void saveAsFile(){
		jfc.setDialogTitle("Specify a file to save");   
		int userSelection = jfc.showSaveDialog(frame);
	
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = jfc.getSelectedFile();
			System.out.println("Save as file: " + fileToSave.getAbsolutePath());
			try{
				String filenameNew = fileToSave.getAbsolutePath();
				if(!filenameNew.toLowerCase().endsWith(".xml")){
					filenameNew = fileToSave.toString() + ".xml";
				}

				dataXML.writeFile (filenameNew);

			}
			catch(Exception e){
				System.out.println("Error in writing file");
			}
		}
	}
	
	/*Creates temp file just in case user exits early*/
	public void backupFile(File source, File dest) throws IOException {
		InputStream inputS = null;
		OutputStream ouputS = null;
		try {
			inputS = new FileInputStream(source);
			ouputS = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputS.read(buffer)) > 0) {
				ouputS.write(buffer, 0, length);
			}
		} finally {
			inputS.close();
			ouputS.close();
		}
	}
	

	
	@Override
	public void itemStateChanged(ItemEvent e) {
		System.out.println("itemStateChanged " + e);
		
	}
	public void actionPerformed(ActionEvent e) throws RuntimeException{
		System.out.println("actionPerformed " + e.getActionCommand());
		try {
			if (e.getActionCommand().equalsIgnoreCase("Exit")) {
				int n = JOptionPane.showConfirmDialog(
					frame,
					"Would you like to save your unsaved edits?",
					"Unsaved Data",
					JOptionPane.YES_NO_OPTION
				);
				
				if (n == JOptionPane.NO_OPTION){
					System.exit(0);
				}
				
				dataXML.writeFile (filename);
			}
			if (e.getActionCommand().equalsIgnoreCase("Save")) {
				
				if (table == null){
					JOptionPane.showMessageDialog(frame, "No XML file has been detected, please upload a xml file.");
				}
				
				dataXML.writeFile (filename);

			}
		
			if (e.getActionCommand().equalsIgnoreCase("Save As")) {
				if (table == null){
					JOptionPane.showMessageDialog(frame, "No XML file has been detected, please upload a xml file.");
				}
				
				saveAsFile();

			}
		}
		
		catch (Exception except){
			System.out.println("Error in opening file for table");
		}
		
		if (e.getActionCommand().equalsIgnoreCase("Open")) {
				insertDataToTable();
		}
		

	}	
	
	public void keyPressed(KeyEvent e) {
	
		int key = e.getKeyCode();
                if ((e.getKeyCode() == 67)) {
                    System.out.println("woot!");
                }
	}
	
	
	
	public static void main(String[] args) {
		TableEditor ste = new TableEditor();
	}
}


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.*;
import java.util.*;
/*Handles all info read in from XML file*/

class XMLFileData {
	private Document doc;
	private NodeList nList;
	private String filename;
	
	XMLFileData() { 
		this.doc = null;
		this.nList = null;
		this.filename = null;
	}
	
	XMLFileData(String filename) { 
		this.doc = null;
		this.nList = null;
		this.filename = filename;
	}
	
	private void setDoc(Document doc) {
		this.doc = doc;
	}
	
	private void setnList(NodeList nList){
		this.nList = nList;
	}
	
	private void setFilename(String filename) {
		this.filename = filename;
	}
	
	public NodeList getList() {
		return nList;
	}
	
	public Document getDoc() {
		return doc;
	}
	
	public String getFilename() {
		return filename;
	}

	public void readFile() {
		try {
			List<Node> delete = new ArrayList<Node>();
			File inputFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();//Saves doc so to not repeat read in process
			
			nList = doc.getDocumentElement().getChildNodes();
		} 
		
		catch (FileNotFoundException exception) {
			System.out.println("Filename does not exist");
			return;
		}
		
		catch (Throwable exception) {
			System.out.println("Invalid XML Structure");
			return;
		}
	}
	
	

	private void changeContents (String cParam, int c, String newVal) throws Exception {
		Node n = nList.item(c);
        Element emp = (Element) n;

		Node name = emp.getElementsByTagName(cParam).item(0).getFirstChild();
        name.setNodeValue(newVal);
		emp.getElementsByTagName(cParam).item(0).setTextContent(newVal);
	}
	
	private void writeFile () throws Exception{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filename));
		transformer.transform(source, result);
		System.out.println("XML file updated successfully");
	}



}
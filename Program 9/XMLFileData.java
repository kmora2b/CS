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

import java.io.StringReader;
import org.xml.sax.InputSource;

import java.io.*;

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
	
	//Setters
	
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	
	public void setnList(NodeList nList){
		this.nList = nList;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
		
	//Getters
	
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
			File inputFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();//Saves doc so to not repeat read in process
			
			nList = doc.getDocumentElement().getChildNodes();
		}
		
		catch (FileNotFoundException exception) {
			System.out.println("Filename does not exist");
			System.exit(0);
		}
		
		catch (Throwable exception) {
			System.out.println("Invalid XML Structure");
			System.exit(0);
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
	
	public String writeXmlDocumentToXmlFile() throws  Exception{
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		String xmlString = "";
		try {
			transformer = tf.newTransformer();
			 
			// Uncomment if you do not require XML declaration
			// transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			 
			//A character stream that collects its output in a string buffer, 
			//which can then be used to construct a string.
			StringWriter writer = new StringWriter();
	 
			//transform document to string 
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
	 
			xmlString = writer.getBuffer().toString();   
			System.out.println(xmlString);                      //Print to console or logs
		
		} 

		catch (Exception e) 
		{
			e.printStackTrace();
		}
		parseXML(xmlString);
		return xmlString;
	}
	
	public void parseXML(String xml) throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
 
        Document pdoc = db.parse(is);
        nList = pdoc.getDocumentElement().getChildNodes();
 
		
    }



}
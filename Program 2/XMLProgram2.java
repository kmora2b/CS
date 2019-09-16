import java.io.*;

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

import java.util.*;

/*******************************************************
*@author: Kimberly Morales

History
-9/11/19: 
	+Initial Version
-9/12/19: 
	+Created class and methods
	+Created currElem
-9/13/19
	+Created Menu
	+Rewrote readFile
-9/14/19
	+Changed whole structure, messy and not reading file
	+Rewrote displayContents
	+Rewrote WriteFile
	+Rewrote XMLNodelist class
-9/15/19
	+Added in comments
	+Refractored code
	+Rewrote ChangeContents, was not saving to file
-9/16/19
	+Final version

Purpose
-To create an XML Parser capable of showing contents of current, prev, and next elements, change params,
and save it as a new xml file.

***********************************************************/
/*
* global variable- currElem
*	Handles the current element index and is created to make it dynamic
*	Starts at first obj, which is 0
*
*/
class CurrElem{
	private int currElem = 0;
	
	public void setElem(int currElem){
		this.currElem = currElem;
		
	}
	
	public int getElem(){
		return currElem;
	}
}
/*
* global variables of nList and Doc
* +Handles changed parameters and saves the read in of doc
*
*
*/
class XMLNodelist {
	private NodeList nList = null;
	private Document doc = null;
	
	public void setList(NodeList nList){
		this.nList = nList;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	public NodeList getList() {
		return nList;
	}
	
	public Document getDoc() {
		return doc;
	}
}
/*
*	Main Class: XMLProgram2
	*	+displayMenu
			+Shows menu of choices
	*	+userChoice
			+Handles logic flow of user choices 
	*	+readFile
			+Reads in file and saves it as document obj
	*	+displayContents
			+Shows contents of current elem, reused with next/prev commands
		+changeContents
			+Changes param of current elem
		+writeFile
			+Saves changes to new file
*/

public class XMLProgram2 {

	public static void displayMenu(){
		System.out.println(
		"SHOW(s)\t\t\t Print contents of current element\n" +
		"CHANGE(c)\t\t Change <param> of current element\n"  +
		"WRITE(w)\t\t DOM obj to XML txt file\n" + 
		"NEXT(n)\t\t\t Output contents of next elements\n" + 
		"PREVIOUS(p)\t\t Output contents of prev elem\n"+
		"EXIT(e)\t\t\t Quit\n"
		);
	}
	
	public static boolean userChoice(String userInput, CurrElem c, XMLNodelist nList){
		String search = "scwnpe";
		Scanner scnr = new Scanner(System.in);
		try {
			if (search.contains(userInput)) {
				switch(userInput) {
					case "s":
						displayContents(nList.getList(), c.getElem());
						break;
					case "c":
						System.out.println("Input Param: ");
						String cParam = scnr.next();
						
						System.out.println("Input New Value: ");
						String newVal = scnr.next();
						changeContents(cParam, nList, c.getElem(), newVal);
						
						break;
					case "w":
						writeFile(nList.getDoc());
						break;
					case "n":
						displayContents(nList.getList(), c.getElem()+1);
						break;
					case "p":
						displayContents(nList.getList(), c.getElem()-1);
						break;
					case "e":
						return false;
					default:
						System.out.println("Please use abbreviated commands or non numeric input");
				}
					
			}
			
			else {
				System.out.println("Please use abbreviated commands or non numeric input");
			}
		}
		
		catch (Exception NullPointerException) {
			System.out.println("No previous or next element found");
		}
		return true;
		
		
	}
	public static void readFile(XMLNodelist nList) throws Exception {
		try {
			List<Node> delete = new ArrayList<Node>();
			File inputFile = new File("input.txt");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			nList.setDoc(doc);		//Saves doc so to not repeat read in process
			NodeList nL = doc.getDocumentElement().getChildNodes();

			for (int temp = 0; temp < nL.getLength(); temp++) {
				Node nNode = nL.item(temp);
				if ((nNode.getNodeType() != Node.ELEMENT_NODE))  {
					delete.add(nNode);
				}
				
			}
			
			for( int i=0; i < delete.size(); i++ ) {
				Node node = delete.get(i);
				node.getParentNode().removeChild(node);
			}
			nList.setList(nL);
		} 
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void displayContents(NodeList nList, int c){
		Node nNode = nList.item(c);
		if ((nNode.getNodeType() == Node.ELEMENT_NODE))  {
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			System.out.println("\nContents :" + nNode.getTextContent());
		}

	}
	public static void changeContents (String cParam, XMLNodelist nList,int c, String newVal) throws Exception {
		NodeList nL = nList.getList();
		Document doc = nList.getDoc();
		Node n = nL.item(c);
		
        Element emp = (Element) n;
		
		Node name = emp.getElementsByTagName(cParam).item(0).getFirstChild();
        name.setNodeValue(newVal);
		emp.getElementsByTagName(cParam).item(0).setTextContent(newVal);
		nList.setList(nL);
	}
	
	
	public static void writeFile (Document doc) throws Exception{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(".\\newInput.xml"));
		transformer.transform(source, result);
		System.out.println("XML file updated successfully");
	}
	
	public static void main(String[]args) throws Exception {
		CurrElem c = new CurrElem();
		c.setElem(0); //Set the value to change current element
		XMLNodelist nList= new XMLNodelist();
		Scanner scnr = new Scanner(System.in);
		readFile(nList);
		boolean isRunning = true;
		
		while (isRunning) {
			displayMenu();
			String userInput = scnr.next().toLowerCase();
			isRunning = userChoice(userInput,c, nList);
		}
	}
}
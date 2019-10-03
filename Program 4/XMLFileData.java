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

	private Document doc = null;
	private NodeList nList = null;
	private ArrayList<String> segNumsL = new ArrayList<String>();
	private ArrayList<String> segLensL = new ArrayList<String>();
	private ArrayList<String> speedLimsL = new ArrayList<String>();
	private String filename;
	private String[][] courseData;
	
	XMLFileData(){
		this.doc = null;
		this.nList = null;
		this.segNumsL = segNumsL;
		this.segLensL = segLensL;
		this.speedLimsL = speedLimsL;
	}
	
	XMLFileData(ArrayList<String> segNumsL, ArrayList<String> segLensL, ArrayList<String> speedLimsL){
		this.doc = null;
		this.nList = null;
		this.segNumsL = segNumsL;
		this.segLensL = segLensL;
		this.speedLimsL = speedLimsL;
	}
	
	
	public void setList(NodeList nList){
		this.nList = nList;
	}
	
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	
	public void setSegNumList(ArrayList<String> segNumsL){
		this.segNumsL = segNumsL;
	}
	
	public void setSegLenList(ArrayList<String> segLensL){
		this.segLensL = segLensL;
	}
	
	public void setSpeedLimList(ArrayList<String> speedLimsL){
		this.speedLimsL = speedLimsL;
	}
	
	public void setCourseData(String[][] courseData) {
		this.courseData = courseData;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public ArrayList<String> getSegNumList(){
		return segNumsL;
	}
	
	public ArrayList<String> getSegLenList(){
		return segLensL;
	}
	
	public ArrayList<String> getSpeedLimList(){
		return speedLimsL;
	}
	
	public Document getDoc() {
		return doc;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String[][] getCourseData(){
		return courseData;
	}
	
	public NodeList getList() {
		return nList;
	}
	
		/*Converts string array list to integer array list, used for converting numbers for math operations*/
	public ArrayList<Integer> getIntegerArray(List<String> stringArray) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(String stringValue : stringArray) {
			try {
				//Convert String to Integer, and store it into integer array list.
				result.add(Integer.parseInt(stringValue));
			} 
			catch(NumberFormatException nfe) {
				System.out.println("Could not parse ");
			} 
		}		
		return result;
	}
	
	/*Converts string array list to double array list, used for converting numbers for math operations*/
	public ArrayList<Double> getDoubleArray(List<String> stringArray) {
		ArrayList<Double> result = new ArrayList<Double>();
		for(String stringValue : stringArray) {
			try {
				//Convert String to Integer, and store it into integer array list.
				result.add(Double.parseDouble(stringValue));
			} 
			catch(NumberFormatException nfe) {
				System.out.println("Could not parse ");
			} 
		}		
		return result;
	}
	
	/*Checks to see if segement numbers are consecutive, non overlapping, and non repeating*/
	public boolean isValidSegNum(List<String> strSegNums) {
		List<String> SstrSegNums = new ArrayList<>(strSegNums);
		Collections.sort(SstrSegNums);
		ArrayList<Integer> segNums = getIntegerArray(SstrSegNums);
		int size = segNums.size();
		int sizeDiff = 0;
		
		/*Traverse through list and subtract current element from next element to find a difference
			-If the difference is not 1 then that means it is not consecutive
		  Then check is current element is not the same as next element to see if a num is repeating
		*/
		
		for (int i = 0; i < size-1; i++) {
			sizeDiff = segNums.get(i+1) - segNums.get(i);
			if (sizeDiff != 1 || segNums.get(i+1) == segNums.get(i)) {
				return false;
			}
		}
		return true;
	}
	
	/*Checks to see if the length of segments is between 0.5< len < 1.0*/
	public boolean isValidSegLen(List<String> strSegLens) {
		ArrayList<Double> segLens = getDoubleArray(strSegLens);
		int size = segLens.size();
		
		/*Traverse through list to see if the current element is between 0.5< len < 1.0 */
		for (int i = 0; i < size; i++) {
			if(segLens.get(i) < 0.5 || segLens.get(i) > 1.0) {
				return false;
			}
		}
		return true;
	}
	
	/*Checks to see if speedLimits is between 5 < speedLimits < 80*/
	public boolean isValidSpeedLim(List<String> strSpeedLims) {
		ArrayList<Double> speedLims = getDoubleArray(strSpeedLims);
		int size = speedLims.size();
		
		/*Traverse through list */
		for (int i = 0; i < size; i++) {
			if(speedLims.get(i) < 5 || speedLims.get(i) > 80) {
				return false;
			}
		}
		return true;
	}
	
	/*Creates an array based on number of segments so Car.java can use it*/
	public String[][] courseInfo () {

		int size = segNumsL.size();
		String courseData [][] = new String[size][3];
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < 3; j++) {
				if (j == 0) {
					courseData[i][j] = segNumsL.get(i);
				}
				
				else if (j == 1) {
					courseData[i][j] = segLensL.get(i);
				}
				
				else {
					courseData[i][j] = speedLimsL.get(i);
				}
			}
		}
		System.out.println(Arrays.deepToString(courseData));
		this.courseData = courseData;
		return courseData;
		
	}
	
	
	public void readFile(String filename) {
		try {
			//System.out.println(filename);
			ArrayList<String> segNums = new ArrayList<String>();
			ArrayList<String> segLens = new ArrayList<String>();
			ArrayList<String> speedLims = new ArrayList<String>();
			
			List<Node> delete = new ArrayList<Node>();
			
			File inputFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			this.doc = dBuilder.parse(inputFile);
			this.doc.getDocumentElement().normalize();
			
			this.setDoc(doc);		//Saves doc so to not repeat read in process
			
			
			NodeList nL = doc.getElementsByTagName("SEGMENT");
			
			//Get all info from tags and store in string array lists
			 for (int temp = 0; temp < nL.getLength(); temp++) {
				Node nNode = nL.item(temp);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					//replaceAll helps with removing unwanted spaces
					String segNum = eElement.getElementsByTagName("SEGMENT_NUMBER").item(0).getTextContent().replaceAll("\\s+", "");
					String segLen = eElement.getElementsByTagName("LENGTH").item(0).getTextContent().replaceAll("\\s+", "");
					String speedLim = eElement.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent().replaceAll("\\s+", "");
					
					segNums.add(segNum);
					segLens.add(segLen);
					speedLims.add(speedLim);
				}
			}

			this.setList(nL);
	
			
			//Add to XMLFileData to preserve and save data
			this.setSegNumList(segNums);
			this.setSegLenList(segLens);
			this.setSpeedLimList(speedLims);
			this.filename = filename;
			

			//Verify to see if the xml info is valid for the program
			if (!isValidSegNum(segNums) || !isValidSegLen(segLens) || !isValidSpeedLim(speedLims)) {
				System.out.println("XML File not valid for course");
				return;
			}

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
	
	public void changeContents (String cParam, int c, String newVal) throws Exception {
		Node n = nList.item(c);
        Element emp = (Element) n;

		
		Node name = emp.getElementsByTagName(cParam).item(0).getFirstChild();
        name.setNodeValue(newVal);
		emp.getElementsByTagName(cParam).item(0).setTextContent(newVal);
		this.nList = nList;
	}
	
	public void writeFile (String filename) throws Exception{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filename));
		transformer.transform(source, result);
		System.out.println("XML file updated successfully");
	}
}
/*******************************************************
*@author: Kimberly Morales
Files Requirements:
	+XMLFileData.java
	+Car.java

Purpose
-To use the XML Parser and Car PRogram to make a course with more segments. 
 This will be done by reading in the segments from the file and storing it to another class.
 Then the data can be reused for Car.java calculations.

History
-9/16/19: 
	+Initial Version
-9/18/19: 
	+Did user input in main
	+Added try/catch in main
	+Created readFile
	+Added XMLNodelist
	+Extends Car
-9/19/19
	+Created getIntegerArray
	+Created getDoubleArray
	+Created isValidSegLen
	+Created isValidSegNum
	+Created isValidSpeedLim
	+Created validation to see if xml course file is valid within readFile
	+Created temp test statements
-9/20/19
	+Fixed indentation
	+Fixed weird var names
	+Modifyed Car.java to have 2d double array of course info
-9/21/19
	+Changed XMLNodelist to XMLFileData
	+Added arraylists to XMLFileData
	+Moved XMLFileData to another java file to seperate classes
	+Changed Car.java's math calculations to make it more dynamic
-9/22/19
	+Final version

***********************************************************/
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
/*	Constraints:
*		(!) -Handled Constraint
*
*		+Allow any num of segments !
*		+Each segment must be at least 0.5 miles in length !
*		+Segs must be consecutive and non overlapping !
*		+Segments cant have same num !
*		+No gaps of segs !
*/

class Program3 extends Car {
	
	/*Converts string array list to integer array list, used for converting numbers for math operations*/
	public static ArrayList<Integer> getIntegerArray(List<String> stringArray) {
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
	public static ArrayList<Double> getDoubleArray(List<String> stringArray) {
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
	public static boolean isValidSegNum(List<String> strSegNums) {
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
	public static boolean isValidSegLen(List<String> strSegLens) {
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
	public static boolean isValidSpeedLim(List<String> strSpeedLims) {
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
	
	/*Reads in XML file while also checking to see if it is a valid course file*/
	public static void readFile(XMLFileData data, String filename) throws Exception {
		try {
			ArrayList<String> segNums = new ArrayList<String>();
			ArrayList<String> segLens = new ArrayList<String>();
			ArrayList<String> speedLims = new ArrayList<String>();
			
			File inputFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			data.setDoc(doc);		//Saves doc so to not repeat read in process
			NodeList nL = doc.getElementsByTagName("SEGMENT");
			
			/*Get all info from tags and store in string array lists*/
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
			
			//Add to XMLFileData to preserve and save data
			data.setSegNumList(segNums);
			data.setSegLenList(segLens);
			data.setSpeedLimList(speedLims);
			
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
	
	/*Creates an array based on number of segments so Car.java can use it*/
	public static double[][] courseInfo (XMLFileData data) {
		//Convert all string array lists to Double for math
		ArrayList<Double> segNums = getDoubleArray(data.getSegNumList());
		ArrayList<Double> segLens = getDoubleArray(data.getSegLenList());
		ArrayList<Double> speedLims = getDoubleArray(data.getSpeedLimList());
		
		int size = segNums.size();
		double courseData [][] = new double[size][3];
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < 3; j++) {
				if (j == 0) {
					courseData[i][j] = segNums.get(i);
				}
				
				else if (j == 1) {
					courseData[i][j] = segLens.get(i);
				}
				
				else {
					courseData[i][j] = speedLims.get(i);
				}
			}
		}
		
		return courseData;
		
	}

	public static void main (String [] args) throws Exception {
		Scanner scnr = new Scanner(System.in);
		XMLFileData data = new XMLFileData();

		String userFilename = "";
		
		if (userFilename == "") {
			System.out.println("No filename specified in code\n");
			System.out.println("NOTE: Type in filename with extension (ex. MyFile.txt)");
			System.out.println("Input filename: ");
			userFilename = scnr.nextLine();
		}
		
		readFile(data, userFilename);
		
		double [][] courseData = courseInfo(data);
		Car A = new Car("A",0.0,courseData);
		Car B = new Car("B",60.0,courseData);
		Car C = new Car("C",120.0,courseData);
		Car allCars[] = {A,B,C};
		
		displayTable(allCars);
		
	}
}

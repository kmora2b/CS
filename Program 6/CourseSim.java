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


class CourseSim {
	private ArrayList<Segment> allSegments;
	private ArrayList<String> segNumsL;
	private ArrayList<String> segLensL;
	private ArrayList<String> speedLimsL;
	private ArrayList<String> numOfLanesL;
	private double[][] courseData;
	
	
	//SETTERS
	public void setSegNumList(ArrayList<String> segNumsL){
		this.segNumsL = segNumsL;
	}
	
	public void setSegLenList(ArrayList<String> segLensL){
		this.segLensL = segLensL;
	}
	
	public void setSegSpeedLimList(ArrayList<String> speedLimsL){
		this.speedLimsL = speedLimsL;
	}
	
	public void setSegmentsList(ArrayList<Segment> allSegments){
		this.allSegments = allSegments;
	}
	
	public void setCourseStringData(double[][] courseData) {
		this.courseData = courseData;
	}
	
	public void setNumOfLanesList(ArrayList<String> numOfLanesL){
		this.numOfLanesL = numOfLanesL;
	}
	//GETTERS
	public ArrayList<String> getSegNumList(){
		return segNumsL;
	}
	
	public ArrayList<String> getSegLenList(){
		return segLensL;
	}
	
	public ArrayList<String> getSegSpeedLimList(){
		return speedLimsL;
	}
	
	public ArrayList<Segment> getSegmentsList(){
		return allSegments;
	}
	
	public double[][] getCourseData(){
		return courseData;
	}
	
	public ArrayList<String> getNumOfLanesList(){
		return numOfLanesL;
	}
	
	//Convert seconds to hours for conversions
	public double convertSecsToHours(double ts){
		return ts / 3600;
	}
	
	//Convert ft/s/s to mph/s
	//1.467 is conversion factor
	public double convertFSStoMPHS(double a){
		return a / 1.467;
	}
	
	/*Sorts 2d array of course data based on first column (Segment numbers)*/
	public void sort2D(double [] [] array, int column_index){
		for(int i=1; i<array.length; i++){
			int index = i;
			for(int j=i-1; j>=0; j--, index--)
				if(array[j][column_index]>array[index][column_index])
					swap2rows(array, index,j);
				else
					break;
		}
	}
	
	/*Swaps elements of sort2D */
	public void swap2rows(double [][] array, int index, int j){
		double temp;
		for(int i=0; i<array[j].length; i++){
			temp = array[j][i];
			array[j][i] = array[index][i];
			array[index][i] = temp;
		}
	}
	
	/*Total distance travelled*/
	public double sumLen(double[][] array2d){
		double sum = 0;

		for(int i=0; i < array2d.length; i++) {
			sum += array2d[i][1];
		}
		
		return sum;
	}
	
	
	//Calculate distance at an instance
	public double calculateDistance(double a, double vf, double v, double timeHour ){
		double t = (vf-v) / a;
		double mphA = convertFSStoMPHS(a); //Convert from ft/s/s to mph/s
		
		return ( (v * timeHour) + (0.5 * mphA * timeHour* timeHour));
	}
	
	
	/*Converts string array list to integer array list, used for converting numbers for math operations*/
	private ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
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
	private ArrayList<Double> getDoubleArray(ArrayList<String> stringArray) {
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
	private boolean isValidSegNum(ArrayList<String> strSegNums) {
		//List<String> SstrSegNums = new ArrayList<>(strSegNums);
		Collections.sort(segNumsL);
		ArrayList<Integer> segNums = getIntegerArray(segNumsL);
		int size = segNums.size();
		int sizeDiff = 0;
		
		/*Traverse through list and subtract current element from next element to find a difference
			-If the difference is not 1 then that means it is not consecutive
		  Then check is current element is not the same as next element to see if a num is repeating
		*/
		
		for (int i = 0; i < size-1; i++) {
			sizeDiff = segNums.get(i+1) - segNums.get(i);

			if (sizeDiff != 1 || segNums.get(i+1) == segNums.get(i) || segNums.get(i) <= 0) {
				return false;
			}
			
		}
		
		return true;
	}
	
	/*Checks to see if the length of segments is between 0.5< len < 1.0*/
	private boolean isValidSegLen(ArrayList<String> segLensL) {
		ArrayList<Double> segLens = getDoubleArray(segLensL);
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
	private boolean isValidSpeedLim(ArrayList<String> segSpeedLimsL) {
		ArrayList<Double> speedLims = getDoubleArray(segSpeedLimsL);
		int size = speedLims.size();
		
		/*Traverse through list */
		for (int i = 0; i < size; i++) {
			if(speedLims.get(i) < 5 || speedLims.get(i) > 80) {
				return false;
			}
		}
		
		return true;
	}
	
	//Check to see if all number of lanes in each segment is the same number as the first segment
	private boolean isValidLanes(ArrayList<String> numOfLanesL) {
		ArrayList<Integer> lanes = getIntegerArray(numOfLanesL);
		int size = lanes.size();
		int numOfLanes = lanes.get(0);
		
		for (int i = 0; i < size; i++) {
			if(lanes.get(i) != numOfLanes || lanes.get(i) <= 0) {
				return false;
			}
		}
 
		return true;
		
	}
	
	//Checks all validation functions to see if XML file has accurate data
	private boolean isValidCourse(){
		if (!isValidSegNum(segNumsL) || !isValidSegLen(segLensL) || !isValidSpeedLim(speedLimsL) || !isValidLanes(numOfLanesL)){
			System.out.println("Segment info in XML file is invalid");
			System.exit(0);
		}
		
		return true;
		
	}
	
	/*Creates an array based on number of segments so Car.java can use it*/
	public double[][] genCourseInfo () {
		ArrayList<Integer> segNums = getIntegerArray(segNumsL);
		ArrayList<Double> segLens = getDoubleArray(segLensL);
		ArrayList<Double> speedLims = getDoubleArray(speedLimsL);
		ArrayList<Integer> numOfLanes = getIntegerArray(numOfLanesL);
		
		int rows = segNumsL.size();
		int numAttr = 3;	//Number of columns
		double courseData [][] = new double[rows][numAttr];
		if (isValidCourse()){
			for(int i = 0; i < rows; i++) {
				for(int j = 0; j < 3; j++) {
					if (j == 0) {
						courseData[i][j] = segNums.get(i);
					}
					
					else if (j == 1) {
						courseData[i][j] = segLens.get(i);
					}
					
					else if (j == 2) {
						courseData[i][j] = speedLims.get(i);
					}
					
					else {
						courseData[i][j] = numOfLanes.get(i);
					}
				}
			}
		}
		//System.out.println(Arrays.deepToString(courseData));
		return courseData;
		
	}

}

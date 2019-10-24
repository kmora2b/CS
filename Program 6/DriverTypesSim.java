/*******************************************************
*@author: Kimberly Morales
Files Requirements:
	+XMLFileData.java
	+Segment.java
	+Driver.java
	+DriverType.java
	+CourseSim.java
	+(Optional) input.xml

Purpose
-To create specialized courseSim that takes into account of different driver types that have their own speeds and
accelearations. These driver types are assigned to each driver and will determine a drivers trajectory in the course.


History
-10/03/19: 
	+Initial Version

-10/04/19: 
	+Made MyTableModel more dynamic by adding constructor
	+Segment class made
	+Moved objects from XMLFileData to new class Segment
	+Renamed Program3 to CourseSim
	+Changed car methods to non static forms

-10/05/19: 
	+Deconstructed readFile in XMLFileData to smaller functions to read in segment, driver, driver type
	+Redone math calculations to make it more intelligent
	+Made getXMLTagData to grab different XML tags
	+Instead of long if statement of validation of file, decided to make different funtion
	+Added getters and setters for ArrayLists for CourseSim
	+Updated input.txt
	+Made xmlTag String in XMLFileData.readFile use equalsIgnoreCase

-10/06/19: 
	+Rename getXMLTagInfo to setXMLTagInfoFromXML to indicate file reading
	+setXMLTagInfoFromXML created to set info more modular
	+Renamed DriverTypes.java to DriverTypesSim to indicate it is the main interface
	+getXMLTagInfoFromXML can now be more dynamic with elements
	+Created Segment, Driver, DriverType classes
	+Made DriverTypeSim 
-10/09/19: 
	+Reprogrammed calculateDistance in Car to make it accurately calculate accelearation
	+Used driver instead of Car for calulcations
	+Used arraylist instead of arrays in calculateDistancetValuesForCars
	+Made final double maxBrakeRate
	+Made conversion from f/s/s to mph/s/s
-10/10-19
	+Completely reworked Car calucaltions
-10/13/19
	+Final version for lab 5
-10/15/19
	+Added in lane num to allSegments


***********************************************************/

/*	Constraints:
*		(!) -Handled Constraint
*
*/
import java.math.*; 
import java.util.ArrayList;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.util.*;
class DriverTypesSim extends CourseSim {
	private XMLFileData data;
	private ArrayList<Driver> allDrivers;
	private ArrayList<DriverType> allDriverTypes; 
	private final double maxBrakeRate = 10.2273;
	
	public DriverTypesSim() {
		//Read in file and save its nodelist of the xml file
	
		Scanner scnr = new Scanner(System.in);
		System.out.println("Input filename: ");
		String filename = scnr.nextLine();
		
		ArrayList<ArrayList<Double>> printData = new ArrayList<ArrayList<Double>>();
		XMLFileData data = new XMLFileData(filename);
		data.readFile();

		//Use nodelist read from xml and store in info for all classes created
		getXMLTagInfoFromXML(data.getList());
		
		//Generate course info
		double [][] courseInfo = genCourseInfo();	//Segment info
		//System.out.println(Arrays.deepToString(courseInfo));
		int numOfLaps = 2;		//Num of laps
		double timeStep = 30.0;	//Step interval for table data

		//Sort courses in correct order
		sort2D(courseInfo, 0);
		
		//Add all lengths for all segments
		double totalDistance  = sumLen(courseInfo);
		
		//Generate data from drivers and print it
		printData = calculateDistanceValuesForDrivers(numOfLaps,totalDistance,courseInfo);
		printTable(printData);
	}
	
	//Setters
	public void setAllDrivers(ArrayList<Driver> allDrivers) {
		this.allDrivers = allDrivers;
	}
	
	public void setAllDriverTypes(ArrayList<DriverType> allDriverTypes) {
		this.allDriverTypes = allDriverTypes;
	}
	
	//getters
	public ArrayList<Driver> getAllDrivers() {
		return allDrivers;
	}
	
	public ArrayList<DriverType> getAllDriverTypes() {
		return allDriverTypes;
	}

	
	//Calculates all distances for all the drivers and also handles when the driver needs to slow down
	public ArrayList<ArrayList<Double>> calculateDistanceValuesForDrivers(double numOfLaps,double totalDistance, double [][] courseInfo){
		ArrayList<ArrayList<Double>> record =  new ArrayList<ArrayList<Double> >(); 		//Records all data dynamically
		ArrayList<Double> row =  new ArrayList<Double>();									//Gets Time,Speed, Location
		
		try {
			double speedLim[] = new double[allDrivers.size()];								//Current Speedlimit
			double timeSec = 0.0;															//time in seconds
			double timeHour = 0.0;  														//time in hours

			double distanceCount[] = new double[allDrivers.size()];						//Keeps track of current amount of distance travlled

			double currSegLen[]= new double[allDrivers.size()];							//Current segment length car is on
			int currSeg[]= {0,0,0};														//Current segment car is on
			double prevV []= new double[allDrivers.size()];								//Previous speedLim
			double timeSecStart []= new double[allDrivers.size()];						//When each car starts
			
			//Keep looping until max total distance reached * numOfLaps is reached for all cars
			while (distanceCount[0] < (numOfLaps * totalDistance) && distanceCount[1] < (numOfLaps *totalDistance) && distanceCount[2] < (numOfLaps *totalDistance)) {
				//Get speedLim for each driver depending on driverType and segment speed limit
				speedLim = InitializeDriverSpeedLim(currSeg, timeSec, courseInfo, currSegLen);
				timeHour = convertSecsToHours(timeSec);
				
				//Keep record of previous speed limits
				prevV = speedLim.clone();
				
				//Change or keep current segment data depending if the car has reached the max length
				for(int i = 0; i < allDrivers.size(); i++){
					if (distanceCount[i] < currSegLen[i]) {
						if(currSeg[i] < courseInfo.length-1){
							currSeg[i] = currSeg[i] + 1;
							currSegLen[i] = courseInfo[currSeg[i]][i];
						}
						else {
							currSeg[i] = 0;
						}
					}
				}
				
				
				//Make sure that each driver is keeping a certain distance depending on followTime distance
				for(int i = 0; i < allDrivers.size(); i++){
					for(int j =1 ; j < allDrivers.size()-1; j++){
						double followTimeDistance =speedLim[i] * convertSecsToHours(allDrivers.get(i).getDriverType().getFollowTime());
						double aheadDriverDistance = distanceCount[j];
						double behindDriverDistance = distanceCount[i] - aheadDriverDistance;
						
						if( followTimeDistance < behindDriverDistance)
							allDrivers.get(i).getDriverType().setMaxAccel(maxBrakeRate);
						
					}
				}
				

				//Calculate distance from Car.java function and add to the current distance count for each driver
				distanceCount[0] += round(calculateDistance(allDrivers.get(0).getDriverType().getMaxAccel(), speedLim[0],prevV[0], timeHour),2);
				
				//Calculate distances for drivers that start later
				if (timeSec >= 60) {
					distanceCount[1] += round(calculateDistance(allDrivers.get(1).getDriverType().getMaxAccel(), speedLim[1], prevV[1], convertSecsToHours(timeSec-timeSecStart[1])), 2);
				}
				
				if (timeSec >= 120) {
					distanceCount[2] += round(calculateDistance(allDrivers.get(2).getDriverType().getMaxAccel(), speedLim[2], prevV[2], convertSecsToHours(timeSec-timeSecStart[2])),2);
				}
				
				//Set the row info to later be printed and add to the overall table called record
				row = setRowInfo(timeSec, speedLim, distanceCount);
				record.add(row);
					
				//Add to the currSegLen counter to see how far each driver is in each segment
				for(int i = 0; i < currSegLen.length; i++)
					currSegLen[i] += distanceCount[i];
				
				timeSec+=30;
			}
		}
		
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("No info for driver was read into the program due to non existing drivertype");
			System.exit(0);
		}
	
		return record;
	}
	
	//Print Time(s) and each driver names on the first header
	private void printHeader(){
		System.out.print("Time(s)\t");
		for (int i = 0; i < allDrivers.size();i++){
			System.out.print("\t\t" + allDrivers.get(i).getName() + "\t\t");
		}
		System.out.println();
		
		for (int i = 0; i < allDrivers.size();i++){
			System.out.print("\t        speed\t      location");
		}
		System.out.println();
	}
	
	//Print whole table 
	private void printTable(ArrayList<ArrayList<Double>> table){
		printHeader();
		
		for (int i = 0; i < table.size();i++){
			for (int j = 0; j < table.get(i).size();j++){
				System.out.printf("%.2f\t\t",table.get(i).get(j));
			}
			System.out.println();
		}
		
	}
	
	//Round numbers to print table easier
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	//Set the info for the 2d Arraylist
	private ArrayList<Double> setRowInfo(double timeSec, double[]speedLim, double[] distanceCount){
		ArrayList<Double> row =  new ArrayList<Double>();
		row.add(timeSec);
		for (int i = 0; i < allDrivers.size();i++){
			row.add(speedLim[i]);
			row.add(distanceCount[i]);
		}
		return row;
	}
	
	
	//Set up drivers speed limit depending on segment info, drivertype, and starttime
	private double[] InitializeDriverSpeedLim(int currSeg[], double timeSec,double [][] courseInfo, double [] segLen){
		double speeds[] = new double[allDrivers.size()];
		
		for(int i =0; i < speeds.length; i++) {
		
			if (timeSec == 0)
				speeds[i] = 0;
			
			//If the the driver's speed limit less than the courses current segment speed limit then either:
			//-If the startTime is more than the current time then the driver has not started so they have no speed
			//-Else, the driver has started and just set the drivers current speed limit based on drivertype
			else if((allDrivers.get(i).getDriverType().getDriverTypeSpeedLim() < courseInfo[currSeg[i]][2])){
				if (allDrivers.get(i).getStartTime() >  timeSec && allDrivers.get(i).getStartTime() != 0)
					speeds[i] = 0;
				else
					speeds[i] = allDrivers.get(i).getDriverType().getDriverTypeSpeedLim();
			}
			
			
			else {
				if (allDrivers.get(i).getStartTime() >  timeSec && allDrivers.get(i).getStartTime() != 0)
					speeds[i] = 0;
				else
					speeds[i] = courseInfo[ currSeg[i] ][2];
			}
			
		}
		
		return speeds;
	}
	
	//Gather all data from XML file
	public void getXMLTagInfoFromXML(NodeList nL){
		ArrayList<Segment> allSegments = new ArrayList<Segment>();
		ArrayList<DriverType> allDriverTypes = new ArrayList<DriverType>();
		ArrayList<Driver> allDrivers = new ArrayList<Driver>();
		ArrayList<String> segNumsL = new ArrayList<String>();
		ArrayList<String> segLensL = new ArrayList<String>();
		ArrayList<String> segSpeedLimsL = new ArrayList<String>();
		ArrayList<String> segLanes = new ArrayList<String>();
		
		try {
			for (int i = 0; i < nL.getLength(); i++) {
				if (nL.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) nL.item(i);

					//Get Segment info and store into arraylist
					if (el.getNodeName().equalsIgnoreCase("SEGMENT")) {
						String segNum = el.getElementsByTagName("SEGMENT_NUMBER").item(0).getTextContent().replaceAll("\\s+", "");
						String segLen = el.getElementsByTagName("LENGTH").item(0).getTextContent().replaceAll("\\s+", "");
						String segSpeedLim = el.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent().replaceAll("\\s+", "");
						String segLane = el.getElementsByTagName("LANES").item(0).getTextContent().replaceAll("\\s+", "");
						segNumsL.add(segNum);
						segLensL.add(segLen);
						segSpeedLimsL.add(segSpeedLim);
						segLanes.add(segLane);
						Segment segment = new Segment(Integer.valueOf(segNum), Double.valueOf(segLen), Double.valueOf(segSpeedLim), Integer.valueOf(segLane));
						
						allSegments.add(segment);
					}
					
					else if (el.getNodeName().equalsIgnoreCase("DRIVER_TYPE")) {
						String driverTypeName = el.getElementsByTagName("TYPE_NAME").item(0).getTextContent().replaceAll("\\s+", "");
						String driverFollowTime = el.getElementsByTagName("FOLLOW_TIME").item(0).getTextContent().replaceAll("\\s+", "");
						String driverTypeSpeedLim = el.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent().replaceAll("\\s+", "");
						String driverTypeMaxAccel = el.getElementsByTagName("MAX_ACCELERATION").item(0).getTextContent().replaceAll("\\s+", "");
						
						//Make sure time or speed limit is negative
						if (Double.parseDouble(driverFollowTime) < 0 ||  Double.parseDouble(driverTypeSpeedLim) < 0) {
							throw new IllegalArgumentException("Driver Type info is invalid with negative values in followTime or speed limit");
							
						}
					
						
						DriverType driverType = new DriverType(driverTypeName, Double.parseDouble(driverFollowTime), Double.parseDouble(driverTypeSpeedLim), Double.parseDouble(driverTypeMaxAccel) );
						allDriverTypes.add( driverType);
					}
					
				}
			}
			
			double timeStep = 60;   //time delay 
			double time = 0;	   //Counter of time delay
			int laneCount = 1;	  //Counter for alternating lane
			int numOfLanes = allSegments.get(0).getNumOfLanes();   //The total number of lanes
			
			//Since driver type is read in, add in driver type info to driver objects
			for (int i = 0; i < nL.getLength(); i++) {
				if (nL.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) nL.item(i);

					if (el.getNodeName().equalsIgnoreCase("DRIVER")) {
						String driverName = el.getElementsByTagName("NAME").item(0).getTextContent().replaceAll("\\s+", "");
						String driverDriverType = el.getElementsByTagName("DRIVER_TYPE").item(0).getTextContent().replaceAll("\\s+", "");
						
						
						for (int j = 0; j < allDriverTypes.size(); j++){
							if(driverDriverType.equalsIgnoreCase(allDriverTypes.get(j).getDriverTypeName())){
								//Reset lanecount to 1 if over the number of lanes
								if (laneCount > numOfLanes) {
									laneCount = 1;
								}
								
								Driver driver = new Driver(driverName, time,allDriverTypes.get(j), laneCount);
								allDrivers.add(driver);
								time+=timeStep;
								laneCount++;
							}
						}
						
					}
					
				}
			}
		}
		
		catch (NumberFormatException nfe) {
			System.out.println("Invalid XML File has invalid datatype");
			System.exit(0);
		}
		
		catch (NullPointerException npe) {
			System.out.println("Missing attribute in XML tag");
			System.exit(0);
		}
		catch (IllegalArgumentException iae) {
			System.out.println("Driver Type info is invalid with negative values in followTime or speed limit");
			System.exit(0);
		}
		catch (Exception e) {
			System.out.println("Invalid XML File or error in reading in file");
			System.exit(0);
		}

		setXMLSegTagInfoFromXML(allSegments, segNumsL,segLensL, segSpeedLimsL, segLanes);
		setXMLDriverTagInfoFromXML(allDriverTypes, allDrivers);
	}
	
	//Set the info for Segments
	private void setXMLSegTagInfoFromXML(ArrayList<Segment> allSegments, ArrayList<String>  segNumsL, ArrayList<String> segLensL, ArrayList<String>  segSpeedLimsL, ArrayList<String>  segLanes){
		setSegNumList(segNumsL);
		setSegLenList(segLensL);
		setSegSpeedLimList(segSpeedLimsL);
		setNumOfLanesList(segLanes);
		setSegmentsList(allSegments);
		
	}
	
	//Set info for Drivers and DriverTypes
	private void setXMLDriverTagInfoFromXML(ArrayList<DriverType> allDriverTypes, ArrayList<Driver> allDrivers) {
		setAllDriverTypes(allDriverTypes);
		setAllDrivers(allDrivers);
	}

	public static void main (String [] args) throws Exception {
		DriverTypesSim courseSimDriver = new DriverTypesSim();
	}
	
}
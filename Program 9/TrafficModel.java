import java.math.*; 
import java.util.ArrayList;
import java.util.*;
import java.util.Collections;
import java.beans.ExceptionListener;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.StringWriter;

public class TrafficModel extends CourseData {
	public int numOfLaps = 2;
	public double timeDelay = 60;

	public ArrayList<ArrayList<Double>> record = new ArrayList<ArrayList<Double>>();
	public ArrayList<Double> currSpeedLim, currDistanceCount, currSegLen;

	public ArrayList<Integer> currSeg;
	
	public ArrayList<Double> prevSpeedLim;
	public ArrayList<Double> timeSecStart;
	public boolean[] driverHasFinished;
	
	double timeSec = 0;
	double timeHour = 0;
	boolean running = true;
	TrafficModel() {
		//Scanner scnr = new Scanner(System.in);
		//System.out.println("Input filename: ");
		//String filename = scnr.nextLine();
		
	}
	
	
	
	public void initSimArrayLists(){
		currSpeedLim = new ArrayList<Double>(getAllDrivers().size());		//Current Speedlimit
		currDistanceCount= new ArrayList<Double>(getAllDrivers().size());	//Keeps track of current amount of distance travelled
		
		currSegLen= new ArrayList<Double>(getAllDrivers().size());		//Current segment length car is on
		currSeg = new ArrayList<Integer>(getAllDrivers().size());		//Current segment car is on
		prevSpeedLim = new ArrayList<Double>(getAllDrivers().size());	//Previous speedLim
		timeSecStart = new ArrayList<Double>(getAllDrivers().size());	//When each car starts
		driverHasFinished = new boolean[getAllDrivers().size()];


		for (int i = 0; i < getAllDrivers().size(); i++) {
			currSpeedLim.add(0.0);
			currDistanceCount.add(0.0);
			currSeg.add(0);
			currSegLen.add(0.0);
			prevSpeedLim.add(0.0);
			timeSecStart.add(0.0);
			
		}
		
	}

	public boolean allDriversHaveFinished(){
		//boolean[] driverHasFinished = new boolean[currDistanceCount.size()];
		for(int i = 0; i <  driverHasFinished.length; i++){
			if (currDistanceCount.get(i) >= numOfLaps * totalDistance) {
				driverHasFinished[i] = true;
				//driverHasFinished.set(i,true);
			}
		}
		
		
		for(int i = 0; i <  driverHasFinished.length; i++){
			if(!driverHasFinished[i] ) 
				return false;
		}
		
	
		return true;
	}
	
	public void calculateDistanceValuesForDrivers(){
		ArrayList<Double> row =  new ArrayList<Double>();		//Gets Time,Speed, Location
		initSimArrayLists();

		try {

			//Keep looping until max total distance reached * numOfLaps is reached for all cars
			while (!allDriversHaveFinished()) {
				//Get speedLim for each driver depending on driverType and segment speed limit
				InitializeDriverSpeedLim();
				timeHour = convertSecsToHours(timeSec);
				//Keep record of previous speed limits
				prevSpeedLim  = (ArrayList<Double>)currSpeedLim.clone();
				
				
				
				
				checkCurrentSegment();
				checkFollowTime();

				//Calculate distance from Car.java function and add to the current 
				checkDistanceCount();
				
				//Set the row info to later be printed and add to the overall table called record
				row = setRowInfo();
				record.add(row);
				System.out.println(Arrays.toString(row.toArray()));
				
				System.out.println("P: " + Arrays.toString(prevSpeedLim.toArray()));
				addToCurrSegLen();
				
				timeSec+=0.01;
			}
		}
		
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("No info for driver was read into the program due to non existing drivertype");
			System.exit(0);
		}
	
	}
	
	
	public void checkDistanceCount(){
		double calD = 0;
		for(int i = 0; i < getAllDrivers().size(); i++){
			System.out.println("startTime " + getAllDrivers().get(i).getStartTime());
			if (!driverHasFinished[i]){
					//currDistanceCount.set(i, 0.0);
				
				
				if (getAllDrivers().get(i).getStartTime() == 0) {
					calD = currDistanceCount.get(i) + calculateDistance(getAllDrivers().get(i).getDriverType().getMaxAccel(), currSpeedLim.get(i), prevSpeedLim.get(i), timeHour);
					currDistanceCount.set(i, calD);
				}
			
				
				
				else if (timeSec >= getAllDrivers().get(i).getStartTime() ) {
					calD = currDistanceCount.get(i) + calculateDistance(getAllDrivers().get(i).getDriverType().getMaxAccel(), currSpeedLim.get(i), prevSpeedLim.get(i), convertSecsToHours(timeSec - getAllDrivers().get(i).getStartTime()/2));
					currDistanceCount.set(i, calD);
				}
			}
			System.out.println("DISTANCE OF VEHICLE " + i + ": " + calD);
			System.out.println("SPEED OF VEHICLE IN CDC " + i + ": " + currSpeedLim.get(i));
			System.out.println("PREV SPEED OF VEHICLE IN CDC " + i + ": " + prevSpeedLim.get(i));
			calD = 0;
		}
	
	}
	
	public void checkCurrentSegment(){
		for(int i = 0; i < getAllDrivers().size(); i++){
			if (currDistanceCount.get(i) < currSegLen.get(i)) {
				if(currSeg.get(i) < getCourseData().length-1){
					currSeg.set(i, currSeg.get(i) + 1);
					currSegLen.set(i,getCourseData()[currSeg.get(i)][i]);
				}
				
				else {
					currSeg.set(i,0);
				}
			
			}
		}
	}
	
	public void checkFollowTime(){
		double followTimeDistance = 0;
		double aheadDriverDistance = 0;
		double behindDriverDistance = 0;
		
		for (int i = 0; i < currSpeedLim.size()-1; i++){
			followTimeDistance = currSpeedLim.get(i) * convertSecsToHours(getAllDrivers().get(i).getDriverType().getFollowTime());
			
			aheadDriverDistance = currDistanceCount.get(i+1);
			behindDriverDistance = currDistanceCount.get(i) - aheadDriverDistance;
			
			if( followTimeDistance < behindDriverDistance && currDistanceCount.get(i) != 0){
				getAllDrivers().get(i).getDriverType().setMaxAccel(maxBrakeRate);
			}
		}
	}
	
	public void addToCurrSegLen(){
		//Add to the currSegLen counter to see how far each driver is in each segment
		double addVal = 0;
		for(int i = 0; i < currSegLen.size(); i++){
			addVal = currSegLen.get(i) + currDistanceCount.get(i);
			currSegLen.set(i,addVal);
			addVal = 0;
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
	private ArrayList<Double> setRowInfo(){
		ArrayList<Double> row =  new ArrayList<Double>();
		row.add(timeSec);
		for (int i = 0; i < getAllDrivers().size();i++){
			row.add(currSpeedLim.get(i));
			row.add(currDistanceCount.get(i));
		}
		return row;
	}
	
	
	//Set up drivers speed limit depending on segment info, drivertype, and starttime
	private void InitializeDriverSpeedLim(){
		ArrayList<Double> speeds = new ArrayList<Double>(getAllDrivers().size());
		
		for(int i =0; i < currSpeedLim.size(); i++) {
		
			if (timeSec == 0){
				currSpeedLim.set(i,0.0);
			}
			
			//If the the driver's speed limit less than the courses current segment speed limit then either:
			//-If the startTime is more than the current time then the driver has not started so they have no speed
			//-Else, the driver has started and just set the drivers current speed limit based on drivertype
			else if((getAllDrivers().get(i).getDriverType().getDriverTypeSpeedLim() < getCourseData()[currSeg.get(i)][2])){
				if (getAllDrivers().get(i).getStartTime() >  timeSec && getAllDrivers().get(i).getStartTime() != 0){
					currSpeedLim.set(i,0.0);
				}
				else{
					currSpeedLim.set(i,getAllDrivers().get(i).getDriverType().getDriverTypeSpeedLim());
				}
			}
			
			
			else {
				if (getAllDrivers().get(i).getStartTime() >  timeSec && getAllDrivers().get(i).getStartTime() != 0){
					currSpeedLim.set(i,0.0);
				}

			}
			System.out.println("SPEED OF VEHICLE " + i + ": " + currSpeedLim.get(i));
			
		}

	}
	
	/*public String jaxbObjectToXML() {
		String xmlContent = "";
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(TrafficModel.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
 			StringWriter sw = new StringWriter();
 			jaxbMarshaller.marshal(this, sw);
 			xmlContent = sw.toString();

 		} 
		
		catch (JAXBException e) {
 			e.printStackTrace();
 		}
		
		return xmlContent;
	}*/

	
	
	

}
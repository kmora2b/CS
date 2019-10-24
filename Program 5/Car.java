/*******************************************************
*@author: Kimberly Morales

History
-9/5/19: 
	+Initial Version
-9/6/19: 
	+Created class and methods
	+Created displayTable
-9/7/19
	+Converted table structure to array list
	+Restructured calulculation
-9/8/19
	+Final version
	
-9/11/19
	+Got rid of static methods
	+Made accurate cals

Purpose
-To simulate the distances of three cars(a,b,c) that start one minute apart on a three segment course.
Each segment is 1 mile and the respective speed limits are in mph: 20,60,30. 

***********************************************************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.io.*;
import java.util.*;

class Car {
	private String name;
	private double startTime;
	private double[][] courseData;
	private ArrayList<ArrayList<Double>> record;
	private double accel;
	private double numOfLaps;
	private double speedLim;
	private double followTime;
	
	//Default constructor
	Car(){
		this.name = name;
		this.startTime = startTime;
		this.courseData = courseData;
		this.accel = accel;
	}
	Car(String name, double startTime, double accel, double speedLim,double numOfLaps, double followTime,double[][]courseData) {
		this.name = name;
		this.startTime = startTime;
		this.courseData = courseData;
		this.accel = accel;
		this.numOfLaps = numOfLaps;
		this.speedLim = speedLim;
		this.followTime = followTime;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getstartTime() {
		return startTime;
	}
	
	
	public ArrayList<ArrayList<Double>> getRecord() {
		return record;
	}

	public void setstartTime(double startTime) {
		this.startTime = startTime;
	}

	public void setCourseData(double[][] courseData) {
		this.courseData = courseData;
	}
	
	public double[][] getCourseData(){
		return courseData;
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
	
	
	
	public double calculateDistance(double a, double vf, double v, double timeHour ){
		double t = (vf-v) / a;
		//double timeHour = 
		//return (v * timeHour);
		double mphA = convertFSStoMPHS(a);
		//return ( (v * timeHour) + (0.5 * mphA * timeHour* timeHour));
		return ( (v * timeHour) + (0.5 * mphA * timeHour* timeHour));
	}
	
	
	
	/*Calculate all distance values for a car*/
	public void calculateDistancetValuesForCar(Car c){
		//System.out.println("ARR2" + Arrays.deepToString(c.courseData));
		sort2D(c.courseData, 0);
		double speedLim = 0.0;								//Current Speedlimit
		double timeSec = 0.0;								//time in seconds
		double timeSecRec = 0.0;
		double timeHour = 0.0;  							//time in hours
		double distanceCount = 0.0;							//Keeps track of current amount of distance travlled
		double totalDistance = sumLen(c.courseData);	//Total distance calculated from all segment lengths
		ArrayList<ArrayList<Double>> record =  new ArrayList<ArrayList<Double> >(); //Records all data dynamically
		ArrayList<Double> row =  new ArrayList<Double>();	//Gets Time,Speed, Location
		double currSegLen = 0.0;							//Current segment length car is on
		int currSeg = 0;									//Current segment car is on
		double prevV = 0;
		
		//Keep looping until max total distance reached
		System.out.println( c.name );
		System.out.println("Time(s)\tSpeed\tLocation"  );
		while (distanceCount < (c.numOfLaps *totalDistance)) {
			
			//Keep going on same segment length until it reaches max length
			while(currSegLen <= c.courseData[currSeg][1]){
				
				//Time that car starts on
				if (c.startTime <= timeSec ) {
					//timeSec = 0;
					speedLim = c.courseData[currSeg][2];
					timeHour = c.convertSecsToHours(timeSec);
					distanceCount += calculateDistance(c.accel, speedLim, prevV, timeHour);
					currSegLen += distanceCount;
					
				}
				
				row.add(timeSec);
				row.add(speedLim);
				row.add(distanceCount);
				System.out.println("ROW" + row);
				record.add(row);
				row =  new ArrayList<Double>();
				prevV = speedLim;
				timeSec+=30;
				timeSecRec += timeSec;
			}
			
			
			if (distanceCount <= totalDistance){
				currSeg++;
				currSegLen = 0.0;
			}
			
			else {
				
				currSeg+= 0;
				currSegLen = 0.0;
				speedLim = c.courseData[currSeg][2];
			}
		}
		
			/*record.stream()
    .flatMap(List::stream)
    .map(String::valueOf)
    .forEach(System.out::println);
*/
	}
	
	
}
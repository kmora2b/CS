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
	String name;
	double startTimeS;
	double[][] courseData;
	
	//Default constructor
	Car() {
		this.name = "";
		this.startTimeS = 0;
	}
	
	Car(String name) {
		this.name = name;
	}
	
	Car(String name, double startTimeS){
		this.name = name;
		this.startTimeS = startTimeS;
	}
	
	Car(String name, double startTimeS,double[][]courseData) {
		this.name = name;
		this.startTimeS = startTimeS;
		this.courseData = courseData;
	}
	
	public double[][] getCourseData(){
		return courseData;
	}
	
	//Convert seconds to hours for conversions
	public static double convertSecsToHours(double ts){
		return ts / 3600;
	}

	/*Sorts 2d array of course data based on first column (Segment numbers)*/
	public static void sort2D(double [] [] array, int column_index){
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
	public static void swap2rows(double [][] array, int index, int j){
		double temp;
		for(int i=0; i<array[j].length; i++){
			temp = array[j][i];
			array[j][i] = array[index][i];
			array[index][i] = temp;
		}
	}
	
	/*Total distance travelled*/
	public static double sumLen(double[][] array2d){
		double sum = 0;

		for(int i=0; i < array2d.length; i++) {
			sum += array2d[i][1];
		}
		

		return sum;
	}

	//Calulcate values for car a and to be reused later
	public static double[][] calculateDefaultValues(Car c[]){
		double t = 0.0;							//time in seconds
		double th = 0.0;						//time in hours
		int r = 1;								//Row number, starts at 1
		double dn [] = new double [2];			//contains speed and distance
		double rec[][] = new double[17][7];		//records all speed and distances from dn 
		int currSeg = 0;							//Current segment travelling on
		double currD = 0;
		double segLen = 0;
		double segSpeed = 0;
		double prevSegLen = 0;						
		double sum = sumLen(c[0].getCourseData());	//Total distance travelled
		sort2D(c[0].getCourseData(), 0);	//Sort 2d Array to get correct order of segments
		
		while(r < 17) {
			if (t == 0.0) {
				for (int i = 0; i < rec[0].length; i++) {
					rec[0][i] = 0.0;
				}
			}
			
			else {
				th = convertSecsToHours(t);
				currD = (Math.round( ((segSpeed * th) *100)) / 100.0);
				if(currSeg >= c[0].getCourseData().length || currD >= sum ) {
					currSeg = 0;
					currD = 0;
					prevSegLen = 0;
				}
				
				segLen = c[0].getCourseData()[currSeg][1] + prevSegLen;
				segSpeed = c[0].getCourseData()[currSeg][2];

				if(!(currD < segLen && currD >= prevSegLen)){ 
					prevSegLen = c[0].getCourseData()[currSeg][1] + prevSegLen;
					currSeg++;
					
				}
				
				rec[r][0] = t;
				rec[r][1] = segSpeed;
				rec[r][2] = currD;
				r++;
			}
			t+=30.0;
		}
		return rec;
	}
	
	//Populates values for car b and c
	public static double[][] populateValues(double r[][]) {
		for (int i = 0; i < r.length; i++) {
			for(int j = 0; j < r[0].length; j++) {	
				if (i >= 2) {
					r[i][3] = r[i-1][1];
					r[i][4] = r[i-1][2];
					if (i >= 4) {
						r[i][5] = r[i-3][1];
						r[i][6] = r[i-3][2];
					}
				}
			}
		}
		return r;
	}
	
	
	public static void displayInfo(double r[][]){
		for (int i = 0; i < r.length; i++) {
			for(int j = 0; j < r[0].length; j++) {
				System.out.print(r[i][j] + "\t\t");
			}
			System.out.println();
		}
	}
	
	//Displays overall table of program
	public static void displayTable(Car car []){
		//Array list that contains overall headers
		ArrayList<String[]> tableRow= new ArrayList <String[]>();
		
		String header [] = {"Time(s)\t\t", 
		"car A\t\t\t\t",
		"car B\t\t\t\t",
		"car C\t\t\t\t"};
		
		String header2 [] = {"\t\t", "speed    location\t\t", 
		"speed    location\t\t", 
		"speed    location\t\t"};
		
		tableRow.add(header);
		tableRow.add(header2);
		
		//Print headers
		for (int i = 0; i < tableRow.size(); i++) {
			String[] strings = tableRow.get(i);
			
			for (int j = 0; j < strings.length; j++) {
				System.out.print(strings[j] + " ");
			}
			System.out.println();
			
		}
		//First initial values calulcated then repopulated for B and C then finally display final info
		double r[][] = calculateDefaultValues(car);
		double rp[][] = populateValues(r);
		displayInfo(rp);
	}
	
	public static void main(String[] args) throws Exception{
		Car A = new Car("A",0.0);
		Car B = new Car("B",60.0);
		Car C = new Car("C",120.0);
		Car allCars[] = {A,B,C};
		
		displayTable(allCars);
	}
	
	
	
}
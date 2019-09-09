/*******************************************************
*@author: Kimberly Morales

History
-9/5/19: Initial Version
-9/6/19: Created class and methods, displayTable
-9/7/19: Converted table structure to array list
-9/8/19: Final version

Purpose
-To simulate the distances of three cars(a,b,c) that start one minute apart on a three segment course.
Each segment is 1 mile and the respective speed limits are in mph: 20,60,30. 

***********************************************************/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.*;
import java.util.*;

class Car {
	String name;
	double startTimeS;
	
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
	
	public static double convertSecsToHours(double ts){
		return ts / 3600;
	}
	
	public static double[] calculateDistance(double th, double d ){
		double di [] = new double [2];
		
		if (th ==0.0 || d < 1.0 ) {
			di[0] = 20; 
			di[1] = Math.round(((20 * th) * 100)) / 100.0;
			return di;
		}
		
		else if (d >= 1.0 && d < 2.0) {
			di[0] = 60; 
			di[1] = Math.round( ((60 * th) * 100)) / 100.0;
			return di;
		}
		
		else if (d >= 2.0 && d < 3.0) {
			di[0] = 30; 
			di[1] = Math.round( ((30 * th) *100)) / 100.0;
			return di;
		}
		
		

		double error [] = {0,0}; 
		return error;
	}

	public static double[][] calculateDefaultValues(Car c[]){
		double t = 0.0;
		double th = 0.0;
		double d = 0.0;
		int r = 1;
		double dn [] = new double [2];
		double rec[][] = new double[17][7];
		//Arrays.fill(nums, 0); 

		
		while(r < 17) {
			if (t == 0.0) {
				for (int i = 0; i < rec[0].length; i++) {
					rec[0][i] = 0.0;
				}
			}
			
			else {
				th = convertSecsToHours(t);
				dn = calculateDistance(th,d);
				rec[r][0] = t;
				rec[r][1] = dn[0];
				rec[r][2] = dn[1];
				r++;
			}
			t+=30.0;
			d = dn[1];
		}
		return rec;
	}
	
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
	
	public static void displayTable(Car car []){
		boolean atEnd = false;
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
		
		for (int i = 0; i < tableRow.size(); i++) {
			String[] strings = tableRow.get(i);
			
			for (int j = 0; j < strings.length; j++) {
				System.out.print(strings[j] + " ");
			}
			System.out.println();
			
		}
		double r[][] = calculateDefaultValues(car);
		double rp[][] = populateValues(r);
		displayInfo(rp);
		
	}
	
	
	public static void main(String[] args){
		Car A = new Car("A",0.0);
		Car B = new Car("B",60.0);
		Car C = new Car("C",120.0);
		Car allCars[] = {A,B,C};
		
		displayTable(allCars);
	}
	
	
	
}
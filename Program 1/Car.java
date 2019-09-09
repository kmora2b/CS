/*******************************************************
*@author: Kimberly Morales




History
-9/5/19: Initial Version
-9/6/19: Created class and methods, displayTable
-9/7/19: Converted table structure to array list
-9/8/19: Final version

Purpose
-



***********************************************************/

/*
A
0   0   0              
30  20   0.16 mi
60  20   0.34 mi 
90  20   0.51 mi
120  20  0.68 mi
150  20  0.84 mi
180  20   1  mi 

%%%%%210   60  3.5
210   20     1.16 mi
240   
270
300


*/
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.*;
import java.util.*;

class Car {
	//List<String[]> tableRow= new ArrayList <>();
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
	
	public static double calculateSlope(double th, double mv, double d ){
		
		if (d <= 1.3 && mv <= 20.0) {
			return 20 * th;
		}
		
		else if (d > 1.3 && d <= 2.3 && mv <= 60.0) {
			return 60 * th;
		}
		
		else if (d > 2.3 && d <= 3.3 && mv <= 30.0) {
			return 0;
		}
	}

	public static void calculateInfo(Car c[]){
		ArrayList<Integer[]> tableRow= new ArrayList <Integer[]>();
		double t = 0.0;
		double th = 0.0;
		double d = 0.0;
		double v = 0.0;
		double nums[] = new double[7];
		Arrays.fill(nums, 0); 
		
		
		
		while(d < 4.0) {
			if (t == 0.0) {
				System.out.println(Arrays.toString(nums));
				d = 4.0;
			}
			t+=30.0
			th = convertSecsToHours(t);
			
			
			
		}
	}
	
	public static void displayTable(Car car []){
		boolean atEnd = false;
		ArrayList<String[]> tableRow= new ArrayList <String[]>();
		
		
		String header [] = {"Time(s)                ", 
		"car A                ",
		"car B                ",
		"car C                "};
		
		String header2 [] = {"", "               speed    location", 
		"      speed    location", 
		"      speed    location"};
		
		tableRow.add(header);
		tableRow.add(header2);
		
		for (int i = 0; i < tableRow.size(); i++) {
			String[] strings = tableRow.get(i);
			for (int j = 0; j < strings.length; j++) {
				System.out.print(strings[j] + " ");
			}
			System.out.println();
			
		}
		
		calculateInfo(car);
		
		
		
		
	}
	
	
	public static void main(String[] args){
		Car A = new Car("A",0.0);
		Car B = new Car("B",60.0);
		Car C = new Car("C",120.0);
		Car allCars[] = {A,B,C};
		
		displayTable(allCars);
	}
	
	
	
}
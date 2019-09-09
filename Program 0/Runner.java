/*This file is for the CS 3331 course
author: Kimberly Morales

Specifications of program:
	-Input
		-Runners name
		-Top speed (m/h)
		-Acceleration(f/s/s)
	-Output
		-Print predicted location (distance from the start)
			-Of each runner
		-Every 5 secs
		
	Considerations
	-Maximum of 5 runners
	-Runners must deccelerate at one point (calculus)

*/



/*Runner class
	-Name
	-Top speed
	-Acceleration
	-Display table 
*/
import java.io.*;
import java.util.*;

class Runner {
	//Parameters
	String name;
	double topSpeed;
	double accel;
	
	//Default constructor
	Runner() {
		this.name = "";
		this.topSpeed = 0.0;
		this.accel = 0;
	}
	
	Runner(String name) {
		this.name = name;
	}
	
	Runner(String name, double topSpeed, double accel){
		this.name = name;
		this.topSpeed = topSpeed;
		this.accel = accel;
	}
	
	
	/*
	METHODS
	calculateDistance - Calculates positive displacment
	calculateNegDistance - calculcates negative displacment once graph hits 600
	displayTable - shows runner info
	displayLocationTime - shows runners times over times
	*/
	public static double calculateDistance(double a, double v, double t){
		double d = ((v * t) - ((v *v) / (2 * a)));

		if (t == 0.0) {
			return 0.0;
		}
		
		return ((v * t) - ((v *v) / (2 * a)));
	}
	
	public static void calculateNegDistance(double d, double a, double v, double t){
		double d2 = d;
		while (d > 0.0){
			d = (d2) - (2*(v * v) / (2 * a));
			d2 = d;
			if (d > 0){
				System.out.print(t + "    " + d);
				System.out.println();
			}
			t += 5.0;
		}
		
	}
	

	public static void displayTable(Runner runner[]) {
		System.out.println("Runner        Max Speed(f/s)        Acceleration(f/s)");
		for (int i = 0; i < runner.length; i++) {
			System.out.println(runner[i].name + "          " + runner[i].topSpeed + "                 " + runner[i].accel);
		}
		System.out.println();
		displayLocationTime(runner);
		
	}
	
	public static void displayLocationTime(Runner runner []){
		boolean atEnd = false;
		double t = 0.0;
		//double d [] = new double [5];
		double d = 0.0;
		double prevD = d;
		System.out.print("Time           ");

		System.out.println();
		for (int i = 0; i < runner.length; i++){
			System.out.println(" " + runner[i].name);

			while (!atEnd){
				d = calculateDistance(runner[i].accel,runner[i].topSpeed,t);
				
				if (d > 600) {
					 calculateNegDistance(d, runner[i].accel,runner[i].topSpeed,t);
					 atEnd = true;
				}

				else {
					System.out.print(t + "    " + d);
					System.out.println();
				}
				
				t += 5.0;
				
			}
			atEnd = false;
			t = 0.0;
		}
	}
	
	
	public static void main(String[] args) {
		Runner Nelly = new Runner("Nelly",30.0,8.0);
		Runner Steve = new Runner("Steve",8.8,3.0);
		Runner Usain = new Runner("Usain",41.0,11.0);
		Runner allRunners [] = {Nelly,Steve,Usain};

		displayTable(allRunners);
	}
}


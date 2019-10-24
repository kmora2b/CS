import java.io.*;
import java.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*	Constraints:
*		(!) -Handled Constraint
*		-non null driverType !
*/

class Driver {
	private String name;
	private DriverType driverType;
	private double startTime;
	private int laneNum;

	
	Driver(String name, double startTime, DriverType driverType, int laneNum) {
		this.name = name;
		this.driverType = driverType;
		this.startTime = startTime;
		this.laneNum = laneNum;
	}


	public String getName() {
		return name;
	}


	private void setName(String name) {
		this.name = name;
	}
	

	public double getStartTime() {
		return startTime;
	}

	private void setStartTime(double startTime) {
		this.startTime = startTime;
	}


	public DriverType getDriverType() {
		return driverType;
	}


	public void setDriverType(DriverType driverType) {
		this.driverType = driverType;
	}
	
	
	public int getLaneNum() {
		return laneNum;
	}


	public void setLaneNum (int laneNum) {
		this.laneNum =laneNum;
	}
	
	
	

}
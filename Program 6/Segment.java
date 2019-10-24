import java.io.*;
import java.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



class Segment {
	private int segNum;
	private double segLen;
	private double segSpeedLim;
	private int numOfLanes;
	
	Segment(int segNum, double segLen, double segSpeedLim, int numOfLanes) {
		this.segNum = segNum;
		this.segLen = segLen;
		this.segSpeedLim = segSpeedLim;
		this.numOfLanes = numOfLanes;
	}
	

	public int getSegNum() {
		return segNum;
	}

	public void setSegNum(int segNum) {
		this.segNum = segNum;
	}

	public double getSegLen() {
		return segLen;
	}
	
	public int getNumOfLanes() {
		return numOfLanes;
	}

	public void setSegLen(double segLen) {
		this.segLen = segLen;
	}

	public double getSegSpeedLim() {
		return segSpeedLim;
	}

	public void setSegSpeedLim(double segSpeedLim) {
		this.segSpeedLim = segSpeedLim;
	}
	
	public void setNumOfLanes(int numOfLanes) {
		this.numOfLanes = numOfLanes;
	}

}
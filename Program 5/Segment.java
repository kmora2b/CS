import java.io.*;
import java.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;



class Segment {
	private double segNum;
	private double segLen;
	private double segSpeedLim;
	
	Segment(double segNum, double segLen, double segSpeedLim) {
		this.segNum = segNum;
		this.segLen = segLen;
		this.segSpeedLim = segSpeedLim;
	}
	

	public double getSegNum() {
		return segNum;
	}

	public void setSegNum(double segNum) {
		this.segNum = segNum;
	}

	public double getSegLen() {
		return segLen;
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

}
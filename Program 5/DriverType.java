import java.io.*;
import java.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*	Constraints:
*		(!) -Handled Constraint
*
*		+speedLim is non negative floatdriverTypeName
*		+followTime is in seconds and non negative 
*		+maxAccel feet/second/second
*/

class DriverType {
	private String driverTypeName;
	private double followTime;
	private double driverTypeSpeedLim;
	private double maxAccel;
	
	DriverType(String driverTypeName, double followTime, double driverTypeSpeedLim,  double maxAccel) {
		this.driverTypeName = driverTypeName;
		this.followTime = followTime;
		this.driverTypeSpeedLim = driverTypeSpeedLim;
		this.maxAccel = maxAccel;
	}

	public void setDriverTypeName(String driverTypeName) {
		this.driverTypeName = driverTypeName;
	}
	
	public void setFollowTime(double followTime) {
		this.followTime = followTime;
	}
	
	public void setDriverTypeSpeedLim(double driverTypeSpeedLim) {
		this.driverTypeSpeedLim = driverTypeSpeedLim;
	}
	
	public void setMaxAccel(double maxAccel) {
		this.maxAccel = maxAccel;
	}
	
	public String getDriverTypeName() {
		return driverTypeName;
	}
	
	public double getFollowTime() {
		return followTime;
	}


	public double getDriverTypeSpeedLim() {
		return driverTypeSpeedLim;
	}

	public double getMaxAccel() {
		return maxAccel;
	}


}
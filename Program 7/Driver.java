/*	Constraints:
*		(!) -Handled Constraint
*		-non null driverType !
*/

class Driver {
	private String name;
	private DriverType driverType;
	private double startTime;
	
	Driver(String name, double startTime, DriverType driverType) {
		this.name = name;
		this.driverType = driverType;
		this.startTime = startTime;
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
	

}
class Segment {
	private int segNum;
	private double segLen;
	private double segSpeedLim;
	
	Segment(int segNum, double segLen, double segSpeedLim) {
		this.segNum = segNum;
		this.segLen = segLen;
		this.segSpeedLim = segSpeedLim;
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
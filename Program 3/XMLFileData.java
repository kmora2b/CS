import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
/*Handles all info read in from XML file*/

class XMLFileData {

	private Document doc = null;
	private ArrayList<String> segNumsL = new ArrayList<String>();
	private ArrayList<String> segLensL = new ArrayList<String>();
	private ArrayList<String> speedLimsL = new ArrayList<String>();
	
	XMLFileData(){
		this.doc = null;
		this.segNumsL = segNumsL;
		this.segLensL = segLensL;
		this.speedLimsL = speedLimsL;
	}
	
	XMLFileData(ArrayList<String> segNumsL, ArrayList<String> segLensL, ArrayList<String> speedLimsL){
		this.segNumsL = segNumsL;
		this.segLensL = segLensL;
		this.speedLimsL = speedLimsL;
	}
	
	public void setSegNumList(ArrayList<String> segNumsL){
		this.segNumsL = segNumsL;
	}
	
	public void setSegLenList(ArrayList<String> segLensL){
		this.segLensL = segLensL;
	}
	
	public void setSpeedLimList(ArrayList<String> speedLimsL){
		this.speedLimsL = speedLimsL;
	}
	
	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public ArrayList<String> getSegNumList(){
		return segNumsL;
	}
	
	public ArrayList<String> getSegLenList(){
		return segLensL;
	}
	
	public ArrayList<String> getSpeedLimList(){
		return speedLimsL;
	}
	
	public Document getDoc() {
		return doc;
	}
}
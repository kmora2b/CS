import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Collections;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.CyclicBarrier;

import java.io.*;
import java.util.*;
// implements ActionListener, ItemListener

public class TrafficController {
	private XMLFileData data;
	Thread t;
	
	private TrafficModel model;
	private TrafficGUIView view;
	
	TrafficController(TrafficModel model, TrafficGUIView view){
		this.model = model;
		this.view = view;
		initView();
	}
	
	public void initView(){
		MenuListener al = new MenuListener();
		ButtonListener bl = new ButtonListener();
		MenuListener il = new MenuListener();
		
		view.menuItemOpen.addActionListener(al);
		view.menuItemExit.addActionListener(al);
		
		view.startButton.addActionListener(bl);
		view.pauseButton.addActionListener(bl);
		view.contButton.addActionListener(bl);
		view.stopButton.addActionListener(bl);
	}
	

	
	class MenuListener implements ActionListener, ItemListener {
		MenuListener(){}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("actionPerformed " + e.getActionCommand());
			
			if (e.getActionCommand().equalsIgnoreCase("Exit")) {
				System.exit(0);
			}
			
			if (e.getActionCommand().equalsIgnoreCase("Open")) {
				System.out.println("Do the open work here");
				data = new XMLFileData(fileChooser());
				data.readFile();
				
				if (data.getList() == null){
					JOptionPane.showMessageDialog(view.frame, "No valid XML file has been detected, please upload a xml file.");
				}
				
				else {
					storeCourseSimInfoFromXML(data.getList());
					model.genCourseInfo();
					model.sort2D(model.getCourseData(),0);
					System.out.println("Sorted Model Data: " + Arrays.deepToString(model.getCourseData()));
					model.setTotalDistance(model.sumLen(model.getCourseData()));
					view.initShapes(model.getAllDrivers().size(),model.getTotalDistance(),model.getCourseData());
					
					model.calculateDistanceValuesForDrivers();
					view.frame.revalidate(); 
					view.frame.repaint();			
					//runTest();
					
				}
				
			}
		}
		
			
		@Override
		public void itemStateChanged(ItemEvent e) {
			System.out.println("itemStateChanged " + e);
		}
		
    }

	class ButtonListener implements ActionListener{
	
		ButtonListener(){}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getActionCommand().equalsIgnoreCase("Start")) {
				runTest();
				//animateOrbit(0);
				view.startButton.setEnabled(false);
				view.pauseButton.setEnabled(true);
				view.contButton.setEnabled(true);
				view.stopButton.setEnabled(true);
				
			}
			
			else if (e.getActionCommand().equalsIgnoreCase("Pause")) {
				view.startButton.setEnabled(false);
				view.pauseButton.setEnabled(true);
				view.contButton.setEnabled(true);
				view.stopButton.setEnabled(true);
			
			}
			
			else if (e.getActionCommand().equalsIgnoreCase("Continue")) {
				view.startButton.setEnabled(false);
				view.pauseButton.setEnabled(true);
				view.contButton.setEnabled(true);
				view.stopButton.setEnabled(true);
			}
			
			else if (e.getActionCommand().equalsIgnoreCase("Stop")) {
				view.startButton.setEnabled(true);
				view.pauseButton.setEnabled(false);
				view.contButton.setEnabled(false);
				view.stopButton.setEnabled(false);
				//System.exit(0);
			}
		}
	}
	
	public String fileChooser() {
		view.showOpenBox();
		File file = view.jfc.getSelectedFile();
		String filename = file.getAbsolutePath();
		System.out.println(filename);
		return filename;
	}
	
	public void animateOrbit(int vehicleNum) {
		double angle = 0; 
		double offSet = 10;
		int i = 0;
		int v = vehicleNum;
		if (v == 0) v +=1;
		/*Based on the unit circle, 2 * pi is one lap around the track */
		for (double t = 0; t < (2 * model.numOfLaps* Math.PI) && t <  model.record.size() ; t+=0.01) {
			/*view.vehicleCoords[vehicleNum][0] = (view.trackROut/2 - view.vehicleR/2) * Math.cos(angle) + view.winSizeX/2 - view.vehicleR/2 + Math.cos(model.record.get(i).get(v+(v-1)));
			
			view.vehicleCoords[vehicleNum][1] = (view.trackROut/2 - view.vehicleR/2) * Math.sin(angle) + view.winSizeY/2 - view.vehicleR/2 + Math.sin(model.record.get(i).get(v + (v-1)));
			*/
			
			view.vehicleCoords[vehicleNum][0] = (view.trackRX/2 + view.vehicleR/2) * Math.cos(angle) + view.winSizeX/2 - view.vehicleR/2 + Math.cos(model.record.get(i).get(v+(v-1)));
			
			view.vehicleCoords[vehicleNum][1] = (view.trackRY/2 + view.vehicleR/2) * Math.sin(angle) + view.winSizeY/2 - view.vehicleR/2 + Math.sin(model.record.get(i).get(v + (v-1)));
			
			
			angle+=0.01;
			i++;
			view.frame.revalidate(); 
			view.frame.repaint();
			
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} 
			
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void runTest(){
		
		Runnable t1 = new Runnable() {
			public void run() {
				animateOrbit(0);
			}
		};

		Runnable t2 = new Runnable() {
			public void run() {
				animateOrbit(1);

			}
		};
		
		Runnable t3 = new Runnable() {
			public void run() {
				animateOrbit(2);

			}
		};
		new Thread(t1).start();
		new Thread(t2).start();
		new Thread(t3).start();
	}
	
	
	
	public void initArrayLists(){
		model.setSegNumList(new ArrayList<String>());
		model.setSegLenList(new ArrayList<String>());
		model.setSegSpeedLimList(new ArrayList<String>());
		model.setNumOfLanesList(new ArrayList<String>());
		model.setSegmentsList(new ArrayList<Segment>());
		model.setAllDrivers(new ArrayList<Driver>());
		model.setAllDriverTypes(new ArrayList<DriverType>());
	}
	
	//Gather all data from XML file
	public void storeCourseSimInfoFromXML(NodeList nL){
		initArrayLists();
		
		try {
			for (int i = 0; i < nL.getLength(); i++) {
				if (nL.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) nL.item(i);
					System.out.println(el.getNodeName());
					
					//Get Segment info and store into arraylist
					if (el.getNodeName().equalsIgnoreCase("SEGMENT")) {
						storeSegmentInfo(el);
					}
					
					else if (el.getNodeName().equalsIgnoreCase("DRIVER_TYPE")) {
						storeDriverTypeInfo(el);
					}
					
				}
			}
			
			storeDriverInfo(nL);
			
		}
		
		catch (NumberFormatException nfe) {
			System.out.println("Invalid XML File has invalid datatype");
			System.exit(0);
		}
		
		catch (NullPointerException npe) {
			System.out.println("Missing attribute in XML tag");
			System.exit(0);
		}
		
		catch (IllegalArgumentException iae) {
			System.out.println("Driver Type info is invalid with negative values in followTime or speed limit");
			System.exit(0);
		}
		catch (Exception e) {
			System.out.println("Invalid XML File or error in reading in file");
			System.exit(0);
		}

	}
	
	public void storeSegmentInfo(Element el){
		String segNum = el.getElementsByTagName("SEGMENT_NUMBER").item(0).getTextContent().replaceAll("\\s+", "");
		String segLen = el.getElementsByTagName("LENGTH").item(0).getTextContent().replaceAll("\\s+", "");
		String segSpeedLim = el.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent().replaceAll("\\s+", "");
		String segLane = el.getElementsByTagName("LANES").item(0).getTextContent().replaceAll("\\s+", "");

		model.getSegNumList().add(segNum);
		model.getSegLenList().add(segLen);
		model.getSegSpeedLimList().add(segSpeedLim);
		model.getNumOfLanesList().add(segLane);

		Segment segment = new Segment(Integer.valueOf(segNum), Double.valueOf(segLen), Double.valueOf(segSpeedLim), Integer.valueOf(segLane));
	
		model.getSegmentsList().add(segment);

	}
	
	
	public void storeDriverInfo(NodeList nL){
		double insertTimeDelay = 0;	   //Counter of time delay
		int laneCount = 1;	  //Counter for alternating lane
		int numOfLanes = Integer.valueOf(model.getNumOfLanesList().get(0));   //The total number of lanes
		
		//Since driver type is read in, add in driver type info to driver objects
		for (int i = 0; i < nL.getLength(); i++) {
			if (nL.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) nL.item(i);

				if (el.getNodeName().equalsIgnoreCase("DRIVER")) {
					String driverName = el.getElementsByTagName("NAME").item(0).getTextContent().replaceAll("\\s+", "");
					String driverDriverType = el.getElementsByTagName("DRIVER_TYPE").item(0).getTextContent().replaceAll("\\s+", "");


					for (int j = 0; j < model.getAllDriverTypes().size(); j++){
						
						if(driverDriverType.equalsIgnoreCase(model.getAllDriverTypes().get(j).getDriverTypeName())){
							if (laneCount > numOfLanes) {
									laneCount = 1;
							}
							
							Driver driver = new Driver(driverName, insertTimeDelay,model.getAllDriverTypes().get(j),laneCount);
							model.getAllDrivers().add(driver);
							insertTimeDelay+=model.timeDelay;
							laneCount++;
						}
					}

				}

			}
		}
		
	}
	
	//Stores XML files driverType info into arraylist and model
	public void storeDriverTypeInfo(Element el){
		String driverTypeName = el.getElementsByTagName("TYPE_NAME").item(0).getTextContent().replaceAll("\\s+", "");
		String driverFollowTime = el.getElementsByTagName("FOLLOW_TIME").item(0).getTextContent().replaceAll("\\s+", "");
		String driverTypeSpeedLim = el.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent().replaceAll("\\s+", "");
		String driverTypeMaxAccel = el.getElementsByTagName("MAX_ACCELERATION").item(0).getTextContent().replaceAll("\\s+", "");
		
		//DEBUG
		System.out.println(driverTypeName + " " + driverFollowTime + " "+ driverTypeSpeedLim + " " +  driverTypeMaxAccel);
			
		//Make sure time or speed limit is negative
		if (Double.parseDouble(driverFollowTime) < 0 ||  Double.parseDouble(driverTypeSpeedLim) < 0) {
			throw new IllegalArgumentException("Driver Type info is invalid with negative values in followTime or speed limit");

		}

		DriverType driverType = new DriverType(driverTypeName, Double.parseDouble(driverFollowTime), Double.parseDouble(driverTypeSpeedLim), Double.parseDouble(driverTypeMaxAccel) );
		model.getAllDriverTypes().add(driverType);
	}
	

	
}
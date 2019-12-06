import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Arc2D;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrafficGUIView extends JFrame implements Runnable {
	public static final long serialVersionUID = 24362462L;

	public JFrame frame = new JFrame ("Course Simulator");
	public JPanel buttonPanel = new JPanel(new FlowLayout());
	public paintPanel trackPanel = new paintPanel();
	
	public JMenu menu = new JMenu ("File");
	public JMenuItem menuItemOpen = new JMenuItem ("Open");
	public JMenuItem menuItemExit = new JMenuItem ("Exit");
	
	public JFileChooser jfc= new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	
	public JButton startButton = new JButton("Start");
	public JButton pauseButton = new JButton("Pause");
	public JButton contButton = new JButton("Continue");
	public JButton stopButton = new JButton("Stop");
	
	private Graphics g;
	private Graphics2D shapeAttr;
	
	public double[][] vehicleCoords;
	public Shape[] vehicleShapes;
	public Arc2D[] segmentArcs;
	public Shape[] laneArcs;
	
	
	private int numOfVehicles,numOfSegs; 
	private double totalSegLen;
	public double[][] courseData;
	
	public double trackRX = 400, trackRY = 400;
	public double trackROutWidth = 250;
	public double trackOutX, trackOutY;
	public double trackROut = trackRX + (trackROutWidth);
	public double trackX, trackY;
	public double vehicleR;
	public double winSizeX, winSizeY;
	public  boolean canDrawVehiclesAndSegments = false;
	private boolean running = true;
	
	TrafficPushClient client;

	TrafficGUIView() {
		TrafficGUIWindow();
	}
	
	TrafficGUIView (TrafficPushClient  client) {
		this.client = client;
	}

	
	/*
	*
	*
	*
	*/
	@Override
	public void run() {
		while (true) {
			try {
				if (running) frame.repaint();
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
	
	public void initShapes(int numOfVehicles, double totalSegLen,double[][] courseData){
		vehicleCoords = new double[numOfVehicles][2];
		vehicleShapes = new Ellipse2D[numOfVehicles];
		segmentArcs = new Arc2D[courseData.length];
		laneArcs = new Arc2D[courseData.length];
		vehicleR = (trackROutWidth / numOfVehicles)/4;
		
		//colors = createColorList();
		//vehicleShapesL = createShapeList();
		//initVehicleCoords();
		this.numOfVehicles = numOfVehicles;
		this.courseData = courseData;
		this.totalSegLen = totalSegLen;
		canDrawVehiclesAndSegments = true;
	}
	
	public void initVehicleCoords(){
		double offset = vehicleR;
		for(int i = 0; i < vehicleCoords.length; i++){
			for(int j = 0; j < vehicleCoords.length; j++ ){
				if (j == 0)
					vehicleCoords[i][j] = trackRX;
				else if (j == 1)
					vehicleCoords[i][j] = trackRY;
			}
			offset+=vehicleR;
		}
	}
	
    public List<Color> createColorList() {
        List<Color> colorList = new ArrayList<>();
		colorList.add(Color.RED);
        colorList.add(Color.BLUE);
        colorList.add(Color.GREEN);
        colorList.add(Color.ORANGE);
        colorList.add(Color.MAGENTA);
        colorList.add(Color.CYAN);
        colorList.add(Color.PINK);
        return colorList;
    }

	
	/**
	*
	*
	*
	*/
    class paintPanel extends JPanel {
        paintPanel() {}

        @Override
		public void paintComponent(Graphics g) {  
			super.paintComponent(g);
			shapeAttr = (Graphics2D) g.create();
			trackX =  getWidth()/2 - trackRX/2;
			trackY = getHeight()/2 - trackRY/2;
			
			trackOutX = getWidth()/2 - trackROut/2;
			trackOutY = getHeight()/2 - trackROut/2;
			
			winSizeX =  getWidth();
			winSizeY = getHeight();
	
			drawTrack(shapeAttr);
			
			if(canDrawVehiclesAndSegments) {
				drawSegments(shapeAttr);
				drawLanes(shapeAttr);
				drawVehicles(shapeAttr);
			}

			shapeAttr.dispose();
		}
    }

	
	/*
	*
	*
	*
	*/
	public void drawTrack(Graphics2D shapeAttr) {
		Shape trackCement = new Ellipse2D.Double(trackOutX, trackOutY, trackROut,trackROut);
		Shape track = new Ellipse2D.Double(trackX, trackY, trackRX, trackRY);
	
		shapeAttr.setColor(Color.GRAY);
		shapeAttr.fill(trackCement);
		
		shapeAttr.setColor(new Color(21,94,21)); //RGB value of dark green
		shapeAttr.fill(track);
	}
	
	/*
	*
	*
	*
	*/
	public void drawSegments(Graphics2D shapeAttr) {
		int red = 10;
		int green = 20;
		int blue = 30;
		
		double angleStart = 0;
		double arcLength;
		double radius = trackRX;
		double angleEnd;
		double angleExtent;
	
		//To allocate number of miles 
		double circumference = 2 * radius * Math.PI;
		double milesToPixels =  circumference / totalSegLen;

		for(int i =0; i < segmentArcs.length; i++) {
			
			arcLength = milesToPixels * courseData[i][1];
			angleExtent = calculateArcLengthAngle(arcLength, radius);
			angleEnd = (angleStart - angleExtent);
			
			Arc2D.Double segment = new Arc2D.Double(Arc2D.OPEN); 
			
			segment.setArcByCenter(winSizeX/2 , winSizeY/2, trackROut/2, angleStart,-angleExtent,Arc2D.OPEN);
			
			segmentArcs[i] = segment;
			
			shapeAttr.setStroke(new BasicStroke(10.0f));
			shapeAttr.setColor(new Color(red,green,blue));
			shapeAttr.draw(segment);

			red+=50;
			angleStart = angleEnd;
		}
	
	}
	
	public void drawLanes(Graphics2D shapeAttr){
		double offSet = 0;
		double offSetStep = 0;
		
		for(int i = 0; i < laneArcs.length; i++){
			for(int j =0; j < courseData[i][3]; j++){
				offSetStep = (trackROutWidth / courseData[i][3])/2;
				Arc2D.Double laneLine = new Arc2D.Double(Arc2D.OPEN); 
		
				laneLine.setArcByCenter(winSizeX/2, winSizeY/2, trackRX/2, segmentArcs[i].getAngleStart(), segmentArcs[i].getAngleExtent(),Arc2D.OPEN);
				
				shapeAttr.setStroke(new BasicStroke(2.0f));
				shapeAttr.setColor(new Color(0,0,0));
				shapeAttr.draw(laneLine);
				offSet+=  offSetStep;
			}
			offSet = 0;
			//offSetStep = 0;
		}
	}
	

	
		/*
	*
	*
	*
	*/
	public void drawVehicles(Graphics2D shapeAttr) {
		int c = 0;
		Color colorChoice [] = {Color.RED, Color.GREEN, Color.BLUE, Color.PINK};

		double offSet = 0;
		for(int i =0; i < vehicleShapes.length; i++){
			if (c > colorChoice.length)
				c = 0;
			
			Shape vehicle = new Ellipse2D.Double(vehicleCoords[i][0],vehicleCoords[i][1], vehicleR, vehicleR);
			vehicleShapes[i] = vehicle;
			shapeAttr.setColor(colorChoice[c]);
			shapeAttr.fill(vehicleShapes[i]);
			offSet+=vehicleR;
			c++;
		}
	}
	

	
	/*
	*
	*
	*
	*/
	public void showOpenBox(){
		jfc.setDialogTitle("Select a File");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filterXML = new FileNameExtensionFilter("XML", "xml");
		FileNameExtensionFilter filterTXT = new FileNameExtensionFilter("TXT", "txt");
		jfc.addChoosableFileFilter(filterXML);
		jfc.addChoosableFileFilter(filterTXT);
		jfc.showOpenDialog(null);
		
	}
	
	/*
	*
	*
	*
	*/
	public double calculateArcLengthAngle(double arcLength, double radius){
		return (360 * arcLength) / (2 * Math.PI * radius);
	}
	
	/*
	*
	*
	*
	*/
	public void TrafficGUIWindow() {
		TrafficGUIMenu();
		TrafficGUIButtons();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame.add(buttonPanel,BorderLayout.PAGE_START);
		frame.add(trackPanel, BorderLayout.CENTER);
		frame.setPreferredSize(screenSize);

		// put window in center of screen
		frame.setLocationRelativeTo(null);
		
		// Show frame
		frame.pack();
		frame.setVisible(true);

	 }
	 
	public void TrafficGUIMenu(){
		JMenuBar menuBar = new JMenuBar();
		menu.add(menuItemOpen);
		menu.add(menuItemExit);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
	}
	
	public void TrafficGUIButtons(){
		buttonPanel.add(startButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(contButton);
		buttonPanel.add(stopButton);
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		pauseButton.setEnabled(false);
		contButton.setEnabled(false);
	}
	
	/*public static void main(String[]args){
		TrafficGUIView v = new TrafficGUIView();
	}*/
	
}









import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Arc2D;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.util.ArrayList;


public class TrafficGUIView extends JPanel {
	public static final long serialVersionUID = 24362462L;

	public JFrame frame = new JFrame ("Course Simulator");
	public JMenu menu = new JMenu ("File");
	public JMenuItem menuItemOpen = new JMenuItem ("Open");
	public JMenuItem menuItemExit = new JMenuItem ("Exit");
	
	public JFileChooser jfc= new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	
	public  double vehicleR = 50;
	private Graphics g;
	private Graphics2D shapeAttr;
	
	public double[][] vehicleCoords;
	public Shape[] vehicleShapes;
	public Shape[] segmentArcs;

	private int numOfVehicles,numOfSegs; 
	private ArrayList<Double> segLens;
	private double totalSegLen;
	
	public double trackRX = 400, trackRY = 400;
	public double trackOutX, trackOutY;
	public double trackROut = trackRX + (2 * vehicleR);
	public double trackX, trackY;
	public double winSizeX, winSizeY;
	public  boolean canDrawVehiclesAndSegments = false;


	TrafficGUIView() {
		TrafficGUIWindow();
	}
	

	public void initShapes(int numOfVehicles,int numOfSegs, ArrayList<Double> segLens, double totalSegLen){
		vehicleCoords = new double[numOfVehicles][2];
		vehicleShapes = new Ellipse2D[numOfVehicles];
		segmentArcs = new Arc2D[numOfSegs];
		this.numOfVehicles = numOfVehicles;
		this.numOfSegs = numOfSegs;
		this.segLens = segLens;
		this.totalSegLen = totalSegLen;
		canDrawVehiclesAndSegments = true;
	}
	
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
			drawVehicles(shapeAttr);
		}

		shapeAttr.dispose();
    }
	
	
	public void createVehicleArray() {
		double offSet = 0;
		for(int i =0; i < vehicleShapes.length; i++){
			Shape vehicle = new Ellipse2D.Double(vehicleCoords[i][0] + offSet,vehicleCoords[i][1], vehicleR, vehicleR);
			vehicleShapes[i] = vehicle;
			offSet+=10;
		}
	}
	
	
	public void drawTrack(Graphics2D shapeAttr) {
		Shape trackCement = new Ellipse2D.Double(trackOutX, trackOutY, trackROut,trackROut);
		Shape track = new Ellipse2D.Double(trackX, trackY, trackRX, trackRY);
	
		shapeAttr.setColor(Color.GRAY);
		shapeAttr.fill(trackCement);
		
		shapeAttr.setColor(new Color(21,94,21)); //RGB value of dark green
		shapeAttr.fill(track);
	}
	
	public void drawSegments(Graphics2D shapeAttr) {
		int red = 10;
		int green = 20;
		int blue = 30;
		
		double angleStart = 90;
		double arcLength;
		double radius = trackRX;
		double angleEnd;
		double angleExtent;
	
		//To allocate number of miles 
		double circumference = 2 * radius * Math.PI;
		double milesToPixels =  circumference / totalSegLen;

	
		for(int i =0; i < segmentArcs.length; i++) {
			
			arcLength = milesToPixels * segLens.get(i);
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
	
	public void drawVehicles(Graphics2D shapeAttr) {
		int c = 0;
		Color colorChoice [] = {Color.RED, Color.GREEN, Color.BLUE, Color.PINK};

		double offSet = 0;
		for(int i =0; i < vehicleShapes.length; i++){
			if (c > colorChoice.length)
				c = 0;
			
			Shape vehicle = new Ellipse2D.Double(vehicleCoords[i][0] + offSet,vehicleCoords[i][1], vehicleR, vehicleR);
			vehicleShapes[i] = vehicle;
			shapeAttr.setColor(colorChoice[c]);
			shapeAttr.fill(vehicleShapes[i]);
			offSet+=10;
			c++;
		}
	}
	
	public void showOpenBox(){
		jfc.setDialogTitle("Select a File");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filterXML = new FileNameExtensionFilter("XML", "xml");
		FileNameExtensionFilter filterTXT = new FileNameExtensionFilter("TXT", "txt");
		jfc.addChoosableFileFilter(filterXML);
		jfc.addChoosableFileFilter(filterTXT);
		jfc.showOpenDialog(null);
		
	}
	
	public double calculateArcLengthAngle(double arcLength, double radius){
		return (360 * arcLength) / (2 * Math.PI * radius);
	}
	
	public void TrafficGUIWindow() {
		JMenuBar menuBar = new JMenuBar();

		menu.add(menuItemOpen);
		menu.add(menuItemExit);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.add(this);
		frame.setPreferredSize(screenSize);

		// put window in center of screen
		frame.setLocationRelativeTo(null);
		
		// Show frame
		frame.pack();
		frame.setVisible(true);

	 }
	
}









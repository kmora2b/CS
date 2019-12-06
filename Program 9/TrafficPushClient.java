import java.io.*;
import java.net.*;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class TrafficPushClient implements Runnable {
 private TrafficController controller;
 //public TrafficModel model;
 private TrafficModel model;
 private TrafficGUIView view;
 
 private Socket socket;
 private InputStreamReader streamReader;
 private BufferedReader clientReader;
 private PrintWriter clientWriter;
 private boolean initialized = false;

 //-----------------------------------------------------------------
 // Constructor
 //-----------------------------------------------------------------
 public TrafficPushClient (TrafficModel model) {
 	this.model = model;
 }

 //-----------------------------------------------------------------
 // Prevent reads and writes to socket until initialized
 //-----------------------------------------------------------------
 public boolean isInitialized() {
 return initialized;
 }

 //-----------------------------------------------------------------
 // open the socket
 //-----------------------------------------------------------------
 public void initSocket() {
	 System.out.println ("TrafficPushClient setting up sockets");
	 try {
		 socket = new Socket ("127.0.0.1", 8888);
		 streamReader = new InputStreamReader (socket.getInputStream());
		 clientReader = new BufferedReader (streamReader);
		 clientWriter = new PrintWriter (socket.getOutputStream());


	 } 
	 
	 catch (UnknownHostException e) {
		 System.out.println("UnknownHostException");
		 e.printStackTrace();
	 } 
	 
	 catch (IOException e) {
		 System.out.println("IOException");
		 e.printStackTrace();
	 }
	 initialized = true;
	 System.out.println ("TrafficPushClient done setting up");
 }

 //-----------------------------------------------------------------
 // read from the server stream until we have received an entire
 // xml package with the <angle> item.
 // There are other ways to do this ....
 //-----------------------------------------------------------------
 private String getXMLPackage() throws Exception {
	 return controller.data.writeXmlDocumentToXmlFile();
 }

 //-----------------------------------------------------------------
 // Wait for an xml package on the server input stream. Read it.
 // update the angleData object with new data.
 //-----------------------------------------------------------------
 public void readSocket() throws Exception {
	 try {
		 String xmlS = getXMLPackage();
		 //controller.data.writeXmlDocumentToXmlFile();
		/*controller.storeCourseSimInfoFromXML(controller.data.getList());
		controller.model.genCourseInfo();
		controller.model.sort2D(model.getCourseData(),0);
		System.out.println("Sorted Model Data: " + Arrays.deepToString(controller.model.getCourseData()));
	    controller.model.setTotalDistance(model.sumLen(model.getCourseData()));*/
	 }


	 catch (IOException ex) {
		 System.out.println ("IO Error");
		 ex.printStackTrace ();
	 }
 }

 //-----------------------------------------------------------------
 // Send a string to the Server
 //-----------------------------------------------------------------
 public void writeSocket(String command) {
	 if (initialized) {
		 clientWriter.println (command);
		 clientWriter.flush();
	 }
 }

//-----------------------------------------------------------------
// run the thread that just listens to the server stream and
 // processes updates
 //-----------------------------------------------------------------
 @Override
 public void run() {
	 initSocket();
	 while (true) {
		//readSocket();
	 }
 }

 //-----------------------------------------------------------------
 //-----------------------------------------------------------------
 public static void main (String [] args) {
	System.out.println ("Starting TrafficClient");
	 // create an instance of Angle to represent data
	 TrafficModel model = new TrafficModel();
	 TrafficGUIView view = new TrafficGUIView();
	 TrafficController controller= new TrafficController(model,view);
	 
 }




}	
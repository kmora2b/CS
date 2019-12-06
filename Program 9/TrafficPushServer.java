import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// When requested, push the Angle object values to the client
public class TrafficPushServer implements Runnable{
	private boolean OK;
	private ServerSocket s;

	private Socket sock;
	private InputStreamReader streamReader;
	private BufferedReader reader;
	private PrintWriter writer;
	private TrafficController controller;
	private TrafficModel model;

	public TrafficPushServer (TrafficModel model,TrafficController controller) {
 		this.controller = controller;
 		this.model = model;
	}

	
	public void initSocket() {
		System.out.println ("TrafficPushServer initSocket()");
		try {
			s = new ServerSocket (8888);
			//s = new Socket ("127.0.0.1", 5000);
			//ServerSocket server = new ServerSocket(60010);
		
			System.out.println ("TrafficPushServer accepting ..."); 
			sock = s.accept();
			System.out.println ("TrafficPushServer accepted connection ");
			streamReader = new InputStreamReader (sock.getInputStream());
			reader = new BufferedReader (streamReader);
			writer = new PrintWriter (sock.getOutputStream());

		} 
	 
		catch (IOException ex) {
			System.out.println ("Server IO Error");
			ex.printStackTrace ();
		}
		
		System.out.println("TrafficPushServer init complete");
		OK = true;
	}
	
// ----------------------------------------------
 // read the input socket. Process requests from client
 // ----------------------------------------------
 public void goReader() {
	 System.out.println ("TrafficPushServer goReader()");
	 try {
		 while (OK) {

			 String q = reader.readLine();

			 processClientRequest (q);
		 }
		 System.out.println ("TrafficPushServerrReader Exit");
		 writer.close();

	 } 
	 
	 catch (IOException ex) {
		 System.out.println ("TrafficPushServerReader IO Error");
		 ex.printStackTrace ();
	 }

 }
 
 // ----------------------------------------------
 // process requests from client. Valid requests are
 // exit, send, toggle
 // ----------------------------------------------
 private void processClientRequest(String clientRequest) {

	 if ("exit".equalsIgnoreCase(clientRequest)) {
		OK = false;

	 } 
	 
	 else if ("send".equalsIgnoreCase(clientRequest)) {
		//String s = model.jaxbObjectToXML();
		String s = "A";
		goWriter(s);

	 } 
	 
	 else if ("toggle".equalsIgnoreCase(clientRequest)) {
		//controller.toggleRunning();
	 }
 }

// ----------------------------------------------
 // write output on the server socket
 //----------------------------------------------

	 public void goWriter(String serverOutput) {
		 writer.println (serverOutput);
		 writer.flush();
	 }

 // ----------------------------------------------
 // run the thread: initialize the socket and start
 // reading from the socket input stream
 // ----------------------------------------------
	 
	 @Override
	 public void run() {
		 initSocket();
		 goReader();
	 }
 
 	public static void main (String[] args){
		TrafficModel model = new TrafficModel();
		Runnable controller = new TrafficController (model);
		
		Thread clockThread = new Thread(controller);
		clockThread.start();

		System.out.println ("Starting ClockPushServer");

		TrafficPushServer trafficServer = new TrafficPushServer(model, (TrafficController)controller);
		Thread serverThread = new Thread(trafficServer);
		serverThread.start();

	}
	
	
	
	
}

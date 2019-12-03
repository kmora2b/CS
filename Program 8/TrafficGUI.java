class TrafficGUI{
	
	public static void main (String[] args) {
		TrafficModel model = new TrafficModel();
		TrafficGUIView view = new TrafficGUIView();
		TrafficController control = new TrafficController(model,view);
		
	}
	
	
}
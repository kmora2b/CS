import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Arc2D; 

class DriverShape extends Ellipse2D.Double {
	Graphics2D shapeAttr;
	private double vehicleR;
	private double startTime;
	double x,y,w,h;
	boolean draw = false;
	boolean orbit = false;
	public Color color;
	
	DriverShape (double x, double y, double w, double h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void drawShape(Graphics2D shapeAttr) {
		if (draw) {
			//g.setColor(color);
			//g.fillOval(randXLoc, y, 30, 30);
			shapeAttr.setColor(color);
			shapeAttr.fill( new Ellipse2D.Double(x,y, w, h));
		}
	}

	public void move() {
		if (draw) {
			if (y <= 50) {
				orbit = true;
			}

			if (orbit) {
				y += 5;
			} 
			
			else {
				y -= 5;
			}
		}
	}

	public void decreaseDelay() {
		if (startTime <= 0) {
			draw = true;
		} 
		
		else {
			startTime -= 1;
		}
	}












}
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;

public class App extends JFrame{

	Ordonnanceur o;

	public App(){
	    setTitle("Orc");
	    setSize(400, 400);
	    setVisible(true);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	@Override
	public void paint(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;
	    //TODO On dessine tout
		//o.draw(g2d);
	}
	
	public static void main(String[] args) {
	
	   new App();
	
	}
}
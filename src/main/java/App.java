import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;

public class App extends JFrame{
	private int tX;
	private int tY;

	private Ordonnanceur o;

	private void initApp() {
		o = new Ordonnanceur(300, 300, 10);
		tX = 50;
		tY = 50;
	}

	public App(){
		initApp();

	    setTitle("Orc");
	    setSize(400, 400);
	    setVisible(true);
	    setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	@Override
	public void paint(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;
	    g2d.clearRect(0, 0, getWidth(), getHeight());
	    g2d.translate(tX, tY);
		o.draw(g2d);
		g2d.translate(-tX, -tY);
	}

	public void update() {
		o.update();
		repaint();
	}

	public static void main(String[] args) throws InterruptedException {

		App a = new App();

		while (true) {
			a.update();
			Thread.sleep(32);
		}
	}
}
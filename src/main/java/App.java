import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;

public class App extends JFrame{
	private int tX;
	private int tY;

	private Ordonnanceur o;

	private void initApp(int width, int height) {
		o = new Ordonnanceur(width-200, height-200, 10);
		tX = 100;
		tY = 100;
	}

	public App(int width, int height){
		initApp(width, height);

	    setTitle("Orc");
	    setSize(width, height);
	    setVisible(true);
	    setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    this.addKeyListener(KeyManager.getInstance());
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

		App a = new App(600, 600);

		while (true) {
			a.update();
			Thread.sleep(16);
		}
	}
}
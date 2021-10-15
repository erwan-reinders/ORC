import java.awt.*;
import static java.lang.Math.*;
import java.util.ArrayList;

public class Orc extends Agent implements Drawable{
    /*Classe abstraite symbolisant un Orc*/
    //Idée : un orc est un agent de notre simulation, et va donc, en plus de posséder des caractéristiques
    // d'un agent, va également posséder des statistiques propres et qui vont varier d'un agent à un autre
    
        private double maxHealth;
	private double health;
	
	private double x;
	private double y;
	private double size;

	public Orc(double health, double maxHealth, double x, double y, double size) {
                ArrayList<Action> actions = new ArrayList<Action>();
                super(actions);
		this.health = health;
		this.maxHealth = maxHealth;
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public void loseHealth(int amount) {
                health = max(health - amount, 0);
	}
        
        public void addHealth(int amount) {
                health = min(maxHealth, health + amount);
        }
        
        public boolean isAlive() {
            return (health > 0);
        }
        
        public void move(double vx, double vy) {
            x += vx;
            y += vy; 
        }

	public void draw(Graphics2D g2d) {
		g2d.setColor(new Color(80, 100, 30));
		g2d.fillOval((int)(x-size*0.5), (int)(y-size*0.5), (int)size, (int)size);

		int healthBarPos = 10;

		g2d.setColor(new Color(210, 10, 20));
		g2d.fillRect((int)(x-size*0.75), (int)(y-size*0.5-healthBarPos), (int)(size*1.5), 10);
		g2d.setColor(new Color(100, 180, 60));
		g2d.fillRect((int)(x-size*0.75), (int)(y-size*0.5-healthBarPos), (int)(size*1.5*(health/maxHealth)), healthBarPos);
	}
}

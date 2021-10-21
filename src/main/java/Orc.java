import java.awt.*;
import static java.lang.Math.*;

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
		super();
		this.health = health;
		this.maxHealth = maxHealth;
		this.x = x;
		this.y = y;
		this.size = size;

		initActions();
	}

	private void initActions() {
		addAction("avancer", new Action_Avancer());
		addAction("attaquer", new Action_Attaquer());
	}

	@Override
	protected Action<Orc> prendreDesision(Environnement env) {
		Action<Orc> choixFinal = null;

		Orc closest = env.getClosestOrc(this);
		if (closest == null) {
			return null;
		}
		if (health < maxHealth*0.5) {
			Action_Avancer choix = (Action_Avancer) getAction("avancer");
			choix.setEnv(env);
			double cX, cY;
			cX = this.x - closest.x;
			cY = this.y - closest.y;

			double norm = Math.sqrt(getSqrDistanceTo(closest));
			cX /= norm;
			cY /= norm;

			if (env.isIn(x + cX, y + cY)) {
				choix.setPosDepX(cX);
				choix.setPosDepY(cY);
			} else {
				choix.setPosDepX(0.0);
				choix.setPosDepY(0.0);
			}
			choixFinal = choix;
		}
		else if (getSqrDistanceTo(closest) <= 4 * size * size) {
			Action_Attaquer choix = (Action_Attaquer) getAction("attaquer");
			choix.setEnv(env);
			choix.setTarget(closest);
			choixFinal = choix;
		} else {
			Action_Avancer choix = (Action_Avancer) getAction("avancer");
			choix.setEnv(env);
			double cX, cY;
			cX = closest.x - this.x;
			cY = closest.y - this.y;
			if (isIn(closest)) {
				cX = -cX;
				cY = -cY;
			}
			else {
				double norm = Math.sqrt(getSqrDistanceTo(closest));
				cX /= norm;
				cY /= norm;
			}
			if (env.isIn(x + cX, y + cY)) {
				choix.setPosDepX(cX);
				choix.setPosDepY(cY);
			} else {
				choix.setPosDepX(0.0);
				choix.setPosDepY(0.0);
			}
			choixFinal = choix;
		}

		return choixFinal;
	}

	public void loseHealth(double amount) {
		health = max(health - amount, 0);
	}
        
	public void addHealth(double amount) {
		health = min(maxHealth, health + amount);
	}

	public boolean isAlive() {
		return (health > 0);
	}

	public void move(double vx, double vy) {
		x += vx;
		y += vy;
	}

	public double getSqrDistanceTo(Orc o) {
		double dx = o.x - this.x;
		double dy = o.y - this.y;
		return (dx*dx + dy*dy);
	}

	public boolean isIn(Orc o) {
		double dx = o.x - this.x;
		double dy = o.y - this.y;
		return (dx*dx + dy*dy) <= this.size*this.size;
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

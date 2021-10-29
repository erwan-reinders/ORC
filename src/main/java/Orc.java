import java.awt.*;
import static java.lang.Math.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Orc implements Agent, Drawable{
    /*Classe abstraite symbolisant un Orc*/
    //Idée : un orc est un agent de notre simulation, et va donc, en plus de posséder des caractéristiques
    // d'un agent, va également posséder des statistiques propres et qui vont varier d'un agent à un autre


	protected List<Action<Orc>> actions;
	private Action<Orc> action_choix;

	private double maxHealth;
	private double health;
	
	private double x;
	private double y;
	private double size;

	public Orc(double health, double maxHealth, double x, double y, double size) {
		actions = new ArrayList<Action<Orc>>();
		action_choix = null;

		this.health = health;
		this.maxHealth = maxHealth;
		this.x = x;
		this.y = y;
		this.size = size;
	}

	/**============ ACTIONS DE AGENT ORC ============**/
	public void prendreDesision(Environnement env){
		Iterator<Action<Orc>> ite = this.actions.iterator();
		while(ite.hasNext()){
			Action<Orc> pa = ite.next();
			if(pa.estExecutable(this)){
				if((action_choix == null) || (action_choix != null && pa.getCout() < action_choix.getCout())){
					action_choix = pa;
				}
			}
		}
	}

	public void remplirActions(){
		this.actions.add( new Action_Avancer());
		this.actions.add( new Action_Attaquer());
	}

	public void executerDesision(){
		if(action_choix != null){
			action_choix.executer(this);
		}
	}

	/**============ ACTIONS DE ORC ============**/
	public void loseHealth(double amount) {
                health = max(health - amount, 0);
	}

	public void addHealth(int amount) {
		health = min(maxHealth, health + amount);
	}

	public void move(double vx, double vy) {
            x += vx;
            y += vy; 
        }

	//Pertinant de la mettre ici ?
	public double getSqrDistanceTo(Orc o) {
		double dx = o.x - this.x;
		double dy = o.y - this.y;
		return (dx*dx + dy*dy);
	}


	//Fonctions de dessin
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

	//GETTERS ET SETTERS
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	//PREDICATS
	public boolean isAlive() {
		return (health > 0);
	}

	public boolean isAffraid(){return (health < maxHealth*.5);}

	public boolean estAPorte(Orc o){
		return this.getSqrDistanceTo(o) <= 4 * size * size;
	}
}

import java.awt.*;
import static java.lang.Math.*;

import java.util.*;
import java.util.List;

import MathClass.Vec2;

public class Orc extends Agent_Combat{
    /*Classe symbolisant un Orc*/
    //Idée : un orc est un agent de notre simulation, et va donc, en plus de posséder des caractéristiques
    // d'un agent, va également posséder des statistiques propres et qui vont varier d'un agent de combat à un autre

	//Gestion des actions
	protected Map<String,Action<Orc>> actions;
	private Action<Orc> action_choix;
	private StrategieDeDeplacement SDD;


	//Gestion des traces
	private Trace tracePrecedente = null;
	private int cpt_trace = 0;
	public static int periode_trace = 20;

	//Gestion pousse de l'affichage
	public boolean afficher_fov = false;
	public boolean afficher_range = false;

	//Gestion de la regen HP
	public static int nb_tps_regen = 30;
	private int cpt_regen = 1;

	public Orc(double health, double maxHealth, Vec2 pos, double size, EquipeData<Orc> eq, ChampsDeVision fov, double vitesse) {
		super(new Cercle(pos,size),maxHealth,health,pos,size,eq,fov);

		//Stratégie de déplacement par défaut d'un orc
		SDD = new SDD_Initialisation(this);
		actions = new HashMap<String, Action<Orc>>();
		action_choix = null;

		remplirActions();
	}

	public Orc(double health, double maxHealth, double x, double y, double size) {
		this(health,maxHealth,x,y,size,null,null);
	}

	public Orc(double health, double maxHealth, double x, double y, double size, EquipeData<Orc> eq, ChampsDeVision fov){
		this(health, maxHealth, x, y, size, eq,fov, 1);
	}

	public Orc(double health, double maxHealth, double x, double y, double size, EquipeData<Orc> eq, ChampsDeVision fov, double vitesse) {
		this(health,maxHealth,new Vec2(x,y),size,eq,fov,vitesse);
	}

		/**============ INITIALISATION ============**/
	//Méthode permettant à un Agent de renseigner ses actions
	private void remplirActions(){
		this.actions.put( "avancer",new Action_Avancer());
		this.actions.put( "attaquer", new Action_Attaquer());
		this.actions.put( "nerienfaire",new Action_NeRienFaire());
	}

	/**============ ACTIONS DE AGENT ORC ============**/
	public void prendreDesision(Environnement env){
		action_choix = null;
		for (Action<Orc> pa : this.actions.values()) {
			//System.out.println("====EVALUATION DE : " + pa);
			if (pa.estExecutable(this)) {
				//System.out.println("          BON");
				if ((action_choix == null) || (pa.getCout() < action_choix.getCout())) {
					action_choix = pa;
					//System.out.println("          PRISE");
				}
			//}else{
				//System.out.println("          PAS BON");
			}
		}
		//System.out.println("CHOIX " + ((action_choix!=null)?"PRIS" : "PAS PRIS"));
	}

	public void executerDesision(){
		//par défaut, l'orc emmet une trace
		if(cpt_trace%periode_trace == 0) this.tracePrecedente = equipe.ajouterTrace(this.getPosition(),tracePrecedente);
		cpt_trace++;
		if(action_choix != null){
			//System.out.println("================== EXECUTION DECISION ==================");
			//System.out.println(this);
			//System.out.println(action_choix);
			if(action_choix.toString().equals("Action_NeRienFaire")){
				//System.out.println("CA FAIT " + cpt_regen + " FOIS QUE JE NE FAIS RIEN");
				cpt_regen++;
			}else{
				cpt_regen = 1;
			}
			action_choix.executer(this);
		}
		//Gestion de la regen HP
		if(isAlive() && cpt_regen>=nb_tps_regen+1){
			double pv = Math.random()/2;
			//System.out.println("JE ME SOIGNE DE " + pv);
			addHealth(pv);
		}
	}

	/**============ ACTIONS DE ORC ============**/
	public void loseHealth(double amount) {
		health = max(health - amount, 0);
	}

	public void addHealth(double amount) {
		health = min(maxHealth, health + amount);
	}

	public void move(Vec2 v) {
            position.x += v.x*vitesse;
            position.y += v.y*vitesse;
	}

	/**============ PREDICATS DE ORC ============**/

	public boolean estAPorte(Orc o){
		return Math.sqrt(Vec2.getSqrDistanceTo(o.position,this.position)) <= getRange();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Orc orc = (Orc) o;
		return Double.compare(orc.maxHealth, maxHealth) == 0 &&
				Double.compare(orc.health, health) == 0 &&
				(this.position ==  orc.position)&&
				Double.compare(orc.size, size) == 0 &&
				id == orc.id;
	}

	public void draw(Graphics2D g2d) {
		//System.out.println("DESSIN DE " + this);
		//Dessin de la fov
		if(afficher_fov) cdv.draw(g2d);
		//Dessin de la range
		if(afficher_range) {
			g2d.setColor(Color.YELLOW);
			g2d.drawOval((int)(getX() - getRange()),(int)(getY()- getRange()),(int)Math.round(getRange()*2),(int)Math.round(getRange()*2));
		}
		//Dessin de la couleur de l'équipe
		//this.apparence.grossir(1.3);
		//this.apparence.draw(true,g2d, this.equipe.getCouleur());

		//this.apparence.grossir(1/1.3);
		//g2d.setColor(new Color(80, 100, 30));
		//g2d.fillOval((int)(x-size*0.5), (int)(y-size*0.5), (int)size, (int)size);
		this.apparence.draw(true,g2d, new Color(80, 100, 30));
		this.apparence.draw(false,g2d, this.equipe.getCouleur());

		/*Dessin de la barre de vie*/
		int healthBarPos = (int)size;
		g2d.setColor(new Color(130, 10, 20));
		g2d.fillRect((int)(this.getX() -size*0.75), (int)(this.getY() -size*0.5-healthBarPos), (int)(size*1.5), healthBarPos/2);
		g2d.setColor(new Color(100, 180, 60));
		g2d.fillRect((int)(this.getX()-size*0.75), (int)(this.getY()-size*0.5-healthBarPos), (int)(size*1.5*(health/maxHealth)), healthBarPos/2);

		/*Dessin de l'id*/
		if(KeyManager.getInstance().C_ENABLED) {
			//System.out.println("============================================================ TOUCHE PRESSEE ============================================================");
			g2d.setColor(new Color(200, 200, 200));
			g2d.drawString(Integer.toString(id), (int) (this.getX()-size*0.25), (int) (this.getY()+size*0.25));
		}
	}

	//GETTERS ET SETTERS
	public StrategieDeDeplacement getSDD() {
		return SDD;
	}

	public void setSDD(StrategieDeDeplacement SDD) {
		this.SDD = SDD;
	}

	public Action<Orc> getAction(String name){
		return actions.get(name);
	}

	@Override
	public String toString() {
		return "Orc{" +
				"maxHealth=" + maxHealth +
				", health=" + health +
				", position=" + position +
				", size=" + size +
				", id=" + id +
				'}';
	}
}

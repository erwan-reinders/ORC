import java.awt.*;
import static java.lang.Math.*;

import java.util.*;
import java.util.List;

import MathClass.Vec2;

public class Orc implements Agent{
    /*Classe abstraite symbolisant un Orc*/
    //Idée : un orc est un agent de notre simulation, et va donc, en plus de posséder des caractéristiques
    // d'un agent, va également posséder des statistiques propres et qui vont varier d'un agent à un autre

	protected Map<String,Action<Orc>> actions;
	private Action<Orc> action_choix;
	private StrategieDeDeplacement SDD;

	/*DATA Orc*/
	private Forme apparence;
	private double maxHealth;
	private double health;

	private Vec2 position;
	private double size;
	private double rapportHauteurLargeur = 2;
	private double vitesse = 1;
	
	private static int cpt = 0;
	private int id;

	private EquipeData<Orc> equipe;
	private ChampsDeVision cdv;

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

	class ChampsDeVision implements Drawable{
		/*Classe interne symbolisant le champs de vision d'un Orc*/
		private Forme aspect;
		private Environnement env;

		//Stratégie particulière
		List<Triangle> triangles = new ArrayList<Triangle>();

		public ChampsDeVision(Forme f, Environnement data){
			aspect = f;
			env = data;
		}

		//ANCIEENNE FONCTION AVEC SEULEMENT DES RAYONS
		public Set<Element> getAllElementInCDV(int precision){
			Set<Element> el = new HashSet<Element>();

			if(precision>1){
				Vec2 c = aspect.getCenter();
				for (int i = 0; i<=precision; i++){
					double angle = Math.PI*2 * ((double)i/(double)precision);
					double x_ray = Math.cos(angle);
					double y_ray = Math.sin(angle);
					boolean fini_ray = false;

					for (int j = 0; j<=precision && (!fini_ray); j++){
						double coef = ((double)j/(double)precision) * aspect.getMaxDistance()/2;
						x_ray = x_ray * coef + c.x;
						y_ray = y_ray * coef + c.y;

						//Si on ne dépasse pas la limite imposée par la forme
						if(aspect.estContennu(x_ray,y_ray)){
							//Gestion des obstacles en Vec2(x_ray,y-ray)
							for (Obstacle o : env.getData().getObstacles()) {
								//S'il y a bien un obstacle non encore rencontré
								if(!el.contains(o) && o.estContennu(x_ray,y_ray)){
									//On l'ajoute
									el.add(o);
									//Si l'orc ne peut pas voir ni au dessus ni au travers de cet obstacle
									if((o.getHauteur()>=size*rapportHauteurLargeur) && o.getMateriel().getCoefTrans()<Math.random()){
										//On ne va pas plus loin dans le traitement
										fini_ray = true;
										break;
									}
								}
							}
							if(!fini_ray){
								//Gestion des autres Agents Orcs
								for(Orc orc : env.getOrcs()){
									//S'il y a bien un orc non encore rencontré
									if(!el.contains(orc) && orc.getApparence().estContennu(x_ray,y_ray)){
										//On l'ajoute
										el.add(orc);
										fini_ray = true;
										break;
									}
								}
							}
						}else{
							fini_ray = true;
						}
					}
				}
			}
			return el;
		}

		public Set<Element> getAllElementInCDV_triangles(int precision){
			triangles.clear();
			Set<Element> el = new HashSet<Element>();

			if(precision>1){
				//System.out.println("DEPART ==== CDV TRIANGLE : " + precision);
				//System.out.println("DEPART ==== CDV ASPECT : " + aspect);

				Vec2 c = new Vec2(aspect.getCenter());

				double increment = Math.PI*2 * ((double)2/(double)(precision*2));

				double angle = Math.PI*2 * ((double)(precision*2-1)/(double)(precision*2));
				Vec2 vd_A = new Vec2(Math.cos(angle),Math.sin(angle));
				angle += increment;
				Vec2 vd_B = new Vec2(Math.cos(angle),Math.sin(angle));

				Vec2 A = new Vec2(env.getData().posMaxAtteinteParRayon_VecDir(c,vd_A,aspect));
				Vec2 B = new Vec2(env.getData().posMaxAtteinteParRayon_VecDir(c,vd_B,aspect));

				//System.out.println("DEPART ==== VECTEUR A :" + A);
				//System.out.println("DEPART ==== VECTEUR B :" + B);

				for (int i = 0; i<precision; i++){
					Triangle t = new Triangle(A,B,c);
					triangles.add(new Triangle(t));
					//System.out.println("TRIANGLE FORME :" + t);
					for (Orc o : env.getOrcs()) {
						if(!el.contains(o) && t.estContennu(o.getX(),o.getY())) el.add(o);
					}
					for (Obstacle o : env.getData().getObstacles()) {
						if(!el.contains(o) && t.estContennu(o.getAspect().getCenter())) el.add(o);
					}

					A = new Vec2(B);
					angle += increment;
					vd_B = new Vec2(Math.cos(angle),Math.sin(angle));
					B = new Vec2(env.getData().posMaxAtteinteParRayon_VecDir(c,vd_B,aspect));
				}
			}
			return el;
		}

		public Set<Orc> getAllOrcInSimpleCDV(){
			Set<Orc> ret = new HashSet<Orc>();
			for(Orc o : env.getOrcs()){
				if(aspect.estContennu(new Vec2(o.getPosition())) && !ret.contains(o)) ret.add(o);
			}
			return ret;
		}

		public void draw(Graphics2D g2d) {
			//System.out.println("ASPECT : " + aspect+ "," + aspect.getCenter());
			if(!triangles.isEmpty()){
				for (Triangle t: triangles) {
					//System.out.println("      DESSIN DU TRIANGLE : " + t);
					t.draw(true,g2d,new Color(200,100,0,120));
				}
				//aspect.draw(g2d,new Color(200,100,0,255));
				triangles.clear();
			}else{
				aspect.draw(true,g2d,new Color(200,100,0,120));
			}
		}
	}

	public Orc(double health, double maxHealth, double x, double y, double size, EquipeData<Orc> eq, ChampsDeVision fov, double vitesse) {
		//Stratégie de déplacement par défaut d'un orc
		SDD = new SDD_Initialisation(this);
		actions = new HashMap<String, Action<Orc>>();
		action_choix = null;

		this.health = health;
		this.maxHealth = maxHealth;
		position = new Vec2(x,y);
		this.size = size;
		apparence = new Cercle(this.position,this.size);
		id = cpt;
		//equipe = cpt;
		cpt++;
		equipe = eq;
		cdv = fov;
		this.vitesse = vitesse;
		remplirActions();
	}

	public Orc(double health, double maxHealth, double x, double y, double size) {
		this(health,maxHealth,x,y,size,null,null);
	}

	public Orc(double health, double maxHealth, double x, double y, double size, EquipeData<Orc> eq, ChampsDeVision fov){
		this(health, maxHealth, x, y, size, eq,fov, 1);
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
	public boolean isAlive() {
		return (health > 0);
	}

	public boolean isAffraid(){return (health < maxHealth*.5);}

	public boolean estAPorte(Orc o){
		return Math.sqrt(Vec2.getSqrDistanceTo(o.position,this.position)) <= getRange();
	}

	public boolean estAllie(Orc o){
		return equipe.isAllie(o.getEquipe());
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
		this.apparence.draw(true,g2d,new Color(80, 100, 30));
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
	public Forme getApparence(){
		return apparence;
	}

	public double getX() {
		return position.x;
	}

	public double getY() {
		return position.y;
	}

	public Vec2 getPosition() {
		return position;
	}

	public StrategieDeDeplacement getSDD() {
		return SDD;
	}

	public void setSDD(StrategieDeDeplacement SDD) {
		this.SDD = SDD;
	}

	public void setCdv(Environnement env, Forme f) {
		this.cdv = new ChampsDeVision(f,env);
	}

	public double getVitesse() {
		return vitesse;
	}

	public void setVitesse(double v){
		this.vitesse = v;
	}

	public ChampsDeVision getCdv() {
		return cdv;
	}

	public EquipeData<Orc> getEquipe() {
		return equipe;
	}

	public Action<Orc> getAction(String name){
		return actions.get(name);
	}

	public double getHealth() {
		return health;
	}

	public double getRange(){
		return 2.5 * size;
	}

	public double getPuissanceAttaque(){
		return (Math.random()+1)*2;
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

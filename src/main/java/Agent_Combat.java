import MathClass.Vec2;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Agent_Combat implements Agent {
    /*Classe abstraite symbolisant un Agent de combat*/

    /*DATA Orc*/
    protected Forme apparence;
    protected double maxHealth;
    protected double health;

    protected Vec2 position;
    protected double size;
    protected double rapportHauteurLargeur = 2;
    protected double vitesse = 1;

    protected static int cpt = 0;
    protected int id;

    protected EquipeData<Orc> equipe;
    protected ChampsDeVision cdv;

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

        //ANCIENNE FONCTION AVEC SEULEMENT DES RAYONS
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
                aspect.draw(true,g2d,new Color(100,200,0,120));
            }
        }
    }

    public Agent_Combat(Forme apparence, double maxHealth, double health, Vec2 position, double size, EquipeData<Orc> equipe, ChampsDeVision cdv) {
        this.apparence = apparence;
        this.maxHealth = maxHealth;
        this.health = health;
        this.position = position;
        this.size = size;


        id = cpt;
        //equipe = cpt;
        cpt++;

        this.equipe = equipe;
        this.cdv = cdv;
    }

    public boolean isAlive() {
        return (health > 0);
    }

    public boolean isAffraid(){return (health < maxHealth*.5);}

    public boolean estAllie(Orc o){
        return equipe.isAllie(o.getEquipe());
    }

    public double getRange(){
        return 2.5 * size;
    }

    public double getPuissanceAttaque(){
        return (Math.random()+1)*2;
    }

    /*GETTER ET SETTER*/
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

    public double getHealth() {
        return health;
    }
}

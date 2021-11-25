import MathClass.Vec2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnvironnementData implements Drawable{
    /*Classe modélisant les données annexes d'un Environnement*/

    private Forme arene;
    private List<Obstacle> obstacles;
    private Color BGColor = new Color(0,0,0);

    VisibilityGraph vg;

    public EnvironnementData(Forme arene) {
        this.arene = arene;
        this.obstacles = new ArrayList<Obstacle>();

        this.vg = new VisibilityGraph();
    }

    public EnvironnementData(Forme arene, Color bg) {
        this(arene);
        BGColor = bg;
    }

    public EnvironnementData(Forme arene, List<Obstacle> obstacles) {
        this.arene = arene;
        this.obstacles = obstacles;
    }

    public void ajouterObstacles(Obstacle ... obs){
        vg.addObstacles(obs);
        obstacles.addAll(Arrays.asList(obs));
    }

    public void draw(Graphics2D g2d) {
        arene.draw(true,g2d,BGColor);
        for(Obstacle o : obstacles){
            o.draw(g2d);
        }
    }

    public boolean isIn(double x, double y) {
        for(Obstacle o : obstacles){
            if(!o.peutPasserAuTravers() && o.estContennu(x,y)) {
                System.out.println(x+","+y + " DEDANS UN OBSTACLE : " + o);
                return false;
            }
        }
        return arene.estContennu(x, y);
    }

    public Element elIsIn(double x, double y){
        //Méthode pour la SDD_Separation
        for(Obstacle o :obstacles){
            if(!o.peutPasserAuTravers() && o.estContennu(x,y)) return o;
        }
        return null;
    }

    public double getRalentissement(Vec2 pos){
        double ralentissement = 1.;
        int deno = 1;
        for (Obstacle o:obstacles) {
            if(o.peutPasserAuTravers() && o.estContennu(pos.x,pos.y)){
                deno+=o.getMateriel().getCoefPassage();
            }
        }
        return ralentissement/deno;
    }

    public Vec2 posMaxAtteinteParRayon(Vec2 depart, Vec2 arrive){
        //System.out.println("    ANALYSE RAYON==============");
        //System.out.println("            DEPART RAYON : " + depart);
        //System.out.println("            ARRIVE RAYON : " + arrive);

        //Tir de rayon, voir pos max atteinte avant rencontre elem
        if(!isIn(depart.x,depart.y)){
            //System.out.println("     RES : " + depart);
            return new Vec2(depart);
        }

        //Vecteur depart->arrive
        Vec2 v = new Vec2(arrive.x-depart.x,arrive.y-depart.y);
        //Vecteur strictement positif depart->arrive_pos
        Vec2 v_pos = new Vec2();
        v_pos.x = (v.x<0)? -v.x : v.x;
        v_pos.y = (v.y<0)? -v.y : v.y;

        Vec2 arrive_pos = new Vec2(depart.x+v_pos.x,depart.y+v_pos.y);

        //System.out.println("     V : " + v);
        //System.out.println("     VPOS : " + v_pos);
        //System.out.println("     ARRIVE POS : " + arrive_pos);

        v.normalize();
        v_pos.normalize();

        Vec2 cur_p = new Vec2(depart.x+v.x,depart.y+v.y);
        Vec2 cur_p_pos = new Vec2(depart.x+v_pos.x,depart.y+v_pos.y);

        //System.out.println("     curP : " + cur_p);
        //System.out.println("     curPPOS : " + cur_p_pos);

        while(cur_p_pos.x <=arrive_pos.x && cur_p_pos.y <=arrive_pos.y && isIn(cur_p.x,cur_p.y)){
            cur_p.x+=v.x;
            cur_p.y+=v.y;

            cur_p_pos.x+=v_pos.x;
            cur_p_pos.y+=v_pos.y;

            //System.out.println("  ON AUGMENTE");
            //System.out.println("     curP : " + cur_p);
            //System.out.println("     curPPOS : " + cur_p_pos);
        }
        //System.out.println("FIN DE BOUCLE curp_p : " + cur_p);
        //System.out.println("              curp_p_pos : " + cur_p_pos);

        //Si on s'est arrété parcequ'on est plus dedans
        if(!isIn(cur_p.x,cur_p.y)){
            cur_p.x-=v.x;
            cur_p.y-=v.y;

            return cur_p;
        }
        //Si on est allé jusqu'au bout, on peut retourner l'arrivee
        return new Vec2(arrive);
    }

    public Vec2 posMaxAtteinteParRayon_VecDir(Vec2 depart, Vec2 vdir){
        //System.out.println(" ANALYSE RAYON==============");
        //System.out.println("        DEPART RAYON : " + depart);
        //System.out.println("        DIRECTION RAYON : " + vdir);

        //Tir de rayon partant de depart direction vdir,
        //voir pos max atteinte avant rencontre elem
        Vec2 d = new Vec2(depart.x+vdir.x,depart.y+vdir.y);
        while(d.x<=arene.getWidth() && d.x>=0 && d.y<=arene.getHeight() && d.y>=0){
            d = new Vec2(d.x+vdir.x,d.y+vdir.y);
        }
        d = new Vec2(d.x-vdir.x,d.y-vdir.y);
        return posMaxAtteinteParRayon(depart,d);
    }

    public Vec2 posMaxAtteinteParRayon_VecDir(Vec2 depart, Vec2 vdir, Forme f){
        //System.out.println(" ANALYSE RAYON==============");
        //System.out.println("        DEPART RAYON : " + depart);
        //System.out.println("        DIRECTION RAYON : " + vdir);
        //System.out.println("        FORME RAYON : " + f);

        //Tir de rayon partant de depart direction vdir,
        //voir pos max atteinte avant rencontre elem ou de sortir de la forme f
        Vec2 d = new Vec2(depart.x+vdir.x,depart.y+vdir.y);
        while(d.x<=arene.getWidth() && d.x>=0 && d.y<=arene.getHeight() && d.y>=0 && f.estContennu(d.x,d.y)){
            d = new Vec2(d.x+vdir.x,d.y+vdir.y);
        }
        d = new Vec2(d.x-vdir.x,d.y-vdir.y);
        //System.out.println("PMAPR - VDIR d : " + d);
        return posMaxAtteinteParRayon(depart,d);
    }

    public Forme getArene() {
        return arene;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }
}

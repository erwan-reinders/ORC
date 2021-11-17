import MathClass.Vec2;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.util.*;
import java.awt.*;
import java.util.List;

public class Environnement {
    /*Classe symbolisant un Environnement*/
    //Ici, notre environnement se modélise par un espace 2D composant les limites de notre arene de combat

    private ArrayList<Orc> orcs;
    private EnvironnementData data;

    public Environnement(EnvironnementData data) {
        this.data = data;
        this.orcs = new ArrayList<Orc>();
    }

    public void addOrc(Orc o) {
        orcs.add(o);
    }

    public ArrayList<Orc> getOrcs() {
        return orcs;
    }

    public EnvironnementData getData() {
        return data;
    }

    public boolean isIn(double x, double y) {
        for(Orc o :orcs){
            if(o.getApparence().estContennu(x,y)){
                System.out.println(x+","+y + " DEDANS UN ORC : " + o);
                return false;
            }
        }
        return data.isIn(x, y);
    }

    public boolean isIn(double x, double y,Orc orc) {
        for(Orc o :orcs){
            if(orc != o && o.getApparence().estContennu(x,y)){
                System.out.println(x+","+y + " DEDANS UN ORC : " + o);
                return false;
            }
        }
        return data.isIn(x, y);
    }

    public Element elIsIn(Orc orc){
        //Méthode pour la SDD_Separation
        for(Orc o :orcs){
            if(o!=orc && o.getApparence().estContennu(orc.getPosition())) return o;
        }
        return data.elIsIn(orc.getX(),orc.getY());
    }

    public Element estAtteintParRayon(){
        //Tir de raton, voir premier elem touche
        //Meme dans envData
        return null;
    }

    public Vec2 posMaxAtteinteParRayon(Vec2 depart, Vec2 arrive){
        System.out.println(" ANALYSE RAYON==============");
        System.out.println("        DEPART RAYON : " + depart);
        System.out.println("        ARRIVE RAYON : " + arrive);

        //Tir de rayon, voir pos max atteinte avant rencontre elem
        if(!isIn(depart.x,depart.y)){
            System.out.println("     RES : " + depart);
            return depart;
        }
        boolean fini_ray = false;
        //Vecteur depart->arrive
        Vec2 v = new Vec2(arrive.x-depart.x,arrive.y-depart.y);
        //Vecteur strictement positif depart->arrive_pos
        Vec2 v_pos = new Vec2();
        v_pos.x = (v.x<0)? -v.x : v.x;
        v_pos.y = (v.y<0)? -v.y : v.y;

        Vec2 arrive_pos = new Vec2(depart.x+v_pos.x,depart.y+v_pos.y);

        System.out.println("     V : " + v);
        System.out.println("     VPOS : " + v_pos);
        System.out.println("     ARRIVE POS : " + arrive_pos);


        v.normalize();
        v_pos.normalize();

        Vec2 cur_p = new Vec2(depart.x+v.x,depart.y+v.y);
        Vec2 cur_p_pos = new Vec2(depart.x+v_pos.x,depart.y+v_pos.y);

        //System.out.println("     curP : " + cur_p);
        //System.out.println("     curPPOS : " + cur_p_pos);

        //int scal = 0;
        while(cur_p_pos.x <=arrive_pos.x && cur_p_pos.y <=arrive_pos.y){
            //for (int i = 0; i < scal; i++) {
            cur_p.x+=v.x;
            cur_p.y+=v.y;

            cur_p_pos.x+=v_pos.x;
            cur_p_pos.y+=v_pos.y;

            //System.out.println("  ON AUGMENTE");
            //System.out.println("     curP : " + cur_p);
            //System.out.println("     curPPOS : " + cur_p_pos);

            //}
            //Si on est plus dedans, on retourne à l'étape précédente et on termine
            //scal++;
        }
        //Si on s'est arrété parcequ'on a dépassé les limites
        if(!isIn(cur_p.x,cur_p.y)){
            cur_p.x-=v.x;
            cur_p.y-=v.y;

            return cur_p;
        }
        //Si on est allé jusqu'au bout, on peut retourner l'arrivee
        return arrive;
    }

    public Vec2 posMaxAtteinteParRayon_VecDir(Vec2 depart, Vec2 vdir){
        //System.out.println(" ANALYSE RAYON==============");
        //System.out.println("        DEPART RAYON : " + depart);
        //System.out.println("        DIRECTION RAYON : " + vdir);
        return posMaxAtteinteParRayon(depart,data.posMaxAtteinteParRayon_VecDir(depart,vdir));
    }

    public Vec2 posMaxAtteinteParRayon_VecDir(Vec2 depart, Vec2 vdir, Forme f){
        //System.out.println(" ANALYSE RAYON==============");
        //System.out.println("        DEPART RAYON : " + depart);
        //System.out.println("        DIRECTION RAYON : " + vdir);
        //System.out.println("        FORME RAYON : " + f);
        return posMaxAtteinteParRayon(depart,data.posMaxAtteinteParRayon_VecDir(depart,vdir,f));
    }

    public Orc getClosestOrc(Orc o) {
        Orc minO = null;
        double minDist = data.getArene().getMaxDistance();
        minDist *= minDist;
        double distance;
        for (Orc oi : orcs) {
            distance = Vec2.getSqrDistanceTo(o.getPosition(),oi.getPosition());
            if (distance < minDist && oi != o) {
                minO = oi;
                minDist = distance;
            }
        }
        return minO;
    }

    public Orc getClosestEnnemiOrc(Orc o){
        Orc minO = null;
        double minDist = data.getArene().getMaxDistance();
        minDist *= minDist;
        double distance;
        for (Orc oi : orcs) {
            distance = Vec2.getSqrDistanceTo(o.getPosition(),oi.getPosition());
            if (distance < minDist && oi != o && !oi.estAllie(o)) {
                minO = oi;
                minDist = distance;
            }
        }
        return minO;
    }
    
    public Orc collidingOrc(Orc o) {
        for (Orc oi : orcs) {
            if (o.getApparence().estContennu(oi.getX(),oi.getY()) && o != oi) {
                return oi;
            }
        }
        return null;
    }
}

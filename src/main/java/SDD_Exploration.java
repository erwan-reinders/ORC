import MathClass.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SDD_Exploration implements StrategieDeDeplacement{
    /*Classe modélisant une stratégie de déplacment basée sur une maximisation des zones à explorer par un orc*/
    //Exploration par maximisation des zones non encore découverte
    //Exploration par carte iso_valeurs

    private Orc orc;
    private Environnement env;

    private Orc ennemi_vu;

    public int precision = 40;
    private List<Vec2> repetitions = new ArrayList<Vec2>();


    public SDD_Exploration(Orc orc,Environnement env){
        this.orc = orc;
        this.env = env;
    }

    public Vec2 getProchainePosition() {
        Vec2 res = rechercheParTriangleEtRange();
        double r = env.getData().getRalentissement(orc.getPosition());
        return new Vec2(res.x*r,res.y*r);
    }

    /*Recherche de leur chemin par stratégie de cercles*/
    private Vec2 rechercheParCercles(){
        int rayon_precision = Orc.periode_trace;


        int b_val = orc.getEquipe().getNbTrace();
        Vec2 b_v2 = new Vec2();
        List<Vec2> mouvPos = new ArrayList<Vec2>();

        for (int i = 0; i<precision; i++){
            double angle = (double) i / (double) precision * 2 * Math.PI;
            Vec2 newp = new Vec2(Math.cos(angle), Math.sin(angle));
            Vec2 npRes = new Vec2(newp.x+orc.getX(),newp.y+orc.getY());

            System.out.println("POS ORC : " + orc.getPosition());
            System.out.println("======== POS A TRAITER");
            System.out.println(newp);
            System.out.println(npRes);

            int cur_val = orc.getEquipe().getNbTrace(new Cercle(npRes, rayon_precision*2));
            boolean b = env.isIn(npRes.x,npRes.y,orc);
            if(b && b_val > cur_val){
                System.out.println("BONNE POSITION CAR " + cur_val + " TRACE");
                b_val = cur_val;
                mouvPos.clear();
                mouvPos.add(new Vec2(newp));
            }else{
                if(b && b_val == cur_val){
                    System.out.println("BONNE POSITION CAR " + cur_val + " TRACE");
                    mouvPos.add(new Vec2(newp));
                }else{
                    System.out.println("PAS MEILLEURE POS CAR " + ((b)?  cur_val + " TRACE":"PAS DEDANS"));
                }
            }
        }
        //Prob toujours max mouv possible (10)
        System.out.println("MOUV POS : " + mouvPos.size());
        if(/*mouvPos.size() == precision ||*/ mouvPos.size()==1){
            System.out.println("=!=!=!=!= PAS DE NOUVEAU TEST =!=!=!=!=");
            for(Vec2 v : mouvPos) System.out.println("VEC PRESENT : " + v);
            b_v2 = mouvPos.get(0);
        }else{
            List<Vec2> posPosBis = new ArrayList<Vec2>();
            System.out.println("=!=!=!=!= NOUVEAU TEST =!=!=!=!=");
            //Sinon on augmente de nb et on recommence le test
            double nb = rayon_precision*2;
            b_val = orc.getEquipe().getNbTrace();

            for (Vec2 p :mouvPos) {
                Vec2 pb = new Vec2(p);
                //Penser a faire une meth incr dans formes pour récupérer la valeur la plus grande pour une pos voulue
                //Soit pos voulue, soit point sur périmètre
                //Dans envDATA pour gerer les obs
                //Meme mecanique que pour les lancers de rayon dans fov
                pb.multByScal(nb);
                Vec2 v = env.getData().posMaxAtteinteParRayon(new Vec2(orc.getX(),orc.getY()),new Vec2(orc.getX()+pb.x,orc.getY()+pb.y));
                System.out.println("POS VOULUE : " + new Vec2(orc.getX()+pb.x,orc.getY()+pb.y));
                System.out.println("POS EU : " + v);

                int cur_val = orc.getEquipe().getNbTrace(new Cercle(v, rayon_precision));

                if(cur_val<b_val){
                    System.out.println("NOUVELLE POS PRISE CAR NBTRACES : " + cur_val);
                    System.out.println(" 2======= VEC2 PRIS : " + p);

                    b_val = cur_val;
                    posPosBis.clear();
                    posPosBis.add(new Vec2(p));
                }else{
                    if(cur_val==b_val){
                        posPosBis.add(new Vec2(p));
                        System.out.println("PRIS CAR NBTRACES : " + cur_val);
                        System.out.println(" 2======= VEC2 PRIS : " + p);
                    }else{
                        System.out.println("PAS PRIS CAR NBTRACES : " + cur_val);
                        System.out.println(" 2======= VEC2 PAS PRIS : " + p);
                    }
                }
            }
            if(posPosBis.size() == 1) {
                b_v2 = mouvPos.get(0);
            }else{
                for(Vec2 vec : posPosBis) System.out.println("DERNIER CHOIX : " + vec);
                b_v2 = posPosBis.get((int)(Math.random()*posPosBis.size()));
            }
        }
        System.out.println("CHOIX : " + b_v2);
        return b_v2;
    }

    /*Recherche de leur chemin par stratégie de triangles*/
    private Vec2 rechercheParTriangle(){
        //double aireMin_tri = precision;
        double el = 2*Math.PI;

        double angVA = (precision*2-1)/(double)(precision*2)*el;
        double angVB = 1/(double)(precision*2)*el;
        Vec2 A = new Vec2(Math.cos(angVA), Math.sin(angVA));
        Vec2 B = new Vec2(Math.cos(angVB), Math.sin(angVB));
        A = new Vec2(env.getData().posMaxAtteinteParRayon_VecDir(orc.getPosition(),A));

        //System.out.println("A DEBUT => " + A);
        //System.out.println("VEC A : " + new Vec2(Math.cos(angVA), Math.sin(angVA)));
        //System.out.println("B DEBUT => " + B);
        //System.out.println("VEC B : " + new Vec2(Math.cos(angVB), Math.sin(angVB)));

        double i = 3;

        double nb = 0;
        double b_val = 1 + 1/(env.getData().getArene().getHeight()*env.getData().getArene().getWidth()/2);
        //Vec2 b_v2 = new Vec2();

        List<Vec2> b_pos = new ArrayList<Vec2>();

        while (nb<precision){
            double angle = nb / (double) precision * 2 * Math.PI;
            Vec2 newp = new Vec2(Math.cos(angle), Math.sin(angle));
            Vec2 npRes = new Vec2(newp.x+orc.getX(),newp.y+orc.getY());

            //System.out.println("POS ORC : " + orc.getPosition());
            //System.out.println("======== POS A TRAITER");
            //System.out.println(newp);
            //System.out.println(npRes);
            //System.out.println("A : " + A);
            //System.out.println("B : " + B);

            if(env.isIn(npRes.x,npRes.y,orc)) {
                //System.out.println("FORME DEDANS");
                //System.out.println("==== BEST VAL : " + b_val);

                B = new Vec2(env.getData().posMaxAtteinteParRayon_VecDir(orc.getPosition(), B));
                Triangle t = new Triangle(new Vec2(A), new Vec2(B), new Vec2(orc.getPosition()));
                //System.out.println("FORME UTILISEE : " + t);
                double b_cur = ((double)orc.getEquipe().getNbTrace(t)/(1000*orc.getEquipe().getNbTrace())) + ((t.getAire()==0)? 1:1/(100*t.getAire()));

                if (/*t.getAire() > aireMin_tri &&*/ b_cur < b_val) {
                    //System.out.println("BONNE POSITION CAR " + b_cur + " TRACE");
                    b_val = b_cur;
                    //b_v2 = newp;
                    b_pos.clear();
                    b_pos.add(new Vec2(newp));
                }else{
                    if(/*t.getAire() > aireMin_tri &&*/ b_cur == b_val){
                        b_pos.add(new Vec2(newp));
                    //}else{
                        //System.out.println((t.getAire() <= aireMin_tri)? "AIRE TROP PETITE " + t.getAire() : "PAS BONNE POSITION CAR " + b_cur + " TRACE");
                    }
                }
            //}else{
                //System.out.println("PAS BONNE POSITION CAR PAS DEDANS");
            }

            angVB = i/(double)(precision*2)*el;
            A = new Vec2(B);
            B = new Vec2(Math.cos(angVB), Math.sin(angVB));
            i+=2;
            nb++;
        }

        return b_pos.size() == 0? new Vec2() : b_pos.get((int)(Math.random()*b_pos.size()));
    }

    /*Recherche de leur chemin par stratégie de triangles et de cercles*/
    private Vec2 rechercheParTriangleEtRange() {
        Forme FOV_EXP = new Cercle(orc.getPosition(),(env.getData().getArene().getWidth()>env.getData().getArene().getWidth())? env.getData().getArene().getWidth()/2:env.getData().getArene().getHeight()/2);
        double el = 2 * Math.PI;

        double angVA = (precision * 2 - 1) / (double) (precision * 2) * el;
        double angVB = 1 / (double) (precision * 2) * el;
        Vec2 A = new Vec2(Math.cos(angVA), Math.sin(angVA));
        Vec2 B = new Vec2(Math.cos(angVB), Math.sin(angVB));
        A = new Vec2(env.getData().posMaxAtteinteParRayon_VecDir(orc.getPosition(), A,FOV_EXP));

        double inc_angB = 2./(double) (precision * 2) * el;
        double inc_angle = 1./ (double) precision * el;
        double angle = 0;

        double nb = 0;
        double b_val = 1 + 1 / (env.getData().getArene().getHeight() * env.getData().getArene().getWidth() / 2);

        List<Vec2> b_pos = new ArrayList<Vec2>();

        while (nb < precision) {
            Vec2 newp = new Vec2(Math.cos(angle), Math.sin(angle));
            Vec2 npRes = new Vec2(newp.x + orc.getX(), newp.y + orc.getY());

            if (env.isIn(npRes.x, npRes.y, orc)) {
                B = new Vec2(env.getData().posMaxAtteinteParRayon_VecDir(orc.getPosition(), B,FOV_EXP));
                Triangle t = new Triangle(new Vec2(A), new Vec2(B), new Vec2(orc.getPosition()));
                double b_cur = ((double) orc.getEquipe().getNbTrace(t) / (1000 * orc.getEquipe().getNbTrace())) + ((t.getAire() == 0) ? 1 : 1 / (200 * t.getAire()));

                if (b_cur < b_val) {
                    b_val = b_cur;
                    b_pos.clear();
                    b_pos.add(new Vec2(newp));
                } else {
                    if (b_cur == b_val) {
                        b_pos.add(new Vec2(newp));
                    }
                }
            }
            angle += inc_angle;
            angVB +=inc_angB;

            A = new Vec2(B);
            B = new Vec2(Math.cos(angVB), Math.sin(angVB));
            nb++;
        }
        return b_pos.size() == 0 ? new Vec2() : b_pos.get((int) (Math.random() * b_pos.size()));
    }

    public boolean estTermine() {
        //Si l'orc rencontre au moins un adversaire, alors il doit changer de stratégie de jeu
        //Il n'explore plus

        //System.out.println("+++ TEST TERMINE SDDEXPLORATION : ");

        //Set<Element> els = orc.getCdv().getAllElementInCDV(100);
        Set<Element> els = orc.getCdv().getAllElementInCDV_triangles(precision);

        for (Element el: els) {
            if(el instanceof Orc){
                //System.out.println("            JE VOIS : " + el);
                if(!orc.getEquipe().isAllie(((Orc)el).getEquipe())){
                    System.out.println("J'AI RENCONTRE UN ENNEMI : " + el);
                    //MAJ des data equipes
                    ennemi_vu = (Orc) el;
                    orc.getEquipe().ajouterEnnemiVu((Orc)el);
                    return true;
                }
            }
        }
        //S'il n'a pas d'ennemis dans son champs de vision, l'orc peut en avoir dans son equipe
        if(orc.getEquipe().ennemiAAttaquer()){
            ennemi_vu = orc.getEquipe().getACible();
            return true;
        }
        return false;
    }

    public StrategieDeDeplacement getProchaineStrategie(Environnement env) {
        SDD_Attaquer s = new SDD_Attaquer(orc,env);
        s.precision = precision;
        s.setTarget(ennemi_vu);
        return s;
    }

    @Override
    public String toString() {
        return "SDD_Exploration{}";
    }
}

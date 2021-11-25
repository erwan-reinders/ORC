import MathClass.Vec2;


import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Ordonnanceur implements Drawable{
    /*Classe symbolisant un Ordonnanceur*/
    //Ici, un ordonnanceur va faire évoluer le temps de la simulation en demandant à chaque agent présent
    //dans la simulation de DO une action
    //Il va posséder une liste d'Agents et les questionner à chaque période de temps donnée
    //Il va également posséder des "conditions de victoires" ou "d'arret de simulation"
    private Environnement env;

    /*Booléen d'affichage*/
    public boolean affichage_trace = true;
    public boolean equipe_upd = false;

    class Pair_IEq<T extends Agent_Combat>{
        int nb;
        EquipeData<T> eq;

        public Pair_IEq(int nb, EquipeData<T> eq) {
            this.nb = nb;
            this.eq = eq;
        }
    }

    private List<Pair_IEq<Orc>> equipes;

    public Ordonnanceur(int width, int height, int nbOrcs) {
        this(width,height,nbOrcs,Color.BLACK);
    }

    public Ordonnanceur(int width, int height, int nbOrcs, Color bgC) {
        env = new Environnement(new EnvironnementData(new Rectangle(0, 0, width, height), bgC));
        this.equipes = new ArrayList<Pair_IEq<Orc>>();

        //scenario_classique(nbOrcs);
        scenario_obstacle(nbOrcs);
    }

    private void scenario_classique(int nbOrcs){
        equipes.add(new Pair_IEq<Orc>(0,new EquipeData<Orc>(new Color(255,0,0))));
        equipes.add(new Pair_IEq<Orc>(0,new EquipeData<Orc>(new Color(0,0,255))));

        addOrcCircle(nbOrcs/2);
        addOrcRandom(nbOrcs/2);
    }

    private void scenario_obstacle(int nbOrcs){
        equipes.add(new Pair_IEq<Orc>(0,new EquipeData<Orc>(new Color(255,0,0))));
        equipes.add(new Pair_IEq<Orc>(0,new EquipeData<Orc>(new Color(0,0,255))));

        env.getData().ajouterObstacles(new Obstacle(Obstacle.typeMateriaux.PIERRE,new Rectangle(0,10,100,10)),new Obstacle(Obstacle.typeMateriaux.PIERRE,new Rectangle(90,10,10,100)),new Obstacle(Obstacle.typeMateriaux.EAU,new Cercle(50,60,40)),new Obstacle(Obstacle.typeMateriaux.PIERRE,new Rectangle(100,100,100,10)));
        addOrcRandom(nbOrcs);
    }

    private void addOrcRandom(int nbOrcs) {
        for (int i = 0; i < nbOrcs; i++) {
            int health = (int)(Math.random() * 50.0 + 100.0);
            int size = health/8;
            double x = size + Math.random() * (env.getData().getArene().getWidth()-size*2);
            double y = size + Math.random() * (env.getData().getArene().getHeight()-size*2);
            Orc o = new Orc(health, health, x,y, size, equipes.get(i%equipes.size()).eq,null,1);
            env.addOrc(o);
            o.setCdv(this.env,new Cercle(o.getPosition(),o.getRange()*3));

            equipes.get(i%equipes.size()).nb++;
        }
    }

    private void addOrcCircle(int nbOrcs) {
        double increment = 2.0*Math.PI / nbOrcs;
        double angle = 0.0;
        for (int i = 0; i < nbOrcs; i++) {
            int health = (int)(50 + (0.5*angle/Math.PI)*100);
            int size = health/8;
            double x = env.getData().getArene().getWidth()*0.5 + Math.cos(angle)*env.getData().getArene().getWidth()*0.2;
            double y = env.getData().getArene().getHeight()*0.5 + Math.sin(angle)*env.getData().getArene().getHeight()*0.2;

            Orc o = new Orc(health, health, x,y, size, equipes.get(i%equipes.size()).eq,null,1);
            env.addOrc(o);
            o.setCdv(this.env,new Cercle(o.getPosition(),o.getRange()*3));

            angle += increment;

            equipes.get(i%equipes.size()).nb++;
        }
    }

    public void update() {
        //On update dans un premier temps les equipes (gestion des traces)
        for (Pair_IEq<Orc> p: equipes) {
            p.eq.update();
        }

        Iterator<Orc> it = env.getOrcs().iterator();
        Orc o;
        while (it.hasNext()) {
            //System.out.println("==================================== NOUVEL ORC ====================================");
            o = it.next();
            System.out.println(o);
            //Si l'orc n'est plus vivant
            if (!o.isAlive()) {
                //On va update les compteurs de l'equipe de l'orc mort
                Iterator<Pair_IEq<Orc>> it_eq = equipes.iterator();
                while (it_eq.hasNext()){
                    Pair_IEq<Orc> p = (Pair_IEq<Orc>)it_eq.next();
                    if(p.eq == o.getEquipe()){
                        p.nb--;
                        if(p.nb == 0) it_eq.remove();
                        equipe_upd = true;
                        break;
                    }
                }
                it.remove();
            }else{
                //On va d'abord voir pour MAJ strat deplacement de l'orc
                if(o.getSDD().estTermine()){
                    //System.out.println("SDD FINI, ON CHANGE");
                    //System.out.println("ANCIENNE SDD : " + o.getSDD());
                    //System.out.println("NOUVELLE SDD : " + o.getSDD().getProchaineStrategie(this.env));
                    //if(o.getSDD().toString().equals("SDD_Attaquer{}")) o.loseHealth(o.getHealth());
                    o.setSDD(o.getSDD().getProchaineStrategie(this.env));
                }
                //Puis lui demander de choisir une action
                o.prendreDesision(env);
            }
        }

        for (Orc orc: env.getOrcs()) {
            orc.executerDesision();
        }
    }

    public boolean isFinished() {
        //Si au moins un orc est mort le tour précédent
        if(equipe_upd) {
            equipe_upd = false;
            Pair_IEq<Orc> p_eq = equipes.get(0);
            for (int i = 1; i<equipes.size();i++) {
                //Si présence d'au moins deux equipes non alliées dans la partie
                if(!equipes.get(i).eq.isAllie(p_eq.eq)) return false;
            }
            equipe_upd = true;
            return true;
        }
        return false;
    }

    //MAJ des primitives d'affichage
    public void upAffichage_fov(){
        for (Orc o:env.getOrcs()) {
            o.afficher_fov = !o.afficher_fov;
        }
    }

    public void upAffichage_range(){
        for (Orc o:env.getOrcs()) {
            o.afficher_range = !o.afficher_range;
        }
    }

    public void draw(Graphics2D g2d) {
        env.getData().draw(g2d);
        if(affichage_trace) {
            for (Pair_IEq<Orc> eq : equipes) {
                //System.out.println("========================================================DESSIN DE : " + eq);
                eq.eq.draw(g2d);
            }
        }else{
            //System.out.println("========================================================PEUT PAS DESSINER LES EQUIPES");
        }
        for (Orc o : env.getOrcs()) {
            o.draw(g2d);
        }
        if (isFinished()) {
            g2d.setColor(new Color(100, 100, 100));
            g2d.drawString("Finnished!", (int)(env.getData().getArene().getWidth()*0.5-10), 50);
        }
    }
}

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

    private List<EquipeData<Orc>> equipes;

    public Ordonnanceur(int width, int height, int nbOrcs) {
        this(width,height,nbOrcs,Color.BLACK);
    }

    public Ordonnanceur(int width, int height, int nbOrcs, Color bgC) {
        env = new Environnement(new EnvironnementData(new Rectangle(0, 0, width, height), bgC));
        this.equipes = new ArrayList<EquipeData<Orc>>();

        equipes.add(new EquipeData<Orc>(new Color(255,0,0)));
        equipes.add(new EquipeData<Orc>(new Color(0,0,255)));

        addOrcCircle(nbOrcs/2);
        addOrcRandom(nbOrcs/2);
    }

    private void addOrcRandom(int nbOrcs) {
        for (int i = 0; i < nbOrcs; i++) {
            int health = (int)(Math.random() * 50.0 + 100.0);
            int size = health/8;
            double x = size + Math.random() * (env.getData().getArene().getWidth()-size*2);
            double y = size + Math.random() * (env.getData().getArene().getHeight()-size*2);
            Orc o = new Orc(health, health, x,y, size, equipes.get(i%equipes.size()),null,1);
            env.addOrc(o);
            o.setCdv(this.env,new Cercle(o.getPosition(),o.getRange()*3));
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

            Orc o = new Orc(health, health, x,y, size, equipes.get(i%equipes.size()),null,1);
            env.addOrc(o);
            o.setCdv(this.env,new Cercle(o.getPosition(),o.getRange()*3));

            angle += increment;
        }
    }

    public void update() {
        //On update dans un premier temps les equipes (gestion des traces)
        for (EquipeData<Orc> e: equipes) {
            e.update();
        }

        Iterator<Orc> it = env.getOrcs().iterator();
        Orc o;
        while (it.hasNext()) {
            //System.out.println("==================================== NOUVEL ORC ====================================");
            o = it.next();
            //System.out.println(o);
            if (!o.isAlive()) {
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
        return env.getOrcs().size() < 2;
    }

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
            for (EquipeData<Orc> eq : equipes) {
                //System.out.println("========================================================DESSIN DE : " + eq);
                eq.draw(g2d);
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

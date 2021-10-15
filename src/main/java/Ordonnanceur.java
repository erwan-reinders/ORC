import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Ordonnanceur implements Drawable{
    /*Classe symbolisant un Ordonnanceur*/
    //Ici, un ordonnanceur va faire évoluer le temps de la simulation en demandant à chaque agent présent
    //dans la simulation de DO une action
    //Il va posséder une liste d'Agents et les questionner à chaque période de temps donnée
    //Il va également posséder des "conditions de victoires" ou "d'arret de simulation"
    private Environnement env;
    private ArrayList<Orc> orcs;

    public Ordonnanceur(int width, int height, int nbOrcs) {
        env = new Environnement(new Rectangle(0, 0, width, height));
        orcs = new ArrayList<Orc>();

        addOrcCircle(nbOrcs/2);
        addOrcRandom(nbOrcs/2);
    }

    private void addOrcRandom(int nbOrcs) {
        for (int i = 0; i < nbOrcs; i++) {
            int health = (int)(Math.random() * 50.0 + 100.0);
            int size = health/4;
            double x = size + Math.random() * (env.getWidth()-size*2);
            double y = size + Math.random() * (env.getHeight()-size*2);
            orcs.add(new Orc(health, health, x, y, size));
        }
    }

    private void addOrcCircle(int nbOrcs) {
        double increment = 2.0*Math.PI / nbOrcs;
        double angle = 0.0;
        for (int i = 0; i < nbOrcs; i++) {
            int health = (int)(50 + (0.5*angle/Math.PI)*100);
            int size = health/4;
            double x = env.getWidth()*0.5 + Math.cos(angle)*env.getWidth()*0.2;
            double y = env.getHeight()*0.5 + Math.sin(angle)*env.getHeight()*0.2;
            orcs.add(new Orc(health, health, x, y, size));
            angle += increment;
        }
    }

    public void update() {
        Iterator<Orc> it = orcs.iterator();
        Orc o;
        while (it.hasNext()) {
            o = it.next();

            //o.prendreDesision(env);
            o.loseHealth(1);

            if (!o.isAlive()) {
                orcs.remove(o);
            }
        }
    }

    public boolean isFinished() {
        return orcs.size() < 2;
    }


    public void draw(Graphics2D g2d) {
        env.draw(g2d);
        for (Orc o :
                orcs) {
            o.draw(g2d);
        }
        if (isFinished()) {
            g2d.drawString("Finnished!", (int)env.getWidth(), 10);
        }
    }
}

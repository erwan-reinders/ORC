import java.awt.*;
import java.util.ArrayList;

public class Ordonnanceur implements Drawable{
    /*Classe symbolisant un Ordonnanceur*/
    //Ici, un ordonnanceur va faire évoluer le temps de la simulation en demandant à chaque agent présent
    //dans la simulation de DO une action
    //Il va posséder une liste d'Agents et les questionner à chaque période de temps donnée
    //Il va également posséder des "conditions de victoires" ou "d'arret de simulation"
    private Environnement env;
    private ArrayList<Orc> orcs;

    public Ordonnanceur(int width, int height, int nbOrcs) {
        env = new Environnement(width, height);
        orcs = new ArrayList<Orc>();

        for (int i = 0; i < nbOrcs; i++) {
            int health = (int)(Math.random() * 50.0 + 100.0);
            int size = health/2;
            double x = size + Math.random() * (width-size*2);
            double y = size + Math.random() * (height-size*2);
            orcs.add(new Orc(health, health, x, y, size));
        }
    }

    public void update() {
        for (Orc o :
                orcs) {
            o.loseHealth(1);
        }
    }


    public void draw(Graphics2D g2d) {
        env.draw(g2d);
        for (Orc o :
                orcs) {
            o.draw(g2d);
        }
    }
}

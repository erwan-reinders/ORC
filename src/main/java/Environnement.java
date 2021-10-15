import java.util.*;
import java.awt.*;
import java.util.List;

public class Environnement implements Drawable{
    /*Classe symbolisant un Environnement*/
    //Ici, notre environnement se modélise par un espace 2D composant les limites de notre arene de combat

    Forme arene;
    List<Forme> obstacles;

    public Environnement(Forme arene) {
        this.arene = arene;
        obstacles = new ArrayList<Forme>();
    }

    public void ajouterObstacles(Forme ... obs){
        obstacles.addAll(Arrays.asList(obs));
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0));
        g2d.drawRect(0, 0, (int) this.arene.getWidth(),(int) this.arene.getHeight());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

import java.util.*;
import java.awt.*;
import java.util.List;

public class Environnement implements Drawable{
    /*Classe symbolisant un Environnement*/
    //Ici, notre environnement se modélise par un espace 2D composant les limites de notre arene de combat

    private Forme arene;
    private List<Forme> obstacles;

    private ArrayList<Orc> orcs;

    public Environnement(Forme arene) {
        this.arene = arene;
        this.orcs = new ArrayList<Orc>();
        obstacles = new ArrayList<Forme>();
    }

    public void addOrc(Orc o) {
        orcs.add(o);
    }

    public ArrayList<Orc> getOrcs() {
        return orcs;
    }

    public void ajouterObstacles(Forme ... obs){
        obstacles.addAll(Arrays.asList(obs));
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0));
        g2d.drawRect(0, 0, (int) this.arene.getWidth(),(int) this.arene.getHeight());
    }

    public double getWidth() {
        return arene.getWidth();
    }

    public double getHeight() {
        return arene.getHeight();
    }

    public boolean isIn(double x, double y) {
        return arene.estContennu(x, y);
    }

    public Orc getClosestOrc(Orc o) {
        Orc minO = null;
        double minDist = arene.getWidth()+arene.getHeight();
        minDist *= minDist;
        double distance;
        for (Orc oi :
                orcs) {
            distance = o.getSqrDistanceTo(oi);
            if (distance < minDist && oi != o) {
                minO = oi;
                minDist = distance;
            }
        }
        return minO;
    }
    
    public Orc collidingOrc(Orc o) {
        for (Orc oi :
                orcs) {
            if (o.isIn(oi) && o != oi) {
                return oi;
            }
        }
        return null;
    }
}

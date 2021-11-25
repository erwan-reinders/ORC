import MathClass.Vec2;

import java.awt.*;
import java.util.List;

public abstract class Forme {
    /*classe abstraite modélisant une forme dans un espace 2D*/
    private Vec2 center;

    public Forme(Vec2 c){
        this.center = c;
    }

    public Forme(){
        this(new Vec2(0,0));
    }

    //Fonction permettant d'indiquer si la forme contient la position (x,y)
    public abstract boolean estContennu(double x, double y);

    public boolean estContennu(Vec2 p){
        return estContennu(p.x,p.y);
    }

    //Fonction permettant d'obtenir la distance maximum que l'on peut parcourri dans la forme en question
    public abstract double getMaxDistance();

    //Fonction permettant de renseigner la largeur d'une forme
    public abstract double getWidth();

    //Fonction permettant de renseigner la hauteur d'une forme
    public abstract double getHeight();

    //Fonction permettant de grossir la forme
    public abstract void grossir(double ratio);

    public Vec2 getCenter() {
        return center;
    }

    public void setCenter(Vec2 center) {
        this.center = center;
    }

    //Fonction permettant d'indiquer comment dessiner une forme
    public abstract void draw(boolean fill,Graphics2D g2d, Color ... colors);

    //Fonction permettant d'indiquer pour une forme ses points clés
    public abstract List<Vec2> getRepresentativePoint();

    @Override
    public String toString() {
        return "Forme{" +
                "center=" + center +
                '}';
    }
}

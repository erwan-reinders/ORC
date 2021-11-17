import MathClass.Vec2;

import java.awt.*;

public class Cercle extends Forme {

    private double rayon;

    public Cercle(double x, double y,double r) {
        this(new Vec2(x,y),r);
    }

    public Cercle(Vec2 pos,double r) {
        super(pos);
        this.rayon = r;
    }

    public boolean estContennu(double x, double y){
        return ((x-this.getCenter().x)*(x-this.getCenter().x) + (y-this.getCenter().y)*(y-this.getCenter().y))<=(this.rayon*this.rayon);
    }

    public double getMaxDistance() {
        return rayon*2;
    }

    public double getHeight() {
        return rayon*2;
    }

    public double getWidth() {
        return rayon*2;
    }

    public void grossir(double ratio) {
        this.rayon*=ratio;
    }

    public void draw(boolean fill,Graphics2D g2d, Color ... colors) {
        //Dessiner un rectangle
        if(colors==null) {
            colors = new Color[]{ new Color(34,34,34)};
        }
        g2d.setColor(colors[0]);
        if(fill) {
            g2d.fillOval((int)(this.getCenter().x-rayon), (int)(this.getCenter().y-rayon), (int)(this.rayon*2), (int)(this.rayon*2));
        }else{
            g2d.drawOval((int)(this.getCenter().x-rayon), (int)(this.getCenter().y-rayon), (int)(this.rayon*2), (int)(this.rayon*2));
        }
    }

    @Override
    public String toString() {
        return  super.toString()+ "Cercle{" +
                "rayon=" + rayon +
                '}';
    }
}

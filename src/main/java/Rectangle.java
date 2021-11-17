import MathClass.Vec2;

import java.awt.*;

public class Rectangle extends Forme {
    private double width;
    private double height;

    public Rectangle(double x, double y, double width, double height) {
        super(new Vec2(x+width/2,y+height/2));
        this.width = width;
        this.height = height;
    }

    //Construction d'un Rectangle à partir de ses coins HautGauche et BasDroit
    public Rectangle(Vec2 pointHG, Vec2 pointBD) {
        super(new Vec2(pointHG.x + Vec2.largeur(pointHG,pointBD)/2,pointHG.y + Vec2.hauteur(pointHG,pointBD)/2));
        this.width = Vec2.largeur(pointHG,pointBD);
        this.height = Vec2.hauteur(pointHG,pointBD);
    }

    public boolean estContennu(double x, double y){
        Vec2 pointBD = new Vec2(getCenter().x+width/2,getCenter().y-height/2);
        Vec2 pointHG = new Vec2(getCenter().x-width/2,getCenter().y+height/2);

        //System.out.println("RECTANGLE : " + pointHG + "," + pointBD);

        return (x<=pointBD.x && x>=pointHG.x) && (y >= pointBD.y && y <= pointHG.y);
    }

    public double getMaxDistance() {
        return Math.max(width,height);
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void grossir(double ratio) {
        this.width*=ratio;
        this.height*=ratio;
    }

    @Override
    public void draw(boolean fill,Graphics2D g2d, Color ... colors) {
        Vec2 pointHG = new Vec2(getCenter().x-width/2,getCenter().y-height/2);

        if(colors==null) {
            colors = new Color[]{new Color(34,34,34)};
        }
        g2d.setColor(colors[0]);
        if(fill) {
            g2d.fillRect((int)pointHG.x, (int)pointHG.y, (int)this.width, (int)this.height);
        }else{
            g2d.drawRect((int)pointHG.x, (int)pointHG.y, (int)this.width, (int)this.height);
        }
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}

import MathClass.Vec2;


import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Triangle extends Forme {

    private Vec2 A;
    private Vec2 B;
    private Vec2 C;

    private double AB;
    private double AC;
    private double BC;

    private double p;
    private double aire;

    public Triangle(Vec2 a,Vec2 b,Vec2 c) {
        super(new Vec2((a.x+b.x+c.x)/3,(a.y+b.y+c.y)/3));
        A = a;
        B = b;
        C = c;

        AB = Math.sqrt(Vec2.getSqrDistanceTo(A,B));
        AC = Math.sqrt(Vec2.getSqrDistanceTo(A,C));
        BC = Math.sqrt(Vec2.getSqrDistanceTo(B,C));
        p = AB+AC+BC;

        double dp = p/2;
        double hA = 2/BC * Math.sqrt(dp*(dp - BC)*(dp-AB)*(dp-AC));
        aire = BC * hA/2;
    }

    public Triangle(Triangle t) {
        super(new Vec2((t.A.x+t.B.x+t.C.x)/3,(t.A.y+t.B.y+t.C.y)/3));
        A = new Vec2(t.A);
        B = new Vec2(t.B);
        C = new Vec2(t.C);

        AB = Math.sqrt(Vec2.getSqrDistanceTo(A,B));
        AC = Math.sqrt(Vec2.getSqrDistanceTo(A,C));
        BC = Math.sqrt(Vec2.getSqrDistanceTo(B,C));
        p = AB+AC+BC;

        double dp = p/2;
        double hA = 2/BC * Math.sqrt(dp*(dp - BC)*(dp-AB)*(dp-AC));
        aire = BC * hA/2;
    }

    public boolean estContennu(double x, double y){
        Vec2 M = new Vec2(x,y);
        double MA = Math.sqrt(Vec2.getSqrDistanceTo(M,A));
        double MB = Math.sqrt(Vec2.getSqrDistanceTo(M,B));
        double MC = Math.sqrt(Vec2.getSqrDistanceTo(M,C));
        double M_ABC = (MA+MB+MC);
        return ((p/2)<=M_ABC) && (M_ABC<=(p-Math.min(Math.min(AB,AC),BC)));
    }

    public double getMaxDistance() {
        return Math.max(AB,Math.max(AC,BC));
    }

    public double getHeight() {
        double dp = p/2;
        double hA = 2/BC * Math.sqrt(dp*(dp - BC)*(dp-AB)*(dp-AC));
        double hB = 2/AC * Math.sqrt(dp*(dp - BC)*(dp-AB)*(dp-AC));
        double hC = 2/AB * Math.sqrt(dp*(dp - BC)*(dp-AB)*(dp-AC));
        return Math.max(hA,Math.max(hB,hC));
    }

    public double getWidth() {
        return getHeight();
    }

    public void grossir(double ratio) {
        Vec2 CA = new Vec2(getCenter().x-A.x,getCenter().y-A.y);
        Vec2 CB = new Vec2(getCenter().x-B.x,getCenter().y-B.y);
        Vec2 CC = new Vec2(getCenter().x-C.x,getCenter().y-C.y);

        CA.multByScal(ratio);
        CB.multByScal(ratio);
        CC.multByScal(ratio);

        A = new Vec2(getCenter().x+CA.x,getCenter().y+CA.y);
        B = new Vec2(getCenter().x+CB.x,getCenter().y+CB.y);
        C = new Vec2(getCenter().x+CC.x,getCenter().y+CC.y);
    }

    public void draw(boolean fill,Graphics2D g2d, Color ... colors) {
        //Dessiner un rectangle
        if(colors==null) {
            colors = new Color[]{ new Color(34,34,34)};
        }
        g2d.setColor(colors[0]);
        if(fill) {
            g2d.fillPolygon(new int[]{(int)A.x,(int)B.x,(int)C.x},new int[]{(int)A.y,(int)B.y,(int)C.y},3);
        }else{
            g2d.drawPolygon(new int[]{(int)A.x,(int)B.x,(int)C.x},new int[]{(int)A.y,(int)B.y,(int)C.y},3);
        }
    }

    @Override
    public List<Vec2> getRepresentativePoint() {
        return new ArrayList<Vec2>(Arrays.asList(A,B,C));
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "A=" + A +
                ",B=" + B +
                ",C=" + C +
                ",PERIMETRE=" + p +
                ",AIRE=" + aire +
                '}';
    }

    public double getAire() {
        return aire;
    }
}

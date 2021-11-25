package MathClass;


public class Vec2 {
    public double x;
    public double y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(double x) {
        this(x, 0.0);
    }

    public Vec2() {
        this(0.0, 0.0);
    }

    public Vec2(Vec2 v) {
        this(v.x, v.y);
    }

    public double length() {
        return Math.sqrt(x*x + y*y);
    }

    public double squareLength() {
        return x*x + y*y;
    }

    public void add(Vec2 v) {
        x += v.x;
        y += v.y;
    }

    public void mult(Vec2 v) {
        x *= v.x;
        y *= v.y;
    }

    public double dot(Vec2 v) {
        return x * v.x + y * v.y;
    }

    public void normalize() {
        double len = length();
        x /= len;
        y /= len;
    }

    public boolean isNull(){
        return x+y == 0.0;
    }

    public static double largeur(Vec2 v1,Vec2 v2){
        return v1.x-v2.x;
    }

    public static double hauteur(Vec2 v1,Vec2 v2){
        return v1.y-v2.y;
    }

    public static double getSqrDistanceTo(Vec2 v1,Vec2 v2) {
        double dx = Vec2.largeur(v1,v2);
        double dy = Vec2.hauteur(v1,v2);
        return (dx*dx + dy*dy);
    }

    //Méthode permettant de récupérer le vecteur normalisé partant de v1 et oposé à v2
    //Utile pour la SDD_Separation
    public static Vec2 getOposite(Vec2 v1,Vec2 v2){
        //v : vecteur v2->v1 en position v1
        Vec2 v = new Vec2((v1.x-v2.x)*2, (v1.y-v2.y)*2);
        System.out.println("GETOP : " + v);
        v.normalize();
        System.out.println("GETOPNORM : " + v);
        return v;
    }

    public void multByScal(double d){
        this.x *=d;
        this.y *=d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2 vec2 = (Vec2) o;
        return vec2.x == x && vec2.y == y;
    }

    @Override
    public String toString() {
        return "Vec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

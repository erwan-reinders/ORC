import MathClass.Vec2;


public class Trace{
    /*Classe modélisant une trace laissée par un agent sur l'environnement*/
    private int dureeDeVie;
    private final Vec2 position;
    private Trace parent;

    //public boolean priseEnCompte = false;

    private static int ddv_max = 500;

    public Trace(int ddv, Vec2 pos){
        position = pos;
        dureeDeVie = ddv;
    }

    public Trace(Vec2 pos){
        this(ddv_max,pos);
    }

    public Trace(Trace t){
        this(t.getDureeDeVie(),t.getPosition());
        this.setParent(t.getParent());
    }

    public void setParent(Trace parent) {
        this.parent = parent;
    }

    public Trace getParent() {
        return parent;
    }

    public void update(){
        dureeDeVie --;
    }

    public boolean isDead(){
        return dureeDeVie<=0;
    }

    public int getDureeDeVie() {
        return dureeDeVie;
    }

    public Vec2 getPosition() {
        return position;
    }

    public static int getDdv_max() {
        return ddv_max;
    }

    public void setDureeDeVie(int dureeDeVie) {
        this.dureeDeVie = dureeDeVie;
    }

    @Override
    public String toString() {
        return "Trace{" +
                "dureeDeVie=" + dureeDeVie +
                ", position=" + position +
                ", parent=" + parent +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trace trace = (Trace) o;
        return position.equals(trace.position);
    }
}


public abstract class Forme {
    /*classe abstraite modélisant une forme dans un espace 2D*/
    private double width;
    private double height;

    public Forme(double w, double h){
        this.width = w;
        this.height = h;
    }

    //Fonction permettant d'indiquer si la forme contient la position (x,y)
    public abstract boolean estContennu(double x, double y);

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}

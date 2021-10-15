public class Rectangle extends Forme {
    //Positions des coins définissant un rectangle
    private double x_coinHG;
    private double y_coinHG;
    private double x_coinBD;
    private double y_coinBD;

    public Rectangle(double x_coinHG, double y_coinHG, double x_coinBD, double y_coinBD) {
        super(x_coinBD - x_coinHG, y_coinHG - y_coinBD);
        this.x_coinHG = x_coinHG;
        this.y_coinHG = y_coinHG;
        this.x_coinBD = x_coinBD;
        this.y_coinBD = y_coinBD;
    }

    public boolean estContennu(double x, double y){
        return (x<=x_coinBD && x>=x_coinHG) && (y >= y_coinBD && y <= y_coinHG);
    }
}

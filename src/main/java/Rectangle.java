public class Rectangle extends Forme {
    //Positions des coins définissant un rectangle
    private double x_coinHG;
    private double y_coinHG;
    private double x_coinBD;
    private double y_coinBD;

    public Rectangle(double x, double y, double width, double height) {
        super(width, height);
        this.x_coinHG = x;
        this.y_coinHG = y;
        this.x_coinBD = x + width;
        this.y_coinBD = y + height;
    }

    public boolean estContennu(double x, double y){
        return (x<=x_coinBD && x>=x_coinHG) && (y >= y_coinBD && y <= y_coinHG);
    }
}

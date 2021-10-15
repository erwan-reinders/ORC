public class Rectangle implements Forme {
    //Positions des coins définissant un rectangle
    private double x_coinHG;
    private double y_coinHG;
    private double x_coinBD;
    private double y_coinBD;

    public boolean estContennu(double x, double y){
        return (x<=x_coinBD && x>=x_coinHG) && (y >= y_coinBD && y <= y_coinHG);
    }
}

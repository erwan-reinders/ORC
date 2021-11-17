import java.awt.*;

public class Obstacle implements Element {
    /*Classe modélisant un obstacle*/
    private Forme aspect;
    private typeMateriaux materiel;
    private double hauteur;

    public enum typeMateriaux{
        PIERRE(1,-1,new Color(88,41,0,255),true),EAU(0,4,new Color(3,34,76,120),true),FEUILLAGE(.5,2, new Color(0,80,10,200),true);
        //CoefTrans : permet de définir ce vers quoi on peut voir à travers ou non [0,1]
        //CoefPassage : permet de définir le coefficient de ralentissement d'un individus qui doit passer par l'obstacle
        //Négatif : intraverssable
        private double coefTrans, coefPassage;
        private Color visuel;
        private boolean fill;

        typeMateriaux(double durete, double passage, Color v, boolean f){
            this.coefTrans = durete;
            coefPassage = passage;
            this.visuel = v;
            fill = f;
        }

        double getCoefTrans(){return coefTrans;}

        double getCoefPassage(){return coefPassage;}

        public Color getVisuel() {
            return visuel;
        }

        public boolean isFill() {
            return fill;
        }
    }

    public Obstacle(typeMateriaux mat,Forme aspect,double hauteur){
        this.aspect = aspect;
        this.materiel = mat;
        this.hauteur = hauteur;
    }

    public Obstacle(typeMateriaux mat,Forme aspect){
        this(mat,aspect,1);
    }

    public boolean estContennu(double x, double y){
        return aspect.estContennu(x,y);
    }

    public void draw(Graphics2D g2d) {
        aspect.draw(materiel.isFill(),g2d, materiel.getVisuel());
    }

    public Forme getAspect() {
        return aspect;
    }

    public typeMateriaux getMateriel() {
        return materiel;
    }

    public double getHauteur() {
        return hauteur;
    }

    @Override
    public String toString() {
        return "Obstacle{" +
                "aspect=" + aspect +
                ", materiel=" + materiel +
                ", hauteur=" + hauteur +
                '}';
    }
}

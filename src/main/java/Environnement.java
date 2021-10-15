import java.awt.*;

public class Environnement implements Drawable{
    /*Classe symbolisant un Environnement*/
    //Ici, notre environnement se modélise par un espace 2D composant les limites de notre arene de combat
    private int width;
    private int height;

    public Environnement(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0));
        g2d.drawRect(0, 0, width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

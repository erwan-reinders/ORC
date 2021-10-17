import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

public class Action_Avancer implements Action<Orc> {
    /*Classe modélisant l'action d'avancer*/

    private Environnement env;
    private double posDepX;
    private double posDepY;

    //Constructeur
    public Action_Avancer() {

    }

    public Action_Avancer(Environnement env, double x, double y){
        this.env = env;
        posDepX = x;
        posDepY = y;
    }

    public void executer(Orc o){
        //Ici, on doit opérer un déplacement
        o.move(posDepX, posDepY);
    }

    public boolean estExecutable(Orc o){
        return false;
    }

    public void setEnv(Environnement env) {
        this.env = env;
    }

    public void setPosDepX(double posDepX) {
        this.posDepX = posDepX;
    }

    public void setPosDepY(double posDepY) {
        this.posDepY = posDepY;
    }
}

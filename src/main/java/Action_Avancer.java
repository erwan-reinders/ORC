//import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

public class Action_Avancer implements Action<Orc> {
    /*Classe modélisant l'action d'avancer*/

    private Environnement env;
    private double posDepX;
    private double posDepY;

    //Constructeur
    public Action_Avancer() {}

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
        double x = o.getX() + posDepX;
        double y = o.getY() + posDepY;

        return env.isIn(x,y);

        /*
        if(!env.getArene().estContennu(x,y)) return false;
        for (Forme f: env.getObstacles()) {
            if(f.estContennu(x, y)) return false;
        }
        return true;

        //REFACTORING
        Orc closest = this.env.getClosestOrc(o);

        if (closest == null) {
            return false;
        }
        if (!o.isAffraid()) {
            double cX, cY;
            cX = o.getX() - closest.getX();
            cY = o.getY() - closest.getY();

            double norm = Math.sqrt(o.getSqrDistanceTo(closest));
            cX /= norm;
            cY /= norm;

            if (env.isIn(x + cX, y + cY)) {
                this.posDepX = cX;
                this.posDepY = cY;
            } else {
                this.posDepX = .0;
                this.posDepY = .0;
            }
            return true;
        }
        else if (getSqrDistanceTo(closest) <= 4 * size * size) {
            Action_Attaquer choix = (Action_Attaquer) getAction("attaquer");
            choix.setEnv(env);
            choix.setTarget(closest);
            choixFinal = choix;
        } else {
            Action_Avancer choix = (Action_Avancer) getAction("avancer");
            choix.setEnv(env);
            double cX, cY;
            cX = closest.x - this.x;
            cY = closest.y - this.y;
            if (isIn(closest)) {
                cX = -cX;
                cY = -cY;
            }
            else {
                double norm = Math.sqrt(getSqrDistanceTo(closest));
                cX /= norm;
                cY /= norm;
            }
            if (env.isIn(x + cX, y + cY)) {
                choix.setPosDepX(cX);
                choix.setPosDepY(cY);
            } else {
                choix.setPosDepX(0.0);
                choix.setPosDepY(0.0);
            }
            choixFinal = choix;
        }*/
    }

    public int getCout(){
        return 1;
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

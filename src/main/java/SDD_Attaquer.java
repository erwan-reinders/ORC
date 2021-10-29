import Math.Vec2;

public class SDD_Attaquer implements StrategieDeDeplacement {
    /*Classe modélisant une stratégie de déplacement basée sur l'attaque d'un adversaire*/
    private Environnement env;
    private Orc orc;

    public SDD_Attaquer(Environnement env, Orc orc){
        this.env= env;
        this.orc = orc;
    }

    public Vec2 getProchainePosition(){

        /*if(!env.getArene().estContennu(x,y)) return false;
        for (Forme f: env.getObstacles()) {
            if(f.estContennu(x, y)) return false;
        }
        return true;


        //REFACTORING
        Orc closest = this.env.getClosestOrc(orc);

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

        return null;
    }
}

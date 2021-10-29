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
*/
        Vec2 newPos = new Vec2();
        Orc closest = this.env.getClosestOrc(orc);

        if (closest == null) {
            return newPos;
        }
        if (!orc.isAffraid()) {
            double cX, cY;
            cX = orc.getX() - closest.getX();
            cY = orc.getY() - closest.getY();

            double norm = Math.sqrt(orc.getSqrDistanceTo(closest));
            cX /= norm;
            cY /= norm;

            if (env.isIn(orc.getX() + cX, orc.getY() + cY)) {
                newPos.x = cX;
                newPos.y = cY;
            }
        }/*
        else if (orc.estAPorte(closest)) {
            Action_Attaquer choix = (Action_Attaquer) getAction("attaquer");
            choix.setEnv(env);
            choix.setTarget(closest);
            choixFinal = choix;
        }*/ else {
            /*Action_Avancer choix = (Action_Avancer) getAction("avancer");
            choix.setEnv(env);*/
            double cX, cY;
            cX = closest.getX() - orc.getX();
            cY = closest.getY() - orc.getY();
            if (orc.isIn(closest)) {
                cX = -cX;
                cY = -cY;
            }
            else {
                double norm = Math.sqrt(orc.getSqrDistanceTo(closest));
                cX /= norm;
                cY /= norm;
            }
            if (env.isIn(orc.getX() + cX, orc.getY() + cY)) {
                newPos.x = cX;
                newPos.y = cY;
            }
        }
        return newPos;
    }


}

import MathClass.Vec2;

import java.util.Set;

public class SDD_Attaquer implements StrategieDeDeplacement {
    /*Classe modélisant une stratégie de déplacement basée sur l'attaque d'un adversaire*/
    private Orc orc;
    private Environnement env;

    private Orc target;

    public int precision = 40;

    public SDD_Attaquer(Orc orc, Environnement env){
        this.orc = orc;
        this.env = env;
    }

    public Vec2 getProchainePosition() {
        Vec2 newPos = new Vec2();

        Orc closest;
        if (target != null && target.isAlive()){
            closest = target;
        }else{
            Orc plusProche = env.getClosestEnnemiOrc(orc);
            closest = plusProche;
            target = plusProche;
        }
        //System.out.println("SDD ATTAQUER CLOSEST");
        //System.out.println(closest);

        if (closest == null) {
            return newPos;
        }
        //On doit également MAJ la cible de l'attaque si attaque il y a
        Action_Attaquer ao = (Action_Attaquer) orc.getAction("attaquer");
        if(ao != null) {
            //System.out.println("ON SET LA TARGET DATTAQUE");
            ao.setTarget(closest);
        }

        if (!orc.isAffraid() && !orc.estAPorte(closest)) {
            double cX, cY;
            //Coordonnées du orc->closest
            cX = closest.getX() - orc.getX();
            cY = closest.getY() - orc.getY();

            /*double norm = Vec2.getSqrDistanceTo(orc.getPosition(),closest.getPosition());
            cX /= norm;
            cY /= norm;*/
            Vec2 dep = new Vec2(cX,cY);
            dep.normalize();

            //System.out.println("nouveau CX : " + cX);
            //System.out.println("nouveau CY : " + cY);
            //System.out.println("normalize : " + dep);

            if(env.isIn(orc.getX() + dep.x, orc.getY() + dep.y,orc)) {
                double r = env.getData().getRalentissement(orc.getPosition());
                newPos.x = dep.x*r;
                newPos.y = dep.y*r;
            }
        }
        //System.out.println("SDD ATTAQUER NOUVELLE POSITIONS : " + newPos);
        return newPos;
    }

    public boolean estTermine() {
        //Si l'orc n'est pas effrayé
        if(!orc.isAffraid()) {
            //S'il n'y a plus d'ennemis dans la zone de l'orc
            //Set<Element> els = orc.getCdv().getAllElementInCDV(precision);
            Set<Element> els = orc.getCdv().getAllElementInCDV_triangles(precision);
            for (Element el : els) {
                if (el instanceof Orc) {
                    if (!orc.getEquipe().isAllie(((Orc) el).getEquipe())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public StrategieDeDeplacement getProchaineStrategie(Environnement env) {
        if(orc.isAffraid()){
            return new SDD_Fuir(orc,env);
        }else{
            return new SDD_Exploration(orc,env);
        }
    }

    public void setTarget(Orc target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "SDD_Attaquer{}";
    }
}

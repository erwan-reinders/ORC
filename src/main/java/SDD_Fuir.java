import MathClass.Vec2;

public class SDD_Fuir implements StrategieDeDeplacement{
    /*Classe modélisant une stratégie de déplacement basée sur la fuite d'un adversaire*/
    private Orc menace;
    private Orc orc;
    private Environnement env;

    public int nb_allie = 3;

    public SDD_Fuir(Orc orc, Environnement env) {
        this.menace = null;
        this.orc = orc;
        this.env = env;
    }

    public Vec2 getProchainePosition() {
        menace = env.getClosestEnnemiOrc(orc);

        //System.out.println("SDD FUIR");

        Vec2 newP = new Vec2();

        if(menace!=null){
            double cX, cY;
            //Coordonnées du vecteur menace->orc
            cX = orc.getX() - menace.getX();
            cY = orc.getY() - menace.getY();


            double norm = Math.sqrt(Vec2.getSqrDistanceTo(new Vec2(orc.getPosition()), new Vec2(menace.getPosition())));
            cX /= norm;
            cY /= norm;

            //System.out.println("menace : " + menace);
            //System.out.println("orc peureux : " + orc);
            //System.out.println("CX : " + cX);
            //System.out.println("CY : " + cY);

            if (env.isIn(orc.getX() + cX, orc.getY() + cY,orc)) {
                newP.x = cX;
                newP.y = cY;
            }
        }
        //System.out.println("SDD FUIR DEP : " + newP);
        return newP;
    }

    public boolean estTermine() {
        //Si l'orc est appeuré
        if(orc.isAffraid()){
            int cpt = 0;
            for (Orc o: orc.getCdv().getAllOrcInSimpleCDV()) {
                if(o.estAllie(orc)){
                    cpt++;
                }
            }
            //S'il a plus de nb_allie à ses alentours, il n'a plus peur
            return cpt >= nb_allie;
        }
        return true;
    }

    public StrategieDeDeplacement getProchaineStrategie(Environnement env) {
        return new SDD_Exploration(orc,env);
    }

    @Override
    public String toString() {
        return "SDD_Fuir{}";
    }
}

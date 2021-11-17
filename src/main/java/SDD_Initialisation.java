import MathClass.Vec2;

public class SDD_Initialisation implements StrategieDeDeplacement {
    /*Classe modélisant une stratégie de déplacement initiale*/
    private Orc orc;

    public SDD_Initialisation(Orc o){
        this.orc = o;
    }

    public Vec2 getProchainePosition() {
        return new Vec2();
    }

    public boolean estTermine(){
        return true;
    }

    public StrategieDeDeplacement getProchaineStrategie(Environnement env) {
        return new SDD_Separation(orc,env);
    }

    @Override
    public String toString() {
        return "SDD_Initialisation{}";
    }
}

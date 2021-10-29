import MathClass.Vec2;

public class Action_Avancer implements Action<Orc> {
    /*Classe modélisant l'action d'avancer*/

    private Vec2 vec;
    private StrategieDeDeplacement SDD;

    //Constructeur
    public Action_Avancer() {}

    public Action_Avancer(StrategieDeDeplacement str){
        this.SDD = str;
        this.vec = new Vec2();
    }

    public void executer(Orc o){
        //Ici, on doit opérer un déplacement
        o.move(this.vec);
    }

    public boolean estExecutable(Orc o){
        Vec2 v = this.SDD.getProchainePosition(null);
        double x = o.getX() + v.x;
        double y = o.getY() + v.y;
        return /*env.isIn(x,y);*/ false;
    }

    public double getCout(){
        return .1;
    }

    /*
    public void setEnv(Environnement env) {
        this.env = env;
    }*/
}

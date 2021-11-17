import MathClass.Vec2;

public class Action_Avancer implements Action<Orc> {
    /*Classe modélisant l'action d'avancer*/

    private Vec2 vec;

    //Constructeur
    public Action_Avancer() {this.vec = new Vec2();}

    public void executer(Orc o){
        //Ici, on doit opérer un déplacement
        o.move(this.vec);
    }

    public boolean estExecutable(Orc o){
        //SDD renvoie par défaut un déplacement valide
        //System.out.println("TEST AVANCER");
        //System.out.println(o);
        //System.out.println(o.getSDD());
        this.vec = o.getSDD().getProchainePosition();
        //System.out.println("POS DEP : " + this.vec);
        //System.out.println("ON " + ((!vec.isNull())? "PEUT AVANCER" : "PEUT PAS AVANCER"));
        //Si le déplacement ne déplace pas le sujet, c'est qu'il n'y a pas de déplacement possible
        return (!vec.isNull());
    }

    public double getCout(){
        return .1;
    }

    @Override
    public String toString() {
        return "Action_Avancer{" +
                "vec=" + vec +
                '}';
    }
}

public class Action_Attaquer implements Action<Orc> {
    /*Classe modélisant l'action d'avancer*/

    private Environnement env;
    private Orc target;

    //Constructeur
    public Action_Attaquer() {

    }

    public Action_Attaquer(Environnement env, Orc target){
        this.env = env;
        this.target = target;
    }

    public void executer(Orc o){
        //Ici, on doit opérer un déplacement
        target.loseHealth(Math.random()*2);
    }

    public boolean estExecutable(Orc o){
        return true;
    }

    public void setEnv(Environnement env) {
        this.env = env;
    }

    public void setTarget(Orc target) {
        this.target = target;
    }
}

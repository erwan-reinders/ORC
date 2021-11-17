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
        //Ici, on doit opérer une attaque
        double at = o.getPuissanceAttaque();
        //System.out.println("ATTAQUE DE " + at);
        target.loseHealth(at);
    }

    public boolean estExecutable(Orc o){
        //System.out.println("TEST ATTAQUER : " + ((target != null)?((o.estAPorte(target))? "BON":"PAS BON") : "PAS BON (null)"));
        //On va se demander si un orc o peut attaquer un orc target
        //System.out.println("PEUT ON ATTAQUER : " + target + " => " + o.estAPorte(target));
        if(target != null && target.isAlive()){
            return o.estAPorte(target);
        }
        return false;
    }

    public double getCout(){
        return .9;
    }

    public void setEnv(Environnement env) {
        this.env = env;
    }

    public void setTarget(Orc target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "Action_Attaquer{" +
                "target=" + target +
                '}';
    }
}



public class Action_NeRienFaire implements Action<Orc> {
    /*Classe modélisant l'action de ne rien faire*/

    public Action_NeRienFaire(){}

    public void executer(Orc a) {}

    public boolean estExecutable(Orc a) {
        //System.out.println("TEST NRF");
        return true;
    }

    public double getCout() {
        return 1.;
    }

    @Override
    public String toString() {
        return "Action_NeRienFaire";
    }
}


public interface Agent {
    /*Interface symbolisant un Agent*/

    //Fonction prendre decision
    public void prendreDesision(Environnement env);

    //Fonction permettant d'exécuter l'action
    public abstract void executerDesision();
}

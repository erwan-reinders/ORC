
public interface Agent extends Element{
    /*Interface symbolisant un Agent*/

    //Fonction prendre decision
    public void prendreDesision(Environnement env);

    //Fonction permettant d'exécuter l'action
    public abstract void executerDesision();

    //Fonction permettant de récupérer l'apparence d'un agent
    Forme getApparence();
}

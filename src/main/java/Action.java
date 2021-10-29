public interface Action<T extends Agent>{
    /*Interface symbolisant une Action*/

    //Fonction permettant d'exécuter une action par rapport à ses paramètres passés à la construction
    void executer(T a);

    //Fonction qui modélise les préconditons d'une actioniç
    boolean estExecutable(T a);

    //Fonction qui modélise le cout d'une action (heuristique)
    double getCout();
}

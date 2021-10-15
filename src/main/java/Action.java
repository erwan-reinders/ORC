public interface Action<T extends Agent>{
    /*Interface symbolisant une Action*/

    //Fonction permettant d'exécuter une action par rapport à ses paramètres passés à la construction
    public void executer(T a);

    //Fonction qui modélise les préconditons d'une actioniç
    public boolean estExecutable(T a);
}

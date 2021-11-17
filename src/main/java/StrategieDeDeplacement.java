import MathClass.Vec2;

public interface StrategieDeDeplacement {
    /*Interface modélisant une stratégie de déplacement*/

    /*Fonction permettant de récupérer une position de déplacement possible par rapport à la stratégie*/
    Vec2 getProchainePosition();

    /*Fonction permettant de déterminer si la stratégie de déplacement est finie*/
    boolean estTermine();

    /*Fonction permettant de récupérer la prochaine stratégie de déplacment à appliquer*/
    StrategieDeDeplacement getProchaineStrategie(Environnement env);
}

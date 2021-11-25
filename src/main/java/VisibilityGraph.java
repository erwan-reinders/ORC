import MathClass.Vec2;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class VisibilityGraph {
/*Classe modélisant un graphe de visibilité d'un espace 2D*/
    private List<Obstacle> obstacles;
    private List<Node_VG> noeuds;


    class Pair<L,R> {
        private L first;
        private R second;
        public Pair(L l, R r){
            this.first = l;
            this.second = r;
        }
        public L getL(){ return first; }
        public R getR(){ return second; }
        public void setL(L l){ this.first = l; }
        public void setR(R r){ this.second = r; }
    }

    class Node_VG {
        private Vec2 rep;
        List<Pair<Double, Vec2>> voisins;

        public Node_VG() {
            this(new Vec2());
            voisins = new ArrayList<Pair<Double, Vec2>>();
        }

        public Node_VG(Vec2 v) {
            this.rep = v;
        }

        public void addVoisin(Vec2 v, double dist){
            voisins.add(new Pair<Double, Vec2>(dist,v));
        }

        public boolean estVoisin(Node_VG n2){
            for (Pair<Double, Vec2> n: voisins) {
                if(n.second == n2.rep){
                    return true;
                }
            }
            return false;
        }

        public double getCoutChemin(Node_VG nv){
            for (Pair<Double, Vec2> n: voisins) {
                if(n.second == nv.rep){
                    return n.first;
                }
            }
            return -1;
        }
    }

    /*Constructeur d'un graphe de visibilité
    * @param points : liste des obstacles à prendre en compte*/
    public VisibilityGraph(Obstacle ... obs){
        obstacles = new ArrayList<Obstacle>();
        if(obs != null){
            obstacles.addAll(Arrays.asList(obs));
        }
        noeuds = new ArrayList<Node_VG>();
    }

    public void addObstacles(Obstacle ... obs){
        if(obs != null){
            obstacles.addAll(Arrays.asList(obs));
        }
    }

    /*Méthode permettant de construire VG*/
    public void construire_graphe(){
        //Pour cela, on va générer une liste de noeuds associée à ces points
        List<Vec2> representants = new ArrayList<Vec2>();
        //Pour cela, on va prendre chacun des points representatifs des obstacles
        for (Obstacle o: obstacles) {
            //Pour chaque représentant
            for (Vec2 v: o.getRepresentativePoint()) {
                representants.add(v);
                ajouterNoeud(v);
            }
        }

        for (int i = 0; i < representants.size(); i++) {
            for (int j = i; j < representants.size(); j++) {
                if(i!=j){
                    Vec2 vmax = posMaxAtteinteParRayon_VG(representants.get(i),representants.get(j));
                    if(vmax == representants.get(j)){
                        ajouterVoisin(representants.get(i),representants.get(j));
                    }
                }
            }
        }
    }

    private void ajouterNoeud(Vec2 v){
        for (Node_VG n: noeuds) {
            if(n.rep == v) return;
        }
        noeuds.add(new Node_VG(v));
    }

    private Node_VG ajouterNoeud_ret(Vec2 v){
        for (Node_VG n: noeuds) {
            if(n.rep == v) return n;
        }
        Node_VG node = new Node_VG(v);
        noeuds.add(node);
        return node;
    }

    private void ajouterVoisin(Vec2 n, Vec2 voisin){
        for (Node_VG noeud: noeuds) {
            if(noeud.rep == n) {
                for (Node_VG noeud_v: noeuds) {
                    if(noeud_v.rep == voisin){
                        double d = Vec2.getSqrDistanceTo(n,voisin);
                        noeud.addVoisin(voisin, d);
                        noeud_v.addVoisin(n, d);
                        return;
                    }
                }
                return;
            }
        }
    }

    private boolean enleverNoeud(Vec2 v){
        Iterator<Node_VG> it = noeuds.iterator();
        while (it.hasNext()){
            Node_VG n = it.next();
            if(n.rep == v){
                it.remove();
                return true;
            }
        }
        return false;
    }

    private boolean isIn_OBS(Vec2 v) {
        for(Obstacle o : obstacles){
            if(!o.peutPasserAuTravers() && o.estContennu(v.x,v.y)) {
                return false;
            }
        }
        return true;
    }

    public Vec2 posMaxAtteinteParRayon_VG(Vec2 depart, Vec2 arrive){
        if(!isIn_OBS(depart)){
            return new Vec2(depart);
        }

        Vec2 v = new Vec2(arrive.x-depart.x,arrive.y-depart.y);
        Vec2 v_pos = new Vec2();
        v_pos.x = (v.x<0)? -v.x : v.x;
        v_pos.y = (v.y<0)? -v.y : v.y;

        Vec2 arrive_pos = new Vec2(depart.x+v_pos.x,depart.y+v_pos.y);

        v.normalize();
        v_pos.normalize();

        Vec2 cur_p = new Vec2(depart.x+v.x,depart.y+v.y);
        Vec2 cur_p_pos = new Vec2(depart.x+v_pos.x,depart.y+v_pos.y);

        while(cur_p_pos.x <=arrive_pos.x && cur_p_pos.y <=arrive_pos.y && isIn_OBS(cur_p)) {
            cur_p.x += v.x;
            cur_p.y += v.y;

            cur_p_pos.x += v_pos.x;
            cur_p_pos.y += v_pos.y;
        }

        if(!isIn_OBS(cur_p)){
            cur_p.x-=v.x;
            cur_p.y-=v.y;

            return cur_p;
        }
        return new Vec2(arrive);
    }

    /*Fonction permettant de récupérer la meilleure position à prendre connaissant :
    * @param posDep : Vec2 position de départ du chemin (pos d'un agent)
    * @param posFin : Vec2 position objectif de l'agent*/
    public Vec2 getProchainePos(Vec2 posDep, Vec2 posFin){
        //On va parcourrir le graphe supposé construit
        if(posDep == posFin) return new Vec2();
        //On va devoir ajouter les deux noeuds contenant posDep et PosFin au graphe
        Node_VG n_dep = ajouterNoeud_ret(posDep);
        Node_VG n_arr = ajouterNoeud_ret(posFin);

        for (Node_VG n : noeuds) {
            Vec2 vmax_dep = posMaxAtteinteParRayon_VG(n_dep.rep,n.rep);
            if(vmax_dep == n.rep){
                ajouterVoisin(n_dep.rep,n.rep);
            }
            Vec2 vmax_arr = posMaxAtteinteParRayon_VG(n_arr.rep,n.rep);
            if(vmax_arr == n.rep){
                ajouterVoisin(n_arr.rep,n.rep);
            }
        }

        Vec2 v = recherche_dijstra(posDep,posFin);
        enleverNoeud(posDep);
        enleverNoeud(posFin);
        return v;
    }

    private Vec2 recherche_dijstra(Vec2 deb, Vec2 fin){
        Vec2 v = new Vec2();
        double dist[] = new double[noeuds.size()];
        boolean P[] = new boolean[noeuds.size()];
        int predecesseur[] = new int[noeuds.size()];

        int indice_deb = -1;

        for (int i = 0; i < noeuds.size(); i++) {
            if(noeuds.get(i).rep == deb){
                dist[i] = 0;
                indice_deb = i;
            }else{
                dist[i] = Double.MAX_VALUE;
            }
            P[i] = false;
        }

        boolean estFinie = false;
        while (!estFinie){
            int indice = -1;
            double val_min = Double.MAX_VALUE;
            for (int i = 0; i < noeuds.size(); i++) {
                if(!P[i] && val_min>dist[i]){
                    indice = i;
                    val_min = dist[i];
                }
            }
            if(indice>=0){
                P[indice] = true;
                for (int i = 0; i < noeuds.size(); i++) {
                    if(!P[i] && noeuds.get(indice).estVoisin(noeuds.get(i))){
                        if(dist[indice]>dist[i]){
                            if(dist[indice]>dist[i] + noeuds.get(indice).getCoutChemin(noeuds.get(i)))
                            dist[indice] = dist[i] + noeuds.get(indice).getCoutChemin(noeuds.get(i));
                            predecesseur[indice] = i;
                        }
                    }
                }
            }else{
                estFinie = true;
            }
        }

        //On recupère le bon sommet
        int indice_fin = -1;
        for (int i = 0; i < noeuds.size(); i++) {
            if (noeuds.get(i).rep == fin) {
                indice_fin = i;
            }
        }

        int pred_deb = -1;
        if(indice_fin>=0){
            while (indice_fin!=indice_deb){
                pred_deb = indice_fin;
                indice_fin = predecesseur[indice_fin];
            }
        }

        if(pred_deb>=0){
            Vec2 dest = noeuds.get(pred_deb).rep;
            Vec2 dep = new Vec2(dest.x - deb.x,dest.y - deb.y);
            deb.normalize();
            v.x = deb.x;
            v.y = deb.y;
        }
        return v;
    }
}

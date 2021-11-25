import MathClass.Vec2;

import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.util.*;
import java.util.List;

public class EquipeData<T extends Agent_Combat> implements Drawable {
    /*Classe modélisant les ressources partagées par une même équipe*/
    private List<EquipeData<T>> allies;
    private List<Trace> traces;
    private Color couleur;
    private TableauNoir<T> data_partagee;

    static class TableauNoir<T extends Agent_Combat> {
        /*Classe symbolisant un tableau noir, centralisant la connaissance des orcs, notament ceux en équipe*/
        private TreeSet<PaireT_int<T>> En_dejaVu;

        static final int maxPrio = 10;

        static class PaireT_int<T> implements Comparable<PaireT_int<T>>{
            T elem;
            int prio;
            PaireT_int(T first, int second){
                elem = first;
                prio = second;
            }
            public int compareTo(PaireT_int<T> o) {
                return o.prio - this.prio;
            }
        }

        public TableauNoir(){
            En_dejaVu = new TreeSet<PaireT_int<T>>();
        }

        public void update(){
            Iterator<PaireT_int<T>> it = En_dejaVu.iterator();
            while(it.hasNext()){
                PaireT_int<T> p = it.next();
                //Si une entité de combat n'est plus en vie, elle est sortie de la liste des ennemis vus
                if(!p.elem.isAlive()) it.remove();
            }
        }

        public void updatePrio(){
            Iterator<PaireT_int<T>> it = En_dejaVu.iterator();
            while(it.hasNext()){
                PaireT_int<T> p = it.next();

                //Si une entité de combat n'est plus en vie, elle est sortie de la liste des ennemis vus
                if(!p.elem.isAlive()) p.prio = maxPrio;
                p.prio++;
                if(p.prio == maxPrio) {
                    System.out.println("SUPRESSION DE L'ELEMENT DE LA LISTE : " + p.elem);
                    it.remove();
                }
            }
        }

        public T getFirstElem(){
            return En_dejaVu.isEmpty()? null : En_dejaVu.first().elem;
        }

        public void ajouterElemVu(T elem){
            updatePrio();
            PaireT_int<T> p = new PaireT_int<T>(elem,0);
            System.out.println("J'AI VU L'ENNEMI : " + elem);
            En_dejaVu.add(p);
        }

        public T getFirstPrio(){
            if(!En_dejaVu.isEmpty()){
                return En_dejaVu.first().elem;
            }
            return null;
        }
    }

    public EquipeData(Color couleur) {
        this.allies = new ArrayList<EquipeData<T>>();
        this.couleur = couleur;
        this.data_partagee = new TableauNoir<T>();
        traces = new ArrayList<Trace>();
    }

    public void ajouterAllie(EquipeData<T> eq) {
        if (!allies.contains(eq)) {
            this.allies.add(eq);
        }
    }

    public boolean isAllie(EquipeData<T> eq){
        return allies.contains(eq) || (this == eq);
    }

    public Trace ajouterTrace(Vec2 p,Trace prec){
        Trace t = new Trace(new Vec2(p));
        for (Trace tr : traces) {
            if (tr == t) {
                tr.setParent((prec == null)? null: new Trace(prec));
                tr.setDureeDeVie(Trace.getDdv_max());
                return tr;
            }
        }
        t.setParent((prec == null)? null: new Trace(prec));
        traces.add(t);
        return t;
    }

    public void update(){
        data_partagee.update();

        Iterator<Trace> it = traces.iterator();
        while(it.hasNext()){
            Trace t = it.next();
            t.update();
            if(t.isDead()) it.remove();
            //t.priseEnCompte = false;
        }
    }

    public void ajouterEnnemiVu(T o){
        data_partagee.ajouterElemVu(o);
    }

    public boolean ennemiAAttaquer(){
        return !data_partagee.En_dejaVu.isEmpty();
    }

    public T getACible(){
        return data_partagee.getFirstElem();
    }

    public Color getCouleur() {
        return couleur;
    }

    public TableauNoir<T> getData_partagee() {
        return data_partagee;
    }

    public int getNbTrace(Forme f){
        int cpt = 0;
        for (Trace t: traces) {
            if(f.estContennu(t.getPosition())) cpt++;
        }
        return cpt;
    }

    public int getNbTrace(){
        return traces.size();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipeData<?> that = (EquipeData<?>) o;
        return allies.equals(that.allies) &&
                couleur.equals(that.couleur) &&
                data_partagee.equals(that.data_partagee);
    }

    public void draw(Graphics2D g2d) {
        int width = 10;
        int height = 10;
        for(Trace t : traces){
            //System.out.println("DESSIN DE : " + t);
            double tier = (double)Trace.getDdv_max()/3;
            double doubletier = tier*2;

            if(t.getDureeDeVie()<tier){
                g2d.setColor(new Color((float)couleur.getRed()/255,(float)couleur.getGreen()/255,(float)couleur.getBlue()/255, .3f));
            }else{
                if(t.getDureeDeVie()<doubletier) {
                    g2d.setColor(new Color((float)couleur.getRed()/255, (float)couleur.getGreen()/255, (float)couleur.getBlue()/255, .5f));
                }else{
                    g2d.setColor(new Color((float)couleur.getRed()/255,(float)couleur.getGreen()/255,(float)couleur.getBlue()/255, .7f));
                }
            }
            g2d.fillOval((int)t.getPosition().x - width/2, (int)t.getPosition().y - height/2, width, height);
            /*if(t.priseEnCompte){
                g2d.setColor(Color.YELLOW);
                g2d.drawOval((int)t.getPosition().x - width/2, (int)t.getPosition().y - height/2, width, height);
            }*/
        }
    }

    @Override
    public String toString() {
        return "EquipeData{" +
                "allies=" + allies +
                ", traces=" + traces +
                ", couleur=" + couleur +
                ", data_partagee=" + data_partagee +
                '}';
    }
}


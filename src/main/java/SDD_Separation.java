import MathClass.Vec2;

public class SDD_Separation implements StrategieDeDeplacement {
    /*Classe modélisant une stratégie de déplacement basée sur l'isolation d'un agent par rapport à son environnement*/
    private Orc orc;
    private Environnement env;

    public SDD_Separation(Orc orc, Environnement env) {
        this.orc = orc;
        this.env = env;
    }

    public Vec2 getProchainePosition() {
        Vec2 newPos = new Vec2();
        Element el = env.elIsIn(orc);
        if(el !=null){
            System.out.println("EL pas nul : ");
            if(el instanceof Obstacle){
                System.out.println("OBSTACLE");
                newPos = Vec2.getOposite(orc.getPosition(),((Obstacle) el).getAspect().getCenter());
            }else {
                if (el instanceof Orc) {
                    System.out.println("ORC : " + el);
                    newPos = Vec2.getOposite(orc.getPosition(), ((Orc) el).getPosition());
                }
            }
        }
        System.out.println("SDD_Separation : " + newPos);
        return newPos;
    }

    public boolean estTermine(){
        return env.elIsIn(orc) == null;
    }

    public StrategieDeDeplacement getProchaineStrategie(Environnement env) {
        return new SDD_Exploration(orc,env);
    }

    @Override
    public String toString() {
        return "SDD_Separation{}";
    }
}

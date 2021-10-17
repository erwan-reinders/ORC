
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Agent {
    /*Classe abstraite symbolisant un Agent*/
    private Map<String, Action> actions;


    public Agent(){
        this.actions = new LinkedHashMap<String, Action>();
    }

    protected abstract Action prendreDesision(Environnement env);

    public void executerDesision(Environnement env) {
        prendreDesision(env).executer(this);
    }

    public void addActions(Map<String, Action> actions){
        this.actions.putAll(actions);
    }

    public void addAction(String name, Action action){
        this.actions.put(name, action);
    }

    public void removeAction(String name){
        actions.remove(name);
    }

    public Action getAction(String name) {
        return actions.get(name);
    }

    public Map<String, Action> getActions() {
        return actions;
    }

}

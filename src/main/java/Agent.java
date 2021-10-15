
import java.util.ArrayList;

public abstract class Agent {
    /*Classe abstraite symbolisant un Agent*/
    private ArrayList<Action> actions;


    public Agent(ArrayList<Action> actions){
        this.actions = actions;
    }
    
    
    
    public void addActions(ArrayList<Action> actions){
        this.actions.addAll(actions);
    }
    
    public void removeAction(Action action){
        actions.remove(action);
    }
    
    public ArrayList<Action> getActions() {
        return actions;
    }
    

}

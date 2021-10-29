import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
	/** Constructeur privé */
    private KeyManager()
    {}
 
    /** Instance unique pré-initialisée */
    private static KeyManager INSTANCE = new KeyManager();
     
    /** Point d'accès pour l'instance unique du singleton */
    public static KeyManager getInstance()
    {   return INSTANCE;
    }
    
    public boolean C_ENABLED;

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case(KeyEvent.VK_C):
			C_ENABLED = !C_ENABLED;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
				
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}

import MathClass.Vec2;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class App extends JFrame{
	private Panneau[][] panneaux;
	private int indiceScene = 0;
	private static JMenuBar bar;

	private int tX = 100;
	private int tY = 100;
	private Ordonnanceur o;
	private int nbOrc = 100;

	public static abstract class Panneau extends JPanel{
		protected int tX;
		protected int tY;

		public Panneau(int decalage_x, int decalage_y){
			tX = decalage_x;
			tY = decalage_y;
		}

		public abstract void update();
	}

	public static class scene_principale extends Panneau {
		private final Ordonnanceur o;
		private int tX;
		private int tY;

		private boolean start = false;

		public scene_principale(int width, int height, int tX, int tY, final Ordonnanceur o){
			super(width,height);
			this.tX = tX;
			this.tY = tY;
			this.o = o;

			setSize(new Dimension(width,height));
			setVisible(true);

			/*Bouton pour démarer la simulation*/
			int tailleb = 50;

			final Button b = new Button("START");

			b.setFont(new Font("Arial",Font.PLAIN,40));

			b.setBackground(Color.GREEN);
			b.setBounds(width/2, 0,tailleb,tailleb/4);
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					start = !start;
					if(b.getLabel().equals("START")){
						b.setLabel("STOP");
						b.setBackground(Color.RED);
					}else{
						b.setLabel("START");
						b.setBackground(Color.GREEN);
					}
				}
			});
			this.add(b);

			/*Gestion des radio button*/
			JMenu option = new JMenu("OPTIONS");
			option.setMnemonic(KeyEvent.VK_O);
			bar.add(option);
			JMenuItem menu_cbAff = new JMenuItem("Affichage");
			option.add(menu_cbAff);
			/*Bouton pour afficher ou non les traces*/
			final JCheckBoxMenuItem tb = new JCheckBoxMenuItem("traces");
			tb.setSelected(true);
			tb.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				o.affichage_trace = !o.affichage_trace;
			}
			});
			option.add(tb);
			/*Bouton pour afficher ou non les ranges*/
			final JCheckBoxMenuItem tr = new JCheckBoxMenuItem("ranges");
			tr.setSelected(false);
			tr.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					o.upAffichage_range();
				}
			});
			option.add(tr);
			/*Bouton pour afficher ou non les fov*/
			final JCheckBoxMenuItem tfov = new JCheckBoxMenuItem("vision");
			tfov.setSelected(false);
			tfov.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					o.upAffichage_fov();
				}
			});
			option.add(tfov);
		}

		public Ordonnanceur getO() {
			return o;
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.clearRect(0, 0, getWidth(), getHeight());
			g2d.translate(tX, tY);
			o.draw(g2d);
			g2d.translate(-tX, -tY);
		}

		public void update() {
			if(start) {
				o.update();
				repaint();
			}
		}
	}

	private void initApp(int width, int height) {
		this.o = new Ordonnanceur(width -2*tX, height-2*tY, nbOrc,new Color(150,150,150));
		/*Menu des options présentes en haut de la fenetre*/
		bar = new JMenuBar();
		this.setJMenuBar(bar);

		panneaux = new Panneau[][]{{new scene_principale(width,height,tX,tY,o)}};
	}

	private void initialiserScene(){
		for(int j = 0; j<panneaux.length; j++){
			if(j == indiceScene) {
				for (int i = 0; i < panneaux[j].length; i++) {
					this.getContentPane().add(panneaux[j][i]);
				}
			}else{
				for (int i = 0; i < panneaux[j].length; i++) {
					this.remove(panneaux[j][i]);
				}
			}
		}

	}

	private void updateScene(){
		for (int i = 0; i < panneaux[indiceScene].length; i++) {
			panneaux[indiceScene][i].update();
		}
	}

	public App(int width, int height){
		initApp(width, height);

	    setTitle("Orc");
	    setSize(width, height);
	    setVisible(true);
	    setResizable(true);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);

	    this.addKeyListener(KeyManager.getInstance());

	    initialiserScene();
	}

	public boolean isFinish(){
		return o.isFinished();
	}

	public static void main(String[] args) throws InterruptedException {
		App a = new App(1000, 500);

		while (!a.isFinish()) {
			a.updateScene();
			Thread.sleep(16);
		}
	}

	/*TODO LIST*/
	//FAIRE UNE MECANIQUE DE PARTAGE DE LINFO DES ORCS ENNEMIS VUS EN EQUIPE POUR STRAT COLLECTIVE
	//DEPLACEMENT ISOVAL POUR REJOINDRE UNE CIBLE DE TYPE ORC ENNEMI (SDD ATTAQUER)
	//VOIR CREATION NOUVELLE SDD SOUTIENT BASEE SUR ISOVALEURS
	//OPTIMISER LE LANCER DE RAYON (VECTORISATION MDR)
	//ZONE A ACTIVER
	//MECANIQUE DES OBSTACLES + VISION
	//MECANIQUE DES OBSTACLES + VITESSE



	/*DEJA FAIT*/
	//SOIT KEYMANAGER, SOIT RDioB
	//AFFICHER FOV (fait)
	//AFFICHER RANGE (fait)
	//AFFICHER TRACES (fait)

	//AVANCER SELON LA BONNE SDD
	//CHANGEMENT DE SDD ET NOUVELLE SDD
	//PARTITIONNEMENT DU CODE EN CLASSE PLUS PARLANTE
	//FIXATION DES BUGS LIES A LAFFICHAGE (JPANNEL)
	//SDD_Exploration : DEPLACEMENT EXPLORATION PAR RAPPORT ETENDU ZONE EXPLORATION ET TRACES
	//FOV PAR TRIANGLES
	//MECANIQUE DE FUITE ET REGEN DE PV SI FAIT RIEN PENDANT UN MOMENT

	//A PRIORIS NON
	//BUG RANGE ET DEP MAX SDD ATTAQUE (POSSIBLE)?
}
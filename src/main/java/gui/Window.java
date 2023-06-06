package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Game;
import model.Player;
import model.PlayerType;

public class Window extends JFrame {

	public static Color BACKGROUND_COLOR = new Color(41, 50, 65),
						DARK_COLOR1 = new Color(61, 90, 128),
						DARK_COLOR2 = new Color(46, 55, 75),
						LIGHT_COLOR1 = new Color(152, 193, 217),
						LIGHT_COLOR2 = new Color(224, 251, 252),
						RED = new Color(238, 108, 77);

	public static Window currentWindow = null;

	private GameView gameView;
	private Game game;

	private GameKeyListener gameKeyListener;
	private GameMouseListener gameMouseListener;
	
	private JPanel lastPanel;
	private int width, height, heightSpace = 45;
	
	boolean isFullscreen = false;

	public Window(int w, int h) {
		this.setTitle("Minecraft2D");
		this.width = w;
		this.height = h;
		this.setMinimumSize(new Dimension(width, height+heightSpace)); // width+75, height));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(true);


		Game game = new Game(Player.createPlayer(PlayerType.STEVE, "leopold"), width, height);
		// System.out.println(game);

		setGameView(game);

		this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // System.out.println("La taille de la fenêtre a été modifiée !");
				// heightSpace enleve l'espace ajouté au debut du constructeur de Window
				Dimension newSize = new Dimension(getWidth(), getHeight() - heightSpace);
		
                // System.out.println("Nouvelle taille : " + newSize.getWidth() + "x" + newSize.getHeight());
				if(gameView != null) {
					gameView.getMapView().setPreferredSize(newSize);
				}
				gameView.updateUI();
				// gameView.revalidate();
				// gameView.repaint();
            }
        });


		// Permet de vérifier si le joueur a mis le jeu en pleine écran

        // GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        // isFullscreen = device.getFullScreenWindow() == this;

		// this.addWindowStateListener(new WindowStateListener() {
        //     @Override
        //     public void windowStateChanged(WindowEvent e) {
        //         boolean wasFullscreen = isFullscreen;
        //         isFullscreen = (e.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
        //         if (wasFullscreen && !isFullscreen) {
        //             // System.out.println("La fenêtre a quitté le mode plein écran.");
        //         } else if (!wasFullscreen && isFullscreen) {
        //             // System.out.println("La fenêtre est maintenant en plein écran !");
        //         }
        //     }
        // });

		this.addWindowListener(new WindowAdapter() {

			// Permet de mettre le jeu en full screen (sans la barre d'état etc...)

			// @Override
            // public void windowOpened(WindowEvent e) {
            //     GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            //     device.setFullScreenWindow(Window.this);
            //     if (device.isFullScreenSupported()) {
            //         System.out.println("La fenêtre est maintenant en plein écran !");
            //     } else {
            //         System.out.println("Le mode plein écran n'est pas pris en charge.");
            //     }
            // }

            // @Override
            // public void windowClosed(WindowEvent e) {
            //     GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            //     device.setFullScreenWindow(null);
            //     System.out.println("La fenêtre a quitté le mode plein écran.");
            // }

            @Override
            public void windowClosing(WindowEvent e) {
				System.exit(0);
            }
        });

		this.setVisible(true);
		Window.currentWindow = this;
	}
	
	public void clearWindow() {
		this.getContentPane().removeAll();
		if(gameKeyListener != null)
			this.removeKeyListener(gameKeyListener);
		if(gameMouseListener != null)
			this.removeMouseListener(gameMouseListener);
	}

	public void refreshWindow() {
		revalidate();
		repaint();
	}

	public void setHomeView() {
		clearWindow();
		//this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// lastPanel = new HomeView(this);
		// this.getContentPane().add(lastPanel);
		refreshWindow();
	}
	
	public void setGameView(Game game) {
		clearWindow();
		this.game = game;
		this.gameView = new GameView(game);
		this.lastPanel = gameView;
		
		gameKeyListener = new GameKeyListener(gameView, game);
		gameMouseListener = new GameMouseListener(gameView, game);
		this.addKeyListener(gameKeyListener);
		this.addMouseListener(gameMouseListener);

		this.getContentPane().add(lastPanel);
		refreshWindow();
	}

}

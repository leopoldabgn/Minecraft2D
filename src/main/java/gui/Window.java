package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.Game;
import model.Player;

public class Window extends JFrame {
	private static final long serialVersionUID = 1L;

	public static Color BACKGROUND_COLOR = new Color(41, 50, 65),
						DARK_COLOR1 = new Color(61, 90, 128),
						DARK_COLOR2 = new Color(46, 55, 75),
						LIGHT_COLOR1 = new Color(152, 193, 217),
						LIGHT_COLOR2 = new Color(224, 251, 252),
						RED = new Color(238, 108, 77);

	private GameView gameView;
	private Game game;

	private GameKeyListener gameKeyListener;
	private GameMouseListener gameMouseListener;
	
	private JPanel lastPanel;
	private int width, height;
	
	public Window(int w, int h) {
		this.setTitle("Worms");
		this.width = w;
		this.height = h;
		this.setMinimumSize(new Dimension(width+75, height+75)); // width+75, height));
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(true);


		Game game = new Game(Player.createPlayer("leopold"));
		System.out.println(game);

		setGameView(game);

		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
				System.exit(0);
            }
        });

		this.setVisible(true);
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

package graphics;


import javax.swing.JPanel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Dimension;


/**
 * Manages the graphical user interface for the game.
 */
public class Screen extends JPanel {

	/** The main view. */
    private MainView mainView;
	/** The game view. */
	private GameView gameView;


	/**
	 * Constructs a new {@code Screen} object.
	 */
	public Screen() {
		this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					Screen.this.mainView.setPreferredSize(Screen.this.getSize());
					Screen.this.gameView.setPreferredSize(Screen.this.getSize());
				    Screen.this.mainView.revalidate();
					Screen.this.gameView.revalidate();
					Screen.this.revalidate();
				}
			});
		
		this.mainView = new MainView(this);
		this.gameView = new GameView(this);

		this.displayMainView();
	}


	/**
	 * Redraws this screen, removing whatever view that might have been added.
	 */
	private void clearGraphicsContext() {
		this.removeAll();
		this.revalidate();
		this.repaint();
	}


	/**
	 * Displays the main view.
	 */
	public void displayMainView() {
		this.clearGraphicsContext();
		this.add(this.mainView);
	}


	/**
	 * Displays the game view after joining an existing game.
	 *
	 * @param ip        the IP address to join the game on.
	 * @param port      the port to join the game on.
	 */
	public void displayGameView(String ip, int port) {
	    this.displayGameView(ip, port, false);
	}


	/**
	 * Displays the game view after hosting and/or joining a new game.
	 *
	 * @param ip        the IP address to host/join the game on.
	 * @param port      the port to host/join the game on.
	 * @param hosting   whether a new game should be hosted, or an existing game joined.
	 */
	public void displayGameView(String ip, int port, boolean hosting) {
		this.clearGraphicsContext();
		this.gameView.connect(ip, port, hosting);
		this.add(this.gameView);
		Thread animateThread = new Thread(new Runnable() {
				@Override
				public void run() {
					Screen.this.gameView.animate();
				}
			});
		animateThread.start();
	}

	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Settings.FOV * 32, Settings.FOV * 32);
	}

}

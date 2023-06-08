package graphics;


import client.ByteStrike;
import world.LevelBuilder;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JComponent;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


/**
 * Displays the main menu of the game.
 *
 * @author Jonathan Uhler
 */
public class MainView extends JPanel {

	/** Screen that controls this view. */
	private Screen screen;
	
	/** Hosts a new game. */
	private JButton hostButton;
	/** Joins an existing game. */
	private JButton joinButton;
	/** Opens the level builder JOptionPane. */
	private JButton levelBuilderButton;
	/** Quits the game. */
	private JButton quitButton;
	

	/**
	 * Constructs a new {@code MainView}. Upon construction, the {@code MainView} object will
	 * render its graphical context.
	 *
	 * @param screen  the parent {@code Screen} of this view.
	 *
	 * @see Screen
	 */
	public MainView(Screen screen) {
		this.setLayout(new GridBagLayout());
		
		this.screen = screen;

		this.hostButton = new JButton("Host Game");
		this.joinButton = new JButton("Join Game");
		this.quitButton = new JButton("Quit Game");
		this.levelBuilderButton = new JButton("Level Builder");

		this.hostButton.addActionListener(e -> this.hostAction());
		this.joinButton.addActionListener(e -> this.joinAction());
		this.quitButton.addActionListener(e -> this.quitAction());
		this.levelBuilderButton.addActionListener(e -> this.levelBuilderAction());

		this.redraw();
	}


	/**
	 * Redraws this view. Redrawing includes:
	 * <ul>
	 * <li> Clearing all child components of this view.
	 * <li> Revalidating this view.
	 * <li> Repainting this view.
	 * <li> Calling this view's {@code display} method to allow other graphical operations.
	 * </ul>
	 *
	 * @see display
	 */
	public void redraw() {
		this.removeAll();
		this.revalidate();
		this.repaint();
		this.display();
	}


	/**
	 * Displays the graphical context of this {@code MainView}.
	 */
	public void display() {
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel titleLabel = new JLabel("Byte Strike");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
		this.add(titleLabel, gbc);

		gbc.gridy++;
		this.add(this.hostButton, gbc);

		gbc.gridy++;
		this.add(this.joinButton, gbc);

		gbc.gridy++;
		this.add(this.levelBuilderButton, gbc);

		gbc.gridy++;
		this.add(this.quitButton, gbc);
	}


	/**
	 * Returns an user-defined IP address and port. If not null, the returned array is guaranteed
	 * to contain exactly two elements, where the first element can always be casted to a
	 * {@code String} and the second element can always be casted to an {@code Integer}. If
	 * the user closes the {@code JOptionPane} without selecting "OK", {@code null} is returned.
	 *
	 * @param prompt  the title of the displayed {@code JOptionPane} shown to the user.
	 *
	 * @return an user-defined IP address and port.
	 */
	private Object[] getConnectionInfo(String prompt) {
		JTextField ipTextField = new JTextField(8);
		JSpinner portSpinner = new JSpinner(new SpinnerNumberModel(1024, 1024, 49151, 1));
		JSpinner.NumberEditor portSpinnerEditor = new JSpinner.NumberEditor(portSpinner, "#");
		portSpinner.setEditor(portSpinnerEditor);
		JComponent[] components = new JComponent[] {new JLabel("IP Address:"),
													ipTextField,
													new JLabel("Port:"),
													portSpinner};

		int confirm = ByteStrike.displayDialog(prompt, components);
		if (confirm != JOptionPane.OK_OPTION)
			return null;

		String ip = ipTextField.getText();
		int port = (Integer) portSpinner.getValue();

		return new Object[] {ip, port};
	}


	/**
	 * The action performed by the {@code hostButton} button.
	 */
	private void hostAction() {
	    Object[] connectionInfo = this.getConnectionInfo("Host Game");
		if (connectionInfo == null)
			return;

		String ip = (String) connectionInfo[0];
		int port = (Integer) connectionInfo[1];

		this.screen.displayGameView(ip, port, true);
	}


	/**
	 * The action performed by the {@code joinButton} button.
	 */
	private void joinAction() {
		Object[] connectionInfo = this.getConnectionInfo("Join Game");
		if (connectionInfo == null)
			return;

		String ip = (String) connectionInfo[0];
		int port = (Integer) connectionInfo[1];

		this.screen.displayGameView(ip, port);
	}


	/**
	 * The action performed by the {@code levelBuilderButton} button;
	 */
	private void levelBuilderAction() {
		LevelBuilder.create();
	}


	/**
	 * The action performed by the {@code quitButton} button.
	 */
	private void quitAction() {
		System.exit(0);
	}

}

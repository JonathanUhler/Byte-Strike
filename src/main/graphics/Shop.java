package graphics;


import item.*;
import interfaces.Weapon;
import server.Communication;
import javax.swing.JComponent;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


public class Shop extends JComponent implements MouseListener, MouseMotionListener {

	private List<ActionListener> actionListeners;

	private Point lastMouseHover;
	private Point lastMouseClick;
	private long lastPurchase;


	public Shop() {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.actionListeners = new ArrayList<>();
		
		this.lastMouseHover = new Point(-1, -1);
		this.lastMouseClick = new Point(-1, -1);
		this.lastPurchase = 0;
	}
	

	@Override
	public void paintComponent(Graphics g) {
		int w = this.getSize().width;
		int h = this.getSize().height;
		int d = Math.min(w, h);

		Weapon[] weapons = {new Pistol(), new SMG(), new Rifle(), new Shotgun(), new Sniper()};

		// Background
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRoundRect(0, 0, w, h, 15, 15);

		// Shop title
		int titleFontSize = d / 10;
		g.setColor(new Color(247, 245, 228));
		g.setFont(new Font("Arial", Font.BOLD, titleFontSize));
		String title = "Shop";
		int titleWidth = g.getFontMetrics().stringWidth(title);
		g.drawString(title, (w - titleWidth) / 2, titleFontSize);

		// Weapons
		int margin = (int) (d * 0.025);
		int weaponAreaX = margin * 2;
		int weaponAreaY = margin + titleFontSize;
		int weaponAreaW = w - 2 * margin;
		int weaponAreaH = h - 2 * margin - titleFontSize;

	    int weaponBoxSize = (weaponAreaW - (weapons.length - 1) * margin) / weapons.length;
		for (int i = 0; i < weapons.length; i++) {
			// Get weapon information
			Weapon weapon = weapons[i];
			String weaponType = weapon.getType();

			// Display weapon information box
			int lastHoverX = (int) this.lastMouseHover.getX();
			int lastHoverY = (int) this.lastMouseHover.getY();

			int boxX = weaponAreaX + (i * weaponBoxSize) + (i - 1) * margin;
			g.setFont(new Font("Arial", Font.PLAIN, titleFontSize / 3));
			if (lastHoverX > boxX && lastHoverX < boxX + weaponBoxSize &&
				lastHoverY > weaponAreaY && lastHoverY < weaponAreaY + weaponBoxSize)
				g.setColor(new Color(113, 180, 209));
			else
				g.setColor(new Color(247, 245, 228));
			g.drawRoundRect(boxX, weaponAreaY, weaponBoxSize, weaponBoxSize, 5, 5);
			// Display weapon information
			g.drawString(weaponType, boxX + margin / 2, weaponAreaY + titleFontSize / 3);
			g.drawString("$" +weapon.cost(), boxX + margin / 2, weaponAreaY + 2*titleFontSize/3);
			SpriteLoader.draw(g, "Shop/Shop" + weaponType,
							  boxX, weaponAreaY + margin, weaponBoxSize);

			// Check for purchase if the box was clicked
			int lastClickX = (int) this.lastMouseClick.getX();
			int lastClickY = (int) this.lastMouseClick.getY();
			long purchaseTime = System.currentTimeMillis();
			if (lastClickX > boxX && lastClickX < boxX + weaponBoxSize &&
				lastClickY > weaponAreaY && lastClickY < weaponAreaY + weaponBoxSize &&
				purchaseTime - lastPurchase >= 500)
			{
				this.lastPurchase = purchaseTime;
				this.lastMouseClick = new Point(-1, -1);
				this.actionEvent(weapon);
			}
		}
	}


	/**
	 * Adds the specified action listener to receive action events from this component.
	 *
	 * @param l  the action listener to be added.
	 */
	public void addActionListener(ActionListener l) {
		this.actionListeners.add(l);
	}


	/**
	 * Removes the specified action listener so that it no longer receives action events from 
	 * this component.
	 *
	 * @param l  the action listener to be removed.
	 */
	public void removeActionListener(ActionListener l) {
		this.actionListeners.remove(l);
	}

	
	private void actionEvent(Weapon weapon) {
		// Create the command with the shop purchase information to send
		Map<String, String> command = Communication.cmdBuy(weapon, 0);
		String commandStr = Communication.serialize(command);

	    // Notify all listeners of this pane that an adapter action was performed
		ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, commandStr);
		for (ActionListener l : this.actionListeners)
			l.actionPerformed(actionEvent);
	}


    @Override
	public void mousePressed(MouseEvent e) {
		this.lastMouseClick = e.getPoint();
	}


	@Override
	public void mouseReleased(MouseEvent e) { }

	
	@Override
	public void mouseMoved(MouseEvent e) {
		this.lastMouseHover = e.getPoint();
	}


	@Override
	public void mouseClicked(MouseEvent e) { }


	@Override
	public void mouseEntered(MouseEvent e) { }


	@Override
	public void mouseExited(MouseEvent e) { }


	@Override
	public void mouseDragged(MouseEvent e) { }

}

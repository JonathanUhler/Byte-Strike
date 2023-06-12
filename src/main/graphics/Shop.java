package graphics;


import item.*;
import interfaces.Weapon;
import interfaces.Item;
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

		Item[] items = {new Pistol(), new SMG(), new Rifle(), new Shotgun(), new Sniper(),
						new Armor(), new HealthKit(), new Grenade()};

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

		// Items
		int margin = (int) (d * 0.025);
		int itemAreaX = margin * 2;
		int itemAreaY = margin * 2 + titleFontSize;
		int itemAreaW = w - 2 * margin;
		int itemAreaH = h - 3 * margin - titleFontSize;
		double whRatio = (double) itemAreaW / itemAreaH;
		int numBoxes = items.length;

		// Calculate size and positioning of the items
		int boxSize = 0;
		int itemRows = numBoxes;
		int itemCols = numBoxes;
		while (itemRows * itemCols > numBoxes) {
			boxSize++;
			int newItemCols = itemAreaW / boxSize;
			int newItemRows = itemAreaH / boxSize;
			if (newItemCols * newItemRows < numBoxes)
				break;
			itemCols = newItemCols;
			itemRows = newItemRows;
		}
		boxSize -= margin;

		// Calculate margin to center all the boxes vertically and horizontally
		int totalBoxesW = (boxSize + margin) * itemCols;
		int totalBoxesH = (boxSize + margin) * itemRows;
		int boxesMarginHoriz = (itemAreaW - totalBoxesW + margin) / 2;
		int boxesMarginVert = (itemAreaH - totalBoxesH + margin) / 2;

		// Draw the item boxes
		for (int r = 0; r < itemRows; r++) {
			for (int c = 0; c < itemCols; c++) {
				if (c + r * itemCols >= items.length)
					continue;

				// Get item information
				Item item = items[c + r * itemCols];
				String itemType = item.getType();

				// Display item information box
				int lastHoverX = (int) this.lastMouseHover.getX();
				int lastHoverY = (int) this.lastMouseHover.getY();

				int boxX = itemAreaX + (c * boxSize) + (c - 1) * margin + boxesMarginHoriz;
				int boxY = itemAreaY + (r * boxSize) + (r - 1) * margin + boxesMarginVert;
				g.setFont(new Font("Arial", Font.PLAIN, titleFontSize / 3));
				if (lastHoverX > boxX && lastHoverX < boxX + boxSize &&
					lastHoverY > boxY && lastHoverY < boxY + boxSize)
					g.setColor(new Color(113, 180, 209));
				else
					g.setColor(new Color(247, 245, 228));
				g.drawRoundRect(boxX, boxY, boxSize, boxSize, 5, 5);
				// Display item information
				g.drawString(itemType, boxX + margin / 2, boxY + titleFontSize / 3);
				g.drawString("$" + item.getCost(), boxX + margin/2, boxY + 2*titleFontSize/3);
				SpriteLoader.draw(g, "Shop/" + itemType, boxX, boxY + margin, boxSize);

				// Check for purchase if the box was clicked
				int lastClickX = (int) this.lastMouseClick.getX();
				int lastClickY = (int) this.lastMouseClick.getY();
				long purchaseTime = System.currentTimeMillis();
				if (lastClickX > boxX && lastClickX < boxX + boxSize &&
					lastClickY > boxY && lastClickY < boxY + boxSize &&
					purchaseTime - lastPurchase >= 500)
				{
					this.lastPurchase = purchaseTime;
					this.lastMouseClick = new Point(-1, -1);
					this.actionEvent(item);
				}
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

	
	private void actionEvent(Item item) {
		// Create the command with the shop purchase information to send
		Map<String, String> command = Communication.cmdBuy(item, 0);
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

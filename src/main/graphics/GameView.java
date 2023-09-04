package graphics;


import jnet.JClientSocket;
import jnet.Log;
import jnet.Bytes;
import server.Server;
import server.Communication;
import client.ByteStrike;
import world.Level;
import entity.Player;
import entity.Bullet;
import entity.Ray;
import item.*;
import interfaces.Weapon;
import interfaces.Item;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Point;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class GameView extends JPanel implements KeyListener,
												MouseListener,
												MouseMotionListener,
												ActionListener {

	private Screen screen;

	private boolean movingUp;
	private boolean movingLeft;
	private boolean movingDown;
	private boolean movingRight;
	private boolean shooting;
	private Point aimingAt;

	private JClientSocket client;
	private int myId;
	private Map<Integer, Player> players;
	private List<Bullet> bullets;
	private Level level;
	
	private Shop shop;
	private boolean showShop;
	

	public GameView(Screen screen) {
	    this.setFocusable(true);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.screen = screen;

		this.movingUp = false;
		this.movingLeft = false;
		this.movingDown = false;
		this.movingRight = false;
		this.shooting = false;
		this.aimingAt = null;

		this.client = null;
		this.myId = -1;
		this.players = new HashMap<>();
		this.bullets = new ArrayList<>();
		this.level = null;
		
		this.shop = null;
		this.showShop = false;
	}


	public void startServer(String ip, int port) {
		Thread serverThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						new Server(ip, port);
					}
					catch (IOException e) {
						ByteStrike.displayMessage("Connection Error", "Unable to start server on " +
												  ip + ":" + port + ".\n" + e);
						Thread.currentThread().interrupt();
						return;
					}
				}
			});
		serverThread.start();
	}


	public void connect(String ip, int port, boolean hosting) {
		// Start new server if hosting
		if (hosting) {
			this.startServer(ip, port);
			try {
				Thread.sleep(500); // Wait for server to open
			}
			catch (InterruptedException e) {
				Log.stdlog(Log.ERROR, "GameView", "error waiting for server to open: " + e);
				return;
			}
		}

		// Connect to server
		this.client = new JClientSocket();

		try {
			this.client.connect(ip, port);
		}
		catch (IOException e) {
			this.client = null;
			this.screen.displayMainView();
			ByteStrike.displayMessage("Connection Error", "Unable to connect to server at " +
									  ip + ":" + port + ".\n" + e);
			return;
		}

		Thread listenThread = new Thread(this::listen);
		listenThread.start();
	}


	private void listen() {
		while (true) {
			byte[] recv = this.client.recv();
			if (recv == null)
				return;

			this.serverCommunicated(recv);
			this.repaint();
		}
	}


	public void serverCommunicated(byte[] recv) {
		String commandStr = Bytes.bytesToString(recv);
		Map<String, String> command = Communication.deserialize(commandStr);
		String opcode = command.get(Communication.KEY_OPCODE);
		if (opcode == null) {
			Log.stdlog(Log.ERROR, "GameView", "null opcode in command: " + command);
			return;
		}

		switch (opcode) {
		case Communication.OPCODE_SHOOT: {
			break;
		}
		case Communication.OPCODE_POS_PLAYER: {
			int playerId;
			double x;
			double y;
			double rad;

			try {
			    playerId = Integer.parseInt(command.get(Communication.KEY_ID));
				x = Double.parseDouble(command.get(Communication.KEY_X));
				y = Double.parseDouble(command.get(Communication.KEY_Y));
				rad = Double.parseDouble(command.get(Communication.KEY_RAD));
			}
			catch (Exception e) {
				Log.stdlog(Log.ERROR, "GameView", "Cant parse posp command: " + command + ", " + e);
				return;
			}

			Player player = this.players.get(playerId);
			if (player == null) {
				Log.stdlog(Log.ERROR, "GameView", "invalid player id: " + playerId);
				return;
			}
			player.setX(x);
			player.setY(y);
			player.setRad(rad);
			break;
		}
		case Communication.OPCODE_NEW_BULLET: {
			double x;
			double y;
			double rad;
			int bulletId;
			int playerId;

			try {
				x = Double.parseDouble(command.get(Communication.KEY_X));
				y = Double.parseDouble(command.get(Communication.KEY_Y));
				rad = Double.parseDouble(command.get(Communication.KEY_RAD));
				bulletId = Integer.parseInt(command.get(Communication.KEY_BULLET_NUM));
				playerId = Integer.parseInt(command.get(Communication.KEY_ID));
			}
			catch (Exception e) {
				Log.stdlog(Log.ERROR, "GameView", "Cant parse newb command: " + command + ", " + e);
				return;
			}

			Player player = this.players.get(playerId);
			if (player == null) {
				Log.stdlog(Log.ERROR, "GameView", "New bullet invalid player id: " + playerId);
				return;
			}

			Weapon weapon = player.getWeapon();
			Bullet bullet = new Bullet(x, y, rad, weapon);
			this.bullets.add(bullet);
			// Fire weapon on client side if this is my player to play the proper sounds
			// Check bullet id to avoid firing the weapon multiple times for weapons that
			// use multiple bullets per shot (e.g. shotguns).
			if (playerId == this.myId && bulletId == 0) {
				weapon.fireBlank();
			}
			break;
		}
		case Communication.OPCODE_JOIN: {
			int playerId;
			double x;
			double y;
			int levelId;

			try {
				playerId = Integer.parseInt(command.get(Communication.KEY_ID));
				x = Double.parseDouble(command.get(Communication.KEY_X));
				y = Double.parseDouble(command.get(Communication.KEY_Y));
				levelId = Integer.parseInt(command.get(Communication.KEY_LEVEL));
			}
			catch (Exception e) {
				Log.stdlog(Log.ERROR, "GameView", "Cant parse join command: " + command + ", " + e);
				return;
			}

			Player player = new Player(x, y);
			if (myId == -1) {
				myId = playerId;
				this.level = new Level(levelId);
				this.shop = new Shop();
				this.shop.addActionListener(this);
				this.revalidate();
			}
			this.players.put(playerId, player);
			break;
		}
		case Communication.OPCODE_LEAVE: {
			int playerId;

			try {
				playerId = Integer.parseInt(command.get(Communication.KEY_ID));
			}
			catch (Exception e) {
				Log.stdlog(Log.ERROR, "GameView", "Cant parse leave cmd: " + command + ", " + e);
				return;
			}
			
			this.players.remove(playerId);
			break;
		}
		case Communication.OPCODE_DAMAGED: {
			int playerId;
			int dmg;

			try {
				playerId = Integer.parseInt(command.get(Communication.KEY_ID));
				dmg = Integer.parseInt(command.get(Communication.KEY_DMG));
			}
			catch (Exception e) {
				Log.stdlog(Log.ERROR, "GameView", "Cant parse dmg cmd: " + command + ", " + e);
				return;
			}
			
		    Player player = this.players.get(playerId);
			if (player == null)
				return;
			player.damage(dmg);
			
			if (playerId == this.myId)
				SoundManager.playSound("hurt");
			break;
		}
		case Communication.OPCODE_RESET: {
			int playerId;
			double x;
			double y;

			try {
				playerId = Integer.parseInt(command.get(Communication.KEY_ID));
				x = Double.parseDouble(command.get(Communication.KEY_X));
				y = Double.parseDouble(command.get(Communication.KEY_Y));
			}
			catch (Exception e) {
				Log.stdlog(Log.ERROR, "GameView", "Cant parse reset cmd: " + command + ", " + e);
				return;
			}

			Player player = this.players.get(playerId);
			if (player == null) {
				Log.stdlog(Log.ERROR, "GameView", "invalid player id for reset: " + playerId);
				return;
			}
			player.setX(x);
			player.setY(y);
			player.reset();
			break;
		}
		case Communication.OPCODE_BUY: {
			int playerId;

			try {
				playerId = Integer.parseInt(command.get(Communication.KEY_ID));
			}
			catch (Exception e) {
				Log.stdlog(Log.ERROR, "GameView", "Can't parse buy command: " + command + ", " + e);
				return;
			}

			Player player = this.players.get(playerId);
			if (player == null) {
				Log.stdlog(Log.ERROR, "GameView", "invalid player id: " + playerId);
				return;
			}

			String itemStr = command.get(Communication.KEY_ITEM);
			if (itemStr == null) {
				Log.stdlog(Log.ERROR, "GameView", "No item in buy command: " + command);
				return;
			}
			Item item = null;
			switch (itemStr) {
			case "Pistol" -> item = new Pistol();
			case "SMG" -> item = new SMG();
			case "Rifle" -> item = new Rifle();
			case "Shotgun" -> item = new Shotgun();
			case "Sniper" -> item = new Sniper();
			case "Armor" -> item = new Armor();
			case "HealthKit" -> item = new HealthKit();
			case "Grenade" -> item = new Grenade();
			default -> {
				Log.stdlog(Log.ERROR, "GameView", "invalid item bought: " + itemStr);
				return;
			}
			}

			player.buy(item);
			break;
		}
		case Communication.OPCODE_PAY: {
			int playerId;
			int money;

			try {
				playerId = Integer.parseInt(command.get(Communication.KEY_ID));
				money = Integer.parseInt(command.get(Communication.KEY_MONEY));
			}
			catch (Exception e) {
				Log.stdlog(Log.ERROR, "GameView", "Can't parse buy command: " + command + ", " + e);
				return;
			}

			Player player = this.players.get(playerId);
			if (player == null) {
				Log.stdlog(Log.ERROR, "GameView", "invalid player id: " + playerId);
				return;
			}

			player.pay(money);
			break;
		}
		case Communication.OPCODE_USE: {
			int itemNum;
			int playerId;

			try {
				itemNum = Integer.parseInt(command.get(Communication.KEY_ITEM_NUM));
				playerId = Integer.parseInt(command.get(Communication.KEY_ID));
			}
			catch (Exception e) {
				Log.stdlog(Log.ERROR, "GameView", "Can't parse use command: " + command + ", " + e);
				return;
			}

			Player player = this.players.get(playerId);
			if (player == null) {
				Log.stdlog(Log.ERROR, "GameView", "invalid player id: " + playerId);
				return;
			}

			player.use(itemNum);
		}
		default:
			Log.stdlog(Log.ERROR, "GameView", "invalid opcode: " + opcode);
			break;
		}

		this.repaint();
	}


	public int getTileSize() {
		int w = this.getSize().width;
		int h = this.getSize().height;
		int maxDim = Math.max(w, h);
		int tileSize = maxDim / Settings.FOV;
		return tileSize;
	}


	public Point getPlayerPaintLocation() {
		Player me = this.players.get(this.myId);
		if (me == null)
			return new Point(-1, -1);
		
		int tileSize = this.getTileSize();
		int wPixels = this.getSize().width;
		int hPixels = this.getSize().height;
		int playerSize = (int) (me.getSize() * tileSize);
		return new Point(wPixels / 2 - playerSize / 2, hPixels / 2 - playerSize / 2);
	}


	public double getPlayerRotation() {
		if (this.aimingAt == null)
			return 0;

		int tileSize = this.getTileSize();
		int wPixels = this.getSize().width;
		int hPixels = this.getSize().height;
		double x = this.aimingAt.x - wPixels / 2;
		double y = this.aimingAt.y - hPixels / 2;
		double rad = x < 0 ? Math.atan(y / x) + Math.PI : Math.atan(y / x);
		return rad;
	}


	public Point getRelativeLocation(double tileX, double tileY) {
		Point playerLocation = this.getPlayerPaintLocation();
		int tileSize = this.getTileSize();
		Player me = this.players.get(this.myId);
		if (me == null)
			return new Point((int) (tileX * tileSize), (int) (tileY * tileSize));
		return new Point(playerLocation.x + (int) ((tileX - me.getX()) * tileSize),
						 playerLocation.y + (int) ((tileY - me.getY()) * tileSize));
	}


	public boolean isViewable(double tileX, double tileY) {
		Player me = this.players.get(this.myId);
		if (me == null)
			return false;
		double x = tileX - me.getX();
		double y = tileY - me.getY();
		double rad = x < 0 ? Math.atan(y / x) + Math.PI : Math.atan(y / x);

		Ray ray = new Ray(me.getX(), me.getY(), rad);
		while (!ray.inRange(tileX, tileY)) {
			if (this.level.collides(ray, ray.getVx(), ray.getVy()))
				return false;
			ray.move();
		}
		return true;
	}


	private void drawThickRoundRect(Graphics g, int x, int y, int size) {
		for (int t = 0; t < size / 10; t++)
			g.drawRoundRect(x + t, y + t, size - 2 * t, size - 2 * t, 5, 5);
	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Player me = this.players.get(this.myId);
		if (me == null)
			return;
		Weapon myWeapon = me.getWeapon();
		
	    int tileSize = this.getTileSize();
		int wPixels = this.getSize().width;
		int hPixels = this.getSize().height;
		int wTiles = wPixels / tileSize;
		int hTiles = hPixels / tileSize;

		// Draw the level
		double pxTile = me.getX();
		double pyTile = me.getY();
		int playerSize = (int) (me.getSize() * tileSize);
		Point playerPx = this.getPlayerPaintLocation();
		int viewRange = (Settings.FOV - 1) / 2;
		for (int r = (int) (pyTile - viewRange); r <= (int) (pyTile + viewRange) + 1; r++) {
			if (r < 0 || r >= this.level.rows())
				continue;
			
			for (int c = (int) (pxTile - viewRange); c <= (int) (pxTile + viewRange) + 1; c++) {
				if (c < 0 || c >= this.level.cols(r))
					continue;

				Level.Tile tileType = this.level.get(r, c);
				int tileXPx = playerPx.x + (int) ((c - pxTile) * tileSize);
				int tileYPx = playerPx.y + (int) ((r - pyTile) * tileSize);
				SpriteLoader.drawTile(g, tileType, tileXPx, tileYPx, tileSize);
				if (tileType == Level.Tile.NONE && !this.isViewable(c, r)) {
					g.setColor(new Color(0, 0, 0, 30));
					g.fillRect(tileXPx, tileYPx, tileSize, tileSize);
				}
			}
		}

		// Draw bullets
		for (int i = 0; i < this.bullets.size(); i++) {
			// Have this bounds check in case the array size is changed by the bullet update
			// loop while this draw loop is running
			if (i >= this.bullets.size())
				continue;
			Bullet bullet = this.bullets.get(i);
			Point bLoc = this.getRelativeLocation(bullet.getX(), bullet.getY());
			int bs = (int) (bullet.getSize() * tileSize);
			SpriteLoader.drawEntity(g, bullet.getType(), bLoc.x, bLoc.y, bs, bullet.getRad());
		}

		// Draw other players
		for (int playerId : this.players.keySet()) {
			if (playerId == this.myId)
				continue;
			Player player = this.players.get(playerId);
			if (player.isDead())
				continue;
			if (!this.isViewable(player.getX(), player.getY()))
				continue;
			Point pLoc = this.getRelativeLocation(player.getX(), player.getY());
			int ps = (int) (player.getSize() * tileSize);
			SpriteLoader.drawPlayer(g, player, pLoc.x, pLoc.y, tileSize, player.getRad());
		}
		
		// Draw the player
		SpriteLoader.drawPlayer(g, me, playerPx.x, playerPx.y, tileSize, this.getPlayerRotation());

		// Draw the GUI overlay
		g.setColor(new Color(70, 70, 70, 150));
		g.fillRect(0, 0, wPixels, tileSize * 2);
		// Health
		g.setColor(new Color(113, 180, 209));
		g.setFont(new Font("Arial", Font.BOLD, tileSize));
		this.drawThickRoundRect(g, tileSize / 2, tileSize / 2, tileSize);
		g.drawString("+ " + me.getHealth(), (int) (tileSize * 0.75), (int) (tileSize * 1.35));
		// Ammo
		this.drawThickRoundRect(g, 4 * tileSize, tileSize / 2, tileSize);
		g.drawString("⁍ " + me.getWeapon().bulletsLeft() + "/" + me.getWeapon().capacity(),
					 (int) (tileSize * 4.25), (int) (tileSize * 1.35));
		// Money
		this.drawThickRoundRect(g, (int) ((wTiles - 3.25) * tileSize), tileSize / 2, tileSize);
		g.drawString("$ " + me.getMoney(),
					 (int) ((wTiles - 3) * tileSize),
					 (int) (tileSize * 1.35));
		// Items
		g.setFont(new Font("Arial", Font.BOLD, tileSize / 4));
		// Primary weapon
		g.drawString("Weapon", (int) (10 * tileSize), (int) (tileSize * 0.4));
		this.drawThickRoundRect(g, (int) (10 * tileSize), tileSize / 2, tileSize);
		SpriteLoader.draw(g, "Shop/" + myWeapon.getType(),
						  (int) (10 * tileSize), tileSize / 2, tileSize);
		// Use item 1
		g.drawString("Use Item 1", (int) (11.5 * tileSize), (int) (tileSize * 0.4));
		this.drawThickRoundRect(g, (int) (11.5 * tileSize), tileSize / 2, tileSize);
		Item item1 = me.getItem(1);
		if (item1 != null)
			SpriteLoader.draw(g, "Shop/" + item1.getType(),
							  (int) (11.5 * tileSize), tileSize / 2, tileSize);
		// Use item 2
		g.drawString("Use Item 2", (int) (13 * tileSize), (int) (tileSize * 0.4));
		this.drawThickRoundRect(g, (int) (13 * tileSize), tileSize / 2, tileSize);
		Item item2 = me.getItem(2);
		if (item2 != null)
			SpriteLoader.draw(g, "Shop/" + item2.getType(),
							  (int) (13 * tileSize), tileSize / 2, tileSize);

		// Update shop size and position if being shown
		if (this.showShop) {
			this.shop.setSize(new Dimension(wPixels / 2, hPixels / 2));
			this.shop.setLocation(new Point(wPixels / 4, hPixels / 4));
		}
	}


	public void animate() {
		while (true) {
			// Sleep
			try {
				Thread.sleep(1000 / Settings.FPS);
			}
			catch (InterruptedException e) {
				return;
			}


		    // Update my movement
			Map<String, String> cmdMove = Communication.cmdMove(this.movingUp,
																this.movingLeft,
																this.movingDown,
																this.movingRight,
																this.getPlayerRotation());
			this.client.send(Communication.serialize(cmdMove));

			// Add bullets if shooting
			if (this.shooting && this.aimingAt != null) {
				Map<String, String> cmdShoot = Communication.cmdShoot();
				this.client.send(Communication.serialize(cmdShoot));
			}

			// Update bullets
			for (int i = 0; i < this.bullets.size(); i++) {
				Bullet bullet = this.bullets.get(i);
				if (this.checkPlayerCollision(bullet) ||
					this.level.collides(bullet, bullet.getVx(), bullet.getVy()) ||
					bullet.getX() < -1000 || bullet.getX() > 1000 ||
					bullet.getY() < -1000 || bullet.getY() > 1000)
				{
					this.bullets.remove(i);
					i--;
				}
				else
					bullet.move();
			}


			// Repaint
			this.repaint();
		}
	}


	private boolean checkPlayerCollision(Bullet bullet) {
		double bulletX = bullet.getX();
		double bulletY = bullet.getY();
		double bulletS = bullet.getSize();

		double bulletCenterX = bulletX + bulletS / 2;
		double bulletCenterY = bulletY + bulletS / 2;

		for (int playerId : this.players.keySet()) {
			Player player = this.players.get(playerId);
			if (player.isDead() || player.getWeapon().equals(bullet.getOriginWeapon()))
				continue;
			double playerX = player.getX();
			double playerY = player.getY();
			double playerS = player.getSize();

			if (bulletCenterX > playerX && bulletCenterX < playerX + playerS &&
				bulletCenterY > playerY && bulletCenterY < playerY + playerS)
			{
				return true;
			}
		}

		return false;
	}


	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W -> this.movingUp = true;
		case KeyEvent.VK_A -> this.movingLeft = true;
		case KeyEvent.VK_S -> this.movingDown = true;
		case KeyEvent.VK_D -> this.movingRight = true;
		case KeyEvent.VK_B -> this.showShop = !this.showShop;
		case KeyEvent.VK_1,
			KeyEvent.VK_2,
			KeyEvent.VK_3,
			KeyEvent.VK_4,
			KeyEvent.VK_5,
			KeyEvent.VK_6,
			KeyEvent.VK_7,
			KeyEvent.VK_8,
			KeyEvent.VK_9 ->
			{
			    Map<String, String> cmdUse = Communication.cmdUse(e.getKeyCode() - 48, 0);
				this.client.send(Communication.serialize(cmdUse));
			}
		}

		if (this.showShop)
			this.add(this.shop);
		else
			this.remove(this.shop);
	}


	@Override
	public void keyReleased(KeyEvent e) {
	    switch (e.getKeyCode()) {
		case KeyEvent.VK_W -> this.movingUp = false;
		case KeyEvent.VK_A -> this.movingLeft = false;
		case KeyEvent.VK_S -> this.movingDown = false;
		case KeyEvent.VK_D -> this.movingRight = false;
		}
	}

	
	@Override
	public void keyTyped(KeyEvent e) { }


	@Override
	public void mousePressed(MouseEvent e) {
		this.shooting = true;
		this.aimingAt = e.getPoint();

		Map<String, String> cmdMove = Communication.cmdMove(false, false, false, false,
															this.getPlayerRotation());
		this.client.send(Communication.serialize(cmdMove));
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		this.shooting = false;
	}


	@Override
	public void mouseClicked(MouseEvent e) { }


	@Override
	public void mouseEntered(MouseEvent e) { }


	@Override
	public void mouseExited(MouseEvent e) { }


	@Override
	public void mouseDragged(MouseEvent e) {
		this.shooting = true;
		this.aimingAt = e.getPoint();

		Map<String, String> cmdMove = Communication.cmdMove(false, false, false, false,
															this.getPlayerRotation());
		this.client.send(Communication.serialize(cmdMove));
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		this.aimingAt = e.getPoint();

		Map<String, String> cmdMove = Communication.cmdMove(false, false, false, false,
															this.getPlayerRotation());
		this.client.send(Communication.serialize(cmdMove));
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String commandStr = e.getActionCommand();
		this.client.send(commandStr);
	}

}

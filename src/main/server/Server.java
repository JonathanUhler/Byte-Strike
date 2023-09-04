package server;


import jnet.JServer;
import jnet.JClientSocket;
import jnet.Bytes;
import jnet.Log;
import entity.Player;
import entity.Bullet;
import world.Level;
import item.*;
import graphics.Settings;
import interfaces.Weapon;
import interfaces.Item;
import java.awt.Point;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class Server extends JServer {

	/** Next available player id. */
	private int nextPlayerId;

	/** Gets player id based on client connection. */
	private Map<JClientSocket, Integer> ids;
	/** Gets player object based on player id. */
	private Map<Integer, Player> players;
	/** Gets bullet object based on bullet id. */
	private List<Bullet> bullets;
	/** Current map. */
	private Level level;


	public Server(String ip, int port) throws IOException {
		super(ip, port);

		this.nextPlayerId = 0;

		this.ids = new HashMap<>();
		this.players = new HashMap<>();
		this.bullets = new ArrayList<>();
		this.level = new Level(1); // MARK: map selection

		this.animate();
	}


	private void animate() {
		while (true) {
			// Sleep
			try {
				Thread.sleep(1000 / Settings.FPS);
			}
			catch (InterruptedException e) {
				return;
			}

			// Update bullets
			for (int i = 0; i < this.bullets.size(); i++) {
				Bullet bullet = this.bullets.get(i);
				// Check for collision with level or player
				if (this.checkPlayerCollision(bullet) || // Player hit
					this.level.collides(bullet, bullet.getVx(), bullet.getVy()) || // Level hit
					bullet.getX() < -1000 || bullet.getX() > 1000 || // Out of bounds x
					bullet.getY() < -1000 || bullet.getY() > 1000) // Out of bounds y
				{
					this.bullets.remove(i);
					i--;
				}
				// Move bullet if no collision
				else
					bullet.move();
			}
		}
	}


	private boolean checkPlayerCollision(Bullet bullet) {
		Weapon weapon = bullet.getOriginWeapon();
		double bulletX = bullet.getX();
		double bulletY = bullet.getY();
		double bulletS = bullet.getSize();
		int baseDmg = bullet.getScaledDamage();

		double bulletCenterX = bulletX + bulletS / 2;
		double bulletCenterY = bulletY + bulletS / 2;

		for (int playerId : this.players.keySet()) {
			Player player = this.players.get(playerId);
			if (player.isDead() || player.getWeapon().equals(bullet.getOriginWeapon()))
				continue;
			double playerX = player.getX();
			double playerY = player.getY();
			double playerS = player.getSize();
			int dmg = player.isArmored() ? (int) (baseDmg * weapon.penetration()) : baseDmg;

			if (bulletCenterX > playerX && bulletCenterX < playerX + playerS &&
				bulletCenterY > playerY && bulletCenterY < playerY + playerS)
			{
				player.damage(dmg);
				Map<String, String> cmdDamaged = Communication.cmdDamaged(playerId, dmg);
				this.sendAll(Communication.serialize(cmdDamaged));
				if (player.isDead()) {
					// Reset the killed player
					Point randomTile = this.getRandomTile();
					player.setX(randomTile.x);
					player.setY(randomTile.y);
					player.reset();
					Map<String, String> cmdReset = Communication.cmdReset(playerId,
																		 randomTile.x,
																		 randomTile.y);
					this.sendAll(Communication.serialize(cmdReset));

					// Pay the player that got the kill
					int attackerId = this.getPlayerIdFromBullet(bullet);
					int moneyEarned = weapon.moneyPerKill();
					this.players.get(attackerId).pay(moneyEarned);
					Map<String, String> cmdPay = Communication.cmdPay(moneyEarned, attackerId);
					this.sendAll(Communication.serialize(cmdPay));
				}
				return true;
			}
		}

		return false;
	}


	public int getPlayerIdFromBullet(Bullet bullet) {
		Weapon weapon = bullet.getOriginWeapon();
		for (int playerId : this.players.keySet()) {
			Player player = this.players.get(playerId);
			if (player.getWeapon().equals(weapon))
				return playerId;
		}
		return -1;
	}


	@Override
	public void clientCommunicated(byte[] recv, JClientSocket clientSocket) {
		String commandStr = Bytes.bytesToString(recv);
		Map<String, String> command = Communication.deserialize(commandStr);
		String opcode = command.get(Communication.KEY_OPCODE);
		if (opcode == null) {
			Log.stdlog(Log.ERROR, "Server", "null opcode in command: " + command);
			return;
		}
		
		int playerId = this.ids.get(clientSocket);
		Player player = this.players.get(playerId);
		if (player == null || player.isDead())
			return;

		switch (opcode) {
		case Communication.OPCODE_MOVE: {
		    boolean up;
			boolean left;
			boolean down;
			boolean right;
			double rad;

			try {
				up = Boolean.parseBoolean(command.get(Communication.KEY_UP));
				left = Boolean.parseBoolean(command.get(Communication.KEY_LEFT));
				down = Boolean.parseBoolean(command.get(Communication.KEY_DOWN));
				right = Boolean.parseBoolean(command.get(Communication.KEY_RIGHT));
				rad = Double.parseDouble(command.get(Communication.KEY_RAD));
			}
			catch (Exception e) {
				Log.stdlog(Log.ERROR, "Server", "Can't parse move command: " + command + ", " + e);
				return;
			}

		    // Update movement
			double baseV = 0.11 * player.getWeapon().mobility();
			if (up) {
				player.setV(0, -baseV);
				while (this.level.collides(player, 0, player.getVy()))
					player.setV(0, player.getVy() / 2);
				player.move();
			}
			if (left) {
				player.setV(-baseV, 0);
				while (this.level.collides(player, player.getVx(), 0))
					player.setV(player.getVx() / 2, 0);
				player.move();
			}
			if (down) {
				player.setV(0, baseV);
				while (this.level.collides(player, 0, player.getVy()))
					player.setV(0, player.getVy() / 2);
				player.move();
			}
			if (right) {
				player.setV(baseV, 0);
				while (this.level.collides(player, player.getVx(), 0))
					player.setV(player.getVx() / 2, 0);
				player.move();
			}
			player.setRad(rad);

			Map<String, String> cmdPosPlayer = Communication.cmdPosPlayer(player, playerId);
			this.sendAll(Communication.serialize(cmdPosPlayer));
		    break;
		}
		case Communication.OPCODE_SHOOT: {
		    Weapon weapon = player.getWeapon();
			Bullet[] bulletsFired = weapon.fire(player.getX(), player.getY(), player.getRad());
			if (bulletsFired != null) {
				for (int bulletId = 0; bulletId < bulletsFired.length; bulletId++) {
					Bullet bullet = bulletsFired[bulletId];
					this.bullets.add(bullet);
					Map<String, String> cmdNewBullet = Communication.cmdNewBullet(bullet,
																				  bulletId,
																				  playerId);
					this.sendAll(Communication.serialize(cmdNewBullet));
				}
			}
		    break;
		}
		case Communication.OPCODE_BUY: {
		    String itemStr = command.get(Communication.KEY_ITEM);
			if (itemStr == null) {
				Log.stdlog(Log.ERROR, "Server", "No item in buy command: " + command);
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
				Log.stdlog(Log.ERROR, "Server", "invalid item bought: " + itemStr);
				return;
			}
			}

			boolean bought = player.buy(item);
			if (bought) {
				Map<String, String> updateBuy = Communication.cmdBuy(item, playerId);
			    this.sendAll(Communication.serialize(updateBuy));
			}
			break;
		}
		case Communication.OPCODE_USE: {
			int itemNum;

			try {
				itemNum = Integer.parseInt(command.get(Communication.KEY_ITEM_NUM));
			}
			catch (Exception e) {
				Log.stdlog(Log.ERROR, "Server", "Can't parse use command: " + command + ", " + e);
				return;
			}

<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
			Item item = player.getItem(itemNum);
>>>>>>> a1a5105 (tmp)
>>>>>>> 809b721 (tmp)
			boolean used = player.use(itemNum);
			if (used) {
				Map<String, String> updateUse = Communication.cmdUse(itemNum, playerId);
				this.sendAll(Communication.serialize(updateUse));
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======

				// Special case items
				if (item instanceof Grenade) {
					
				}
>>>>>>> a1a5105 (tmp)
>>>>>>> 809b721 (tmp)
			}
		}
		default:
			Log.stdlog(Log.ERROR, "Server", "invalid opcode: " + opcode);
			break;
		}
	}


	private Point getRandomTile() {
		int x = -1;
		int y = -1;

		while (x < 0 || y < 0 || this.level.isFilled(y, x)) {
			y = (int) (Math.random() * this.level.rows());
			x = (int) (Math.random() * this.level.cols(y));
		}

		return new Point(x, y);
	}


	@Override
	public void clientConnected(JClientSocket clientSocket) {
		int playerId = this.nextPlayerId;
		this.nextPlayerId++;

		Point randomTile = this.getRandomTile();
		Player player = new Player(randomTile.x, randomTile.y);
		this.ids.put(clientSocket, playerId);
		this.players.put(playerId, player);

		Map<String, String> cmdJoin = Communication.cmdJoin(playerId,
															player.getX(),
															player.getY(),
															this.level.toInteger());
		this.sendAll(Communication.serialize(cmdJoin));

		// Update this player with all other player positions
		for (int existingId : this.players.keySet()) {
			Player existingPlayer = this.players.get(existingId);
			Map<String, String> updateJoin = Communication.cmdJoin(existingId,
																   existingPlayer.getX(),
																   existingPlayer.getY(),
																   this.level.toInteger());
			this.send(Communication.serialize(updateJoin), clientSocket);
		}

		// Update this player will all the existing bullets
		for (Bullet existingBullet : this.bullets) {
			// Get the player that fired this bullet
			Weapon weaponUsed = existingBullet.getOriginWeapon();
		    int attackingId = -1;
		    for (int existingId : this.players.keySet()) {
				Player existingPlayer = this.players.get(existingId);
				if (existingPlayer.getWeapon().equals(weaponUsed))
					attackingId = existingId;
			}

			// Send the bullet update command
			Map<String, String> updateNewBullet = Communication.cmdNewBullet(existingBullet,
																			 1, attackingId);
			this.send(Communication.serialize(updateNewBullet), clientSocket);
		}
	}


	@Override
	public void clientDisconnected(JClientSocket clientSocket) {
		if (!this.ids.containsKey(clientSocket))
			return;
		
		int playerId = this.ids.get(clientSocket);
		this.ids.remove(clientSocket);
		this.players.remove(playerId);

		Map<String, String> cmdLeave = Communication.cmdLeave(playerId);
	    this.sendAll(Communication.serialize(cmdLeave));
	}

}

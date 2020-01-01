package gamePackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import gamePackage.shipPackage.Ship;
import gamePackage.shipPackage.TheBullet;
import gamePackage.shipPackage.Asteroid;
import gamePackage.shipPackage.TradeVessel;
import gamePackage.shotPackage.BorgShot;
import gamePackage.shotPackage.PDSShot;
import gamePackage.shotPackage.ShieldPiercingShot;
import gamePackage.shotPackage.ShortShot;
import gamePackage.shotPackage.Shot;
import gamePackage.shotPackage.StandardShot;
import gamePackage.shipPackage.Defiant;
import gamePackage.shipPackage.Galaxy;
import gamePackage.shipPackage.ImperialFrigate;
import gamePackage.shipPackage.JunkShip;
import gamePackage.shipPackage.LargeFreighter;
import gamePackage.shipPackage.MobileTradeStation;
import gamePackage.shipPackage.PlanetaryDefenseSystem;
import gamePackage.playerPackage.CardassianPlayer;
import gamePackage.shipPackage.Intrepid;
import gamePackage.playerPackage.Player;
import gamePackage.shipPackage.BorgCube;
import gamePackage.shipPackage.BorgSphere;
import gamePackage.shipPackage.CardassianBrig;
import gamePackage.shipPackage.CargoShip;
import gamePackage.shipPackage.ColonyShip;
import gamePackage.shipPackage.Constitution;

public class GameController extends PApplet {

	public List<Ship> ships;
	public List<Shot> shots;
	public List<Button> buttons;
	public int whoseTurn = -1;
	public int numOfPlayers = 2;
	public String selectedMap;
	public Map map;

	public static void main(String[] args) {
		PApplet.main("gamePackage.GameController");
	}

	public void settings() {
		fullScreen();
	}

	public void setup() {
		// style
		fill(255);
		noStroke();
		// load font
		PFont myFont = createFont("Oswald-Light.ttf", 30);
		textFont(myFont);
		textAlign(CENTER, CENTER);

		// setup arrays
		ships = new ArrayList<>();
		shots = new ArrayList<>();
		buttons = new ArrayList<>();

		// pregame interface

		// faction select
		String[] factionNames = new String[] { "United Federation of Planets", "Cardassian Empire", "Ferengi Alliance", "The Borg" };

		// i represents the row #
		for (int i = 0; i < 4; i++) {
			int leftMargin = 230;
			String group = "factionRow" + (i + 1);
			// j represents the components of each row
			for (int j = 0; j < factionNames.length; j++) {
				int width = round(textWidth(factionNames[j])) + 20;
				buttons.add(new Button(leftMargin, 150 + i * 75, width, 50, factionNames[j], 28, this, -1, "toggle, groupExclusive", group));
				leftMargin += width + 30;
			}
		}

		// map select

//@formatter:off
		
		String[][] mapNames = new String[][] { 
			{ "The Neutral Zone", "Chasm", "Battle at Wolf 359" }, // 2 player
			{ "Farpoint", "Battle of Bajor", "Zeon" }, // 3 player
			{ "Attack on Yadera Prime", "Paradox", "Tartarus" } }; // 4 player
			
//@formatter:on

		// i represents the row #
		for (int i = 0; i < mapNames.length; i++) {
			int leftMargin = 230;
			// j represents the components of each row
			for (int j = 0; j < mapNames[i].length; j++) {
				int width = round(textWidth(mapNames[i][j])) + 20;
				buttons.add(new Button(leftMargin, 620 + i * 75, width, 50, mapNames[i][j], 28, this, -1, "toggle, groupExclusive", "mapGroup"));
				leftMargin += width + 30;
			}
		}

		// Start Game button
		pushStyle();
		textSize(70);
		String goButtonText = "START GAME";
		int width = round(textWidth(goButtonText)) + 25;
		buttons.add(new Button(80, 900, width, 90, goButtonText, 65, this, -1, "gameStart", ""));
		popStyle();
	}

	public void draw() {
		background(0);

		if (whoseTurn == -1) { // pregame interface

			// Draw Faction Select Titles
			pushStyle();
			textAlign(LEFT, CENTER);
			textSize(50);
			text("FACTION SELECT", 80, 80);
			text("MAP SELECT", 80, 550);
			textSize(28);
			for (int i = 0; i < 4; i++) {
				text("PLAYER " + (i + 1), 80, 169 + i * 75);
			}

			// Draw Map Select Titles
			numOfPlayers = howManyPlayers();
			for (int i = 0; i < 3; i++) {
				int yCoord = 639 + i * 75;
				int color;
				if (numOfPlayers == i + 2) {
					color = 240;
					for (Button b : buttons) {
						if (b.y == yCoord - 19) {
							b.isEnabled = true;
						}
					}
				} else {
					color = 100;
					for (Button b : buttons) {
						if (b.y == yCoord - 19) {
							b.isSelected = false;
							b.isEnabled = false;
						}
					}

				}
				fill(color);
				text((i + 2) + "-PLAYER", 80, yCoord);

			}
			popStyle();

		} else { // main game

			map.update(whoseTurn);

			// deal with ships
			{
				Iterator<Ship> space = ships.iterator();
				while (space.hasNext()) {
					Ship o = (Ship) space.next();
					if (o.isDestroyed()) {
						if (o instanceof BorgSphere) {
							for (Ship x : ships) {
								if (x instanceof BorgCube && x.allegiance == o.allegiance)
									((BorgCube) x).removeSphereBuff();
							}
							// deals with death of a ferengi trade station
						} else if (o instanceof MobileTradeStation) {
							boolean found = false;
							for (Ship x : ships) {
								if (x instanceof MobileTradeStation && x != o && x.allegiance == o.allegiance) {
									found = true;
									break;
								}
							}
							if (!found) {
								for (Button b : buttons) {
									if (b.type.contains("tradeStation") && b.allegiance == o.allegiance)
										b.isEnabled = false;
								}
							}
						}
						space.remove();
						break; // Continue should be used instead? look into this
					}
					// ship collision procedure
					{
						Iterator<Ship> collider = ships.iterator();
						while (collider.hasNext()) {
							Ship otherShip = (Ship) collider.next();
							if (otherShip != o && o.collidesWith(otherShip)) {
								otherShip.takeDamage(-1);
								o.takeDamage(-1);
							}
						}
						Iterator<Player> baseCollider = map.players.iterator();
						while (baseCollider.hasNext()) {
							Player base = (Player) baseCollider.next();
							if (base.isDestroyed()) {
								baseCollider.remove();
								break; // Continue should be used instead? look into this
							}
							if (base.collidesWith(o)) {
								base.takeDamage();
								o.takeDamage(-1);
							}
						}
						// ship collides with asteroid
						for (Asteroid a : map.asteroids) {
							if (o.collidesWith(a)) {
								// build planetary defense system
								if (o instanceof PlanetaryDefenseSystem) {
									if (!a.pds())
										a.applyPDS(o);
								} else {
									o.takeDamage(-1);
									// build colony
									if (o instanceof ColonyShip && !a.colonized()) {
										a.buildColony(o.allegiance);
										for (Player p : map.players) {
											if (p.allegiance == a.allegiance) {
												((CardassianPlayer) p).colonyBuilt();
											}
										}
									}
								}

							}
						}
					}
					// shot ship collision procedure
					{
						Iterator<Shot> collider = shots.iterator();
						while (collider.hasNext()) {
							Shot s = (Shot) collider.next();
							if (o.collidesWith(s)) {
								if (s instanceof BorgShot)
									o.assimilate(s.allegiance);
								else if (s instanceof ShieldPiercingShot)
									o.takeDirectDamage(s.allegiance);
								else
									o.takeDamage(s.allegiance);
								collider.remove();
								break;
							}
						}
					}

					// normal procedure
					o.move();
					if (o.isHovered()) {
						o.updateInfo();
						o.showInfo();
					}
					if (o.isSelected) {
						o.findAngle();
						o.showMoveGuide();
						o.showSelection();
						if (mousePressed && mouseButton == RIGHT)
							o.showAimGuide();
						if (o instanceof BorgCube && keyPressed && key == 'a')
							((BorgCube) o).showAssimilationAimGuide();
					}
					if (o.isOffScreen())
						o.takeDirectDamage(-1);

					o.show(whoseTurn);
				}

			}
			{
				// shot timeout & asteroid procedure
				{
					Iterator<Shot> shotIterator = shots.iterator();
					while (shotIterator.hasNext()) {
						Shot s = (Shot) shotIterator.next();
						for (Asteroid a : map.asteroids) {
							if (a.collidesWith(s) && !(s instanceof PDSShot)) {
								if (a.colonized() || a.pds()) {
									if (s instanceof ShieldPiercingShot) {
										a.takeDirectDamage(s.allegiance);
									} else
										a.takeDamage(s.allegiance);
									if (a.colonized() && a.colonyIsDestroyed()) {
										// destroy colony
										a.destroyColony();
										for (Player p : map.players) {
											if (p.allegiance == a.allegiance) {
												((CardassianPlayer) p).colonyDestroyed();
											}
										}
									}
									if (a.pds() && a.pdsIsDestroyed()) {
										a.destroyPDS();
									}

								}
								shotIterator.remove();
								continue;
							}
						}
						if (s.timedOut())
							shotIterator.remove();
					}
				}
			}
			{
				Iterator<Player> baseCollider = map.players.iterator();
				while (baseCollider.hasNext()) {
					Player base = (Player) baseCollider.next();
					Iterator<Shot> collider = shots.iterator();
					while (collider.hasNext()) {
						Shot s = (Shot) collider.next();
						if (base.collidesWith(s)) {
							base.takeDamage();
							collider.remove();
							break; // neccesary?
						}
						if (base.isDestroyed()) {
							baseCollider.remove();
							break; // Continue should be used instead? look into this
						}
					}
				}
			}

			// deal with shots
			for (Shot s : shots) {
				s.move();
				s.show();
			}

			// there is only one player left
			if (map.players.size() == 1) {
				endGame();
			}
		}

		// in game interface
		for (Button b : buttons) {
			// check if it's okay for the game to start right now
			if (whoseTurn == -1 && b.type.contains("gameStart")) {
				// checking if all the groups have something selected
				boolean mapSelected = false;
				int factionRowsSelected = numOfPlayers;
				for (Button c : buttons) {
					if (c.group == "mapGroup" && c.isSelected) {
						mapSelected = true;
						selectedMap = c.text;
					}
				}
				boolean enoughPlayers = factionRowsSelected > 1;
				boolean allGroupsSelected = mapSelected && enoughPlayers;
				b.isEnabled = allGroupsSelected;
			}

			if (b.allegiance == whoseTurn) {
				b.show();
			}
		}

	}

	// only usable (and useful) while whoseTurn == -1
	public int howManyPlayers() {
		int count = 0;
		for (Button b : buttons) {
			if (b.group.contains("factionRow1") && b.isSelected)
				count++;
			else if (b.group.contains("factionRow2") && b.isSelected)
				count++;
			else if (b.group.contains("factionRow3") && b.isSelected)
				count++;
			else if (b.group.contains("factionRow4") && b.isSelected)
				count++;
		}
		return count;
	}

	public String[] getPlayerFactions() {
		ArrayList<String> temp = new ArrayList<>();
		for (Button b : buttons) {
			if (b.group.contains("factionRow1") && b.isSelected)
				temp.add(b.text);
			else if (b.group.contains("factionRow2") && b.isSelected)
				temp.add(b.text);
			else if (b.group.contains("factionRow3") && b.isSelected)
				temp.add(b.text);
			else if (b.group.contains("factionRow4") && b.isSelected)
				temp.add(b.text);
		}
		return temp.toArray(new String[temp.size()]);
	}

	public void addShip(String type, int allegiance, PVector basePosition, float startAngle) {

		switch (type) {
		case "Planetary Defense System":
			ships.add(new PlanetaryDefenseSystem(this, allegiance, basePosition, startAngle, type));
			break;
		case "Defiant Class":
			ships.add(new Defiant(this, allegiance, basePosition, startAngle, type));
			break;
		case "Intrepid Class":
			ships.add(new Intrepid(this, allegiance, basePosition, startAngle, type));
			break;
		case "Constitution Class":
			ships.add(new Constitution(this, allegiance, basePosition, startAngle, type));
			break;
		case "Galaxy Class":
			ships.add(new Galaxy(this, allegiance, basePosition, startAngle, type));
			break;
		case "Cardassian Brig":
			ships.add(new CardassianBrig(this, allegiance, basePosition, startAngle, type));
			break;
		case "Colony Ship":
			ships.add(new ColonyShip(this, allegiance, basePosition, startAngle, type));
			break;
		case "The Bullet":
			ships.add(new TheBullet(this, allegiance, basePosition, startAngle, type));
			break;
		case "Imperial Frigate":
			ships.add(new ImperialFrigate(this, allegiance, basePosition, startAngle, type));
			break;
		case "Trade Vessel":
			ships.add(new TradeVessel(this, allegiance, basePosition, startAngle, type));
			break;
		case "Junk Ship":
			ships.add(new JunkShip(this, allegiance, basePosition, startAngle, type));
			break;
		case "Cargo Ship":
			ships.add(new CargoShip(this, allegiance, basePosition, startAngle, type));
			break;
		case "Large Freighter":
			ships.add(new LargeFreighter(this, allegiance, basePosition, startAngle, type));
			break;
		case "Mobile Trade Station":
			ships.add(new MobileTradeStation(this, allegiance, basePosition, startAngle, type));
			break;
		case "Borg Cube":
			ships.add(new BorgCube(this, allegiance, basePosition, startAngle, type));
			break;
		case "Borg Sphere":
			ships.add(new BorgSphere(this, allegiance, basePosition, startAngle, type));
			break;
		}
	}

	public void addShot(String type, float t, PVector p, float r, int allegiance) {
		switch (type) {
		case "StandardShot":
			shots.add(new StandardShot(t, p, this, r, allegiance));
			break;
		case "PDSShot":
			shots.add(new PDSShot(t, p, this, r, allegiance));
			break;
		case "BorgShot":
			shots.add(new BorgShot(t, p, this, r, allegiance));
			break;
		case "ShieldPiercingShot":
			shots.add(new ShieldPiercingShot(t, p, this, r, allegiance));
			break;
		case "ShortShot":
			shots.add(new ShortShot(t, p, this, r, allegiance));
		}
	}

	public void nextTurn() {
		whoseTurn++;
		deselectAllShips();
		if (whoseTurn > numOfPlayers - 1) {
			whoseTurn = 0;
		}

		for (Ship s : ships) {
			if (s.allegiance == whoseTurn)
				s.reset();
			s.assimilationTick();
		}

		for (Player p : map.players) {
			if (p.allegiance == whoseTurn) {
				p.addScrap();
			}
		}
	}

	public void deselectAllShips() {
		for (Ship o : ships) {
			o.deselect();
		}
	}

	public boolean anyShipsHovered() {
		for (Ship o : ships) {
			if (o.isHovered())
				return true;
		}
		return false;
	}

	public void mouseReleased() {
		if (mouseButton == LEFT) {
			for (Ship o : ships) {
				if (o.isSelected) {
					if (anyShipsHovered())
						o.deselect();
					else
						o.boost();
				} else if (o.isHovered() && o.allegiance == whoseTurn) {
					o.select();
				}
			}
		} else if (mouseButton == RIGHT) {
			for (Ship o : ships) {
				if (o.isSelected) {
					o.shoot();
				}
			}
		}
	}

	public void startGame() {
		String[] factions = getPlayerFactions();
		map = new Map(this, selectedMap, factions);
		nextTurn();
	}

	public void mousePressed() {

		for (Button b : buttons) {
			if (b.isHovered() && b.allegiance == whoseTurn && b.isEnabled) {
				boolean oldState = b.isSelected;
				if (b.type.contains("groupExclusive")) {
					deselectButtonGroup(b.group);
				}
				b.isSelected = oldState;
				b.press();
			}
		}
		ArrayList<Button> buttonsCopy = new ArrayList<>(buttons);
		for (Button b : buttonsCopy) {
			if (b.isHovered() && whoseTurn == -1 && b.type.contains("gameStart")) {
				startGame();
			}
		}
	}

	public void deselectButtonGroup(String groupName) {
		for (Button b : buttons) {
			if (b.group == groupName) {
				b.deselect();
			}
		}
	}

	public void keyPressed() {
		switch (key) {
		case RETURN:
		case ENTER:
			nextTurn();
			break;
		}
	}

	public void keyReleased() {
		switch (key) {
		case 'a':
			for (Ship o : ships) {
				if (o instanceof BorgCube && o.isSelected) {
					((BorgCube) o).borgShoot();
				}
			}
		}
	}

	public void endGame() {
		noLoop();
		textSize(70);
		text(map.players.get(0).faction + " Is Victorious.", this.height / 2, this.width / 2);
	}

}
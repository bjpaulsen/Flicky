package gamePackage;

import java.util.ArrayList;
import java.util.List;

import gamePackage.playerPackage.BorgPlayer;
import gamePackage.playerPackage.CardassianPlayer;
import gamePackage.playerPackage.FederationPlayer;
import gamePackage.playerPackage.FerengiPlayer;
import gamePackage.playerPackage.Player;
import gamePackage.shipPackage.Asteroid;
import processing.core.PApplet;
import processing.core.PVector;

public class Map {
	public ArrayList<Player> players;
	public ArrayList<Asteroid> asteroids;
	public static final int INTERFACE_BUFFER = 400;

	public Map(PApplet display, String name, String[] factions) {
		players = new ArrayList<Player>();
		for (int playerAllegiance = 0; playerAllegiance < factions.length; playerAllegiance++) {

			float baseX = display.width / 2;
			float baseY = display.height / 2;

			// 2 player
			if (name == "The Neutral Zone" || name == "Battle at Wolf 359") {
				baseX = 150 + (display.width - 300 - INTERFACE_BUFFER) * playerAllegiance;
			}
			if (name == "Battle at Wolf 359" || name == "Chasm") {
				baseY = 150 + (display.height - 300) * playerAllegiance;
			}

			// 3 player
			if (name == "Farpoint" || name == "Battle of Bajor" || name == "Zeon") {
				if (playerAllegiance == 2) {
					baseY = 150;
					baseX = display.width / 2 - INTERFACE_BUFFER / 2;
				} else {
					baseX = 200 + (display.width - 400 - INTERFACE_BUFFER) * playerAllegiance;
					baseY = display.height - 150;
				}

				// 4 player
			} else if (name == "Attack on Yadera Prime" || name == "Paradox" || name == "Tartarus") {
				baseX = playerAllegiance < 2 ? 150 : display.width - 150 - INTERFACE_BUFFER;
				baseY = ((playerAllegiance + 1) % 2 == 0) ? 150 : display.height - 150;
			}

			PVector basePosition = new PVector(baseX, baseY);

			switch (factions[playerAllegiance]) {
			case "United Federation of Planets":
				players.add(new FederationPlayer(display, playerAllegiance, basePosition));
				break;
			case "Cardassian Empire":
				players.add(new CardassianPlayer(display, playerAllegiance, basePosition));
				break;
			case "Ferengi Alliance":
				players.add(new FerengiPlayer(display, playerAllegiance, basePosition));
				break;
			case "The Borg":
				players.add(new BorgPlayer(display, playerAllegiance, basePosition));
			}
		}
		asteroids = new ArrayList<Asteroid>();

		// asteroids
		switch (name) {
		case "The Neutral Zone":
			asteroids.add(new Asteroid(display, 150, 150));
			asteroids.add(new Asteroid(display, 600, 400));
			asteroids.add(new Asteroid(display, 900, 1000));
			asteroids.add(new Asteroid(display, 1200, 300));
			asteroids.add(new Asteroid(display, 500, 850));
			break;
		case "Chasm":
			asteroids.add(new Asteroid(display, 150, 200));
			asteroids.add(new Asteroid(display, 400, display.height / 2 + 100));
			for (int i = 0; i < 7; i++) {
				asteroids.add(new Asteroid(display, 600 + i*110, (int) Math.round(display.height/2+(Math.random()*90-90))));
			}
			asteroids.add(new Asteroid(display, 1600, display.height/2-150));
			asteroids.add(new Asteroid(display, 1250, 1050));
			break;
		case "Farpoint":
			asteroids.add(new Asteroid(display, display.width/2-200, display.height/2));
			asteroids.add(new Asteroid(display, display.width/2-250-200, display.height/2+150));
			asteroids.add(new Asteroid(display, display.width/2+150-200, display.height/2-150));
			asteroids.add(new Asteroid(display, display.width/2+100-200, display.height/2+500));
			asteroids.add(new Asteroid(display, display.width/2-300-200, display.height/2-300));
			asteroids.add(new Asteroid(display, display.width-150-200, 300));
			
		}

	}

	public void update(int whoseTurn) {
		for (Player p : players) {
			p.drawBase(whoseTurn);
			if (p.allegiance == whoseTurn) {
				p.showScrap();
				p.showLabels();
			}
		}
		for (Asteroid a : asteroids) {
			if (a.colonized())
				a.showColony(whoseTurn);
			a.show();
		}
	}

}

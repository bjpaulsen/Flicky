package gamePackage.shipPackage;

import java.util.ArrayList;
import java.util.Iterator;

import gamePackage.GameController;
import gamePackage.playerPackage.Player;
import gamePackage.shotPackage.Shot;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Asteroid {
	private PApplet display;
	public PVector position;
	public int radius, allegiance;
	private float tilt;
	private static final int NUM_SEGMENTS = 12;
	private int[] variations = new int[NUM_SEGMENTS];

	private boolean colonized = false, pds = false;
	private ArrayList<float[]> buildings;
	private int colonyHealth;
	private int pdsHealth;
	private int shieldLayers;
	private int shieldTimer;

	public Asteroid(PApplet a, int x, int y) {
		display = a;
		position = new PVector((float) (x + Math.random() * 10 - 10), (float) (y + Math.random() * 10 - 10));
		radius = (int) (45 + Math.random() * 65);
		tilt = (float) (Math.random() * Math.PI);

		for (int i = 0; i < variations.length; i++) {
			int variationFactor = radius / 3;
			variations[i] = (int) (Math.random() * (variationFactor) - (variationFactor) / 2);
		}
	}

	public void show() {

		shieldAnimation();
		display.pushStyle();
		display.pushMatrix();
		display.fill(Math.round(130 - radius * .55));
		display.translate(position.x, position.y);

		// vertex method
		display.beginShape();
		for (int i = 0; i < NUM_SEGMENTS; i++) {
			float angle = PApplet.map(i, 0, NUM_SEGMENTS, 0, (float) (Math.PI * 2)) + tilt;
			float r = radius + variations[i];
			float x = (float) (r * Math.cos(angle));
			float y = (float) (r * Math.sin(angle));
			display.vertex(x, y);
		}
		display.endShape(PConstants.CLOSE);

		display.popMatrix();
		display.popStyle();
	}

	public void showColony(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();

		display.translate(position.x, position.y);

		display.strokeWeight(10);
		display.strokeCap(PConstants.SQUARE);

		if (allegiance == whoseTurn)
			display.stroke(180);
		else
			display.stroke(230, 10, 50);

		for (int i = 0; i < buildings.size(); i++) {
			display.line(buildings.get(i)[0], buildings.get(i)[1], 0, 0);
		}

		display.popMatrix();
		display.popStyle();

		if (colonyHealth < 1) {
			destroyColony();
		}
	}

	public void buildColony(int allegiance) {
		colonized = true;
		this.allegiance = allegiance;
		colonyHealth = 2;

		int numCities = (int) (1 + Math.round(Math.random() * 4));
		buildings = new ArrayList<float[]>();

		float angle = (float) (Math.random() * Math.PI * 2);
		for (int i = 0; i < numCities; i++) {
			int numBuildings = (int) (3 + Math.round(Math.random() * 3));
			for (int j = 0; j < numBuildings; j++) {
				float r = (float) (10 + radius + Math.random() * 25);
				float x = (float) (r * Math.cos(angle));
				float y = (float) (r * Math.sin(angle));

				buildings.add(new float[] { x, y });

				angle += Math.PI / 20;
			}
			angle += Math.PI * 2 / numCities;
		}
	}

	public void destroyColony() {
		colonized = false;
	}

	public boolean colonized() {
		return colonized;
	}

	public boolean pds() {
		return pds;
	}

	public void applyPDS(Ship o) {
		pds = true;
		pdsHealth = 1;
		shieldLayers = 4;
		o.velocity = new PVector(0, 0);
		o.position.x = position.x - display.width / 2;
		o.position.y = position.y - display.height / 2;
		o.shotsPerTurn = 1;
		o.movesPerTurn = 0;
	}

	public void destroyPDS() {
		pds = false;
		shieldLayers = 0;
		Iterator<Ship> shipIterator = ((GameController) display).ships.iterator();
		while (shipIterator.hasNext()) {
			Ship o = (Ship) shipIterator.next();
			if (o.translatedPos.x == position.x && o.translatedPos.y == position.y) {
				shipIterator.remove();
				break;
			}
		}
	}

	public boolean collidesWith(Shot a) {
		float d = PApplet.dist(position.x, position.y, a.position.x, a.position.y);
		return d < this.radius;
	}

	public boolean colonyIsDestroyed() {
		return colonyHealth < 1;
	}

	public boolean pdsIsDestroyed() {
		return pdsHealth < 1;
	}

	public boolean shieldFails() {
		return Math.random() * 6 > shieldLayers;
	}

	public void takeDamage(int allegiance) {
		if (shieldFails()) {
			takeDirectDamage(allegiance);
		} else {
			shieldTimer = 1;
			shieldAnimation();
		}
	}
	
	public void takeDirectDamage(int allegiance) {
		colonyHealth--;
		pdsHealth--;
		if ((pds() && pdsIsDestroyed()) || (colonized() && colonyIsDestroyed())) {
			for (Player p : ((GameController) display).map.players) {
				if (p.allegiance == allegiance) {
					p.killReward();
					break;
				}
			}
		}
	}

	public void shieldAnimation() {
		if (shieldTimer > 0) {
			display.pushStyle();
			display.pushMatrix();
			display.noFill();
			display.strokeWeight(6);
			display.stroke(20, 10, 210);

			display.arc(position.x, position.y, radius + 120, radius + 120, (float) (float) (-Math.cos((shieldTimer) * Math.PI / 180) * Math.PI),
					(float) (2 * -Math.cos((shieldTimer) * Math.PI / 180) * Math.PI));

			display.popMatrix();
			display.popStyle();
			shieldTimer += 4;
		}
		if (shieldTimer > 360)
			shieldTimer = 0;
	}
}

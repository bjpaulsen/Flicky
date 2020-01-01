package gamePackage.shipPackage;

import gamePackage.GameController;
import gamePackage.playerPackage.BorgPlayer;
import gamePackage.playerPackage.FerengiPlayer;
import gamePackage.playerPackage.Player;
import gamePackage.shotPackage.Shot;
import gamePackage.InfoPanel;
import processing.core.PApplet;
import processing.core.PVector;

public abstract class Ship {
	public float MAX_VELOCITY = 7;
	private static final float MOUSE_BOOST_MULTIPLIER = (float) .03;
	public static final float DRAG = (float) .98;

	public PApplet display;
	public PVector position, velocity, translatedPos;
	public float angle, radius;
	public boolean isSelected, isAiming;

	public int allegiance, health, movesPerTurn, movesLeft, shotsPerTurn, shotsLeft, shieldLayers, assimilationTimer;
	private int shieldTimer = 0, assimilationAnimationTimer = 0;

	private String name;
	private InfoPanel info;
	public String description = "";
	private int originalShieldLayers;

	public Ship(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		display = a;

		this.name = name;
		PVector start = new PVector(basePosition.x, basePosition.y);

		position = start.sub(new PVector(display.width / 2, display.height / 2)).add(PVector.fromAngle(startAngle).mult(70));
		translatedPos = new PVector(position.x + display.width / 2, position.y + display.height / 2);

		velocity = new PVector(0, 0);
		angle = 0;
		radius = 10;
		isSelected = false;
		isAiming = false;

		allegiance = al;
		health = 1;
		movesPerTurn = 1;
		shotsPerTurn = 1;
		shieldLayers = 0;
		assimilationTimer = 0;
	}

	public abstract void show(int whoseTurn);

	public void generateInfo() {
		info = new InfoPanel(display, name, description, health, shieldLayers, shotsPerTurn, movesPerTurn);
	}

	public void updateInfo() {
		info.updateInfo(health, shieldLayers, shotsPerTurn, movesPerTurn);
	}

	// update movement function, includes drag and stop
	public void move() {
		position.add(velocity);
		velocity.mult(DRAG);
		if (velocity.mag() <= .04) {
			velocity = new PVector(0, 0);
		}
		translatedPos = new PVector(position.x + display.width / 2, position.y + display.height / 2);
	}

	public void findAngle() {
		if (!translatedPos.equals(new PVector(display.mouseX, display.mouseY)))
			angle = (float) Math.atan2(translatedPos.y - display.mouseY, translatedPos.x - display.mouseX);
	}

	public void showMoveGuide() {
		display.pushStyle();
		display.stroke(255, 100);
		display.strokeWeight(2);

		if (!translatedPos.equals(new PVector(display.mouseX, display.mouseY))) {
			PVector mouseVector = new PVector(display.mouseX, display.mouseY).sub(translatedPos);
			float limitedDist = PApplet.constrain(mouseVector.mag(), 0, MAX_VELOCITY / MOUSE_BOOST_MULTIPLIER);
			PVector limitedVector = PVector.fromAngle((float) (angle - Math.PI)).mult(limitedDist);
			limitedVector.add(translatedPos);
			float totalDots = limitedDist / 14;

			for (int i = 0; i <= totalDots; i++) {
				display.stroke(255, 255, 255, 255 - i * 196/(MAX_VELOCITY*2));
				float x = PApplet.lerp(translatedPos.x, limitedVector.x, (float) (i / totalDots));
				float y = PApplet.lerp(translatedPos.y, limitedVector.y, (float) (i / totalDots));
				display.point(x, y);
			}
		}

		display.popStyle();
	}

	public void showAimGuide() {
		display.pushStyle();
		display.strokeWeight(2);

		float hypotenuse = 280;
		float aimX = (float) (hypotenuse * Math.cos(angle));
		float aimY = (float) (hypotenuse * Math.sin(angle));

		for (int i = 0; i < 12; i++) {
			display.stroke(255, 0, 0, 255 - i * 20);
			float x = PApplet.lerp(translatedPos.x, translatedPos.x + aimX, (float) (i / (float) 12));
			float y = PApplet.lerp(translatedPos.y, translatedPos.y + aimY, (float) (i / (float) 12));
			display.point(x, y);
		}

		display.popStyle();
	}

	// begin movement
	public void boost() {
		if (movesLeft > 0) {
			PVector mouseVector = new PVector(display.mouseX, display.mouseY).sub(translatedPos);
			velocity.add(PVector.fromAngle(angle).mult(PApplet.constrain((float) (mouseVector.mag() * .03), 0, MAX_VELOCITY)));
			movesLeft--;
		}
	}

	public boolean isHovered() {
		float mousedist = PApplet.dist(display.mouseX, display.mouseY, translatedPos.x, translatedPos.y);
		return mousedist <= radius + 5;
	}

	public void showSelection() {
		display.pushMatrix();
		display.pushStyle();
		display.noFill();
		display.stroke(255, 100);

		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		float d = 2 * radius + 30;
		display.ellipse(0, 0, d, d);

		display.popStyle();
		display.popMatrix();
	}

	public void select() {
		isSelected = true;
	}

	public void deselect() {
		isSelected = false;
	}

	public void shoot() {
		if (shotsLeft > 0) {
			((GameController) display).addShot("StandardShot", angle, translatedPos, radius, allegiance);
			shotsLeft--;
		}
	}

	public boolean shieldFails() {
		return Math.random() * 6 > shieldLayers;
	}

	// method overload
	public boolean collidesWith(Ship a) {
		float d = PApplet.dist(translatedPos.x, translatedPos.y, a.translatedPos.x, a.translatedPos.y);
		return d < this.radius + a.radius;
	}

	// method overload
	public boolean collidesWith(Shot a) {
		float d = PApplet.dist(translatedPos.x, translatedPos.y, a.position.x, a.position.y);
		return d < this.radius;
	}

	// method overload
	public boolean collidesWith(Asteroid a) {
		float d = PApplet.dist(translatedPos.x, translatedPos.y, a.position.x, a.position.y);
		return d < this.radius + a.radius;
	}

	public void shieldAnimation() {
		if (shieldTimer > 0) {
			display.pushStyle();
			display.pushMatrix();
			display.noFill();
			display.strokeWeight(6);
			display.stroke(20, 10, 210);

			display.arc(translatedPos.x, translatedPos.y, radius + 50, radius + 50, (float) (float) (-Math.cos((shieldTimer) * Math.PI / 180) * Math.PI),
					(float) (2 * -Math.cos((shieldTimer) * Math.PI / 180) * Math.PI));

			display.popMatrix();
			display.popStyle();
			shieldTimer += 4;
		}
		if (shieldTimer > 360)
			shieldTimer = 0;
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
		health--;
		if (isDestroyed()) {
			for (Player p : ((GameController) display).map.players) {
				if (p.allegiance == allegiance) {
					p.killReward();
					break;
				}
			}
		}
	}

	public void assimilate(int allegiance) {
		if (shieldFails()) {
			health--;
			if (health < 1) {
				for (Player p : ((GameController) display).map.players) {
					if (this.allegiance == p.allegiance && p instanceof FerengiPlayer && (this instanceof CargoShip || this instanceof LargeFreighter)) {
						((FerengiPlayer) p).loseCargo();
						break;
					}
				}
				health = 1;
				this.allegiance = allegiance;
				assimilationTimer = 2 * ((GameController) display).numOfPlayers;
				originalShieldLayers = shieldLayers;
				shieldLayers = assimilationTimer + 2;
				shotsLeft = 0;
				movesLeft = 0;
			}
		} else {
			shieldTimer = 1;
			shieldAnimation();
		}
	}

	public void assimilationTick() {
		if (assimilationTimer > 0) {
			assimilationTimer--;
			shieldLayers = assimilationTimer + 2;
			if (assimilationTimer > 0) {
				shotsLeft = 0;
				movesLeft = 0;
			} else {
				assimilationAnimationTimer = 0;
				shieldLayers = originalShieldLayers;
			}
		}
	}

	public void assimilationAnimation() {
		if (assimilationTimer > 0) {
			display.pushStyle();
			display.pushMatrix();
			display.noFill();
			display.strokeWeight(6);
			display.stroke(0, 200, 50);

			display.arc(translatedPos.x, translatedPos.y, (float) (-Math.cos(assimilationAnimationTimer * Math.PI / 180) * radius * 4),
					(float) (-Math.cos(assimilationAnimationTimer * Math.PI / 180) * radius * 4), 0, (float) (Math.PI * 2));

			display.popMatrix();
			display.popStyle();
			assimilationAnimationTimer += 2;
		}
	}

	public boolean isDestroyed() {
		return health < 1;
	}

	public boolean isOffScreen() {
		// TODO Auto-generated method stub
		return false;
	}

	public void reset() {
		movesLeft = movesPerTurn;
		shotsLeft = shotsPerTurn;
	}

	public void showInfo() {
		info.show();
	}
}

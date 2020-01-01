package gamePackage.shipPackage;

import gamePackage.GameController;
import gamePackage.playerPackage.FerengiPlayer;
import gamePackage.playerPackage.Player;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class CargoShip extends Ship {

	public CargoShip(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 16;
		health = 3;
		MAX_VELOCITY = 5;
		
		description = "Slow, and heavily armored to protect valuable cargo that is lost upon death.";

		generateInfo();
	}

	@Override
	public void show(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();

		shieldAnimation();
		assimilationAnimation();

		display.strokeWeight(5);

		float r = radius;

		if (allegiance != whoseTurn) {
			display.stroke(230, 10, 50);
			display.fill(230, 10, 50);
		} else {
			display.stroke(255);
		}

		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		display.rotate(angle + PConstants.HALF_PI);

		display.ellipse(0, 0, (float) 2 * r, (float) 2 * r);

		display.strokeWeight(8);
		display.stroke(0);
		display.line(0F, 2F, 0F, (float) r + 2);

		display.popMatrix();
		display.popStyle();
	}

	public void takeDamage(int enemyAl) {
		super.takeDamage(enemyAl);
		if (isDestroyed()) {
			for (Player p : ((GameController) display).map.players) {
				if (p.allegiance == allegiance)
					((FerengiPlayer) p).loseCargo();
			}
		}
	}

}

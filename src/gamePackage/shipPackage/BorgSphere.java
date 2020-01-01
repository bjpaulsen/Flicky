package gamePackage.shipPackage;

import gamePackage.GameController;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class BorgSphere extends Ship {

	public BorgSphere(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 14;
		shotsPerTurn = 1;
		movesPerTurn = 1;
		MAX_VELOCITY = 9;
		
		for (Ship o : ((GameController) display).ships) {
			if (o instanceof BorgCube && o.allegiance == allegiance)
				((BorgCube) o).applySphereBuff();
		}

		description = "Improves Borg Cubes' shields and engines. Wields a short-range defense cannon.";
		
		generateInfo();
	}
	
	public void show(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();

		shieldAnimation();
		assimilationAnimation();
		
		display.strokeWeight(6);
		display.stroke(255);
		display.noFill();

		float d = 2*radius;

		if (allegiance != whoseTurn) {
			display.stroke(230, 10, 50);
		}

		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		display.rotate(angle + PConstants.HALF_PI);

		display.arc(0, 0, d, d, 0, (float) (Math.PI*2));

		display.popMatrix();
		display.popStyle();
	}

	public void shoot() {
		if (shotsLeft > 0) {
			((GameController) display).addShot("ShortShot", angle, translatedPos, radius, allegiance);
			shotsLeft--;
		}
	}
}

package gamePackage.shipPackage;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class ColonyShip extends Ship {

	public ColonyShip(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		shotsPerTurn = 0;
		radius = 12;
		description = "Lands on an asteroid to build a scrap-producing outpost.";
		generateInfo();
	}
	
	public void show(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();

		shieldAnimation();
		assimilationAnimation();
		
		float r = radius;

		if (allegiance != whoseTurn) {
			display.fill(230, 10, 50);
		}

		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		display.rotate(angle + PConstants.HALF_PI);

		display.triangle(-r+3, r, 0, -r-2, r-3, r);
		display.ellipse(0, -r, 10, 10);

		display.popMatrix();
		display.popStyle();
	}

}

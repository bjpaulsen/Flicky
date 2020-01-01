package gamePackage.shotPackage;

import processing.core.PApplet;
import processing.core.PVector;

public class ShieldPiercingShot extends StandardShot {

	public ShieldPiercingShot(float t, PVector p, PApplet a, float r, int allegiance) {
		super(t, p, a, r, allegiance);
	}
	
	public void show() {
		display.pushStyle();
		display.strokeWeight(4);
		display.stroke(220, 0, 150, 255-deathCount*3);

		display.point(position.x, position.y);

		display.popStyle();
	}

}

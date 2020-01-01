package gamePackage.shotPackage;

import processing.core.PApplet;
import processing.core.PVector;

public class BorgShot extends StandardShot {

	public BorgShot(float t, PVector p, PApplet a, float r, int allegiance) {
		super(t, p, a, r, allegiance);
	}

	@Override
	public void show() {
		display.pushStyle();
		display.strokeWeight(4);
		display.stroke(0, 200, 50, 250-deathCount*3);

		display.point(position.x, position.y);

		display.popStyle();
	}
}

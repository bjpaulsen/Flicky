package gamePackage.shotPackage;

import processing.core.PApplet;
import processing.core.PVector;

public class PDSShot extends StandardShot {

	public PDSShot(float t, PVector p, PApplet a, float r, int allegiance) {
		super(t, p, a, r, allegiance);
		maxTimer = 55;
	}
	
	public void show() {
		display.pushStyle();
		display.strokeWeight(5);
		display.stroke(0, 100, 255, (float) (255-deathCount*3.5));

		display.point(position.x, position.y);

		display.popStyle();
	}

}

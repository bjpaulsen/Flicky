package gamePackage.shotPackage;

import processing.core.PApplet;
import processing.core.PVector;

public class StandardShot extends Shot {


	// creates a shot with an angle t, location p, display a, and offsets it by r
	public StandardShot(float t, PVector p, PApplet a, float r, int allegiance) {
		display = a;
		trajectory = t;
		speed = 9;

		PVector offset = PVector.fromAngle(trajectory).mult(r * 2);
		PVector duplicate = new PVector(p.x, p.y);
		duplicate.add(offset);
		position = duplicate;

		velocity = PVector.fromAngle(trajectory).mult(speed);

		maxTimer = 45;
		this.allegiance = allegiance;
	}

	public void show() {
		display.pushStyle();
		display.strokeWeight(4);
		display.stroke(150, 150, 150, (float) (255-deathCount*4.5));

		display.point(position.x, position.y);

		display.popStyle();
	}
}

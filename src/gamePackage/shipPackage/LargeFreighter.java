package gamePackage.shipPackage;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class LargeFreighter extends CargoShip {

	public LargeFreighter(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 24;
		health = 6;
		shotsPerTurn = 2;
		MAX_VELOCITY = 3;
		
		description = "Very slow, very heavily armored, and well armed to protect valuable cargo that is lost upon death.";

		generateInfo();
	}
	
	public void show(int whoseTurn) {
		super.show(whoseTurn);
		display.pushStyle();
		display.pushMatrix();
		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		display.rotate(angle + PConstants.HALF_PI);
		display.stroke(0);
		display.strokeWeight(15);
		display.line(0, -radius-2, 0, -radius+3);
		display.popMatrix();
		display.popStyle();
	}

}

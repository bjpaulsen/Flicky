package gamePackage.shipPackage;

import processing.core.PApplet;
import processing.core.PVector;

public class Galaxy extends Ship {

	public Galaxy(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 21;
		shotsPerTurn = 2;
		health = 2;
		shieldLayers = 3;
		movesPerTurn = 2;
		
		description = "The Federation flagship, the Galaxy Class commands the respect of the entire Alpha Quadrant.";
		
		generateInfo();
	}

	public void show(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();
		
		shieldAnimation();
		assimilationAnimation();

		float d = 2 * radius;
		
		display.strokeWeight(2);

		if (allegiance != whoseTurn)
			display.fill(230, 10, 50);
		

		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		display.rotate(angle);

		display.ellipse(0, 0, d, d);
		if (allegiance == whoseTurn)
			display.stroke(255);
		else
			display.stroke(230, 10, 50);
		display.line(0, 0, -d, 0);
		display.line(0, -7, -d+5, -7);
		display.line(0, 7, -d+5, 7);

		display.popMatrix();
		display.popStyle();
	}
}

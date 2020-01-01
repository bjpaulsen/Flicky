package gamePackage.shipPackage;

import processing.core.PApplet;
import processing.core.PVector;

public class Intrepid extends Ship {

	public Intrepid(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 12;
		health = 2;
		shieldLayers = 1;
		movesPerTurn = 3;
		
		description = "A science vessel, the Intrepid Class is best known for its impressive speed.";
		
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

		display.ellipse(0, 0, d+10, d-5);
		if (allegiance == whoseTurn)
			display.stroke(255);
		else
			display.stroke(230, 10, 50);
		display.line(0, -4, -d-5, -4);
		display.line(0, 4, -d-5, 4);

		display.popMatrix();
		display.popStyle();
	}
}




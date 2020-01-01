package gamePackage.shipPackage;

import processing.core.PVector;

import java.awt.Color;

import processing.core.PApplet;

public class Defiant extends Ship {

	public Defiant(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 12;
		shieldLayers = 0;
		
		description = "A small fighter built to battle The Borg.";
		
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

		display.popMatrix();
		display.popStyle();
	}
}

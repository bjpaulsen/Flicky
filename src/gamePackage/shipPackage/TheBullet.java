package gamePackage.shipPackage;

import gamePackage.GameController;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class TheBullet extends Ship {

	public TheBullet(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 15;
		movesPerTurn = 5;
		
		description = "Fragile, extremely quick, and strikes with a unique weapon that pierces shields.";
		
		generateInfo();
	}
	
	public void show(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();

		display.rectMode(PConstants.CENTER);
		
		shieldAnimation();
		assimilationAnimation();
		
		float r = radius;

		if (allegiance != whoseTurn) {
			display.fill(230, 10, 50);
		}

		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		display.rotate(angle + PConstants.HALF_PI);

		display.rect(0, r/2-3, r, (float) (r*2));
		display.triangle(-r/2, -r/2-2, r/2, -r/2-2, 0, -2*r);
		
		if (allegiance != whoseTurn)
			display.stroke(230, 10, 50);
		else
			display.stroke(255);
		display.strokeWeight(2);
		display.line(-r/2+1, r/2-3+r, -r/2+1, r/2-2+r+5);
		display.line(r/2-1, r/2-3+r, r/2-1, r/2-2+r+5);

		display.popMatrix();
		display.popStyle();
	}
	
	public void shoot() {
		if (shotsLeft > 0) {
			((GameController) display).addShot("ShieldPiercingShot", angle, translatedPos, radius, allegiance);
			shotsLeft--;
		}
	}

}

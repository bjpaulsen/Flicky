package gamePackage.shipPackage;

import gamePackage.GameController;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class BorgCube extends Ship {
	
	public boolean isAimingAssimilation = false;
	
	public BorgCube(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 17;
		shieldLayers = 2;
		health = 2;
		movesPerTurn = 2;
		
		for (Ship o : ((GameController) display).ships) {
			if (o instanceof BorgSphere && o.allegiance == allegiance)
				applySphereBuff();
		}
		
		description = "The Borg's colossal warship. Assimilates ships [A] or destroys them for scrap.";
		generateInfo();
	}
	
	public void show(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();
		display.rectMode(PApplet.CENTER);

		shieldAnimation();
		assimilationAnimation();

		float d = 2*radius;

		if (allegiance != whoseTurn) {
			display.fill(230, 10, 50);
		}

		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		display.rotate(angle + PConstants.HALF_PI);

		display.rect(0, 0, d, d);

		display.popMatrix();
		display.popStyle();
	}
	
	public void borgShoot() {
		if (shotsLeft > 0) {
			((GameController) display).addShot("BorgShot", angle, translatedPos, radius, allegiance);
			shotsLeft--;
		}
	}
	
	public void showAssimilationAimGuide() {
		display.pushStyle();
		display.strokeWeight(2);

		float hypotenuse = 400;
		float aimX = (float) (hypotenuse * Math.cos(angle));
		float aimY = (float) (hypotenuse * Math.sin(angle));

		for (int i = 0; i < 15; i++) {
			display.stroke(0, 200, 50, 255 - i * 15);
			float x = PApplet.lerp(translatedPos.x, translatedPos.x + aimX, (float) (i / (float) 15));
			float y = PApplet.lerp(translatedPos.y, translatedPos.y + aimY, (float) (i / (float) 15));
			display.point(x, y);
		}

		display.popStyle();
	}

	public void applySphereBuff() {
		shieldLayers++;
		movesPerTurn++;
	}
	
	public void removeSphereBuff() {
		shieldLayers--;
		movesPerTurn--;
	}
}

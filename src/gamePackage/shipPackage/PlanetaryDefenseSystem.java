package gamePackage.shipPackage;

import gamePackage.GameController;
import processing.core.PApplet;
import processing.core.PVector;

public class PlanetaryDefenseSystem extends Ship {

	public PlanetaryDefenseSystem(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 12;
		shotsPerTurn = 0;
		health = 1;
		shieldLayers = 3;
		
		description = "Lands on an asteroid to defend a large region around it.";
		
		generateInfo();
	}

	@Override
	public void show(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();
		
		shieldAnimation();
		assimilationAnimation();

		display.strokeWeight(4);
		
		float d = 2 * radius;

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
		display.line(d*.80F, 0, -d*.80F, 0);
		display.line(0, -d*.80F, 0, d*.80F);

		display.popMatrix();
		display.popStyle();
		
	}
	
	public void showAimGuide() {
		display.pushStyle();
		display.strokeWeight(2);

		float hypotenuse = 450;
		float aimX = (float) (hypotenuse * Math.cos(angle));
		float aimY = (float) (hypotenuse * Math.sin(angle));

		for (int i = 0; i < 16; i++) {
			display.stroke(0, 100, 255, 200 - i * 15);
			float x = PApplet.lerp(translatedPos.x, translatedPos.x + aimX, (float) (i / (float) 16));
			float y = PApplet.lerp(translatedPos.y, translatedPos.y + aimY, (float) (i / (float) 16));
			display.point(x, y);
		}

		display.popStyle();
	}
	
	public void shoot() {
		if (shotsLeft > 0) {
			((GameController) display).addShot("PDSShot", angle, translatedPos, radius, allegiance);
			shotsLeft--;
		}
	}

}

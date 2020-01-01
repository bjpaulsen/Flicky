package gamePackage.shipPackage;

import gamePackage.GameController;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class JunkShip extends Ship {

	public JunkShip(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 13;

		description = "A cheap junk ship whose engines and weapons are prone to failure.";

		generateInfo();
	}

	public void show(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();

		shieldAnimation();
		assimilationAnimation();

		display.noFill();
		display.strokeWeight(5);

		float r = radius;

		if (allegiance != whoseTurn)
			display.stroke(230, 10, 50);
		else
			display.stroke(255);

		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		display.rotate(angle + PConstants.HALF_PI);

		display.arc(0F, 0F, (float) 2 * r, (float) 2 * r, (float) -Math.PI / 6, (float) (Math.PI + Math.PI / 6));
		display.line(0F, 2F, 0F, (float) r - 3);

		display.popMatrix();
		display.popStyle();
	}

	@Override
	public void boost() {
		if (movesLeft > 0) {
			PVector mouseVector = new PVector(display.mouseX, display.mouseY).sub(translatedPos);
			float die = (float) (Math.random() * 6);
			if (die >= 3)
				velocity.add(PVector.fromAngle(angle).mult(PApplet.constrain((float) (mouseVector.mag() * .03), 0, 7)));
			else if (die >= 1)
				velocity.add(PVector.fromAngle(angle).mult(PApplet.constrain((float) (mouseVector.mag() * .03) / 2, 0, 7)));
			else
				velocity.add(PVector.fromAngle(angle));
			movesLeft--;
		}
	}

	@Override
	public void shoot() {
		float die = (float) (Math.random() * 6);
		if (shotsLeft > 0) {
			if (die >= 2.8)
				((GameController) display).addShot("StandardShot", angle, translatedPos, radius, allegiance);
			shotsLeft--;
		}
	}

}

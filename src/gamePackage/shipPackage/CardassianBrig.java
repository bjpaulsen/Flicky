package gamePackage.shipPackage;

import processing.core.PVector;
import gamePackage.GameController;
import processing.core.PApplet;
import processing.core.PConstants;

public class CardassianBrig extends Ship {

	public CardassianBrig(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 12;
		
		description = "The frontline of the Cardassian Imperial Fleet.";
		
		generateInfo();
	}

	public void show(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();

		shieldAnimation();
		assimilationAnimation();
		
		float r = radius;

		if (allegiance != whoseTurn) {
			display.fill(230, 10, 50);
		}

		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		display.rotate(angle + PConstants.HALF_PI);

		display.triangle(-r, r, 0, -r, r, r);

		display.popMatrix();
		display.popStyle();
	}
	
}

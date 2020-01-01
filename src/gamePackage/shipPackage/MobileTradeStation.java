package gamePackage.shipPackage;

import gamePackage.Button;
import gamePackage.GameController;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class MobileTradeStation extends Ship {

	public MobileTradeStation(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 25;
		shotsPerTurn = 0;
		movesPerTurn = 1;
		MAX_VELOCITY = 9;
		
		for (Button b : ((GameController) display).buttons) {
			if (b.type.contains("tradeStation") && b.allegiance == allegiance)
				b.isEnabled = true;
		}
		
		description = "Allows the Ferengi to purchase more and better ships.";
		
		generateInfo();
	}

	@Override
	public void show(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();

		shieldAnimation();
		assimilationAnimation();
		
		display.strokeWeight(5);
		display.stroke(255);
		display.noFill();

		float d = 2*radius;

		if (allegiance != whoseTurn) {
			display.stroke(230, 10, 50);
		}

		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		display.rotate(angle + PConstants.HALF_PI);

		display.ellipse(0, 0, (float) (radius*.85), (float) (radius*.85));
		display.arc(0, 0, d, d, 0F, (float) (Math.PI));
		display.line(0, radius, 0, radius+10);

		display.popMatrix();
		display.popStyle();	}

}

package gamePackage.shipPackage;

import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PConstants;

public class TradeVessel extends Ship {

	public TradeVessel(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 15;
		
		description = "Armed to aid in high-risk \"trading\" missions.";

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

		display.arc(0F, 0F, (float) 2*r, (float) 2*r, 0F, (float) Math.PI);
		display.line(0F, -10F, 0F, (float) 1*r-3); 
		

		display.popMatrix();
		display.popStyle();
	}

}

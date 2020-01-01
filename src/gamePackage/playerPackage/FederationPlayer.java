package gamePackage.playerPackage;

import gamePackage.GameController;
import gamePackage.Map;
import processing.core.PApplet;
import processing.core.PVector;

public class FederationPlayer extends Player {

	public FederationPlayer(PApplet display, int allegiance, PVector basePosition) {
		super(display, "United Federation of Planets", allegiance, basePosition);
		baseName = 'U';
		scrap = -3;
		
		//@formatter:off
		String[][] temp = {	
				{"Planetary Defense System", "3"},
				{"Defiant Class", "3"},
				{"Intrepid Class", "5"},
				{"Constitution Class", "7"},
				{"Galaxy Class", "11"}
				};
		
		shipCosts = temp;
		//@formatter:on

		createButtons();
		
		((GameController) display).addShip("Defiant Class", allegiance, basePosition, startAngle);
		startAngle += Math.PI/4;
		((GameController) display).addShip("Planetary Defense System", allegiance, basePosition, startAngle);
		startAngle += Math.PI/4;
		((GameController) display).addShip("Defiant Class", allegiance, basePosition, startAngle);
		startAngle += Math.PI/4;
	}

	public void drawBase(int whoseTurn) {
		display.pushStyle();
		display.noStroke();
		if(whoseTurn != allegiance)
			display.fill(230, 10, 50);
		display.ellipse(basePosition.x, basePosition.y, radius * 2, radius * 2);
		display.popStyle();
		drawLetter();
	}

	@Override
	public void addScrap() {
		scrap+=3;

	}

	@Override
	public void killReward() {
		scrap += 0;
		
	}

}

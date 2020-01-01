package gamePackage.playerPackage;

import gamePackage.Button;
import gamePackage.GameController;
import gamePackage.playerPackage.Player;
import gamePackage.shipPackage.BorgCube;
import gamePackage.shipPackage.Ship;
import processing.core.PApplet;
import processing.core.PVector;

public class BorgPlayer extends Player {

	public BorgPlayer(PApplet display, int allegiance, PVector basePosition) {
		super(display, "The Borg", allegiance, basePosition);
		baseName = 'B';
		scrap = 0;

		//@formatter:off
		String[][] temp = {	
				{"Borg Cube", "3"},
				{"Borg Sphere", "3"}
				};
				
		shipCosts = temp;
		//@formatter:on

		createButtons();

		((GameController) display).addShip("Borg Cube", allegiance, basePosition, startAngle);
		startAngle += Math.PI / 4;
	}

	public void drawBase(int whoseTurn) {
		display.pushStyle();
		if (whoseTurn != allegiance)
			display.fill(230, 10, 50);
		display.ellipse(basePosition.x, basePosition.y, radius * 2, radius * 2);
		display.popStyle();
		drawLetter();
	}

	@Override
	public void addScrap() {
		boolean found = false;
		for (Ship o : ((GameController) display).ships) {
			if (o.allegiance == allegiance && o instanceof BorgCube) {
				found = true;
				break;
			}
		}
		if (!found) {
			scrap += 2;
		}
	}

	@Override
	public void killReward() {
		scrap += 2;

	}

}

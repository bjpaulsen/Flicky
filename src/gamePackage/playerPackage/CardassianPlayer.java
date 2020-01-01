package gamePackage.playerPackage;

import gamePackage.Button;
import gamePackage.GameController;
import gamePackage.playerPackage.Player;
import processing.core.PApplet;
import processing.core.PVector;

public class CardassianPlayer extends Player {

	private int scrapPerTurn = 2;
	private static final int colonyBuff = 2;

	public CardassianPlayer(PApplet display, int allegiance, PVector basePosition) {
		super(display, "Cardassian Union", allegiance, basePosition);
		baseName = 'C';
		scrap = 0;

		//@formatter:off
		String[][] temp = {	
				{"Cardassian Brig", "3"},
				{"Colony Ship", "4"},
				{"The Bullet", "8"},
				{"Imperial Frigate", "13"}
				};
				
		shipCosts = temp;
		//@formatter:on

		createButtons();

		((GameController) display).addShip("Cardassian Brig", allegiance, basePosition, startAngle);
		startAngle += Math.PI / 4;
		((GameController) display).addShip("Colony Ship", allegiance, basePosition, startAngle);
		startAngle += Math.PI / 4;
		((GameController) display).addShip("Cardassian Brig", allegiance, basePosition, startAngle);
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
		scrap += scrapPerTurn;
	}

	@Override
	public void killReward() {

	}

	public void colonyDestroyed() {
		scrapPerTurn -= colonyBuff;
	}

	public void colonyBuilt() {
		scrapPerTurn += colonyBuff;
	}

}

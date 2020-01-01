package gamePackage.playerPackage;

import gamePackage.Button;
import gamePackage.GameController;
import gamePackage.playerPackage.Player;
import processing.core.PApplet;
import processing.core.PVector;

public class FerengiPlayer extends Player {

	public FerengiPlayer(PApplet display, int allegiance, PVector basePosition) {
		super(display, "Ferengi Alliance", allegiance, basePosition);
		baseName = 'F';
		scrap = 3;

		//@formatter:off
		String[][] temp = {	
				{"Trade Vessel", "4"},
				{"Mobile Trade Station", "7"},
				{"Junk Ship", "2"},
				{"Cargo Ship", "4"},
				{"Large Freighter", "8"}
				};
				
		shipCosts = temp;
		//@formatter:on

		createButtons();

		((GameController) display).addShip("Trade Vessel", allegiance, basePosition, startAngle);
		startAngle += Math.PI / 4;
		((GameController) display).addShip("Trade Vessel", allegiance, basePosition, startAngle);
		startAngle += Math.PI / 4;
	}

	@Override
	public void createButtons() {
		for (int i = 0; i < shipCosts.length; i++) {
			String text = shipCosts[i][0] + " " + shipCosts[i][1];
			int width = Math.round(display.textWidth(text)) + 20;
			int x = display.width - width - 30;
			String type = "ship";
			if (i >= 2)
				type += " tradeStation";
			((GameController) display).buttons.add(new Button(x, 190 + i * 75, width, 50, text, 28, display, allegiance, type, "", shipCosts[i][0]));
		}
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
		scrap += 2;

	}

	@Override
	public void killReward() {
		scrap += 3;

	}

	public void loseCargo() {
		scrap -= 4;
	}

}

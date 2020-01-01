package gamePackage.playerPackage;

import gamePackage.Button;
import gamePackage.GameController;
import gamePackage.shipPackage.Ship;
import gamePackage.shotPackage.Shot;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public abstract class Player {
	public PApplet display;
	public PVector basePosition;
	public char baseName;
	public int scrap, allegiance, radius, health;
	public String faction;
	public String[][] shipCosts;
	PFont baseFont;
	
	public float startAngle;
	
	public Player(PApplet display, String faction, int allegiance, PVector basePosition) {
		display.pushMatrix();
		display.translate(display.width/2, display.height/2);
		
		startAngle = (float) (Math.random()*Math.PI*2);
		
		display.popMatrix();
		
		baseFont = display.createFont("Oswald-Regular.ttf", 30);

		this.display = display;
		this.allegiance = allegiance;
		this.basePosition = basePosition;
		this.faction = faction;

		this.radius = 40;
		this.health = 5;

	}

	public abstract void addScrap();
	
	public abstract void drawBase(int whoseTurn);
	
	public abstract void killReward();

	public void drawLetter() {
		display.pushStyle();
		
		display.fill(0);
		display.textFont(baseFont);
		display.textSize(50);
		display.text(baseName, basePosition.x, basePosition.y - 6);
		
		display.popStyle();
	}

	public void addShip(String shipName) {
		int shipCost = 999;
		for (int i = 0; i < shipCosts.length; i++) {
			if (shipCosts[i][0].equals(shipName)) {
				shipCost = Integer.parseInt(shipCosts[i][1]);
				break;
			}
		}
		if (scrap >= shipCost) {
			((GameController) display).addShip(shipName, allegiance, basePosition, startAngle);
			scrap -= shipCost;
			startAngle+=Math.PI/4;
		}
		
	}

	public void showScrap() {
		display.pushStyle();

		display.textAlign(PApplet.RIGHT, PApplet.TOP);
		display.textSize(28);

		display.text(scrap + " SCRAP", display.width - 30, 30);

		display.popStyle();
	}
	
	public void createButtons(){
		for (int i = 0; i < shipCosts.length; i++) {
			String text = shipCosts[i][0] + " " + shipCosts[i][1];
			int width = Math.round(display.textWidth(text)) + 20;
			int x = display.width - width - 30;
			((GameController) display).buttons.add(new Button(x, 190 + i*75, width, 50, text, 28, display, allegiance, "ship", "", shipCosts[i][0]));
		}
	}
	
	

	public void showLabels() {
		display.pushStyle();

		display.textAlign(PApplet.RIGHT, PApplet.TOP);
		display.textSize(60);

		display.text("BUILD", display.width - 30, 100);
		

		display.popStyle();
	}

	// method overload
	public boolean collidesWith(Ship a) {
		float d = PApplet.dist(basePosition.x, basePosition.y, a.translatedPos.x, a.translatedPos.y);
		return d < this.radius + a.radius;
	}

	// method overload
	public boolean collidesWith(Shot a) {
		float d = PApplet.dist(basePosition.x, basePosition.y, a.position.x, a.position.y);
		return d < this.radius;
	}

	public void takeDamage() {
		health--;
	}

	public boolean isDestroyed() {
		return health < 1;
	}

}
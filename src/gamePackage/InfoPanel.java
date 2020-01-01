package gamePackage;

import processing.core.PApplet;

public class InfoPanel {
	PApplet display;
	private String name, description;
	private int health, shields;
	private int shotsPerTurn;
	private int movesPerTurn;

	public InfoPanel(PApplet display, String name, String description, int health, int shields, int shotsPerTurn, int movesPerTurn) {
		this.display = display;
		this.name = name;
		this.description = description;
		this.health = health;
		this.shields = shields;
		this.shotsPerTurn = shotsPerTurn;
		this.movesPerTurn = movesPerTurn;
	}

	public void show() {
		display.pushStyle();

		display.textAlign(PApplet.RIGHT, PApplet.TOP);
		display.textSize(60);
		display.text("INFO", display.width-30, 650);

		display.textSize(40);

		display.text(name, display.width-30, 740);
		display.textSize(28);
		display.text(shields + " SHIELD", display.width-30, 810);
		display.text(health + " HEALTH", display.width-30, 845);
		display.text(shotsPerTurn + " ATTACK", display.width-30, 880);
		display.text(movesPerTurn + " SPEED", display.width-30, 915);
		display.text(description, display.width-30, 975);
		

		display.popStyle();
	}
	
	public void updateInfo(int health, int shields, int shotsPerTurn, int movesPerTurn) {
		this.health = health;
		this.shields = shields;
		this.shotsPerTurn = shotsPerTurn;
		this.movesPerTurn = movesPerTurn;
	}
}

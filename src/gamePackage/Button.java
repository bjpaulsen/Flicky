package gamePackage;

import java.awt.Color;

import processing.core.PApplet;

public class Button {
	PApplet display;
	public int x, y, width, height, allegiance, fSize;
	public String type, text, group, shipName;
	public boolean isSelected = false, isEnabled = true;

	public Button(int x, int y, int width, int height, String text, int fSize, PApplet display, int allegiance, String type, String group) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.fSize = fSize;
		this.display = display;
		this.allegiance = allegiance;
		this.type = type;
		this.group = group;

		if (type.contains("default")) {
			isSelected = true;
		}
	}
	
	public Button(int x, int y, int width, int height, String text, int fSize, PApplet display, int allegiance, String type, String group, String shipName) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.fSize = fSize;
		this.display = display;
		this.allegiance = allegiance;
		this.type = type;
		this.group = group;
		this.shipName = shipName;

		if (type.contains("default")) 
			isSelected = true;
		
		if (type.contains("tradeStation"))
			isEnabled = false;
	}

	public void show() {
		display.pushStyle();
		display.textSize(fSize);
		display.strokeWeight(2);

		int color = isEnabled ? (isSelected ? Color.HSBtoRGB((((float) x - (float) 100) / (float) display.width) * (float) 2, 1, 1) : 240) : 100;

		int displacement = ((isHovered() && isEnabled) ? (display.mousePressed ? 0 : 6) : 4);
		
		int cornerRadius = height / 2;

		display.stroke(color);
		display.fill(color);
		for (int i = 0; i < displacement; i++) {
			display.rect(x + i, y - i, width, height, cornerRadius);
		}
		display.fill(0);
		display.rect(x + displacement, y - displacement, width, height, cornerRadius);
		display.fill(color);
		display.text(text, x + width / 2 + displacement, y + height / 2 - displacement - (height/(float)12));

		// specialty buttons

		// crossed out circle (keeping for later just in case)
//		if (type.contains("n/a")) {
//			display.pushStyle();
//			display.noFill();
//			display.strokeWeight(3);
//			display.ellipse(x + width / 2 + displacement, y + height / 2 - displacement, 30, 30);
//			display.line(x + 10 + displacement, y + height - 10 - displacement, x + width - 10 + displacement, y + 10 - displacement);
//			display.popStyle();
//		}

		display.popStyle();
	}

	
	public void press() {
		if (type.contains("toggle")) {
			isSelected = !isSelected;
		}
		if (type.contains("ship")) {
			((GameController) display).map.players.get(allegiance).addShip(shipName);
		}
	}

	public boolean isHovered() {
		return display.mouseX > x && display.mouseY > y && display.mouseX < x + width && display.mouseY < y + height;
	}

	public void deselect() {
		isSelected = false;
	}

}

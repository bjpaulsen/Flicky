package gamePackage.shotPackage;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Shot {
	public PApplet display;
	public float trajectory;
	public PVector position;
	public PVector velocity;
	public float speed;
	public int deathCount = 0;
	public int maxTimer;
	public int allegiance;

	public void move() {
		position.add(velocity);
	}
	
	public boolean timedOut() {
		deathCount++;
		return deathCount >= maxTimer;
	}

	public abstract void show();

}

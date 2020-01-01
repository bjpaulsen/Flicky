package gamePackage.shotPackage;

import processing.core.PApplet;
import processing.core.PVector;

public class ShortShot extends StandardShot {

	public ShortShot(float t, PVector p, PApplet a, float r, int allegiance) {
		super(t, p, a, r, allegiance);
	}
	
	@Override
	public boolean timedOut() {
		deathCount+=3;
		return deathCount >= maxTimer;
	}

}

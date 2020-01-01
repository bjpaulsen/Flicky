package gamePackage.shipPackage;

import gamePackage.GameController;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class ImperialFrigate extends Ship {
	private static final int BULLETS_PER_SHOT = 3;

	public ImperialFrigate(PApplet a, int al, PVector basePosition, float startAngle, String name) {
		super(a, al, basePosition, startAngle, name);
		radius = 25;
		shotsPerTurn = 1;
		health = 3;
		movesPerTurn = 1;

		description = "A massive, terrifying battleship with strong defenses and one devastating attack.";

		generateInfo();
	}

	@Override
	public void show(int whoseTurn) {
		display.pushStyle();
		display.pushMatrix();

		shieldAnimation();
		assimilationAnimation();

		triangleSimple(0, 0, radius * 2 + 5, radius * 2 + 5, 255, whoseTurn, 0);
		triangleSimple(0, 0, radius + 4, radius + 4, 0, whoseTurn, (float) Math.PI);

		display.popMatrix();
		display.popStyle();
	}

	void triangleSimple(float x, float y, float w, float h, int color, int whoseTurn, float tilt) {

		display.pushMatrix();

		display.pushStyle();

		if (allegiance != whoseTurn && color == 255) {
			display.fill(230, 10, 50);
		} else if (color != 255) {
			display.fill(color);
		}

		display.translate(display.width / 2, display.height / 2);
		display.translate(position.x, position.y);
		display.rotate(angle + PConstants.HALF_PI);

		// equilateral triangle (actually w and h should have the same value?)

		// 2 parallel arrays we fill the x and the y for our 3 corners in:
		float[] xArray = new float[3];
		float[] yArray = new float[3];

		// start angle
		float currentAngle = -PConstants.HALF_PI + tilt; // this tells the angle (start angle)
		// when you want to rotate (e.g.) the triangle, please make
		// currentAngle a parameter and give it different values.
		// See example function below.

		// go from diameter to radius (r is for radius)
		float wr = (float) (w * 0.5);
		float hr = (float) (h * 0.5);

		// This is the formula for a circle.

		// Since the triangle is equilateral, all
		// corners have the same distance from the
		// center x,y.

		// Thus all 3 corners lie on a circle.

		// Thus we can use cos and sin to calculate the corners as
		// part of an invisible circle (cos and sin are normally used to
		// calculate the circle).
		// 3 corners make the arrays:
		for (int i = 0; i < 3; i++) {

			// This is the formula for a circle.
			xArray[i] = (float) (x + wr * Math.cos(currentAngle));
			yArray[i] = (float) (y + hr * Math.sin(currentAngle));

			// the angle for the next corner is 1/3 of a full circle (360 degrees)
			// away, so we add 1/3 of 360 degree.
			currentAngle = (float) (currentAngle + 2 * Math.PI / 3);
		} // for

		// we use our 2 parallel arrays we filled the x and the y for our 3 corners in
		// to draw a triangle:
		display.triangle(xArray[0], yArray[0], xArray[1], yArray[1], xArray[2], yArray[2]);

		display.popMatrix();
		display.popStyle();
	}

	public void shoot() {
		Thread thread = new Thread() {
			public void run() {
				for (int i = 0; i < BULLETS_PER_SHOT; i++) {
					((GameController) display).addShot("StandardShot", angle, translatedPos, radius, allegiance);

					try {
						Thread.sleep(80);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		if (shotsLeft > 0) {
			thread.start();
			shotsLeft--;
		}
	}

}

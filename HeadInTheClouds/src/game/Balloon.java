// BALLOON POWER UP


package game;

import javafx.scene.image.Image;

public class Balloon extends Sprite{
	
	private final static Image BALLOON = new Image("file:src/images/balloon.PNG");

	private boolean used;
	
	public Balloon(double xPos, double yPos) {
		super(xPos, yPos, Balloon.BALLOON);
		this.width = 50;
		this.height = 75;
		this.used = false;
	}
	
	public void setUsed() {
		this.used = true; // Indicates that the balloon is already used
	}

	public boolean getUsed() {
		return this.used;
	}
	
}

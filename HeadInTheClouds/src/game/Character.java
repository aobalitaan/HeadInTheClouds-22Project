// BRAIN/CHARACTER CLASS

package game;

import javafx.scene.image.Image;

public class Character extends Sprite{
	
	// Image variations
	private final static Image CHARACTER_IMAGE_RIGHT = new Image("file:src/images/character_right.PNG");
	private final static Image CHARACTER_IMAGE_LEFT = new Image("file:src/images/character_left.PNG");
	private final static Image CHARACTER_IMAGE_COLLIDE = new Image("file:src/images/character_collide.PNG");
	
	// Jump strength of character
	private final static double JUMP_VELOCITY = -4.5;
	
	Character (double xPos, double yPos){
		super(xPos, yPos, Character.CHARACTER_IMAGE_RIGHT);
		this.width = 90;
		this.height = 60;
	}
	
	public void jump()
	{
		// Makes the character jump
        setDY(JUMP_VELOCITY);
	}
	
	
	// Sets and render Images
	public void setImageLeft() {
		this.img = CHARACTER_IMAGE_LEFT;
	}
	
	public void setImageRight() {
		this.img = CHARACTER_IMAGE_RIGHT;
	}
	
	public void setImageCollide() {
		this.img = CHARACTER_IMAGE_COLLIDE;
	}
	
	
}

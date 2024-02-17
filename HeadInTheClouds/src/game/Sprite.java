package game;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
	protected Image img;
	protected double xPos, yPos, dx, dy;
	protected boolean visible;
	protected double width;
	protected double height;

    public Sprite(double xPos, double yPos, Image image){
		this.xPos = xPos;
		this.yPos = yPos;
		this.loadImage(image);
		this.visible = true;
	}

	private Rectangle2D getBounds(){
		// Getting sprites bounds
		return new Rectangle2D(this.xPos, this.yPos, this.width, this.height);
	}

	private void setSize(){
		// sets widht and height
		
		this.width = this.img.getWidth();
        this.height = this.img.getHeight();
	}

	protected boolean collidesWith(Sprite rect2)	{
		
		// Checks for collision
		
		Rectangle2D rectangle1 = this.getBounds();
		Rectangle2D rectangle2 = rect2.getBounds();

		return rectangle1.intersects(rectangle2);
	}

	protected void loadImage(Image image){
		// Loads image
		
		try{
			this.img = image;
	        this.setSize();
		} catch(Exception e)	{
			e.printStackTrace();
		}
	}

	public void render(GraphicsContext gc){
		// renders sprite to the window
		
		if (this.visible == true){
			gc.drawImage(this.img, this.xPos, this.yPos, this.width, this.height);
		}
    }

	
	public Image getImage(){
		return this.img;
	}

	public double getWidth(){
		return this.width;
	}

	public double getHeight(){
		return this.height;
	}
	
	public double getXPos(){
		return this.xPos;
	}

	public double getYPos(){
		return this.yPos;
	}

	public void setXPos(double val){
		this.xPos = val;
	}

	public void setYPos(double val){
		this.yPos = val;
	}

	public void setDX(double val){
		this.dx = val;
	}

	public void setDY(double val){
		this.dy = val;
	}

	public double getDX(){
		return this.dx;
	}

	public double getDY(){
		return this.dy;
	}
	
	public boolean isVisible(){
		return visible;
	}

	public void vanish(){
		this.visible = false;
	}
}

// CLOUD CLASS



package game;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class Cloud extends Sprite{
	
	// Cloud Images
	private final static Image NORMAL_CLOUD = new Image("file:src/images/cloud.PNG");
	private final static Image FADE_CLOUD = new Image("file:src/images/fadecloud.PNG");
	private final static Image FRENZY_NORMAL_CLOUD = new Image("file:src/images/frenzynormal.gif");
	private final static Image FRENZY_FADE_CLOUD = new Image("file:src/images/frenzyfade.gif");
	
	private String type;	// Indicates cloud type
	
	private boolean isMoving;	// Indicates if the cloud should be moving
	
	private boolean isFrenzy;	// Tells if a cloud is a frenzy cloud
	private Random r;	// Randomizer
	private boolean randomed;	// Tells if the cloud was already randomed/frenzied
	private boolean roll;	// Tells if a roll is allowed
	
	private float multiplier;
	
	private final static AudioClip REGULAR_JUMP = new AudioClip("file:src/media/sfx_regularjump.mp3");
	private final static AudioClip FADE_JUMP = new AudioClip("file:src/media/sfx_fadejump.mp3");
	
	private final static AudioClip FRENZY_CLOUD = new AudioClip("file:src/media/sfx_frenzycloud.wav");
	
		// Sprite(double xPos, double yPos, Image image)
	Cloud (double xPos, double yPos, int typeIndicator, double score){
		super(xPos, yPos, Cloud.NORMAL_CLOUD);
		this.width = 100;
		this.height = 35;
		this.r = new Random();
		this.randomed = false;
		this.roll = true;
		this.isFrenzy = false;
		this.multiplier = 1;
		setType(typeIndicator, score);
		
		
		REGULAR_JUMP.setVolume(0.3);
		FADE_JUMP.setVolume(0.3);
		FRENZY_CLOUD.setVolume(0.05);
	}
	
	private void setType(int typeIndicator, double score)
	{
		// If the type was a moving cloud
		if (typeIndicator == 3){
			float speed = 1;	// Base speed is 1
			
			isMoving = true;	// Indicates that it is a moving cloud
			typeIndicator = 1;	// Based co-type is a normal moving cloud
			
			
			if (score%800 >= GameTimer.PHASE[3]){	// if passed Phase 3, 
				speed = r.nextFloat(0.7f) + 1.3f;	// randomize moving cloud velocity  1.3f - 2.0f
			}
			
			if (score%800 >= GameTimer.PHASE[4]){	// If passed Phase 4,
				speed = r.nextFloat(0.8f) + 1.5f;	// randomize moving cloud velocity 1.5f - 2.3f
				
				typeIndicator = r.nextInt(2) + 1;	// randomize co-type fade/normal
			}
			
			
			if (score >= GameTimer.CYCLE){	// If passed the first cycle
				this.isFrenzy = (r.nextInt(4) > 0 ? true : false);	// may generate a frenzy cloud (P: 75%)
			}
			
			// Sets the random dx speed
			speed = (r.nextInt(2) == 1 ? speed : -speed);
			this.setDX(speed);
			System.out.println("Generated Moving cloud with speed: " + speed);
			
		}
		
		
		if (typeIndicator == 1){	// If normal cloud
			this.type = "Normal";
			
			if (isFrenzy){	// If normal frenzied moving cloud
				this.img = FRENZY_NORMAL_CLOUD;
			}
			else{
				this.img = NORMAL_CLOUD;
			}
		}
		else if (typeIndicator == 2){	 // If fade cloud
			this.type = "Fade";
			
			if (isFrenzy){	// If fade frenzied moving cloud
				this.img = FRENZY_FADE_CLOUD;
			}
			else{
				this.img = FADE_CLOUD;
			}
		}	
	}
	
	public String getType()
	{
		return type;	// return cloud type
	}
	
	public void varyVel() {
		
		// FOR FRENZY CLOUDS
		
        if (!isFrenzy) {
            return;
        }

        // Gets current time
        int currentTime = (int) (System.currentTimeMillis() / 1000) % 10;

        // Resets velocity
        if (currentTime % 3 == 1 && randomed) {
            reduceVelocity();
        }

        // randomize velocity
        if (roll && currentTime % 3 == 0 && !randomed) {
            randomizeVelocity();
        }

        // Allows rolling every 3 seconds
        if (currentTime % 3 == 2 && !roll) {
            roll = true;
        }
    }

    private void reduceVelocity() {
    	
    	// Brings back velocity to regular
    	
        setDX(getDX() / this.multiplier);;
        randomed = false;
    }

    private void randomizeVelocity() {
    	
    	// Gets random integer
    	
        int num1 = r.nextInt(5);
        roll = false;	// Indicates that already rolled a number

        // If the random integer is greater than 0 (P : 80%)
        if (num1 > 0) {
        	
        	// Play frenzy cloud sound, and randomize velocity
        	Cloud.FRENZY_CLOUD.play();
        	this.multiplier = r.nextFloat(1.0f) + 1.5f;
        	
            setDX(getDX() * this.multiplier);
            randomed = true;
            
            System.out.println("Frenzy Cloud Sped Up By: " + this.multiplier + "x");
        }
    }
	
	public void stepped(Character brain)
	{
		// Cloud stepped
		if (this.type == "Normal"){
			// if normal just make the character jump
			System.out.println("Stepped on a Normal" + (isMoving ? " Moving" : "") + (isFrenzy ? " Frenzy" : "") + " Cloud");
			REGULAR_JUMP.play();
			brain.jump();
		}
		else if (this.type == "Fade")
		{
			// if fade disappears and makes the character jump
			System.out.println("Stepped on a Fade" + (isMoving ? " Moving" : "") + (isFrenzy ? " Frenzy" : "") + " Cloud");
			FADE_JUMP.play();
			this.vanish();
			brain.jump();
		}
	}
	
}

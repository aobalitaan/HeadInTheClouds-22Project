package game;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import scenes.Over;


class GameTimer extends AnimationTimer{
	private GraphicsContext gc;
	private Scene scene;
	private Stage stage;
	private Scene gameOverScene;
	
	
	
	// ArrayList of spawning objects
	
	private ArrayList<Cloud> cloudsOnScreen;
	private ArrayList<Balloon> balloonsOnScreen;
	
	
	
	
	// Brain, and its other attributes while in game
	
	private Character character;
	private final static int MAXJUMPHEIGHT = 320;	// The max height of character's jump
	public final static double MOVE_SPEED = 2;		// Speed of left right movement
	public final static double GRAVITY = 1;			// Constant dy pulling character down
	
	
	
	// Game starting initializations
	
	private boolean loaded; 	// Indicates if it is the first time the game was loaded 
	private final Random RANDOMIZER = new Random(); 	//Initialize randomizer
	
	private double score; 	// brain score
	private boolean givePoints;		// For animating score adding
	
	
	
	// Cloud Spawing
	public final static int MAX_CLOUDS_PER_ROW = 4;		// Maximum number of clouds per row
	private final static int NORMAL_CLOUD = 1;		// Indicator of if should spawn a Normal Cloud
    private final static int FADE_CLOUD = 2;		// Indicator of if should spawn a Fade Cloud
    private final static int MOVING_CLOUD = 3;		// Indicator of if should spawn a Moving Cloud
    private final static int XPOS[] = {5, 125, 255, 375};		// Initial possible x positions clouds could spawn
	private final static double YPOS[] = {20, 230, 440, 660};	// Initial y Positions
	
	private boolean shouldSpawnClouds;			// Indicator for if a row already spawned another row of clouds
    private Cloud collidedCloud;
	
    
    
    // Game Proper
    
	public final static int CYCLE = 800;		// When a game completes a cycle (in terms of difficulty) resets phases
	public final static int PHASE[] = {0, 50, 100, 400, 600, 700};	// Points checkpoint to change phases

	
	
	
	// Power Up
	
    private Balloon collidedBalloon;	// stores which balloon is currently in use
    private double powerUpCounter;		// For counting how long the power up takes effect
	private boolean powerUp;		// Indicator of if a powerUp is taking effect
    
	
	
	
	// Files and other imports
	
	private final static Image GAME_BACKGROUND = new Image("file:src/images/playBackground.png");
	private final static Font scoreFont = Font.loadFont("file:src/images/Daydream.ttf", 25);
	
	private final AudioClip BALLOON_SFX = new AudioClip("file:src/media/sfx_balloon.mp3");
	private final AudioClip GAMEOVER_SFX = new AudioClip("file:src/media/sfx_gameOver.mp3");
	
   
	GameTimer(Scene scene, GraphicsContext gc, Stage stage){
		this.scene = scene;
		this.stage = stage;
		this.gc = gc;
		this.cloudsOnScreen = new ArrayList<Cloud>();
		this.balloonsOnScreen = new ArrayList<Balloon>();
		this.loaded = false;
		
		this.character = new Character(200, 400);
		
		this.collidedBalloon = null;
		this.collidedCloud = null;
		this.score = 0;
		
		this.shouldSpawnClouds = false;
		
		this.givePoints = false;
		this.powerUp = false;
		this.powerUpCounter = 0;
		
		this.BALLOON_SFX.setVolume(0.6);
		this.GAMEOVER_SFX.setVolume(0.75);
	}


	public void handle(long currentNanoTime){
		this.gc.clearRect(0, 0, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);	// Clears screen
		this.gc.drawImage(GAME_BACKGROUND, 0, 0);
		
		if (loaded == false){ 	// If first run, loads initial clouds on starting
			this.loadInitialClouds();
			this.loaded = true;
		}
		
		this.renderSprites();	// Renders sprites everytime
		this.handleKeyPressEvent();
		
		this.updateSprites();
		
		// If character fell out of screen
		if (character.getYPos() > GameStage.WINDOW_HEIGHT){
			this.stop();
			this.initGameOver();	// Initializes game over scene
			GAMEOVER_SFX.play();	// Plays game over sfx
			
			this.stage.setScene(gameOverScene);	// Sets stage to the game over
		}

	}
	
	private void handleKeyPressEvent() {
		
		// Keyboard listener
		
		this.scene.setOnKeyPressed(
	        new EventHandler<KeyEvent>()
	        {
				public void handle(KeyEvent e)
	            {	
					if (e.getCode() == KeyCode.H) {		// Helper key ***** FOR PRESENTATION PURPOSES ONLY *****
						setPowerUp();
	            	} else if (e.getCode() == KeyCode.LEFT) {	// Move character to left
		                moveCharacterLeft();
		            } else if (e.getCode() == KeyCode.RIGHT) {	// Move character to the right
		                moveCharacterRight();
		            } 
	            }
	        });
		
		
		 this.scene.setOnKeyReleased(
	        new EventHandler<KeyEvent>() {
	            public void handle(KeyEvent e) {
	                if (e.getCode() == KeyCode.LEFT) {
	                    stopMomentum();
	                } else if (e.getCode() == KeyCode.RIGHT) {
	                	stopMomentum();
	                }
	            }
	        });
		
    }


	private void renderSprites(){
		
		// Renders the clouds every refresh
		
		for (Cloud cloud : this.cloudsOnScreen)
        	cloud.render(this.gc);
		
		// Renders balloons (if any) every refresh
		
		for (Balloon balloon : this.balloonsOnScreen)
			balloon.render(this.gc);
	
		//Renders character every refresh
		
		character.render(this.gc);
	}
	
	private void updateSprites() {
		
		// Updates sprites every refresh
		updateCharacter();
		updateClouds();
		updateBalloons();
		
		// Checks collision
		collides();
		
		// Updates scores
		addPoints();
		updateScore();
		
		// Power Up 
		passThrough();
			
	}
	

	

	
	
	
	
	//SCORE LOGIC
	
	
	private void updateScore(){ 
		// Updates and prints score
		gc.setFill(Color.WHITE); 
        gc.setFont(scoreFont); 
        gc.fillText("SCORE:  " + (int) this.score, 18, 38); 
	}

	
	private void addPoints() {
		// Score adder
		
		// Adds 10 points little by little for animation
		if (this.givePoints) {
			this.score += 0.5;
		}
		
		if (this.score % 10 == 0) {
			this.givePoints = false;
			return;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//CLOUD SPAWNING LOGIC
	
	
	private void loadInitialClouds(){
		
		// Loads the clouds in the starting

		// Spawns clouds on initial y positions
		for (int i = 0; i < YPOS.length; i++){
			spawnClouds(YPOS[i]);
		}
	}

	private int[] randomizeCloudRow()
	{
		// CLOUD RANDOMIZER SPAWNING LOGIC
		
		
		int xRow[] = {0, 0, 0, 0};	// Sets initially to no clouds to spawn
	   
		
		// Randomize how many clouds will be generated for the row
	    int numCloudsRow = RANDOMIZER.nextInt(MAX_CLOUDS_PER_ROW - 1) + 1;	
	    
	    
	    // If the player already passed the Phase 1 of the game, 
	    // *may* reduce the number of clouds in row by 1 (assuming the row already has more than 1 cloud) 
	    if ((this.score % CYCLE >= PHASE[3]) && (numCloudsRow > 1)) {
	        numCloudsRow -= RANDOMIZER.nextInt(1);
	    }

	    
	    // Generate the clouds based on how many numCloudsRow randomized
	    for (int j = 0; j < numCloudsRow; j++){
	    	
	    	
	    	// Initially set to generate normal clouds
	    	
	    	xRow[j] = NORMAL_CLOUD;	
	    	
	    	
	    	// If user passed the Phase 1, may generate fade clouds
	    	
	    	if ((this.score % CYCLE) >= PHASE[1]){
	    		xRow[j] = RANDOMIZER.nextInt(FADE_CLOUD) + 1;
	    	}
	    	
	    	
	    	// If user passed the Phase 2, may generate moving clouds
	    	
	    	if ((this.score % CYCLE) >= PHASE[2]){
	    		if ((numCloudsRow == 1) || (RANDOMIZER.nextInt(4) == 0 && (j == 0))){
	    			xRow[0] = RANDOMIZER.nextInt(MOVING_CLOUD - 1) + 2;
	    			break;
	    		}
	    	}
	    	
	    	// If user passed Phase 4, only spawns moving or fade clouds
	    	
	    	if ((this.score % CYCLE) >= PHASE[4]){
	    		if (xRow[j] != FADE_CLOUD || xRow[j] != MOVING_CLOUD){
	    			xRow[j] = FADE_CLOUD;
	    		}
	    	}
	    	
	    	
	    	// If user passed Phase 5, increase probability of spawning moving clouds
	    	
	    	if ((this.score % CYCLE) >= PHASE[5]){
	    		if (numCloudsRow == 1) {
	    			xRow[0] = MOVING_CLOUD;
	    		}
	    		xRow[j] = RANDOMIZER.nextInt(MOVING_CLOUD - 1) + 2;
	    	}
	    }
	    
	    return xRow;
	}
	
	private void spawnClouds(double yPos) {
	    
	    int xRow[] = randomizeCloudRow();	// Generate clouds for specific row based on current score
	   
	    // Initially all active clouds are in the start of the array {1, 1, 1, 0, 0}
	    // Shuffles their positions
	    
	    List<Integer> xList = Arrays.stream(xRow).boxed().collect(Collectors.toList());
	    Collections.shuffle(xList);
    
	    
	    
	    // Generate the clouds
	    
	    for (int k = 0; k < xList.size(); k++) {
	    	
	    	// If the value in the list is not set to 0 (don't spawn), generate corresponding type of cloud
	    	
	        if (xList.get(k) != 0) {
	        	
	        	int adder = randomize(k, xList);	// Randomizer for X positions        	
	        	
	        	// Generate a cloud in the corresponding (x position + adder), and current yPos, and their type
	            Cloud c = new Cloud(XPOS[k] + adder, yPos, xList.get(k), this.score);
	            this.cloudsOnScreen.add(c);
	        }
	    }
	    
	    spawnPowerUp(yPos);		// Spawns power up
	}
	
	
	private int randomize(int k, List<Integer> arraylist){
		
		// Randomizer for X postions
		
		int adder = 0;
	
		// If the cloud doesnt have a cloud on its right generate an x adder
		if (k < arraylist.size() - 1 && arraylist.get(k + 1) == 0){
			adder = RANDOMIZER.nextInt(50 - 0);
		}
		
		// If the cloud doesnt have a cloud on its left generate a negative x adder
		if (k > 0 && arraylist.get(k - 1) == 0){
			adder = RANDOMIZER.nextInt(50 - 0);
			adder = -adder;
		}
		
		return adder;
	}

	
	
	
	
	
	//POWER UP
	
	
	private void spawnPowerUp(double yPos) {
		
		// every 1/4 of the game cycle, spawns a balloon power up
		
		if (this.score != 0 && (this.score % (CYCLE / 4) == 0)) {
			
			// Randomizes balloon position
			int xPos = RANDOMIZER.nextInt(GameStage.WINDOW_WIDTH - 50);
			int yPosAdder = RANDOMIZER.nextInt(GameStage.WINDOW_HEIGHT);
			
			// Spawn balloon
			Balloon balloon = new Balloon(xPos, yPos - yPosAdder);
			this.balloonsOnScreen.add(balloon);
			
			System.out.println("Spawned a balloon.");
		}
		
	}
	
	
	
	
	
	
	
	
	//CLOUDS MOVEMENT/Spawning LOGIC
	
	
	private void cloudsSpawner() {
		// Method that indicates whether should spawn a balloon
		
		if (this.shouldSpawnClouds) {
			spawnClouds(-130);
			this.givePoints = true;	// Gives points
			this.shouldSpawnClouds = false;	// Indicates that row already spawned a balloon
		}
	}
	
	
	private void updateBalloons() {
		// Updating sprites of balloons
		Iterator<Balloon> iterator = this.balloonsOnScreen.iterator();
		
		while (iterator.hasNext()) {
		    
		    Balloon balloon = iterator.next();
		    
		    // Update balloon position every refresh
		    balloon.setXPos(balloon.getXPos() + balloon.getDX());
			balloon.setYPos(balloon.getYPos() + balloon.getDY());
			
			// If balloon is already way outside the current screen (2 screens away)
			// Removes the balloon
		    if (balloon.getYPos() <= -(GameStage.WINDOW_HEIGHT * 2)) {
		        iterator.remove();
		        this.collidedBalloon = null;
		    }
		}
	}
	
	private void stopClouds() {
		
		// Stop clouds y movement
		for (Cloud cloud: this.cloudsOnScreen) {
			cloud.setDY(0);
		}
		
		// Stop balloons y movement except the collided/activated ones
		for (Balloon balloon: this.balloonsOnScreen) {
			if (balloon != this.collidedBalloon){
				balloon.setDY(0);
			}
		}
	}
	
	private void moveCloudsDown(double velocity) {
		
		// Moves the balloon and clouds down
		
		for (Cloud cloud: this.cloudsOnScreen){ 
    		cloud.setDY(velocity);
		}			
		
		for (Balloon balloon: this.balloonsOnScreen) {
			balloon.setDY(velocity);
		}

	}
	
	private void updateClouds() {
		
		// Updates the sprite of clouds
		
		Iterator<Cloud> iterator = this.cloudsOnScreen.iterator();

	    while (iterator.hasNext()) {
	        Cloud cloud = iterator.next();
	        
	        // Updates cloud position
	        
	        cloud.setXPos(cloud.getXPos() + cloud.getDX());
			cloud.setYPos(cloud.getYPos() + cloud.getDY());
	        
			
	        // *** FOR MOVING CLOUDS *** If cloud bump left and right bounds, reverse direction
			
	        if ((cloud.getXPos() <= 0) || (cloud.getXPos() >= GameStage.WINDOW_WIDTH - 100)){
				cloud.setDX(-cloud.getDX());
			}
	        
	        // *** FOR FRENZY CLOUDS *** randomizes velocity
	        
	        cloud.varyVel();
	        
	        
	        // If a cloud row already exits the screen, removes them from list, and spawns new row
	        
	        if (cloud.getYPos() >= GameStage.WINDOW_HEIGHT) {
	        	cloud.vanish();
	        	iterator.remove();
	            this.shouldSpawnClouds = true;	  
	        } 
	    }
	    
	    
	    // Spawns clouds
	    
	    cloudsSpawner();
	    
	    
	    // If has a power up, locks the steps below
	    if (this.powerUp) {
	    	return;
	    }
	    
	    
	    // Stops the scrolling when the row is in the correct position
	    
	    if (this.collidedCloud == null || this.collidedCloud.getYPos() >= YPOS[3]) {
	    	stopClouds();
	    	return;
	    }
	    
	    // Moves clouds down
	    
	    moveCloudsDown(3);
	}
	
	
	
	
	
	
	//POWER UP LOGIC
	

	
	private void passThrough() {
		// If doesn't have a power up pass
		if (this.powerUp == false){
			return;
		}
		
		
		// Counter for power up effect
		this.powerUpCounter += 1;
		character.setDY(-5);	// Sets the velocity of character to max
		
		
		moveCloudsDown(8);	// scrolls downwards
		
		
		// Sets balloon position
		if (this.collidedBalloon != null && !this.collidedBalloon.getUsed()) {
			this.collidedBalloon.setXPos(character.getXPos()-40);
			this.collidedBalloon.setYPos(MAXJUMPHEIGHT - 40);	
		}
		
		
		// If the power Up is used up
		if (this.powerUpCounter >= 200) {
			stopClouds(); // Stop the clouds
			this.powerUp = false;	// Indicates no power Up is activated
			this.powerUpCounter = 0;	// Reset counter
			
			
			// Flys the balloon outside screen
			if (this.collidedBalloon != null) {
				int determiner = RANDOMIZER.nextInt(2);
				determiner = (determiner == 0 ? 1 : -1);
				
				this.collidedBalloon.setDX(determiner * 3);
				this.collidedBalloon.setDY(-12);
				this.collidedBalloon.setUsed();	// Sets the balloon as used
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	//COLLISION LOGIC
	
	private void setPowerUp() {
		// Indicates that a power up is used
		this.powerUp = true;
	}
	
	private boolean collisionChecker(Cloud cloud, Character brain) {
		
		if (!cloud.isVisible()) {
			return false;
		}
		
		// If character and cloud doesn't collide, or character is not yet landing
		if (!cloud.collidesWith(character) || character.getDY() < 0) {
			return false;
		}
		
		
		double b_bottom = character.getYPos() + character.getHeight();
		double c_top = cloud.getYPos() + cloud.getHeight()/2;
		
		
		// If the foot of the character is not yet higher than the cloud
		if (b_bottom < c_top) {
			return false;
		}
		
	
		double b_LHit = character.getXPos() + (character.getWidth()/2) - 10;
		double b_RHit = character.getXPos() + (character.getWidth()/2) + 10;
		
		double c_LHit =  cloud.getXPos() + (cloud.getWidth()/2) - 45;
		double c_RHit =  cloud.getXPos() + (cloud.getWidth()/2) + 45;
		
		
		// If the character is within (almost) the middle part of the cloud
		if ((b_RHit >= c_LHit && b_RHit <= c_RHit) || 
			(b_LHit >= c_LHit && b_LHit <= c_RHit)) {
			return true;
		}
		
		return false;
	}
	
	private void collides(){
		for (Cloud cloud: this.cloudsOnScreen){	
			
			// If character hits the hitbox of cloud
			if (collisionChecker(cloud, this.character)){
				
				character.setImageCollide();	// Change character image
				cloud.stepped(character);	// Calls stepped function (that makes character jump)
				this.collidedCloud = cloud;	// Sets the cloud as the collided cloud
				
			}
		}
		
		
		for (Balloon balloon: this.balloonsOnScreen) {
			
			// If balloon is not yet used and balloon collides with the character
			if (!balloon.getUsed() && balloon.collidesWith(character) && balloon != this.collidedBalloon) {
			    this.collidedBalloon = balloon;  // Sets the balloon as the collided balloon
			    setPowerUp();  // Use the power-up effect
			    System.out.println("Collected balloon");
			    BALLOON_SFX.play();  // Plays sound effect
			}
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//CHARACTER MOVEMNET LOGIC
	
	
	private void moveCharacterRight() {
    	character.setDX(MOVE_SPEED);
    	character.setImageRight();
	}
	
	private void moveCharacterLeft() {
        character.setDX(-MOVE_SPEED);
		character.setImageLeft();
	}
	
	private void stopMomentum() {
		character.setDX(0);

	}
	
    private void updateCharacter() {
    	
    	// Constant gravitational pull    	
    	character.setDY(character.getDY() + (GRAVITY * 0.05)); 
    	
    	// Updates character position
    	character.setXPos(character.getXPos() + character.getDX());
        character.setYPos(character.getYPos() + character.getDY());
    	
        // If character exceeds max height, puts them in max height position
        if (character.getYPos() <= MAXJUMPHEIGHT) {
        	character.setYPos(MAXJUMPHEIGHT);
        }
        
        // Bounds for left and right 
        if (character.getXPos() < 0) {
        	character.setXPos(0);
        	character.setDX(0);
        	
        } else if (character.getXPos() > GameStage.WINDOW_WIDTH - character.getWidth()) {
        	character.setXPos(GameStage.WINDOW_WIDTH - character.getWidth());
        	character.setDX(0);
        }
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //GAME OVER SCENE
    
    private void initGameOver(){
    	Group gameOverRoot = new Group();
    	this.gameOverScene = new Over(gameOverRoot, GameStage.mainScene, this.stage, this.score);

    }
}

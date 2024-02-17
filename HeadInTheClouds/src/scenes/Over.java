package scenes;

import game.GameStage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Over extends Scene{

	private Stage stage;
	private Group root;
	private double score;

	// font used for score, credits can be found in the References scene
	private Font scoreFont = Font.loadFont("file:src/images/Daydream.ttf", 25); 
	private final static Image GAME_OVER_GIF = new Image("file:src/images/gameOvermoving.gif");
	private final static Image MAIN_MENU_BUTTON = new Image("file:src/images/backtomainButton.PNG");

	// back-up background if GIF version doesn't show up
	//private final static Image GAME_OVER_GIF = new Image("file:src/images/gameOverbg.png"); 


    public Over(Group root, Scene mainSce, Stage stage, double score) {
		super(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.root = root;
		this.stage = stage;
		this.score = score; 
		
		// initialize Game Over scene with total score
		initGameOver(this.stage); 
	}

    // initialize the Game Over Scene
    private void initGameOver(Stage stage){
    	Menu.toggleBackgroundMusic();
		ImageView gameOverBackground = createOverBackground(); // create background for Game Over scene
		ImageView overMainButton = createOverMainButton(); // create button that will go back to main menu
		Text totalScore = createTextScore();
		totalScore.setTextAlignment(TextAlignment.CENTER); // to display the score

		this.root.getChildren().addAll(gameOverBackground, overMainButton, totalScore); // add above components
    }

    // creates background for Game Over Scene
	private ImageView createOverBackground() {
		Image over = GAME_OVER_GIF; // use gif as Game Over background
		ImageView gameOverBackground = new ImageView(over);
		gameOverBackground.setPreserveRatio(true);

		return gameOverBackground;
    }

	// creates a Main Menu button that can be found in Game Over Scene
    private ImageView createOverMainButton() {
		Image overMain = MAIN_MENU_BUTTON;
		ImageView overMainButton = new ImageView(overMain);

		overMainButton.setFitWidth(180);
		overMainButton.setPreserveRatio(true);
		overMainButton.setX(150);
		overMainButton.setY(500);

		setOverMainButton(overMainButton); // add hover effect and link button to Main Menu scene
		return overMainButton;
    }

    // adds shadow effect on button when hovered and links the button to Main Menu scene
    private void setOverMainButton(ImageView overMainButton) {
		DropShadow dropShadow = new DropShadow();
		overMainButton.setOnMouseEntered(event -> overMainButton.setEffect(dropShadow));
		overMainButton.setOnMouseExited(event -> overMainButton.setEffect(null));
		overMainButton.setOnMouseClicked(event -> {stage.setScene(GameStage.mainScene); Menu.toggleVideoBgSound(); Menu.toggleBackgroundMusic();});
    }

    // creates total score that will be displayed
    private Text createTextScore() {
        Text totalScore = new Text("SCORE: " + (int) this.score);
        totalScore.setFont(scoreFont); // use Daydream font by DoubleGum
        totalScore.setFill(Color.WHITE);

        totalScore.setX((GameStage.WINDOW_WIDTH/2) - (totalScore.getLayoutBounds().getWidth()/2));
		totalScore.setY(425);

		return totalScore;
    }


}


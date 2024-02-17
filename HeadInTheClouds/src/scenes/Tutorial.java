package scenes;

import game.GameStage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Tutorial extends Scene {

    private Scene mainScene; // Assuming you have a mainScene variable in your GameStage class
    private Stage stage;
	private Group root;

    private final static Image TUTORIAL_IMAGE =  new Image("file:src/images/tutorial_bg.png");
	private final static Image BACK_TO_MAIN_BUTTON = new Image("file:src/images/backtomainButton.PNG");
	private final static Image ARROWS_IMAGE = new Image("file:src/images/controlbutton.gif");
	private final static Image BRAIN_JUMPING = new Image("file:src/images/tutorialJumping.gif");


    public Tutorial(Group root, Scene mainScene, Stage stage) {
        super(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
        this.mainScene = mainScene;
        this.stage = stage;
        this.root = root;
        
        initTutorial(this.stage); 
    }

    // initialize the Tutorial Scene
    private void initTutorial(Stage stage) {
        ImageView tutorialBackground = createBackground(); // create background for Tutorial scene
        ImageView controlArrowsGIF = createArrows(); // create left and right arrows GIF
        ImageView tutorialMainButton = createButton(); // create Main Menu button found in Tutorial scene
        bindButton(tutorialMainButton); // add hover effect and link button to Main Menu
        ImageView tutorialJumpingGIF = createJumpingGIF(); // create Character Jumping demonstration GIF

        this.root.getChildren().addAll(tutorialBackground, tutorialMainButton, controlArrowsGIF, tutorialJumpingGIF); // add above components
    }

    // create background for Tutorial scene
    private ImageView createBackground() {
        Image tutorial = TUTORIAL_IMAGE;
        ImageView tutorialBackground = new ImageView(tutorial);
        tutorialBackground.setPreserveRatio(true);

        return tutorialBackground;
    }

    // create Main Menu button for Tutorial scene
    private ImageView createButton() {
        Image tutorialMain = BACK_TO_MAIN_BUTTON;
    	ImageView tutorialMainButton = new ImageView(tutorialMain);

        tutorialMainButton.setFitWidth(180);
        tutorialMainButton.setPreserveRatio(true);
        tutorialMainButton.setX(150);
        tutorialMainButton.setY(650);

        return tutorialMainButton;
    }

    // add hover effect and link button to Main Menu
    private void bindButton(ImageView button) {
        DropShadow dropShadow = new DropShadow();
        button.setOnMouseEntered(event -> button.setEffect(dropShadow));
        button.setOnMouseExited(event -> button.setEffect(null));
        button.setOnMouseClicked(event -> stage.setScene(mainScene));
    }

    // create the control left and right arrows GIF
    private ImageView createArrows() {
    	Image arrows = ARROWS_IMAGE;
    	ImageView controlArrowsGIF = new ImageView(arrows);
    	controlArrowsGIF.setFitWidth(230);
    	controlArrowsGIF.setPreserveRatio(true);
    	controlArrowsGIF.setX(125);
    	controlArrowsGIF.setY(400);

    	return controlArrowsGIF;
    }

    // create the Character Jumping demonstration GIF
    private ImageView createJumpingGIF() {
    	Image brainJumping = BRAIN_JUMPING;
    	ImageView jumpingGIF = new ImageView(brainJumping);
    	jumpingGIF.setFitWidth(400);
    	jumpingGIF.setPreserveRatio(true);
    	jumpingGIF.setX(40);
    	jumpingGIF.setY(100);

    	return jumpingGIF;
    }

}
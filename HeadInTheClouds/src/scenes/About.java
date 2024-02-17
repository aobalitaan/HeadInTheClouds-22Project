package scenes;


import game.GameStage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class About extends Scene {

    private Scene mainScene; 
    private Scene referenceScene; 
    private Stage stage;
	private Group root;

    private final static Image ABOUT_IMAGE =  new Image("file:src/images/developer_scene_bg.gif");
	private final static Image BACK_TO_MAIN_BUTTON = new Image("file:src/images/backtomainButton.PNG");
	private final static Image NEXT_BUTTON = new Image("file:src/images/next_button.PNG");
    private final static Image REFERENCES_IMAGE =  new Image("file:src/images/references_page.png");
	private final static Image BACK_BUTTON = new Image("file:src/images/back_button.PNG");
	
    public About(Group root, Scene mainScene, Stage stage) {
        super(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
        this.mainScene = mainScene;
        this.stage = stage;
        this.root = root;
        
        //initializes about and references scene
        initReferences(this.stage); 
        initAbout(this.stage);
    }

    // initialize the About Scene
    private void initAbout(Stage stage) {
    	
    	//initialize backgrounds and buttons
        ImageView aboutBackground = createBackground(ABOUT_IMAGE); 
        ImageView aboutMainButton = createButton(BACK_TO_MAIN_BUTTON, 160, 663, 160); 
        ImageView nextToReference = createButton(NEXT_BUTTON, 330, 668, 50);
        
        //binds button to main scene and reference scene
        bindButton(aboutMainButton, mainScene); 
        bindButton(nextToReference, referenceScene); 

        this.root.getChildren().addAll(aboutBackground, aboutMainButton, nextToReference); 
    }


    // initialize the References Scene
    private void initReferences(Stage stage) {
    	Group referencesRoot = new Group();
    	
    	//initialize backgrounds and buttons
        ImageView referencesBackground = createBackground(REFERENCES_IMAGE); 
        ImageView referencesMainButton = createButton(BACK_TO_MAIN_BUTTON, 160, 663, 160); 
        ImageView backToAbout = createButton(BACK_BUTTON, 100, 668, 50);

        //bind buttons to main and about scene
        bindButton(referencesMainButton, mainScene); 
        bindButton(backToAbout, this); 

        referencesRoot.getChildren().addAll(referencesBackground, referencesMainButton, backToAbout);
        this.referenceScene = new Scene(referencesRoot, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
    }


    // creating a background for a scene
    ImageView createBackground(Image backgroundImage) {
        ImageView setBackground = new ImageView(backgroundImage);
        setBackground.setPreserveRatio(true);

        return setBackground;
    }


    // creates a button and set up its properties
     ImageView createButton(Image buttonImage, double xPos, double yPos, double width) {
        ImageView button = new ImageView(buttonImage);

        button.setFitWidth(width);
        button.setPreserveRatio(true);
        button.setX(xPos);
        button.setY(yPos);

        return button;
    }


    // adds shadow effect on button when hovered and links the button to its destination scene
     void bindButton(ImageView button, Scene sceneToGo) {
        DropShadow dropShadow = new DropShadow();
        button.setOnMouseEntered(event -> button.setEffect(dropShadow));
        button.setOnMouseExited(event -> button.setEffect(null));
        button.setOnMouseClicked(event -> stage.setScene(sceneToGo));
    }

}

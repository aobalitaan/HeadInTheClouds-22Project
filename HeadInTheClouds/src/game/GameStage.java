package game;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import scenes.About;
import scenes.Menu;
import scenes.Tutorial;

public class GameStage {
	
	private Stage stage;

	public static Scene mainScene;				
	private Scene playScene;			
	private Scene tutorialScene;		
	private Scene aboutScene;			
	private Group root;
	private Canvas canvas;
	
	public final static int WINDOW_WIDTH = 480;
	public final static int WINDOW_HEIGHT = 720;
	private static final String BACKGROUND_VIDEO_URL = "src/media/bg_main.mp4";

	public GameStage(){
		this.canvas = new Canvas(GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
		this.root = new Group();
        this.root.getChildren().add(this.canvas);
        this.playScene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT,Color.CORNFLOWERBLUE);
	}


	public void setStage(Stage stage) {
		this.stage = stage;
		this.stage.setTitle("Head In The Clouds");
		this.stage.getIcons().add(new Image("file:src/images/icon.PNG"));
		
		//initializes different scenes
		this.initMain(stage);			
		this.initAbout(stage);
		this.initTutorial(stage);
		
		this.stage.setScene(GameStage.mainScene); 
        this.stage.setResizable(false);
		this.stage.show();
	}
	
	//initialize main menu, backgrounds, sounds, buttons and bindings
	private void initMain(Stage stage) {
		StackPane root = new StackPane();

		Menu.setBackground(BACKGROUND_VIDEO_URL, root);
        Menu.toggleBackgroundMusic();

        ImageView playButton = Menu.createButton("file:src/images/btn_play.png", 350, 80);
        playButton.setOnMouseClicked(event -> setGame(stage));


        ImageView aboutButton = Menu.createButton("file:src/images/btn_about.png", 170, 65);
        aboutButton.setOnMouseClicked(event -> stage.setScene(this.aboutScene));

        ImageView tutorialButton = Menu.createButton("file:src/images/btn_tutorial.png", 170, 65);
        tutorialButton.setOnMouseClicked(event -> stage.setScene(this.tutorialScene));

        HBox aboutTutorialHBox = Menu.createHBox(10, aboutButton, tutorialButton, Pos.CENTER);
        VBox buttonVBox = Menu.createVBox(10, playButton, aboutTutorialHBox, Pos.CENTER);
        buttonVBox.setTranslateY(180);

        root.getChildren().addAll(buttonVBox);
        GameStage.mainScene = new Scene(root, GameStage.WINDOW_WIDTH, GameStage.WINDOW_HEIGHT);
	}

	//create an about scene by instantiating the about class
	private void initAbout(Stage stage){ 
		Group aboutRoot = new Group();
		this.aboutScene = new About(aboutRoot, GameStage.mainScene, this.stage);
	}

	//create a tutorial scene by instantiating the tutorial class
	private void initTutorial(Stage stage){
		Group tutorialRoot = new Group();
		this.tutorialScene = new Tutorial(tutorialRoot, GameStage.mainScene, this.stage);
	}

	//change scene to the game
	void setGame(Stage stage) { 
		Menu.toggleVideoBgSound();
		
        this.stage.setScene(this.playScene);
        
        //create an instance of the game timer class to handle the game animation and logic
        GraphicsContext gc = this.canvas.getGraphicsContext2D();	
        GameTimer gameTimer = new GameTimer(playScene, gc, this.stage);
        gameTimer.start();

	}
}


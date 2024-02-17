package main;

import javafx.application.Application;
import javafx.stage.Stage;
import game.GameStage;

public class App extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
    public void start(Stage stage)
    {
       GameStage game = new GameStage();
       game.setStage(stage);
    }

}

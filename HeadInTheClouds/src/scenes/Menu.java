package scenes;

import java.io.File;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Menu{

	private static MediaPlayer mediaPlayer;
	
	private static AudioClip bgm = new AudioClip("file:src/media/audio_bgmdraft.mp3");
	
	
	// sets the background of the Main Menu, video plays infinitely
	public static void setBackground(String videoUrl, StackPane root) { // called by initMain()
        Media backgroundVideo = new Media(new File(videoUrl).toURI().toString());
        mediaPlayer = new MediaPlayer(backgroundVideo);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        MediaView mediaView = new MediaView(mediaPlayer);
        
        // add to Menu's root
        root.getChildren().add(mediaView);
    }
	
	public static void toggleVideoBgSound() {
		mediaPlayer.setMute(!mediaPlayer.isMute());
	}

	// sets/plays the background music of the game
	public static void toggleBackgroundMusic() { // called by initMain()		
        if (bgm.isPlaying()) {
        	bgm.stop();
        	return;
        }	
		
        bgm.setVolume(0.75);
        bgm.setCycleCount(AudioClip.INDEFINITE);
        bgm.play();
    }
	
	
	// creates buttons and adds shadow hover effect on them
	public static ImageView createButton(String imageUrl, int width, int height) { // called by initMain()
        Image image = new Image(imageUrl);
        ImageView button = new ImageView(image);
        button.setFitWidth(width);
        button.setFitHeight(height);
        button.setPreserveRatio(false);

        DropShadow dropShadow = new DropShadow(); // hover effect
        button.setOnMouseEntered(event -> button.setEffect(dropShadow));
        button.setOnMouseExited(event -> button.setEffect(null));

        return button;
    }

	// used to set About and Tutorial buttons side-by-side
	public static HBox createHBox(int spacing, ImageView btn1, ImageView btn2, Pos alignment) { // called by initMain()
        HBox hBox = new HBox(spacing);
        hBox.getChildren().addAll(btn1, btn2);
        hBox.setAlignment(alignment);
        return hBox;
    }

	// used to set Play button and grouped About and Tutorial buttons on top of each other
	public static VBox createVBox(int spacing, ImageView btn1, HBox btn2, Pos alignment) { // called by initMain()
        VBox vBox = new VBox(spacing);
        vBox.getChildren().addAll(btn1, btn2);
        vBox.setAlignment(alignment);
        return vBox;
    }
}
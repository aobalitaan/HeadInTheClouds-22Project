module Hi {
	requires javafx.controls;
	requires javafx.media;
	requires javafx.graphics;
	
	opens main to javafx.graphics, javafx.fxml;
	opens game to javafx.graphics, javafx.fxml;

}

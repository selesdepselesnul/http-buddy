package selesdepselesnul.httpbuddy;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.load(ClassLoader.getSystemResourceAsStream(
					"selesdepselesnul/httpbuddy/HttpBuddy.fxml"));
			Scene scene = new Scene(fxmlLoader.getRoot(), 400, 400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

package testing;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Client extends Application {

	private AnimationTimer timer;
	private Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();
		Scene scene = new Scene(root, 480, 480);
		scene.setFill(Color.BLACK);
		stage = primaryStage;
		stage.setTitle("MMORPG - unnamed"); // Set the stage title
		stage.setScene(scene); // Placing the scene in the stage
		stage.setFullScreen(false);
		stage.setResizable(false);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.show();

		Game game = new Game(stage, scene, root);

		// game loop
		timer = new AnimationTimer() {

			@Override
			public void handle(long timestamp) {
				game.tick(root);
			}
		};
		timer.start();

	}

}
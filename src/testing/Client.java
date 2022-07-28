package testing;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Client extends Application {

	private PlayerController playerController;
	private AnimationTimer timer;
	private Player player;
	private List<WorldObstacle> tiles;
	private Stage stage;

	private long frameCounter = 0;

	@Override
	public void start(Stage primaryStage) throws Exception {
		initPlayer();
		initWorldObjects();

		Group root = new Group();
		visualizeObstacles(root);
		visualizePlayer(root);

		Scene scene = new Scene(root, 200, 200);

		stage = primaryStage;
		stage.setTitle("MMORPG - unnamed"); // Set the stage title
		stage.setScene(scene); // Place the scene in the stage
		stage.setFullScreen(true);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.show();

		// input/ event handler
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				if (arg0.getText().equalsIgnoreCase("w")) {
					playerController.setPlayerInputUp(true);
				}
				if (arg0.getText().equalsIgnoreCase("s")) {
					playerController.setPlayerInputDown(true);
				}
				if (arg0.getText().equalsIgnoreCase("a")) {
					playerController.setPlayerInputLeft(true);
				}
				if (arg0.getText().equalsIgnoreCase("d")) {
					playerController.setPlayerInputRight(true);
				}
				if (arg0.getCode() == KeyCode.ESCAPE) {
					endGame();
				}
			}
		});
		scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				if (arg0.getText().equalsIgnoreCase("w")) {
					playerController.setPlayerInputUp(false);
				}
				if (arg0.getText().equalsIgnoreCase("s")) {
					playerController.setPlayerInputDown(false);
				}
				if (arg0.getText().equalsIgnoreCase("a")) {
					playerController.setPlayerInputLeft(false);
				}
				if (arg0.getText().equalsIgnoreCase("d")) {
					playerController.setPlayerInputRight(false);
				}
			}
		});
		// game loop
		timer = new AnimationTimer() {
			LongProperty lastUpdateTime = new SimpleLongProperty(0);

			@Override
			public void handle(long timestamp) {
				if (lastUpdateTime.get() == 0) {
					lastUpdateTime.set(timestamp);
					return;
				}
				if (timestamp - lastUpdateTime.get() > 1000000000 / 100) {
					tick();
					lastUpdateTime.set(timestamp);
					return;
				}
			}
		};
		timer.start();
	}

	private void initPlayer() {
		try {
			SpriteSheet sprites = new SpriteSheet("assets/sprites/SpriteSheet.png", 32, 32);
			Animator animator = new Animator();
			animator.addAnimation("IDLE_UP");
			animator.addFrameToAnimation("IDLE_UP", new Rectangle2D(0 * 32, 11 * 32, 32, 32));
			animator.addAnimation("MOVE_UP");
			animator.addFrameToAnimation("MOVE_UP", new Rectangle2D(1 * 32, 11 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_UP", new Rectangle2D(0 * 32, 11 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_UP", new Rectangle2D(2 * 32, 11 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_UP", new Rectangle2D(0 * 32, 11 * 32, 32, 32));
			animator.addAnimation("IDLE_DOWN");
			animator.addFrameToAnimation("IDLE_DOWN", new Rectangle2D(0 * 32, 8 * 32, 32, 32));
			animator.addAnimation("MOVE_DOWN");
			animator.addFrameToAnimation("MOVE_DOWN", new Rectangle2D(1 * 32, 8 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_DOWN", new Rectangle2D(0 * 32, 8 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_DOWN", new Rectangle2D(2 * 32, 8 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_DOWN", new Rectangle2D(0 * 32, 8 * 32, 32, 32));
			animator.addAnimation("IDLE_LEFT");
			animator.addFrameToAnimation("IDLE_LEFT", new Rectangle2D(0 * 32, 9 * 32, 32, 32));
			animator.addAnimation("MOVE_LEFT");
			animator.addFrameToAnimation("MOVE_LEFT", new Rectangle2D(1 * 32, 9 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_LEFT", new Rectangle2D(0 * 32, 9 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_LEFT", new Rectangle2D(2 * 32, 9 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_LEFT", new Rectangle2D(0 * 32, 9 * 32, 32, 32));
			animator.addAnimation("IDLE_RIGHT");
			animator.addFrameToAnimation("IDLE_RIGHT", new Rectangle2D(0 * 32, 10 * 32, 32, 32));
			animator.addAnimation("MOVE_RIGHT");
			animator.addFrameToAnimation("MOVE_RIGHT", new Rectangle2D(1 * 32, 10 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_RIGHT", new Rectangle2D(0 * 32, 10 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_RIGHT", new Rectangle2D(2 * 32, 10 * 32, 32, 32));
			animator.addFrameToAnimation("MOVE_RIGHT", new Rectangle2D(0 * 32, 10 * 32, 32, 32));
			player = new Player(sprites, 0, 0, 32, 32, animator);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		player.addHitboxUp(5, player.getHeight() - 10, 32 - 10, 1);
		player.addHitboxDown(5, player.getHeight(), 32 - 10, 1);
		player.addHitboxLeft(4, player.getHeight() - 9, 1, 9);
		player.addHitboxRight(player.getWidth() - 5, player.getHeight() - 9, 1, 9);

		player.moveTo(50, 50);

		playerController = new PlayerController(player, 1);
	}

	private void visualizePlayer(Group root) {
		/*
		 * root.getChildren().add(player.hitboxUp);
		 * root.getChildren().add(player.hitboxDown);
		 * root.getChildren().add(player.hitboxLeft);
		 * root.getChildren().add(player.hitboxRight);
		 */
		root.getChildren().add(player.getBody());
	}

	private void initWorldObjects() {
		tiles = new List<>();
		SpriteSheet sprites = null;
		try {
			sprites = new SpriteSheet("assets/sprites/SpriteSheet.png", 32, 32);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Animator animator = new Animator();
		animator.addAnimation("STANDARD");
		animator.addFrameToAnimation("STANDARD", new Rectangle2D(0 * 32, 6 * 32, 32, 32));
		animator.addFrameToAnimation("STANDARD", new Rectangle2D(1 * 32, 6 * 32, 32, 32));
		animator.addFrameToAnimation("STANDARD", new Rectangle2D(2 * 32, 6 * 32, 32, 32));
		animator.addFrameToAnimation("STANDARD", new Rectangle2D(1 * 32, 6 * 32, 32, 32));
		WorldObstacle object = new WorldObstacle(sprites, 100, 100, 32, 32, animator, true, true);
		object.addHitbox(0, 0, 32, 32);
		tiles.add(object);

		animator = new Animator();
		animator.addAnimation("STANDARD");
		animator.addFrameToAnimation("STANDARD", new Rectangle2D(15 * 32, 2 * 32, 32, 32));
		object = new WorldObstacle(sprites, 132, 100, 32, 32, animator, false, false);
		object.addHitbox(0, 0, 32, 32);
		tiles.add(object);
	}

	private void visualizeObstacles(Group root) {
		for (int i = 0; i < tiles.length(); i++) {
			root.getChildren().add(tiles.get(i).getBody());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void tick() {
		// when window focus is lost, no input shall be proceeded
		if (!stage.isFocused()) {
			playerController.setAllPlayerInputs(false);
		}
		// player movement every frame
		if (frameCounter % 1 == 0) {
			playerController.checkCollision(tiles);
			playerController.updatePlayerPosition();
			playerController.checkPlayerStoppedMoving();
		}
		// basic animation: every tenth frame, move frame by one
		if (frameCounter % 10 == 0) {
			player.nextAnimationFrame();
			for (int i = 0; i < tiles.length(); i++) {
				if (!tiles.get(i).hasAnimation()) {
					continue;
				}
				tiles.get(i).nextAnimationFrame();
			}
		}
		// incrementing frameCounter
		frameCounter++;
	}

	private void endGame() {
		System.exit(0);
	}
}
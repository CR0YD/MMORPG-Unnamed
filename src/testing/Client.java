package testing;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.EventHandler;
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
	private List<MapObstacle> tiles;
	private Stage stage;

	private long frameCounter = 0;

	public static void main(String[] args) {
		launch(args);
	}

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
		stage.setFullScreen(false);
		stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		stage.show();

		// input/ event handler
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getText().equalsIgnoreCase("w")) {
					playerController.setPlayerInputUp(true);
				}
				if (event.getText().equalsIgnoreCase("s")) {
					playerController.setPlayerInputDown(true);
				}
				if (event.getText().equalsIgnoreCase("a")) {
					playerController.setPlayerInputLeft(true);
				}
				if (event.getText().equalsIgnoreCase("d")) {
					playerController.setPlayerInputRight(true);
				}
				if (event.getCode() == KeyCode.ESCAPE) {
					endGame();
				}
			}
		});
		scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getText().equalsIgnoreCase("w")) {
					playerController.setPlayerInputUp(false);
				}
				if (event.getText().equalsIgnoreCase("s")) {
					playerController.setPlayerInputDown(false);
				}
				if (event.getText().equalsIgnoreCase("a")) {
					playerController.setPlayerInputLeft(false);
				}
				if (event.getText().equalsIgnoreCase("d")) {
					playerController.setPlayerInputRight(false);
				}
				if (event.getText().equalsIgnoreCase("f")) {
					playerController.playerWantsToInteract();
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
			SpriteSheet sprites = new SpriteSheet("assets/sprites/charsets_12_m-f_complete_by_antifarea_modified.png",
					32, 36);
			Animator animator = new Animator(sprites);
			animator.addAnimation("IDLE_UP");
			animator.addFrameToAnimation("IDLE_UP", 1, 0);
			animator.addAnimation("MOVE_UP");
			animator.addFrameToAnimation("MOVE_UP", 0, 0);
			animator.addFrameToAnimation("MOVE_UP", 1, 0);
			animator.addFrameToAnimation("MOVE_UP", 2, 0);
			animator.addFrameToAnimation("MOVE_UP", 1, 0);
			animator.addAnimation("IDLE_DOWN");
			animator.addFrameToAnimation("IDLE_DOWN", 1, 2);
			animator.addAnimation("MOVE_DOWN");
			animator.addFrameToAnimation("MOVE_DOWN", 0, 2);
			animator.addFrameToAnimation("MOVE_DOWN", 1, 2);
			animator.addFrameToAnimation("MOVE_DOWN", 2, 2);
			animator.addFrameToAnimation("MOVE_DOWN", 1, 2);
			animator.addAnimation("IDLE_LEFT");
			animator.addFrameToAnimation("IDLE_LEFT", 1, 3);
			animator.addAnimation("MOVE_LEFT");
			animator.addFrameToAnimation("MOVE_LEFT", 0, 3);
			animator.addFrameToAnimation("MOVE_LEFT", 1, 3);
			animator.addFrameToAnimation("MOVE_LEFT", 2, 3);
			animator.addFrameToAnimation("MOVE_LEFT", 1, 3);
			animator.addAnimation("IDLE_RIGHT");
			animator.addFrameToAnimation("IDLE_RIGHT", 1, 1);
			animator.addAnimation("MOVE_RIGHT");
			animator.addFrameToAnimation("MOVE_RIGHT", 0, 1);
			animator.addFrameToAnimation("MOVE_RIGHT", 1, 1);
			animator.addFrameToAnimation("MOVE_RIGHT", 2, 1);
			animator.addFrameToAnimation("MOVE_RIGHT", 1, 1);
			player = new Player(50, 50, sprites.COLUMN_WIDTH, sprites.ROW_HEIGHT, animator);

			player.addHitboxUp(2, player.getHeight() - 10, sprites.COLUMN_WIDTH - 6, 1);
			player.addHitboxDown(2, player.getHeight(), sprites.COLUMN_WIDTH - 6, 1);
			player.addHitboxLeft(1, player.getHeight() - 9, 1, 9);
			player.addHitboxRight(player.getWidth() - 4, player.getHeight() - 9, 1, 9);
			player.addInteractionTriggerBox(-4, player.getHeight() - 15, 38, 20);
			
			player.checkPlayerBeforeStart();
			
			playerController = new PlayerController(player, 1);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private void visualizePlayer(Group root) {
		root.getChildren().add(player.getInteractionTriggerBox());

		root.getChildren().add(player.hitboxUp);
		root.getChildren().add(player.hitboxDown);
		root.getChildren().add(player.hitboxLeft);
		root.getChildren().add(player.hitboxRight);

		root.getChildren().add(player.getBody());
	}

	private void initWorldObjects() {
		tiles = new List<>();
		SpriteSheet sprites = null;
		try {
			sprites = new SpriteSheet("assets/sprites/charsets_12_m-f_complete_by_antifarea_modified.png", 32, 36);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Animator animator = new Animator(sprites);
		animator.addAnimation("STANDARD");
		animator.addFrameToAnimation("STANDARD", 3,2);
		animator.addFrameToAnimation("STANDARD",3,0);
		MapObstacle object = new MapObstacle(100, 100, sprites.COLUMN_WIDTH, sprites.ROW_HEIGHT, animator,
				false, true, true);
		object.addHitbox(0, 0, sprites.COLUMN_WIDTH, sprites.ROW_HEIGHT);
		object.setInteractionBox(-16, -16, 64, 64);
		object.setInteraction(new Interaction() {

			@Override
			public void onInteraction() {
				object.nextAnimationFrame();
			}
		});
		tiles.add(object);
	}

	private void visualizeObstacles(Group root) {
		for (int i = 0; i < tiles.length(); i++) {
			root.getChildren().add(tiles.get(i).getInteractionBox());
			// root.getChildren().add(tiles.get(i).getHitboxList().get(0));
			root.getChildren().add(tiles.get(i).getBody());
		}
	}

	private void tick() {
		// when window focus is lost, no input shall be proceeded
		if (!stage.isFocused()) {
			playerController.setAllPlayerInputs(false);
		}
		// player movement every frame
		if (frameCounter % 1 == 0) {
			playerController.checkCollision(tiles);
			playerController.checkPlayerAnimation();
			playerController.updatePlayerPosition();
			playerController.checkPlayerStoppedMoving();
			playerController.checkInteraction(tiles);
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
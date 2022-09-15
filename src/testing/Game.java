package testing;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Game {

	private Player player;
	private PlayerController playerController;

	private Stage stage;
	private Scene scene;
	private Group root;

	@SuppressWarnings("unused")
	private long prevTime;
	@SuppressWarnings("unused")
	private int fps = 0;

	private long frameCounter = 0;

	private Map map;

	public Game(Stage stage, Scene scene, Group root) {
		this.stage = stage;
		this.scene = scene;
		this.root = root;

		map = new Map();
		map.switchFieldTo(0, root, stage);

		initPlayer();
		initEventHandler();
		this.root.getChildren().add(player.getNode());
	}

	public void tick(Group root) {
		//fps counter
//		if (System.currentTimeMillis() - prevTime >= 1000) {
//			prevTime = System.currentTimeMillis();
//			System.out.println("FPS: " + fps);
//			fps = 0;
//		} else {
//			fps++;
//		}
		// when window focus is lost, no input shall be proceeded
		if (!stage.isFocused()) {
			playerController.setAllPlayerInputs(false);
		}
		// player movement every frame
		if (frameCounter % 1 == 0) {
			playerController.checkCollisionWithObstacle(map.getCurrentField().getObstacles());
			playerController.checkPlayerAnimation();
			playerController.updatePlayerPosition(map, scene, root);
			playerController.checkPlayerStoppedMoving();
		}
		// basic animation: every tenth frame, move frame by one
		if (frameCounter % 10 == 0) {
			player.nextAnimationFrame();
		}
		// incrementing frameCounter
		frameCounter++;
		if (frameCounter < 0) {
			frameCounter = 0;
		}
	}

	private void initEventHandler() {
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
	}

	private void endGame() {
		System.exit(0);
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

			player = new Player((int) ((stage.getWidth() - 32) / 2), (int) ((stage.getHeight() - 36) / 2), sprites.COLUMN_WIDTH,
					sprites.ROW_HEIGHT, animator);

			player.addHitboxUp(2, player.getHeight() - 10, sprites.COLUMN_WIDTH - 6, 1);
			player.addHitboxDown(2, player.getHeight(), sprites.COLUMN_WIDTH - 6, 1);
			player.addHitboxLeft(1, player.getHeight() - 9, 1, 9);
			player.addHitboxRight(player.getWidth() - 4, player.getHeight() - 9, 1, 9);

			player.configureCenter();
			player.checkPlayerBeforeStart();

			playerController = new PlayerController(player, 2);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}

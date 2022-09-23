package testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
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
		this.root.getChildren().add(player.getVisualizer().getImageView());
	}

	public void tick(Group root) {
		// fps counter
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
			player.getVisualizer().nextAnimationFrame();
			for (int i = 0; i < map.getCurrentField().getObstacles().length(); i++) {
				if (!map.getCurrentField().getObstacles().get(i).hasAnimation()) {
					continue;
				}
				map.getCurrentField().getObstacles().get(i).getVisualizer().nextAnimationFrame();
			}
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
			ImageView imageView = new ImageView(new Image(new FileInputStream(new File("assets/sprites/charsets_12_m-f_complete_by_antifarea_modified.png"))));
			Visualizer visualizer = new Visualizer(imageView, 32, 36);
			visualizer.addAnimation("IDLE_UP");
			visualizer.addFrameToAnimation("IDLE_UP", 1, 0);
			visualizer.addAnimation("MOVE_UP");
			visualizer.addFrameToAnimation("MOVE_UP", 0, 0);
			visualizer.addFrameToAnimation("MOVE_UP", 1, 0);
			visualizer.addFrameToAnimation("MOVE_UP", 2, 0);
			visualizer.addFrameToAnimation("MOVE_UP", 1, 0);
			visualizer.addAnimation("IDLE_DOWN");
			visualizer.addFrameToAnimation("IDLE_DOWN", 1, 2);
			visualizer.addAnimation("MOVE_DOWN");
			visualizer.addFrameToAnimation("MOVE_DOWN", 0, 2);
			visualizer.addFrameToAnimation("MOVE_DOWN", 1, 2);
			visualizer.addFrameToAnimation("MOVE_DOWN", 2, 2);
			visualizer.addFrameToAnimation("MOVE_DOWN", 1, 2);
			visualizer.addAnimation("IDLE_LEFT");
			visualizer.addFrameToAnimation("IDLE_LEFT", 1, 3);
			visualizer.addAnimation("MOVE_LEFT");
			visualizer.addFrameToAnimation("MOVE_LEFT", 0, 3);
			visualizer.addFrameToAnimation("MOVE_LEFT", 1, 3);
			visualizer.addFrameToAnimation("MOVE_LEFT", 2, 3);
			visualizer.addFrameToAnimation("MOVE_LEFT", 1, 3);
			visualizer.addAnimation("IDLE_RIGHT");
			visualizer.addFrameToAnimation("IDLE_RIGHT", 1, 1);
			visualizer.addAnimation("MOVE_RIGHT");
			visualizer.addFrameToAnimation("MOVE_RIGHT", 0, 1);
			visualizer.addFrameToAnimation("MOVE_RIGHT", 1, 1);
			visualizer.addFrameToAnimation("MOVE_RIGHT", 2, 1);
			visualizer.addFrameToAnimation("MOVE_RIGHT", 1, 1);

			Rectangle rect = new Rectangle(0, 0, 30, 14);
			rect.setTranslateX(0);
			rect.setTranslateY(25);
			player = new Player((int) ((stage.getWidth() - 32) / 2), (int) ((stage.getHeight() - 36) / 2),
					visualizer, rect);

			playerController = new PlayerController(player, 2);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}

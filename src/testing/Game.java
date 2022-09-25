package testing;

import java.io.IOException;

import finished.List;
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

	private List<ObjectModel> objectModels;
	
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
		
		objectModels = ObjectFilesReader.read("assets/map/objects");

		map = new Map(objectModels);
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
			Character character = null;
			for (int i = 0; i < objectModels.length(); i++) {
				if (objectModels.get(i).TYPE.equals("character")) {
					character = CharacterCreator.createCharactr(objectModels.get(i).PARAMETERS);
				}
			}

			player = new Player((int) ((stage.getWidth() - 32) / 2), (int) ((stage.getHeight() - 36) / 2),
					character.getVisualizer(), character.getCollisionBox());

			playerController = new PlayerController(player, 2);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}

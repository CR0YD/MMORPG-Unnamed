package testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import finished.List;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Game {

	private Player player;

	private Stage stage;
	private Scene scene;
	private Group root;

	private List<ObjectModel> objectModels;

	@SuppressWarnings("unused")
	private long prevTime;
	@SuppressWarnings("unused")
	private int fps = 0;

	@SuppressWarnings("unused")
	private long frameCounter = 0;

	private int test = 0;

	private Map map;

	private String[] controlls;
	private boolean[] controllsPressed;

	public Game(Stage stage, Scene scene, Group root) {
		this.stage = stage;
		this.scene = scene;
		this.root = root;

		initControlls();
		objectModels = ObjectFilesReader.read("assets/map/objects");

		map = new Map(objectModels);
		map.switchFieldTo(0, root, stage);

		initPlayer();
		initEventHandler();
//		this.root.getChildren().add(player.getCollisionBox());
		this.root.getChildren().add(player.getVisualizer().getImageView());
//		this.root.getChildren().add(new Rectangle(player.getX() + player.getCenterPoint().getX(),
//				player.getY() + player.getCenterPoint().getY(), 10, 10));
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
			for (int i = 0; i < controlls.length; i++) {
				controllsPressed[i] = false;
			}
		}
//		player tick method
		player.tick(controlls, controllsPressed, map.getCurrentField().getObstacles(), map, scene, root);
//		object tick method
		for (int i = 0; i < map.getCurrentField().getObstacles().length(); i++) {
			map.getCurrentField().getObstacles().get(i).tick();
		}
//		z-positioning
//		...
		if (player.changedYPosition()) {
			root.getChildren().remove(player.getVisualizer().getImageView());
			for (int i = 0; i < map.getCurrentField().getCollisionDummies().length(); i++) {
				if (map.getCurrentField().getCollisionDummies().get(i).getTranslateY()
						+ map.getCurrentField().getCollisionDummies().get(i)
								.getHeight() > player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight()) {
					root.getChildren().add(map.getCurrentField().getChunks().length() + i,
							player.getVisualizer().getImageView());
					break;
				}
			}
			if (!root.getChildren().contains(player.getVisualizer().getImageView())) {
				root.getChildren().add(player.getVisualizer().getImageView());
			}
		}
		// incrementing frameCounter
//		frameCounter++;
//		if (frameCounter < 0) {
//			frameCounter = 0;
//		}
	}

	private void initEventHandler() {
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE) {
					endGame();
				}
				for (int i = 0; i < controlls.length; i++) {
					if (event.getText().equalsIgnoreCase(controlls[i].split("->")[0])) {
						controllsPressed[i] = true;
						return;
					}
				}
			}
		});
		scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getText().equalsIgnoreCase("q")) {
					if (test == 0) {
						test++;
						map.switchFieldTo(1, root, stage);
						return;
					}
					test = 0;
					map.switchFieldTo(0, root, stage);
				}
				for (int i = 0; i < controlls.length; i++) {
					if (event.getText().equalsIgnoreCase(controlls[i].split("->")[0])) {
						controllsPressed[i] = false;
						return;
					}
				}
			}
		});
	}

	private void endGame() {
		System.exit(0);
	}

	private void initControlls() {
		String controllsString = "";
		Scanner scan;
		try {
			scan = new Scanner(new File("assets/controlls.config"));
			while (scan.hasNextLine()) {
				controllsString += scan.nextLine() + ";";
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		controlls = controllsString.split(";");
		controllsPressed = new boolean[controlls.length];
		for (int i = 0; i < controlls.length; i++) {
			controllsPressed[i] = false;
		}
	}

	private void initPlayer() {
		try {
			Character character = null;
			for (int i = 0; i < objectModels.length(); i++) {
				if (objectModels.get(i).TYPE.equals("character")) {
					character = CharacterCreator.createCharacter(objectModels.get(i).PARAMETERS);
					break;
				}
			}

			player = new Player((int) (stage.getWidth() / 2), (int) (stage.getHeight() / 2), character.getVisualizer(),
					character.getCollisionBox(), character.getCenterPoint());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}

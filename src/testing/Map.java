package testing;

import java.io.File;

import finished.List;
import javafx.scene.Group;
import javafx.stage.Stage;

public class Map {

	private final List<Field> FIELDS;
	private int currentFieldIdx;

	private final List<ObjectModel> OBJECT_MODEL;

	public Map(List<ObjectModel> objectModel) {
		OBJECT_MODEL = objectModel;
		FIELDS = new List<>();
		File[] mapFolder = new File("assets/map").listFiles();
		for (int i = 0; i < mapFolder.length; i++) {
			if (mapFolder[i].getName().equals("objects")) {
				continue;
			}
			FIELDS.add(new Field(mapFolder[i].getPath(), OBJECT_MODEL));
		}
		currentFieldIdx = -1;
	}

	public void switchFieldTo(int idx, Group root, Stage stage) {
		if (currentFieldIdx == idx || idx >= FIELDS.length() || idx < 0) {
			return;
		}
		if (currentFieldIdx == -1) {
			currentFieldIdx = idx;
			for (int i = 0; i < FIELDS.get(currentFieldIdx).getChunks().length(); i++) {
				root.getChildren().add(i, FIELDS.get(currentFieldIdx).getChunks().get(i).BODY);
			}
			for (int i = 0; i < FIELDS.get(currentFieldIdx).getObstacles().length(); i++) {
				root.getChildren()
						.add(FIELDS.get(currentFieldIdx).getObstacles().get(i).getVisualizer().getImageView());
			}
			if (FIELDS.get(currentFieldIdx).WIDTH < stage.getWidth()) {
				moveField((stage.getWidth() - FIELDS.get(currentFieldIdx).WIDTH) / 2, 0);
			}
			if (FIELDS.get(currentFieldIdx).HEIGHT < stage.getHeight()) {
				moveField(0, (stage.getHeight() - FIELDS.get(currentFieldIdx).HEIGHT) / 2);
			}
			return;
		}
		for (int i = 0; i < FIELDS.get(currentFieldIdx).getChunks().length(); i++) {
			root.getChildren().remove(FIELDS.get(currentFieldIdx).getChunks().get(i).BODY);
		}
		for (int i = 0; i < FIELDS.get(currentFieldIdx).getObstacles().length(); i++) {
			root.getChildren().remove(FIELDS.get(currentFieldIdx).getObstacles().get(i).getVisualizer().getImageView());
		}
		if (FIELDS.get(currentFieldIdx).WIDTH < stage.getWidth()) {
			moveField(-(stage.getWidth() - FIELDS.get(currentFieldIdx).WIDTH) / 2, 0);
		}
		if (FIELDS.get(currentFieldIdx).HEIGHT < stage.getHeight()) {
			moveField(0, -(stage.getHeight() - FIELDS.get(currentFieldIdx).HEIGHT) / 2);
		}
		currentFieldIdx = idx;
		for (int i = 0; i < FIELDS.get(currentFieldIdx).getChunks().length(); i++) {
			root.getChildren().add(i, FIELDS.get(currentFieldIdx).getChunks().get(i).BODY);
		}
		for (int i = 0; i < FIELDS.get(currentFieldIdx).getObstacles().length(); i++) {
			root.getChildren().add(FIELDS.get(currentFieldIdx).getObstacles().get(i).getVisualizer().getImageView());
		}
		if (FIELDS.get(currentFieldIdx).WIDTH < stage.getWidth()) {
			moveField((stage.getWidth() - FIELDS.get(currentFieldIdx).WIDTH) / 2, 0);
		}
		if (FIELDS.get(currentFieldIdx).HEIGHT < stage.getHeight()) {
			moveField(0, (stage.getHeight() - FIELDS.get(currentFieldIdx).HEIGHT) / 2);
		}
	}

	public void moveField(double x, double y) {
		for (int i = 0; i < FIELDS.get(currentFieldIdx).getChunks().length(); i++) {
			FIELDS.get(currentFieldIdx).getChunks().get(i).BODY
					.setTranslateX(FIELDS.get(currentFieldIdx).getChunks().get(i).BODY.getTranslateX() + x);
			FIELDS.get(currentFieldIdx).getChunks().get(i).BODY
					.setTranslateY(FIELDS.get(currentFieldIdx).getChunks().get(i).BODY.getTranslateY() + y);
		}
		for (int i = 0; i < FIELDS.get(currentFieldIdx).getObstacles().length(); i++) {
			FIELDS.get(currentFieldIdx).getObstacles().get(i).move(x, y);
		}
	}

	public void moveFieldTo(double x, double y) {
		for (int i = 0; i < FIELDS.get(currentFieldIdx).getChunks().length(); i++) {
			FIELDS.get(currentFieldIdx).getChunks().get(i).BODY
					.setTranslateX(FIELDS.get(currentFieldIdx).getChunks().get(i).OFFSET_X + x);
			FIELDS.get(currentFieldIdx).getChunks().get(i).BODY
					.setTranslateY(FIELDS.get(currentFieldIdx).getChunks().get(i).OFFSET_Y + y);
		}
		for (int i = 0; i < FIELDS.get(currentFieldIdx).getObstacles().length(); i++) {
			FIELDS.get(currentFieldIdx).getObstacles().get(i).moveTo(x, y);
		}
	}

	public Field getCurrentField() {
		return FIELDS.get(currentFieldIdx);
	}
}

package testing;

import java.io.File;

import finished.List;
import javafx.scene.Group;

public class Map {

	public final List<Field> FIELDS;
	private int currentFieldIdx;

	public Map() {
		FIELDS = new List<>();
		File[] mapFolder = new File("assets/map").listFiles();
		for (int i = 0; i < mapFolder.length; i++) {
			FIELDS.add(new Field(mapFolder[i].getPath()));
		}
		currentFieldIdx = -1;
	}

	public void switchMapTo(int idx, Group root) {
		if (currentFieldIdx == idx || idx >= FIELDS.length() || idx < 0) {
			return;
		}
		if (currentFieldIdx == -1) {
			currentFieldIdx = idx;
			for (int i = 0; i < FIELDS.get(currentFieldIdx).CHUNKS.length(); i++) {
				root.getChildren().add(i,FIELDS.get(currentFieldIdx).CHUNKS.get(i).BODY);
			}
			return;
		}
		for (int i = 0; i < FIELDS.get(currentFieldIdx).CHUNKS.length(); i++) {
			root.getChildren().remove(FIELDS.get(currentFieldIdx).CHUNKS.get(i).BODY);
		}
		currentFieldIdx = idx;
		for (int i = 0; i < FIELDS.get(currentFieldIdx).CHUNKS.length(); i++) {
			root.getChildren().add(i,FIELDS.get(currentFieldIdx).CHUNKS.get(i).BODY);
		}
	}
}

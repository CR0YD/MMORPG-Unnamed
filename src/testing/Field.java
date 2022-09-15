package testing;

import java.io.File;
import java.io.FileNotFoundException;

import finished.List;

public class Field {

	public final List<Chunk> CHUNKS;
	private final File[] FILES;
	public final int WIDTH, HEIGHT;
	private String[] objects;

	public final String PATH;
	private final List<String[]> OBJECT_MODEL;
	private List<Obstacle> obstacles;

	public Field(String path, List<String[]> objectModel) {
		PATH = path;
		OBJECT_MODEL = objectModel;
		CHUNKS = new List<>();
		FILES = new File(PATH).listFiles();
		int maxX = 0, maxY = 0;
		for (int i = 0; i < FILES.length; i++) {
			if (!FILES[i].getPath().endsWith(".png")) {
				continue;
			}
			try {
				CHUNKS.add(new Chunk(FILES[i].getPath()));
				if (maxX < CHUNKS.get(CHUNKS.length() - 1).BODY.getTranslateX()) {
					maxX = (int) CHUNKS.get(CHUNKS.length() - 1).BODY.getTranslateX();
				}
				if (maxY < CHUNKS.get(CHUNKS.length() - 1).BODY.getTranslateY()) {
					maxY = (int) CHUNKS.get(CHUNKS.length() - 1).BODY.getTranslateY();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		WIDTH = maxX + 480;
		HEIGHT = maxY + 480;

		obstacles = new List<>();
		createObjects();
	}

	private void createObjects() {
		FieldObjectsReader reader = new FieldObjectsReader(PATH + "\\" + new File(PATH).getName());
		objects = reader.read();
		String objectName = "";
		String[] objectParameters;
		int blueprintID;
		for (int i = 0; i < objects.length; i++) {
			objectName = objects[i].substring(0, objects[i].indexOf("("));
			objectParameters = objects[i].substring(objects[i].indexOf("(") + 1, objects[i].indexOf(")")).split(",");

			blueprintID = -1;
			for (int j = 0; j < OBJECT_MODEL.length(); j++) {
				for (int k = 0; k < OBJECT_MODEL.get(j).length; k++) {
					if (OBJECT_MODEL.get(j)[k].split(":")[0].equals("name")) {
						if (OBJECT_MODEL.get(j)[k].split(":")[1].equals(objectName)) {
							blueprintID = j;
							break;
						}
						continue;
					}
				}
			}
			if (blueprintID == -1) {
				System.err.println("There is no object named '" + objectName + "'.");
				continue;
			}

			for (int j = 0; j < OBJECT_MODEL.get(blueprintID).length; j++) {
				if (OBJECT_MODEL.get(blueprintID)[j].split(":")[0].equals("type")) {
					if (OBJECT_MODEL.get(blueprintID)[j].split(":")[1].equals("obstacle")) {
						try {
							obstacles.add(new Obstacle(connectArrays(blueprintID, objectParameters)));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
		}
	}
	
	private String[] connectArrays(int blueprintID, String[] objectParameters) {
		String allObjectParameters = "";
		for (int i = 0; i < OBJECT_MODEL.get(blueprintID).length; i++) {
			if (OBJECT_MODEL.get(blueprintID)[i].split(":")[0].equals("name") || OBJECT_MODEL.get(blueprintID)[i].split(":")[0].equals("type") || OBJECT_MODEL.get(blueprintID)[i].split(":")[1].equals("none")) {
				continue;
			}
			allObjectParameters += OBJECT_MODEL.get(blueprintID)[i] + "qwertzuiop";
		}
		for (int i = 0; i < objectParameters.length; i++) {
			allObjectParameters += objectParameters[i] + "qwertzuiop";
		}
		allObjectParameters = allObjectParameters.replace("fWidth", "" + WIDTH).replace("fHeight", "" + HEIGHT);
		return allObjectParameters.split("qwertzuiop");
	}
	
	public List<Obstacle> getObstacles() {
		return obstacles;
	}
}

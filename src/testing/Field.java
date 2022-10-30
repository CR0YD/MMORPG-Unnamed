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
	private final List<ObjectModel> OBJECT_MODEL;
	private List<Obstacle> obstacles;

	public Field(String path, List<ObjectModel> objectModel) {
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
				if (OBJECT_MODEL.get(j).NAME.equals(objectName)) {
					blueprintID = j;
					break;
				}
			}
			if (blueprintID == -1) {
				System.err.println("There is no object named '" + objectName + "'.");
				continue;
			}

			if (OBJECT_MODEL.get(blueprintID).TYPE.equals("obstacle")) {
				try {
					obstacles.add(ObstacleCreator.createObstacle(connectArrays(blueprintID, objectParameters)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String[] connectArrays(int blueprintID, String[] objectParameters) {
		String allObjectParameters = "";
		for (int i = 0; i < OBJECT_MODEL.get(blueprintID).PARAMETERS.length && !OBJECT_MODEL.get(blueprintID).PARAMETERS[0].equals(""); i++) {
			if (OBJECT_MODEL.get(blueprintID).PARAMETERS[i].substring(OBJECT_MODEL.get(blueprintID).PARAMETERS[i].indexOf("++") + 2).equals("none")) {
				continue;
			}
			allObjectParameters += OBJECT_MODEL.get(blueprintID).PARAMETERS[i] + "qwertzuiop";
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

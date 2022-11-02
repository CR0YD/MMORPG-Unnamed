package testing;

import java.io.File;
import java.io.FileNotFoundException;

import finished.List;
import javafx.scene.shape.Rectangle;

public class Field {

	private List<Chunk> chunks;
	private final File[] FILES;
	public final int WIDTH, HEIGHT;
	private String[] objects;

	public final String PATH;
	private final List<ObjectModel> OBJECT_MODEL;
	private List<Obstacle> obstacles;
	private List<Rectangle> collisionRectangles;

	public Field(String path, List<ObjectModel> objectModel) {
		PATH = path;
		OBJECT_MODEL = objectModel;
		chunks = new List<>();
		FILES = new File(PATH).listFiles();
		int maxX = 0, maxY = 0;
		for (int i = 0; i < FILES.length; i++) {
			if (!FILES[i].getPath().endsWith(".png")) {
				continue;
			}
			try {
				chunks.add(new Chunk(FILES[i].getPath()));
				if (maxX < chunks.get(chunks.length() - 1).BODY.getTranslateX()) {
					maxX = (int) chunks.get(chunks.length() - 1).BODY.getTranslateX();
				}
				if (maxY < chunks.get(chunks.length() - 1).BODY.getTranslateY()) {
					maxY = (int) chunks.get(chunks.length() - 1).BODY.getTranslateY();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		WIDTH = maxX + 480;
		HEIGHT = maxY + 480;

		obstacles = new List<>();
		collisionRectangles = new List<>();
		createObjects();
	}

	public List<Chunk> getChunks() {
		return chunks;
	}

	public List<Rectangle> getCollisionDummies() {
		return collisionRectangles;
	}

	private void createObjects() {
		FieldObjectsReader reader = new FieldObjectsReader(PATH + "\\" + new File(PATH).getName());
		objects = reader.read();
		String objectName = "";
		String[] objectParameters;
		int blueprintID;
		boolean objectAddedToList = false;
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
					Obstacle newObstacle = ObstacleCreator.createObstacle(connectArrays(blueprintID, objectParameters));
					for (int j = 0; j < collisionRectangles.length(); j++) {
						if (collisionRectangles.get(j).getTranslateY()
								+ collisionRectangles.get(j).getHeight() < newObstacle.getCollisionBox().getTranslateY()
										+ newObstacle.getCollisionBox().getHeight()) {
							collisionRectangles.add(newObstacle.getCollisionBox(), j + 1);
							break;
						}
					}
					if (collisionRectangles.length() == 0) {
						collisionRectangles.add(newObstacle.getCollisionBox());
					}
					if (obstacles.length() == 0) {
						obstacles.add(newObstacle);
						continue;
					}
					objectAddedToList = false;
					for (int j = 0; j < obstacles.length(); j++) {
						if (obstacles.get(j).getCollisionBox().getTranslateY()
								+ obstacles.get(j).getCollisionBox().getHeight() < newObstacle.getCollisionBox().getTranslateY()
										+ newObstacle.getCollisionBox().getHeight()) {
							obstacles.add(newObstacle, j + 1);
							objectAddedToList = true;
							break;
						}
					}
					if (!objectAddedToList) {
						obstacles.add(newObstacle);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String[] connectArrays(int blueprintID, String[] objectParameters) {
		String allObjectParameters = "";
		for (int i = 0; i < OBJECT_MODEL.get(blueprintID).PARAMETERS.length
				&& !OBJECT_MODEL.get(blueprintID).PARAMETERS[0].equals(""); i++) {
			if (OBJECT_MODEL.get(blueprintID).PARAMETERS[i]
					.substring(OBJECT_MODEL.get(blueprintID).PARAMETERS[i].indexOf("++") + 2).equals("none")) {
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

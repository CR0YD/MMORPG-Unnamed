package testing;

import java.io.File;
import java.io.FileNotFoundException;

import finished.List;

public class Field {

	public final List<Chunk> CHUNKS;
	private final File[] FILES;
	public final int WIDTH, HEIGHT;

	public final String PATH;

	public Field(String path) {
		PATH = path;
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
	}
}

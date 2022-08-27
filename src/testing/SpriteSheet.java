package testing;

import java.io.FileInputStream;

public class SpriteSheet {

	public final String IMAGE_PATH;
	public final int COLUMN_WIDTH, ROW_HEIGHT;
	
	public SpriteSheet(String imagePath, int columnWidth, int rowHeight) {
		try {
			new FileInputStream(imagePath).close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		IMAGE_PATH = imagePath;
		COLUMN_WIDTH = columnWidth;
		ROW_HEIGHT = rowHeight;
	}
	
}

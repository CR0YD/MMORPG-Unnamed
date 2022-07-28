package testing;

import java.io.FileInputStream;
import java.io.IOException;

public class SpriteSheet {

	public final String IMAGE_PATH;
	public final int COLUMN_WIDTH, ROW_HEIGHT;
	
	public SpriteSheet(String imagePath, int columnWidth, int rowHeight) throws IOException {
		new FileInputStream(imagePath).close();
		IMAGE_PATH = imagePath;
		COLUMN_WIDTH = columnWidth;
		ROW_HEIGHT = rowHeight;
	}
	
}

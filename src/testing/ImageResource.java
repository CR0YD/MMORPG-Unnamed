package testing;

import java.io.FileInputStream;
import java.io.IOException;

public class ImageResource {

	public final String PATH;
	public final int WIDTH, HEIGHT;
	
	public ImageResource(String path, int width, int height) throws IOException {
		new FileInputStream(path).close();
		PATH = path;
		WIDTH = width;
		HEIGHT = height;
	}
	
}

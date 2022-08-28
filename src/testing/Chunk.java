package testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Chunk {

	public final ImageView BODY;
	public final int OFFSET_X, OFFSET_Y;

	public Chunk(String path) throws FileNotFoundException {
		BODY = new ImageView(new Image(new FileInputStream(new File(path))));
		while (path.contains("\\")) {
			path = path.substring(path.indexOf("\\") + 1);
		}
		OFFSET_Y = Integer.parseInt(path.substring(0, path.indexOf(".")).split("-")[0]) * 480;
		OFFSET_X = Integer.parseInt(path.substring(0, path.indexOf(".")).split("-")[1]) * 480;
		BODY.setTranslateX(OFFSET_X);
		BODY.setTranslateY(OFFSET_Y);
		BODY.setId(path);
	}

}

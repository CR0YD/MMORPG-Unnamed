package testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Chunk {

	public final ImageView BODY;
	
	public Chunk(String path) throws FileNotFoundException {
		BODY = new ImageView(new Image(new FileInputStream(new File(path))));
		while (path.contains("\\")) {
			path = path.substring(path.indexOf("\\") + 1);
		}
		BODY.setTranslateX(Integer.parseInt(path.substring(0, path.indexOf(".")).split("-")[0]) * 480);
		BODY.setTranslateY(Integer.parseInt(path.substring(0, path.indexOf(".")).split("-")[1]) * 480);
	}
	
}

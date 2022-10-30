package testing;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class ObstacleCreator {

	private static Image sprite;
	private static ImageView imageView;
	private static Rectangle collisionBox;
	private static Visualizer visualizer;

	private static String x, y, cX, cY, cW, cH, spritePath, scale, animation;

	public static Obstacle createObstacle(String[] parameters) throws IOException {
		resetAttributes();
		for (int i = 0; i < parameters.length; i++) {
			switch (parameters[i].contains("++") ? parameters[i].substring(0, parameters[i].indexOf("++")) : parameters[i].split(":")[0]) {
			case "x" -> x = parameters[i].split(":")[1];
			case "y" -> y = parameters[i].split(":")[1];
			case "cX" -> cX = parameters[i].split(":")[1];
			case "cY" -> cY = parameters[i].split(":")[1];
			case "cW" -> cW = parameters[i].split(":")[1];
			case "cH" -> cH = parameters[i].split(":")[1];
			case "sprite" -> spritePath = parameters[i].substring(parameters[i].indexOf("++") + 2);
			case "scale" -> scale = parameters[i].split(":")[1];
			case "animation" -> animation = parameters[i].substring(parameters[i].indexOf("++") + 2);
			default -> {
			}
			}
		}
		processSprite();
		processVisualization();
		processImageViewCoordinates();
		processHitboxValues();
		return new Obstacle(visualizer, collisionBox);
	}

	private static void resetAttributes() {
		collisionBox = null;
		imageView = null;
		sprite = null;
		x = "";
		y = "";
		cX = "";
		cY = "";
		cW = "";
		cH = "";
		spritePath = "";
		scale = "";
		animation = "";
	}

	private static void processSprite() throws IOException {
		if (spritePath.equals("")) {
			imageView = new ImageView();
			return;
		}
		if (!spritePath.equals("") && scale.equals("")) {
			FileInputStream read = new FileInputStream(new File(spritePath));
			sprite = new Image(read);
			read.close();
			imageView = new ImageView(sprite);
			imageView.setViewport(new Rectangle2D(0, 0, sprite.getWidth(), sprite.getHeight()));
			return;
		}
		if (!spritePath.equals("") && !scale.equals("")) {
			FileInputStream read = new FileInputStream(scaleImage(Double.parseDouble(scale), spritePath));
			sprite = new Image(read);
			read.close();
			imageView = new ImageView(sprite);
			imageView.setViewport(new Rectangle2D(0, 0, sprite.getWidth(), sprite.getHeight()));
			return;
		}
	}

	private static File scaleImage(double scale, String path) throws IOException {
		String newImagePath = path.split(".png")[0] + (scale == 1 ? "" : scale) + ".png";
		File newImageFile = new File(newImagePath);
		if (!newImageFile.exists()) {
			BufferedImage img = ImageIO.read(new File(path));
			BufferedImage newImage = new BufferedImage((int) (img.getWidth() * scale), (int) (img.getHeight() * scale),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = newImage.createGraphics();
			g2d.drawImage(img, 0, 0, (int) (img.getWidth() * scale), (int) (img.getHeight() * scale), null);
			newImageFile = new File(newImagePath);
			if (!newImageFile.exists()) {
				newImageFile.createNewFile();
			}
			ImageIO.write(newImage, "png", newImageFile);
		}
		return newImageFile;
	}

	private static void processImageViewCoordinates() {
		if (!x.equals("")) {
			imageView.setTranslateX(Integer.parseInt(x));
		}
		if (!y.equals("")) {
			imageView.setTranslateY(Integer.parseInt(y));
		}
	}

	private static void processHitboxValues() {
		collisionBox = new Rectangle();
		collisionBox.setTranslateX(imageView.getTranslateX());
		if (!cX.equals("")) {
			collisionBox.setTranslateX(imageView.getTranslateX() + Integer.parseInt(cX));
		}
		collisionBox.setTranslateY(imageView.getTranslateY());
		if (!cY.equals("")) {
			collisionBox.setTranslateY(imageView.getTranslateY() + Integer.parseInt(cY));
		}
		if (!cW.equals("") && !cW.equals("sWidth")) {
			collisionBox.setWidth(Integer.parseInt(cW));
		}
		if ((cW.equals("") || cW.equals("sWidth")) && sprite != null) {
			collisionBox.setWidth(imageView.getViewport().getWidth());
		}
		if (!cH.equals("") && !cH.equals("sHeight")) {
			collisionBox.setHeight(Integer.parseInt(cH));
		}
		if ((cH.equals("") || cH.equals("sHeight")) && sprite != null) {
			collisionBox.setHeight(imageView.getViewport().getHeight());
		}
	}

	private static void processVisualization() {
		if (sprite == null) {
			visualizer = new Visualizer(imageView, 0, 0);
			return;
		}
		if (animation.equals("")) {
			visualizer = new Visualizer(imageView, sprite.getWidth(), sprite.getHeight());
			return;
		}
		int width = 0, height = 0;
		String[] animationParameterArr = animation.split(";");
		if (animationParameterArr[0].substring(0, animationParameterArr[0].indexOf("(")).equals("aParam")) {
			width = Integer.parseInt(animationParameterArr[0]
					.substring(animationParameterArr[0].indexOf("(") + 1, animationParameterArr[0].length() - 1)
					.split(",")[0]);
			height = Integer.parseInt(animationParameterArr[0]
					.substring(animationParameterArr[0].indexOf("(") + 1, animationParameterArr[0].length() - 1)
					.split(",")[1]);
		}
		visualizer = new Visualizer(imageView, width * Double.parseDouble(scale), height * Double.parseDouble(scale));
		String animationName = "";
		String[] animationArr;
		for (int i = 1; i < animationParameterArr.length; i++) {
			animationName = animationParameterArr[i].substring(0, animationParameterArr[i].indexOf("("));
			visualizer.addAnimation(animationName, Integer.parseInt(animationParameterArr[i].substring(animationParameterArr[i].indexOf(")") + 2, animationParameterArr[i].length())));
			animationArr = animationParameterArr[i]
					.substring(animationParameterArr[i].indexOf("(") + 1, animationParameterArr[i].indexOf(")"))
					.split(",");
			for (int j = 0; j < animationArr.length; j++) {
				visualizer.addFrameToAnimation(animationName,
						Integer.parseInt(animationArr[j].split("-")[0]),
						Integer.parseInt(animationArr[j].split("-")[1]));
			}
		}
	}

}

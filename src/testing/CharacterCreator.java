package testing;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class CharacterCreator {

	private static Image sprite;
	private static ImageView imageView;
	private static Rectangle collisionBox;
	private static Visualizer visualizer;
	private static Point2D centerPoint;

	private static String collisionBoxMeasures, spritePath, scale, animation, center;

	public static Character createCharacter(String[] parameters) throws IOException {
		resetAttributes();
		for (int i = 0; i < parameters.length; i++) {
			switch (parameters[i].substring(0, parameters[i].indexOf("++"))) {
			case "collisionBox" -> collisionBoxMeasures = parameters[i].substring(parameters[i].indexOf("++") + 2);
			case "sprite" -> spritePath = parameters[i].substring(parameters[i].indexOf("++") + 2);
			case "scale" -> scale = parameters[i].substring(parameters[i].indexOf("++") + 2);
			case "animation" -> animation = parameters[i].substring(parameters[i].indexOf("++") + 2);
			case "center" -> center = parameters[i].substring(parameters[i].indexOf("++") + 2);
			default -> {
			}
			}
		}
		processSprite();
		processVisualization();
		processHitboxValues();
		processCenter();
		return new Character(visualizer, collisionBox, centerPoint);
	}

	private static void resetAttributes() {
		sprite = null;
		imageView = null;
		collisionBox = null;
		visualizer = null;
		centerPoint = null;
		collisionBoxMeasures = "";
		spritePath = "";
		scale = "";
		animation = "";
		center = "";
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

	private static void processHitboxValues() {
		collisionBox = new Rectangle(0, 0, 0, 0);
		String[] collisionBoxMeasuresArr = collisionBoxMeasures.split(",");
		if (collisionBoxMeasuresArr.length > 4 || collisionBoxMeasuresArr.length == 0) {
			return;
		}
		double scalingFactor = 1;
		if (!scale.equals("")) {
			scalingFactor = Double.parseDouble(scale);
		}
		collisionBox.setTranslateX(Integer.parseInt(collisionBoxMeasuresArr[0]) * scalingFactor);
		collisionBox.setTranslateY(Integer.parseInt(collisionBoxMeasuresArr[1]) * scalingFactor);
		try {
			collisionBox.setWidth(Integer.parseInt(collisionBoxMeasuresArr[2]) * scalingFactor);
		} catch (Exception e) {
			e.printStackTrace();
			if (collisionBoxMeasuresArr[2].equals("sWidth")) {
				collisionBox.setWidth(imageView.getViewport().getWidth() * scalingFactor);
			}
		}
		try {
			collisionBox.setHeight(Integer.parseInt(collisionBoxMeasuresArr[3]) * scalingFactor);
		} catch (Exception e) {
			e.printStackTrace();
			if (collisionBoxMeasuresArr[3].equals("sHeight")) {
				collisionBox.setHeight(imageView.getViewport().getHeight() * scalingFactor);
			}
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
				visualizer.addFrameToAnimation(animationName, Integer.parseInt(animationArr[j].split("-")[0]),
						Integer.parseInt(animationArr[j].split("-")[1]));
			}
		}
	}

	private static void processCenter() {
		double scalingFactor = 1;
		if (!scale.equals("")) {
			scalingFactor = Double.parseDouble(scale);
		}
		if (center.equals("")) {
			centerPoint = new Point2D((visualizer.getFrameWidth() * scalingFactor) / 2,
					(visualizer.getFrameHeight() * scalingFactor) / 2);
			return;
		}
		centerPoint = new Point2D(Double.parseDouble(center.split(",")[0]) * scalingFactor,
				Double.parseDouble(center.split(",")[0]) * scalingFactor);
	}

}

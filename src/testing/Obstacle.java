package testing;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Obstacle {

	private Rectangle collisionBox;
	private ImageView image;
	private Image sprite;
	// o -> object; c -> collision
	private double oX, oY, cX, cY;

	// The different forms an object can be created with
	private final String[] CASE_0 = { "x", "y", "cX", "cY", "cW", "cH" };
	private final String[] CASE_1 = { "sprite", "x", "y", "scale", "cX", "cY", "cW", "cH" };
	private final String[][] CASES = { CASE_0, CASE_1 };

	public Obstacle(String[] parameters) throws Exception {
		int count = 0;
		for (int i = 0; i < CASES.length; i++) {
			if (parameters.length == CASES[i].length) {
				for (int j = 0; j < parameters.length; j++) {
					if (CASES[i][j].equals(parameters[j].split(":")[0])) {
						count++;
					}
				}
				if (count == CASES[i].length) {
					chooseProcessingMethode(i, parameters);
					return;
				}
				throw new Exception("The parameters cannot be read!");
			}
		}
	}

	private void chooseProcessingMethode(int pCase, String[] parameters) throws FileNotFoundException {
		switch (pCase) {
		case 0:
			processAsCase0(parameters);
			break;
		case 1:
			processAsCase1(parameters);
			break;

		default:
			break;
		}
	}

	private void processAsCase0(String[] parameters) {
		image = new ImageView();
		image.setTranslateX(Integer.parseInt(parameters[0].split(":")[1]));
		oX = Integer.parseInt(parameters[0].split(":")[1]);
		image.setTranslateY(Integer.parseInt(parameters[1].split(":")[1]));
		oY = Integer.parseInt(parameters[1].split(":")[1]);
		collisionBox = new Rectangle(0, 0, Integer.parseInt(parameters[4].split(":")[1]),
				Integer.parseInt(parameters[5].split(":")[1]));
		collisionBox.setTranslateX(oX + Integer.parseInt(parameters[2].split(":")[1]));
		cX = collisionBox.getTranslateX();
		collisionBox.setTranslateY(oY + Integer.parseInt(parameters[3].split(":")[1]));
		cY = collisionBox.getTranslateY();
	}

	private void processAsCase1(String[] parameters) throws FileNotFoundException {
//		for (int i = 0; i < parameters.length; i++) {
//			System.out.println(parameters[i]);
//		}

		double scale = Double.parseDouble(parameters[3].split(":")[1]);
		String newImagePath = parameters[0].split(":")[1].split(".png")[0] + (scale == 1 ? "" : scale) + ".png";
		File newImageFile = new File(newImagePath);
		if (!newImageFile.exists()) {
			try {
				BufferedImage img = ImageIO.read(new File(parameters[0].split(":")[1]));
				BufferedImage newImage = new BufferedImage((int) (img.getWidth() * scale),
						(int) (img.getHeight() * scale), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = newImage.createGraphics();
				g2d.drawImage(img, 0, 0, (int) (img.getWidth() * scale), (int) (img.getHeight() * scale), null);
				newImageFile = new File(newImagePath);
				if (!newImageFile.exists()) {
					newImageFile.createNewFile();
				}
				ImageIO.write(newImage, "png", newImageFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		sprite = new Image(new FileInputStream(newImageFile));
		image = new ImageView(sprite);
		prepareParameter(parameters);
		image.setTranslateX(Integer.parseInt(parameters[1].split(":")[1]));
		oX = Integer.parseInt(parameters[1].split(":")[1]);
		image.setTranslateY(Integer.parseInt(parameters[2].split(":")[1]));
		oY = Integer.parseInt(parameters[2].split(":")[1]);

		collisionBox = new Rectangle(0, 0, Integer.parseInt(parameters[6].split(":")[1]),
				Integer.parseInt(parameters[7].split(":")[1]));
		collisionBox.setTranslateX(oX + Integer.parseInt(parameters[4].split(":")[1]));
		cX = Integer.parseInt(parameters[4].split(":")[1]);
		collisionBox.setTranslateY(oY + Integer.parseInt(parameters[5].split(":")[1]));
		cY = Integer.parseInt(parameters[5].split(":")[1]);

	}

	private String[] prepareParameter(String[] parameters) {
		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = parameters[i].replace("sWidth", image.getViewport() == null ? ("" + (int) sprite.getWidth())
					: ("" + (int) image.getViewport().getWidth()));
			parameters[i] = parameters[i].replace("sHeight",
					image.getViewport() == null ? ("" + (int) sprite.getHeight())
							: ("" + (int) image.getViewport().getHeight()));
		}
		return parameters;
	}

	public void move(double x, double y) {
		image.setTranslateX(image.getTranslateX() + x);
		image.setTranslateY(image.getTranslateY() + y);
		collisionBox.setTranslateX(collisionBox.getTranslateX() + x);
		collisionBox.setTranslateY(collisionBox.getTranslateY() + y);
	}

	public void moveTo(double x, double y) {
		image.setTranslateX(oX + x);
		image.setTranslateY(oY + y);
		collisionBox.setTranslateX(oX + cX + x);
		collisionBox.setTranslateY(oX + cY + y);
	}

	public Rectangle getCollisionBox() {
		return collisionBox;
	}

	public ImageView getImageView() {
		return image;
	}

}

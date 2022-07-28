package testing;

import java.io.FileInputStream;
import java.io.IOException;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapObject {

	protected final SpriteSheet SPRITES;
	protected ImageView body;
	public final double WIDTH, HEIGHT;
	protected final Animator ANIMATOR;

	public MapObject(SpriteSheet sprites, double x, double y, double width, double height, Animator animator) {
		SPRITES = sprites;
		WIDTH = width;
		HEIGHT = height;
		try {
			body = new ImageView(new Image(new FileInputStream(SPRITES.IMAGE_PATH)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		body.setFitWidth(width);
		body.setFitHeight(height);
		ANIMATOR = animator;
		ANIMATOR.setCurrentAnimation("STANDARD");
		body.setViewport(ANIMATOR.getViewport());
		moveTo(x, y);
	}

	public double getWidth() {
		return body.getFitWidth();
	}

	public double getHeight() {
		return body.getFitHeight();
	}

	public void nextAnimationFrame() {
		ANIMATOR.nextAnimationFrame();
		body.setViewport(ANIMATOR.getViewport());
	}

	public void changeAnimationTo(String animationName) {
		ANIMATOR.setCurrentAnimation(animationName);
		body.setViewport(ANIMATOR.getViewport());
	}

	public String getCurrentAnimationName() {
		return ANIMATOR.getCurrentAnimationName();
	}

	public int getCurrentFrameIdx() {
		return ANIMATOR.getCurrentFrameIdx();
	}

	public ImageView getBody() {
		return body;
	}

	public void switchImageTo(int column, int row) {
		body.setViewport(new Rectangle2D(SPRITES.COLUMN_WIDTH * column, SPRITES.ROW_HEIGHT * row, SPRITES.COLUMN_WIDTH,
				SPRITES.ROW_HEIGHT));
	}

	public void move(double x, double y) {
		body.setTranslateX(body.getTranslateX() + x);
		body.setTranslateY(body.getTranslateY() + y);
	}

	public void moveTo(double x, double y) {
		body.setTranslateX(x);
		body.setTranslateY(y);
	}

}

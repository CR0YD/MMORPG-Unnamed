package testing;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapObject {

	protected ImageView body;
	public final double WIDTH, HEIGHT;
	protected final Animator ANIMATOR;
	protected Point center;

	public MapObject(double x, double y, double width, double height, Animator animator, String firstAnimationName) {
		WIDTH = width;
		HEIGHT = height;
		ANIMATOR = animator;
		ANIMATOR.setCurrentAnimation(firstAnimationName);
		try {
			body = new ImageView(new Image(new FileInputStream(ANIMATOR.SPRITES.IMAGE_PATH)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		body.setFitWidth(width);
		body.setFitHeight(height);
		body.setViewport(ANIMATOR.getViewport());
		moveTo(x, y);
	}

	protected void setCenter(double x, double y) {
		center = new Point((int) x, (int) y);
	}

	public Point getCenter() {
		return center;
	}
	
	public double getX() {
		return body.getTranslateX();
	}

	public double getY() {
		return body.getTranslateY();
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

	public Node getNode() {
		return body;
	}

	public Bounds getBounds() {
		return body.getBoundsInParent();
	}

	public void switchImageTo(int column, int row) {
		body.setViewport(new Rectangle2D(ANIMATOR.SPRITES.COLUMN_WIDTH * column, ANIMATOR.SPRITES.ROW_HEIGHT * row,
				ANIMATOR.SPRITES.COLUMN_WIDTH, ANIMATOR.SPRITES.ROW_HEIGHT));
	}

	public void move(double x, double y) {
		body.setTranslateX(body.getTranslateX() + x);
		body.setTranslateY(body.getTranslateY() + y);
		center.x += x;
		center.y += y;
	}

	public void moveTo(double x, double y) {
		body.setTranslateX(x);
		body.setTranslateY(y);
	}

}

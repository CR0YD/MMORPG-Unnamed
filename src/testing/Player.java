package testing;

import javafx.scene.shape.Rectangle;

public class Player {

	private Visualizer visualizer;
	private Rectangle collisionbox;
	private double hitboxOffsetX, hitboxOffsetY;

	public Player(double x, double y, Visualizer visualizer, Rectangle collisionbox) {
		this.visualizer = visualizer;
		this.collisionbox = collisionbox;
		hitboxOffsetX = collisionbox.getTranslateX();
		hitboxOffsetY = collisionbox.getTranslateY();
		moveTo(x, y);
	}
	
	public double getX() {
		return visualizer.getImageView().getTranslateX();
	}
	
	public double getY() {
		return visualizer.getImageView().getTranslateY();
	}

	public void move(double x, double y) {
		visualizer.getImageView().setTranslateX(visualizer.getImageView().getTranslateX() + x);
		visualizer.getImageView().setTranslateY(visualizer.getImageView().getTranslateY() + y);
		collisionbox.setTranslateX(collisionbox.getTranslateX() + x);
		collisionbox.setTranslateY(collisionbox.getTranslateY() + y);
	}

	public void moveTo(double x, double y) {
		visualizer.getImageView().setTranslateX(x);
		visualizer.getImageView().setTranslateY(y);
		collisionbox.setTranslateX(hitboxOffsetX + x);
		collisionbox.setTranslateY(hitboxOffsetY + y);
	}
	
	public void moveFromCollisionBoxTo(double x, double y) {
		visualizer.getImageView().setTranslateX(x - hitboxOffsetX);
		visualizer.getImageView().setTranslateY(y - hitboxOffsetY);
		collisionbox.setTranslateX(x);
		collisionbox.setTranslateY(y);
	}
	
	public Visualizer getVisualizer() {
		return visualizer;
	}
	
	public Rectangle getCollisionBox() {
		return collisionbox;
	}

}
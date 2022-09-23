package testing;

import javafx.scene.shape.Rectangle;

public class Obstacle {
	
	private Rectangle collisionBox;
	private double collisionOffsetX, collisionOffsetY;
	private Visualizer visualizer;
	
	public Obstacle(Visualizer visualizer, Rectangle collisionBox) {
		this.collisionBox = collisionBox;
		collisionOffsetX = collisionBox.getTranslateX() - visualizer.getImageView().getTranslateX();
		collisionOffsetY = collisionBox.getTranslateY() - visualizer.getImageView().getTranslateY();
		this.visualizer = visualizer;
	}
	
	public void move(double x, double y) {
		visualizer.getImageView().setTranslateX(visualizer.getImageView().getTranslateX() + x);
		visualizer.getImageView().setTranslateY(visualizer.getImageView().getTranslateY() + y);
		collisionBox.setTranslateX(collisionBox.getTranslateX() + x);
		collisionBox.setTranslateY(collisionBox.getTranslateY() + y);
	}
	
	public void moveTo(double x, double y) {
		visualizer.getImageView().setTranslateX(x);
		visualizer.getImageView().setTranslateY(y);
		collisionBox.setTranslateX(visualizer.getImageView().getTranslateX() + collisionOffsetX);
		collisionBox.setTranslateY(visualizer.getImageView().getTranslateY() + collisionOffsetY);
	}
	
	public Rectangle getCollisionBox() {
		return collisionBox;
	}
	
	public Visualizer getVisualizer() {
		return visualizer;
	}
	
	public boolean hasAnimation() {
		return visualizer.isAnimated();
	}

}

package testing;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class Character {
	
	private Visualizer visualizer;
	private Rectangle collisionBox;
	private Point2D centerPoint;
	
	public Character(Visualizer visualizer, Rectangle collisionBox, Point2D centerPoint) {
		this.visualizer = visualizer;
		this.collisionBox = collisionBox;
		this.centerPoint = centerPoint;
	}
	
	public Visualizer getVisualizer() {
		return visualizer;
	}
	
	public Rectangle getCollisionBox() {
		return collisionBox;
	}
	
	public Point2D getCenterPoint() {
		return centerPoint;
	}

}

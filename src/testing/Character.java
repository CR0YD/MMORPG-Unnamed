package testing;

import javafx.scene.shape.Rectangle;

public class Character {
	
	private Visualizer visualizer;
	private Rectangle collisionBox;
	
	public Character(Visualizer visualizer, Rectangle collisionBox) {
		this.visualizer = visualizer;
		this.collisionBox = collisionBox;
	}
	
	public Visualizer getVisualizer() {
		return visualizer;
	}
	
	public Rectangle getCollisionBox() {
		return collisionBox;
	}

}

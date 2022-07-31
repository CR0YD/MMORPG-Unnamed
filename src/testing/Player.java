package testing;

import java.io.IOException;

import javafx.scene.paint.Color;

public class Player extends MapObject {

	public ObjectBox hitboxUp, hitboxDown, hitboxLeft, hitboxRight, interactionTriggerBox;

	public enum CollisionDirection {
		Up, Down, Left, Right;
	}

	public Player(double x, double y, double width, double height, Animator animator) throws IOException {
		super(x, y, width, height, animator, "IDLE_DOWN");
		firstMoveTo(x, y);
	}

	@Override
	public void move(double x, double y) {
		super.move(x, y);
		alignHitbox();
		interactionTriggerBox.moveTo(body.getTranslateX(), body.getTranslateY());
	}

	/**
	 * Moving the player to the right position without moving the not yet
	 * initialized hit boxes.
	 */
	private void firstMoveTo(double x, double y) {
		body.setTranslateX(x);
		body.setTranslateY(y);
	}

	public void moveHitbox(double x, double y) {
		hitboxUp.move(x, y);
		hitboxDown.move(x, y);
		hitboxLeft.move(x, y);
		hitboxRight.move(x, y);
	}

	public void moveHitboxTo(double x, double y) {
		hitboxUp.moveTo(x, y);
		hitboxDown.moveTo(x, y);
		hitboxLeft.moveTo(x, y);
		hitboxRight.moveTo(x, y);
	}

	public void alignHitbox() {
		hitboxUp.moveTo(body.getTranslateX(), body.getTranslateY());
		hitboxDown.moveTo(body.getTranslateX(), body.getTranslateY());
		hitboxLeft.moveTo(body.getTranslateX(), body.getTranslateY());
		hitboxRight.moveTo(body.getTranslateX(), body.getTranslateY());
	}

	public boolean isColliding(List<ObjectBox> collidingObjectHitbox, CollisionDirection detecionDirection) {
		for (int i = 0; i < collidingObjectHitbox.length(); i++) {
			// Moving the player to the nearest possible point, at which he doesn't collide
			// with the object that his collision box detects.
			if (detecionDirection == CollisionDirection.Up
					&& collidingObjectHitbox.get(i).getBoundsInParent().intersects(hitboxUp.getBoundsInParent())) {
				moveTo(body.getTranslateX(),
						collidingObjectHitbox.get(i).getBoundsInParent().getMaxY() - hitboxUp.OFFSET_Y);
				return true;
			}
			if (detecionDirection == CollisionDirection.Down
					&& collidingObjectHitbox.get(i).getBoundsInParent().intersects(hitboxDown.getBoundsInParent())) {
				moveTo(body.getTranslateX(),
						collidingObjectHitbox.get(i).getBoundsInParent().getMinY() - hitboxDown.OFFSET_Y - 1);
				return true;
			}
			if (detecionDirection == CollisionDirection.Left
					&& collidingObjectHitbox.get(i).getBoundsInParent().intersects(hitboxLeft.getBoundsInParent())) {
				moveTo(collidingObjectHitbox.get(i).getBoundsInParent().getMaxX() - hitboxLeft.OFFSET_X,
						body.getTranslateY());
				return true;
			}
			if (detecionDirection == CollisionDirection.Right
					&& collidingObjectHitbox.get(i).getBoundsInParent().intersects(hitboxRight.getBoundsInParent())) {
				moveTo(collidingObjectHitbox.get(i).getBoundsInParent().getMinX() - hitboxRight.OFFSET_X - 1,
						body.getTranslateY());
				return true;
			}
		}
		return false;
	}

	/**
	 * @param x Relative to the x-coordinate of the player's body.
	 * @param y Relative to the y-coordinate of the player's body.
	 */
	public void addHitboxUp(double x, double y, double width, double height) {
		hitboxUp = new ObjectBox(x, y, width, height);
		hitboxUp.moveTo(body.getTranslateX(), body.getTranslateY());
	}

	/**
	 * @param x Relative to the x-coordinate of the player's body.
	 * @param y Relative to the y-coordinate of the player's body.
	 */
	public void addHitboxDown(double x, double y, double width, double height) {
		hitboxDown = new ObjectBox(x, y, width, height);
		hitboxDown.moveTo(body.getTranslateX(), body.getTranslateY());
	}

	/**
	 * @param x Relative to the x-coordinate of the player's body.
	 * @param y Relative to the y-coordinate of the player's body.
	 */
	public void addHitboxLeft(double x, double y, double width, double height) {
		hitboxLeft = new ObjectBox(x, y, width, height);
		hitboxLeft.moveTo(body.getTranslateX(), body.getTranslateY());
	}

	/**
	 * @param x Relative to the x-coordinate of the player's body.
	 * @param y Relative to the y-coordinate of the player's body.
	 */
	public void addHitboxRight(double x, double y, double width, double height) {
		hitboxRight = new ObjectBox(x, y, width, height);
		hitboxRight.moveTo(body.getTranslateX(), body.getTranslateY());
	}

	public void addInteractionTriggerBox(double x, double y, double width, double height) {
		interactionTriggerBox = new ObjectBox(x, y, width, height);
		interactionTriggerBox.setFill(Color.BLUE);
		interactionTriggerBox.moveTo(body.getTranslateX(), body.getTranslateY());
	}

	public ObjectBox getInteractionTriggerBox() {
		return interactionTriggerBox;
	}

	/**
	 * Ends the program if the Player object is missing an important object. (Which
	 * object misses will be printed out.)
	 * Should be called before the main game loop.
	 */
	public void checkPlayerBeforeStart() {
		if (hitboxUp == null) {
			System.err.println("Player object is missing a hitboxUp object!");
			System.exit(0);
		}
		if (hitboxDown == null) {
			System.err.println("Player object is missing a hitboxDown object!");
			System.exit(0);
		}
		if (hitboxLeft == null) {
			System.err.println("Player object is missing a hitboxLeft object!");
			System.exit(0);
		}
		if (hitboxRight == null) {
			System.err.println("Player object is missing a hitboxRight object!");
			System.exit(0);
		}
		if (interactionTriggerBox == null) {
			System.err.println("Player object is missing a interactionTriggerBox object!");
			System.exit(0);
		}
	}
}

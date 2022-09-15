package testing;

import java.io.IOException;

import javafx.scene.shape.Rectangle;

public class Player extends MapObject {

	public ObjectBox hitboxUp, hitboxDown, hitboxLeft, hitboxRight;

	public enum CollisionDirection {
		Up, Down, Left, Right;
	}

	public Player(double x, double y, double width, double height, Animator animator) throws IOException {
		super(x, y, width, height, animator, "IDLE_DOWN");
	}

	@Override
	public void move(double x, double y) {
		super.move(x, y);
		alignHitbox();
	}

	@Override
	public void moveTo(double x, double y) {
		super.moveTo(x, y);
		if (hitboxUp == null || hitboxDown == null || hitboxLeft == null || hitboxRight == null) {
			return;
		}
		setCenter(
				hitboxLeft.getBoundsInParent().getMinX()
						+ (hitboxRight.getBoundsInParent().getMaxX() - hitboxLeft.getBoundsInParent().getMinX()) / 2,
				hitboxUp.getBoundsInParent().getMinY()
						+ (hitboxDown.getBoundsInParent().getMaxY() - hitboxUp.getBoundsInParent().getMinY()) / 2);
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

//	public boolean isColliding(ObjectBox collidingObjectHitbox, CollisionDirection detecionDirection) {
//		if (detecionDirection == CollisionDirection.Up
//				&& collidingObjectHitbox.getBoundsInParent().intersects(hitboxUp.getBoundsInParent())) {
//			moveTo(body.getTranslateX(), collidingObjectHitbox.getBoundsInParent().getMaxY() - hitboxUp.OFFSET_Y);
//			return true;
//		}
//		if (detecionDirection == CollisionDirection.Down
//				&& collidingObjectHitbox.getBoundsInParent().intersects(hitboxDown.getBoundsInParent())) {
//			moveTo(body.getTranslateX(), collidingObjectHitbox.getBoundsInParent().getMinY() - hitboxDown.OFFSET_Y - 1);
//			return true;
//		}
//		if (detecionDirection == CollisionDirection.Left
//				&& collidingObjectHitbox.getBoundsInParent().intersects(hitboxLeft.getBoundsInParent())) {
//			moveTo(collidingObjectHitbox.getBoundsInParent().getMaxX() - hitboxLeft.OFFSET_X, body.getTranslateY());
//			return true;
//		}
//		if (detecionDirection == CollisionDirection.Right
//				&& collidingObjectHitbox.getBoundsInParent().intersects(hitboxRight.getBoundsInParent())) {
//			moveTo(collidingObjectHitbox.getBoundsInParent().getMinX() - hitboxRight.OFFSET_X - 1,
//					body.getTranslateY());
//			return true;
//		}
//		return false;
//	}
	
	public boolean isColliding(Rectangle collidingObjectHitbox, CollisionDirection detecionDirection) {
		if (detecionDirection == CollisionDirection.Up
				&& collidingObjectHitbox.getBoundsInParent().intersects(hitboxUp.getBoundsInParent())) {
			moveTo(body.getTranslateX(), collidingObjectHitbox.getBoundsInParent().getMaxY() - hitboxUp.OFFSET_Y);
			return true;
		}
		if (detecionDirection == CollisionDirection.Down
				&& collidingObjectHitbox.getBoundsInParent().intersects(hitboxDown.getBoundsInParent())) {
			moveTo(body.getTranslateX(), collidingObjectHitbox.getBoundsInParent().getMinY() - hitboxDown.OFFSET_Y - 1);
			return true;
		}
		if (detecionDirection == CollisionDirection.Left
				&& collidingObjectHitbox.getBoundsInParent().intersects(hitboxLeft.getBoundsInParent())) {
			moveTo(collidingObjectHitbox.getBoundsInParent().getMaxX() - hitboxLeft.OFFSET_X, body.getTranslateY());
			return true;
		}
		if (detecionDirection == CollisionDirection.Right
				&& collidingObjectHitbox.getBoundsInParent().intersects(hitboxRight.getBoundsInParent())) {
			moveTo(collidingObjectHitbox.getBoundsInParent().getMinX() - hitboxRight.OFFSET_X - 1,
					body.getTranslateY());
			return true;
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
	
	public ObjectBox getHitboxUp() {
		return hitboxUp;
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
	
	/**
	 * Ends the program if the Player object is missing an important object. (Which
	 * object misses will be printed out.) Should be called before the main game
	 * loop.
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
		if (center == null) {
			System.err.println("Player object is missing a center object!");
			System.exit(0);
		}
	}

	public void configureCenter() {
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
		setCenter(
				hitboxLeft.getBoundsInParent().getMinX()
						+ (hitboxRight.getBoundsInParent().getMaxX() - hitboxLeft.getBoundsInParent().getMinX()) / 2,
				hitboxUp.getBoundsInParent().getMinY()
						+ (hitboxDown.getBoundsInParent().getMaxY() - hitboxUp.getBoundsInParent().getMinY()) / 2);
	}
}

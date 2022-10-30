package testing;

import finished.List;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;

public class Player {

	private Visualizer visualizer;
	private Rectangle collisionbox;
	private Point2D centerPoint;
	private double hitboxOffsetX, hitboxOffsetY;
	private final int PLAYER_SPEED = 4;
	private int frameAnimationCounter = 0;

	public Player(double x, double y, Visualizer visualizer, Rectangle collisionbox, Point2D centerPoint) {
		this.visualizer = visualizer;
		this.collisionbox = collisionbox;
		this.centerPoint = centerPoint;
		hitboxOffsetX = collisionbox.getTranslateX();
		hitboxOffsetY = collisionbox.getTranslateY();
		moveFromCenterTo(x, y);
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

	public void moveFromCenterTo(double x, double y) {
		visualizer.getImageView().setTranslateX(x - centerPoint.getX());
		visualizer.getImageView().setTranslateY(y - centerPoint.getY());
		collisionbox.setTranslateX(hitboxOffsetX + x - centerPoint.getX());
		collisionbox.setTranslateY(hitboxOffsetY + y - centerPoint.getY());
	}

	public Visualizer getVisualizer() {
		return visualizer;
	}

	public Rectangle getCollisionBox() {
		return collisionbox;
	}

	public Point2D getCenterPoint() {
		return centerPoint;
	}

	public void tick(String[] controlls, boolean[] controllsPressed, List<Obstacle> obstacles, Map map, Scene scene,
			Group root) {
		boolean up = false, down = false, left = false, right = false, hasMoved = false;
		for (int i = 0; i < controlls.length; i++) {
			if (controlls[i].split("->")[1].equals("UP")) {
				up = controllsPressed[i];
				continue;
			}
			if (controlls[i].split("->")[1].equals("DOWN")) {
				down = controllsPressed[i];
				continue;
			}
			if (controlls[i].split("->")[1].equals("LEFT")) {
				left = controllsPressed[i];
				continue;
			}
			if (controlls[i].split("->")[1].equals("RIGHT")) {
				right = controllsPressed[i];
				continue;
			}
		}
//		0. ??
//		1. movement
		hasMoved = movement(up, down, left, right, obstacles, map, scene, root);
//		2. animation
		animation(up, down, left, right, hasMoved);
//		3. interaction
//		4. ??
	}

	private void animation(boolean up, boolean down, boolean left, boolean right, boolean hasMoved) {
		if (!visualizer.isAnimated()) {
			return;
		}
		frameAnimationCounter++;
		if (hasMoved) {
			if (up && (!(down || left || right) || (!down && left && right))
					&& !visualizer.getCurrentAnimationName().equals("MOVE_UP")) {
				visualizer.setCurrentAnimation("MOVE_UP");
				frameAnimationCounter = 0;
			}
			if (down && (!(up || left || right) || (!up && left && right))
					&& !visualizer.getCurrentAnimationName().equals("MOVE_DOWN")) {
				visualizer.setCurrentAnimation("MOVE_DOWN");
				frameAnimationCounter = 0;
			}
			if (left && !right && !visualizer.getCurrentAnimationName().equals("MOVE_LEFT")) {
				visualizer.setCurrentAnimation("MOVE_LEFT");
				frameAnimationCounter = 0;
			}
			if (right && !left && !visualizer.getCurrentAnimationName().equals("MOVE_RIGHT")) {
				visualizer.setCurrentAnimation("MOVE_RIGHT");
				frameAnimationCounter = 0;
			}
			if (frameAnimationCounter - visualizer.getCurrentFrameSpeed() == 0) {
				visualizer.nextAnimationFrame();
				frameAnimationCounter = 0;
			}
			return;
		}
		if (!visualizer.getCurrentAnimationName()
				.equals("IDLE_" + visualizer.getCurrentAnimationName().split("_")[1])) {
			visualizer.setCurrentAnimation("IDLE_" + visualizer.getCurrentAnimationName().split("_")[1]);
			frameAnimationCounter = 0;
		}
		if (frameAnimationCounter - visualizer.getCurrentFrameSpeed() == 0) {
			visualizer.nextAnimationFrame();
			frameAnimationCounter = 0;
		}
	}

	private boolean movement(boolean up, boolean down, boolean left, boolean right, List<Obstacle> obstacles, Map map,
			Scene scene, Group root) {
//		cases of no movement
		if ((up && down && left && right) || (up && down && !left && !right) || (!up && !down && left && right)) {
			return false;
		}
//		collision check and proper positioning
		for (int i = 0; i < obstacles.length(); i++) {
			if (up && !down && left && !right) {
				if (collisionbox.getTranslateX() + collisionbox.getWidth() >= obstacles.get(i).getCollisionBox()
						.getTranslateX()
						&& collisionbox.getTranslateX() <= obstacles.get(i).getCollisionBox().getTranslateX()
								+ obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() - PLAYER_SPEED >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& collisionbox.getTranslateY() - PLAYER_SPEED <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(collisionbox.getTranslateX(),
							obstacles.get(i).getCollisionBox().getTranslateY()
									+ obstacles.get(i).getCollisionBox().getHeight() + 1);
					up = false;
				}
				if (collisionbox.getTranslateX() - PLAYER_SPEED >= obstacles.get(i).getCollisionBox().getTranslateX()
						&& collisionbox.getTranslateX() - PLAYER_SPEED <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& collisionbox.getTranslateY() <= obstacles.get(i).getCollisionBox().getTranslateY()
								+ obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(obstacles.get(i).getCollisionBox().getTranslateX()
							+ obstacles.get(i).getCollisionBox().getWidth() + 1, collisionbox.getTranslateY());
					left = false;
				}
				continue;
			}
			if (up && !down && right && !left) {
				if (collisionbox.getTranslateX() + collisionbox.getWidth() >= obstacles.get(i).getCollisionBox()
						.getTranslateX()
						&& collisionbox.getTranslateX() <= obstacles.get(i).getCollisionBox().getTranslateX()
								+ obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() - PLAYER_SPEED >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& collisionbox.getTranslateY() - PLAYER_SPEED <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(collisionbox.getTranslateX(),
							obstacles.get(i).getCollisionBox().getTranslateY()
									+ obstacles.get(i).getCollisionBox().getHeight() + 1);
					up = false;
				}
				if (collisionbox.getTranslateX() + collisionbox.getWidth() + PLAYER_SPEED >= obstacles.get(i)
						.getCollisionBox().getTranslateX()
						&& collisionbox.getTranslateX() + collisionbox.getWidth() + PLAYER_SPEED <= obstacles.get(i)
								.getCollisionBox().getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& collisionbox.getTranslateY() <= obstacles.get(i).getCollisionBox().getTranslateY()
								+ obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(
							obstacles.get(i).getCollisionBox().getTranslateX() - collisionbox.getWidth() - 1,
							collisionbox.getTranslateY());
					right = false;
				}
				continue;
			}
			if (down && !up && left && !right) {
				if (collisionbox.getTranslateX() + collisionbox.getWidth() >= obstacles.get(i).getCollisionBox()
						.getTranslateX()
						&& collisionbox.getTranslateX() <= obstacles.get(i).getCollisionBox().getTranslateX()
								+ obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() + PLAYER_SPEED >= obstacles.get(i)
								.getCollisionBox().getTranslateY()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() + PLAYER_SPEED <= obstacles.get(i)
								.getCollisionBox().getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(collisionbox.getTranslateX(),
							obstacles.get(i).getCollisionBox().getTranslateY() - collisionbox.getHeight() - 1);
					down = false;
				}
				if (collisionbox.getTranslateX() - PLAYER_SPEED >= obstacles.get(i).getCollisionBox().getTranslateX()
						&& collisionbox.getTranslateX() - PLAYER_SPEED <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& collisionbox.getTranslateY() <= obstacles.get(i).getCollisionBox().getTranslateY()
								+ obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(obstacles.get(i).getCollisionBox().getTranslateX()
							+ obstacles.get(i).getCollisionBox().getWidth() + 1, collisionbox.getTranslateY());
					left = false;
				}
				continue;
			}
			if (down && !up && right && !left) {
				if (collisionbox.getTranslateX() + collisionbox.getWidth() >= obstacles.get(i).getCollisionBox()
						.getTranslateX()
						&& collisionbox.getTranslateX() <= obstacles.get(i).getCollisionBox().getTranslateX()
								+ obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() + PLAYER_SPEED >= obstacles.get(i)
								.getCollisionBox().getTranslateY()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() + PLAYER_SPEED <= obstacles.get(i)
								.getCollisionBox().getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(collisionbox.getTranslateX(),
							obstacles.get(i).getCollisionBox().getTranslateY() - collisionbox.getHeight() - 1);
					down = false;
				}
				if (collisionbox.getTranslateX() + collisionbox.getWidth() + PLAYER_SPEED >= obstacles.get(i)
						.getCollisionBox().getTranslateX()
						&& collisionbox.getTranslateX() + collisionbox.getWidth() + PLAYER_SPEED <= obstacles.get(i)
								.getCollisionBox().getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& collisionbox.getTranslateY() <= obstacles.get(i).getCollisionBox().getTranslateY()
								+ obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(
							obstacles.get(i).getCollisionBox().getTranslateX() - collisionbox.getWidth() - 1,
							collisionbox.getTranslateY());
					right = false;
				}
				continue;
			}
			if (up && !down && (left || right || (!(left || right) || (left && right)))) {
				if (collisionbox.getTranslateX() + collisionbox.getWidth() >= obstacles.get(i).getCollisionBox()
						.getTranslateX()
						&& collisionbox.getTranslateX() <= obstacles.get(i).getCollisionBox().getTranslateX()
								+ obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() - PLAYER_SPEED >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& collisionbox.getTranslateY() - PLAYER_SPEED <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(collisionbox.getTranslateX(),
							obstacles.get(i).getCollisionBox().getTranslateY()
									+ obstacles.get(i).getCollisionBox().getHeight() + 1);
					up = false;
				}
				continue;
			}
			if (down && !up && (left || right || (!(left || right) || (left && right)))) {
				if (collisionbox.getTranslateX() + collisionbox.getWidth() >= obstacles.get(i).getCollisionBox()
						.getTranslateX()
						&& collisionbox.getTranslateX() <= obstacles.get(i).getCollisionBox().getTranslateX()
								+ obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() + PLAYER_SPEED >= obstacles.get(i)
								.getCollisionBox().getTranslateY()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() + PLAYER_SPEED <= obstacles.get(i)
								.getCollisionBox().getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(collisionbox.getTranslateX(),
							obstacles.get(i).getCollisionBox().getTranslateY() - collisionbox.getHeight() - 1);
					down = false;
				}
				continue;
			}
			if (left && !right && (up || down || (!(up || down) || (up && down)))) {
				if (collisionbox.getTranslateX() - PLAYER_SPEED >= obstacles.get(i).getCollisionBox().getTranslateX()
						&& collisionbox.getTranslateX() - PLAYER_SPEED <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& collisionbox.getTranslateY() <= obstacles.get(i).getCollisionBox().getTranslateY()
								+ obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(obstacles.get(i).getCollisionBox().getTranslateX()
							+ obstacles.get(i).getCollisionBox().getWidth() + 1, collisionbox.getTranslateY());
					left = false;
				}
				continue;
			}
			if (right && !left && (up || down || (!(up || down) || (up && down)))) {
				if (collisionbox.getTranslateX() + collisionbox.getWidth() + PLAYER_SPEED >= obstacles.get(i)
						.getCollisionBox().getTranslateX()
						&& collisionbox.getTranslateX() + collisionbox.getWidth() + PLAYER_SPEED <= obstacles.get(i)
								.getCollisionBox().getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& collisionbox.getTranslateY() + collisionbox.getHeight() >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& collisionbox.getTranslateY() <= obstacles.get(i).getCollisionBox().getTranslateY()
								+ obstacles.get(i).getCollisionBox().getHeight()) {
					moveFromCollisionBoxTo(
							obstacles.get(i).getCollisionBox().getTranslateX() - collisionbox.getWidth() - 1,
							collisionbox.getTranslateY());
					right = false;
				}
				continue;
			}
		}
//		normal movement
		if (up && !down && left && !right) {
			move((int) (-Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) - 1),
					(int) (-Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) - 1));
			if (0 > root.getTranslateX()
					&& getX() + centerPoint.getX() < map.getCurrentField().WIDTH - scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() + (int) (Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) + 1));
				if (root.getTranslateX() > 0) {
					root.setTranslateX(0);
				}
			}
			if (0 > root.getTranslateY()
					&& getY() + centerPoint.getY() < map.getCurrentField().HEIGHT - scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() + (int) (Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) + 1));
				if (root.getTranslateY() > 0) {
					root.setTranslateY(0);
				}
			}
			return true;
		}
		if (up && !down && right && !left) {
			move((int) (Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) + 1),
					(int) (-Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) - 1));
			if (map.getCurrentField().WIDTH - scene.getWidth() > -root.getTranslateX()
					&& getX() + centerPoint.getX() > scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() + (int) (-Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) - 1));
				if (root.getTranslateX() < -map.getCurrentField().WIDTH + scene.getWidth()) {
					root.setTranslateX(-map.getCurrentField().WIDTH + scene.getWidth());
				}
			}
			if (0 > root.getTranslateY()
					&& getY() + centerPoint.getY() < map.getCurrentField().HEIGHT - scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() + (int) (Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) + 1));
				if (root.getTranslateY() > 0) {
					root.setTranslateY(0);
				}
			}
			return true;
		}
		if (down && !up && left && !right) {
			move((int) (-Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) - 1),
					(int) (Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) + 1));
			if (0 > root.getTranslateX()
					&& getX() + centerPoint.getX() < map.getCurrentField().WIDTH - scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() + (int) (Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) + 1));
				if (root.getTranslateX() > 0) {
					root.setTranslateX(0);
				}
			}
			if (map.getCurrentField().HEIGHT - scene.getHeight() > -root.getTranslateY()
					&& getY() + centerPoint.getY() > scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() + (int) (-Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) - 1));
				if (root.getTranslateY() < -map.getCurrentField().HEIGHT + scene.getHeight()) {
					root.setTranslateY(-map.getCurrentField().HEIGHT + scene.getHeight());
				}
			}
			return true;
		}
		if (down && !up && right && !left) {
			move((int) (Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) + 1),
					(int) (Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) + 1));
			if (map.getCurrentField().WIDTH - scene.getWidth() > -root.getTranslateX()
					&& getX() + centerPoint.getX() > scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() + (int) (-Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) - 1));
				if (root.getTranslateX() < -map.getCurrentField().WIDTH + scene.getWidth()) {
					root.setTranslateX(-map.getCurrentField().WIDTH + scene.getWidth());
				}
			}
			if (map.getCurrentField().HEIGHT - scene.getHeight() > -root.getTranslateY()
					&& getY() + centerPoint.getY() > scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() + (int) (-Math.sqrt(Math.pow(PLAYER_SPEED, 2) / 2) - 1));
				if (root.getTranslateY() < -map.getCurrentField().HEIGHT + scene.getHeight()) {
					root.setTranslateY(-map.getCurrentField().HEIGHT + scene.getHeight());
				}
			}
			return true;
		}
		if (up && !down && (left || right || (!(left || right) || (left && right)))) {
			move(0, -PLAYER_SPEED);
			if (0 > root.getTranslateY()
					&& getY() + centerPoint.getY() < map.getCurrentField().HEIGHT - scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() + PLAYER_SPEED);
				if (root.getTranslateY() > 0) {
					root.setTranslateY(0);
				}
			}
			return true;
		}
		if (down && !up && (left || right || (!(left || right) || (left && right)))) {
			move(0, PLAYER_SPEED);
			if (map.getCurrentField().HEIGHT - scene.getHeight() > -root.getTranslateY()
					&& getY() + centerPoint.getY() > scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() - PLAYER_SPEED);
				if (root.getTranslateY() < -map.getCurrentField().HEIGHT + scene.getHeight()) {
					root.setTranslateY(-map.getCurrentField().HEIGHT + scene.getHeight());
				}
			}
			return true;
		}
		if (left && !right && (up || down || (!(up || down) || (up && down)))) {
			move(-PLAYER_SPEED, 0);
			if (0 > root.getTranslateX()
					&& getX() + centerPoint.getX() < map.getCurrentField().WIDTH - scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() + PLAYER_SPEED);
				if (root.getTranslateX() > 0) {
					root.setTranslateX(0);
				}
			}
			return true;
		}
		if (right && !left && (up || down || (!(up || down) || (up && down)))) {
			move(PLAYER_SPEED, 0);
			if (map.getCurrentField().WIDTH - scene.getWidth() > -root.getTranslateX()
					&& getX() + centerPoint.getX() > scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() - PLAYER_SPEED);
				if (root.getTranslateX() < -map.getCurrentField().WIDTH + scene.getWidth()) {
					root.setTranslateX(-map.getCurrentField().WIDTH + scene.getWidth());
				}
			}
			return true;
		}
		return false;
	}

}
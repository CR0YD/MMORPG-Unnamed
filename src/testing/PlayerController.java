package testing;

import finished.List;
import javafx.scene.Group;
import javafx.scene.Scene;
import testing.Player.CollisionDirection;

public class PlayerController {

	private boolean up, down, left, right, collisionUp, collisionDown, collisionLeft, collisionRight = false;
	private double playerX, playerY, playerSpeed;
	private boolean wantsToInteract;

	private Player player;
	private PlayerFacingDirection currentPlayerDirection;

	private enum PlayerFacingDirection {
		UP, DOWN, LEFT, RIGHT;
	}

	public PlayerController(Player player, double playerSpeed) {
		this.player = player;
		this.playerSpeed = playerSpeed;
		playerX = this.player.getX();
		playerY = this.player.getY();
		currentPlayerDirection = PlayerFacingDirection.DOWN;
	}

	public void playerWantsToInteract() {
		wantsToInteract = true;
	}

	public void setPlayerInputUp(boolean up) {
		this.up = up;
	}

	public void setPlayerInputDown(boolean down) {
		this.down = down;
	}

	public void setPlayerInputLeft(boolean left) {
		this.left = left;
	}

	public void setPlayerInputRight(boolean right) {
		this.right = right;
	}

	public void setAllPlayerInputs(boolean input) {
		up = input;
		down = input;
		left = input;
		right = input;
	}

	public void updatePlayerPosition(Map map, Scene scene, Group root) {
		if (up && !down && left && !right && !collisionUp && !collisionLeft) {
			player.move((int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1),
					(int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
			if (0 > root.getTranslateX() && player.getX() < map.getCurrentField().WIDTH - scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() + (int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
			}
			if (0 > root.getTranslateY() && player.getY() < map.getCurrentField().HEIGHT - scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() + (int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
			}
		}
		if (up && !down && right && !left && !collisionUp && !collisionRight) {
			player.move((int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1),
					(int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
			if (map.getCurrentField().WIDTH - scene.getWidth() > -root.getTranslateX() && player.getX() > scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() + (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
			}
			if (0 > root.getTranslateY() && player.getY() < map.getCurrentField().HEIGHT - scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() + (int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
			}
		}
		if (down && !up && left && !right && !collisionDown && !collisionLeft) {
			player.move((int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1),
					(int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
			if (0 > root.getTranslateX() && player.getX() < map.getCurrentField().WIDTH - scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() + (int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
			}
			if (map.getCurrentField().HEIGHT - scene.getHeight() > -root.getTranslateY() && player.getY() > scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() + (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
			}
		}
		if (down && !up && right && !left && !collisionDown && !collisionRight) {
			player.move((int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1),
					(int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
			if (map.getCurrentField().WIDTH - scene.getWidth() > -root.getTranslateX() && player.getX() > scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() + (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
			}
			if (map.getCurrentField().HEIGHT - scene.getHeight() > -root.getTranslateY() && player.getY() > scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() + (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
			}
		}
		if (up && !down
				&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
				&& !collisionUp) {
			player.move(0, -playerSpeed);
			if (0 > root.getTranslateY() && player.getY() < map.getCurrentField().HEIGHT - scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() + playerSpeed);
			}
		}
		if (down && !up
				&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
				&& !collisionDown) {
			player.move(0, playerSpeed);
			if (map.getCurrentField().HEIGHT - scene.getHeight() > -root.getTranslateY() && player.getY() > scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() - playerSpeed);
			}
		}
		if (left && !right && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
				&& !collisionLeft) {
			player.move(-playerSpeed, 0);
			if (0 > root.getTranslateX() && player.getX() < map.getCurrentField().WIDTH - scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() + playerSpeed);
			}
		}
		if (right && !left && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
				&& !collisionRight) {
			player.move(playerSpeed, 0);
			if (map.getCurrentField().WIDTH - scene.getWidth() > -root.getTranslateX() && player.getX() > scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() - playerSpeed);
			}
		}
		collisionUp = false;
		collisionDown = false;
		collisionLeft = false;
		collisionRight = false;
	}

	public void checkPlayerStoppedMoving() {
		if (player.getX() == playerX && player.getY() == playerY
				&& (player.getCurrentFrameIdx() == 1 || player.getCurrentFrameIdx() == 3)
				&& ((!up && !down && !left && !right) || ((up && down) && !(left || right))
						|| ((left && right) && !(up || down)))) {
			if (currentPlayerDirection == PlayerFacingDirection.UP
					&& !player.getCurrentAnimationName().equals("IDLE_UP")) {
				player.changeAnimationTo("IDLE_UP");
			}
			if (currentPlayerDirection == PlayerFacingDirection.DOWN
					&& !player.getCurrentAnimationName().equals("IDLE_DOWN")) {
				player.changeAnimationTo("IDLE_DOWN");
			}
			if (currentPlayerDirection == PlayerFacingDirection.LEFT
					&& !player.getCurrentAnimationName().equals("IDLE_LEFT")) {
				player.changeAnimationTo("IDLE_LEFT");
			}
			if (currentPlayerDirection == PlayerFacingDirection.RIGHT
					&& !player.getCurrentAnimationName().equals("IDLE_RIGHT")) {
				player.changeAnimationTo("IDLE_RIGHT");
			}
		}
		playerX = player.getX();
		playerY = player.getY();
	}

//	public void checkCollision(List<MapObstacle> obstacles) {
//		for (int i = 0; i < obstacles.length(); i++) {
//			if (!obstacles.get(i).hasCollision()) {
//				continue;
//			}
//			if (up && !down && left && !right && !collisionUp && !collisionLeft) {
//				player.moveHitbox(0, (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Up)) {
//					collisionUp = true;
//				}
//				player.alignHitbox();
//				player.moveHitbox((int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1), 0);
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Left)) {
//					collisionLeft = true;
//				}
//				player.alignHitbox();
//			}
//			if (up && !down && right && !left && !collisionUp && !collisionRight) {
//				player.moveHitbox(0, (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Up)) {
//					collisionUp = true;
//				}
//				player.alignHitbox();
//				player.moveHitbox((int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1), 0);
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Right)) {
//					collisionRight = true;
//				}
//				player.alignHitbox();
//			}
//			if (down && !up && left && !right && !collisionDown && !collisionLeft) {
//				player.moveHitbox(0, (int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Down)) {
//					collisionDown = true;
//				}
//				player.alignHitbox();
//				player.moveHitbox((int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1), 0);
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Left)) {
//					collisionLeft = true;
//				}
//				player.alignHitbox();
//			}
//			if (down && !up && right && !left && !collisionDown && !collisionRight) {
//				player.moveHitbox(0, (int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Down)) {
//					collisionDown = true;
//				}
//				player.alignHitbox();
//				player.moveHitbox((int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1), 0);
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Right)) {
//					collisionRight = true;
//				}
//				player.alignHitbox();
//			}
//			if (up && !down
//					&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
//					&& !collisionUp) {
//				player.moveHitbox(0, -playerSpeed);
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Up)) {
//					collisionUp = true;
//				}
//				player.alignHitbox();
//			}
//			if (down && !up
//					&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
//					&& !collisionDown) {
//				player.moveHitbox(0, playerSpeed);
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Down)) {
//					collisionDown = true;
//				}
//				player.alignHitbox();
//			}
//			if (left && !right && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
//					&& !collisionLeft) {
//				player.moveHitbox(-playerSpeed, 0);
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Left)) {
//					collisionLeft = true;
//				}
//				player.alignHitbox();
//			}
//			if (right && !left && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
//					&& !collisionRight) {
//				player.moveHitbox(playerSpeed, 0);
//				if (player.isColliding(obstacles.get(i).getHitbox(), CollisionDirection.Right)) {
//					collisionRight = true;
//				}
//				player.alignHitbox();
//			}
//		}
//	}
	
	public void checkCollisionWithObstacle(List<Obstacle> obstacles) {
		for (int i = 0; i < obstacles.length(); i++) {
			if (up && !down && left && !right && !collisionUp && !collisionLeft) {
				player.moveHitbox(0, (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Up)) {
					collisionUp = true;
				}
				player.alignHitbox();
				player.moveHitbox((int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1), 0);
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Left)) {
					collisionLeft = true;
				}
				player.alignHitbox();
			}
			if (up && !down && right && !left && !collisionUp && !collisionRight) {
				player.moveHitbox(0, (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Up)) {
					collisionUp = true;
				}
				player.alignHitbox();
				player.moveHitbox((int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1), 0);
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Right)) {
					collisionRight = true;
				}
				player.alignHitbox();
			}
			if (down && !up && left && !right && !collisionDown && !collisionLeft) {
				player.moveHitbox(0, (int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Down)) {
					collisionDown = true;
				}
				player.alignHitbox();
				player.moveHitbox((int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1), 0);
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Left)) {
					collisionLeft = true;
				}
				player.alignHitbox();
			}
			if (down && !up && right && !left && !collisionDown && !collisionRight) {
				player.moveHitbox(0, (int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Down)) {
					collisionDown = true;
				}
				player.alignHitbox();
				player.moveHitbox((int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1), 0);
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Right)) {
					collisionRight = true;
				}
				player.alignHitbox();
			}
			if (up && !down
					&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
					&& !collisionUp) {
				player.moveHitbox(0, -playerSpeed);
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Up)) {
					collisionUp = true;
				}
				player.alignHitbox();
			}
			if (down && !up
					&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
					&& !collisionDown) {
				player.moveHitbox(0, playerSpeed);
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Down)) {
					collisionDown = true;
				}
				player.alignHitbox();
			}
			if (left && !right && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
					&& !collisionLeft) {
				player.moveHitbox(-playerSpeed, 0);
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Left)) {
					collisionLeft = true;
				}
				player.alignHitbox();
			}
			if (right && !left && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
					&& !collisionRight) {
				player.moveHitbox(playerSpeed, 0);
				if (player.isColliding(obstacles.get(i).getCollisionBox(), CollisionDirection.Right)) {
					collisionRight = true;
				}
				player.alignHitbox();
			}
		}
	}

	public void checkInteraction(List<MapObstacle> tiles) {
		if (!wantsToInteract) {
			return;
		}
		for (int i = 0; i < tiles.length(); i++) {
			if (!tiles.get(i).isInteractable()) {
				continue;
			}
			if (player.getCenter().getX()
					- tiles.get(i).getCenter().getX() >= -tiles.get(i).getMinInteractionDistanceX()
					&& player.getCenter().getX() - tiles.get(i).getCenter().getX() <= tiles.get(i)
							.getMaxInteractionDistanceX()
					&& player.getCenter().getY()
							- tiles.get(i).getCenter().getY() >= -tiles.get(i).getMinInteractionDistanceY()
					&& player.getCenter().getY() - tiles.get(i).getCenter().getY() <= tiles.get(i)
							.getMaxInteractionDistanceY()) {
				tiles.get(i).onInteraction();
				wantsToInteract = false;
				return;
			}
		}
		wantsToInteract = false;
	}

	public void checkPlayerAnimation() {
		if (up && (!(down || left || right) || (!down && left && right))
				&& !player.getCurrentAnimationName().equals("MOVE_UP")) {
			player.changeAnimationTo("MOVE_UP");
			currentPlayerDirection = PlayerFacingDirection.UP;
		}
		if (down && (!(up || left || right) || (!up && left && right))
				&& !player.getCurrentAnimationName().equals("MOVE_DOWN")) {
			player.changeAnimationTo("MOVE_DOWN");
			currentPlayerDirection = PlayerFacingDirection.DOWN;
		}
		if (left && !right && !player.getCurrentAnimationName().equals("MOVE_LEFT")) {
			player.changeAnimationTo("MOVE_LEFT");
			currentPlayerDirection = PlayerFacingDirection.LEFT;
		}
		if (right && !left && !player.getCurrentAnimationName().equals("MOVE_RIGHT")) {
			player.changeAnimationTo("MOVE_RIGHT");
			currentPlayerDirection = PlayerFacingDirection.RIGHT;
		}
	}

}

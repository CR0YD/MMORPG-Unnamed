package testing;

import testing.Player.CollisionDirection;

public class PlayerController {

	private boolean up, down, left, right, collisionUp, collisionDown, collisionLeft, collisionRight = false;
	private double playerX, playerY, playerSpeed;

	private Player player;
	private PlayerFacingDirection currentPlayerDirection;

	private enum PlayerFacingDirection {
		UP, DOWN, LEFT, RIGHT;
	}

	public PlayerController(Player player, double playerSpeed) {
		this.player = player;
		this.playerSpeed = playerSpeed;
		playerX = this.player.getBody().getTranslateX();
		playerY = this.player.getBody().getTranslateY();
		currentPlayerDirection = PlayerFacingDirection.UP;
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

	public void updatePlayerPosition() {
		if (up && !down && left && !right && !collisionUp && !collisionLeft) {
			player.move((int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1),
					(int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
		}
		if (up && !down && right && !left && !collisionUp && !collisionRight) {
			player.move((int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1),
					(int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
		}
		if (down && !up && left && !right && !collisionDown && !collisionLeft) {
			player.move((int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1),
					(int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
		}
		if (down && !up && right && !left && !collisionDown && !collisionRight) {
			player.move((int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1),
					(int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
		}
		if (up && !down
				&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
				&& !collisionUp) {
			player.move(0, -playerSpeed);
		}
		if (down && !up
				&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
				&& !collisionDown) {
			player.move(0, playerSpeed);
		}
		if (left && !right && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
				&& !collisionLeft) {
			player.move(-playerSpeed, 0);
		}
		if (right && !left && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
				&& !collisionRight) {
			player.move(playerSpeed, 0);
		}
		collisionUp = false;
		collisionDown = false;
		collisionLeft = false;
		collisionRight = false;
	}

	public void checkPlayerStoppedMoving() {
		if (player.getBody().getTranslateX() == playerX && player.getBody().getTranslateY() == playerY
				&& (player.getCurrentFrameIdx() == 1 || player.getCurrentFrameIdx() == 3)
				&& ((!up && !down && !left && !right) || (up && down) || (left && right))) {
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
		playerX = player.getBody().getTranslateX();
		playerY = player.getBody().getTranslateY();
	}

	public void checkCollision(List<WorldObstacle> obstacles) {
		for (int i = 0; i < obstacles.length(); i++) {
			if (!obstacles.get(i).hasCollision()) {
				continue;
			}
			if (up && !down && left && !right && !collisionUp && !collisionLeft) {
				player.moveHitbox(0, (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Up)) {
					collisionUp = true;
				}
				player.alignHitbox();
				player.moveHitbox((int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1), 0);
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Left)) {
					collisionLeft = true;
				}
				player.alignHitbox();
				if (!player.getCurrentAnimationName().equals("MOVE_LEFT")) {
					player.changeAnimationTo("MOVE_LEFT");
					currentPlayerDirection = PlayerFacingDirection.LEFT;
				}
				continue;
			}
			if (up && !down && right && !left && !collisionUp && !collisionRight) {
				player.moveHitbox(0, (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Up)) {
					collisionUp = true;
				}
				player.alignHitbox();
				player.moveHitbox((int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1), 0);
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Right)) {
					collisionRight = true;
				}
				player.alignHitbox();
				if (!player.getCurrentAnimationName().equals("MOVE_RIGHT")) {
					player.changeAnimationTo("MOVE_RIGHT");
					currentPlayerDirection = PlayerFacingDirection.RIGHT;
				}
				continue;
			}
			if (down && !up && left && !right && !collisionDown && !collisionLeft) {
				player.moveHitbox(0, (int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Down)) {
					collisionDown = true;
				}
				player.alignHitbox();
				player.moveHitbox((int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1), 0);
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Left)) {
					collisionLeft = true;
				}
				player.alignHitbox();
				if (!player.getCurrentAnimationName().equals("MOVE_LEFT")) {
					player.changeAnimationTo("MOVE_LEFT");
					currentPlayerDirection = PlayerFacingDirection.LEFT;
				}
				continue;
			}
			if (down && !up && right && !left && !collisionDown && !collisionRight) {
				player.moveHitbox(0, (int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Down)) {
					collisionDown = true;
				}
				player.alignHitbox();
				player.moveHitbox((int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1), 0);
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Right)) {
					collisionRight = true;
				}
				player.alignHitbox();
				if (!player.getCurrentAnimationName().equals("MOVE_RIGHT")) {
					player.changeAnimationTo("MOVE_RIGHT");
					currentPlayerDirection = PlayerFacingDirection.RIGHT;
				}
				continue;
			}
			if (up && !down
					&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
					&& !collisionUp) {
				player.moveHitbox(0, -playerSpeed);
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Up)) {
					collisionUp = true;
				}
				player.alignHitbox();
				if (!player.getCurrentAnimationName().equals("MOVE_UP")) {
					player.changeAnimationTo("MOVE_UP");
					currentPlayerDirection = PlayerFacingDirection.UP;
				}
				continue;
			}
			if (down && !up
					&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
					&& !collisionDown) {
				player.moveHitbox(0, playerSpeed);
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Down)) {
					collisionDown = true;
				}
				player.alignHitbox();
				if (!player.getCurrentAnimationName().equals("MOVE_DOWN")) {
					player.changeAnimationTo("MOVE_DOWN");
					currentPlayerDirection = PlayerFacingDirection.DOWN;
				}
				continue;
			}
			if (left && !right && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
					&& !collisionLeft) {
				player.moveHitbox(-playerSpeed, 0);
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Left)) {
					collisionLeft = true;
				}
				player.alignHitbox();
				if (!player.getCurrentAnimationName().equals("MOVE_LEFT")) {
					player.changeAnimationTo("MOVE_LEFT");
					currentPlayerDirection = PlayerFacingDirection.LEFT;
				}
				continue;
			}
			if (right && !left && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
					&& !collisionRight) {
				player.moveHitbox(playerSpeed, 0);
				if (player.isColliding(obstacles.get(i).getHitboxList(), CollisionDirection.Right)) {
					collisionRight = true;
				}
				player.alignHitbox();
				if (!player.getCurrentAnimationName().equals("MOVE_RIGHT")) {
					player.changeAnimationTo("MOVE_RIGHT");
					currentPlayerDirection = PlayerFacingDirection.RIGHT;
				}
				continue;
			}
		}
	}

}

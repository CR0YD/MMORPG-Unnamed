package testing;

import finished.List;
import javafx.scene.Group;
import javafx.scene.Scene;

public class PlayerController {

	private boolean up, down, left, right, collisionUp, collisionDown, collisionLeft, collisionRight;
	private double playerX, playerY, playerSpeed;
	@SuppressWarnings("unused")
	private boolean wantsToInteract;

	private Player player;
	private PlayerFacingDirection currentPlayerDirection;

	private enum PlayerFacingDirection {
		UP, DOWN, LEFT, RIGHT;
	}

	public PlayerController(Player player, double playerSpeed) {
		this.player = player;
		this.playerSpeed = playerSpeed;
		playerX = this.player.getY();
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
			if (map.getCurrentField().WIDTH - scene.getWidth() > -root.getTranslateX()
					&& player.getX() > scene.getWidth() / 2) {
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
			if (map.getCurrentField().HEIGHT - scene.getHeight() > -root.getTranslateY()
					&& player.getY() > scene.getHeight() / 2) {
				root.setTranslateY(root.getTranslateY() + (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
			}
		}
		if (down && !up && right && !left && !collisionDown && !collisionRight) {
			player.move((int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1),
					(int) (Math.sqrt(Math.pow(playerSpeed, 2) / 2) + 1));
			if (map.getCurrentField().WIDTH - scene.getWidth() > -root.getTranslateX()
					&& player.getX() > scene.getWidth() / 2) {
				root.setTranslateX(root.getTranslateX() + (int) (-Math.sqrt(Math.pow(playerSpeed, 2) / 2) - 1));
			}
			if (map.getCurrentField().HEIGHT - scene.getHeight() > -root.getTranslateY()
					&& player.getY() > scene.getHeight() / 2) {
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
			if (map.getCurrentField().HEIGHT - scene.getHeight() > -root.getTranslateY()
					&& player.getY() > scene.getHeight() / 2) {
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
			if (map.getCurrentField().WIDTH - scene.getWidth() > -root.getTranslateX()
					&& player.getX() > scene.getWidth() / 2) {
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
				&& (player.getVisualizer().getCurrentFrameIdx() == 1
						|| player.getVisualizer().getCurrentFrameIdx() == 3)
				&& ((!up && !down && !left && !right) || ((up && down) && !(left || right))
						|| ((left && right) && !(up || down)))) {
			if (currentPlayerDirection == PlayerFacingDirection.UP
					&& !player.getVisualizer().getCurrentAnimationName().equals("IDLE_UP")) {
				player.getVisualizer().setCurrentAnimation("IDLE_UP");
			}
			if (currentPlayerDirection == PlayerFacingDirection.DOWN
					&& !player.getVisualizer().getCurrentAnimationName().equals("IDLE_DOWN")) {
				player.getVisualizer().setCurrentAnimation("IDLE_DOWN");
			}
			if (currentPlayerDirection == PlayerFacingDirection.LEFT
					&& !player.getVisualizer().getCurrentAnimationName().equals("IDLE_LEFT")) {
				player.getVisualizer().setCurrentAnimation("IDLE_LEFT");
			}
			if (currentPlayerDirection == PlayerFacingDirection.RIGHT
					&& !player.getVisualizer().getCurrentAnimationName().equals("IDLE_RIGHT")) {
				player.getVisualizer().setCurrentAnimation("IDLE_RIGHT");
			}
		}
		playerX = player.getX();
		playerY = player.getY();
	}

	public void checkCollisionWithObstacle(List<Obstacle> obstacles) {
		for (int i = 0; i < obstacles.length(); i++) {
			if (up && !down && left && !right && !collisionUp && !collisionLeft) {
				if (player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth() >= obstacles.get(i)
						.getCollisionBox().getTranslateX()
						&& player.getCollisionBox().getTranslateX() <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() - playerSpeed >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& player.getCollisionBox().getTranslateY() - playerSpeed <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					collisionUp = true;
					player.moveFromCollisionBoxTo(player.getCollisionBox().getTranslateX(), obstacles.get(i).getCollisionBox().getTranslateY()
							+ obstacles.get(i).getCollisionBox().getHeight() + 1);
				}
				if (player.getCollisionBox().getTranslateX() - playerSpeed >= obstacles.get(i).getCollisionBox()
						.getTranslateX()
						&& player.getCollisionBox().getTranslateX() - playerSpeed <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight() >= obstacles
								.get(i).getCollisionBox().getTranslateY()
						&& player.getCollisionBox().getTranslateY() <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					collisionLeft = true;
					player.moveFromCollisionBoxTo(obstacles.get(i).getCollisionBox().getTranslateX()
							+ obstacles.get(i).getCollisionBox().getWidth() + 1, player.getCollisionBox().getTranslateY());
				}
				continue;
			}
			if (up && !down && right && !left && !collisionUp && !collisionRight) {
				if (player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth() >= obstacles.get(i)
						.getCollisionBox().getTranslateX()
						&& player.getCollisionBox().getTranslateX() <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() - playerSpeed >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& player.getCollisionBox().getTranslateY() - playerSpeed <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					collisionUp = true;
					player.moveFromCollisionBoxTo(player.getCollisionBox().getTranslateX(), obstacles.get(i).getCollisionBox().getTranslateY()
							+ obstacles.get(i).getCollisionBox().getHeight() + 1);
				}
				if (player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth()
						+ playerSpeed >= obstacles.get(i).getCollisionBox().getTranslateX()
						&& player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth()
								+ playerSpeed <= obstacles.get(i).getCollisionBox().getTranslateX()
										+ obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight() >= obstacles
								.get(i).getCollisionBox().getTranslateY()
						&& player.getCollisionBox().getTranslateY() <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					collisionRight = true;
					player.moveFromCollisionBoxTo(obstacles.get(i).getCollisionBox().getTranslateX()
							- player.getCollisionBox().getWidth() - 1, player.getCollisionBox().getTranslateY());
				}
				continue;
			}
			if (down && !up && left && !right && !collisionDown && !collisionLeft) {
				if (player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth() >= obstacles.get(i)
						.getCollisionBox().getTranslateX()
						&& player.getCollisionBox().getTranslateX() <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight()
								+ playerSpeed >= obstacles.get(i).getCollisionBox().getTranslateY()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight()
								+ playerSpeed <= obstacles.get(i).getCollisionBox().getTranslateY()
										+ obstacles.get(i).getCollisionBox().getHeight()) {
					collisionDown = true;
					player.moveFromCollisionBoxTo(player.getCollisionBox().getTranslateX(), obstacles.get(i).getCollisionBox().getTranslateY()
							- player.getCollisionBox().getHeight() - 1);
				}
				if (player.getCollisionBox().getTranslateX() - playerSpeed >= obstacles.get(i).getCollisionBox()
						.getTranslateX()
						&& player.getCollisionBox().getTranslateX() - playerSpeed <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight() >= obstacles
								.get(i).getCollisionBox().getTranslateY()
						&& player.getCollisionBox().getTranslateY() <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					collisionLeft = true;
					player.moveFromCollisionBoxTo(obstacles.get(i).getCollisionBox().getTranslateX()
							+ obstacles.get(i).getCollisionBox().getWidth() + 1, player.getCollisionBox().getTranslateY());
				}
				continue;
			}
			if (down && !up && right && !left && !collisionDown && !collisionRight) {
				if (player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth() >= obstacles.get(i)
						.getCollisionBox().getTranslateX()
						&& player.getCollisionBox().getTranslateX() <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight()
								+ playerSpeed >= obstacles.get(i).getCollisionBox().getTranslateY()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight()
								+ playerSpeed <= obstacles.get(i).getCollisionBox().getTranslateY()
										+ obstacles.get(i).getCollisionBox().getHeight()) {
					collisionDown = true;
					player.moveFromCollisionBoxTo(player.getCollisionBox().getTranslateX(), obstacles.get(i).getCollisionBox().getTranslateY()
							- player.getCollisionBox().getHeight() - 1);
				}
				if (player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth()
						+ playerSpeed >= obstacles.get(i).getCollisionBox().getTranslateX()
						&& player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth()
								+ playerSpeed <= obstacles.get(i).getCollisionBox().getTranslateX()
										+ obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight() >= obstacles
								.get(i).getCollisionBox().getTranslateY()
						&& player.getCollisionBox().getTranslateY() <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					collisionRight = true;
					player.moveFromCollisionBoxTo(obstacles.get(i).getCollisionBox().getTranslateX()
							- player.getCollisionBox().getWidth() - 1, player.getCollisionBox().getTranslateY());
				}
				continue;
			}
			if (up && !down
					&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
					&& !collisionUp) {
				if (player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth() >= obstacles.get(i)
						.getCollisionBox().getTranslateX()
						&& player.getCollisionBox().getTranslateX() <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() - playerSpeed >= obstacles.get(i).getCollisionBox()
								.getTranslateY()
						&& player.getCollisionBox().getTranslateY() - playerSpeed <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					collisionUp = true;
					player.moveFromCollisionBoxTo(player.getCollisionBox().getTranslateX(), obstacles.get(i).getCollisionBox().getTranslateY()
							+ obstacles.get(i).getCollisionBox().getHeight() + 1);
				}
				continue;
			}
			if (down && !up
					&& ((left && collisionLeft) || (right && collisionRight) || (!(left || right) || (left && right)))
					&& !collisionDown) {
				if (player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth() >= obstacles.get(i)
						.getCollisionBox().getTranslateX()
						&& player.getCollisionBox().getTranslateX() <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight()
								+ playerSpeed >= obstacles.get(i).getCollisionBox().getTranslateY()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight()
								+ playerSpeed <= obstacles.get(i).getCollisionBox().getTranslateY()
										+ obstacles.get(i).getCollisionBox().getHeight()) {
					collisionDown = true;
					player.moveFromCollisionBoxTo(player.getCollisionBox().getTranslateX(), obstacles.get(i).getCollisionBox().getTranslateY()
							- player.getCollisionBox().getHeight() - 1);
				}
				continue;
			}
			if (left && !right && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
					&& !collisionLeft) {
				if (player.getCollisionBox().getTranslateX() - playerSpeed >= obstacles.get(i).getCollisionBox()
						.getTranslateX()
						&& player.getCollisionBox().getTranslateX() - playerSpeed <= obstacles.get(i).getCollisionBox()
								.getTranslateX() + obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight() >= obstacles
								.get(i).getCollisionBox().getTranslateY()
						&& player.getCollisionBox().getTranslateY() <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					collisionLeft = true;
					player.moveFromCollisionBoxTo(obstacles.get(i).getCollisionBox().getTranslateX()
							+ obstacles.get(i).getCollisionBox().getWidth() + 1, player.getCollisionBox().getTranslateY());
				}
				continue;
			}
			if (right && !left && ((up && collisionUp) || (down && collisionDown) || (!(up || down) || (up && down)))
					&& !collisionRight) {
				if (player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth()
						+ playerSpeed >= obstacles.get(i).getCollisionBox().getTranslateX()
						&& player.getCollisionBox().getTranslateX() + player.getCollisionBox().getWidth()
								+ playerSpeed <= obstacles.get(i).getCollisionBox().getTranslateX()
										+ obstacles.get(i).getCollisionBox().getWidth()
						&& player.getCollisionBox().getTranslateY() + player.getCollisionBox().getHeight() >= obstacles
								.get(i).getCollisionBox().getTranslateY()
						&& player.getCollisionBox().getTranslateY() <= obstacles.get(i).getCollisionBox()
								.getTranslateY() + obstacles.get(i).getCollisionBox().getHeight()) {
					collisionRight = true;
					player.moveFromCollisionBoxTo(obstacles.get(i).getCollisionBox().getTranslateX()
							- player.getCollisionBox().getWidth() - 1, player.getCollisionBox().getTranslateY());
				}
				continue;
			}
		}
	}

	public void checkPlayerAnimation() {
		if (up && (!(down || left || right) || (!down && left && right))
				&& !player.getVisualizer().getCurrentAnimationName().equals("MOVE_UP")) {
			player.getVisualizer().setCurrentAnimation("MOVE_UP");
			currentPlayerDirection = PlayerFacingDirection.UP;
		}
		if (down && (!(up || left || right) || (!up && left && right))
				&& !player.getVisualizer().getCurrentAnimationName().equals("MOVE_DOWN")) {
			player.getVisualizer().setCurrentAnimation("MOVE_DOWN");
			currentPlayerDirection = PlayerFacingDirection.DOWN;
		}
		if (left && !right && !player.getVisualizer().getCurrentAnimationName().equals("MOVE_LEFT")) {
			player.getVisualizer().setCurrentAnimation("MOVE_LEFT");
			currentPlayerDirection = PlayerFacingDirection.LEFT;
		}
		if (right && !left && !player.getVisualizer().getCurrentAnimationName().equals("MOVE_RIGHT")) {
			player.getVisualizer().setCurrentAnimation("MOVE_RIGHT");
			currentPlayerDirection = PlayerFacingDirection.RIGHT;
		}
	}

}

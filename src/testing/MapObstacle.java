package testing;

public class MapObstacle extends MapObject {

	private double minInteractionDistanceX, maxInteractionDistanceX, minInteractionDistanceY, maxInteractionDistanceY;
	private boolean hasCollision, hasAnimation, isInteractable;

	private ObjectBox hitbox;
	private Interaction interaction;

	public MapObstacle(double x, double y, double width, double height, Animator animator, boolean hasAnimation) {
		super(x, y, width, height, animator, "STANDARD");
		this.hasAnimation = hasAnimation;
		hasCollision = false;
		isInteractable = false;
		moveTo(x, y);
	}

	@Override
	public void move(double x, double y) {
		super.move(x, y);
		if (hasCollision) {
			moveHitboxTo(body.getTranslateX(), body.getTranslateY());
		}
	}

	@Override
	public void moveTo(double x, double y) {
		super.moveTo(x, y);
		if (hasCollision) {
			moveHitboxTo(body.getTranslateX(), body.getTranslateY());
		}
		if (center != null) {
			setCenter(hitbox.getBoundsInParent().getMinX() + hitbox.getWidth(),
					hitbox.getBoundsInParent().getMinY() + hitbox.getHeight());
		}
	}

	public void moveHitboxTo(double x, double y) {
		hitbox.moveTo(x, y);
	}

	public void alignHitbox() {
		hitbox.moveTo(body.getTranslateX(), body.getTranslateY());
	}

	/**
	 * @param x Relative to the x-coordinate of the object's body.
	 * @param y Relative to the y-coordinate of the object's body.
	 */
	public void setHitbox(double x, double y, double width, double height) {
		if (!hasCollision) {
			hasCollision = true;
		}
		hitbox = new ObjectBox(x, y, width, height);
		alignHitbox();
		configureCenter();
	}

	public ObjectBox getHitbox() {
		return hitbox;
	}

	public boolean hasCollision() {
		return hasCollision;
	}

	public boolean hasAnimation() {
		return hasAnimation;
	}

	public boolean isInteractable() {
		return isInteractable;
	}

	public void setInteraction(Interaction interaction) {
		isInteractable = true;
		this.interaction = interaction;
	}

	public void setInteractionDistance(double minX, double maxX, double minY, double maxY) {
		minInteractionDistanceX = minX;
		maxInteractionDistanceX = maxX;
		minInteractionDistanceY = minY;
		maxInteractionDistanceY = maxY;
	}

	public double getMinInteractionDistanceX() {
		return minInteractionDistanceX;
	}

	public double getMaxInteractionDistanceX() {
		return maxInteractionDistanceX;
	}
	
	public double getMinInteractionDistanceY() {
		return minInteractionDistanceY;
	}

	public double getMaxInteractionDistanceY() {
		return maxInteractionDistanceY;
	}

	public void onInteraction() {
		interaction.onInteraction();
	}

	private void configureCenter() {
		setCenter(hitbox.getBoundsInParent().getMinX() + hitbox.getWidth() / 2,
				hitbox.getBoundsInParent().getMinY() + hitbox.getHeight() / 2);
	}

}

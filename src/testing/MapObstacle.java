package testing;

import javafx.scene.shape.Rectangle;

public class MapObstacle extends MapObject {

	private List<ObjectBox> hitbox;
	private boolean hasCollision, hasAnimation, isInteractable, hasInteractionBox;

	private Interaction interaction;
	private InteractionBox interactionBox;

	public MapObstacle(SpriteSheet sprites, double x, double y, double width, double height, Animator animator,
			boolean hasAnimation, boolean hasCollision, boolean isInteractable) {
		super(sprites, x, y, width, height, animator);
		this.hasAnimation = hasAnimation;
		this.hasCollision = hasCollision;
		this.isInteractable = isInteractable;
		if (this.hasCollision) {
			hitbox = new List<>();
		}
		moveTo(x, y);
	}

	@Override
	public void move(double x, double y) {
		super.move(x, y);
		if (hasCollision) {
			moveHitboxTo(body.getTranslateX(), body.getTranslateY());
		}
		if (hasInteractionBox) {
			interactionBox.moveTo(body.getTranslateX(), body.getTranslateY());
		}
	}

	@Override
	public void moveTo(double x, double y) {
		super.moveTo(x, y);
		if (hasCollision) {
			moveHitboxTo(body.getTranslateX(), body.getTranslateY());
		}
		if (hasInteractionBox) {
			interactionBox.moveTo(body.getTranslateX(), body.getTranslateY());
		}
	}

	public void moveHitboxTo(double x, double y) {
		for (int i = 0; i < hitbox.length(); i++) {
			hitbox.get(i).moveTo(x, y);
		}
	}

	public void alignHitbox() {
		for (int i = 0; i < hitbox.length(); i++) {
			hitbox.get(i).moveTo(body.getTranslateX(), body.getTranslateY());
		}
	}

	/**
	 * @param x Relative to the x-coordinate of the object's body.
	 * @param y Relative to the y-coordinate of the object's body.
	 */
	public void addHitbox(double x, double y, double width, double height) {
		if (!hasCollision) {
			return;
		}
		hitbox.add(new ObjectBox(x, y, width, height));
		alignHitbox();
	}

	public List<ObjectBox> getHitboxList() {
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
		this.interaction = interaction;
	}

	public void setInteractionBox(double x, double y, double width, double height) {
		interactionBox = new InteractionBox(x, y, width, height);
		hasInteractionBox = true;
	}

	public void onInteraction() {
		interaction.onInteraction();
	}

	private class InteractionBox extends Rectangle {

		public final double OFFSET_X, OFFSET_Y;

		public InteractionBox(double x, double y, double width, double height) {
			super(0, 0, width, height);
			setTranslateX(x);
			setTranslateY(y);
			OFFSET_X = x;
			OFFSET_Y = y;
			return;
		}

		public void move(double x, double y) {
			setTranslateX(getTranslateX() + x);
			setTranslateY(getTranslateY() + y);
		}

		public void moveTo(double x, double y) {
			setTranslateX(x + OFFSET_X);
			setTranslateY(y + OFFSET_Y);
		}

	}

}

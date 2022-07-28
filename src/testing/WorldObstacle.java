package testing;

public class WorldObstacle extends WorldObject {

	private List<BasicHitbox> hitbox;
	private boolean hasCollision, hasAnimation;

	public WorldObstacle(SpriteSheet sprites, double x, double y, double width, double height, Animator animator,
			boolean hasAnimation, boolean hasCollision) {
		super(sprites, x, y, width, height, animator);
		this.hasAnimation = hasAnimation;
		this.hasCollision = hasCollision;
		if (this.hasCollision) {
			hitbox = new List<>();
		}
		moveTo(x, y);
	}

	@Override
	public void nextAnimationFrame() {
		if (hasAnimation) {
			super.nextAnimationFrame();
		}
	}

	@Override
	public void changeAnimationTo(String animationName) {
		if (hasAnimation) {
			super.changeAnimationTo(animationName);
		}
	}

	@Override
	public String getCurrentAnimationName() {
		if (hasAnimation) {
			return super.getCurrentAnimationName();
		}
		return "";
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
		hitbox.add(new BasicHitbox(x, y, width, height));
		alignHitbox();
	}

	public List<BasicHitbox> getHitboxList() {
		return hitbox;
	}

	public boolean hasCollision() {
		return hasCollision;
	}

	public boolean hasAnimation() {
		return hasAnimation;
	}

}

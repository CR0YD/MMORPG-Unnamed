package testing;

import finished.List;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

public class Visualizer {

	private ImageView imageView;
	private List<List<Rectangle2D>> animations;
	private List<String> animationName;
	private int currentAnimationFrame, currentAnimationID;
	private double spriteColumnWidth, spriteRowHeight;
	private boolean isAnimated;

	public Visualizer(ImageView imageView, double spriteColumnWidth, double spriteRowHeight) {
		this.imageView = imageView;
		this.spriteColumnWidth = spriteColumnWidth;
		this.spriteRowHeight = spriteRowHeight;
		animations = new List<>();
		animationName = new List<>();
		isAnimated = false;
		setImageTo(0, 0);
	}
	
	public void setImageTo(int column, int row) {
		imageView.setViewport(new Rectangle2D(spriteColumnWidth * column, spriteRowHeight * row, spriteColumnWidth,
				spriteRowHeight));
	}

	public void addAnimation(String name) {
		if (!isAnimated) {
			isAnimated = true;
		}
		if (animationName.contains(name)) {
			return;
		}
		animationName.add(name);
		animations.add(new List<Rectangle2D>());
	}
	
	public void addFrameToAnimation(String name, int column, int row) {
		for (int i = 0; i < animationName.length(); i++) {
			if (animationName.get(i).equals(name)) {
				animations.get(i).add(new Rectangle2D(spriteColumnWidth * column, spriteRowHeight * row,
						spriteColumnWidth, spriteRowHeight));
				return;
			}
		}
	}

	public void addAnimation(String name, List<Rectangle2D> frames) {
		if (animationName.contains(name)) {
			return;
		}
		animationName.add(name);
		animations.add(frames);
	}

	public void addFramesToAnimation(String name, List<Rectangle2D> frames) {
		for (int i = 0; i < animationName.length(); i++) {
			if (animationName.get(i).equals(name)) {
				for (int j = 0; j < frames.length(); j++) {
					animations.get(i).add(frames.get(j));
				}
				return;
			}
		}
	}

	public void setCurrentAnimation(String name) {
		for (int i = 0; i < animationName.length(); i++) {
			if (animationName.get(i).equals(name)) {
				currentAnimationID = i;
				currentAnimationFrame = 0;
				imageView.setViewport(animations.get(i).get(currentAnimationFrame));
				return;
			}
		}
	}

	public void nextAnimationFrame() {
		currentAnimationFrame++;
		if (animations.get(currentAnimationID).length() == currentAnimationFrame) {
			currentAnimationFrame = 0;
		}
		imageView.setViewport(animations.get(currentAnimationID).get(currentAnimationFrame));
	}

	public String getCurrentAnimationName() {
		return animationName.get(currentAnimationID);
	}

	public int getCurrentFrameIdx() {
		return currentAnimationFrame;
	}
	
	public boolean isAnimated() {
		return isAnimated;
	}
	
	public ImageView getImageView() {
		return imageView;
	}

}

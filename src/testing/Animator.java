package testing;

import javafx.geometry.Rectangle2D;

public class Animator {

	private List<List<Rectangle2D>> animations;
	private List<String> animationName;
	private int currentAnimationFrame, currentAnimationID;
	private Rectangle2D view;
	
	public Animator() {
		animations = new List<>();
		animationName = new List<>();
		view = new Rectangle2D(0, 0, 0, 0);
	}
	
	public Rectangle2D getViewport() {
		return view;
	}
	
	public void addAnimation(String name) {
		if (animationName.contains(name)) {
			return;
		}
		animationName.add(name);
		animations.add(new List<Rectangle2D>());
	}
	
	public void addAnimation(String name, List<Rectangle2D> frames) {
		if (animationName.contains(name)) {
			return;
		}
		animationName.add(name);
		animations.add(frames);
	}
	
	public void addFrameToAnimation(String name, Rectangle2D frame) {
		for (int i = 0; i < animationName.length(); i++) {
			if (animationName.get(i).equals(name)) {
				animations.get(i).add(frame);
				return;
			}
		}
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
				view = animations.get(i).get(currentAnimationFrame);
				return;
			}
		}
	}
	
	public void nextAnimationFrame() {
		currentAnimationFrame++;
		if (animations.get(currentAnimationID).length() == currentAnimationFrame) {
			currentAnimationFrame = 0;
		}
		view = animations.get(currentAnimationID).get(currentAnimationFrame);
	}
	
	public String getCurrentAnimationName() {
		return animationName.get(currentAnimationID);
	}
	
	public int getCurrentFrameIdx() {
		return currentAnimationFrame;
	}
}

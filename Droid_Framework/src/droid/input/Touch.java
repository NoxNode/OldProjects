package droid.input;

import twoD.math.Vector2F;
import util.Time;

public class Touch {
	public int id;
	public Vector2F start, current;
	public long pressedTime;
	public static final int IDLE = 0, PRESSED = 1, RELEASED = 2,
			PRESSEDUSED = 3, RELEASEDUSED = 4, HELD = 5;
	int state;
	private boolean isTap, isScroll;

	public Touch() {
		this.id = 0;
		this.start = new Vector2F();
		this.current = new Vector2F();
		this.pressedTime = Time.getTimeInMillis();
		this.state = 0;
	}

	public Touch(int id, Vector2F start, Vector2F current, long pressedTime,
			int state) {
		this.id = id;
		this.start = new Vector2F(start);
		this.current = new Vector2F(current);
		this.pressedTime = pressedTime;
		this.state = state;
	}

	public Touch(Touch t) {
		this.id = t.id;
		this.start = new Vector2F(t.start);
		this.current = new Vector2F(t.current);
		this.pressedTime = t.pressedTime;
		this.state = t.state;
	}

	public boolean isPressed() {
		return state == Touch.PRESSEDUSED;
	}

	public boolean isReleased() {
		return state == Touch.RELEASEDUSED;
	}

	public boolean isTap() {
		return isTap;
	}

	public void setTap(boolean isTap) {
		this.isTap = isTap;
	}

	public boolean isScroll() {
		return isScroll;
	}

	public void setScroll(boolean isScroll) {
		this.isScroll = isScroll;
	}

	public boolean isSwipe() {
		return isScroll() && isTap();
	}
}

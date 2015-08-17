package com.nox.engine.input;

import com.nox.engine.util.Time;

public class Button {
	public static final int UP = 0, PRESSED = 1, PRESSEDUSED = 2, DOWN = 3, RELEASED = 4, RELEASEDUSED = 5;
	private int id, state;
	private long pressedTime, tapTime;
	private boolean isTap, wasTap, isDoubleTap, toBeReleased;

	public Button(int id) {
		this.id = id;
		this.state = Button.UP;
		this.pressedTime = 0;
		this.tapTime = 0;
		this.isTap = false;
		this.wasTap = false;
		this.isDoubleTap = false;
	}

	/**
	 * can remove Button when state == Button.UP and isTap == false
	 */
	public void refresh() {
		if(wasTap && Time.currentTimeMillis() - tapTime > Input.maxDoubleTapUpTimeInMillis) {
			wasTap = false;
		}
		if(state == Button.PRESSEDUSED) {
			if(toBeReleased) {
				state = Button.RELEASED;
			}
			else {
				state = Button.DOWN;
			}
		}
		if(state == Button.RELEASEDUSED) {
			state = Button.UP;
			if(isDoubleTap) {
				wasTap = false;
				isDoubleTap = false;
			}
			isTap = false;
			toBeReleased = false;
		}
		if(state == Button.PRESSED) {
			pressedTime = Time.currentTimeMillis();
			state = Button.PRESSEDUSED;
		}
		if(state == Button.RELEASED) {
			state = Button.RELEASEDUSED;
			long releasedTime = Time.currentTimeMillis();
			if(releasedTime - pressedTime < Input.maxTapTimeDownInMillis) {
				if(wasTap) {
					isDoubleTap = true;
				}
				isTap = true;
				wasTap = true;
				tapTime = Time.currentTimeMillis();
			}
		}
	}

	public int getId() {
		return id;
	}

	public Button setId(int id) {
		this.id = id;
		return this;
	}

	public int getState() {
		return state;
	}

	public Button setState(int state) {
		this.state = state;
		return this;
	}

	public Button setPressedTime(long time) {
		this.pressedTime = time;
		return this;
	}

	public long getTimeHeld(long currentTime) {
		return currentTime - pressedTime;
	}

	public boolean isPressed() {
		return state == Button.PRESSEDUSED;
	}

	public boolean isDown() {
		return state == Button.DOWN || state == Button.PRESSEDUSED || state == Button.RELEASEDUSED;
	}

	public boolean isReleased() {
		return state == Button.RELEASEDUSED;
	}

	public boolean isTap() {
		return isTap;
	}

	public boolean wasTap() {
		return wasTap;
	}

	public boolean isDoubleTap() {
		return isDoubleTap;
	}

	public void setToBeReleased(boolean val) {
		toBeReleased = val;
	}
}

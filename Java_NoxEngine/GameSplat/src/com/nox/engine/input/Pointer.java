package com.nox.engine.input;

import com.nox.engine.math2d.Vector2f;

public class Pointer {
	private Vector2f pos;
	private final Button[] buttons;
	private final Vector2f[] pressPositions;
	private int nButtons;

	public Pointer(Vector2f pos, Button[] buttons) {
		this.pos = pos;
		this.buttons = buttons;
		this.nButtons = buttons.length;
		this.pressPositions = new Vector2f[nButtons];
	}

	public Pointer(Vector2f pos) {
		this.pos = pos;
		this.buttons = null;
		this.nButtons = 0;
		this.pressPositions = null;
	}

	public void refresh() {
		for(int i = 0; i < nButtons; i++) {
			Button button = buttons[i];
			button.refresh();
			if(button.isPressed()) {
				this.pressPositions[i] = new Vector2f(pos);
			}
			if(button.isReleased()) {
				this.pressPositions[i] = null;
			}
		}
	}

	public Vector2f getPos() {
		return pos;
	}

	public void setPos(Vector2f pos) {
		this.pos = pos;
	}

	public Button getButton(int index) {
		return buttons[index];
	}

	public boolean isButtonScroll(int index) {
		if(pressPositions[index] != null) {
			return pos.sub(pressPositions[index]).length() > Input.minScrollDist;
		}
		return false;
	}

	public boolean isButtonSwipe(int index) {
		if(pressPositions[index] != null) {
			return buttons[index].isTap() && isButtonScroll(index);
		}
		return false;
	}

	public Vector2f getButtonScrollVector(int index) {
		if(pressPositions[index] != null) {
			return pos.sub(pressPositions[index]);
		}
		return new Vector2f();
	}
}

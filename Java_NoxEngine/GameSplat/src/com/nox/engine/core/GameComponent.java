package com.nox.engine.core;

import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.input.Input;

public abstract class GameComponent {
	private GameObject parent;
	private String tag;

	public GameComponent(String tag) {
		this.tag = tag;
	}

	public abstract void input(Input input);

	public abstract void update(float delta);

	public abstract void draw(Bitmap context);

	public GameObject getParent() {
		return parent;
	}

	public GameComponent setParent(GameObject parent) {
		this.parent = parent;
		return this;
	}

	public String getTag() {
		return tag;
	}

	public GameComponent setTag(String tag) {
		this.tag = tag;
		return this;
	}
}

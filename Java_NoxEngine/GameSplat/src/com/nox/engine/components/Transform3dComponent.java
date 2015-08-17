package com.nox.engine.components;

import com.nox.engine.core.GameComponent;
import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.input.Input;
import com.nox.engine.math3d.Transform3d;

public class Transform3dComponent extends GameComponent {
	private Transform3d transform;

	public Transform3dComponent() {
		super("Transform3d");
	}

	@Override
	public void input(Input input) {

	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(Bitmap context) {

	}

	public Transform3d getTransform() {
		return transform;
	}

	public void setTransform(Transform3d transform) {
		this.transform = transform;
	}
}

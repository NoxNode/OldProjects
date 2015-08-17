package com.nox.engine.components;

import java.util.ArrayList;

import com.nox.engine.core.GameComponent;
import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.input.Input;
import com.nox.engine.math2d.Transform2d;

public class BitmapRenderer extends GameComponent {
	private Bitmap bmp;

	public BitmapRenderer(Bitmap bmp) {
		super("BitmapRenderer");
		this.bmp = bmp;
	}

	@Override
	public void input(Input input) {

	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(Bitmap context) {
		ArrayList<GameComponent> parentTransformComponents = getParent().getGameComponents();
		Transform2dComponent parentTransformComponent = (Transform2dComponent) parentTransformComponents.get(Transform2dComponent.Transform2dComponentIndex);
		Transform2d parentTransform = parentTransformComponent.getTransform();

		context.drawBMP(bmp, parentTransform);
	}

}

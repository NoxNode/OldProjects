package com.nox.engine.components;

import java.util.ArrayList;

import com.nox.engine.core.GameComponent;
import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.input.Input;
import com.nox.engine.math2d.Transform2d;
import com.nox.engine.math2d.Vector2f;

public class Transform2dComponent extends GameComponent {
	public static final int Transform2dComponentIndex = 0;
	public static final String tag = "Transform2dComponent";
	private Transform2d transform;

	public Transform2dComponent() {
		super("Transform2dComponent");
		transform = new Transform2d(new Vector2f(0, 0), new Vector2f(1, 1), 0);
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

	public Transform2d getTransform() {
		Transform2d parentTransform;
		if(this.getParent().getParent() != null) {
			ArrayList<GameComponent> parentTransformComponents = this.getParent().getGameComponents();
			if(parentTransformComponents.size() > 0) {
				GameComponent parentTransformComponentUnCasted = parentTransformComponents.get(Transform2dComponent.Transform2dComponentIndex);
				if(parentTransformComponentUnCasted.getTag() == Transform2dComponent.tag) {
					parentTransform = ((Transform2dComponent) parentTransformComponentUnCasted).getTransform();
					Transform2d localTransform = new Transform2d();

					localTransform.setPos(parentTransform.getPos().add(this.transform.getPos()));
					localTransform.setScale(parentTransform.getScale().add(this.transform.getScale()));
					localTransform.setRot(parentTransform.getRot() + this.transform.getRot());
					return localTransform;
				}
			}
		}
		return transform;
	}

	public void setTransform(Transform2d transform) {
		this.transform = transform;
	}
}

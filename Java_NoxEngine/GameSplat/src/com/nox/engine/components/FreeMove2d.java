package com.nox.engine.components;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.nox.engine.core.GameComponent;
import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.input.Input;
import com.nox.engine.math2d.Transform2d;
import com.nox.engine.math2d.Vector2f;

public class FreeMove2d extends GameComponent {
	private Vector2f velocity;
	private float speed, maxSpeed;

	public FreeMove2d(float speed, float maxSpeed) {
		super("FreeMove2d");
		this.speed = speed;
		this.maxSpeed = maxSpeed;
		this.velocity = new Vector2f(1, 1);
	}

	@Override
	public void input(Input input) {
		velocity = velocity.mul(0.75f);
		speed = 40;
		maxSpeed = 4000;
		float length = velocity.length();
		if(length > -maxSpeed && (input.getButton(KeyEvent.VK_W).isDown() || input.getButton(KeyEvent.VK_UP).isDown())) {
			velocity.y -= speed;
		}
		if(length > -maxSpeed && (input.getButton(KeyEvent.VK_A).isDown() || input.getButton(KeyEvent.VK_LEFT).isDown())) {
			velocity.x -= speed;
		}
		if(length < maxSpeed && (input.getButton(KeyEvent.VK_S).isDown() || input.getButton(KeyEvent.VK_DOWN).isDown())) {
			velocity.y += speed;
		}
		if(length < maxSpeed && (input.getButton(KeyEvent.VK_D).isDown() || input.getButton(KeyEvent.VK_RIGHT).isDown())) {
			velocity.x += speed;
		}
//		if(length == 0) {
//			this.getParent().getLayer().getScene().getGame().getEngine().setIsRedrawing(false);
//		}
//		else {
//			this.getParent().getLayer().getScene().getGame().getEngine().setIsRedrawing(true);
//		}
	}

	@Override
	public void update(float delta) {
		ArrayList<GameComponent> parentTransformComponents = getParent().getGameComponents();
		Transform2dComponent parentTransformComponent = (Transform2dComponent) parentTransformComponents.get(Transform2dComponent.Transform2dComponentIndex);
		Transform2d parentTransform = parentTransformComponent.getTransform();

		parentTransform.setPos(parentTransform.getPos().add(velocity.mul(delta)));
	}

	@Override
	public void draw(Bitmap context) {

	}
}

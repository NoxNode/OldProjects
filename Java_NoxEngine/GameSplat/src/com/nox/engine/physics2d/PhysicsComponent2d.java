package com.nox.engine.physics2d;

import com.nox.engine.core.GameComponent;
import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.input.Input;

public class PhysicsComponent2d extends GameComponent {
	private CollisionAction2d action;
	private PhysicsEngine2d physicsEngine;
	private Collider2d collider;

	public PhysicsComponent2d(CollisionAction2d action, PhysicsEngine2d physicsEngine, Collider2d collider) {
		super("PhysicsComponent2d");
		this.action = action;
		this.physicsEngine = physicsEngine;
		this.physicsEngine.addPhysicsComponent(this);
		this.collider = collider;
	}

	public Collider2d getCollider() {
		return collider;
	}

	public void performCollisionAction(CollisionInfo2d info) {
		this.action.performAction(info);
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
}

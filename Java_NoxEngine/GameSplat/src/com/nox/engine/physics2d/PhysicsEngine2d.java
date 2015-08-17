package com.nox.engine.physics2d;

import java.util.ArrayList;

import com.nox.engine.physics.PhysicsEngine;

public class PhysicsEngine2d extends PhysicsEngine {
	private ArrayList<PhysicsComponent2d> physicsComponents;

	public PhysicsEngine2d() {
		this.physicsComponents = new ArrayList<PhysicsComponent2d>();
	}

	public void addPhysicsComponent(PhysicsComponent2d component) {
		this.physicsComponents.add(component);
	}

	@Override
	public void update() {
		for(int i = 0, size = physicsComponents.size(); i < size; i++) {
			PhysicsComponent2d component = physicsComponents.get(i);
			for(int j = i + 1; j < size; j++) {
				PhysicsComponent2d other = physicsComponents.get(j);
				CollisionInfo2d info = component.getCollider().getCollisionInfo(other.getCollider());
				component.performCollisionAction(info);
				other.performCollisionAction(info);
			}
		}
	}
}

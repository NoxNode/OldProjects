package com.nox.engine.physics2d;

import com.nox.engine.math2d.Vector2f;

public class CollisionInfo2d {
	private final Collider2d collider, other;
	private final Vector2f centerDist;

	public CollisionInfo2d(Collider2d collider, Collider2d other, Vector2f centerDist) {
		this.collider = collider;
		this.other = other;
		this.centerDist = centerDist;
	}

	public Collider2d getCollider() {
		return collider;
	}

	public Collider2d getOther() {
		return other;
	}

	public Vector2f getCenterDist() {
		return centerDist;
	}
}

package com.nox.engine.physics2d;


public abstract class Collider2d {
	public abstract CollisionInfo2d getCollisionInfo(Collider2d other);
}

package com.nox.engine.physics2d;

import com.nox.engine.math2d.Rectf;
import com.nox.engine.math2d.Transform2d;
import com.nox.engine.math2d.Vector2f;

public class RectCollider extends Collider2d {
	private final Rectf rect;
	private final Transform2d transform;

	public RectCollider(Rectf rect, Transform2d transform) {
		this.rect = rect;
		this.transform = transform;
	}

	public Rectf getRect() {
		return rect;
	}

	public Transform2d getTransform() {
		return transform;
	}

	@Override
	public CollisionInfo2d getCollisionInfo(Collider2d other) {
		if(other.getClass() == RectCollider.class) {
			RectCollider otherCollider = (RectCollider) other;
			Vector2f otherPos = new Vector2f(otherCollider.getTransform().getPos());
			Vector2f otherScale = new Vector2f(otherCollider.getTransform().getScale());
			Rectf otherRect = new Rectf(otherCollider.getRect());
			otherRect.x += otherPos.x;
			otherRect.y += otherPos.y;
			otherRect.width *= otherScale.x;
			otherRect.height *= otherScale.y;

			Rectf thisRect = new Rectf(this.rect);
			thisRect.x += this.transform.getPos().x;
			thisRect.y += this.transform.getPos().y;
			thisRect.width += this.transform.getScale().x;
			thisRect.height += this.transform.getScale().x;

			return new CollisionInfo2d(this, other, new Vector2f(otherRect.x + otherRect.width / 2, otherRect.y + otherRect.height / 2).sub(new Vector2f(thisRect.x + thisRect.width / 2, thisRect.y
					+ thisRect.height / 2)));
		}
		return null;
	}
}

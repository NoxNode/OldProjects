package com.nox.engine.math2d;

public class Circlef {
	public float cx, cy, radius;

	/**
	 * center = (0, 0)
	 */
	public Circlef(float radius) {
		this.cx = 0;
		this.cy = 0;
		this.radius = radius;
	}

	/**
	 * radius = 1
	 */
	public Circlef(float x, float y) {
		this.cx = x;
		this.cy = y;
		this.radius = 1;
	}

	public Circlef(float x, float y, float radius) {
		this.cx = x;
		this.cy = y;
		this.radius = radius;
	}

	public Circlef(Vector2f center, float radius) {
		this.cx = center.x;
		this.cy = center.y;
		this.radius = radius;
	}

	public Circlef(Circlef circle) {
		this.cx = circle.cx;
		this.cy = circle.cy;
		this.radius = circle.radius;
	}

	public boolean isInCircle(float x, float y) {
		if(new Vector2f(x, y).distFrom(new Vector2f(this.cx, this.cy)) < radius) {
			return true;
		}
		return false;
	}

	public boolean isInCircle(Vector2f v) {
		if(new Vector2f(v.x, v.y).distFrom(new Vector2f(this.cx, this.cy)) < radius) {
			return true;
		}
		return false;
	}

	public boolean collidingWithCircle(Circlef circle) {
		if(new Vector2f(circle.cx, circle.cy).distFrom(new Vector2f(this.cx, this.cy)) < radius + circle.radius) {
			return true;
		}
		return false;
	}

	public boolean collidingWithRect(Rectf rect) {
		if(this.cx > rect.x && this.cx < rect.x + rect.width && this.cy > rect.y && this.cy < rect.y + rect.height) {
			return true;
		}
		for(int i = 0; i < rect.width; i++) {
			if(new Vector2f(rect.x + i, rect.y).distFrom(new Vector2f(this.cx, this.cy)) < radius) {
				return true;
			}
			if(new Vector2f(rect.x + i, rect.y + rect.height).distFrom(new Vector2f(this.cx, this.cy)) < radius) {
				return true;
			}
		}
		for(int i = 0; i < rect.height; i++) {
			if(new Vector2f(rect.x + rect.width, rect.y + i).distFrom(new Vector2f(this.cx, this.cy)) < radius) {
				return true;
			}
			if(new Vector2f(rect.x, rect.y + i).distFrom(new Vector2f(this.cx, this.cy)) < radius) {
				return true;
			}
		}
		return false;
	}
}

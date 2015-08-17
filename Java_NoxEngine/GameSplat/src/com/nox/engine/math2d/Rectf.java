package com.nox.engine.math2d;

public class Rectf {
	public float x, y, width, height;

	public Rectf(float width, float height) {
		this.x = 0;
		this.y = 0;
		this.width = width;
		this.height = height;
	}

	public Rectf(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Rectf(Vector2f topLeft, float width, float height) {
		this.x = topLeft.x;
		this.y = topLeft.y;
		this.width = width;
		this.height = height;
	}

	public Rectf(Rectf rect) {
		this.x = rect.x;
		this.y = rect.y;
		this.width = rect.width;
		this.height = rect.height;
	}

	public boolean pointIsInThisRect(float x, float y) {
		return (x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height);
	}

	public boolean pointIsInThisRect(Vector2f v) {
		return (v.x >= this.x && v.x < this.x + this.width && v.y >= this.y && v.y < this.y + this.height);
	}

	public boolean isInRect(Rectf rect) {
		return (this.x >= rect.x && this.y >= rect.y && this.x + this.width < rect.x + rect.width && this.y + this.height < rect.x + rect.height);
	}

	public boolean collidingWithRect(Rectf rect) {
		return (this.x + this.width > rect.x && this.x < rect.x + rect.width && this.y + this.height > rect.y && this.y < rect.y + rect.height);
	}

	public boolean collidingWithCircle(Circlef circle) {
		if(circle.cx >= this.x && circle.cx < this.x + this.width && circle.cy > this.y && circle.cy < this.y + this.height) {
			return true;
		}
		for(int i = 0; i < this.width; i++) {
			if(new Vector2f(this.x + i, this.y).distFrom(new Vector2f(circle.cx, circle.cy)) < circle.radius) {
				return true;
			}
			if(new Vector2f(this.x + i, this.y + this.height).distFrom(new Vector2f(circle.cx, circle.cy)) < circle.radius) {
				return true;
			}
		}
		for(int i = 0; i < this.height; i++) {
			if(new Vector2f(this.x + this.width, this.y + i).distFrom(new Vector2f(circle.cx, circle.cy)) < circle.radius) {
				return true;
			}
			if(new Vector2f(this.x, this.y + i).distFrom(new Vector2f(circle.cx, circle.cy)) < circle.radius) {
				return true;
			}
		}
		return false;
	}
}

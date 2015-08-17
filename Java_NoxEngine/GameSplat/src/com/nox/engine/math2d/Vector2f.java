package com.nox.engine.math2d;

public class Vector2f {
	public float x;
	public float y;

	/**
	 * inits x and y as 0
	 */
	public Vector2f() {
		this.x = 0;
		this.y = 0;
	}

	public Vector2f(Vector2f v) {
		this.x = v.x;
		this.y = v.y;
	}

	public boolean equals(Vector2f v) {
		return x == v.x && y == v.y;
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f add(Vector2f v) {
		return new Vector2f(this.x + v.x, this.y + v.y);
	}

	public Vector2f add(float scalar) {
		return new Vector2f(this.x + scalar, this.y + scalar);
	}

	public Vector2f sub(Vector2f v) {
		return new Vector2f(this.x - v.x, this.y - v.y);
	}

	public Vector2f sub(float scalar) {
		return new Vector2f(this.x - scalar, this.y - scalar);
	}

	public Vector2f mul(Vector2f v) {
		return new Vector2f(this.x * v.x, this.y * v.y);
	}

	public Vector2f mul(float scalar) {
		return new Vector2f(this.x * scalar, this.y * scalar);
	}

	public Vector2f div(Vector2f v) {
		return new Vector2f(this.x / v.x, this.y / v.y);
	}

	public Vector2f div(float scalar) {
		return new Vector2f(this.x / scalar, this.y / scalar);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float dot(Vector2f v) {
		return this.x * v.x + this.y * v.y;
	}

	public Vector2f normalized() {
		float length = this.length();
		return new Vector2f(this.x / length, this.y / length);
	}

	public float cross(Vector2f v) {
		return this.x * v.y - this.y * v.x;
	}

	public Vector2f rotated(float radians) {
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);

		return new Vector2f((float) (this.x * cos - this.y * sin), (float) (this.x * sin + this.y * cos));
	}

	/**
	 * @return the angle of the vector in radians within the range [-pi..pi]
	 */
	public float angle() {
		return (float) Math.atan2(this.y, this.x);
	}

	public float distFrom(Vector2f v) {
		return (float) Math.sqrt((this.x - v.x) * (this.x - v.x) + (this.y - v.y) * (this.y - v.y));
	}
}

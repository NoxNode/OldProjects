package com.nox.engine.math3d;

public class Vector3f {
	public float x;
	public float y;
	public float z;

	/**
	 * inits x and y as 0
	 */
	public Vector3f() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vector3f(Vector3f v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public boolean equals(Vector3f v) {
		return x == v.x && y == v.y && z == v.z;
	}

	public Vector3f add(Vector3f v) {
		return new Vector3f(this.x + v.x, this.y + v.y, this.z + v.z);
	}

	public Vector3f add(float scalar) {
		return new Vector3f(this.x + scalar, this.y + scalar, this.z + scalar);
	}

	public Vector3f sub(Vector3f v) {
		return new Vector3f(this.x - v.x, this.y - v.y, this.z - v.z);
	}

	public Vector3f sub(float scalar) {
		return new Vector3f(this.x - scalar, this.y - scalar, this.z - scalar);
	}

	public Vector3f mul(Vector3f v) {
		return new Vector3f(this.x * v.x, this.y * v.y, this.z * v.z);
	}

	public Vector3f mul(float scalar) {
		return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
	}

	public Vector3f div(Vector3f v) {
		return new Vector3f(this.x / v.x, this.y / v.y, this.z / v.z);
	}

	public Vector3f div(float scalar) {
		return new Vector3f(this.x / scalar, this.y / scalar, this.z / scalar);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public float dot(Vector3f v) {
		return this.x * v.x + this.y * v.y + this.z + v.z;
	}

	public Vector3f normalized() {
		float length = this.length();
		return new Vector3f(this.x / length, this.y / length, this.z / length);
	}

	// TODO 3d cross product
	public float cross(Vector3f v) {
		return this.x * v.y - this.y * v.x;
	}

	public float distFrom(Vector3f v) {
		return (float) Math.sqrt((this.x - v.x) * (this.x - v.x) + (this.y - v.y) * (this.y - v.y) + (this.z - v.z) * (this.z - v.z));
	}
}

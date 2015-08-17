package com.nox.engine.math2d;

public class Transform2d {
	private Vector2f pos;
	private Vector2f scale;
	private float rot;

	public Transform2d() {
		pos = new Vector2f();
		scale = new Vector2f(1, 1);
		rot = 0;
	}

	public Transform2d(Vector2f pos, Vector2f scale, float rot) {
		this.pos = pos;
		this.scale = scale;
		this.rot = rot;
	}

	public Transform2d(Transform2d transform) {
		this.pos = new Vector2f(transform.pos);
		this.scale = new Vector2f(transform.scale);
		this.rot = transform.rot;
	}

	public Vector2f getPos() {
		return pos;
	}

	public void setPos(Vector2f pos) {
		this.pos = pos;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public float getRot() {
		return rot;
	}

	public void setRot(float rot) {
		this.rot = rot;
	}
}

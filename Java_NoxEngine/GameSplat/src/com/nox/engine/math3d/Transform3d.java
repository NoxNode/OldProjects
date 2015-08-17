package com.nox.engine.math3d;


public class Transform3d {
	private Vector3f pos;
	private Vector3f scale;
	private Vector3f rot;

	public Transform3d() {
		pos = new Vector3f();
		scale = new Vector3f();
		rot = new Vector3f();
	}

	public Transform3d(Vector3f pos, Vector3f scale, Vector3f rot) {
		this.pos = pos;
		this.scale = scale;
		this.rot = rot;
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f pos) {
		this.pos = pos;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public Vector3f getRot() {
		return rot;
	}

	public void setRot(Vector3f rot) {
		this.rot = rot;
	}
}

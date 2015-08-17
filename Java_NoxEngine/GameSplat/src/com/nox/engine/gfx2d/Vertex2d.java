package com.nox.engine.gfx2d;

import com.nox.engine.math2d.Vector2f;

public class Vertex2d {
	Vector2f screenPos;
	Vector2f texPos;

	public Vertex2d(Vector2f screenPos, Vector2f texPos) {
		this.screenPos = screenPos;
		this.texPos = texPos;
	}
}

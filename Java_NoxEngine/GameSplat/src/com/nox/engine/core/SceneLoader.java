package com.nox.engine.core;

public abstract class SceneLoader {
	private String tag;

	public SceneLoader(String tag) {
		this.tag = tag;
	}

	public abstract Scene loadScene();

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}

package com.nox.engine.core;

import java.util.ArrayList;

import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.input.Input;
import com.nox.engine.physics.PhysicsEngine;

public class GameObjectLayer {
	private Scene scene;
	private String tag;
	private final PhysicsEngine physicsEngine;
	private ArrayList<GameObject> objects;

	public GameObjectLayer(String tag, PhysicsEngine physicsEngine) {
		this.tag = tag;
		this.physicsEngine = physicsEngine;
		objects = new ArrayList<GameObject>();
	}

	public void inputAll(Input input) {
		for(GameObject object : objects) {
			object.inputAll(input);
		}
	}

	public void updateAll(float delta) {
		for(GameObject object : objects) {
			object.updateAll(delta);
		}
		if(physicsEngine != null) {
			physicsEngine.update();
		}
	}

	public void drawAll(Bitmap context) {
		for(GameObject object : objects) {
			object.drawAll(context);
		}
	}

	public Scene getScene() {
		return scene;
	}

	public GameObjectLayer setScene(Scene scene) {
		this.scene = scene;
		return this;
	}

	public String getTag() {
		return tag;
	}

	public GameObjectLayer setTag(String tag) {
		this.tag = tag;
		return this;
	}

	public PhysicsEngine getPhysicsEngine() {
		return physicsEngine;
	}

	public GameObjectLayer addGameObject(GameObject object) {
		object.setLayer(this);
		objects.add(object);
		return this;
	}

	public ArrayList<GameObject> getGameObjects() {
		return objects;
	}

	public GameObjectLayer removeGameObjectsWithTag(String tag) {
		for(int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			if(object.getTag() == tag) {
				objects.remove(i);
				i--;
			}
		}
		return this;
	}

	public GameObjectLayer removeGameObject(int index) {
		if(index < objects.size() && index >= 0) {
			objects.remove(index);
		}
		return this;
	}
}

package com.nox.engine.core;

import java.util.ArrayList;

import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.input.Input;

public class GameObject {
	private GameObjectLayer layer;
	private GameObject parent;
	private String tag;
	private ArrayList<GameObject> children;
	private ArrayList<GameComponent> components;

	public GameObject(String tag) {
		this.tag = tag;
		children = new ArrayList<GameObject>();
		components = new ArrayList<GameComponent>();
	}

	public void input(Input input) {
		for(GameComponent component : components) {
			component.input(input);
		}
	}

	public void inputAll(Input input) {
		input(input);
		for(GameObject child : children) {
			child.inputAll(input);
		}
	}

	public void update(float delta) {
		for(GameComponent component : components) {
			component.update(delta);
		}
	}

	public void updateAll(float delta) {
		update(delta);
		for(GameObject child : children) {
			child.updateAll(delta);
		}
	}

	public void draw(Bitmap context) {
		for(GameComponent component : components) {
			component.draw(context);
		}
	}

	public void drawAll(Bitmap context) {
		draw(context);
		for(GameObject child : children) {
			child.drawAll(context);
		}
	}

	public GameObjectLayer getLayer() {
		return layer;
	}

	public GameObject setLayer(GameObjectLayer layer) {
		this.layer = layer;
		return this;
	}

	public GameObject getParent() {
		return parent;
	}

	public void setParent(GameObject parent) {
		this.parent = parent;
	}

	public String getTag() {
		return tag;
	}

	public GameObject setTag(String tag) {
		this.tag = tag;
		return this;
	}

	public GameObject addChild(GameObject child) {
		child.setParent(this);
		child.setLayer(this.layer);
		children.add(child);
		return this;
	}

	public ArrayList<GameObject> getChildren() {
		return children;
	}

	public GameObject removeChildrenWithTag(String tag) {
		for(int i = 0; i < children.size(); i++) {
			GameObject child = children.get(i);
			if(child.getTag() == tag) {
				children.remove(i);
				i--;
			}
		}
		return this;
	}

	public GameObject removeChild(int index) {
		if(index < children.size() && index >= 0) {
			children.remove(index);
		}
		return this;
	}

	public GameObject addGameComponent(GameComponent component) {
		component.setParent(this);
		components.add(component);
		return this;
	}

	public ArrayList<GameComponent> getGameComponents() {
		return components;
	}

	public GameObject removeGameComponentsWithTag(String tag) {
		for(int i = 0; i < components.size(); i++) {
			GameComponent component = components.get(i);
			if(component.getTag() == tag) {
				components.remove(i);
				i--;
			}
		}
		return this;
	}

	public GameObject removeGameComponent(int index) {
		if(index < components.size() && index >= 0) {
			components.remove(index);
		}
		return this;
	}
}

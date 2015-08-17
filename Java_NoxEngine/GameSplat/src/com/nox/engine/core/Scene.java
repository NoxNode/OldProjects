package com.nox.engine.core;

import java.util.ArrayList;

import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.input.Input;

public abstract class Scene {
	private Game game;
	private String tag;
	private ArrayList<GameObjectLayer> layers;

	public Scene(String tag) {
		this.tag = tag;
		layers = new ArrayList<GameObjectLayer>();
	}

	public void input(Input input) {
		for(GameObjectLayer layer : layers) {
			layer.inputAll(input);
		}
	}

	public void update(float delta) {
		for(GameObjectLayer layer : layers) {
			layer.updateAll(delta);
		}
	}

	public void draw(Bitmap context) {
		for(GameObjectLayer layer : layers) {
			layer.drawAll(context);
		}
	}

	public abstract void clearScreen(Bitmap context);

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getTag() {
		return tag;
	}

	public Scene setTag(String tag) {
		this.tag = tag;
		return this;
	}

	public Scene addGameObjectLayer(GameObjectLayer layer) {
		layer.setScene(this);
		layers.add(layer);
		return this;
	}

	public ArrayList<GameObjectLayer> getGameObjectLayers() {
		return layers;
	}

	public Scene removeGameObjectLayersWithTag(String tag) {
		for(int i = 0; i < layers.size(); i++) {
			GameObjectLayer layer = layers.get(i);
			if(layer.getTag() == tag) {
				layers.remove(i);
				i--;
			}
		}
		return this;
	}

	public Scene removeGameObjectLayer(int index) {
		if(index < layers.size() && index >= 0) {
			layers.remove(index);
		}
		return this;
	}
}

package com.nox.engine.core;

import java.util.ArrayList;

import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.input.Input;

public abstract class Game {
	private CoreEngine engine;
	private Input input;
	private Scene drawingScene;
	private ArrayList<Scene> activeScenes;
	private ArrayList<SceneLoader> sceneLoaders;

	public Game() {
		this.activeScenes = new ArrayList<Scene>();
		this.sceneLoaders = new ArrayList<SceneLoader>();
	}

	/**
	 * need to initialize input variable to a new class that extends Input for whatever platform
	 */
	public abstract void initInput();

	public abstract void initGame();

	public void update(float delta) {
		input.refresh();
		for(int i = 0; i < activeScenes.size(); i++) {
			Scene activeScene = activeScenes.get(i);
			activeScene.input(input);
			activeScene.update(delta);
		}
	}

	public void draw(Bitmap context) {
		drawingScene.clearScreen(context);
		drawingScene.draw(context);
	}

	public CoreEngine getEngine() {
		return engine;
	}

	public void setEngine(CoreEngine engine) {
		this.engine = engine;
	}

	public Input getInput() {
		return input;
	}

	public Game setInput(Input input) {
		input.setGame(this);
		this.input = input;
		return this;
	}

	public Scene getDrawingScene() {
		return drawingScene;
	}

	public ArrayList<Scene> getActiveScenes() {
		return activeScenes;
	}

	public ArrayList<SceneLoader> getSceneLoaders() {
		return sceneLoaders;
	}

	public Game addSceneLoader(SceneLoader sceneLoader) {
		sceneLoaders.add(sceneLoader);
		return this;
	}

	public Game loadScene(String sceneLoaderTag) {
		for(int i = 0; i < sceneLoaders.size(); i++) {
			SceneLoader sceneLoader = sceneLoaders.get(i);
			if(sceneLoader.getTag() == sceneLoaderTag) {
				Scene scene = sceneLoader.loadScene();
				scene.setGame(this);
				activeScenes.add(scene);
			}
		}
		return this;
	}

	public Game loadScene(int index) {
		SceneLoader sceneLoader = sceneLoaders.get(index);
		Scene scene = sceneLoader.loadScene();
		scene.setGame(this);
		activeScenes.add(scene);
		return this;
	}

	public Game removeScenesWithTag(String activeSceneTag) {
		for(int i = 0; i < activeScenes.size(); i++) {
			Scene activeScene = activeScenes.get(i);
			if(activeScene.getTag() == activeSceneTag) {
				activeScenes.remove(i);
				i--;
			}
		}
		return this;
	}

	public Game removeScene(int index) {
		activeScenes.remove(index);
		return this;
	}

	public Game setDrawingScene(String activeSceneTag) {
		for(int i = 0; i < activeScenes.size(); i++) {
			Scene activeScene = activeScenes.get(i);
			if(activeScene.getTag() == activeSceneTag) {
				this.drawingScene = activeScene;
			}
		}
		return this;
	}

	public Game setDrawingScene(int index) {
		Scene activeScene = activeScenes.get(index);
		this.drawingScene = activeScene;
		return this;
	}

	public Game removeSceneLoadersWithTag(String tag) {
		for(int i = 0; i < sceneLoaders.size(); i++) {
			SceneLoader sceneLoader = sceneLoaders.get(i);
			if(sceneLoader.getTag() == tag) {
				sceneLoaders.remove(i);
				i--;
			}
		}
		return this;
	}

	public Game removeSceneLoader(int index) {
		if(index < sceneLoaders.size() && index >= 0) {
			sceneLoaders.remove(index);
		}
		return this;
	}
}

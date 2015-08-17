package com.nox.engine.core;

import java.awt.Canvas;

import com.nox.engine.input.Input;

public abstract class CoreEngine {

	public abstract boolean isRedrawing();

	public abstract void setIsRedrawing(boolean isRedrawing);

	public abstract int getFPS();

	public abstract void setFPS(int fps);

	public abstract void setResolution(int width, int height);

	public abstract void setCanvasSize(int width, int height);

	public abstract void start();

	public abstract void stop();

	public abstract void update(float deltaTime);

	public abstract void draw();

	public abstract void addInputListener(Input input);

	public abstract Window getWindow();

	public abstract Canvas getCanvas();

	public abstract void exit();

	public abstract void addClosingAction(ClosingAction action);
}

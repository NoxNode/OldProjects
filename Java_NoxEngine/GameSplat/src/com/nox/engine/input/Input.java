package com.nox.engine.input;

import com.nox.engine.core.Game;

public abstract class Input {
	public static final long maxTapTimeDownInMillis = 250, maxDoubleTapUpTimeInMillis = 250;
	public static final float minScrollDist = 25;
	private Game game;

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public abstract void restart();

	public abstract void refresh();

	public abstract Button getButton(int id);

	public abstract Axis getAxis(int id);

	public abstract Pointer getPointer(int id);
}

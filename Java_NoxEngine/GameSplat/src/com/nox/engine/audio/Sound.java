package com.nox.engine.audio;

public abstract class Sound {
	public abstract void play();

	public abstract void stop();

	public abstract void loop(int count);

	public abstract void loop();
}

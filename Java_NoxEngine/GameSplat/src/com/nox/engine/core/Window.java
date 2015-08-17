package com.nox.engine.core;

import java.awt.Canvas;
import java.awt.LayoutManager;

public abstract class Window {
	public abstract boolean isClosing();

	public abstract boolean isActivated();

	public abstract boolean isDeactivated();

	public abstract boolean isIconified();

	public abstract boolean isDeiconified();

	public abstract void setLayout(LayoutManager layout);

	public abstract void add(Canvas canvas, Object constraints);

	public abstract void pack();

	public abstract void setSize(int width, int height);

	public abstract void setTitle(String title);

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract void setLastWidth(int width);

	public abstract void setLastHeight(int height);

	public abstract boolean isResized();
}

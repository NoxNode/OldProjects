package com.nox.engine.core;

import java.awt.Canvas;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class JavaFrameWindow extends Window {
	private final Frame frame;
	private boolean isClosing, isActivated, isDeactivated, isIconified, isDeiconified;
	private int lastWidth, lastHeight;
	private final String title;

	public JavaFrameWindow(String title) {
		this.title = title;
		frame = new Frame(title);
		frame.setResizable(false);
		frame.setVisible(true);

		this.isClosing = false;
		this.isActivated = false;
		this.isDeactivated = false;
		this.isIconified = false;
		this.isDeiconified = false;
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowClosing(WindowEvent arg0) {
				isClosing = true;
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				isActivated = true;
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				isDeactivated = true;
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				isIconified = true;
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				isDeiconified = true;
			}

			@Override
			public void windowOpened(WindowEvent arg0) {

			}

			@Override
			public void windowClosed(WindowEvent arg0) {

			}
		});
	}

	@Override
	public boolean isClosing() {
		return isClosing;
	}

	@Override
	public boolean isActivated() {
		boolean retVal = isActivated;
		isActivated = false;
		return retVal;
	}

	@Override
	public boolean isDeactivated() {
		boolean retVal = isDeactivated;
		isDeactivated = false;
		return retVal;
	}

	@Override
	public boolean isIconified() {
		boolean retVal = isIconified;
		isIconified = false;
		return retVal;
	}

	@Override
	public boolean isDeiconified() {
		boolean retVal = isDeiconified;
		isDeiconified = false;
		return retVal;
	}

	@Override
	public void setLayout(LayoutManager layout) {
		frame.setLayout(layout);
	}

	@Override
	public void add(Canvas canvas, Object constraints) {
		frame.add(canvas, constraints);
	}

	@Override
	public void pack() {
		frame.pack();
	}

	@Override
	public void setSize(int width, int height) {
		frame.setSize(width, height);
	}

	@Override
	public int getWidth() {
		return frame.getWidth();
	}

	@Override
	public int getHeight() {
		return frame.getHeight();
	}

	@Override
	public void setLastWidth(int width) {
		lastWidth = width;
	}

	@Override
	public void setLastHeight(int height) {
		lastHeight = height;
	}

	@Override
	public boolean isResized() {
		return (lastWidth != getWidth() || lastHeight != getHeight());
	}

	@Override
	public void setTitle(String title) {
		frame.setTitle(this.title + " - " + title);
	}
}

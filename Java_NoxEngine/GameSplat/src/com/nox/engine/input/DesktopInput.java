package com.nox.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.nox.engine.math2d.Vector2f;

public class DesktopInput extends Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	public static final int leftClick = 0, rightClick = 1, middleClick = 2;
	private ArrayList<Button> keys;
	private Axis mouseWheel;
	private Pointer mouse;

	public DesktopInput() {
		restart();
	}

	@Override
	public void restart() {
		keys = new ArrayList<Button>();
		mouseWheel = new Axis();
		mouse = new Pointer(new Vector2f(), new Button[] { new Button(leftClick), new Button(rightClick), new Button(middleClick) });
	}

	@Override
	public void refresh() {
		for(int i = 0; i < keys.size(); i++) {
			Button key = keys.get(i);
			key.refresh();
			if(key.getState() == Button.UP && !key.wasTap()) {
				keys.remove(i);
				i--;
			}
		}
		mouseWheel.refresh();
		mouse.refresh();
	}

	@Override
	public Button getButton(int id) {
		for(int i = 0; i < keys.size(); i++) {
			Button key = keys.get(i);
			if(key.getId() == id) {
				return key;
			}
		}
		return new Button(-1);
	}

	@Override
	public Axis getAxis(int id) {
		return mouseWheel;
	}

	@Override
	public Pointer getPointer(int id) {
		return mouse;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		boolean inKeysDown = false;
		for(Button key : keys) {
			if(key.getId() == keyCode) {
				if(key.wasTap() && key.getState() == Button.UP) {
					key.setState(Button.PRESSED);
					return;
				}
				inKeysDown = true;
				break;
			}
		}
		Button key = new Button(keyCode);
		if(!inKeysDown) {
			key.setState(Button.PRESSED);
			keys.add(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		for(Button key : keys) {
			if(key.getId() == keyCode) {
				if(key.getState() == Button.PRESSED) {
					key.setToBeReleased(true);
				}
				else {
					key.setState(Button.RELEASED);
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			mouse.getButton(DesktopInput.leftClick).setState(Button.PRESSED);
		}
		else if(SwingUtilities.isRightMouseButton(e)) {
			mouse.getButton(DesktopInput.rightClick).setState(Button.PRESSED);
		}
		else if(SwingUtilities.isMiddleMouseButton(e)) {
			mouse.getButton(DesktopInput.middleClick).setState(Button.PRESSED);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			mouse.getButton(DesktopInput.leftClick).setState(Button.RELEASED);
		}
		else if(SwingUtilities.isRightMouseButton(e)) {
			mouse.getButton(DesktopInput.rightClick).setState(Button.RELEASED);
		}
		else if(SwingUtilities.isMiddleMouseButton(e)) {
			mouse.getButton(DesktopInput.middleClick).setState(Button.RELEASED);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouse.setPos(new Vector2f(e.getX(), e.getY()));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse.setPos(new Vector2f(e.getX(), e.getY()));
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouse.setPos(new Vector2f(e.getX(), e.getY()));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouse.setPos(new Vector2f(e.getX(), e.getY()));
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheel.setValue((float) e.getPreciseWheelRotation());
	}
}

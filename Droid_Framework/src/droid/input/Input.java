package droid.input;

import java.util.ArrayList;

import twoD.math.RectF;
import twoD.math.Vector2F;
import util.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Input implements OnTouchListener {
	private ArrayList<Touch> touches = new ArrayList<Touch>();
	private float minScrollDist;
	private long maxTapTimeDownInMillis;

	public Input(float minScrollDist, long maxTapTimeDownInMillis) {
		this.minScrollDist = minScrollDist;
		this.maxTapTimeDownInMillis = maxTapTimeDownInMillis;
	}

	/**
	 * Use this method at the start or end of the method or thread that updates
	 * the program based on input
	 */
	public void refresh() {
		for(int i = 0; i < touches.size(); i++) {
			Touch t = touches.get(i);
			if(t.state == Touch.HELD) {
				if(Vector2F.dist(t.start, t.current) < minScrollDist) {
					t.setScroll(true);
				}
			}
			else if(t.state == Touch.PRESSEDUSED) {
				t.state = Touch.HELD;
			}
			else if(t.state == Touch.RELEASEDUSED) {
				touches.remove(i);
				i--;
			}
			else if(t.state == Touch.PRESSED) {
				t.state = Touch.PRESSEDUSED;
			}
			else if(t.state == Touch.RELEASED) {
				if(Time.getTimeSinceMillis(t.pressedTime) < maxTapTimeDownInMillis) {
					t.setTap(true);
				}
				t.state = Touch.RELEASEDUSED;
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return true if the given rectangle is pressed by one or more touches
	 */
	public boolean isRectPressed(int x, int y, int width, int height) {
		for(Touch t : touches) {
			if(t.isPressed()
					&& RectF.isInRect(t.current, new RectF(x, y, width, height))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return true if the given rectangle is released by one or more touches
	 */
	public boolean isRectReleased(int x, int y, int width, int height) {
		for(Touch t : touches) {
			if(t.isReleased()
					&& RectF.isInRect(t.current, new RectF(x, y, width, height))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param maxTimeHeld
	 * @return true if the given rectangle is tapped by one or more touches
	 */
	public boolean isRectTapped(int x, int y, int width, int height) {
		for(Touch t : touches) {
			if(t.isTap()
					&& RectF.isInRect(t.start, new RectF(x, y, width, height))
					&& RectF.isInRect(t.current, new RectF(x, y, width, height))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return true if the given rectangle is touched by one or more touches
	 */
	public boolean isRectTouched(int x, int y, int width, int height) {
		for(Touch t : touches) {
			if(RectF.isInRect(t.current, new RectF(x, y, width, height))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return the first touch found in the given rectangle
	 */
	public Touch getTouchInRect(int x, int y, int width, int height) {
		for(Touch t : touches) {
			if(RectF.isInRect(t.current, new RectF(x, y, width, height))) {
				return new Touch(t);
			}
		}
		return new Touch();
	}

	public ArrayList<Touch> getTouches() {
		ArrayList<Touch> ret = new ArrayList<Touch>();
		for(Touch t : touches) {
			ret.add(new Touch(t));
		}
		return ret;
	}

	/**
	 * @param maxHeldTime
	 * @param minDist
	 * @return true if one or more touches was swiped
	 */
	public boolean isSwiped() {
		for(Touch t : touches) {
			if(t.isSwipe()) {
				return true;
			}
		}
		return false;
	}

	public boolean isScrolled() {
		for(Touch t : touches) {
			if(t.isScroll()) {
				return true;
			}
		}
		return false;
	}

	public boolean isPinched() {
		int nScrolls = 0;
		for(Touch t : touches) {
			if(t.isScroll()) {
				nScrolls++;
			}
		}
		return nScrolls == 2;
	}

	public ArrayList<Touch> getSwipes() {
		ArrayList<Touch> swipes = new ArrayList<Touch>();
		for(Touch t : touches) {
			if(t.isSwipe()) {
				swipes.add(new Touch(t));
			}
		}
		return swipes;
	}

	public ArrayList<Touch> getScrolls() {
		ArrayList<Touch> scrolls = new ArrayList<Touch>();
		for(Touch t : touches) {
			if(t.isScroll()) {
				scrolls.add(new Touch(t));
			}
		}
		return scrolls;
	}

	public ArrayList<Touch> getPinches() {
		ArrayList<Touch> pinches = new ArrayList<Touch>();
		if(isPinched()) {
			for(Touch t : touches) {
				if(t.isScroll()) {
					pinches.add(new Touch(t));
				}
			}
		}
		return pinches;
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		v.performClick();

		int pointerCount = e.getPointerCount();
		int pointerIndex = e.getActionIndex();
		int pointerId = e.getPointerId(pointerIndex);

		switch(e.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			Vector2F p = new Vector2F(e.getX(pointerIndex),
					e.getY((pointerIndex)));
			Touch t = new Touch(pointerId, p, p, Time.getTimeInMillis(),
					Touch.PRESSED);
			touches.add(new Touch(t));
			break;
		case MotionEvent.ACTION_MOVE:
			for(int i = 0; i < pointerCount; i++) {
				Vector2F p1 = new Vector2F(e.getX(i), e.getY(i));
				Touch t1 = new Touch();
				t1.id = e.getPointerId(i);
				t1.current = p1;
				for(int j = 0; j < touches.size(); j++) {
					Touch touch = touches.get(j);
					if(touch.id == t1.id) {
						touches.set(j, new Touch(t1.id, touch.start,
								t1.current, touch.pressedTime, touch.state));
					}
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			Vector2F p2 = new Vector2F(e.getX(pointerIndex),
					e.getY(pointerIndex));
			Touch t2 = new Touch();
			t2.id = pointerId;
			t2.current = p2;
			t2.state = Touch.RELEASED;
			for(int j = 0; j < touches.size(); j++) {
				Touch touch = touches.get(j);
				if(touch.id == t2.id) {
					touches.set(j, new Touch(t2.id, touch.start, t2.current,
							touch.pressedTime, t2.state));
				}
			}
			break;
		}

		return true;
	}
}

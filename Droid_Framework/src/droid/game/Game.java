package droid.game;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import droid.input.Input;
import droid.input.Touch;

public class Game {
	private int SCREENWIDTH, SCREENHEIGHT;
	private boolean initialized;
	private Input input;
	private Context context;
	private Paint white;

	public Game(Context context, Input input) {
		this.context = context;
		this.input = input;
		this.initialized = false;
	}

	public void init(int SCREENWIDTH, int SCREENHEIGHT) {
		this.SCREENWIDTH = SCREENWIDTH;
		this.SCREENHEIGHT = SCREENHEIGHT;

		white = new Paint();
		white.setColor(Color.WHITE);
		white.setStyle(Style.STROKE);
	}

	public void update() {
		if(initialized) {

		}
	}

	int boolTime = 0;

	public void draw(Canvas gfx) {
		if(!initialized) {
			SCREENWIDTH = gfx.getWidth();
			SCREENHEIGHT = gfx.getHeight();
			init(SCREENWIDTH, SCREENHEIGHT);
		}
		ArrayList<Touch> touches = input.getTouches();
		gfx.drawText("" + touches.size(), SCREENWIDTH / 2,
				SCREENHEIGHT / 2 + 200, white);

		if(boolTime > 0) {
			gfx.drawRect(SCREENWIDTH / 2, SCREENHEIGHT / 2,
					SCREENWIDTH / 2 + 20, SCREENHEIGHT / 2 + 20, white);
			boolTime--;
		}
		if(boolTime == 0 && touches.size() > 0 && touches.get(0).isTap()) {
			boolTime = 120;
		}
		gfx.drawText("" + boolTime, 20, 20, white);

		// for (int i = 0; i < touches.size(); i++) {
		// Touch t = touches.get(i);
		// gfx.drawLine(t.start.x, t.start.y, t.current.x, t.current.y, white);
		// }
	}
}

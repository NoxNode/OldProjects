package droid.game;

import util.Time;
import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import droid.input.Input;

public class Surface extends SurfaceView implements Runnable {

	SurfaceHolder surfaceHolder;
	Thread gameThread;
	private boolean running;
	private int preferredFPS;
	Input input;
	Game game;

	public Surface(Context context, Input input) {
		super(context);
		surfaceHolder = getHolder();
		this.input = input;

		game = new Game(context, input);

		preferredFPS = 60;

		start();
	}

	public void setPreferredFPS(int preferredFPS) {
		this.preferredFPS = preferredFPS;
	}

	public synchronized void start() {
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			gameThread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(!surfaceHolder.getSurface().isValid()) {
			try {
				Thread.sleep(10);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

		long lastTime = Time.getTimeInNanos();
		double nanosPerUpdate = 1000000000D / preferredFPS;
		double updatesToDo = 0;
		int millisToSleep = 5;

		while(running) {
			long currentTime = Time.getTimeInNanos();
			long deltaTime = currentTime - lastTime;
			updatesToDo += deltaTime / nanosPerUpdate;
			lastTime = currentTime;

			if(deltaTime < nanosPerUpdate) {
				millisToSleep++;
			}
			if(deltaTime > nanosPerUpdate && millisToSleep > 0) {
				millisToSleep--;
			}

			while(updatesToDo >= 1) {
				update();
				updatesToDo--;
			}

			draw();

			try {
				Thread.sleep(millisToSleep);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void update() {
		input.refresh();
		game.update();
	}

	public void draw() {
		Canvas canvas = surfaceHolder.lockCanvas();

		canvas.drawRGB(0, 0, 0); // background
		game.draw(canvas);

		surfaceHolder.unlockCanvasAndPost(canvas);
	}

	@Override
	public boolean performClick() {
		super.performClick();
		return true;

	}
}
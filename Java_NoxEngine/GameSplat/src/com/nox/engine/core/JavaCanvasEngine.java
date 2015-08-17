package com.nox.engine.core;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.input.Input;
import com.nox.engine.util.Time;

public class JavaCanvasEngine extends CoreEngine implements Runnable {
	private final Window window;
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics g;
	private BufferedImage img;
	private Bitmap context;
	private final Game game;
	private Thread mainThread;
	private boolean isRunning, isRedrawing, lastIsRedrawing;
	private int fps, lastFrames;
	private boolean canvasAdded;
	private ArrayList<ClosingAction> closingActions;

	public JavaCanvasEngine(Window window, int width, int height, int fps, Game game) {
		this.window = window;
		// init the canvas to be the window size set in Main.java
		setCanvasSize(window.getWidth(), window.getHeight());
		canvasAdded = true;

		// init the context (resolution of the game) to be the passed width and height
		setResolution(width, height);

		// init game
		this.game = game;
		this.game.setEngine(this);
		this.closingActions = new ArrayList<ClosingAction>();

		this.fps = fps;
		this.lastFrames = fps;
		this.isRunning = false;
		this.isRedrawing = true;
		this.lastIsRedrawing = false;
	}

	@Override
	public boolean isRedrawing() {
		return this.isRedrawing;
	}

	@Override
	public void setIsRedrawing(boolean isRedrawing) {
		this.isRedrawing = isRedrawing;
	}

	@Override
	public int getFPS() {
		return lastFrames;
	}

	@Override
	public void setFPS(int fps) {
		if(fps > 0) {
			this.fps = fps;
		}
	}

	@Override
	public void setResolution(int width, int height) {
		if(width > 0 && height > 0) {
			this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
			this.context = new Bitmap(width, height, pixels);
		}
	}

	@Override
	public void setCanvasSize(int width, int height) {
		if(width > 0 && height > 0) {
			// init Canvas
			Dimension dimension = new Dimension(width, height);
			if(!canvasAdded) {
				this.canvas = new Canvas();
				this.window.setLayout(new BorderLayout());
				this.window.add(canvas, BorderLayout.CENTER);
			}
			this.canvas.setMaximumSize(dimension);
			this.canvas.setMinimumSize(dimension);
			this.canvas.setPreferredSize(dimension);
			this.window.pack();

			this.canvas.setFocusable(true);
			this.canvas.requestFocusInWindow();

			// set lastWidth and lastHeight of window
			this.window.setLastWidth(window.getWidth());
			this.window.setLastHeight(window.getHeight());

			// init BufferStrategy
			canvas.createBufferStrategy(1);
			this.bs = canvas.getBufferStrategy();
			this.g = bs.getDrawGraphics();
		}
	}

	@Override
	public synchronized void start() {
		if(this.isRunning) {
			return;
		}
		this.isRunning = true;
		this.mainThread = new Thread(this, "mainThread");
		this.mainThread.start();
	}

	@Override
	public synchronized void stop() {
		if(!this.isRunning) {
			return;
		}
		this.isRunning = false;
		try {
			mainThread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		mainThread = null;
	}

	@Override
	public void run() {
		final double nanosPerFrame = 1000000000D / fps;
		long currentLoopTime = 0;
		long lastLoopTime = Time.nanoTime();
		long timeSinceLastLoop = 0;
		long deltaLoopTime = 0;

		long currentUpdateTime = 0;
		long lastUpdateTime = Time.nanoTime();
		long deltaUpdateTime = 0;

		// for taking up only necessary processing power
		long millisToSleep = 1;

		game.initInput();
		game.initGame();

		// TODO delete fps counter when distributing?
		int frames = 0;
		int updates = 0;
		long lastPrint = Time.currentTimeMillis();

		while(this.isRunning) {
			currentLoopTime = Time.nanoTime();
			timeSinceLastLoop = currentLoopTime - lastLoopTime;
			deltaLoopTime += currentLoopTime - lastLoopTime;
			lastLoopTime = currentLoopTime;

			// for taking up only necessary processing power
			if(timeSinceLastLoop > nanosPerFrame && millisToSleep > 0) {
				millisToSleep--;
			}
			if(timeSinceLastLoop < nanosPerFrame) {
				millisToSleep++;
			}

			int updateCount = 0;
			while(deltaLoopTime >= nanosPerFrame) {
				currentUpdateTime = Time.nanoTime();
				deltaUpdateTime = currentUpdateTime - lastUpdateTime;
				lastUpdateTime = currentUpdateTime;

				this.update(deltaUpdateTime / 1000000000f);
				deltaLoopTime -= nanosPerFrame;

				updateCount++;
				// DEBUG CODE - delete when distributing
				updates++;
			}

			if(lastIsRedrawing) {
				this.draw();
				// DEBUG CODE - delete when distributing
				frames++;
			}
			if(isRedrawing != lastIsRedrawing) {
				lastIsRedrawing = isRedrawing;
			}

			// DEBUG CODE - delete when distributing
			if(Time.currentTimeMillis() - lastPrint >= 1000) {
				lastPrint += 1000;
				window.setTitle("fps: " + frames + " ups: " + updates + " millisToSleep: " + millisToSleep);
//				System.out.println("fps: " + frames + " ups: " + updates + " millisToSleep: " + millisToSleep);
				updates = 0;
				this.lastFrames = frames;
				frames = 0;
			}

			if(window.isClosing()) {
				exit();
			}

			try {
				Thread.sleep(millisToSleep);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		this.game.update(deltaTime);
	}

	@Override
	public void draw() {
		this.game.draw(context);
		g.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bs.show();
	}

	@Override
	public void addInputListener(Input input) {
		this.canvas.addKeyListener((KeyListener) input);
		this.canvas.addMouseListener((MouseListener) input);
		this.canvas.addMouseMotionListener((MouseMotionListener) input);
		this.canvas.addMouseWheelListener((MouseWheelListener) input);
	}

	@Override
	public void addClosingAction(ClosingAction action) {
		closingActions.add(action);
	}

	@Override
	public Window getWindow() {
		return window;
	}

	@Override
	public void exit() {
		for(int i = 0, size = closingActions.size(); i < size; i++) {
			closingActions.get(i).performAction();
		}
		g.dispose();
		System.exit(0);
	}

	@Override
	public Canvas getCanvas() {
		return canvas;
	}
}
package com.nox.game;

import com.nox.engine.core.CoreEngine;
import com.nox.engine.core.JavaCanvasEngine;
import com.nox.engine.core.JavaFrameWindow;
import com.nox.engine.core.Window;

public class Main {
	public static void main(String[] args) {
		Window window = new JavaFrameWindow("Nox Engine");
		window.setSize(800, 600);
		CoreEngine engine = new JavaCanvasEngine(window, 800, 600, 60, new GameSplat());
		engine.start();
	}
}

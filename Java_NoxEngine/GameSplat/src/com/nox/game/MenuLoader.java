package com.nox.game;

import java.awt.event.KeyEvent;

import com.nox.engine.core.Game;
import com.nox.engine.core.GameComponent;
import com.nox.engine.core.GameObject;
import com.nox.engine.core.GameObjectLayer;
import com.nox.engine.core.Scene;
import com.nox.engine.core.SceneLoader;
import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.gfx2d.Font;
import com.nox.engine.input.Input;
import com.nox.engine.math2d.Transform2d;
import com.nox.engine.math2d.Vector2f;
import com.nox.engine.net.Client;

public class MenuLoader extends SceneLoader {
    private Client client;

    public MenuLoader(Client client) {
	super("menuLoader");
	this.client = client;
    }

    @Override
    public Scene loadScene() {
	Scene menuScene = new Scene("menuScene") {
	    @Override
	    public void clearScreen(Bitmap context) {
		context.clear(0);
	    }
	};

	GameObjectLayer menuLayer = new GameObjectLayer("menuLayer", null);
	GameObject menuObj = new GameObject("menuObj");
	GameComponent menuComponent = new GameComponent("menuComponent") {
	    Bitmap dinaBMP = new Bitmap("res/fonts/dina.bmp").replacedColor(
		    0xFFFF00FF, 0).replacedColor(0xFF000000, 0xFFFF0000);
	    Font dina = new Font(dinaBMP);
	    Transform2d titleTransform = new Transform2d(new Vector2f(80, 50),
		    new Vector2f(3.5f, 3.5f), 0);
	    Transform2d continueTransform = new Transform2d(new Vector2f(80,
		    125), new Vector2f(1.5f, 1.5f), 0);

	    @Override
	    public void input(Input input) {
		if (input.getButton(KeyEvent.VK_SPACE).isDown()) {
		    Game game = this.getParent().getLayer().getScene()
			    .getGame();

		    game.addSceneLoader(new LobbyLoader(client));
		    game.loadScene(1);
		    game.setDrawingScene(1);

		    game.removeScene(0);
		}
	    }

	    @Override
	    public void update(float delta) {

	    }

	    @Override
	    public void draw(Bitmap context) {
		dina.drawStringToBMP(context, "GameSplat!", titleTransform);
		dina.drawStringToBMP(context, "Press space to start",
			continueTransform);
	    }
	};

	menuObj.addGameComponent(menuComponent);
	menuLayer.addGameObject(menuObj);
	menuScene.addGameObjectLayer(menuLayer);

	return menuScene;
    }
}

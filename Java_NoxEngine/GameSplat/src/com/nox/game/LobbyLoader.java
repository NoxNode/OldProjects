package com.nox.game;

import java.awt.event.KeyEvent;
import java.net.DatagramPacket;
import java.util.ArrayList;

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
import com.nox.engine.util.Serializer;

public class LobbyLoader extends SceneLoader {
    private Client client;

    public LobbyLoader(Client client) {
	super("lobbyLoader");
	this.client = client;
    }

    @Override
    public Scene loadScene() {
	Scene lobbyScene = new Scene("lobbyScene") {
	    @Override
	    public void clearScreen(Bitmap context) {
		context.clear(0);
	    }
	};

	GameObjectLayer lobbyLayer = new GameObjectLayer("lobbyLayer", null);
	GameObject lobbyObj = new GameObject("lobbyObj");
	GameComponent lobbyComponent = new GameComponent("lobbyComponent") {
	    Bitmap dinaBMP = new Bitmap("res/fonts/dina.bmp").replacedColor(
		    0xFFFF00FF, 0).replacedColor(0xFF000000, 0xFFFF0000);
	    Font dina = new Font(dinaBMP);
	    ArrayList<Transform2d> transforms = new ArrayList<Transform2d>();
	    Transform2d drawingTransform = new Transform2d();
	    boolean registered = false;
	    int index = 0;

	    @Override
	    public void input(Input input) {
		// TODO maybe add a way to add code to the game that always
		// updates no matter the scene
		// TODO if the above is done, do that for the bottom two
		// statements
		if (input.getButton(KeyEvent.VK_ESCAPE).isDown()) {
		    this.getParent().getLayer().getScene().getGame()
			    .getEngine().exit();
		}
		if (this.getParent().getLayer().getScene().getGame()
			.getEngine().getWindow().isDeactivated()) {
		    input.restart();
		}

		if (registered) {
		    final int speed = 5;

		    if (input.getButton(KeyEvent.VK_W).isDown()) {
			client.send(new byte[] { 'W' });
			// transforms.get(index).setPos(transforms.get(index).getPos().add(new
			// Vector2f(0, -speed)));
		    }
		    if (input.getButton(KeyEvent.VK_A).isDown()) {
			client.send(new byte[] { 'A' });
			// transforms.get(index).setPos(transforms.get(index).getPos().add(new
			// Vector2f(-speed, 0)));
		    }
		    if (input.getButton(KeyEvent.VK_S).isDown()) {
			client.send(new byte[] { 'S' });
			// transforms.get(index).setPos(transforms.get(index).getPos().add(new
			// Vector2f(0, speed)));
		    }
		    if (input.getButton(KeyEvent.VK_D).isDown()) {
			client.send(new byte[] { 'D' });
			// transforms.get(index).setPos(transforms.get(index).getPos().add(new
			// Vector2f(speed, 0)));
		    }
		}
	    }

	    int nRegisterRequests = 0;

	    @Override
	    public void update(float delta) {
		if (!registered) {
		    nRegisterRequests++;
		    client.send(new byte[] { 'R', (byte) nRegisterRequests });
		}
		ArrayList<DatagramPacket> packets = client.getPackets(true);
		for (int i = 0, size = packets.size(); i < size; i++) {
		    DatagramPacket packet = packets.get(i);
		    byte[] data = packet.getData();

		    if (data.length > 0) {
			if (!registered && data[0] == 'R') {
			    registered = true;
			    index = data[1];
			    while (transforms.size() - 1 < index) {
				transforms.add(new Transform2d());
			    }
			} else {
			    int index = data[0];
			    float x = (Serializer.getIntFromByteBuffer(data, 1,
				    4));
			    float y = (Serializer.getIntFromByteBuffer(data, 5,
				    4));
			    while (transforms.size() - 1 < index) {
				transforms.add(new Transform2d());
			    }
			    transforms.get(index).setPos(new Vector2f(x, y));
			}
		    }
		}
	    }

	    @Override
	    public void draw(Bitmap context) {
		for (int i = 0; i < transforms.size(); i++) {
		    dina.drawStringToBMP(context, "drawing", transforms.get(i));
		}
	    }
	};

	lobbyObj.addGameComponent(lobbyComponent);
	lobbyLayer.addGameObject(lobbyObj);
	lobbyScene.addGameObjectLayer(lobbyLayer);

	return lobbyScene;
    }
}

package com.nox.game;

import com.nox.engine.core.Game;
import com.nox.engine.core.GameComponent;
import com.nox.engine.core.GameObject;
import com.nox.engine.core.GameObjectLayer;
import com.nox.engine.core.Scene;
import com.nox.engine.core.SceneLoader;
import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.gfx2d.BitmapAnimation;
import com.nox.engine.input.DesktopInput;
import com.nox.engine.input.Input;
import com.nox.engine.math2d.Transform2d;
import com.nox.engine.math2d.Vector2f;
import com.nox.engine.physics.PhysicsEngine;

public class GameSplat extends Game {

	@Override
	public void initInput() {
		Input input = new DesktopInput();
		this.setInput(input);
		this.getEngine().addInputListener(input);
	}

	@Override
	public void initGame() {
		SceneLoader sceneLoader = new SceneLoader("sceneLoader") {
			@Override
			public Scene loadScene() {
				Scene scene = new Scene("scene") {
					@Override
					public void clearScreen(Bitmap context) {
						context.clear(0);
					}
				};
				GameObjectLayer objLayer = new GameObjectLayer("objLayer", new PhysicsEngine() {
					@Override
					public void update() {
						
					}
				});
				GameObject obj = new GameObject("obj");
				obj.addGameComponent(new GameComponent("component") {
					Bitmap spriteSheet = new Bitmap("res/images/will_sprite_sheet.bmp").replacedColor(0xFFFF00FF, 0);
					Bitmap right_idleAnimStrip = spriteSheet.cropped(0, 0, 48, 32);
					Bitmap right_runAnimStrip = spriteSheet.cropped(0, 32, 96, 32);
					Bitmap right_rightAttackAnimStrip = spriteSheet.cropped(0, 64, 48, 32);

					BitmapAnimation right_idleAnim = new BitmapAnimation("right_idleAnim", right_idleAnimStrip, 24, 30);
					BitmapAnimation right_rightAttackAnim = new BitmapAnimation("right_rightAttackAnim", right_rightAttackAnimStrip, 24, 15);
					BitmapAnimation right_runAnim = new BitmapAnimation("right_runAnim", right_runAnimStrip, 24, 10);

					BitmapAnimation left_idleAnim = right_idleAnim.xFlippedAnimationBMPs();
					BitmapAnimation left_rightAttackAnim = right_rightAttackAnim.xFlippedAnimationBMPs();
					BitmapAnimation left_runAnim = right_runAnim.xFlippedAnimationBMPs();

					int state = 0;
					boolean facingLeft = false;
					float scale = 3;
					Transform2d transform = new Transform2d(new Vector2f(0, 284), new Vector2f(scale, scale), 0);
					
					@Override
					public void input(Input input) {
						final float speed = 2.5f;
						if(input.getButton('L').isDown()) {
							state = 1;
							facingLeft = false;
						}
						else if(input.getButton('D').isDown()) {
							state = 2;
							facingLeft = false;
							transform.setPos(transform.getPos().add(new Vector2f(speed, 0)));
						}
						else if(input.getButton('A').isDown()) {
							state = 3;
							facingLeft = true;
							transform.setPos(transform.getPos().add(new Vector2f(-speed, 0)));
						}
						else if(input.getButton('J').isDown()) {
							state = 4;
							facingLeft = true;
						}
						else {
							if(facingLeft) {
								state = 5;
							}
							else {
								state = 0;
							}
						}
					}
					@Override
					public void update(float delta) {
						scale = 3;
						transform.setScale(new Vector2f(scale, scale));
					}
					@Override
					public void draw(Bitmap context) {
						switch(state) {
						case 0:
							right_rightAttackAnim.restart();
							right_runAnim.restart();
							left_runAnim.restart();
							left_rightAttackAnim.restart();
							left_idleAnim.restart();
							
							right_idleAnim.refresh();
							right_idleAnim.draw(context, transform);
							break;
						case 1:
							right_idleAnim.restart();
							right_runAnim.restart();
							left_runAnim.restart();
							left_rightAttackAnim.restart();
							left_idleAnim.restart();
							
							right_rightAttackAnim.refresh();
							right_rightAttackAnim.draw(context, transform);
							break;
						case 2:
							right_idleAnim.restart();
							right_rightAttackAnim.restart();
							left_runAnim.restart();
							left_rightAttackAnim.restart();
							left_idleAnim.restart();
							
							right_runAnim.refresh();
							right_runAnim.draw(context, transform);
							break;
						case 3:
							right_rightAttackAnim.restart();
							right_idleAnim.restart();
							right_runAnim.restart();
							left_rightAttackAnim.restart();
							left_idleAnim.restart();
							
							left_runAnim.refresh();
							left_runAnim.draw(context, transform);
							break;
						case 4:
							right_rightAttackAnim.restart();
							right_idleAnim.restart();
							right_runAnim.restart();
							left_runAnim.restart();
							left_idleAnim.restart();
							
							left_rightAttackAnim.refresh();
							left_rightAttackAnim.draw(context, transform);
							break;
						case 5:
							right_rightAttackAnim.restart();
							right_idleAnim.restart();
							right_runAnim.restart();
							left_runAnim.restart();
							left_rightAttackAnim.restart();
							
							left_idleAnim.refresh();
							left_idleAnim.draw(context, transform);
						}
					}
				});
				objLayer.addGameObject(obj);
				scene.addGameObjectLayer(objLayer);
				return scene;
			}
		};
		
		this.addSceneLoader(sceneLoader);
		this.loadScene(0);
		this.setDrawingScene(0);
	}
}

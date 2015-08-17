package com.nox.engine.components;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.nox.engine.core.GameComponent;
import com.nox.engine.gfx2d.Bitmap;
import com.nox.engine.gfx2d.BitmapAnimation;
import com.nox.engine.input.Input;
import com.nox.engine.math2d.Transform2d;

public class AnimatedBitmapRenderer extends GameComponent {
	private BitmapAnimation[] bitmapAnimations;
	private int currentAnimationIndex, nextAnimationIndex;

	public AnimatedBitmapRenderer(Bitmap animationSheet, String[] animationTags, int[] nBMPsPerAnimation, int animationBMPWidth, int animationBMPHeight, int framesPerBitmap) {
		super("AnimatedBitmapRenderer");

		int nAnimations = animationSheet.getHeight() / animationBMPHeight;
		if(animationTags.length == nAnimations) {
			bitmapAnimations = new BitmapAnimation[nAnimations];
			for(int i = 0; i < nAnimations; i++) {
				bitmapAnimations[i] = new BitmapAnimation(animationTags[i], animationSheet.cropped(0, i * animationBMPHeight, nBMPsPerAnimation[i] * animationBMPWidth, animationBMPHeight),
						animationBMPWidth, framesPerBitmap);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Error: the number of animationTags do not match the number of bitmap animations");
			System.exit(0);
		}
		currentAnimationIndex = 0;
		nextAnimationIndex = 0;
	}

	@Override
	public void input(Input input) {

	}

	@Override
	public void update(float delta) {
		bitmapAnimations[currentAnimationIndex].refresh();
		if(bitmapAnimations[currentAnimationIndex].reachedEnd()) {
			bitmapAnimations[currentAnimationIndex].restart();
			bitmapAnimations[currentAnimationIndex] = bitmapAnimations[nextAnimationIndex];
		}
	}

	@Override
	public void draw(Bitmap context) {
		ArrayList<GameComponent> parentTransformComponents = this.getParent().getGameComponents();
		Transform2dComponent parentTransformComponent = (Transform2dComponent) parentTransformComponents.get(Transform2dComponent.Transform2dComponentIndex);
		Transform2d parentTransform = parentTransformComponent.getTransform();

		bitmapAnimations[currentAnimationIndex].draw(context, parentTransform);
	}

	public void setFramesPerBitmap(int framesPerBitmap) {
		for(int i = 0; i < bitmapAnimations.length; i++) {
			bitmapAnimations[i].setFramesPerBitmap(framesPerBitmap);
		}
	}

	public void setCurrentAnimation(String tag) {
		for(int i = 0; i < bitmapAnimations.length; i++) {
			if(bitmapAnimations[i].getTag() == tag) {
				this.currentAnimationIndex = i;
			}
		}
	}

	public void setCurrentAnimation(int index) {
		this.currentAnimationIndex = index;
	}

	public String getCurrentAnimationTag() {
		return bitmapAnimations[this.currentAnimationIndex].getTag();
	}

	public int getCurrentAnimationIndex() {
		return this.currentAnimationIndex;
	}

	public void setNextAnimation(String tag) {
		for(int i = 0; i < bitmapAnimations.length; i++) {
			if(bitmapAnimations[i].getTag() == tag) {
				this.nextAnimationIndex = i;
			}
		}
	}

	public void setNextAnimation(int index) {
		this.nextAnimationIndex = index;
	}

	public String getNextAnimationTag() {
		return bitmapAnimations[this.nextAnimationIndex].getTag();
	}

	public int getNextAnimationIndex() {
		return this.nextAnimationIndex;
	}
}

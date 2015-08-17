package com.nox.engine.gfx2d;

import com.nox.engine.math2d.Transform2d;

public class BitmapAnimation {
	private String tag;
	private Bitmap[] animationBMPs;
	private int framesPerBitmap, frames;

	public BitmapAnimation(String tag, Bitmap animationStrip, int animationBMPWidth, int framesPerBitmap) {
		this.tag = tag;
		int nAnimationBMPs = animationStrip.getWidth() / animationBMPWidth;
		animationBMPs = new Bitmap[nAnimationBMPs];
		for(int i = 0; i < nAnimationBMPs; i++) {
			animationBMPs[i] = animationStrip.cropped(i * animationBMPWidth, 0, animationBMPWidth, animationStrip.getHeight());
		}
		this.framesPerBitmap = framesPerBitmap;
		this.frames = 0;
	}

	public void draw(Bitmap context, Transform2d transform) {
		context.drawBMP(animationBMPs[(frames / framesPerBitmap) % animationBMPs.length], transform);
	}
	
	public BitmapAnimation xFlippedAnimationBMPs() {
		Bitmap bmp = new Bitmap(animationBMPs[0].getWidth() * animationBMPs.length, animationBMPs[0].getHeight());
		BitmapAnimation retBMPAnim = new BitmapAnimation(tag, bmp, animationBMPs[0].getWidth(), framesPerBitmap);
		for(int i = 0; i < animationBMPs.length; i++) {
			retBMPAnim.animationBMPs[i] = animationBMPs[i].xFlipped();
		}
		return retBMPAnim;
	}

	public void refresh() {
		this.frames++;
	}

	public void restart() {
		frames = 0;
	}

	public boolean reachedEnd() {
		return frames >= framesPerBitmap * animationBMPs.length;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setFramesPerBitmap(int framesPerBitmap) {
		this.framesPerBitmap = framesPerBitmap;
	}
}

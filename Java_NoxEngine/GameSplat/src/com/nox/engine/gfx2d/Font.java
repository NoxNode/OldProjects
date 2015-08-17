package com.nox.engine.gfx2d;

import com.nox.engine.math2d.Transform2d;
import com.nox.engine.math2d.Vector2f;

public class Font {
	public static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345abcdefghijklmnopqrstuvwxyz67890{}[]()<>$*-+=/#_%^@\\&|~?\'\"!,.;: `";
	private Bitmap fontBMP;
	private Bitmap[] charBMPs;
	private int charWidth, charHeight;

	public Font(String fileName) {
		this.initFromFontBMP(new Bitmap(fileName));
	}

	public Font(Bitmap fontBMP) {
		this.initFromFontBMP(fontBMP);
	}

	public Font(Font font) {
		this.initFromFontBMP(font.fontBMP);
	}

	public void initFromFontBMP(Bitmap fontBMP) {
		this.fontBMP = fontBMP;
		int nChars = chars.length();
		this.charWidth = fontBMP.getWidth() / nChars;
		this.charHeight = fontBMP.getHeight();
		this.charBMPs = new Bitmap[nChars];
		for(int i = 0; i < nChars; i++) {
			// 32 is the ascii of space and the first ascii char of the string chars
			this.charBMPs[chars.charAt(i) - 32] = fontBMP.cropped(i * this.charWidth, 0, this.charWidth, this.charHeight);
		}
	}

	public void setFontBMP(Bitmap fontBMP) {
		this.fontBMP = fontBMP;
	}

	public int getCharWidth() {
		return charWidth;
	}

	public int getCharHeight() {
		return charHeight;
	}

	public void drawStringToBMP(Bitmap context, String string, Transform2d transform) {
		int strlen = string.length();
		Transform2d stringTransform = new Transform2d(transform);
		for(int i = 0; i < strlen; i++) {
			context.drawBMP(this.charBMPs[string.charAt(i) - 32], stringTransform);
			stringTransform.setPos(stringTransform.getPos().add(new Vector2f(this.charWidth * stringTransform.getScale().x, 0)));
		}
	}
}

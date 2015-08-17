package com.nox.engine.gfx2d;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.nox.engine.math2d.Transform2d;
import com.nox.engine.math2d.Vector2f;

public class Bitmap {
    private int width, height;
    private int[] pixels;

    public Bitmap(String fileName) {
	try {
	    BufferedImage image = ImageIO.read(new File(fileName));
	    this.width = image.getWidth();
	    this.height = image.getHeight();
	    this.pixels = new int[this.width * this.height];
	    image.getRGB(0, 0, this.width, this.height, this.pixels, 0,
		    this.width);
	} catch (IOException e) {
	    JOptionPane
		    .showMessageDialog(
			    null,
			    "Image file "
				    + fileName
				    + " was not found. Make sure you entered the file path and name correctly.");
	    e.printStackTrace();
	    System.exit(0);
	}
    }

    public Bitmap(int width, int height, int[] pixels) {
	this.width = width;
	this.height = height;
	this.pixels = pixels;
    }

    public Bitmap(int width, int height) {
	this.width = width;
	this.height = height;
	this.pixels = new int[width * height];
    }

    public Bitmap(Bitmap bmp) {
	this.width = bmp.width;
	this.height = bmp.height;
	this.pixels = bmp.pixels;
    }

    public Bitmap xFlipped() {
	int[] newPixels = new int[this.pixels.length];
	copyPixels(newPixels);

	for (int y = 0; y < this.height; y++) {
	    int yOffs = y * this.width;
	    for (int x = 0; x < this.width; x++) {
		newPixels[yOffs + ((this.width - 1) - x)] = this.pixels[yOffs
			+ x];
	    }
	}
	return new Bitmap(this.width, this.height, newPixels);
    }

    public Bitmap yFlipped() {
	int[] newPixels = new int[this.pixels.length];
	copyPixels(newPixels);

	for (int y = 0; y < this.height; y++) {
	    for (int x = 0; x < this.width; x++) {
		newPixels[((this.height - 1) - y) * this.width + x] = this.pixels[y
			* this.width + x];
	    }
	}
	return new Bitmap(this.width, this.height, newPixels);
    }

    public Bitmap CW90() {
	int[] newPixels = new int[this.pixels.length];
	copyPixels(newPixels);

	for (int y = 0; y < this.height; y++) {
	    for (int x = 0; x < this.width; x++) {
		int newX = y;
		int newY = -x;
		newPixels[y * this.width + x] = this.pixels[(newY + this.height - 1)
			* this.width + newX];
	    }
	}
	return new Bitmap(this.height, this.width, newPixels);
    }

    public Bitmap CCW90() {
	int[] newPixels = new int[this.pixels.length];
	copyPixels(newPixels);

	for (int y = 0; y < this.height; y++) {
	    for (int x = 0; x < this.width; x++) {
		int newX = -y;
		int newY = x;
		newPixels[y * this.width + x] = this.pixels[newY * this.width
			+ (newX + this.width - 1)];
	    }
	}
	return new Bitmap(this.height, this.width, newPixels);
    }

    public Bitmap scaled(float xScale, float yScale) {
	Bitmap scaledBMP = new Bitmap((int) (this.width * xScale),
		(int) (this.height * yScale));

	for (int y = 0; y < scaledBMP.getHeight(); y++) {
	    for (int x = 0; x < scaledBMP.getWidth(); x++) {
		int yCoord = (int) ((y * this.height) / (float) scaledBMP
			.getHeight());
		int xCoord = (int) ((x * this.width) / (float) scaledBMP
			.getWidth());
		int color = this.pixels[yCoord * this.width + xCoord];
		scaledBMP.setPixel(x, y, color);
	    }
	}

	return scaledBMP;
    }

    public Bitmap cropped(int xOffs, int yOffs, int width, int height) {
	int[] pixels = new int[width * height];
	for (int y = 0; y < height; y++) {
	    for (int x = 0; x < width; x++) {
		int pixIndex = y * width + x;
		int croppedPixIndex = (y + yOffs) * this.width + (x + xOffs);
		pixels[y * width + x] = this.pixels[croppedPixIndex];
	    }
	}
	return new Bitmap(width, height, pixels);
    }

    public Bitmap replacedColor(int toReplace, int replacement) {
	Bitmap replacedColorBMP = new Bitmap(this);
	for (int i = 0; i < this.pixels.length; i++) {
	    if (replacedColorBMP.getPixel(i) == toReplace) {
		replacedColorBMP.setPixel(i, replacement);
	    }
	}
	return replacedColorBMP;
    }

    public void drawBMP(Bitmap bmp, Transform2d transform) {
	Vector2f pos = transform.getPos();
	Vector2f scale = transform.getScale();

	float scaledWidth = bmp.getWidth() * scale.x;
	float scaledHeight = bmp.getHeight() * scale.y;

	int left = (int) (pos.x);
	int top = (int) (pos.y);
	int right = (int) (pos.x + scaledWidth);
	int bottom = (int) (pos.y + scaledHeight);

	int startPosX = left;
	int startPosY = top;
	int endPosX = right;
	int endPosY = bottom;

	if (startPosX < 0) {
	    startPosX = 0;
	}
	if (startPosY < 0) {
	    startPosY = 0;
	}
	if (endPosX > this.getWidth()) {
	    endPosX = this.getWidth();
	}
	if (endPosY > this.getHeight()) {
	    endPosY = this.getHeight();
	}

	int yDiff = bottom - top;
	int xDiff = right - left;

	float bmpAlphaYStep = (yDiff * bmp.getHeight() / scaledHeight) / yDiff;
	float bmpAlphaXStep = (xDiff * bmp.getWidth() / scaledWidth) / xDiff;

	float bmpAlphaYStart = (startPosY - top) * bmp.getHeight()
		/ scaledHeight;
	float bmpAlphaXStart = (startPosX - left) * bmp.getWidth()
		/ scaledWidth;

	float bmpAlphaX = bmpAlphaXStart;
	float bmpAlphaY = bmpAlphaYStart;

	int yCoord = startPosY * this.getWidth();
	for (int y = startPosY; y < endPosY; y++) {
	    for (int x = startPosX; x < endPosX; x++) {
		int backPixelIndex = yCoord + x;
		int color = bmp.getPixel((int) bmpAlphaX, (int) bmpAlphaY);
		int alphaBlendedColor = alphaBlend(this.pixels[backPixelIndex],
			color);
		this.pixels[backPixelIndex] = alphaBlendedColor;
		bmpAlphaX += bmpAlphaXStep;
	    }
	    yCoord += this.getWidth();
	    bmpAlphaX = bmpAlphaXStart;
	    bmpAlphaY += bmpAlphaYStep;
	}
    }

    public void drawRotatedBMP(Bitmap bmp, Transform2d transform, Vector2f pivot) {
	Vector2f pos = transform.getPos();
	Vector2f scale = transform.getScale();
	float radians = transform.getRot();

	float halfScaledWidth = bmp.getWidth() / 2.0f * scale.x;
	float halfScaledHeight = bmp.getHeight() / 2.0f * scale.y;

	Bitmap rotatedBMP = new Bitmap(bmp);
	float PI = (float) Math.PI;

	Vector2f topLeft = new Vector2f(-halfScaledWidth, -halfScaledHeight)
		.rotated(radians);
	Vector2f topRight = new Vector2f(halfScaledWidth, -halfScaledHeight)
		.rotated(radians);
	Vector2f bottomLeft = new Vector2f(-halfScaledWidth, halfScaledHeight)
		.rotated(radians);
	Vector2f bottomRight = new Vector2f(halfScaledWidth, halfScaledHeight)
		.rotated(radians);

	Vertex2d topLeftV = new Vertex2d(topLeft.add(pos), new Vector2f(0, 0));
	Vertex2d topRightV = new Vertex2d(topRight.add(pos), new Vector2f(
		bmp.getWidth(), 0));
	Vertex2d bottomLeftV = new Vertex2d(bottomLeft.add(pos), new Vector2f(
		0, bmp.getHeight()));
	Vertex2d bottomRightV = new Vertex2d(bottomRight.add(pos),
		new Vector2f(bmp.getWidth(), bmp.getHeight()));

	if (Math.abs(topRightV.screenPos.y - bottomLeftV.screenPos.y) > Math
		.abs(topLeftV.screenPos.y - bottomRightV.screenPos.y)) {
	    this.drawTriangle2D(topRightV, topLeftV, bottomLeftV, bmp);
	    this.drawTriangle2D(topRightV, bottomRightV, bottomLeftV, bmp);
	} else {
	    this.drawTriangle2D(topLeftV, topRightV, bottomRightV, bmp);
	    this.drawTriangle2D(topLeftV, bottomLeftV, bottomRightV, bmp);
	}
    }

    // TODO maybe make texture coordinates from 0 to 1
    // TODO maybe make all drawing from -1 to 1

    public void drawTriangle2D(Vertex2d v1, Vertex2d v2, Vertex2d v3,
	    Bitmap texture) {
	if (v1.screenPos.y > v2.screenPos.y) {
	    Vertex2d temp = v2;
	    v2 = v1;
	    v1 = temp;
	}
	if (v2.screenPos.y > v3.screenPos.y) {
	    Vertex2d temp = v2;
	    v2 = v3;
	    v3 = temp;
	}
	if (v1.screenPos.y > v2.screenPos.y) {
	    Vertex2d temp = v2;
	    v2 = v1;
	    v1 = temp;
	}

	Vertex2d yMin = v1;
	Vertex2d yMid = v2;
	Vertex2d yMax = v3;

	Vector2f p1 = yMin.screenPos;
	Vector2f p2 = yMax.screenPos;
	Vector2f p3 = yMid.screenPos;

	// use cross product to find which side yMid is on
	boolean isLeftHanded = ((p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y)
		* (p3.x - p1.x)) > 0;

	drawSortedTriangle2D(yMin, yMid, yMax, isLeftHanded, texture);
    }

    // TODO maybe step by adding, not by multiplying to improve performance
    // TODO maybe step texture coords in a way that it won't go outside the
    // texture
    // TODO use a fill convention to make things look smoother

    public void drawSortedTriangle2D(Vertex2d yMinV, Vertex2d yMidV,
	    Vertex2d yMaxV, boolean isLeftHanded, Bitmap texture) {
	// screenPos
	Vector2f yMinToyMid = yMidV.screenPos.sub(yMinV.screenPos);
	float yMinToyMidxStep = yMinToyMid.x / yMinToyMid.y;

	Vector2f yMidToyMax = yMaxV.screenPos.sub(yMidV.screenPos);
	float yMidToyMaxxStep = yMidToyMax.x / yMidToyMax.y;

	Vector2f yMinToyMax = yMaxV.screenPos.sub(yMinV.screenPos);
	float yMinToyMaxxStep = yMinToyMax.x / yMinToyMax.y;

	// texPos
	Vector2f yMinToyMidTex = yMidV.texPos.sub(yMinV.texPos);
	float yMinToyMidTexxStep = yMinToyMidTex.x / yMinToyMid.y;
	float yMinToyMidTexyStep = yMinToyMidTex.y / yMinToyMid.y;

	Vector2f yMidToyMaxTex = yMaxV.texPos.sub(yMidV.texPos);
	float yMidToyMaxTexxStep = yMidToyMaxTex.x / yMidToyMax.y;
	float yMidToyMaxTexyStep = yMidToyMaxTex.y / yMidToyMax.y;

	Vector2f yMinToyMaxTex = yMaxV.texPos.sub(yMinV.texPos);
	float yMinToyMaxTexxStep = yMinToyMaxTex.x / yMinToyMax.y;
	float yMinToyMaxTexyStep = yMinToyMaxTex.y / yMinToyMax.y;

	float xMin = yMinV.screenPos.x;
	float xMax = yMinV.screenPos.x;
	float yMin = yMinV.screenPos.y;
	float yMid = yMidV.screenPos.y;
	float yMax = yMaxV.screenPos.y;

	Vector2f minSideAlphaTexPos;
	Vector2f maxSideAlphaTexPos;
	Vector2f texPos;
	float xAlpha;

	int color;

	float yToyMin;
	float yToyMid;

	float endY;
	float endX;

	if (isLeftHanded) {
	    endY = yMax > this.getHeight() ? this.getHeight() : yMax;
	    for (float y = yMin < 0 ? 0 : yMin; y < endY; y++) {
		yToyMin = y - yMin;
		yToyMid = y - yMid;
		if (y < yMid) {
		    xMin = yMinV.screenPos.x + yToyMin * yMinToyMidxStep;
		    minSideAlphaTexPos = new Vector2f(yMinV.texPos.x
			    + yMinToyMidTexxStep * yToyMin, yMinV.texPos.y
			    + yMinToyMidTexyStep * yToyMin);
		} else {
		    xMin = yMidV.screenPos.x + yToyMid * yMidToyMaxxStep;
		    minSideAlphaTexPos = new Vector2f(yMidV.texPos.x
			    + yMidToyMaxTexxStep * yToyMid, yMidV.texPos.y
			    + yMidToyMaxTexyStep * yToyMid);
		}
		xMax = yMinV.screenPos.x + yToyMin * yMinToyMaxxStep;
		maxSideAlphaTexPos = new Vector2f(yMinV.texPos.x
			+ yMinToyMaxTexxStep * yToyMin, yMinV.texPos.y
			+ yMinToyMaxTexyStep * yToyMin);

		endX = xMax > this.getWidth() ? this.getWidth() : xMax;
		for (float x = xMin < 0 ? 0 : xMin; x < endX; x++) {
		    xAlpha = (x - xMin) / (xMax - xMin);

		    texPos = new Vector2f(minSideAlphaTexPos.x * (1 - xAlpha)
			    + maxSideAlphaTexPos.x * xAlpha,
			    minSideAlphaTexPos.y * (1 - xAlpha)
				    + maxSideAlphaTexPos.y * xAlpha);

		    if (texPos.x > texture.getWidth() - 1) {
			texPos.x = texture.getWidth() - 1;
		    }
		    if (texPos.y > texture.getHeight() - 1) {
			texPos.y = texture.getHeight() - 1;
		    }

		    color = texture.getPixel((int) texPos.x, (int) texPos.y);

		    int screenX = (int) (x);
		    int screenY = (int) (y);

		    int blendedColor = alphaBlend(
			    this.getPixel(screenX, screenY), color);

		    this.setPixel(screenX, screenY, blendedColor);
		}
	    }
	} else {
	    endY = yMax > this.getHeight() ? this.getHeight() : yMax;
	    for (float y = yMin < 0 ? 0 : yMin; y < endY; y++) {
		yToyMin = y - yMin;
		yToyMid = y - yMid;
		if (y < yMid) {
		    xMax = yMinV.screenPos.x + yToyMin * yMinToyMidxStep;
		    maxSideAlphaTexPos = new Vector2f(yMinV.texPos.x
			    + yMinToyMidTexxStep * yToyMin, yMinV.texPos.y
			    + yMinToyMidTexyStep * yToyMin);
		} else {
		    xMax = yMidV.screenPos.x + yToyMid * yMidToyMaxxStep;
		    maxSideAlphaTexPos = new Vector2f(yMidV.texPos.x
			    + yMidToyMaxTexxStep * yToyMid, yMidV.texPos.y
			    + yMidToyMaxTexyStep * yToyMid);
		}
		xMin = yMinV.screenPos.x + yToyMin * yMinToyMaxxStep;
		minSideAlphaTexPos = new Vector2f(yMinV.texPos.x
			+ yMinToyMaxTexxStep * yToyMin, yMinV.texPos.y
			+ yMinToyMaxTexyStep * yToyMin);

		endX = xMax > this.getWidth() ? this.getWidth() : xMax;
		for (float x = xMin < 0 ? 0 : xMin; x < endX; x++) {
		    xAlpha = (x - xMin) / (xMax - xMin);

		    texPos = new Vector2f(minSideAlphaTexPos.x * (1 - xAlpha)
			    + maxSideAlphaTexPos.x * xAlpha,
			    minSideAlphaTexPos.y * (1 - xAlpha)
				    + maxSideAlphaTexPos.y * xAlpha);

		    if (texPos.x > texture.getWidth() - 1) {
			texPos.x = texture.getWidth() - 1;
		    }
		    if (texPos.y > texture.getHeight() - 1) {
			texPos.y = texture.getHeight() - 1;
		    }

		    color = texture.getPixel((int) texPos.x, (int) texPos.y);

		    int screenX = (int) (x);
		    int screenY = (int) (y);

		    int blendedColor = alphaBlend(
			    this.getPixel(screenX, screenY), color);

		    this.setPixel(screenX, screenY, blendedColor);
		}
	    }
	}
    }

    public static int alphaBlend(int background, int color) {
	float alpha = (color >>> 24) / 255.0f;

	byte r = (byte) (((background << 8) >>> 24) * (1.0f - alpha) + ((color << 8) >>> 24)
		* alpha);
	byte g = (byte) (((background << 16) >>> 24) * (1.0f - alpha) + ((color << 16) >>> 24)
		* alpha);
	byte b = (byte) (((background << 24) >>> 24) * (1.0f - alpha) + ((color << 24) >>> 24)
		* alpha);

	return 0xFF000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public void clear(int color) {
	for (int x = 0; x < width; x++) {
	    for (int y = 0; y < height; y++) {
		this.pixels[x + y * width] = color;
	    }
	}
    }

    public void copyPixels(int[] pixels) {
	System.arraycopy(this.pixels, 0, pixels, 0, pixels.length);
    }

    public int getPixel(int x, int y) {
	return this.pixels[x + y * width];
    }

    public int getPixel(int i) {
	return this.pixels[i];
    }

    public void setPixel(int x, int y, int color) {
	this.pixels[x + y * width] = color;
    }

    public void setPixel(int i, int color) {
	this.pixels[i] = color;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }
}

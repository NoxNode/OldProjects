package com.bme.zombierunner;

import android.graphics.Point;

public class Settings {

	public static int jStickMove = 10, pointerScale = 5, pointerSideScale = 10,
			dSpeed = 6, dSize = 8, dBH = 200, scale = 0, speed = dSpeed,
			BOTHEIGHT = 200, jStickRad = 20, miniMapScale = 5,
			currentSize = 10, SCREENWIDTH = 800, SCREENHEIGHT = 600,
			lifeLevel = 5, starLevel = 15, iceLevel = 10, enemyLevel = 1,
			lenemyLevel = 10, fireLevel = 5, personLevel = 2, hWaterLevel = 12,
			lifeAppear = 216, starAppear = 180, iceAppear = 195,
			hWaterAppear = 185, starATime = 400, iceATime = 400,
			fireATime = 400, pTurn = 40, leTurn = 80, hWaterSpeed = 5,
			hWaterRad = 200, pCoin = 3;

	public static final int EASY = 0, NORMAL = 1, HARD = 2, CUSTOM = 3;
	public static int difficulty = Settings.NORMAL;

	public static boolean lifeOn = true, invulnOn = true, iceOn = true,
			hWaterOn = true, enemyOn = true, lenemyOn = true, fireOn = true,
			personOn = true;

	public static String settings = "";

	public static Point[] sizes = { new Point(240, 320), new Point(240, 400),
			new Point(240, 432), new Point(320, 480), new Point(360, 640),
			new Point(480, 640), new Point(400, 800), new Point(480, 800),
			new Point(600, 800), new Point(480, 854), new Point(540, 960),
			new Point(640, 960), new Point(600, 1024), new Point(768, 1024),
			new Point(800, 1216), new Point(720, 1280), new Point(768, 1280),
			new Point(800, 1280), new Point(768, 1366), new Point(900, 1440),
			new Point(1152, 1536), new Point(1080, 1920),
			new Point(1152, 1920), new Point(1200, 1920),
			new Point(1536, 2048), new Point(1536, 2560), new Point(1600, 2560) };
	public static Point WSize = sizes[currentSize];

	public static int jStickCenter = WSize.x / 2;
	public static int djStickCenter = jStickCenter;

	public static void defaults() {

		for(int i = 0; i < Settings.sizes.length - 1; i++) {
			if(Settings.SCREENWIDTH == Settings.sizes[i].x
					&& Settings.SCREENHEIGHT == Settings.sizes[i].y) {
				Settings.currentSize = i;
				Settings.WSize.x = Settings.SCREENWIDTH;
				Settings.WSize.y = Settings.SCREENHEIGHT;
				Settings.dSize = i;
				break;
			}
		}
		Settings.scale = Settings.WSize.x * Settings.WSize.y;

		Settings.dBH = Settings.scale / 2592;
		Settings.BOTHEIGHT = Settings.dBH;
		Settings.dSpeed = Settings.scale / 86400;
		Settings.speed = Settings.dSpeed;
		Settings.jStickRad = Settings.scale / 25920;
		Settings.jStickCenter = Settings.sizes[Settings.currentSize].x / 2;
		Settings.djStickCenter = Settings.jStickCenter;
		Settings.difficulty = 1;

		Settings.speed = Settings.dSpeed;
		Settings.BOTHEIGHT = Settings.dBH;
		Settings.jStickRad = Settings.scale / 25920;
		Settings.miniMapScale = 5;
		Settings.currentSize = Settings.dSize;
		Settings.WSize = Settings.sizes[Settings.currentSize];
		Settings.jStickCenter = Settings.djStickCenter;
		Settings.difficulty = 1;

		Settings.lifeOn = true;
		Settings.invulnOn = true;
		Settings.iceOn = true;
		Settings.hWaterOn = true;
		Settings.enemyOn = true;
		Settings.lenemyOn = true;
		Settings.personOn = true;
		Settings.fireOn = true;

		Settings.lifeLevel = 5;
		Settings.starLevel = 15;
		Settings.iceLevel = 10;
		Settings.hWaterLevel = 12;
		Settings.lifeAppear = (int) (Settings.scale / 2400.0);
		Settings.iceAppear = (int) (Settings.scale / 2658.4615);
		Settings.starAppear = (int) (Settings.scale / 2880.0);
		Settings.hWaterAppear = (int) (Settings.scale / 2802.1621);
		Settings.starATime = (int) (Settings.scale / 1296.0);
		Settings.iceATime = (int) (Settings.scale / 1296.0);
		Settings.hWaterRad = (int) (Settings.scale / 2592.0);
		Settings.hWaterSpeed = (int) (Settings.scale / 51840.0);

		Settings.enemyLevel = 1;
		Settings.lenemyLevel = 10;
		Settings.personLevel = 2;
		Settings.fireLevel = 5;
		Settings.pTurn = 40;
		Settings.leTurn = 80;
		Settings.fireATime = Settings.scale / 1296;
		Settings.pCoin = 3;
	}

	public static void saveSettings() {

	}
}

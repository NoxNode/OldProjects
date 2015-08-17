package com.bme.zombierunner;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.bme.zombierunner.entities.Coin;
import com.bme.zombierunner.entities.Door;
import com.bme.zombierunner.entities.Enemy;
import com.bme.zombierunner.entities.Fire;
import com.bme.zombierunner.entities.FireEnemy;
import com.bme.zombierunner.entities.HolyWater;
import com.bme.zombierunner.entities.Ice;
import com.bme.zombierunner.entities.LEnemy;
import com.bme.zombierunner.entities.Life;
import com.bme.zombierunner.entities.Person;
import com.bme.zombierunner.entities.Player;
import com.bme.zombierunner.entities.Revive;
import com.bme.zombierunner.entities.Star;

public class ZRSurface extends SurfaceView implements Runnable {

	SurfaceHolder myHolder;
	Thread myThread = null;
	boolean isRunning = false;

	public void pause() {
		isRunning = false;
		try {
			myThread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		myThread = null;
	}

	public void resume() {
		One.input1[One.mx] = 0;
		One.input1[One.my] = 0;
		One.input2[One.mx] = 0;
		One.input2[One.my] = 0;

		mx1 = 0;
		my1 = 0;
		mx2 = 0;
		my2 = 0;

		isRunning = true;
		myThread = new Thread(this);
		myThread.start();
	}

	/***************************************** Start Game Specific Vars ********************************************/

	Bitmap img_player = BitmapFactory.decodeResource(getResources(),
			R.drawable.faceone);
	Bitmap img_coin = BitmapFactory.decodeResource(getResources(),
			R.drawable.coin);
	Bitmap img_enemy = BitmapFactory.decodeResource(getResources(),
			R.drawable.enemy);
	Bitmap img_lenemy = BitmapFactory.decodeResource(getResources(),
			R.drawable.lenemy);
	Bitmap img_life = BitmapFactory.decodeResource(getResources(),
			R.drawable.life);
	Bitmap img_star = BitmapFactory.decodeResource(getResources(),
			R.drawable.star);
	Bitmap img_ice = BitmapFactory.decodeResource(getResources(),
			R.drawable.ice);
	Bitmap img_fire = BitmapFactory.decodeResource(getResources(),
			R.drawable.fire);
	Bitmap img_person = BitmapFactory.decodeResource(getResources(),
			R.drawable.person);
	Bitmap img_revive = BitmapFactory.decodeResource(getResources(),
			R.drawable.revive);
	Bitmap img_door = BitmapFactory.decodeResource(getResources(),
			R.drawable.door);
	Bitmap img_skull = BitmapFactory.decodeResource(getResources(),
			R.drawable.skull);
	Bitmap img_hWater = BitmapFactory.decodeResource(getResources(),
			R.drawable.hwater);

	Paint White = new Paint();
	Paint Grey = new Paint();
	Paint Black = new Paint();
	Paint Blue = new Paint();
	Paint Yellow = new Paint();
	Paint Red = new Paint();
	Paint Green = new Paint();
	Paint StarYellow = new Paint();
	Paint IceBlue = new Paint();
	Paint FireRed = new Paint();
	Paint PersonYellow = new Paint();
	Paint Brown = new Paint();

	int GHEIGHT = Settings.SCREENHEIGHT - Settings.BOTHEIGHT;
	int img_len = img_player.getWidth();
	int p_changeDir = 0;
	int e_changeDir = 0;
	int le_changeDir = 0;
	int f_changeDir = 0;

	float lsx1 = 0, lsy1 = 0;
	float lsx2 = 0, lsy2 = 0;

	public static boolean touching1 = false, touching2 = false,
			clicked1 = false, clicked2 = false;
	public static float mx1 = 0, my1 = 0, mx2 = 0, my2 = 0, sx1 = 0, sy1 = 0,
			sx2 = 0, sy2 = 0;
	int GTOP = 0;

	public static boolean gameOver = false, paused = false;

	public static boolean hWaterThrown = false;

	Player one = new Player(true);
	Player two = new Player(false);
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<LEnemy> lenemies = new ArrayList<LEnemy>();
	Coin coin = new Coin(0, 0);

	Life life = new Life(0, 0);
	Star star = new Star(0, 0);
	Ice ice = new Ice(0, 0);
	Fire fire = new Fire();
	ArrayList<FireEnemy> fires = new ArrayList<FireEnemy>();
	ArrayList<Person> people = new ArrayList<Person>();
	Revive rev = new Revive(0, 0);
	Door door = new Door(img_len);
	ArrayList<HolyWater> hWaters = new ArrayList<HolyWater>();

	/***************************************** End Game Specific Vars ********************************************/

	public ZRSurface(Context context) {
		super(context);
		myHolder = getHolder();

		/***************************************** Start Game Specific Init ********************************************/

		White.setARGB(255, 255, 255, 255);
		if(Settings.SCREENHEIGHT / 32 >= 15)
			White.setTextSize(Settings.SCREENHEIGHT / 32);
		else
			White.setTextSize(15);
		White.setTextAlign(Align.CENTER);
		Grey.setARGB(255, 150, 150, 150);
		Black.setARGB(255, 0, 0, 0);
		if(Settings.BOTHEIGHT / 10 >= 10)
			Black.setTextSize(Settings.BOTHEIGHT / 10);
		else
			Black.setTextSize(10);
		Black.setTextAlign(Align.LEFT);
		Blue.setARGB(255, 50, 50, 255);
		Yellow.setARGB(255, 255, 255, 0);
		Red.setARGB(255, 255, 50, 50);
		Green.setARGB(255, 50, 255, 50);
		StarYellow.setARGB(255, 100, 100, 0);
		IceBlue.setARGB(255, 0, 0, 125);
		FireRed.setARGB(255, 125, 0, 0);
		PersonYellow.setARGB(255, 150, 150, 0);
		Brown.setARGB(255, 150, 100, 50);

		coin.x = (int) (Math.random() * (Settings.WSize.x - img_len));
		coin.y = (int) (Math.random() * (Settings.WSize.y - img_len));

		life.x = (int) (Math.random() * (Settings.WSize.x - img_len));
		life.y = (int) (Math.random() * (Settings.WSize.y - img_len));

		star.x = (int) (Math.random() * (Settings.WSize.x - img_len));
		star.y = (int) (Math.random() * (Settings.WSize.y - img_len));

		ice.x = (int) (Math.random() * (Settings.WSize.x - img_len));
		ice.y = (int) (Math.random() * (Settings.WSize.y - img_len));

		/***************************************** End Game Specific Init ********************************************/
	}

	@Override
	public void run() {
		while(isRunning) {
			try {
				Thread.sleep(16);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			if(!myHolder.getSurface().isValid())
				continue;

			// Two Player Input

			if(Two.getInput1(Two.my) > Settings.SCREENHEIGHT / 2
					&& Two.touching1) {
				sx1 = Two.getInput1(Two.sx);
				sy1 = Two.getInput1(Two.sy);
				mx1 = Two.getInput1(Two.mx);
				my1 = Two.getInput1(Two.my);
				clicked1 = Two.clicked1;
				touching1 = true;
			}
			else if(Two.getInput2(Two.my) > Settings.SCREENHEIGHT / 2
					&& Two.touching2) {
				sx1 = Two.getInput2(Two.sx);
				sy1 = Two.getInput2(Two.sy);
				mx1 = Two.getInput2(Two.mx);
				my1 = Two.getInput2(Two.my);
				clicked1 = Two.clicked2;
				touching1 = true;
			}
			else {
				touching1 = false;
			}
			if(Two.getInput1(Two.my) < Settings.SCREENHEIGHT / 2
					&& Two.touching1) {
				sx2 = Two.getInput1(Two.sx);
				sy2 = Two.getInput1(Two.sy);
				mx2 = Two.getInput1(Two.mx);
				my2 = Two.getInput1(Two.my);
				clicked2 = Two.clicked1;
				touching2 = true;
			}
			else if(Two.getInput2(Two.my) < Settings.SCREENHEIGHT / 2
					&& Two.touching2) {
				sx2 = Two.getInput2(Two.sx);
				sy2 = Two.getInput2(Two.sy);
				mx2 = Two.getInput2(Two.mx);
				my2 = Two.getInput2(Two.my);
				clicked2 = Two.clicked2;
				touching2 = true;
			}
			else {
				touching2 = false;
			}

			if(Two.twoPlayer)
				GTOP = Settings.SCREENHEIGHT / 2;

			if(!Two.twoPlayer) {
				if(lsx1 != One.getInput(One.sx) || lsy1 != One.getInput(One.sy)) {
					lsx1 = One.getInput(One.sx);
					lsy1 = One.getInput(One.sy);
					One.clicked1 = true;
				}
				if(lsx2 != One.getInput2(One.sx)
						|| lsy2 != One.getInput2(One.sy)) {
					lsx2 = One.getInput2(One.sx);
					lsy2 = One.getInput2(One.sy);
					One.clicked2 = true;
				}
			}
			else {
				if(lsx1 != sx1 || lsy1 != sy1) {
					lsx1 = sx1;
					lsy1 = sy1;
					clicked1 = true;
				}
				if(lsx2 != sx2 || lsy2 != sy2) {
					lsx2 = sx2;
					lsy2 = sy2;
					clicked2 = true;
				}
			}

			/*********************************** Start Update ********************************************/

			Rect bounds = new Rect();
			Black.getTextBounds("0", 0, "0".length(), bounds);
			int buttonPadding = 5;
			int buttonTop = GHEIGHT + bounds.height() * 3 / 2 + buttonPadding;
			int buttonRight = buttonPadding + Settings.SCREENWIDTH / 4;
			int buttonBottom = Settings.SCREENHEIGHT - buttonPadding;
			int buttonLeft = buttonPadding;
			int buttonWidth = buttonRight - buttonLeft;
			int buttonHeight = buttonBottom - buttonTop;
			// GUI
			if((Two.twoPlayer && ((clicked1 && mx1 > buttonLeft
					&& mx1 < buttonRight && my1 > buttonTop && my1 < buttonBottom) || (clicked2
					&& mx2 > Settings.SCREENWIDTH - buttonPadding
							- Settings.SCREENWIDTH / 4
					&& mx2 < Settings.SCREENWIDTH - buttonPadding
					&& my2 > buttonPadding && my2 < Settings.BOTHEIGHT
					- bounds.height() * 3 / 2 - buttonPadding)))
					|| (One.clicked1 && One.getInput(One.mx) > buttonLeft
							&& One.getInput(One.mx) < buttonRight
							&& One.getInput(One.my) > buttonTop && One
							.getInput(One.my) < buttonBottom)) {
				if(paused)
					paused = false;
				else if(gameOver) {
					gameOver = false;
					coin.score = 0;
					one.saved = 0;
					one.x = 0;
					one.y = 0;
					one.camx = 0;
					one.camy = 0;
					one.dead = false;
					one.hWater = 0;
					two.x = 0;
					two.y = 0;
					two.camx = 0;
					two.camy = 0;
					two.dead = false;
					two.hWater = 0;
					enemies.clear();
					lenemies.clear();
					fires.clear();
					people.clear();
					fire.fired = false;
					fire.atime = 0;
					fire.active = false;
					ice.iced = false;
					ice.atime = 0;
					ice.active = false;
					hWaters.clear();
				}
				else
					paused = true;
			}

			// Entities
			if(!gameOver && !paused) {
				if(!one.dead)
					one.Update(GHEIGHT, img_len, GTOP);
				if(Two.twoPlayer && !two.dead)
					two.Update(GHEIGHT, img_len, GTOP);
				coin.Update(one, two, enemies, lenemies, fires, people, fire,
						life, star, ice, hWaters, img_len);
				life.Update(one, two, img_len, coin.score);
				star.Update(one, two, img_len, coin.score);
				rev.Update(one, two, img_len);

				for(int i = 0; i < hWaters.size(); i++) {
					if(hWaters.get(i).Update(one, two, enemies, lenemies,
							people, fire.fired, img_len, coin.score)) {
						hWaters.remove(i);
					}
				}
				hWaterThrown = false;

				door.Update(one, two, people, img_len);

				ice.Update(one, two, enemies, lenemies, fire, img_len,
						coin.score);
				fire.Update(one, enemies, lenemies, img_len);
				if(!fire.collected && coin.score % Settings.fireLevel == 0
						&& coin.score != 0) {
					for(int i = 0; i < fires.size(); i++) {
						fires.get(i).Update(one, two, ice, fire, img_len,
								coin.score);
					}
				}
				for(int i = 0; i < people.size(); i++) {
					if(people.get(i).Update(one, two, coin, people, enemies,
							lenemies, img_len, fire.fired)) {
						people.remove(i);
						i--;
					}
				}
				for(int i = 0; i < enemies.size(); i++) {
					if(!ice.iced)
						enemies.get(i).Update(img_len, fire.fired);
					if(!one.invuln
							&& Math.sqrt((one.x - enemies.get(i).x)
									* (one.x - enemies.get(i).x)
									+ (one.y - enemies.get(i).y)
									* (one.y - enemies.get(i).y)) < img_len) {
						if(one.lives == 0 && !(i == one.nHit))
							one.dead = true;
						else if(!one.hit) {
							one.hit = true;
							one.nHit = i;
							one.lives--;
						}
					}
					else if(i == one.nHit) {
						one.hit = false;
						one.nHit = -1;
					}
					if(Two.twoPlayer
							&& !two.invuln
							&& Math.sqrt((two.x - enemies.get(i).x)
									* (two.x - enemies.get(i).x)
									+ (two.y - enemies.get(i).y)
									* (two.y - enemies.get(i).y)) < img_len) {
						if(two.lives == 0 && !(i == two.nHit))
							two.dead = true;
						else if(!two.hit) {
							two.hit = true;
							two.nHit = i;
							two.lives--;
						}
					}
					else if(i == two.nHit) {
						two.hit = false;
						two.nHit = -1;
					}
				}
				for(int i = 0; i < lenemies.size(); i++) {
					if(!ice.iced)
						lenemies.get(i).Update(one, two, img_len, fire.fired);
					if(!one.invuln
							&& Math.sqrt((one.x - lenemies.get(i).x - img_len / 2)
									* (one.x - lenemies.get(i).x - img_len / 2)
									+ (one.y - lenemies.get(i).y - img_len / 2)
									* (one.y - lenemies.get(i).y - img_len / 2)) < img_len * 3 / 2) {
						if(one.lives < 2 && !(i == one.lnHit)) {
							one.dead = true;
							one.lives = 0;
						}
						else if(!one.lhit) {
							one.lhit = true;
							one.lnHit = i;
							one.lives -= 2;
						}
					}
					else if(i == one.lnHit) {
						one.lhit = false;
						one.lnHit = -1;
					}
					if(!two.invuln
							&& Math.sqrt((two.x - lenemies.get(i).x - img_len / 2)
									* (two.x - lenemies.get(i).x - img_len / 2)
									+ (two.y - lenemies.get(i).y - img_len / 2)
									* (two.y - lenemies.get(i).y - img_len / 2)) < img_len * 3 / 2) {
						if(two.lives < 2 && !(i == two.lnHit)) {
							two.dead = true;
							two.lives = 0;
						}
						else if(!two.lhit) {
							two.lhit = true;
							two.lnHit = i;
							two.lives -= 2;
						}
					}
					else if(i == two.lnHit) {
						two.lhit = false;
						two.lnHit = -1;
					}
				}
				if(Two.twoPlayer && one.dead && two.dead)
					gameOver = true;
				if(!Two.twoPlayer && one.dead)
					gameOver = true;
			}

			float[] pts = {
					-one.camx,
					-one.camy + GTOP,
					Settings.WSize.x - one.camx - 1,
					-one.camy + GTOP, // ln 1

					Settings.WSize.x - one.camx - 1,
					-one.camy + GTOP,
					Settings.WSize.x - one.camx - 1,
					Settings.WSize.y - one.camy + GTOP - 1, // ln 2

					Settings.WSize.x - one.camx - 1,
					Settings.WSize.y - one.camy + GTOP - 1,
					-one.camx,
					Settings.WSize.y - one.camy + GTOP - 1, // ln 3

					-one.camx, Settings.WSize.y - one.camy + GTOP - 1,
					-one.camx, -one.camy + GTOP, // ln 4
			};

			float[] map_pts = {
					0,
					GTOP, // p1
					Settings.WSize.x / Settings.miniMapScale,
					GTOP, // p2

					Settings.WSize.x / Settings.miniMapScale,
					GTOP, // p2
					Settings.WSize.x / Settings.miniMapScale,
					Settings.WSize.y / Settings.miniMapScale + GTOP, // p3

					Settings.WSize.x / Settings.miniMapScale,
					Settings.WSize.y / Settings.miniMapScale + GTOP, // p3
					0, Settings.WSize.y / Settings.miniMapScale + GTOP, // p4

					0, Settings.WSize.y / Settings.miniMapScale + GTOP, // p4
					0, GTOP // p1
			};

			/*********************************** End Update ********************************************/

			Canvas canvas = myHolder.lockCanvas();
			canvas.drawRGB(0, 0, 0);

			/**************************************** Start Draw *********************************************/

			// World Bounds
			canvas.drawLines(pts, White);

			// Two Play Split Screen
			if(Two.twoPlayer)
				canvas.drawLine(0, Settings.SCREENHEIGHT / 2,
						Settings.SCREENWIDTH, Settings.SCREENHEIGHT / 2, Blue);

			// Coin pointer
			float a = coin.y - one.y;
			float b = coin.x - one.x;

			if(!(b == 0)) {
				float pointerAngle = (float) Math.atan(a / b);
				float pointer1x = (float) (Math.cos(pointerAngle - 10) * Settings.pointerSideScale);
				float pointer1y = (float) (Math.sin(pointerAngle - 10) * Settings.pointerSideScale);

				float pointer2x = (float) (Math.cos(pointerAngle + 10) * Settings.pointerSideScale);
				float pointer2y = (float) (Math.sin(pointerAngle + 10) * Settings.pointerSideScale);

				if(b > 0) {
					canvas.drawLine(
							one.x
									+ img_len
									/ 2
									- one.camx
									+ (float) (Math.cos(pointerAngle) * img_len),
							one.y
									+ img_len
									/ 2
									- one.camy
									+ GTOP
									+ (float) (Math.sin(pointerAngle) * img_len),
							one.x + img_len / 2 + (coin.x - one.x)
									/ Settings.pointerScale - one.camx, one.y
									+ img_len / 2 + (coin.y - one.y)
									/ Settings.pointerScale - one.camy + GTOP,
							White);

					canvas.drawLine(one.x + img_len / 2 + (coin.x - one.x)
							/ Settings.pointerScale - one.camx, one.y + img_len
							/ 2 + (coin.y - one.y) / Settings.pointerScale
							- one.camy + GTOP, one.x + img_len / 2
							+ (coin.x - one.x) / Settings.pointerScale
							- one.camx + pointer1x, one.y + img_len / 2
							+ (coin.y - one.y) / Settings.pointerScale
							- one.camy + GTOP + pointer1y, White);
					canvas.drawLine(one.x + img_len / 2 + (coin.x - one.x)
							/ Settings.pointerScale - one.camx, one.y + img_len
							/ 2 + (coin.y - one.y) / Settings.pointerScale
							- one.camy + GTOP, one.x + img_len / 2
							+ (coin.x - one.x) / Settings.pointerScale
							- one.camx + pointer2x, one.y + img_len / 2
							+ (coin.y - one.y) / Settings.pointerScale
							- one.camy + GTOP + pointer2y, White);
				}
				if(b < 0) {
					canvas.drawLine(
							one.x
									+ img_len
									/ 2
									- one.camx
									- (float) (Math.cos(pointerAngle) * img_len),
							one.y
									+ img_len
									/ 2
									- one.camy
									+ GTOP
									- (float) (Math.sin(pointerAngle) * img_len),
							one.x + img_len / 2 + (coin.x - one.x)
									/ Settings.pointerScale - one.camx, one.y
									+ img_len / 2 + (coin.y - one.y)
									/ Settings.pointerScale - one.camy + GTOP,
							White);

					canvas.drawLine(one.x + img_len / 2 + (coin.x - one.x)
							/ Settings.pointerScale - one.camx, one.y + img_len
							/ 2 + (coin.y - one.y) / Settings.pointerScale
							- one.camy + GTOP, one.x + img_len / 2
							+ (coin.x - one.x) / Settings.pointerScale
							- one.camx - pointer1x, one.y + img_len / 2
							+ (coin.y - one.y) / Settings.pointerScale
							- one.camy + GTOP - pointer1y, White);
					canvas.drawLine(one.x + img_len / 2 + (coin.x - one.x)
							/ Settings.pointerScale - one.camx, one.y + img_len
							/ 2 + (coin.y - one.y) / Settings.pointerScale
							- one.camy + GTOP, one.x + img_len / 2
							+ (coin.x - one.x) / Settings.pointerScale
							- one.camx - pointer2x, one.y + img_len / 2
							+ (coin.y - one.y) / Settings.pointerScale
							- one.camy + GTOP - pointer2y, White);
				}
			}
			else if(a > 0) {
				canvas.drawLine(one.x + img_len / 2 - one.camx, one.y + img_len
						/ 2 - one.camy + GTOP + img_len, one.x + img_len / 2
						+ (coin.x - one.x) / Settings.pointerScale - one.camx,
						one.y + img_len / 2 + (coin.y - one.y)
								/ Settings.pointerScale - one.camy + GTOP,
						White);

				canvas.drawLine(one.x + img_len / 2 + (coin.x - one.x)
						/ Settings.pointerScale - one.camx, one.y + img_len / 2
						+ (coin.y - one.y) / Settings.pointerScale - one.camy
						+ GTOP, one.x + img_len / 2 + (coin.x - one.x)
						/ Settings.pointerScale - one.camx, one.y + img_len / 2
						+ (coin.y - one.y) / Settings.pointerScale - one.camy
						+ GTOP + Settings.pointerSideScale, White);
				canvas.drawLine(one.x + img_len / 2 + (coin.x - one.x)
						/ Settings.pointerScale - one.camx, one.y + img_len / 2
						+ (coin.y - one.y) / Settings.pointerScale - one.camy
						+ GTOP, one.x + img_len / 2 + (coin.x - one.x)
						/ Settings.pointerScale - one.camx, one.y + img_len / 2
						+ (coin.y - one.y) / Settings.pointerScale - one.camy
						+ GTOP + Settings.pointerSideScale, White);
			}
			else if(a < 0) {
				canvas.drawLine(one.x + img_len / 2 - one.camx, one.y + img_len
						/ 2 - one.camy + GTOP + img_len, one.x + img_len / 2
						+ (coin.x - one.x) / Settings.pointerScale - one.camx,
						one.y + img_len / 2 + (coin.y - one.y)
								/ Settings.pointerScale - one.camy + GTOP,
						White);

				canvas.drawLine(one.x + img_len / 2 + (coin.x - one.x)
						/ Settings.pointerScale - one.camx, one.y + img_len / 2
						+ (coin.y - one.y) / Settings.pointerScale - one.camy
						+ GTOP, one.x + img_len / 2 + (coin.x - one.x)
						/ Settings.pointerScale - one.camx, one.y + img_len / 2
						+ (coin.y - one.y) / Settings.pointerScale - one.camy
						+ GTOP - Settings.pointerSideScale, White);
				canvas.drawLine(one.x + img_len / 2 + (coin.x - one.x)
						/ Settings.pointerScale - one.camx, one.y + img_len / 2
						+ (coin.y - one.y) / Settings.pointerScale - one.camy
						+ GTOP, one.x + img_len / 2 + (coin.x - one.x)
						/ Settings.pointerScale - one.camx, one.y + img_len / 2
						+ (coin.y - one.y) / Settings.pointerScale - one.camy
						+ GTOP - Settings.pointerSideScale, White);
			}

			// Entities
			for(int i = 0; i < hWaters.size(); i++) {
				if(hWaters.get(i).blast_frames > 0)
					canvas.drawCircle(hWaters.get(i).x + img_len / 2,
							hWaters.get(i).y + img_len / 2, Settings.hWaterRad,
							Blue);
			}
			if(!one.dead) {
				if(!(one.invuln && star.itime % 20 < 10)
						&& star.itime < Settings.starATime * 4 / 5)
					canvas.drawBitmap(img_player, one.x - one.camx, one.y
							- one.camy + GTOP, null);
				if(!(one.invuln && star.itime % 7 < 3)
						&& star.itime > Settings.starATime * 4 / 5)
					canvas.drawBitmap(img_player, one.x - one.camx, one.y
							- one.camy + GTOP, null);
			}
			else
				canvas.drawBitmap(img_skull, one.x - one.camx, one.y - one.camy
						+ GTOP, null);

			if(Two.twoPlayer) {
				if(!two.dead) {
					if(!(two.invuln && star.itime % 20 < 10)
							&& star.itime < Settings.starATime * 4 / 5)
						canvas.drawBitmap(img_player, two.x - one.camx, two.y
								- one.camy + GTOP, null);
					if(!(two.invuln && star.itime % 7 < 3)
							&& star.itime > Settings.starATime * 4 / 5)
						canvas.drawBitmap(img_player, two.x - one.camx, two.y
								- one.camy + GTOP, null);
				}
				else
					canvas.drawBitmap(img_skull, two.x - one.camx, two.y
							- one.camy + GTOP, null);
			}

			canvas.drawBitmap(img_coin, coin.x - one.camx, coin.y - one.camy
					+ GTOP, null);

			if(!(ice.iced && ice.atime % 7 > 3 && ice.atime > Settings.starATime * 4 / 5)) {
				for(int i = 0; i < enemies.size(); i++)
					canvas.drawBitmap(img_enemy, enemies.get(i).x - one.camx,
							enemies.get(i).y - one.camy + GTOP, null);
				for(int i = 0; i < lenemies.size(); i++)
					canvas.drawBitmap(img_lenemy, lenemies.get(i).x - one.camx,
							lenemies.get(i).y - one.camy + GTOP, null);
			}

			if(!life.collected && life.time < Settings.lifeAppear
					&& coin.score % Settings.lifeLevel == 0 && coin.score != 0)
				canvas.drawBitmap(img_life, life.x - one.camx, life.y
						- one.camy + GTOP, null);
			if(Two.twoPlayer && (one.dead || two.dead))
				canvas.drawBitmap(img_revive, rev.x - one.camx, rev.y
						- one.camy + GTOP, null);
			if(!star.collected && star.time < Settings.starAppear
					&& coin.score % Settings.starLevel == 0 && coin.score != 0)
				canvas.drawBitmap(img_star, star.x - one.camx, star.y
						- one.camy + GTOP, null);
			if(!ice.collected && ice.time < Settings.iceAppear
					&& coin.score % Settings.iceLevel == 0 && coin.score != 0)
				canvas.drawBitmap(img_ice, ice.x - one.camx, ice.y - one.camy
						+ GTOP, null);

			for(int i = 0; i < hWaters.size(); i++) {
				if((!hWaters.get(i).collected
						&& hWaters.get(i).time < Settings.hWaterAppear
						&& coin.score % Settings.hWaterLevel == 0 && coin.score != 0)
						|| hWaters.get(i).thrown)
					canvas.drawBitmap(img_hWater, hWaters.get(i).x - one.camx,
							hWaters.get(i).y - one.camy + GTOP, null);
			}

			if(!fire.collected && coin.score % Settings.fireLevel == 0
					&& coin.score != 0)
				for(int i = 0; i < fires.size(); i++)
					canvas.drawBitmap(img_fire, fires.get(i).x - one.camx,
							fires.get(i).y - one.camy + GTOP, null);
			boolean saved = false;
			for(int i = 0; i < people.size(); i++) {
				canvas.drawBitmap(img_person, people.get(i).x - one.camx,
						people.get(i).y - one.camy + GTOP, null);
				if(people.get(i).saved1 || people.get(i).saved2)
					saved = true;
			}
			if(saved)
				canvas.drawBitmap(img_door, door.x - one.camx, door.y
						- one.camy + GTOP, null);

			// Mini Map
			canvas.drawLines(map_pts, White);
			canvas.drawRect((one.x) / Settings.miniMapScale, (one.y)
					/ Settings.miniMapScale + GTOP, (one.x + img_len)
					/ Settings.miniMapScale, (one.y + img_len)
					/ Settings.miniMapScale + GTOP, Blue);
			canvas.drawRect((two.x) / Settings.miniMapScale, (two.y)
					/ Settings.miniMapScale + GTOP, (two.x + img_len)
					/ Settings.miniMapScale, (two.y + img_len)
					/ Settings.miniMapScale + GTOP, Blue);
			canvas.drawRect((coin.x) / Settings.miniMapScale, (coin.y)
					/ Settings.miniMapScale + GTOP, (coin.x + img_len)
					/ Settings.miniMapScale, (coin.y + img_len)
					/ Settings.miniMapScale + GTOP, Yellow);
			for(int i = 0; i < enemies.size(); i++)
				canvas.drawRect((enemies.get(i).x) / Settings.miniMapScale,
						(enemies.get(i).y) / Settings.miniMapScale + GTOP,
						(enemies.get(i).x + img_len) / Settings.miniMapScale,
						(enemies.get(i).y + img_len) / Settings.miniMapScale
								+ GTOP, Red);
			for(int i = 0; i < lenemies.size(); i++)
				canvas.drawRect((lenemies.get(i).x) / Settings.miniMapScale,
						(lenemies.get(i).y) / Settings.miniMapScale + GTOP,
						(lenemies.get(i).x + img_len * 2)
								/ Settings.miniMapScale,
						(lenemies.get(i).y + img_len * 2)
								/ Settings.miniMapScale + GTOP, Red);

			if(Settings.lifeOn && !life.collected
					&& life.time < Settings.lifeAppear
					&& coin.score % Settings.lifeLevel == 0 && coin.score != 0)
				canvas.drawRect((life.x) / Settings.miniMapScale, (life.y)
						/ Settings.miniMapScale + GTOP, (life.x + img_len)
						/ Settings.miniMapScale, (life.y + img_len)
						/ Settings.miniMapScale + GTOP, Green);
			if(Settings.invulnOn && !star.collected
					&& star.time < Settings.starAppear
					&& coin.score % Settings.starLevel == 0 && coin.score != 0)
				canvas.drawRect((star.x) / Settings.miniMapScale, (star.y)
						/ Settings.miniMapScale + GTOP, (star.x + img_len)
						/ Settings.miniMapScale, (star.y + img_len)
						/ Settings.miniMapScale + GTOP, StarYellow);
			if(Settings.iceOn && !ice.collected
					&& ice.time < Settings.iceAppear
					&& coin.score % Settings.iceLevel == 0 && coin.score != 0)
				canvas.drawRect((ice.x) / Settings.miniMapScale, (ice.y)
						/ Settings.miniMapScale + GTOP, (ice.x + img_len)
						/ Settings.miniMapScale, (ice.y + img_len)
						/ Settings.miniMapScale + GTOP, IceBlue);

			for(int i = 0; i < hWaters.size(); i++) {
				if((Settings.hWaterOn && !hWaters.get(i).collected
						&& hWaters.get(i).time < Settings.hWaterAppear
						&& coin.score % Settings.hWaterLevel == 0 && coin.score != 0)
						|| hWaters.get(i).thrown)
					canvas.drawRect((hWaters.get(i).x) / Settings.miniMapScale,
							(hWaters.get(i).y) / Settings.miniMapScale + GTOP,
							(hWaters.get(i).x + img_len)
									/ Settings.miniMapScale,
							(hWaters.get(i).y + img_len)
									/ Settings.miniMapScale + GTOP, White);
			}

			if(Two.twoPlayer && (one.dead || two.dead))
				canvas.drawRect((rev.x) / Settings.miniMapScale, (rev.y)
						/ Settings.miniMapScale + GTOP, (rev.x + img_len)
						/ Settings.miniMapScale, (rev.y + img_len)
						/ Settings.miniMapScale + GTOP, IceBlue);
			if(!fire.collected && coin.score % Settings.fireLevel == 0
					&& coin.score != 0)
				for(int i = 0; i < fires.size(); i++)
					canvas.drawRect((fires.get(i).x) / Settings.miniMapScale,
							(fires.get(i).y) / Settings.miniMapScale + GTOP,
							(fires.get(i).x + img_len) / Settings.miniMapScale,
							(fires.get(i).y + img_len) / Settings.miniMapScale
									+ GTOP, FireRed);
			for(int i = 0; i < people.size(); i++) {
				canvas.drawRect((people.get(i).x) / Settings.miniMapScale,
						(people.get(i).y) / Settings.miniMapScale + GTOP,
						(people.get(i).x + img_len) / Settings.miniMapScale,
						(people.get(i).y + img_len) / Settings.miniMapScale
								+ GTOP, PersonYellow);
			}
			if(saved)
				canvas.drawRect((door.x) / Settings.miniMapScale, (door.y)
						/ Settings.miniMapScale + GTOP, (door.x + img_len)
						/ Settings.miniMapScale, (door.y + img_len)
						/ Settings.miniMapScale + GTOP, Brown);

			boolean playing = !gameOver && !paused;
			// Controls (joy stick)
			if(!Two.twoPlayer) {
				canvas.drawRect(0, GHEIGHT, Settings.SCREENWIDTH,
						Settings.SCREENHEIGHT, Grey);

				if(One.touching1 && !gameOver && !paused
						&& One.getInput(One.mx) > Settings.jStickCenter
						&& !gameOver && One.getInput(One.sy) > GHEIGHT
						&& One.getInput(One.sx) > buttonRight)
					canvas.drawCircle(
							Settings.jStickCenter
									+ Settings.jStickMove
									* (float) Math
											.cos(Math.atan((double) (One
													.getInput(One.my) - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
													/ (One.getInput(One.mx) - Settings.jStickCenter))),
							(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
									+ Settings.jStickMove
									* (float) Math.sin(Math.atan((double) (One
											.getInput(One.my) - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
											/ (One.getInput(One.mx) - Settings.jStickCenter))),
							Settings.jStickRad, Black);
				if(One.touching1 && !gameOver && !paused
						&& One.getInput(One.mx) < Settings.jStickCenter
						&& !gameOver && One.getInput(One.sy) > GHEIGHT
						&& One.getInput(One.sx) > buttonRight)
					canvas.drawCircle(
							Settings.jStickCenter
									- Settings.jStickMove
									* (float) Math
											.cos(Math.atan((double) (One
													.getInput(One.my) - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
													/ (One.getInput(One.mx) - Settings.jStickCenter))),
							(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
									- Settings.jStickMove
									* (float) Math.sin(Math.atan((double) (One
											.getInput(One.my) - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
											/ (One.getInput(One.mx) - Settings.jStickCenter))),
							Settings.jStickRad, Black);
				if(One.touching1 && !gameOver && !paused
						&& One.getInput(One.mx) == Settings.jStickCenter
						&& !gameOver && One.getInput(One.sy) > GHEIGHT
						&& One.getInput(One.sx) > buttonRight) {
					if(One.getInput(One.my) > GHEIGHT
							+ (Settings.BOTHEIGHT / 2))
						canvas.drawCircle(
								Settings.jStickCenter,
								(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
										+ Settings.jStickMove,
								Settings.jStickRad, Black);
					if(One.getInput(One.my) < GHEIGHT
							+ (Settings.BOTHEIGHT / 2))
						canvas.drawCircle(
								Settings.jStickCenter,
								(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
										- Settings.jStickMove,
								Settings.jStickRad, Black);
				}
				if((!One.touching1 || gameOver || paused)
						|| (One.touching1 && !(One.getInput(One.sy) > GHEIGHT && One
								.getInput(One.sx) > buttonRight)))
					canvas.drawCircle(Settings.jStickCenter,
							(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)),
							Settings.jStickRad, Black);
			}
			else {
				// Controls (joy stick) two player 1
				canvas.drawRect(0, GHEIGHT, Settings.SCREENWIDTH,
						Settings.SCREENHEIGHT, Grey);
				boolean inHitBox1 = (sy1 > GHEIGHT && sx1 > buttonRight);

				if(mx1 > Settings.jStickCenter && inHitBox1 && playing
						&& touching1)
					canvas.drawCircle(
							Settings.jStickCenter
									+ Settings.jStickMove
									* (float) Math
											.cos(Math
													.atan((double) (my1 - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
															/ (mx1 - Settings.jStickCenter))),
							(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
									+ Settings.jStickMove
									* (float) Math.sin(Math
											.atan((double) (my1 - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
													/ (mx1 - Settings.jStickCenter))),
							Settings.jStickRad, Black);
				if(mx1 < Settings.jStickCenter && inHitBox1 && playing
						&& touching1)
					canvas.drawCircle(
							Settings.jStickCenter
									- Settings.jStickMove
									* (float) Math
											.cos(Math
													.atan((double) (my1 - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
															/ (mx1 - Settings.jStickCenter))),
							(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
									- Settings.jStickMove
									* (float) Math.sin(Math
											.atan((double) (my1 - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
													/ (mx1 - Settings.jStickCenter))),
							Settings.jStickRad, Black);
				if(mx1 == Settings.jStickCenter && inHitBox1 && playing
						&& touching1) {
					if(my1 > GHEIGHT + Settings.BOTHEIGHT / 2)
						canvas.drawCircle(
								Settings.jStickCenter,
								(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
										+ Settings.jStickMove,
								Settings.jStickRad, Black);
					if(my1 < GHEIGHT + Settings.BOTHEIGHT / 2)
						canvas.drawCircle(
								Settings.jStickCenter,
								(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
										- Settings.jStickMove,
								Settings.jStickRad, Black);
				}
				if((!touching1 || gameOver || paused)
						|| (touching1 && !(inHitBox1)))
					canvas.drawCircle(Settings.jStickCenter,
							(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)),
							Settings.jStickRad, Black);
			}

			// Score / Settings
			if(coin.score < 10)
				Black.getTextBounds("Score: 0-", 0, "Score: 0-".length(),
						bounds);
			else if(coin.score < 100)
				Black.getTextBounds("Score: 00-", 0, "Score: 00-".length(),
						bounds);
			else if(coin.score < 1000)
				Black.getTextBounds("Score: 000-", 0, "Score: 000-".length(),
						bounds);

			Rect bounds2 = new Rect();
			if(Settings.personOn && one.saved + two.saved < 10)
				Black.getTextBounds(" + 0--", 0, " + 0--".length(), bounds2);
			else if(one.saved + two.saved < 100)
				Black.getTextBounds(" + 00--", 0, " + 00--".length(), bounds2);
			else if(one.saved + two.saved < 1000)
				Black.getTextBounds(" + 000--", 0, " + 000--".length(), bounds2);

			canvas.drawText("Score:  " + Integer.toString(coin.score), 5,
					GHEIGHT + bounds.height() * 3 / 2, Black);
			canvas.drawText(" + " + Integer.toString(one.saved + two.saved),
					5 + bounds.width(), GHEIGHT + bounds.height() * 3 / 2,
					Black);
			canvas.drawText("High score: " + Integer.toString(One.highscore), 5
					+ bounds.width() + bounds2.width(),
					GHEIGHT + bounds.height() * 3 / 2, Black);

			// Lives
			if(one.lives < 10)
				Black.getTextBounds("Lives: 0-", 0, "Lives: 0-".length(),
						bounds);
			if(one.lives >= 10)
				Black.getTextBounds("Lives: 00-", 0, "Lives: 00-".length(),
						bounds);

			canvas.drawText("Lives: " + Integer.toString(one.lives),
					Settings.SCREENWIDTH - bounds.width(),
					GHEIGHT + bounds.height() * 3 / 2, Black);

			// Holy Water
			if(one.hWater < 10)
				Black.getTextBounds("Holy Water: 0-", 0,
						"Holy Water: 0-".length(), bounds);
			if(one.hWater >= 10)
				Black.getTextBounds("Holy Water: 00-", 0,
						"Holy Water: 00-".length(), bounds);

			canvas.drawText("Holy Water: " + Integer.toString(one.hWater),
					Settings.SCREENWIDTH - bounds.width(),
					GHEIGHT + bounds.height() * 9 / 4, Black);

			// Speed
			if(Settings.speed < 10)
				Black.getTextBounds("Speed: 0-", 0, "Speed: 0-".length(),
						bounds);
			if(Settings.speed >= 10)
				Black.getTextBounds("Speed: 00-", 0, "Speed: 00-".length(),
						bounds);

			canvas.drawText("Speed: " + Integer.toString(Settings.speed),
					Settings.SCREENWIDTH - bounds.width(),
					Settings.SCREENHEIGHT - bounds.height() * 21 / 5, Black);

			// Difficulty
			if(Settings.difficulty == 0) {
				Black.getTextBounds("Difficulty: Easy-", 0,
						"Difficulty: Easy-".length(), bounds);
				canvas.drawText("Difficulty: Easy", Settings.SCREENWIDTH
						- bounds.width(),
						Settings.SCREENHEIGHT - bounds.height() * 16 / 5, Black);
			}
			if(Settings.difficulty == 1) {
				Black.getTextBounds("Difficulty: Medium-", 0,
						"Difficulty: Medium-".length(), bounds);
				canvas.drawText("Difficulty: Medium", Settings.SCREENWIDTH
						- bounds.width(),
						Settings.SCREENHEIGHT - bounds.height() * 16 / 5, Black);
			}
			if(Settings.difficulty == 2) {
				Black.getTextBounds("Difficulty: Hard-", 0,
						"Difficulty: Hard-".length(), bounds);
				canvas.drawText("Difficulty: Hard", Settings.SCREENWIDTH
						- bounds.width(),
						Settings.SCREENHEIGHT - bounds.height() * 16 / 5, Black);
			}
			if(Settings.difficulty == 3) {
				Black.getTextBounds("Difficulty: Custom-", 0,
						"Difficulty: Custom-".length(), bounds);
				canvas.drawText("Difficulty: Custom", Settings.SCREENWIDTH
						- bounds.width(),
						Settings.SCREENHEIGHT - bounds.height() * 16 / 5, Black);
			}

			// Powerups
			int powerups = 0;
			Black.getTextBounds("Power Ups: 0-", 0, "Power Ups: 0-".length(),
					bounds);
			if(Settings.lifeOn)
				powerups++;
			if(Settings.invulnOn)
				powerups++;
			if(Settings.iceOn)
				powerups++;
			if(Settings.hWaterOn)
				powerups++;
			canvas.drawText("Power Ups: " + Integer.toString(powerups),
					Settings.SCREENWIDTH - bounds.width(),
					Settings.SCREENHEIGHT - bounds.height() * 5 / 2, Black);

			// Enemies
			int nenemies = 0;
			Black.getTextBounds("Enemies: 0-", 0, "Enemies: 0-".length(),
					bounds);
			if(Settings.enemyOn)
				nenemies++;
			if(Settings.lenemyOn)
				nenemies++;
			if(Settings.personOn)
				nenemies++;
			if(Settings.fireOn)
				nenemies++;
			canvas.drawText("Enemies: " + Integer.toString(nenemies),
					Settings.SCREENWIDTH - bounds.width(),
					Settings.SCREENHEIGHT - bounds.height() * 9 / 5, Black);

			// World Size
			if(Settings.WSize.x < 1000 && Settings.WSize.y < 1000)
				Black.getTextBounds("World Size: 000 X 000-", 0,
						"World Size: 000 X 000-".length(), bounds);
			else
				Black.getTextBounds("World Size: 000 X 0000-", 0,
						"World Size: 000 X 0000-".length(), bounds);
			if(Settings.WSize.x > 1000 && Settings.WSize.y > 1000)
				Black.getTextBounds("World Size: 0000 X 0000-", 0,
						"World Size: 0000 X 0000-".length(), bounds);

			canvas.drawText("World Size: " + Integer.toString(Settings.WSize.x)
					+ " X " + Integer.toString(Settings.WSize.y),
					Settings.SCREENWIDTH - bounds.width(),
					Settings.SCREENHEIGHT - bounds.height() / 2, Black);

			// GUI
			canvas.drawRect(buttonLeft, buttonTop, buttonRight, buttonBottom,
					Blue);
			if(!paused && !gameOver) {
				// Pause bars
				canvas.drawRect(buttonLeft + (buttonWidth * 2) / 8, buttonTop
						+ buttonPadding, buttonLeft + (buttonWidth * 3) / 8,
						buttonBottom - buttonPadding, Black);
				canvas.drawRect(buttonLeft + (buttonWidth * 5) / 8, buttonTop
						+ buttonPadding, buttonLeft + (buttonWidth * 6) / 8,
						buttonBottom - buttonPadding, Black);
			}
			if(Two.twoPlayer)
				canvas.drawRect(0, Settings.BOTHEIGHT, Settings.SCREENWIDTH,
						Settings.SCREENHEIGHT / 2, Black);
			if(paused) {
				// Play button
				Black.setStyle(Paint.Style.FILL);

				Path path = new Path();
				path.moveTo(buttonLeft + buttonWidth / 8, buttonTop
						+ buttonPadding);
				path.lineTo(buttonLeft + (buttonWidth * 7) / 8, buttonTop
						+ buttonHeight / 2);
				path.lineTo(buttonLeft + buttonWidth / 8, buttonBottom
						- buttonPadding);
				path.lineTo(buttonLeft + buttonWidth / 8, buttonTop
						+ buttonPadding);
				path.close();

				canvas.drawPath(path, Black);

				Black.setStyle(Paint.Style.FILL_AND_STROKE);

				canvas.drawText("Game paused", Settings.SCREENWIDTH / 2,
						GHEIGHT / 2 - img_len * 2, White);
			}

			if(gameOver) {
				RectF oval = new RectF();
				oval.set(buttonLeft + buttonPadding, buttonTop + buttonPadding,
						buttonRight - buttonPadding, buttonBottom
								- buttonPadding);
				canvas.drawArc(oval, 30, 300, true, Black);
				oval.set(buttonLeft + buttonPadding + buttonWidth / 16,
						buttonTop + buttonPadding + buttonWidth / 16,
						buttonRight - buttonPadding - buttonWidth / 16,
						buttonBottom - buttonPadding - buttonWidth / 16);
				canvas.drawArc(oval, 5, 350, true, Blue);

				float x = buttonPadding + buttonWidth / 2
						+ ((buttonWidth - buttonPadding * 2) / 2) * (0.86602f);// cos
																				// 30
				float y = buttonTop + buttonHeight / 2
						+ ((buttonHeight - buttonPadding * 2) / 2) * (0.5f);// sin
																			// 30

				Black.setStrokeWidth(buttonWidth / 16);

				canvas.drawLine(x, y, x - (x - buttonPadding - buttonWidth / 2)
						* 2 / 3, y, Black);
				canvas.drawLine(x, y, x, y + (buttonBottom - y) * 2 / 3, Black);

				Black.setStrokeWidth(0);

				canvas.drawText("Game Over", Settings.SCREENWIDTH / 2, GHEIGHT
						/ 2 - img_len * 2, White);
			}

			// Two Player
			if(Two.twoPlayer) {
				canvas.save(Canvas.MATRIX_SAVE_FLAG);
				canvas.rotate(180, Settings.SCREENWIDTH / 2,
						Settings.SCREENHEIGHT / 2);

				// World Bounds
				canvas.drawLine(Settings.WSize.x - two.camx, GTOP,
						Settings.WSize.x - two.camx, Settings.WSize.y
								- two.camy + GTOP - 1, White);
				canvas.drawLine(Settings.WSize.x - two.camx, Settings.WSize.y
						- two.camy + GTOP - 1, -two.camx + 1, Settings.WSize.y
						- two.camy + GTOP - 1, White);
				canvas.drawLine(-two.camx + 1, Settings.WSize.y - two.camy
						+ GTOP - 1, -two.camx + 1, GTOP, White);

				// Coin pointer
				float a2 = coin.y - two.y;
				float b2 = coin.x - two.x;

				if(!(b2 == 0)) {
					float pointerAngle = (float) Math.atan(a2 / b2);
					float pointer1x = (float) (Math.cos(pointerAngle - 10) * Settings.pointerSideScale);
					float pointer1y = (float) (Math.sin(pointerAngle - 10) * Settings.pointerSideScale);

					float pointer2x = (float) (Math.cos(pointerAngle + 10) * Settings.pointerSideScale);
					float pointer2y = (float) (Math.sin(pointerAngle + 10) * Settings.pointerSideScale);

					if(b2 > 0) {
						canvas.drawLine(
								two.x
										+ img_len
										/ 2
										- two.camx
										+ (float) (Math.cos(pointerAngle) * img_len),
								two.y
										+ img_len
										/ 2
										- two.camy
										+ GTOP
										+ (float) (Math.sin(pointerAngle) * img_len),
								two.x + img_len / 2 + (coin.x - two.x)
										/ Settings.pointerScale - two.camx,
								two.y + img_len / 2 + (coin.y - two.y)
										/ Settings.pointerScale - two.camy
										+ GTOP, White);

						canvas.drawLine(two.x + img_len / 2 + (coin.x - two.x)
								/ Settings.pointerScale - two.camx, two.y
								+ img_len / 2 + (coin.y - two.y)
								/ Settings.pointerScale - two.camy + GTOP,
								two.x + img_len / 2 + (coin.x - two.x)
										/ Settings.pointerScale - two.camx
										+ pointer1x, two.y + img_len / 2
										+ (coin.y - two.y)
										/ Settings.pointerScale - two.camy
										+ GTOP + pointer1y, White);
						canvas.drawLine(two.x + img_len / 2 + (coin.x - two.x)
								/ Settings.pointerScale - two.camx, two.y
								+ img_len / 2 + (coin.y - two.y)
								/ Settings.pointerScale - two.camy + GTOP,
								two.x + img_len / 2 + (coin.x - two.x)
										/ Settings.pointerScale - two.camx
										+ pointer2x, two.y + img_len / 2
										+ (coin.y - two.y)
										/ Settings.pointerScale - two.camy
										+ GTOP + pointer2y, White);
					}
					if(b2 < 0) {
						canvas.drawLine(
								two.x
										+ img_len
										/ 2
										- two.camx
										- (float) (Math.cos(pointerAngle) * img_len),
								two.y
										+ img_len
										/ 2
										- two.camy
										+ GTOP
										- (float) (Math.sin(pointerAngle) * img_len),
								two.x + img_len / 2 + (coin.x - two.x)
										/ Settings.pointerScale - two.camx,
								two.y + img_len / 2 + (coin.y - two.y)
										/ Settings.pointerScale - two.camy
										+ GTOP, White);

						canvas.drawLine(two.x + img_len / 2 + (coin.x - two.x)
								/ Settings.pointerScale - two.camx, two.y
								+ img_len / 2 + (coin.y - two.y)
								/ Settings.pointerScale - two.camy + GTOP,
								two.x + img_len / 2 + (coin.x - two.x)
										/ Settings.pointerScale - two.camx
										- pointer1x, two.y + img_len / 2
										+ (coin.y - two.y)
										/ Settings.pointerScale - two.camy
										+ GTOP - pointer1y, White);
						canvas.drawLine(two.x + img_len / 2 + (coin.x - two.x)
								/ Settings.pointerScale - two.camx, two.y
								+ img_len / 2 + (coin.y - two.y)
								/ Settings.pointerScale - two.camy + GTOP,
								two.x + img_len / 2 + (coin.x - two.x)
										/ Settings.pointerScale - two.camx
										- pointer2x, two.y + img_len / 2
										+ (coin.y - two.y)
										/ Settings.pointerScale - two.camy
										+ GTOP - pointer2y, White);
					}
				}
				else if(a2 > 0) {
					canvas.drawLine(two.x + img_len / 2 - two.camx, two.y
							+ img_len / 2 - two.camy + GTOP + img_len, two.x
							+ img_len / 2 + (coin.x - two.x)
							/ Settings.pointerScale - two.camx, two.y + img_len
							/ 2 + (coin.y - two.y) / Settings.pointerScale
							- two.camy + GTOP, White);

					canvas.drawLine(two.x + img_len / 2 + (coin.x - two.x)
							/ Settings.pointerScale - two.camx, two.y + img_len
							/ 2 + (coin.y - two.y) / Settings.pointerScale
							- two.camy + GTOP, two.x + img_len / 2
							+ (coin.x - two.x) / Settings.pointerScale
							- two.camx, two.y + img_len / 2 + (coin.y - two.y)
							/ Settings.pointerScale - two.camy + GTOP
							+ Settings.pointerSideScale, White);
					canvas.drawLine(two.x + img_len / 2 + (coin.x - two.x)
							/ Settings.pointerScale - two.camx, two.y + img_len
							/ 2 + (coin.y - two.y) / Settings.pointerScale
							- two.camy + GTOP, two.x + img_len / 2
							+ (coin.x - two.x) / Settings.pointerScale
							- two.camx, two.y + img_len / 2 + (coin.y - two.y)
							/ Settings.pointerScale - two.camy + GTOP
							+ Settings.pointerSideScale, White);
				}
				else if(a2 < 0) {
					canvas.drawLine(two.x + img_len / 2 - two.camx, two.y
							+ img_len / 2 - two.camy + GTOP + img_len, two.x
							+ img_len / 2 + (coin.x - two.x)
							/ Settings.pointerScale - two.camx, two.y + img_len
							/ 2 + (coin.y - two.y) / Settings.pointerScale
							- two.camy + GTOP, White);

					canvas.drawLine(two.x + img_len / 2 + (coin.x - two.x)
							/ Settings.pointerScale - two.camx, two.y + img_len
							/ 2 + (coin.y - two.y) / Settings.pointerScale
							- two.camy + GTOP, two.x + img_len / 2
							+ (coin.x - two.x) / Settings.pointerScale
							- two.camx, two.y + img_len / 2 + (coin.y - two.y)
							/ Settings.pointerScale - two.camy + GTOP
							- Settings.pointerSideScale, White);
					canvas.drawLine(two.x + img_len / 2 + (coin.x - two.x)
							/ Settings.pointerScale - two.camx, two.y + img_len
							/ 2 + (coin.y - two.y) / Settings.pointerScale
							- two.camy + GTOP, two.x + img_len / 2
							+ (coin.x - two.x) / Settings.pointerScale
							- two.camx, two.y + img_len / 2 + (coin.y - two.y)
							/ Settings.pointerScale - two.camy + GTOP
							- Settings.pointerSideScale, White);
				}

				// Entities
				for(int i = 0; i < hWaters.size(); i++) {
					if(hWaters.get(i).blast_frames > 0
							&& hWaters.get(i).y + img_len / 2 - two.camy
									- Settings.hWaterRad >= 0)
						canvas.drawCircle(hWaters.get(i).x + img_len / 2,
								hWaters.get(i).y + img_len / 2,
								Settings.hWaterRad, Blue);
					if(hWaters.get(i).blast_frames > 0
							&& hWaters.get(i).y + img_len / 2 - two.camy
									- Settings.hWaterRad >= -Settings.hWaterRad * 2) {
						RectF oval = new RectF();
						oval.set(hWaters.get(i).x + img_len / 2
								- Settings.hWaterRad, hWaters.get(i).y
								+ img_len / 2 - Settings.hWaterRad,
								hWaters.get(i).x + img_len / 2
										+ Settings.hWaterRad, hWaters.get(i).y
										+ img_len / 2 + Settings.hWaterRad);
						float Angle = (float) Math
								.toDegrees(Math.acos((hWaters.get(i).y
										+ img_len / 2 - two.camy - Settings.hWaterRad)
										/ (Settings.hWaterRad)));
						canvas.drawArc(oval, 90 - Angle, 90 + Angle, true, Blue);
						canvas.drawRect(
								(float) (hWaters.get(i).x + img_len / 2 - Settings.hWaterRad
										* Math.cos(Angle)),
								hWaters.get(i).y + img_len / 2 - two.camy
										- Settings.hWaterRad,
								(float) (hWaters.get(i).x + img_len / 2 + Settings.hWaterRad
										* Math.cos(Angle)), hWaters.get(i).y
										+ img_len / 2, Blue);
					}
				}

				if(!one.dead) {
					if(one.y - two.camy >= 0
							&& !(one.invuln && star.itime % 20 < 10)
							&& star.itime < Settings.starATime * 4 / 5)
						canvas.drawBitmap(img_player, one.x - two.camx, one.y
								- two.camy + GTOP, null);
					if(one.y - two.camy >= -img_len && one.y - two.camy < 0
							&& !(one.invuln && star.itime % 20 < 10)
							&& star.itime < Settings.starATime * 4 / 5) {
						Bitmap cropped = Bitmap.createBitmap(img_player, 0,
								-(int) (one.y - two.camy), img_len, img_len
										+ (int) (one.y - two.camy));
						canvas.drawBitmap(cropped, (int) one.x - two.camx,
								one.y - two.camy + GTOP
										- (int) (one.y - two.camy), null);
					}
					if(one.y - two.camy >= 0
							&& !(one.invuln && star.itime % 7 < 3)
							&& star.itime > Settings.starATime * 4 / 5)
						canvas.drawBitmap(img_player, one.x - two.camx, one.y
								- two.camy + GTOP, null);
					if(one.y - two.camy >= -img_len && one.y - two.camy < 0
							&& !(one.invuln && star.itime % 7 < 3)
							&& star.itime > Settings.starATime * 4 / 5) {
						Bitmap cropped = Bitmap.createBitmap(img_player, 0,
								-(int) (one.y - two.camy), img_len, img_len
										+ (int) (one.y - two.camy));
						canvas.drawBitmap(cropped, one.x - two.camx, one.y
								- two.camy + GTOP - (int) (one.y - two.camy),
								null);
					}
				}
				else {
					if(one.y - two.camy >= 0)
						canvas.drawBitmap(img_skull, one.x - two.camx, one.y
								- two.camy + GTOP, null);
					if(one.y - two.camy >= -img_len && one.y - two.camy < 0) {
						Bitmap cropped = Bitmap.createBitmap(img_skull, 0,
								-(int) (one.y - two.camy), img_len, img_len
										+ (int) (one.y - two.camy));
						canvas.drawBitmap(cropped, one.x - two.camx, one.y
								- two.camy + GTOP - (int) (one.y - two.camy),
								null);
					}
				}

				if(!two.dead) {
					if(!(two.invuln && star.itime % 20 < 10)
							&& star.itime < Settings.starATime * 4 / 5)
						canvas.drawBitmap(img_player, two.x - two.camx, two.y
								- two.camy + GTOP, null);
					if(!(two.invuln && star.itime % 7 < 3)
							&& star.itime > Settings.starATime * 4 / 5)
						canvas.drawBitmap(img_player, two.x - two.camx, two.y
								- two.camy + GTOP, null);
				}
				else
					canvas.drawBitmap(img_skull, two.x - two.camx, two.y
							- two.camy + GTOP, null);

				if(coin.y - two.camy >= 0)
					canvas.drawBitmap(img_coin, coin.x - two.camx, coin.y
							- two.camy + GTOP, null);
				if(coin.y - two.camy >= -img_len && coin.y - two.camy < 0) {
					Bitmap cropped = Bitmap.createBitmap(img_coin, 0,
							-(coin.y - (int) two.camy), img_len, img_len
									+ (coin.y - (int) two.camy));
					canvas.drawBitmap(cropped, coin.x - two.camx, coin.y
							- two.camy + GTOP - (coin.y - (int) two.camy), null);
				}

				if(!(ice.iced && ice.atime % 7 > 3 && ice.atime > Settings.starATime * 4 / 5)) {
					for(int i = 0; i < enemies.size(); i++) {
						if(enemies.get(i).y - two.camy >= 0)
							canvas.drawBitmap(img_enemy, enemies.get(i).x
									- two.camx, enemies.get(i).y - two.camy
									+ GTOP, null);
						if(enemies.get(i).y - two.camy >= -img_len
								&& enemies.get(i).y - two.camy < 0) {
							Bitmap cropped = Bitmap
									.createBitmap(
											img_enemy,
											0,
											-(int) (enemies.get(i).y - two.camy),
											img_len,
											img_len
													+ (int) (enemies.get(i).y - two.camy));
							canvas.drawBitmap(cropped, enemies.get(i).x
									- two.camx, enemies.get(i).y - two.camy
									+ GTOP
									- (int) (enemies.get(i).y - two.camy), null);
						}
					}
					for(int i = 0; i < lenemies.size(); i++) {
						if(lenemies.get(i).y - two.camy >= 0)
							canvas.drawBitmap(img_lenemy, lenemies.get(i).x
									- two.camx, lenemies.get(i).y - two.camy
									+ GTOP, null);
						if(lenemies.get(i).y - two.camy >= -img_len * 2
								&& lenemies.get(i).y - two.camy < 0) {
							Bitmap cropped = Bitmap
									.createBitmap(
											img_lenemy,
											0,
											-(int) (lenemies.get(i).y - two.camy),
											img_len * 2,
											img_len
													* 2
													+ (int) (lenemies.get(i).y - two.camy));
							canvas.drawBitmap(cropped, lenemies.get(i).x
									- two.camx, lenemies.get(i).y - two.camy
									+ GTOP
									- (int) (lenemies.get(i).y - two.camy),
									null);
						}
					}
				}

				if(life.y - two.camy >= 0 && Settings.lifeOn && !life.collected
						&& life.time < Settings.lifeAppear
						&& coin.score % Settings.lifeLevel == 0
						&& coin.score != 0)
					canvas.drawBitmap(img_life, life.x - two.camx, life.y
							- two.camy + GTOP, null);
				if(life.y - two.camy >= -img_len && life.y - two.camy < 0
						&& Settings.lifeOn && !life.collected
						&& life.time < Settings.lifeAppear
						&& coin.score % Settings.lifeLevel == 0
						&& coin.score != 0) {
					Bitmap cropped = Bitmap.createBitmap(img_life, 0,
							-(int) (life.y - two.camy), img_len, img_len
									+ (int) (life.y - two.camy));
					canvas.drawBitmap(cropped, life.x - two.camx, life.y
							- two.camy + GTOP - (int) (life.y - two.camy), null);
				}
				if(rev.y - two.camy >= 0 && (one.dead || two.dead))
					canvas.drawBitmap(img_revive, rev.x - two.camx, rev.y
							- two.camy + GTOP, null);
				if(rev.y - two.camy >= -img_len && rev.y - two.camy < 0
						&& (one.dead || two.dead)) {
					Bitmap cropped = Bitmap.createBitmap(img_revive, 0,
							-(int) (rev.y - two.camy), img_len, img_len
									+ (int) (rev.y - two.camy));
					canvas.drawBitmap(cropped, rev.x - two.camx, rev.y
							- two.camy + GTOP - (int) (rev.y - two.camy), null);
				}
				if(star.y - two.camy >= 0 && Settings.invulnOn
						&& !star.collected && star.time < Settings.starAppear
						&& coin.score % Settings.starLevel == 0
						&& coin.score != 0)
					canvas.drawBitmap(img_star, star.x - two.camx, star.y
							- two.camy + GTOP, null);
				if(star.y - two.camy >= -img_len && star.y - two.camy < 0
						&& Settings.invulnOn && !star.collected
						&& star.time < Settings.starAppear
						&& coin.score % Settings.starLevel == 0
						&& coin.score != 0) {
					Bitmap cropped = Bitmap.createBitmap(img_star, 0,
							-(int) (star.y - two.camy), img_len, img_len
									+ (int) (star.y - two.camy));
					canvas.drawBitmap(cropped, star.x - two.camx, star.y
							- two.camy + GTOP - (int) (star.y - two.camy), null);
				}
				if(ice.y - two.camy >= 0 && Settings.iceOn && !ice.collected
						&& ice.time < Settings.iceAppear
						&& coin.score % Settings.iceLevel == 0
						&& coin.score != 0)
					canvas.drawBitmap(img_ice, ice.x - two.camx, ice.y
							- two.camy + GTOP, null);
				if(ice.y - two.camy >= -img_len && ice.y - two.camy < 0
						&& Settings.iceOn && !ice.collected
						&& ice.time < Settings.iceAppear
						&& coin.score % Settings.iceLevel == 0
						&& coin.score != 0) {
					Bitmap cropped = Bitmap.createBitmap(img_ice, 0,
							-(int) (ice.y - two.camy), img_len, img_len
									+ (int) (ice.y - two.camy));
					canvas.drawBitmap(cropped, ice.x - two.camx, ice.y
							- two.camy + GTOP - (int) (ice.y - two.camy), null);
				}

				for(int i = 0; i < hWaters.size(); i++) {
					if(hWaters.get(i).y - two.camy >= 0
							&& ((!hWaters.get(i).collected
									&& hWaters.get(i).time < Settings.hWaterAppear
									&& coin.score % Settings.hWaterLevel == 0 && coin.score != 0) || hWaters
										.get(i).thrown))
						canvas.drawBitmap(img_hWater, hWaters.get(i).x
								- two.camx, hWaters.get(i).y - two.camy + GTOP,
								null);
					if(hWaters.get(i).y - two.camy >= -img_len
							&& hWaters.get(i).y - two.camy < 0
							&& ((!hWaters.get(i).collected
									&& hWaters.get(i).time < Settings.hWaterAppear
									&& coin.score % Settings.hWaterLevel == 0 && coin.score != 0) || hWaters
										.get(i).thrown)) {
						Bitmap cropped = Bitmap.createBitmap(img_hWater, 0,
								-(int) (hWaters.get(i).y - two.camy), img_len,
								img_len + (int) (hWaters.get(i).y - two.camy));
						canvas.drawBitmap(cropped, hWaters.get(i).x - two.camx,
								hWaters.get(i).y - two.camy + GTOP
										- (int) (hWaters.get(i).y - two.camy),
								null);
					}
				}

				if(!fire.collected && coin.score % Settings.fireLevel == 0
						&& coin.score != 0)
					for(int i = 0; i < fires.size(); i++) {
						if(fires.get(i).y - two.camy >= 0)
							canvas.drawBitmap(img_fire, fires.get(i).x
									- two.camx, fires.get(i).y - two.camy
									+ GTOP, null);
						if(fires.get(i).y - two.camy >= -img_len
								&& fires.get(i).y - two.camy < 0) {
							Bitmap cropped = Bitmap
									.createBitmap(
											img_fire,
											0,
											-(int) (fires.get(i).y - two.camy),
											img_len,
											img_len
													+ (int) (fires.get(i).y - two.camy));
							canvas.drawBitmap(cropped, fires.get(i).x
									- two.camx, fires.get(i).y - two.camy
									+ GTOP - (int) (fires.get(i).y - two.camy),
									null);
						}
					}
				for(int i = 0; i < people.size(); i++) {
					if(people.get(i).y - two.camy >= 0) {
						canvas.drawBitmap(img_person, people.get(i).x
								- two.camx, people.get(i).y - two.camy + GTOP,
								null);
					}
					if(people.get(i).y - two.camy >= -img_len
							&& people.get(i).y - two.camy < 0) {
						Bitmap cropped = Bitmap.createBitmap(img_person, 0,
								-(int) (people.get(i).y - two.camy), img_len,
								img_len + (int) (people.get(i).y - two.camy));
						canvas.drawBitmap(cropped, people.get(i).x - two.camx,
								people.get(i).y - two.camy + GTOP
										- (int) (people.get(i).y - two.camy),
								null);
					}
				}
				if(saved && door.y - two.camy >= 0)
					canvas.drawBitmap(img_door, door.x - two.camx, door.y
							- two.camy + GTOP, null);
				if(saved && door.y - two.camy >= -img_len
						&& door.y - two.camy < 0) {
					Bitmap cropped = Bitmap.createBitmap(img_door, 0,
							-(door.y - (int) two.camy), img_len, img_len
									+ (door.y - (int) two.camy));
					canvas.drawBitmap(cropped, door.x - two.camx, door.y
							- two.camy + GTOP - (door.y - (int) two.camy), null);
				}

				// Mini Map
				canvas.drawLines(map_pts, White);
				canvas.drawRect((one.x) / Settings.miniMapScale, (one.y)
						/ Settings.miniMapScale + GTOP, (one.x + img_len)
						/ Settings.miniMapScale, (one.y + img_len)
						/ Settings.miniMapScale + GTOP, Blue);
				canvas.drawRect((two.x) / Settings.miniMapScale, (two.y)
						/ Settings.miniMapScale + GTOP, (two.x + img_len)
						/ Settings.miniMapScale, (two.y + img_len)
						/ Settings.miniMapScale + GTOP, Blue);
				canvas.drawRect((coin.x) / Settings.miniMapScale, (coin.y)
						/ Settings.miniMapScale + GTOP, (coin.x + img_len)
						/ Settings.miniMapScale, (coin.y + img_len)
						/ Settings.miniMapScale + GTOP, Yellow);
				for(int i = 0; i < enemies.size(); i++)
					canvas.drawRect((enemies.get(i).x) / Settings.miniMapScale,
							(enemies.get(i).y) / Settings.miniMapScale + GTOP,
							(enemies.get(i).x + img_len)
									/ Settings.miniMapScale,
							(enemies.get(i).y + img_len)
									/ Settings.miniMapScale + GTOP, Red);
				for(int i = 0; i < lenemies.size(); i++)
					canvas.drawRect(
							(lenemies.get(i).x) / Settings.miniMapScale,
							(lenemies.get(i).y) / Settings.miniMapScale + GTOP,
							(lenemies.get(i).x + img_len * 2)
									/ Settings.miniMapScale,
							(lenemies.get(i).y + img_len * 2)
									/ Settings.miniMapScale + GTOP, Red);

				if(Settings.lifeOn && !life.collected
						&& life.time < Settings.lifeAppear
						&& coin.score % Settings.lifeLevel == 0
						&& coin.score != 0)
					canvas.drawRect((life.x) / Settings.miniMapScale, (life.y)
							/ Settings.miniMapScale + GTOP, (life.x + img_len)
							/ Settings.miniMapScale, (life.y + img_len)
							/ Settings.miniMapScale + GTOP, Green);
				if(Settings.invulnOn && !star.collected
						&& star.time < Settings.starAppear
						&& coin.score % Settings.starLevel == 0
						&& coin.score != 0)
					canvas.drawRect((star.x) / Settings.miniMapScale, (star.y)
							/ Settings.miniMapScale + GTOP, (star.x + img_len)
							/ Settings.miniMapScale, (star.y + img_len)
							/ Settings.miniMapScale + GTOP, StarYellow);
				if(Settings.iceOn && !ice.collected
						&& ice.time < Settings.iceAppear
						&& coin.score % Settings.iceLevel == 0
						&& coin.score != 0)
					canvas.drawRect((ice.x) / Settings.miniMapScale, (ice.y)
							/ Settings.miniMapScale + GTOP, (ice.x + img_len)
							/ Settings.miniMapScale, (ice.y + img_len)
							/ Settings.miniMapScale + GTOP, IceBlue);

				for(int i = 0; i < hWaters.size(); i++) {
					if(Settings.hWaterOn && !hWaters.get(i).collected
							&& hWaters.get(i).time < Settings.hWaterAppear
							&& coin.score % Settings.hWaterLevel == 0
							&& coin.score != 0)
						canvas.drawRect((hWaters.get(i).x)
								/ Settings.miniMapScale, (hWaters.get(i).y)
								/ Settings.miniMapScale + GTOP,
								(hWaters.get(i).x + img_len)
										/ Settings.miniMapScale,
								(hWaters.get(i).y + img_len)
										/ Settings.miniMapScale + GTOP, White);
				}

				if(Two.twoPlayer && (one.dead || two.dead))
					canvas.drawRect((rev.x) / Settings.miniMapScale, (rev.y)
							/ Settings.miniMapScale + GTOP, (rev.x + img_len)
							/ Settings.miniMapScale, (rev.y + img_len)
							/ Settings.miniMapScale + GTOP, IceBlue);
				if(!fire.collected && coin.score % Settings.fireLevel == 0
						&& coin.score != 0)
					for(int i = 0; i < fires.size(); i++)
						canvas.drawRect((fires.get(i).x)
								/ Settings.miniMapScale, (fires.get(i).y)
								/ Settings.miniMapScale + GTOP,
								(fires.get(i).x + img_len)
										/ Settings.miniMapScale,
								(fires.get(i).y + img_len)
										/ Settings.miniMapScale + GTOP, FireRed);
				for(int i = 0; i < people.size(); i++) {
					canvas.drawRect(
							(people.get(i).x) / Settings.miniMapScale,
							(people.get(i).y) / Settings.miniMapScale + GTOP,
							(people.get(i).x + img_len) / Settings.miniMapScale,
							(people.get(i).y + img_len) / Settings.miniMapScale
									+ GTOP, PersonYellow);
				}
				if(saved)
					canvas.drawRect((door.x) / Settings.miniMapScale, (door.y)
							/ Settings.miniMapScale + GTOP, (door.x + img_len)
							/ Settings.miniMapScale, (door.y + img_len)
							/ Settings.miniMapScale + GTOP, Brown);

				// Controls (joy stick) two player 2
				canvas.drawRect(0, GHEIGHT, Settings.SCREENWIDTH,
						Settings.SCREENHEIGHT, Grey);
				boolean inHitBox2 = (sy2 < Settings.BOTHEIGHT && sx2 < Settings.SCREENWIDTH
						- buttonPadding - Settings.SCREENWIDTH / 4);

				if(mx2 > Settings.jStickCenter && inHitBox2 && playing
						&& touching2)
					canvas.drawCircle(
							Settings.jStickCenter
									- Settings.jStickMove
									* (float) Math
											.cos(Math
													.atan((double) (my2 - Settings.BOTHEIGHT / 2)
															/ (mx2 - Settings.jStickCenter))),
							(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
									- Settings.jStickMove
									* (float) Math.sin(Math
											.atan((double) (my2 - Settings.BOTHEIGHT / 2)
													/ (mx2 - Settings.jStickCenter))),
							Settings.jStickRad, Black);
				if(mx2 < Settings.jStickCenter && inHitBox2 && playing
						&& touching2)
					canvas.drawCircle(
							Settings.jStickCenter
									+ Settings.jStickMove
									* (float) Math
											.cos(Math
													.atan((double) (my2 - Settings.BOTHEIGHT / 2)
															/ (mx2 - Settings.jStickCenter))),
							(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
									+ Settings.jStickMove
									* (float) Math.sin(Math
											.atan((double) (my2 - Settings.BOTHEIGHT / 2)
													/ (mx2 - Settings.jStickCenter))),
							Settings.jStickRad, Black);
				if(mx2 == Settings.jStickCenter && inHitBox2 && playing
						&& touching2) {
					if(my2 > GHEIGHT + Settings.BOTHEIGHT / 2)
						canvas.drawCircle(
								Settings.jStickCenter,
								(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
										- Settings.jStickMove,
								Settings.jStickRad, Black);
					if(my2 < GHEIGHT + Settings.BOTHEIGHT / 2)
						canvas.drawCircle(
								Settings.jStickCenter,
								(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2))
										+ Settings.jStickMove,
								Settings.jStickRad, Black);
				}
				if((!touching2 || gameOver || paused)
						|| (touching2 && !(inHitBox2)))
					canvas.drawCircle(Settings.jStickCenter,
							(Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)),
							Settings.jStickRad, Black);

				// Score / Settings
				if(coin.score < 10)
					Black.getTextBounds("Score: 0-", 0, "Score: 0-".length(),
							bounds);
				else if(coin.score < 100)
					Black.getTextBounds("Score: 00-", 0, "Score: 00-".length(),
							bounds);
				else if(coin.score < 1000)
					Black.getTextBounds("Score: 000-", 0,
							"Score: 000-".length(), bounds);

				Rect bounds3 = new Rect();
				if(Settings.personOn && one.saved + two.saved < 10)
					Black.getTextBounds(" + 0--", 0, " + 0--".length(), bounds3);
				else if(one.saved + two.saved < 100)
					Black.getTextBounds(" + 00--", 0, " + 00--".length(),
							bounds3);
				else if(one.saved + two.saved < 1000)
					Black.getTextBounds(" + 000--", 0, " + 000--".length(),
							bounds3);

				canvas.drawText("Score:  " + Integer.toString(coin.score), 5,
						GHEIGHT + bounds.height() * 3 / 2, Black);
				canvas.drawText(
						" + " + Integer.toString(one.saved + two.saved),
						5 + bounds.width(), GHEIGHT + bounds.height() * 3 / 2,
						Black);
				canvas.drawText(
						"High score: " + Integer.toString(One.highscore), 5
								+ bounds.width() + bounds3.width(), GHEIGHT
								+ bounds.height() * 3 / 2, Black);

				// Lives
				if(two.lives < 10)
					Black.getTextBounds("Lives: 0-", 0, "Lives: 0-".length(),
							bounds);
				if(two.lives >= 10)
					Black.getTextBounds("Lives: 00-", 0, "Lives: 00-".length(),
							bounds);

				canvas.drawText("Lives: " + Integer.toString(two.lives),
						Settings.SCREENWIDTH - bounds.width(),
						GHEIGHT + bounds.height() * 3 / 2, Black);

				// Holy Water
				if(two.hWater < 10)
					Black.getTextBounds("Holy Water: 0-", 0,
							"Holy Water: 0-".length(), bounds);
				if(two.hWater >= 10)
					Black.getTextBounds("Holy Water: 00-", 0,
							"Holy Water: 00-".length(), bounds);

				canvas.drawText("Holy Water: " + Integer.toString(two.hWater),
						Settings.SCREENWIDTH - bounds.width(),
						GHEIGHT + bounds.height() * 9 / 4, Black);

				// GUI
				canvas.drawRect(buttonLeft, buttonTop, buttonRight,
						buttonBottom, Blue);
				if(!paused && !gameOver) {
					// Pause bars
					canvas.drawRect(buttonLeft + (buttonWidth * 2) / 8,
							buttonTop + buttonPadding, buttonLeft
									+ (buttonWidth * 3) / 8, buttonBottom
									- buttonPadding, Black);
					canvas.drawRect(buttonLeft + (buttonWidth * 5) / 8,
							buttonTop + buttonPadding, buttonLeft
									+ (buttonWidth * 6) / 8, buttonBottom
									- buttonPadding, Black);
				}
				if(paused) {
					// Play button
					Black.setStyle(Paint.Style.FILL);

					Path path = new Path();
					path.moveTo(buttonLeft + buttonWidth / 8, buttonTop
							+ buttonPadding);
					path.lineTo(buttonLeft + (buttonWidth * 7) / 8, buttonTop
							+ buttonHeight / 2);
					path.lineTo(buttonLeft + buttonWidth / 8, buttonBottom
							- buttonPadding);
					path.lineTo(buttonLeft + buttonWidth / 8, buttonTop
							+ buttonPadding);
					path.close();

					canvas.drawPath(path, Black);

					Black.setStyle(Paint.Style.FILL_AND_STROKE);
				}

				if(gameOver) {
					RectF oval = new RectF();
					oval.set(buttonLeft + buttonPadding, buttonTop
							+ buttonPadding, buttonRight - buttonPadding,
							buttonBottom - buttonPadding);
					canvas.drawArc(oval, 30, 300, true, Black);
					oval.set(buttonLeft + buttonPadding + buttonWidth / 16,
							buttonTop + buttonPadding + buttonWidth / 16,
							buttonRight - buttonPadding - buttonWidth / 16,
							buttonBottom - buttonPadding - buttonWidth / 16);
					canvas.drawArc(oval, 5, 350, true, Blue);

					float x = buttonPadding + buttonWidth / 2
							+ ((buttonWidth - buttonPadding * 2) / 2)
							* (0.86602f);// cos 30
					float y = buttonTop + buttonHeight / 2
							+ ((buttonHeight - buttonPadding * 2) / 2) * (0.5f);// sin
																				// 30

					Black.setStrokeWidth(buttonWidth / 16);

					canvas.drawLine(x, y, x
							- (x - buttonPadding - buttonWidth / 2) * 2 / 3, y,
							Black);
					canvas.drawLine(x, y, x, y + (buttonBottom - y) * 2 / 3,
							Black);

					Black.setStrokeWidth(0);
				}

				canvas.restore();
			}

			/**************************************** End Draw *********************************************/

			One.clicked1 = false;
			One.clicked2 = false;
			for(int i = 0; i < 6; i++) {
				One.input2[i] = Settings.SCREENHEIGHT;
			}
			Two.clicked1 = false;
			Two.clicked2 = false;

			myHolder.unlockCanvasAndPost(canvas);
		}
	}
}
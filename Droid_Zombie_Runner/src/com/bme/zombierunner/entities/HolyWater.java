package com.bme.zombierunner.entities;

import java.util.ArrayList;

import com.bme.zombierunner.entities.Player;
import com.bme.zombierunner.One;
import com.bme.zombierunner.Settings;
import com.bme.zombierunner.Two;
import com.bme.zombierunner.ZRSurface;

public class HolyWater {
	public int x, y, time, blast_frames;
	public float vx, vy;
	public boolean collected, thrown, hit;

	public HolyWater(int img_len) {
		x = (int) (Math.random() * (Settings.WSize.x - img_len));
		y = (int) (Math.random() * (Settings.WSize.y - img_len));
		vx = 0;
		vy = 0;
		this.time = 0;
		this.collected = false;
		thrown = false;
		blast_frames = 0;
		hit = false;
	}

	private void getThrow(boolean playerOne, boolean clicked, float mx,
			float my, Player one, Player two, int img_len) {
		if(playerOne) {
			if(one.hWater > 0 && clicked
					&& my < Settings.SCREENHEIGHT - Settings.BOTHEIGHT
					&& my > Settings.BOTHEIGHT && !thrown) {
				x = (int) one.x;
				y = (int) one.y;
				int GTOP = 0;
				if(one.x - one.camx + img_len / 2 != mx) {
					float cos = 0;
					float sin = 0;
					if(Two.twoPlayer) {
						GTOP = Settings.SCREENHEIGHT / 2;
					}
					cos = (float) Math.cos(Math.atan(((one.y - one.camy
							+ img_len / 2 + GTOP) - my)
							/ (one.x - one.camx + img_len / 2 - mx)));
					sin = (float) Math.sin(Math.atan(((one.y - one.camy
							+ img_len / 2 + GTOP) - my)
							/ (one.x - one.camx + img_len / 2 - mx)));
					if(one.x - one.camx + img_len / 2 > mx) {
						vx = -Settings.hWaterSpeed * cos;
						vy = -Settings.hWaterSpeed * sin;
					}
					else {
						vx = Settings.hWaterSpeed * cos;
						vy = Settings.hWaterSpeed * sin;
					}
				}
				else {
					vx = 0;
					if(one.y - one.camy + img_len / 2 + GTOP > my)
						vy = -Settings.hWaterSpeed;
					else
						vy = Settings.hWaterSpeed;
				}
				thrown = true;
				ZRSurface.hWaterThrown = true;
				one.hWater--;
			}
		}
		else {
			if(two.hWater > 0 && clicked && my > Settings.BOTHEIGHT
					&& my < Settings.SCREENHEIGHT - Settings.BOTHEIGHT
					&& !thrown) {
				x = (int) two.x;
				y = (int) two.y;
				if(Settings.SCREENWIDTH - two.x - two.camx + img_len / 2 != mx) {
					float cos = (float) Math
							.cos(Math
									.atan((Settings.SCREENHEIGHT
											- (two.y - two.camy + img_len / 2 + Settings.SCREENHEIGHT / 2) - my)
											/ (Settings.SCREENWIDTH
													- (two.x - two.camx + img_len / 2) - mx)));
					float sin = (float) Math
							.sin(Math
									.atan((Settings.SCREENHEIGHT
											- (two.y - two.camy + img_len / 2 + Settings.SCREENHEIGHT / 2) - my)
											/ (Settings.SCREENWIDTH
													- (two.x - two.camx + img_len / 2) - mx)));
					if(Settings.SCREENWIDTH - two.x - two.camx + img_len / 2 > mx) {
						vx = Settings.hWaterSpeed * cos;
						vy = Settings.hWaterSpeed * sin;
					}
					else {
						vx = -Settings.hWaterSpeed * cos;
						vy = -Settings.hWaterSpeed * sin;
					}
				}
				else {
					vx = 0;
					if(Settings.SCREENHEIGHT
							- (two.y - two.camy + img_len / 2 + Settings.SCREENHEIGHT / 2) > my)
						vy = Settings.hWaterSpeed;
					else
						vy = -Settings.hWaterSpeed;
				}
				thrown = true;
				ZRSurface.hWaterThrown = true;
				two.hWater--;
			}
		}
	}

	public boolean Update(Player one, Player two, ArrayList<Enemy> enemies,
			ArrayList<LEnemy> lenemies, ArrayList<Person> people,
			boolean fired, int img_len, int score) {
		if(Settings.hWaterOn) {
			if(!collected && time < Settings.hWaterAppear
					&& score % Settings.hWaterLevel == 0 && score != 0) {
				time++;
				if(x + img_len > one.x && x < one.x + img_len
						&& y + img_len > one.y && y < one.y + img_len) {
					one.hWater++;
					collected = true;
					x = (int) (Math.random() * (Settings.WSize.x - img_len));
					y = (int) (Math.random() * (Settings.WSize.y - img_len));
				}
				if(Two.twoPlayer && x + img_len > two.x && x < two.x + img_len
						&& y + img_len > two.y && y < two.y + img_len) {
					two.hWater++;
					collected = true;
					x = (int) (Math.random() * (Settings.WSize.x - img_len));
					y = (int) (Math.random() * (Settings.WSize.y - img_len));
				}
			}
			if(thrown) {
				x += vx;
				y += vy;
				// hit enemy
				for(int i = 0; i < enemies.size(); i++) {
					if(x + img_len > enemies.get(i).x
							&& x < enemies.get(i).x + img_len
							&& y + img_len > enemies.get(i).y
							&& y < enemies.get(i).y + img_len) {
						hit = true;
						for(int index = 0; index < enemies.size(); index++) {
							if(Math.sqrt((x - enemies.get(index).x)
									* (x - enemies.get(index).x)
									+ (y - enemies.get(index).y)
									* (y - enemies.get(index).y)) < Settings.hWaterRad) {
								Person person = new Person(
										enemies.get(index).x,
										enemies.get(index).y, fired);
								person.RandomizeMSpeed();
								people.add(person);
								enemies.remove(index);
								index--;
							}
						}
						for(int index = 0; index < lenemies.size(); index++) {
							if(Math.sqrt((x - lenemies.get(index).x)
									* (x - lenemies.get(index).x)
									+ (y - lenemies.get(index).y)
									* (y - lenemies.get(index).y)) < Settings.hWaterRad) {
								Person person = new Person(
										lenemies.get(index).x,
										lenemies.get(index).y, fired);
								person.RandomizeMSpeed();
								people.add(person);
								lenemies.remove(index);
								index--;
							}
						}
						thrown = false;
						blast_frames = 10;
					}
				}
				// hit lenemy
				if(!hit) {
					for(int i = 0; i < lenemies.size(); i++) {
						if(x + img_len > lenemies.get(i).x
								&& x < lenemies.get(i).x + img_len * 2
								&& y + img_len > lenemies.get(i).y
								&& y < lenemies.get(i).y + img_len * 2) {
							hit = true;
							for(int index = 0; index < enemies.size(); index++) {
								if(Math.sqrt((x - enemies.get(index).x)
										* (x - enemies.get(index).x)
										+ (y - enemies.get(index).y)
										* (y - enemies.get(index).y)) < Settings.hWaterRad) {
									Person person = new Person(
											enemies.get(index).x,
											enemies.get(index).y, fired);
									person.RandomizeMSpeed();
									people.add(person);
									enemies.remove(index);
									index--;
								}
							}
							for(int index = 0; index < lenemies.size(); index++) {
								if(Math.sqrt((x - lenemies.get(index).x)
										* (x - lenemies.get(index).x)
										+ (y - lenemies.get(index).y)
										* (y - lenemies.get(index).y)) < Settings.hWaterRad) {
									Person person = new Person(
											lenemies.get(index).x + img_len / 4,
											lenemies.get(index).y + img_len / 4,
											fired);
									person.RandomizeMSpeed();
									people.add(person);
									lenemies.remove(index);
									index--;
								}
							}
							thrown = false;
							blast_frames = 10;
						}
					}
				}
				if(x < -img_len * 5 || x > Settings.WSize.x + img_len * 5
						|| y < -img_len * 5
						|| y > Settings.WSize.y + img_len * 5) {
					thrown = false;
					return true;
				}
			}
			if(blast_frames > 0) {
				blast_frames--;
				vx = 0;
				vy = 0;
				hit = false;
			}
			else if(hit) {
				return true;
			}

			// Throwing
			if(!ZRSurface.hWaterThrown) {
				if(!Two.twoPlayer && !one.dead) {
					getThrow(true, One.clicked1, One.getInput(One.mx),
							One.getInput(One.my), one, two, img_len);
					getThrow(true, One.clicked2, One.getInput2(One.mx),
							One.getInput2(One.my), one, two, img_len);
				}
				else {
					if(!one.dead)
						getThrow(true, ZRSurface.clicked1, ZRSurface.mx1,
								ZRSurface.my1, one, two, img_len);
					if(!two.dead)
						getThrow(false, ZRSurface.clicked2, ZRSurface.mx2,
								ZRSurface.my2, one, two, img_len);
				}
			}
		}
		return false;
	}
}
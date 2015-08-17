package com.bme.zombierunner.entities;

import java.util.ArrayList;

import com.bme.zombierunner.Settings;
import com.bme.zombierunner.Two;

public class Person {
	public int changeDir;
	public float x, y, vx, vy, mspeed;
	public boolean saved1, saved2;

	public Person(float x, float y, boolean fired) {
		this.x = x;
		this.y = y;
		RandomizeDir(fired);
		this.saved1 = false;
		this.saved2 = false;
		changeDir = 0;
	}

	public void RandomizeDir(boolean fired) {
		if(!fired) {
			vx = (float) (((Math.random() * 2) - 1)
					* (Math.cos(Math.random() * 314 - 157)) * Settings.speed);
			vy = (float) (((Math.random() * 2) - 1)
					* (Math.sin(Math.random() * 314 - 157)) * Settings.speed);
		}
		else {
			vx = (float) (((Math.random() * 2) - 1)
					* (Math.cos(Math.random() * 314 - 157)) * Settings.speed * 2);
			vy = (float) (((Math.random() * 2) - 1)
					* (Math.sin(Math.random() * 314 - 157)) * Settings.speed * 2);
		}
	}

	public void RandomizeMSpeed() {
		mspeed = (float) (Math.random() * Settings.speed / 5 + Settings.speed * 4 / 5);
	}

	public boolean Update(Player one, Player two, Coin coin,
			ArrayList<Person> people, ArrayList<Enemy> enemies,
			ArrayList<LEnemy> lenemies, int img_len, boolean fired) {
		changeDir++;

		if(!(saved1 || saved2) && changeDir > Math.random() * 40 + 40) {
			RandomizeDir(fired);
			RandomizeMSpeed();
			changeDir = 0;
		}
		if(!(saved1 || saved2)) {
			x += vx;
			y += vy;

			if(x < 0) {
				x = 0;
				vx = -vx;
			}
			if(x > Settings.WSize.x - img_len) {
				x = Settings.WSize.x - img_len;
				vx = -vx;
			}
			if(y < 0) {
				y = 0;
				vy = -vy;
			}
			if(y > Settings.WSize.y - img_len) {
				y = Settings.WSize.y - img_len;
				vy = -vy;
			}
		}
		else {
			float dx = 0;
			float dy = 0;
			if(saved1) {
				dx = one.x - x;
				dy = one.y - y;
			}
			if(saved2) {
				dx = two.x - x;
				dy = two.y - y;
			}
			float Angle = 0;
			if(dx != 0)
				Angle = (float) (Math.atan(dy / dx));
			if(dx == 0) {
				if(dy >= 0)
					Angle = (float) (Math.PI / 2);
				if(dy < 0)
					Angle = (float) (Math.PI * 3 / 2);
			}
			float ivx = (float) (Math.cos(Angle) * mspeed);
			float ivy = (float) (Math.sin(Angle) * mspeed);

			if(saved1 && one.x < x) {
				ivx = -ivx;
				ivy = -ivy;
			}
			if(saved2 && two.x < x) {
				ivx = -ivx;
				ivy = -ivy;
			}

			float speed = mspeed / Settings.pTurn;

			if(vx < ivx)
				vx += speed;
			if(vx > ivx)
				vx -= speed;
			if(vy < ivy)
				vy += speed;
			if(vy > ivy)
				vy -= speed;

			x += vx;
			y += vy;

			if(x < 0)
				x = 0;
			if(x > Settings.WSize.x - img_len)
				x = Settings.WSize.x - img_len;
			if(y < 0)
				y = 0;
			if(y > Settings.WSize.y - img_len)
				y = Settings.WSize.y - img_len;
		}
		if(Math.sqrt((x - one.x) * (x - one.x) + (y - one.y) * (y - one.y)) < img_len) {
			saved1 = true;
			saved2 = false;
		}
		if(Two.twoPlayer
				&& Math.sqrt((x - two.x) * (x - two.x) + (y - two.y)
						* (y - two.y)) < img_len) {
			saved2 = true;
			saved1 = false;
		}

		if(saved1 && one.dead)
			saved1 = false;
		if(saved2 && two.dead)
			saved2 = false;

		int times_collided = 0;
		ArrayList<Integer> collided_ids = new ArrayList<Integer>();

		for(int i = 0; i < people.size(); i++) {
			float foo = (float) Math.sqrt((x - people.get(i).x)
					* (x - people.get(i).x) + (y - people.get(i).y)
					* (y - people.get(i).y));
			if(foo < img_len && foo != 0) {
				times_collided++;
				collided_ids.add(i);
			}
		}
		if(times_collided > 0) {
			for(int i = 0; i < collided_ids.size(); i++) {
				people.get(collided_ids.get(i)).saved1 = this.saved1;
				people.get(collided_ids.get(i)).saved2 = this.saved2;
			}
		}

		for(int i = 0; i < enemies.size(); i++) {
			if(Math.sqrt((x - enemies.get(i).x) * (x - enemies.get(i).x)
					+ (y - enemies.get(i).y) * (y - enemies.get(i).y)) < img_len) {
				enemies.add(new Enemy(x, y, fired));
				return true;
			}
		}
		for(int i = 0; i < lenemies.size(); i++) {
			if(Math.sqrt((x - lenemies.get(i).x - img_len / 2)
					* (x - lenemies.get(i).x - img_len / 2)
					+ (y - lenemies.get(i).y - img_len / 2)
					* (y - lenemies.get(i).y - img_len / 2)) < img_len * 3 / 2) {
				lenemies.add(new LEnemy(x, y, fired));
				return true;
			}
		}
		return false;
	}
}
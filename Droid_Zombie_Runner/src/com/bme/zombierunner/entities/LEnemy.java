package com.bme.zombierunner.entities;

import com.bme.zombierunner.Settings;
import com.bme.zombierunner.Two;

public class LEnemy {
	public int changeDir;
	public float x, y, vx, vy, mspeed;

	public LEnemy(float x, float y, boolean fired) {
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = 0;
		RandomizeMSpeed(fired);
		changeDir = 0;
	}

	public void RandomizeMSpeed(boolean fired) {
		if(fired)
			mspeed = (float) (Math.random() * Settings.speed * 2);
		else
			mspeed = (float) (Math.random() * Settings.speed);
	}

	public void Update(Player one, Player two, int img_len, boolean fired) {
		changeDir++;
		if(changeDir > Math.random() * 40 + 40) {
			RandomizeMSpeed(fired);
			changeDir = 0;
		}
		float dx1 = one.x - x;
		float dy1 = one.y - y;

		float dx2 = two.x - x;
		float dy2 = two.y - y;

		float Angle = 0;

		boolean following_one = false;

		if(!one.dead) {
			if((Two.twoPlayer && (Math.sqrt(dx1 * dx1 - dy1 * dy1) < Math
					.sqrt(dx2 * dx2 - dy2 * dy2) || two.dead))
					|| !Two.twoPlayer) {
				if(dx1 != 0)
					Angle = (float) (Math.atan(dy1 / dx1));
				if(dx1 == 0) {
					if(dy1 >= 0)
						Angle = (float) (Math.PI / 2);
					if(dy1 < 0)
						Angle = (float) (Math.PI * 3 / 2);
				}
				following_one = true;
			}
			else {
				if(dx2 != 0)
					Angle = (float) (Math.atan(dy2 / dx2));
				if(dx2 == 0) {
					if(dy2 >= 0)
						Angle = (float) (Math.PI / 2);
					if(dy2 < 0)
						Angle = (float) (Math.PI * 3 / 2);
				}
			}
		}
		else if(Two.twoPlayer && !two.dead) {
			if(dx2 != 0)
				Angle = (float) (Math.atan(dy2 / dx2));
			if(dx2 == 0) {
				if(dy2 >= 0)
					Angle = (float) (Math.PI / 2);
				if(dy2 < 0)
					Angle = (float) (Math.PI * 3 / 2);
			}
		}

		float ivx = (float) (Math.cos(Angle) * mspeed);
		float ivy = (float) (Math.sin(Angle) * mspeed);

		if(one.x < x + img_len / 4 && following_one) {
			ivx = -ivx;
			ivy = -ivy;
		}
		if(Two.twoPlayer && two.x < x + img_len / 4 && !following_one) {
			ivx = -ivx;
			ivy = -ivy;
		}

		float speed = mspeed / Settings.leTurn;

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

		if(x < 0) {
			x = 0;
		}
		if(x > Settings.WSize.x - img_len * 2) {
			x = Settings.WSize.x - img_len * 2;
		}
		if(y < 0) {
			y = 0;
		}
		if(y > Settings.WSize.y - img_len * 2) {
			y = Settings.WSize.y - img_len * 2;
		}
	}
}
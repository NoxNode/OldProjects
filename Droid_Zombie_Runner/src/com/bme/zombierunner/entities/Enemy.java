package com.bme.zombierunner.entities;

import com.bme.zombierunner.Settings;

public class Enemy {
	public int changeDir;
	public float x, y, vx, vy;

	public Enemy(float x, float y, boolean fired) {
		this.x = x;
		this.y = y;
		RandomizeDir(fired);
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

	public void Update(int img_len, boolean fired) {
		changeDir++;
		if(changeDir > Math.random() * 40 + 40) {
			RandomizeDir(fired);
			changeDir = 0;
		}
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
}
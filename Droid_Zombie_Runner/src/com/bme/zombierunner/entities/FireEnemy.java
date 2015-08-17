package com.bme.zombierunner.entities;

import com.bme.zombierunner.entities.Fire;
import com.bme.zombierunner.entities.Ice;
import com.bme.zombierunner.entities.Player;
import com.bme.zombierunner.Settings;

public class FireEnemy {
	public int changeDir;
	public float x, y, vx, vy;

	public FireEnemy(float x, float y) {
		this.x = x;
		this.y = y;
		this.vx = (float) (((Math.random() * 2) - 1)
				* (Math.cos(Math.random() * 314 - 157)) * Settings.speed * 2);
		this.vy = (float) (((Math.random() * 2) - 1)
				* (Math.sin(Math.random() * 314 - 157)) * Settings.speed * 2);
		changeDir = 0;
	}

	public void RandomizeDir() {
		vx = (float) (((Math.random() * 2) - 1)
				* (Math.cos(Math.random() * 314 - 157)) * Settings.speed * 2);
		vy = (float) (((Math.random() * 2) - 1)
				* (Math.sin(Math.random() * 314 - 157)) * Settings.speed * 2);
	}

	public void Update(Player one, Player two, Ice ice, Fire f, int img_len,
			int score) {
		changeDir++;
		if(changeDir > Math.random() * 40 + 40) {
			RandomizeDir();
			changeDir = 0;
		}
		if(Settings.fireOn) {
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

			if(!f.collected && score % Settings.fireLevel == 0 && score != 0) {
				if((one.x + img_len > x && one.x < x + img_len
						&& one.y + img_len > y && one.y < y + img_len)
						|| (two.x + img_len > x && two.x < x + img_len
								&& two.y + img_len > y && two.y < y + img_len)) {
					f.active = true;
					f.collected = true;
					f.atime = 0;
					ice.active = false;
					ice.atime = 0;
					ice.iced = false;
				}
			}
		}
	}
}
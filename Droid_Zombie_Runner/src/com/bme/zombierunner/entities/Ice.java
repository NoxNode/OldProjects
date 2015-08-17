package com.bme.zombierunner.entities;

import java.util.ArrayList;

import com.bme.zombierunner.entities.Enemy;
import com.bme.zombierunner.entities.Fire;
import com.bme.zombierunner.entities.LEnemy;
import com.bme.zombierunner.entities.Player;
import com.bme.zombierunner.Settings;
import com.bme.zombierunner.Two;

public class Ice {
	public int x, y, time, atime;
	public boolean collected, active, iced;

	public Ice(int x, int y) {
		this.x = x;
		this.y = y;
		this.time = 0;
		this.atime = 0;
		this.collected = false;
		this.active = false;
		this.iced = false;
	}

	public void Update(Player one, Player two, ArrayList<Enemy> enemies,
			ArrayList<LEnemy> lenemies, Fire fire, int img_len, int score) {
		if(Settings.iceOn) {
			if(!collected && time < Settings.iceAppear
					&& score % Settings.iceLevel == 0 && score != 0) {
				time++;
				if((one.x + img_len > x && one.x < x + img_len
						&& one.y + img_len > y && one.y < y + img_len)
						|| (Two.twoPlayer && two.x + img_len > x
								&& two.x < x + img_len && two.y + img_len > y && two.y < y
								+ img_len)) {
					active = true;
					collected = true;
					atime = 0;
					fire.active = false;
					fire.atime = 0;
					fire.fired = false;
					x = (int) (Math.random() * (Settings.WSize.x - img_len));
					y = (int) (Math.random() * (Settings.WSize.y - img_len));
				}
			}
			if(active) {
				if(!iced)
					iced = true;
				if(iced)
					atime++;
				if(atime > Settings.iceATime) {
					atime = 0;
					iced = false;
					active = false;
					for(int i = 0; i < enemies.size(); i++) {
						enemies.set(i,
								new Enemy(enemies.get(i).x, enemies.get(i).y,
										fire.fired));
					}
					for(int i = 0; i < lenemies.size(); i++) {
						lenemies.set(i,
								new LEnemy(lenemies.get(i).x,
										lenemies.get(i).y, fire.fired));
					}
				}
			}
		}
	}
}
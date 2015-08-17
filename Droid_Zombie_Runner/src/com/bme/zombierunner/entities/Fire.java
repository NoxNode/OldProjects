package com.bme.zombierunner.entities;

import java.util.ArrayList;

import com.bme.zombierunner.entities.Enemy;
import com.bme.zombierunner.entities.LEnemy;
import com.bme.zombierunner.entities.Player;
import com.bme.zombierunner.Settings;

public class Fire {
	public int atime;
	public boolean collected, active, fired;

	public Fire() {
		this.atime = 0;
		this.collected = false;
		this.active = false;
		this.fired = false;
	}

	public void Update(Player one, ArrayList<Enemy> enemies,
			ArrayList<LEnemy> lenemies, int img_len) {
		if(active && Settings.fireOn) {
			if(!fired) {
				fired = true;
				for(int i = 0; i < enemies.size(); i++)
					enemies.set(i, new Enemy(enemies.get(i).x,
							enemies.get(i).y, fired));
				for(int i = 0; i < lenemies.size(); i++) {
					lenemies.set(i,
							new LEnemy(lenemies.get(i).x, lenemies.get(i).y,
									fired));
				}
			}
			if(fired)
				atime++;
			if(atime > Settings.fireATime) {
				atime = 0;
				fired = false;
				active = false;
				for(int i = 0; i < enemies.size(); i++) {
					enemies.set(i, new Enemy(enemies.get(i).x,
							enemies.get(i).y, fired));
				}
				for(int i = 0; i < lenemies.size(); i++) {
					lenemies.set(i,
							new LEnemy(lenemies.get(i).x, lenemies.get(i).y,
									fired));
				}
			}
		}
	}
}
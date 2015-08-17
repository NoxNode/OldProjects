package com.bme.zombierunner.entities;

import com.bme.zombierunner.entities.Player;
import com.bme.zombierunner.Settings;
import com.bme.zombierunner.Two;

public class Revive {
	public int x, y;

	public Revive(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void Update(Player one, Player two, int img_len) {
		if(Two.twoPlayer) {
			if((Math.sqrt((x - one.x) * (x - one.x) + (y - one.y) * (y - one.y)) < img_len)
					|| (Math.sqrt((x - two.x) * (x - two.x) + (y - two.y)
							* (y - two.y)) < img_len)) {
				two.dead = false;
				one.dead = false;
				x = (int) (Math.random() * (Settings.WSize.x - img_len));
				y = (int) (Math.random() * (Settings.WSize.y - img_len));
			}
		}
	}
}
package com.bme.zombierunner.entities;

import java.util.ArrayList;

import com.bme.zombierunner.entities.Player;
import com.bme.zombierunner.Settings;

public class Door {
	public int x, y;

	public Door(int img_len) {
		x = (int) (Math.random() * (Settings.WSize.x - img_len));
		y = (int) (Math.random() * (Settings.WSize.y - img_len));
	}

	public void Update(Player one, Player two, ArrayList<Person> people,
			int img_len) {
		boolean saved = false;
		for(int i = 0; i < people.size(); i++) {
			if(people.get(i).saved1 || people.get(i).saved2)
				saved = true;
			if(people.get(i).x + img_len > x && people.get(i).x < x + img_len
					&& people.get(i).y + img_len > y
					&& people.get(i).y < y + img_len) {
				if(people.get(i).saved1) {
					one.saved += Settings.pCoin;
					people.remove(i);
					i--;
				}
				else {
					if(people.get(i).saved2) {
						two.saved += Settings.pCoin;
						people.remove(i);
						i--;
					}
				}
			}
		}
		if(!saved) {
			x = (int) (Math.random() * (Settings.WSize.x - img_len));
			y = (int) (Math.random() * (Settings.WSize.y - img_len));
		}

	}
}
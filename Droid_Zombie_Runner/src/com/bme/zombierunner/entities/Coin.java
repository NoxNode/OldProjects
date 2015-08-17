package com.bme.zombierunner.entities;

import java.util.ArrayList;

import com.bme.zombierunner.One;
import com.bme.zombierunner.Settings;
import com.bme.zombierunner.Two;
import com.bme.zombierunner.entities.Player;
import com.bme.zombierunner.entities.Enemy;
import com.bme.zombierunner.entities.LEnemy;
import com.bme.zombierunner.entities.Fire;
import com.bme.zombierunner.entities.FireEnemy;
import com.bme.zombierunner.entities.Person;
import com.bme.zombierunner.entities.Life;
import com.bme.zombierunner.entities.Star;
import com.bme.zombierunner.entities.Ice;

public class Coin {
	public int x, y, score;

	public Coin(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void Update(Player one, Player two, ArrayList<Enemy> enemies,
			ArrayList<LEnemy> lenemies, ArrayList<FireEnemy> fires,
			ArrayList<Person> people, Fire f, Life life, Star star, Ice ice,
			ArrayList<HolyWater> hWaters, int img_len) {
		if((Math.sqrt((x - one.x) * (x - one.x) + (y - one.y) * (y - one.y)) < img_len)
				|| (Two.twoPlayer && Math.sqrt((x - two.x) * (x - two.x)
						+ (y - two.y) * (y - two.y)) < img_len)) {

			score++;

			if(score + one.saved > One.highscore) {
				One.highscore = score + one.saved;
			}
			if(Two.twoPlayer && score + one.saved + two.saved > Two.highscore) {
				Two.highscore = score + one.saved + two.saved;
			}

			x = (int) (Math.random() * (Settings.WSize.x - img_len));
			y = (int) (Math.random() * (Settings.WSize.y - img_len));

			// Person
			if(Settings.personOn && score % Settings.personLevel == 0
					&& score != 0) {
				Person person = new Person(
						(float) (Math.random() * (Settings.WSize.x - img_len)),
						(float) (Math.random() * (Settings.WSize.y - img_len)),
						f.fired);
				person.RandomizeMSpeed();
				people.add(person);
			}

			if(Settings.enemyOn && score % Settings.enemyLevel == 0
					&& score != 0) {
				Enemy enemy = new Enemy(0, 0, f.fired);

				if(!Two.twoPlayer) {
					if(one.x + img_len / 2 < Settings.WSize.x / 2
							&& one.y + img_len / 2 < Settings.WSize.y / 2) {
						enemy.x = Settings.WSize.x;
						enemy.y = Settings.WSize.y - img_len;
					}
					else if(one.x + img_len / 2 < Settings.WSize.x / 2
							&& one.y + img_len / 2 > Settings.WSize.y / 2) {
						enemy.x = Settings.WSize.x;
						enemy.y = 0;
					}
					else if(one.y + img_len / 2 < Settings.WSize.y / 2) {
						enemy.x = 0;
						enemy.y = Settings.WSize.y - img_len;
					}
					else {
						enemy.x = 0;
						enemy.y = 0;
					}
				}
				else {
					if(one.x + img_len / 2 < Settings.WSize.x / 2
							&& one.y + img_len / 2 < Settings.WSize.y / 2) {
						if(two.x + img_len / 2 > Settings.WSize.x / 2
								&& two.y + img_len / 2 < Settings.WSize.y / 2) {
							enemy.x = 0;
							enemy.y = Settings.WSize.y - img_len;
						}
						else {
							enemy.x = Settings.WSize.x - img_len;
							enemy.y = 0;
						}
					}
					else if(one.x + img_len / 2 > Settings.WSize.x / 2
							&& one.y + img_len / 2 < Settings.WSize.y / 2) {
						if(two.x + img_len / 2 < Settings.WSize.x / 2
								&& two.y + img_len / 2 < Settings.WSize.y / 2) {
							enemy.x = 0;
							enemy.y = Settings.WSize.y - img_len;
						}
						else {
							enemy.x = 0;
							enemy.y = 0;
						}
					}
					else if(one.x + img_len / 2 < Settings.WSize.x / 2
							&& one.y + img_len / 2 > Settings.WSize.y / 2) {
						if(two.x + img_len / 2 < Settings.WSize.x / 2
								&& two.y + img_len / 2 < Settings.WSize.y / 2) {
							enemy.x = Settings.WSize.x - img_len;
							enemy.y = Settings.WSize.y - img_len;
						}
						else {
							enemy.x = 0;
							enemy.y = 0;
						}
					}
					else {
						if(two.x + img_len / 2 < Settings.WSize.x / 2
								&& two.y + img_len / 2 < Settings.WSize.y / 2) {
							enemy.x = Settings.WSize.x - img_len;
							enemy.y = 0;
						}
						else {
							enemy.x = 0;
							enemy.y = 0;
						}
					}
				}
				enemies.add(enemy);
			}

			if(Settings.lenemyOn && score % Settings.lenemyLevel == 0
					&& score != 0) {
				LEnemy lenemy = new LEnemy(0, 0, f.fired);

				if(one.x < Settings.WSize.x / 2 && one.y < Settings.WSize.y / 2) {
					lenemy.x = Settings.WSize.x;
					lenemy.y = Settings.WSize.y - img_len * 2;
				}
				else if(one.x < Settings.WSize.x / 2
						&& one.y > Settings.WSize.y / 2) {
					lenemy.x = Settings.WSize.x;
					lenemy.y = 0;
				}
				else if(one.y < Settings.WSize.y / 2) {
					lenemy.x = 0;
					lenemy.y = Settings.WSize.y - img_len * 2;
				}
				else {
					lenemy.x = 0;
					lenemy.y = 0;
				}
				lenemies.add(lenemy);
			}

			if(Settings.fireOn && score % Settings.fireLevel == 0 && score != 0) {
				FireEnemy fire = new FireEnemy(0, 0);

				if(one.x < Settings.WSize.x / 2 && one.y < Settings.WSize.y / 2) {
					fire.x = Settings.WSize.x;
					fire.y = Settings.WSize.y - img_len;
				}
				else if(one.x < Settings.WSize.x / 2
						&& one.y > Settings.WSize.y / 2) {
					fire.x = Settings.WSize.x;
					fire.y = 0;
				}
				else if(one.y < Settings.WSize.y / 2) {
					fire.x = 0;
					fire.y = Settings.WSize.y - img_len;
				}
				else {
					fire.x = 0;
					fire.y = 0;
				}
				fires.add(fire);
				for(int i = 0; i < fires.size(); i++) {
					FireEnemy newfire = new FireEnemy(fire.x, fire.y);
					fires.set(i, newfire);
				}
			}
			life.collected = false;
			life.time = 0;
			life.x = (int) (Math.random() * (Settings.WSize.x - img_len));
			life.y = (int) (Math.random() * (Settings.WSize.y - img_len));
			star.collected = false;
			star.time = 0;
			star.x = (int) (Math.random() * (Settings.WSize.x - img_len));
			star.y = (int) (Math.random() * (Settings.WSize.y - img_len));
			ice.collected = false;
			ice.time = 0;
			ice.x = (int) (Math.random() * (Settings.WSize.x - img_len));
			ice.y = (int) (Math.random() * (Settings.WSize.y - img_len));
			f.collected = false;

			if(Settings.hWaterOn && score % Settings.hWaterLevel == 0
					&& score != 0) {
				for(int i = 0; i < hWaters.size(); i++) {
					if(!hWaters.get(i).collected) {
						hWaters.remove(i);
					}
				}
				HolyWater hWater = new HolyWater(img_len);
				hWaters.add(hWater);
			}
		}
	}
}
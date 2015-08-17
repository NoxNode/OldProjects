package com.bme.zombierunner.entities;

import com.bme.zombierunner.One;
import com.bme.zombierunner.Settings;
import com.bme.zombierunner.Two;
import com.bme.zombierunner.ZRSurface;

public class Player {
	public float x, y, camx, camy;
	public int lives, nHit, lnHit, saved, hWater;
	public boolean hit, lhit, invuln, one, dead;

	public Player(boolean one) {
		this.x = 0;
		this.y = 0;
		this.camx = 0;
		this.camy = 0;
		this.lives = 0;
		this.nHit = -1;
		this.hit = false;
		this.lnHit = -1;
		this.lhit = false;
		this.invuln = false;
		this.saved = 0;
		this.one = one;
		this.dead = false;
		this.hWater = 0;
	}

	public void Update(int GHEIGHT, int img_len, int GTOP) {
		if(!Two.twoPlayer) {
			if(One.touching1 && One.getInput(One.mx) > Settings.jStickCenter
					&& One.getInput(One.sy) > GHEIGHT
					&& One.getInput(One.sx) > Settings.SCREENWIDTH / 4) {
				x += (Settings.speed * (Math
						.cos(Math.atan((double) (One.getInput(One.my) - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
								/ (One.getInput(One.mx) - Settings.jStickCenter)))));
				y += (Settings.speed * (Math
						.sin(Math.atan((double) (One.getInput(One.my) - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
								/ (One.getInput(One.mx) - Settings.jStickCenter)))));
			}
			if(One.touching1 && One.getInput(One.mx) < Settings.jStickCenter
					&& One.getInput(One.sy) > GHEIGHT
					&& One.getInput(One.sx) > Settings.SCREENWIDTH / 4) {
				x -= (Settings.speed * (Math
						.cos(Math.atan((double) (One.getInput(One.my) - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
								/ (One.getInput(One.mx) - Settings.jStickCenter)))));
				y -= (Settings.speed * (Math
						.sin(Math.atan((double) (One.getInput(One.my) - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
								/ (One.getInput(One.mx) - Settings.jStickCenter)))));
			}
			if(One.touching1 && One.getInput(One.mx) == Settings.jStickCenter
					&& One.getInput(One.sy) > GHEIGHT
					&& One.getInput(One.sx) > Settings.SCREENWIDTH / 4) {
				if(One.getInput(One.my) > GHEIGHT + (Settings.BOTHEIGHT / 2))
					y += Settings.speed;
				if(One.getInput(One.my) < GHEIGHT + (Settings.BOTHEIGHT / 2))
					y -= Settings.speed;
			}
		}
		else {
			if(one) {
				if(ZRSurface.touching1 && ZRSurface.mx1 > Settings.jStickCenter
						&& ZRSurface.sy1 > GHEIGHT
						&& ZRSurface.sx1 > Settings.SCREENWIDTH / 4) {
					x += (Settings.speed * (Math
							.cos(Math
									.atan((double) (ZRSurface.my1 - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
											/ (ZRSurface.mx1 - Settings.jStickCenter)))));
					y += (Settings.speed * (Math
							.sin(Math
									.atan((double) (ZRSurface.my1 - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
											/ (ZRSurface.mx1 - Settings.jStickCenter)))));
				}
				if(ZRSurface.touching1 && ZRSurface.mx1 < Settings.jStickCenter
						&& ZRSurface.sy1 > GHEIGHT
						&& ZRSurface.sx1 > Settings.SCREENWIDTH / 4) {
					x -= (Settings.speed * (Math
							.cos(Math
									.atan((double) (ZRSurface.my1 - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
											/ (ZRSurface.mx1 - Settings.jStickCenter)))));
					y -= (Settings.speed * (Math
							.sin(Math
									.atan((double) (ZRSurface.my1 - (Settings.SCREENHEIGHT - (Settings.BOTHEIGHT / 2)))
											/ (ZRSurface.mx1 - Settings.jStickCenter)))));
				}
				if(ZRSurface.touching1
						&& ZRSurface.mx1 == Settings.jStickCenter
						&& ZRSurface.sy1 > GHEIGHT
						&& ZRSurface.sx1 > Settings.SCREENWIDTH / 4) {
					if(ZRSurface.my1 > GHEIGHT + (Settings.BOTHEIGHT / 2))
						y += Settings.speed;
					if(ZRSurface.my1 < GHEIGHT + (Settings.BOTHEIGHT / 2))
						y -= Settings.speed;
				}
			}
			else {
				if(ZRSurface.touching2
						&& ZRSurface.mx2 > Settings.jStickCenter
						&& ZRSurface.sy2 < Settings.BOTHEIGHT
						&& ZRSurface.sx2 < Settings.SCREENWIDTH - 5
								- Settings.SCREENWIDTH / 4) {
					x -= (Settings.speed * (Math
							.cos(Math
									.atan((double) (ZRSurface.my2 - Settings.BOTHEIGHT / 2)
											/ (ZRSurface.mx2 - Settings.jStickCenter)))));
					y -= (Settings.speed * (Math
							.sin(Math
									.atan((double) (ZRSurface.my2 - Settings.BOTHEIGHT / 2)
											/ (ZRSurface.mx2 - Settings.jStickCenter)))));
				}
				if(ZRSurface.touching2
						&& ZRSurface.mx2 < Settings.jStickCenter
						&& ZRSurface.sy2 < Settings.BOTHEIGHT
						&& ZRSurface.sx2 < Settings.SCREENWIDTH - 5
								- Settings.SCREENWIDTH / 4) {
					x += (Settings.speed * (Math
							.cos(Math
									.atan((double) (ZRSurface.my2 - Settings.BOTHEIGHT / 2)
											/ (ZRSurface.mx2 - Settings.jStickCenter)))));
					y += (Settings.speed * (Math
							.sin(Math
									.atan((double) (ZRSurface.my2 - Settings.BOTHEIGHT / 2)
											/ (ZRSurface.mx2 - Settings.jStickCenter)))));
				}
				if(ZRSurface.touching2
						&& ZRSurface.mx2 == Settings.jStickCenter
						&& ZRSurface.sy2 < Settings.BOTHEIGHT
						&& ZRSurface.sx2 < Settings.SCREENWIDTH - 5
								- Settings.SCREENWIDTH / 4) {
					if(ZRSurface.my2 > GHEIGHT + (Settings.BOTHEIGHT / 2))
						y -= Settings.speed;
					if(ZRSurface.my2 < GHEIGHT + (Settings.BOTHEIGHT / 2))
						y += Settings.speed;
				}
			}
		}

		if(x < 0)
			x = 0;
		if(x > Settings.WSize.x - img_len)
			x = Settings.WSize.x - img_len;
		if(y < 0)
			y = 0;
		if(y > Settings.WSize.y - img_len)
			y = Settings.WSize.y - img_len;

		if(x + img_len / 2 > Settings.SCREENWIDTH / 2
				&& x + img_len / 2 < Settings.WSize.x - Settings.SCREENWIDTH
						/ 2)
			camx = x + img_len / 2 - Settings.SCREENWIDTH / 2;
		if(Settings.WSize.x > Settings.SCREENWIDTH) {
			if(x + img_len / 2 < Settings.SCREENWIDTH / 2)
				camx = 0;
			if(x + img_len / 2 > Settings.WSize.x - Settings.SCREENWIDTH / 2)
				camx = Settings.WSize.x - Settings.SCREENWIDTH;
		}

		if(y + img_len / 2 > GHEIGHT / 2 - GTOP / 2
				&& y + img_len / 2 < Settings.WSize.y - GHEIGHT / 2 + GTOP / 2)
			camy = y + img_len / 2 - GHEIGHT / 2 + GTOP / 2;
		if(Settings.WSize.y > GHEIGHT) {
			if(y + img_len / 2 < GHEIGHT / 2 - GTOP / 2)
				camy = 0;
			if(y + img_len / 2 > Settings.WSize.y - GHEIGHT / 2 + GTOP / 2)
				camy = Settings.WSize.y - GHEIGHT + GTOP;
		}
	}
}
package com.bme.zombierunner;

import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ZRMenu extends Activity implements OnClickListener {

	Button one, two, set;
	Intent i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		one = (Button) findViewById(R.id.bOne);
		two = (Button) findViewById(R.id.bTwo);
		set = (Button) findViewById(R.id.bSettings);

		one.setOnClickListener(this);
		two.setOnClickListener(this);
		set.setOnClickListener(this);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		Settings.SCREENWIDTH = displaymetrics.widthPixels;
		Settings.SCREENHEIGHT = displaymetrics.heightPixels;

		for(int i = 0; i < Settings.sizes.length - 1; i++) {
			if(Settings.SCREENWIDTH == Settings.sizes[i].x
					&& Settings.SCREENHEIGHT == Settings.sizes[i].y) {
				Settings.currentSize = i;
				Settings.WSize.x = Settings.SCREENWIDTH;
				Settings.WSize.y = Settings.SCREENHEIGHT;
				Settings.dSize = i;
				break;
			}
		}
		Settings.scale = Settings.WSize.x * Settings.WSize.y;

		Settings.defaults();

		loadSettings();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.bOne:
			i = new Intent("com.bme.zombierunner.ONE");
			startActivity(i);
			break;
		case R.id.bTwo:
			i = new Intent("com.bme.zombierunner.TWO");
			startActivity(i);
			break;
		case R.id.bSettings:
			i = new Intent("com.bme.zombierunner.SETTINGS");
			startActivity(i);
			break;
		}
	}

	private void loadSettings() {
		FileInputStream fis = null;

		try {
			fis = openFileInput("Settings");

			final int nIntSettings = 28;
			final int nBSettings = 8;

			int new_byte = 0;
			int settings[] = new int[nIntSettings];
			boolean bsettings[] = new boolean[nBSettings];
			int current_int_setting = 0;
			int current_b_setting = 0;
			String line = "";

			while((new_byte = fis.read()) != -1) {
				if(new_byte == 10) {
					if(line.contains("true")) {
						bsettings[current_b_setting] = true;
						current_b_setting++;
					}
					else if(line.contains("false")) {
						bsettings[current_b_setting] = false;
						current_b_setting++;
					}
					else {
						settings[current_int_setting] = Integer.parseInt(line);
						current_int_setting++;
					}
					line = "";
				}
				else {
					line = line + (char) new_byte;
				}
			}

			if(current_int_setting > 0) {
				Settings.speed = settings[0];
				Settings.BOTHEIGHT = settings[1];
				Settings.jStickRad = settings[2];
				Settings.miniMapScale = settings[3];
				Settings.WSize.x = settings[4];
				Settings.WSize.y = settings[5];
				Settings.jStickCenter = settings[6];
				Settings.difficulty = settings[7];
				Settings.lifeLevel = settings[8];
				Settings.starLevel = settings[9];
				Settings.iceLevel = settings[10];
				Settings.enemyLevel = settings[11];
				Settings.lenemyLevel = settings[12];
				Settings.fireLevel = settings[13];
				Settings.personLevel = settings[14];
				Settings.hWaterLevel = settings[15];
				Settings.lifeAppear = settings[16];
				Settings.starAppear = settings[17];
				Settings.iceAppear = settings[18];
				Settings.hWaterAppear = settings[19];
				Settings.starATime = settings[20];
				Settings.iceATime = settings[21];
				Settings.fireATime = settings[22];
				Settings.pTurn = settings[23];
				Settings.leTurn = settings[24];
				Settings.hWaterSpeed = settings[25];
				Settings.hWaterRad = settings[26];
				Settings.pCoin = settings[27];

				Settings.lifeOn = bsettings[0];
				Settings.invulnOn = bsettings[1];
				Settings.iceOn = bsettings[2];
				Settings.hWaterOn = bsettings[3];
				Settings.enemyOn = bsettings[4];
				Settings.lenemyOn = bsettings[5];
				Settings.fireOn = bsettings[6];
				Settings.personOn = bsettings[7];
			}

			fis.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

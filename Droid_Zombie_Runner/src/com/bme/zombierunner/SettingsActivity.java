package com.bme.zombierunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener {

	TextView tvSpeed, tvControls, tvJStickRad, tvWSize, tvMap, tvJStickCenter,
			tvDifficulty, tvScores;

	EditText etSpeed, etControls, etJStickRad, etWSizeX, etWSizeY, etMap,
			etJStickCenter, etDifficulty;

	Button bDefaults, bReScore, bPowerUps, bEnemies, bEasy, bMedium, bHard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		tvSpeed = (TextView) findViewById(R.id.tvSpeed);
		tvControls = (TextView) findViewById(R.id.tvControls);
		tvJStickRad = (TextView) findViewById(R.id.tvJStickRad);
		tvWSize = (TextView) findViewById(R.id.tvWSize);
		tvMap = (TextView) findViewById(R.id.tvMap);
		tvJStickCenter = (TextView) findViewById(R.id.tvJStickCenter);
		tvDifficulty = (TextView) findViewById(R.id.tvDifficulty);
		tvScores = (TextView) findViewById(R.id.tvScores);

		etSpeed = (EditText) findViewById(R.id.etSpeed);
		etControls = (EditText) findViewById(R.id.etControls);
		etJStickRad = (EditText) findViewById(R.id.etJStickRad);
		etWSizeX = (EditText) findViewById(R.id.etWSizeX);
		etWSizeY = (EditText) findViewById(R.id.etWSizeY);
		etMap = (EditText) findViewById(R.id.etMap);
		etJStickCenter = (EditText) findViewById(R.id.etJStickCenter);

		bEasy = (Button) findViewById(R.id.bEasy);
		bMedium = (Button) findViewById(R.id.bMedium);
		bHard = (Button) findViewById(R.id.bHard);
		bDefaults = (Button) findViewById(R.id.bDefaults);
		bReScore = (Button) findViewById(R.id.bReScore);
		bPowerUps = (Button) findViewById(R.id.bPowerUps);
		bEnemies = (Button) findViewById(R.id.bEnemies);

		bEasy.setOnClickListener(this);
		bMedium.setOnClickListener(this);
		bHard.setOnClickListener(this);
		bDefaults.setOnClickListener(this);
		bReScore.setOnClickListener(this);
		bPowerUps.setOnClickListener(this);
		bEnemies.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		Settings.speed = Integer.parseInt(etSpeed.getText().toString());
		Settings.BOTHEIGHT = Integer.parseInt(etControls.getText().toString());
		Settings.jStickRad = Integer.parseInt(etJStickRad.getText().toString());
		Settings.WSize.x = Integer.parseInt(etWSizeX.getText().toString());
		Settings.WSize.y = Integer.parseInt(etWSizeY.getText().toString());
		Settings.miniMapScale = Integer.parseInt(etMap.getText().toString());
		Settings.jStickCenter = Integer.parseInt(etJStickCenter.getText()
				.toString());

		if(Settings.difficulty == 0) {
			Settings.lifeLevel = 3;
			Settings.starLevel = 10;
			Settings.iceLevel = 7;
			Settings.hWaterLevel = 8;
			Settings.lifeAppear = Settings.scale / 1600;
			Settings.iceAppear = (int) (Settings.scale / 1769.2832);
			Settings.starAppear = Settings.scale / 1920;
			Settings.starATime = (int) (Settings.scale / 929.0322);
			Settings.iceATime = (int) (Settings.scale / 929.0322);
			Settings.hWaterAppear = (int) (Settings.scale / 1844.8398);
			Settings.hWaterRad = Settings.scale / 1728;
			Settings.hWaterSpeed = Settings.scale / 86400;

			Settings.enemyLevel = 1;
			Settings.lenemyLevel = 15;
			Settings.personLevel = 2;
			Settings.pTurn = 40;
			Settings.leTurn = 80;
			Settings.fireLevel = 8;
			Settings.fireATime = (int) (Settings.scale / 929.0322);
			Settings.pCoin = 4;
		}
		if(Settings.difficulty == 1) {
			Settings.lifeLevel = 5;
			Settings.starLevel = 15;
			Settings.iceLevel = 10;
			Settings.hWaterLevel = 12;
			Settings.lifeAppear = Settings.scale / 2400;
			Settings.iceAppear = (int) (Settings.scale / 2658.4615);
			Settings.starAppear = Settings.scale / 2880;
			Settings.starATime = Settings.scale / 1296;
			Settings.iceATime = Settings.scale / 1296;
			Settings.hWaterAppear = (int) (Settings.scale / 2802.1621);
			Settings.hWaterRad = Settings.scale / 2592;
			Settings.hWaterSpeed = Settings.scale / 103680;
			Settings.pCoin = 3;

			Settings.enemyLevel = 1;
			Settings.lenemyLevel = 10;
			Settings.personLevel = 2;
			Settings.pTurn = 40;
			Settings.leTurn = 80;
			Settings.fireLevel = 5;
			Settings.fireATime = Settings.scale / 1296;
		}
		if(Settings.difficulty == 2) {
			Settings.lifeLevel = 8;
			Settings.starLevel = 23;
			Settings.iceLevel = 15;
			Settings.hWaterLevel = 18;
			Settings.lifeAppear = (int) (Settings.scale / 3676.5957);
			Settings.iceAppear = (int) (Settings.scale / 3987.6923);
			Settings.starAppear = Settings.scale / 4320;
			Settings.starATime = (int) (Settings.scale / 2090.3225);
			Settings.iceATime = (int) (Settings.scale / 2090.3225);
			Settings.hWaterAppear = (int) (Settings.scale / 4147.2);
			Settings.hWaterRad = Settings.scale / 5184;
			Settings.hWaterSpeed = Settings.scale / 129600;

			Settings.enemyLevel = 1;
			Settings.lenemyLevel = 7;
			Settings.personLevel = 2;
			Settings.pTurn = 40;
			Settings.leTurn = 80;
			Settings.fireLevel = 3;
			Settings.fireATime = (int) (Settings.scale / 2090.3225);
			Settings.pCoin = 2;
		}

		saveSettings();
	}

	@Override
	protected void onResume() {
		super.onResume();

		etSpeed.setText("" + Settings.speed);
		etControls.setText("" + Settings.BOTHEIGHT);
		etJStickRad.setText("" + Settings.jStickRad);
		etWSizeX.setText("" + Settings.WSize.x);
		etWSizeY.setText("" + Settings.WSize.y);
		etMap.setText("" + Settings.miniMapScale);
		etJStickCenter.setText("" + Settings.jStickCenter);

		if(Settings.difficulty == 0)
			tvDifficulty.setText("Difficulty: Easy");
		if(Settings.difficulty == 1)
			tvDifficulty.setText("Difficulty: Medium");
		if(Settings.difficulty == 2)
			tvDifficulty.setText("Difficulty: Hard");
		if(Settings.difficulty == 3)
			tvDifficulty.setText("Difficulty: Custom");

		loadHighscores();
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		case R.id.bDefaults:
			Settings.defaults();

			etSpeed.setText("" + Settings.speed);
			etControls.setText("" + Settings.BOTHEIGHT);
			etJStickRad.setText("" + Settings.jStickRad);
			etWSizeX.setText("" + Settings.WSize.x);
			etWSizeY.setText("" + Settings.WSize.y);
			etMap.setText("" + Settings.miniMapScale);
			etJStickCenter.setText("" + Settings.jStickCenter);
			tvDifficulty.setText("Difficulty: Medium");
			break;
		case R.id.bPowerUps:
			Intent powerup = new Intent("com.bme.zombierunner.POWERUPS");
			startActivity(powerup);
			break;
		case R.id.bEnemies:
			Intent enemy = new Intent("com.bme.zombierunner.ENEMIES");
			startActivity(enemy);
			break;
		case R.id.bEasy:
			Settings.difficulty = 0;
			tvDifficulty.setText("Difficulty: Easy");

			Settings.lifeLevel = 3;
			Settings.starLevel = 10;
			Settings.iceLevel = 7;
			Settings.hWaterLevel = 8;
			Settings.lifeAppear = Settings.scale / 1600;
			Settings.iceAppear = (int) (Settings.scale / 1769.2832);
			Settings.starAppear = Settings.scale / 1920;
			Settings.starATime = (int) (Settings.scale / 929.0322);
			Settings.iceATime = (int) (Settings.scale / 929.0322);
			Settings.hWaterAppear = (int) (Settings.scale / 1844.8398);
			Settings.hWaterRad = Settings.scale / 1728;
			Settings.hWaterSpeed = Settings.scale / 86400;

			Settings.enemyLevel = 1;
			Settings.lenemyLevel = 15;
			Settings.personLevel = 2;
			Settings.pTurn = 40;
			Settings.leTurn = 80;
			Settings.fireLevel = 8;
			Settings.fireATime = (int) (Settings.scale / 929.0322);
			Settings.pCoin = 4;
			break;
		case R.id.bMedium:
			Settings.difficulty = 1;
			tvDifficulty.setText("Difficulty: Medium");

			Settings.lifeLevel = 5;
			Settings.starLevel = 15;
			Settings.iceLevel = 10;
			Settings.hWaterLevel = 12;
			Settings.lifeAppear = Settings.scale / 2400;
			Settings.iceAppear = (int) (Settings.scale / 2658.4615);
			Settings.starAppear = Settings.scale / 2880;
			Settings.starATime = Settings.scale / 1296;
			Settings.iceATime = Settings.scale / 1296;
			Settings.hWaterAppear = (int) (Settings.scale / 2802.1621);
			Settings.hWaterRad = Settings.scale / 2592;
			Settings.hWaterSpeed = Settings.scale / 103680;

			Settings.enemyLevel = 1;
			Settings.lenemyLevel = 10;
			Settings.personLevel = 2;
			Settings.pTurn = 40;
			Settings.leTurn = 80;
			Settings.fireLevel = 5;
			Settings.fireATime = Settings.scale / 1296;
			Settings.pCoin = 3;
			break;
		case R.id.bHard:
			Settings.difficulty = 2;
			tvDifficulty.setText("Difficulty: Hard");

			Settings.lifeLevel = 8;
			Settings.starLevel = 23;
			Settings.iceLevel = 15;
			Settings.hWaterLevel = 18;
			Settings.lifeAppear = (int) (Settings.scale / 3676.5957);
			Settings.iceAppear = (int) (Settings.scale / 3987.6923);
			Settings.starAppear = Settings.scale / 4320;
			Settings.starATime = (int) (Settings.scale / 2090.3225);
			Settings.iceATime = (int) (Settings.scale / 2090.3225);
			Settings.hWaterAppear = (int) (Settings.scale / 4147.2);
			Settings.hWaterRad = Settings.scale / 5184;
			Settings.hWaterSpeed = Settings.scale / 129600;

			Settings.enemyLevel = 1;
			Settings.lenemyLevel = 7;
			Settings.personLevel = 2;
			Settings.pTurn = 40;
			Settings.leTurn = 80;
			Settings.fireLevel = 3;
			Settings.fireATime = (int) (Settings.scale / 2090.3225);
			Settings.pCoin = 2;
			break;
		case R.id.bReScore:
			FileOutputStream fos = null;

			try {
				fos = openFileOutput("Highscore1", Context.MODE_PRIVATE);

				String s = 0 + "\n" + 0 + "\n" + 0 + "\n" + 0 + "\n";

				fos.write(s.getBytes());

				fos.close();
			} catch(IOException e) {
				e.printStackTrace();
			}

			fos = null;

			try {
				fos = openFileOutput("Highscore2", Context.MODE_PRIVATE);

				String s = 0 + "\n" + 0 + "\n" + 0 + "\n" + 0 + "\n";

				fos.write(s.getBytes());

				fos.close();
			} catch(IOException e) {
				e.printStackTrace();
			}

			tvScores.setText("High Scores: \nOne Player: \nEasy=" + 0
					+ "; Medium=" + 0 + "; Hard=" + 0 + "; Custom=" + 0
					+ "\nTwo Player: \nEasy=" + 0 + "; Medium=" + 0 + "; Hard="
					+ 0 + "; Custom=" + 0);
			break;
		}
	}

	private void loadHighscores() {
		int eh1 = 0;
		int mh1 = 0;
		int hh1 = 0;
		int ch1 = 0;

		int eh2 = 0;
		int mh2 = 0;
		int hh2 = 0;
		int ch2 = 0;

		FileInputStream fis = null;

		try {
			fis = openFileInput("Highscore1");

			int new_byte = 0;
			int index = 0;
			int nlindex = 0;
			int new_lines[] = new int[4];
			String line = "";

			while((new_byte = fis.read()) != -1) {
				line = line + (char) new_byte;
				if(new_byte == '\n') {
					new_lines[index] = nlindex;
					index++;
				}
				nlindex++;
			}

			if(line.contains("\n")) {
				eh1 = Integer.parseInt(line.substring(0, new_lines[0]));
				mh1 = Integer.parseInt(line.substring(new_lines[0] + 1,
						new_lines[1]));
				hh1 = Integer.parseInt(line.substring(new_lines[1] + 1,
						new_lines[2]));
				ch1 = Integer.parseInt(line.substring(new_lines[2] + 1,
						new_lines[3]));
			}

			fis.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

		fis = null;

		try {
			fis = openFileInput("Highscore2");

			int new_byte = 0;
			int index = 0;
			int nlindex = 0;
			int new_lines[] = new int[4];
			String line = "";

			while((new_byte = fis.read()) != -1) {
				line = line + (char) new_byte;
				if(new_byte == '\n') {
					new_lines[index] = nlindex;
					index++;
				}
				nlindex++;
			}

			if(line.contains("\n")) {
				eh2 = Integer.parseInt(line.substring(0, new_lines[0]));
				mh2 = Integer.parseInt(line.substring(new_lines[0] + 1,
						new_lines[1]));
				hh2 = Integer.parseInt(line.substring(new_lines[1] + 1,
						new_lines[2]));
				ch2 = Integer.parseInt(line.substring(new_lines[2] + 1,
						new_lines[3]));
			}

			fis.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

		tvScores.setText("High Scores: \nOne Player: \nEasy=" + eh1
				+ "; Medium=" + mh1 + "; Hard=" + hh1 + "; Custom=" + ch1
				+ "\nTwo Player: \nEasy=" + eh2 + "; Medium=" + mh2 + "; Hard="
				+ hh2 + "; Custom=" + ch2);
	}

	private void saveSettings() {
		FileOutputStream fos = null;

		try {
			fos = openFileOutput("Settings", Context.MODE_PRIVATE);

			String s = Integer.toString(Settings.speed) + "\n"
					+ Integer.toString(Settings.BOTHEIGHT) + "\n"
					+ Integer.toString(Settings.jStickRad) + "\n"
					+ Integer.toString(Settings.miniMapScale) + "\n"
					+ Integer.toString(Settings.WSize.x) + "\n"
					+ Integer.toString(Settings.WSize.y) + "\n"
					+ Integer.toString(Settings.jStickCenter) + "\n"
					+ Integer.toString(Settings.difficulty) + "\n"
					+ Integer.toString(Settings.lifeLevel) + "\n"
					+ Integer.toString(Settings.starLevel) + "\n"
					+ Integer.toString(Settings.iceLevel) + "\n"
					+ Integer.toString(Settings.enemyLevel) + "\n"
					+ Integer.toString(Settings.lenemyLevel) + "\n"
					+ Integer.toString(Settings.fireLevel) + "\n"
					+ Integer.toString(Settings.personLevel) + "\n"
					+ Integer.toString(Settings.hWaterLevel) + "\n"
					+ Integer.toString(Settings.lifeAppear) + "\n"
					+ Integer.toString(Settings.starAppear) + "\n"
					+ Integer.toString(Settings.iceAppear) + "\n"
					+ Integer.toString(Settings.hWaterAppear) + "\n"
					+ Integer.toString(Settings.starATime) + "\n"
					+ Integer.toString(Settings.iceATime) + "\n"
					+ Integer.toString(Settings.fireATime) + "\n"
					+ Integer.toString(Settings.pTurn) + "\n"
					+ Integer.toString(Settings.leTurn) + "\n"
					+ Integer.toString(Settings.hWaterSpeed) + "\n"
					+ Integer.toString(Settings.hWaterRad) + "\n"
					+ Integer.toString(Settings.pCoin) + "\n" +

					Boolean.toString(Settings.lifeOn) + "\n"
					+ Boolean.toString(Settings.invulnOn) + "\n"
					+ Boolean.toString(Settings.iceOn) + "\n"
					+ Boolean.toString(Settings.hWaterOn) + "\n"
					+ Boolean.toString(Settings.enemyOn) + "\n"
					+ Boolean.toString(Settings.lenemyOn) + "\n"
					+ Boolean.toString(Settings.fireOn) + "\n"
					+ Boolean.toString(Settings.personOn) + "\n";

			fos.write(s.getBytes());

			fos.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

package com.bme.zombierunner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PowerUps extends Activity implements OnClickListener {

	TextView tvLifeLevel, tvInvulnLevel, tvIceLevel, tvhWaterLevel, tvLifeTime,
			tvInvulnTime, tvIceTime, tvhWaterTime, tvInvulnATime, tvIceATime,
			tvhWaterSpeed, tvhWaterRad;

	Button bDefaults, bLife, bInvuln, bIce, bhWater;

	EditText etLifeLevel, etInvulnLevel, etIceLevel, ethWaterLevel, etLifeTime,
			etInvulnTime, etIceTime, ethWaterTime, etInvulnATime, etIceATime,
			ethWaterSpeed, ethWaterRad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.powerups);

		tvLifeLevel = (TextView) findViewById(R.id.tvLifeLevel);
		tvInvulnLevel = (TextView) findViewById(R.id.tvInvulnLevel);
		tvIceLevel = (TextView) findViewById(R.id.tvIceLevel);
		tvhWaterLevel = (TextView) findViewById(R.id.tvhWaterLevel);
		tvLifeTime = (TextView) findViewById(R.id.tvLifeTime);
		tvInvulnTime = (TextView) findViewById(R.id.tvInvulnTime);
		tvIceTime = (TextView) findViewById(R.id.tvIceTime);
		tvhWaterTime = (TextView) findViewById(R.id.tvhWaterTime);
		tvInvulnATime = (TextView) findViewById(R.id.tvInvulnATime);
		tvIceATime = (TextView) findViewById(R.id.tvIceATime);
		tvhWaterSpeed = (TextView) findViewById(R.id.tvhWaterSpeed);
		tvhWaterRad = (TextView) findViewById(R.id.tvhWaterRad);

		bDefaults = (Button) findViewById(R.id.bDefaults);

		bLife = (Button) findViewById(R.id.bLife);
		bInvuln = (Button) findViewById(R.id.bInvuln);
		bIce = (Button) findViewById(R.id.bIce);
		bhWater = (Button) findViewById(R.id.bhWater);

		etLifeLevel = (EditText) findViewById(R.id.etLifeLevel);
		etInvulnLevel = (EditText) findViewById(R.id.etInvulnLevel);
		etIceLevel = (EditText) findViewById(R.id.etIceLevel);
		ethWaterLevel = (EditText) findViewById(R.id.ethWaterLevel);
		etLifeTime = (EditText) findViewById(R.id.etLifeTime);
		etInvulnTime = (EditText) findViewById(R.id.etInvulnTime);
		etIceTime = (EditText) findViewById(R.id.etIceTime);
		ethWaterTime = (EditText) findViewById(R.id.ethWaterTime);
		etInvulnATime = (EditText) findViewById(R.id.etInvulnATime);
		etIceATime = (EditText) findViewById(R.id.etIceATime);
		etInvulnATime = (EditText) findViewById(R.id.etInvulnATime);
		ethWaterSpeed = (EditText) findViewById(R.id.ethWaterSpeed);
		ethWaterRad = (EditText) findViewById(R.id.ethWaterRad);

		bDefaults.setOnClickListener(this);

		bLife.setOnClickListener(this);
		bInvuln.setOnClickListener(this);
		bIce.setOnClickListener(this);
		bhWater.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if(Integer.parseInt(etLifeLevel.getText().toString()) != Settings.lifeLevel)
			Settings.difficulty = 3;
		if(Integer.parseInt(etInvulnLevel.getText().toString()) != Settings.starLevel)
			Settings.difficulty = 3;
		if(Integer.parseInt(etIceLevel.getText().toString()) != Settings.iceLevel)
			Settings.difficulty = 3;
		if(Integer.parseInt(ethWaterLevel.getText().toString()) != Settings.hWaterLevel)
			Settings.difficulty = 3;
		if(Integer.parseInt(etLifeTime.getText().toString()) != Settings.lifeAppear)
			Settings.difficulty = 3;
		if(Integer.parseInt(etInvulnTime.getText().toString()) != Settings.starAppear)
			Settings.difficulty = 3;
		if(Integer.parseInt(etIceTime.getText().toString()) != Settings.iceAppear)
			Settings.difficulty = 3;
		if(Integer.parseInt(ethWaterTime.getText().toString()) != Settings.hWaterAppear)
			Settings.difficulty = 3;
		if(Integer.parseInt(etInvulnATime.getText().toString()) != Settings.starATime)
			Settings.difficulty = 3;
		if(Integer.parseInt(etIceATime.getText().toString()) != Settings.iceATime)
			Settings.difficulty = 3;
		if(Integer.parseInt(ethWaterSpeed.getText().toString()) != Settings.hWaterSpeed)
			Settings.difficulty = 3;
		if(Integer.parseInt(ethWaterRad.getText().toString()) != Settings.hWaterRad)
			Settings.difficulty = 3;

		Settings.lifeLevel = Integer.parseInt(etLifeLevel.getText().toString());
		Settings.starLevel = Integer.parseInt(etInvulnLevel.getText()
				.toString());
		Settings.iceLevel = Integer.parseInt(etIceLevel.getText().toString());
		Settings.hWaterLevel = Integer.parseInt(ethWaterLevel.getText()
				.toString());
		Settings.lifeAppear = Integer.parseInt(etLifeTime.getText().toString());
		Settings.starAppear = Integer.parseInt(etInvulnTime.getText()
				.toString());
		Settings.iceAppear = Integer.parseInt(etIceTime.getText().toString());
		Settings.hWaterAppear = Integer.parseInt(ethWaterTime.getText()
				.toString());
		Settings.starATime = Integer.parseInt(etInvulnATime.getText()
				.toString());
		Settings.iceATime = Integer.parseInt(etIceATime.getText().toString());
		Settings.hWaterSpeed = Integer.parseInt(ethWaterSpeed.getText()
				.toString());
		Settings.hWaterRad = Integer.parseInt(ethWaterRad.getText().toString());
	}

	@Override
	protected void onResume() {
		super.onResume();

		etLifeLevel.setText("" + Settings.lifeLevel);
		etInvulnLevel.setText("" + Settings.starLevel);
		etIceLevel.setText("" + Settings.iceLevel);
		ethWaterLevel.setText("" + Settings.hWaterLevel);
		etLifeTime.setText("" + Settings.lifeAppear);
		etInvulnTime.setText("" + Settings.starAppear);
		etIceTime.setText("" + Settings.iceAppear);
		ethWaterTime.setText("" + Settings.hWaterAppear);
		etInvulnATime.setText("" + Settings.starATime);
		etIceATime.setText("" + Settings.iceATime);
		ethWaterSpeed.setText("" + Settings.hWaterSpeed);
		ethWaterRad.setText("" + Settings.hWaterRad);

		tvLifeLevel.setText("Appears Every " + Settings.lifeLevel + " Levels");
		tvInvulnLevel
				.setText("Appears Every " + Settings.starLevel + " Levels");
		tvIceLevel.setText("Appears Every " + Settings.iceLevel + " Levels");
		tvhWaterLevel.setText("Appears Every " + Settings.hWaterLevel
				+ " Levels");
		tvLifeTime.setText("Appears For " + Settings.lifeAppear / 60.0f
				+ " Seconds");
		tvInvulnTime.setText("Appears For " + Settings.starAppear / 60.0f
				+ " Seconds");
		tvIceTime.setText("Appears For " + Settings.iceAppear / 60.0f
				+ " Seconds");
		tvhWaterTime.setText("Appears For " + Settings.hWaterAppear / 60.0f
				+ " Seconds");
		tvInvulnATime.setText("Active For " + Settings.starATime / 60.0f
				+ " Seconds");
		tvIceATime.setText("Active For " + Settings.iceATime / 60.0f
				+ " Seconds");
		tvhWaterSpeed.setText("Throwing Speed: " + Settings.hWaterSpeed);
		tvhWaterRad.setText("Splash Radius: " + Settings.hWaterRad);

		if(Settings.lifeOn)
			bLife.setText("Enabled");
		else
			bLife.setText("Disabled");
		if(Settings.invulnOn)
			bInvuln.setText("Enabled");
		else
			bInvuln.setText("Disabled");
		if(Settings.iceOn)
			bIce.setText("Enabled");
		else
			bIce.setText("Disabled");
		if(Settings.hWaterOn)
			bhWater.setText("Enabled");
		else
			bhWater.setText("Disabled");
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		case R.id.bDefaults:
			Settings.lifeLevel = 5;
			Settings.starLevel = 15;
			Settings.iceLevel = 10;
			Settings.hWaterLevel = 12;
			if(Settings.difficulty == 0) {
				Settings.lifeAppear = (int) (Settings.WSize.y / 2.96);
				Settings.iceAppear = (int) (Settings.WSize.y / 3.27);
				Settings.hWaterAppear = (int) (Settings.WSize.y / 3.41);
				Settings.starAppear = (int) (Settings.WSize.y / 3.55);
				Settings.starATime = (int) (Settings.WSize.y / 1.72);
				Settings.iceATime = (int) (Settings.WSize.y / 1.72);
			}
			if(Settings.difficulty == 1) {
				Settings.lifeAppear = (int) (Settings.WSize.y / 4.44);
				Settings.iceAppear = (int) (Settings.WSize.y / 4.9);
				Settings.hWaterAppear = (int) (Settings.WSize.y / 5.2);
				Settings.starAppear = (int) (Settings.WSize.y / 5.33);
				Settings.starATime = (int) (Settings.WSize.y / 2.58);
				Settings.iceATime = (int) (Settings.WSize.y / 2.58);
			}
			if(Settings.difficulty == 2) {
				Settings.lifeAppear = (int) (Settings.WSize.y / 6.66);
				Settings.iceAppear = (int) (Settings.WSize.y / 7.35);
				Settings.hWaterAppear = (int) (Settings.WSize.y / 7.675);
				Settings.starAppear = (int) (Settings.WSize.y / 8.0);
				Settings.starATime = (int) (Settings.WSize.y / 3.87);
				Settings.iceATime = (int) (Settings.WSize.y / 3.87);
			}

			etLifeLevel.setText("" + Settings.lifeLevel);
			etInvulnLevel.setText("" + Settings.starLevel);
			etIceLevel.setText("" + Settings.iceLevel);
			ethWaterLevel.setText("" + Settings.hWaterLevel);
			etLifeTime.setText("" + Settings.lifeAppear);
			etInvulnTime.setText("" + Settings.starAppear);
			etIceTime.setText("" + Settings.iceAppear);
			ethWaterTime.setText("" + Settings.hWaterAppear);
			etInvulnATime.setText("" + Settings.starATime);
			etIceATime.setText("" + Settings.iceATime);
			ethWaterSpeed.setText("" + Settings.hWaterSpeed);
			ethWaterRad.setText("" + Settings.hWaterRad);

			tvLifeLevel.setText("Appears Every " + Settings.lifeLevel
					+ " Levels");
			tvInvulnLevel.setText("Appears Every " + Settings.starLevel
					+ " Levels");
			tvIceLevel
					.setText("Appears Every " + Settings.iceLevel + " Levels");
			tvhWaterLevel.setText("Appears Every " + Settings.hWaterLevel
					+ " Levels");
			tvLifeTime.setText("Appears For " + Settings.lifeAppear / 60.0f
					+ " Seconds");
			tvInvulnTime.setText("Appears For " + Settings.starAppear / 60.0f
					+ " Seconds");
			tvIceTime.setText("Appears For " + Settings.iceAppear / 60.0f
					+ " Seconds");
			tvhWaterTime.setText("Appears For " + Settings.hWaterAppear / 60.0f
					+ " Seconds");
			tvInvulnATime.setText("Active For " + Settings.starATime / 60.0f
					+ " Seconds");
			tvIceATime.setText("Active For " + Settings.iceATime / 60.0f
					+ " Seconds");
			tvhWaterSpeed.setText("Throwing Speed: " + Settings.hWaterSpeed);
			tvhWaterRad.setText("Splash Radius: " + Settings.hWaterRad);
			break;
		case R.id.bLife:
			if(!Settings.lifeOn) {
				bLife.setText("Enabled");
				Settings.lifeOn = true;
			}
			else {
				bLife.setText("Disabled");
				Settings.lifeOn = false;
			}
			break;
		case R.id.bInvuln:
			if(!Settings.invulnOn) {
				bInvuln.setText("Enabled");
				Settings.invulnOn = true;
			}
			else {
				bInvuln.setText("Disabled");
				Settings.invulnOn = false;
			}
			break;
		case R.id.bIce:
			if(!Settings.iceOn) {
				bIce.setText("Enabled");
				Settings.iceOn = true;
			}
			else {
				bIce.setText("Disabled");
				Settings.iceOn = false;
			}
			break;
		case R.id.bhWater:
			if(!Settings.hWaterOn) {
				bhWater.setText("Enabled");
				Settings.hWaterOn = true;
			}
			else {
				bhWater.setText("Disabled");
				Settings.hWaterOn = false;
			}
			break;
		}
	}
}

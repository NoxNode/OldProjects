package com.bme.zombierunner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Enemies extends Activity implements OnClickListener {

	TextView tvEnemyLevel, tvLEnemyLevel, tvleTurn, tvFireLevel, tvFireATime,
			tvPersonLevel, tvpTurn, tvpCoin;

	Button bDefaults, bEnemy, bLEnemy, bFire, bPerson;

	EditText etEnemyLevel, etLEnemyLevel, etFireLevel, etPersonLevel,
			etFireATime, etleTurn, etpTurn, etpCoin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enemies);

		tvEnemyLevel = (TextView) findViewById(R.id.tvEnemyLevel);
		tvLEnemyLevel = (TextView) findViewById(R.id.tvLEnemyLevel);
		tvPersonLevel = (TextView) findViewById(R.id.tvPersonLevel);
		tvleTurn = (TextView) findViewById(R.id.tvleTurn);
		tvpTurn = (TextView) findViewById(R.id.tvpTurn);
		tvFireLevel = (TextView) findViewById(R.id.tvFireLevel);
		tvFireATime = (TextView) findViewById(R.id.tvFireATime);
		tvpCoin = (TextView) findViewById(R.id.tvpCoin);

		bDefaults = (Button) findViewById(R.id.bDefaults);

		bEnemy = (Button) findViewById(R.id.bEnemy);
		bLEnemy = (Button) findViewById(R.id.bLEnemy);
		bPerson = (Button) findViewById(R.id.bPerson);
		bFire = (Button) findViewById(R.id.bFire);

		etEnemyLevel = (EditText) findViewById(R.id.etEnemyLevel);
		etLEnemyLevel = (EditText) findViewById(R.id.etLEnemyLevel);
		etPersonLevel = (EditText) findViewById(R.id.etPersonLevel);
		etFireLevel = (EditText) findViewById(R.id.etFireLevel);
		etpTurn = (EditText) findViewById(R.id.etpTurn);
		etleTurn = (EditText) findViewById(R.id.etleTurn);
		etFireATime = (EditText) findViewById(R.id.etFireATime);
		etpCoin = (EditText) findViewById(R.id.etpCoin);

		bDefaults.setOnClickListener(this);

		bEnemy.setOnClickListener(this);
		bLEnemy.setOnClickListener(this);
		bPerson.setOnClickListener(this);
		bFire.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if(Integer.parseInt(etEnemyLevel.getText().toString()) != Settings.enemyLevel)
			Settings.difficulty = 3;
		if(Integer.parseInt(etLEnemyLevel.getText().toString()) != Settings.lenemyLevel)
			Settings.difficulty = 3;
		if(Integer.parseInt(etPersonLevel.getText().toString()) != Settings.personLevel)
			Settings.difficulty = 3;
		if(Integer.parseInt(etFireLevel.getText().toString()) != Settings.fireLevel)
			Settings.difficulty = 3;
		if(Integer.parseInt(etpTurn.getText().toString()) != Settings.pTurn)
			Settings.difficulty = 3;
		if(Integer.parseInt(etleTurn.getText().toString()) != Settings.leTurn)
			Settings.difficulty = 3;
		if(Integer.parseInt(etFireATime.getText().toString()) != Settings.fireATime)
			Settings.difficulty = 3;
		if(Integer.parseInt(etpCoin.getText().toString()) != Settings.pCoin)
			Settings.difficulty = 3;

		Settings.enemyLevel = Integer.parseInt(etEnemyLevel.getText()
				.toString());
		Settings.lenemyLevel = Integer.parseInt(etLEnemyLevel.getText()
				.toString());
		Settings.personLevel = Integer.parseInt(etPersonLevel.getText()
				.toString());
		Settings.fireLevel = Integer.parseInt(etFireLevel.getText().toString());
		Settings.pTurn = Integer.parseInt(etpTurn.getText().toString());
		Settings.leTurn = Integer.parseInt(etleTurn.getText().toString());
		Settings.fireATime = Integer.parseInt(etFireATime.getText().toString());
		Settings.pCoin = Integer.parseInt(etpCoin.getText().toString());
	}

	@Override
	protected void onResume() {
		super.onResume();

		etEnemyLevel.setText("" + Settings.enemyLevel);
		etLEnemyLevel.setText("" + Settings.lenemyLevel);
		etPersonLevel.setText("" + Settings.personLevel);
		etFireLevel.setText("" + Settings.fireLevel);
		etpTurn.setText("" + Settings.pTurn);
		etleTurn.setText("" + Settings.leTurn);
		etFireATime.setText("" + Settings.fireATime);
		etpCoin.setText("" + Settings.pCoin);

		tvEnemyLevel
				.setText("Appears Every " + Settings.enemyLevel + " Levels");
		tvLEnemyLevel.setText("Appears Every " + Settings.lenemyLevel
				+ " Levels");
		tvPersonLevel.setText("Appears Every " + Settings.personLevel
				+ " Levels");
		tvleTurn.setText("Turning Speed: " + (float) Settings.speed
				/ Settings.leTurn);
		tvpTurn.setText("Turning Speed: " + (float) Settings.speed
				/ Settings.pTurn);
		tvFireLevel.setText("Appears Every " + Settings.fireLevel + " Levels");
		tvFireATime.setText("Active For " + Settings.fireATime / 60.0f
				+ " Seconds");
		tvpCoin.setText("Points per save: " + Settings.pCoin);

		if(Settings.enemyOn)
			bEnemy.setText("Enabled");
		else
			bEnemy.setText("Disabled");
		if(Settings.lenemyOn)
			bLEnemy.setText("Enabled");
		else
			bLEnemy.setText("Disabled");
		if(Settings.personOn)
			bPerson.setText("Enabled");
		else
			bPerson.setText("Disabled");
		if(Settings.fireOn)
			bFire.setText("Enabled");
		else
			bFire.setText("Disabled");
	}

	@Override
	public void onClick(View v) {

		switch(v.getId()) {
		case R.id.bDefaults:
			Settings.enemyLevel = 1;
			Settings.lenemyLevel = 10;
			Settings.personLevel = 2;
			Settings.fireLevel = 5;
			Settings.pTurn = 40;
			Settings.leTurn = 80;
			Settings.pCoin = 3;

			if(Settings.difficulty == 0)
				Settings.fireATime = (int) (Settings.WSize.y / 1.72);
			if(Settings.difficulty == 1)
				Settings.fireATime = (int) (Settings.WSize.y / 2.58);
			if(Settings.difficulty == 2)
				Settings.fireATime = (int) (Settings.WSize.y / 3.87);

			etEnemyLevel.setText("" + Settings.enemyLevel);
			etLEnemyLevel.setText("" + Settings.lenemyLevel);
			etPersonLevel.setText("" + Settings.personLevel);
			etFireLevel.setText("" + Settings.fireLevel);
			etpTurn.setText("" + Settings.pTurn);
			etleTurn.setText("" + Settings.leTurn);
			etFireATime.setText("" + Settings.fireATime);

			tvEnemyLevel.setText("Appears Every " + Settings.enemyLevel
					+ " Levels");
			tvLEnemyLevel.setText("Appears Every " + Settings.lenemyLevel
					+ " Levels");
			tvPersonLevel.setText("Appears Every " + Settings.personLevel
					+ " Levels");
			tvleTurn.setText("Turning Speed: " + (float) Settings.speed
					/ Settings.leTurn);
			tvpTurn.setText("Turning Speed: " + (float) Settings.speed
					/ Settings.pTurn);
			tvFireLevel.setText("Appears Every " + Settings.fireLevel
					+ " Levels");
			tvFireATime.setText("Active For " + Settings.fireATime / 60.0f
					+ " Seconds");

			Settings.enemyOn = true;
			Settings.lenemyOn = true;
			Settings.personOn = true;
			Settings.fireOn = true;

			bEnemy.setText("Enabled");
			bLEnemy.setText("Enabled");
			bPerson.setText("Enabled");
			bFire.setText("Enabled");
			break;
		case R.id.bEnemy:
			if(!Settings.enemyOn) {
				bEnemy.setText("Enabled");
				Settings.enemyOn = true;
			}
			else {
				bEnemy.setText("Disabled");
				Settings.enemyOn = false;
			}
			break;
		case R.id.bLEnemy:
			if(!Settings.lenemyOn) {
				bLEnemy.setText("Enabled");
				Settings.lenemyOn = true;
			}
			else {
				bLEnemy.setText("Disabled");
				Settings.lenemyOn = false;
			}
			break;
		case R.id.bPerson:
			if(!Settings.personOn) {
				bPerson.setText("Enabled");
				Settings.personOn = true;
			}
			else {
				bPerson.setText("Disabled");
				Settings.personOn = false;
			}
			break;
		case R.id.bFire:
			if(!Settings.fireOn) {
				bFire.setText("Enabled");
				Settings.fireOn = true;
			}
			else {
				bFire.setText("Disabled");
				Settings.fireOn = false;
			}
			break;
		}
	}
}
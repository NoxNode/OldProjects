package com.bme.zombierunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Game extends Activity implements OnTouchListener {

	ZRSurface mySurfaceView;

	public static boolean twoPlayer = false;

	public static float input1[] = { 0, 0, 0, 0, 0, 0 };
	public static float input2[] = { 0, 0, 0, 0, 0, 0 };

	public static boolean touching1 = false, clicked1 = false,
			touching2 = false, clicked2 = false;

	public static int highscore = 0;

	public static final int mx = 0, my = 1, sx = 2, sy = 3, fx = 4, fy = 5;

	public static float getInput1(int i) {
		return input1[i];
	}

	public static float getInput2(int i) {
		return input2[i];
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mySurfaceView = new ZRSurface(this);
		mySurfaceView.setOnTouchListener(this);
		setContentView(mySurfaceView);

		twoPlayer = true;

		loadHighscore();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mySurfaceView.pause();
		saveHighscore();
		twoPlayer = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mySurfaceView.resume();
		twoPlayer = true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int pointerCount = event.getPointerCount();

		if(pointerCount == 1) {
			input1[0] = event.getX(0);
			input1[1] = event.getY(0);

			switch(event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				touching1 = true;
				clicked1 = true;
				input1[2] = event.getX(0);
				input1[3] = event.getY(0);
				break;
			case MotionEvent.ACTION_UP:
				touching1 = false;
				input1[4] = event.getX(0);
				input1[5] = event.getY(0);
				break;
			}
		}
		if(pointerCount == 2) {
			input1[0] = event.getX(0);
			input1[1] = event.getY(0);
			input2[0] = event.getX(1);
			input2[1] = event.getY(1);

			switch(event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				touching1 = true;
				clicked1 = true;
				input1[2] = event.getX(0);
				input1[3] = event.getY(0);
				break;
			case MotionEvent.ACTION_UP:
				touching1 = false;
				input1[4] = event.getX(0);
				input1[5] = event.getY(0);
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				touching2 = true;
				clicked2 = true;
				input2[2] = event.getX(1);
				input2[3] = event.getY(1);
				break;
			case MotionEvent.ACTION_POINTER_UP:
				touching2 = false;
				input2[4] = event.getX(1);
				input2[5] = event.getY(1);
				break;
			}
		}
		if(input1[my] < Settings.SCREENHEIGHT / 2
				&& input2[my] > Settings.SCREENHEIGHT / 2) {
			float[] temp = input1;
			input1 = input2;
			input2 = temp;
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		if(ZRSurface.paused || ZRSurface.gameOver) {
			super.onBackPressed();
			ZRSurface.paused = false;
			ZRSurface.gameOver = false;
		}
	}

	public void loadHighscore() {
		FileInputStream fis = null;

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
				switch(Settings.difficulty) {
				case 0:
					highscore = Integer.parseInt(line
							.substring(0, new_lines[0]));
					break;
				case 1:
					highscore = Integer.parseInt(line.substring(
							new_lines[0] + 1, new_lines[1]));
					break;
				case 2:
					highscore = Integer.parseInt(line.substring(
							new_lines[1] + 1, new_lines[2]));
					break;
				case 3:
					highscore = Integer.parseInt(line.substring(
							new_lines[2] + 1, new_lines[3]));
					break;
				}
			}

			fis.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void saveHighscore() {
		FileInputStream fis = null;

		int new_byte = 0;
		int index = 0;
		int nlindex = 0;
		int new_lines[] = new int[4];
		String line = "";

		try {
			fis = openFileInput("Highscore2");

			while((new_byte = fis.read()) != -1) {
				line = line + (char) new_byte;
				if(new_byte == '\n') {
					new_lines[index] = nlindex;
					index++;
				}
				nlindex++;
			}

			fis.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

		FileOutputStream fos = null;

		try {
			fos = openFileOutput("Highscore2", Context.MODE_PRIVATE);

			String s = "";

			if(line.contains("\n")) {
				s = line;
				ArrayList<Character> cal = new ArrayList<Character>();
				char[] ca = new char[s.length()];
				ca = s.toCharArray();
				for(int c = 0; c < ca.length; c++) {
					cal.add(ca[c]);
				}

				switch(Settings.difficulty) {
				case 0:
					String eh = "" + highscore;
					for(int i = 0; i < new_lines[0] - 0; i++) {
						cal.remove(0);
					}
					for(int cindex = 0; cindex < eh.length(); cindex++) {
						cal.add(0 + cindex, eh.charAt(cindex));
					}
					break;
				case 1:
					String mh = "" + highscore;
					for(int i = 0; i < new_lines[1] - (new_lines[0] + 1); i++) {
						cal.remove(new_lines[0] + 1);
					}
					for(int cindex = 0; cindex < mh.length(); cindex++) {
						cal.add(new_lines[0] + 1 + cindex, mh.charAt(cindex));
					}
					break;
				case 2:
					String hh = "" + highscore;
					for(int i = 0; i < new_lines[2] - (new_lines[1] + 1); i++) {
						cal.remove(new_lines[1] + 1);
					}
					for(int cindex = 0; cindex < hh.length(); cindex++) {
						cal.add(new_lines[1] + 1 + cindex, hh.charAt(cindex));
					}
					break;
				case 3:
					String ch = "" + highscore;
					for(int i = 0; i < new_lines[3] - (new_lines[2] + 1); i++) {
						cal.remove(new_lines[2] + 1);
					}
					for(int cindex = 0; cindex < ch.length(); cindex++) {
						cal.add(new_lines[2] + 1 + cindex, ch.charAt(cindex));
					}
					break;
				}
				line = cal.toString();
				s = "";

				for(int i = 0; i < line.length(); i++) {
					if(line.charAt(i) != '[' && line.charAt(i) != ']'
							&& line.charAt(i) != ',' && line.charAt(i) != ' ') {
						s = s + line.charAt(i);
					}
				}

				fos.write(s.getBytes());
			}
			else {
				s = 0 + "\n" + 0 + "\n" + 0 + "\n" + 0 + "\n";

				fos.write(s.getBytes());
			}

			fos.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

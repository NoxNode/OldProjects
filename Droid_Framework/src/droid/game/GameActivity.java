package droid.game;

import android.app.Activity;
import android.os.Bundle;
import droid.input.Input;

public class GameActivity extends Activity {

	Surface surface;
	Input input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		input = new Input(25, 5000);

		surface = new Surface(this, input);
		surface.setOnTouchListener(input);
		setContentView(surface);
	}

	@Override
	protected void onPause() {
		super.onPause();
		surface.stop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		surface.start();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}

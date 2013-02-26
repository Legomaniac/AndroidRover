package com.jimmyblaze.roverremote;

import com.jimmyblaze.roverremote.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private Button startJsButton;
	private EditText ip;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startJsButton = (Button) findViewById(R.id.joystickStartButton);
		ip = (EditText) findViewById(R.id.editText1);
		ip.setText("153.90.106.88");

		startJsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent joyIntent = new Intent(MainActivity.this, JSActivity.class);
				joyIntent.putExtra("incomingIP", ip.getText().toString());
				MainActivity.this.startActivity(joyIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}

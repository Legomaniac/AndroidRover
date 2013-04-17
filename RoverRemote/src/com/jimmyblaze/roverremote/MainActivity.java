package com.jimmyblaze.roverremote;

import com.jimmyblaze.roverremote.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private Button startJsButton;
	private EditText ip;
	private String savedIP;
	public static final String PREFS_NAME = "StoredPrefsFile";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startJsButton = (Button) findViewById(R.id.joystickStartButton);
		ip = (EditText) findViewById(R.id.editText1);
		ip.setText(savedIP);

		startJsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent joyIntent = new Intent(MainActivity.this, JSActivity.class);
				joyIntent.putExtra("incomingIP", ip.getText().toString());
				MainActivity.this.startActivity(joyIntent);
			}
		});
		
		// Restore preferences
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    savedIP = settings.getString("lastIP", "192.168.1.136");
	}
	
	protected void onStop(){
	      super.onStop();
	      // Saved preferences editor
	      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      editor.putString("lastIP", ip.getText().toString());

	      // Commit the edits!
	      editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}

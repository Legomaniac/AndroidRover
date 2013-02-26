package com.jimmyblaze.roverremote;

/* This source does not belong to me, it is under the new
 * BSD license located at http://opensource.org/licenses/BSD-3-Clause
 * 
 * The Google Code page for this open-source widget is at 
 * http://code.google.com/p/mobile-anarchy-widgets/
 */

import com.jimmyblaze.roverremote.R;
import com.jimmyblaze.roverremote.DualJoystickView;
import com.jimmyblaze.roverremote.JoystickMovedListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class JSActivity extends Activity {

	TextView txtX1, txtY1;
	TextView txtX2, txtY2;
	DualJoystickView joystick;
	private String ip;
	private UDPThread udpThread;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dualjoystick);

		txtX1 = (TextView)findViewById(R.id.TextViewX1);
        txtY1 = (TextView)findViewById(R.id.TextViewY1);
        
		txtX2 = (TextView)findViewById(R.id.TextViewX2);
        txtY2 = (TextView)findViewById(R.id.TextViewY2);
        
        Intent joyIntent = getIntent();
		ip = joyIntent.getStringExtra("incomingIP");
		
		udpThread = new UDPThread(ip);
		new Thread(udpThread).start();

        joystick = (DualJoystickView)findViewById(R.id.dualjoystickView);
        
        joystick.setOnJostickMovedListener(_listenerLeft, _listenerRight);
	}

    private JoystickMovedListener _listenerLeft = new JoystickMovedListener() {

		@Override
		public void OnMoved(int pan, int tilt) {
			txtX1.setText(Integer.toString(pan));
			txtY1.setText(Integer.toString(tilt));
			udpThread.setLeftX((pan/10)*127);
			udpThread.setLeftY((tilt/10)*127);
		}

		@Override
		public void OnReleased() {
			txtX1.setText("released");
			txtY1.setText("released");
		}
		
		public void OnReturnedToCenter() {
			txtX1.setText("stopped");
			txtY1.setText("stopped");
			udpThread.setLeftX(0);
			udpThread.setLeftY(0);
		};
	}; 

    private JoystickMovedListener _listenerRight = new JoystickMovedListener() {

		@Override
		public void OnMoved(int pan, int tilt) {
			txtX2.setText(Integer.toString(pan));
			txtY2.setText(Integer.toString(tilt));
			udpThread.setRightX((pan/10)*127);
			udpThread.setRightY((tilt/10)*127);
		}

		@Override
		public void OnReleased() {
			txtX2.setText("released");
			txtY2.setText("released");
		}
		
		public void OnReturnedToCenter() {
			txtX2.setText("stopped");
			txtY2.setText("stopped");
			udpThread.setRightX(0);
			udpThread.setRightY(0);
		};
	}; 
}

package com.jimmyblaze.roverserver;

import com.jimmyblaze.roverserver.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import ioio.lib.api.DigitalInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOService;

public class RoverService extends IOIOService {

	private volatile float rightXVal;
	private volatile float rightYVal;
	private volatile float leftXVal;
	private volatile float leftYVal;
	private float forwardBack;
	private float leftRight;
	private float spin;
	private float frontLeft;
	private float frontRight;
	private float rearLeft;
	private float rearRight;
	private float max;
	private UDPThread udpThread;
	private int NOTIFICATION_ID = 1;

	protected IOIOLooper createIOIOLooper() {
		return new BaseIOIOLooper() {
			
			private PwmOutput ch1pwm;
			private PwmOutput ch2pwm;
			private PwmOutput ch3pwm;
			private PwmOutput ch4pwm;
			
			private PwmOutput servoA;
			private PwmOutput servoB;
			
			private DigitalOutput ch1dir;
			private DigitalOutput ch2dir;
			private DigitalOutput ch3dir;
			private DigitalOutput ch4dir;
			
			private DigitalInput encA1;
			private DigitalInput encA2;
			private DigitalInput encB1;
			private DigitalInput encB2;
			private DigitalInput encC1;
			private DigitalInput encC2;
			private DigitalInput encD1;
			private DigitalInput encD2;
			

			@Override
			protected void setup() throws ConnectionLostException,InterruptedException 
			{
				ch1pwm = ioio_.openPwmOutput(3, 50);
				ch2pwm = ioio_.openPwmOutput(4, 50);
				ch3pwm = ioio_.openPwmOutput(5, 50);
				ch4pwm = ioio_.openPwmOutput(6, 50);
				
				servoA = ioio_.openPwmOutput(48, 50);
				servoB = ioio_.openPwmOutput(47, 50);
				
				ch1dir = ioio_.openDigitalOutput(22);
				ch2dir = ioio_.openDigitalOutput(23);
				ch3dir = ioio_.openDigitalOutput(24);
				ch4dir = ioio_.openDigitalOutput(25);
				
				encA2 = ioio_.openDigitalInput(7);
				encA1 = ioio_.openDigitalInput(10);
				encB2 = ioio_.openDigitalInput(11);
				encB1 = ioio_.openDigitalInput(12);
				encC2 = ioio_.openDigitalInput(13);
				encC1 = ioio_.openDigitalInput(14);
				encD2 = ioio_.openDigitalInput(18);
				encD1 = ioio_.openDigitalInput(19);
			}

			@Override
			public void loop() throws ConnectionLostException,InterruptedException {
				rightXVal = ((float)udpThread.getRightX())/127;
				rightYVal = ((float)udpThread.getRightY())/127;
				leftXVal = ((float)udpThread.getLeftX())/127;
				leftYVal = ((float)udpThread.getLeftY())/127;
				
				// Motor control logic section
			
				// For 2 joysticks controlling drive
				forwardBack = -rightYVal;
				leftRight = rightXVal;
				spin = leftXVal;
				
				// Front Left
				frontLeft = forwardBack + spin + leftRight;
				// Front Right
				frontRight = forwardBack - spin - leftRight;
				// Rear Left
				rearLeft = forwardBack + spin - leftRight;
				// Rear Right
				rearRight = forwardBack - spin + leftRight;
				
				// Normalize
				max = Math.abs(frontLeft);
				
				if (Math.abs(frontRight) > max)
					max = Math.abs(frontRight);
				if (Math.abs(rearLeft) > max)
					max = Math.abs(rearLeft);
				if (Math.abs(rearRight) > max)
					max = Math.abs (rearRight);
				
				if (max > 1)
				{
					frontLeft /= max;
					frontRight /= max;
					rearLeft /= max;
					rearRight /= max;
				}
				
				// Send output to motors
				
				// Channel One
				if (frontLeft >= 0)
					ch1dir.write(true);
				else
					ch1dir.write(false);
				ch1pwm.setDutyCycle(Math.abs(frontLeft));
				
				// Channel Two
				// These booleans are the opposite of the others due to crap wiring from Dagu
				if (frontRight >= 0)
					ch2dir.write(false);   
				else
					ch2dir.write(true);    
				ch2pwm.setDutyCycle(Math.abs(frontRight));
				
				// Channel Three
				if (rearLeft >= 0)
					ch3dir.write(true);
				else
					ch3dir.write(false);
				ch3pwm.setDutyCycle(Math.abs(rearLeft));
				
				// Channel Four
				if (rearRight >= 0)
					ch4dir.write(true);
				else
					ch4dir.write(false);
				ch4pwm.setDutyCycle(Math.abs(rearRight));
				
				Thread.sleep(30);
			}
		};
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		udpThread = new UDPThread();
		new Thread(udpThread).start();
		
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		if (intent != null && intent.getAction() != null && intent.getAction().equals("stop")) {
			// User clicked the notification. Need to stop the service.
			nm.cancel(0);
			stopSelf();
		} else {
			// Service starting. Create a notification.
			Notification notification = new Notification(R.drawable.ic_launcher, "Android Car service running", System.currentTimeMillis());
			notification.setLatestEventInfo(this, "Android Car Service", "Click to stop",
							PendingIntent.getService(this, 0, new Intent("stop", null, this, this.getClass()), 0));
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			startForeground(NOTIFICATION_ID, notification);
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}

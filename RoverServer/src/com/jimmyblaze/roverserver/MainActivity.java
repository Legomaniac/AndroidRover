package com.jimmyblaze.roverserver;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.jimmyblaze.roverserver.R;

public class MainActivity extends Activity {
	private Button startButton;
	private Button startService;
	private TextView displayIP;
	private EditText skypeName;
	private String skypeInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.button1);
        startService = (Button) findViewById(R.id.button2);
        displayIP = (TextView) findViewById(R.id.textView3);
        skypeName = (EditText) findViewById(R.id.editText1);
        
        displayIP.setText(getLocalIpAddress());
        
        final SkypeHandler skypeHandler = new SkypeHandler();
        
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	skypeInput = skypeName.getText().toString();
            	skypeHandler.initiateSkypeUri(getBaseContext(), "skype:" + skypeInput + "?call&video=true");
            }
          });
        
        startService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent serviceIntent = new Intent(MainActivity.this, RoverService.class);
            	startService(serviceIntent);
            }
          });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    //http://www.droidnova.com/get-the-ip-address-of-your-device,304.html
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IP Getter:", ex.toString());
        }
        return "No IP Found, please try again!";
    }
}

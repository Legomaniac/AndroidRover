package com.jimmyblaze.roverserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import android.util.Log;

public class UDPThread implements Runnable {
	
	Thread t;
	private int rightYIn;
	private int rightXIn;
	private int leftXIn;
	private int leftYIn;
	private int panTiltIn;

	//UDP variables
	int thePort = 9002;
	byte[] incomingBytes = new byte[5];
	DatagramSocket s;
	DatagramPacket p;

	public UDPThread()
	{
		try 
        {
        	t = new Thread(this);
        	t.start();
        } 
        catch (Exception e){e.printStackTrace();}
        
        try {
			s = new DatagramSocket(thePort);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.d("UDP Thread: ", "At least got to the constructor");
	}

	@Override
	public void run() {
		try {
			doTheUDPBro();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doTheUDPBro() throws InterruptedException
	{
		while (true)
		{	
			p = new DatagramPacket(incomingBytes, incomingBytes.length);

			try {
				s.receive(p);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
			}
			
			rightXIn = (int)incomingBytes[0];
			rightYIn = (int)incomingBytes[1];
			leftXIn = (int)incomingBytes[2];
			leftYIn = (int)incomingBytes[3];
			panTiltIn = (int)incomingBytes[4];
			
//			Log.d("UDP Receive: ", "rightXVal: " + rightXIn);
//		    Log.d("UDP Receive: ", "rightYVal: " + rightYIn);
//			Log.d("UDP Receive: ", "leftXVal: " + leftXIn);
//			Log.d("UDP Receive: ", "leftYVal: " + leftYIn);
//			Log.d("UDP Receive: ", "panTiltVal: " + panTiltIn);
			
    		Thread.sleep(30);    		
		}
	}
	
	public int getRightY()
	{
		return rightYIn;
	}
	
	public int getRightX()
	{
		return rightXIn;
	}
	
	public int getLeftX()
	{
		return leftXIn;
	}
	
	public int getLeftY()
	{
		return leftYIn;
	}
	
	public boolean getPanTilt()
	{
		if (panTiltIn == 0)
			return false;
		else
			return true;
	}
}
package com.jimmyblaze.roverremote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

public class UDPThread implements Runnable {
	
	Thread t;
	private int rightX = 0;
	private int rightY = 0;
	private int leftX = 0;
	private int leftY = 0;
	
	//UDP variables
	int thePort = 9002;
	byte[] outgoingBytes = new byte[4];
	DatagramSocket s;
	DatagramPacket p;
	InetAddress carAddr;
	String ip;

	
	public UDPThread(String ipIn)
	{
		ip = ipIn;
		
		try 
        {
        	t = new Thread(this);
        	t.start();
        } 
        catch (Exception e){e.printStackTrace();}
        
    	try {
			carAddr = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			s = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.d("UDP Constructor: ", "UDP thread got this IP: " + ip);
	}

	@Override
	public void run() {
		try {
			doTheUDPBro();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doTheUDPBro() throws InterruptedException, IOException
	{
		while (true)
		{	
			outgoingBytes[0] = (byte) rightX;
			outgoingBytes[1] = (byte) rightY;
			outgoingBytes[2] = (byte) leftX;
			outgoingBytes[3] = (byte) leftY;
			
			p = new DatagramPacket(outgoingBytes, outgoingBytes.length, carAddr, thePort);
			
			s.send(p);
	
    		Thread.sleep(35);
		}
	}
	
	public void setRightY(int inRightY)
	{
		rightY = inRightY;
	}
	
	public void setRightX(int inRightX)
	{
		rightX = inRightX;
	}
	
	public void setLeftX(int inLeftX)
	{
		leftX = inLeftX;
	}
	
	public void setLeftY(int inLeftY)
	{
		leftY = inLeftY;
	}
}
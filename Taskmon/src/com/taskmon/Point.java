package com.taskmon;

import android.util.Log;

public class Point {
	
	private long u; //utilization in percentage
	private long t; //time-stamp
	private static long min = 0;
	
	public Point( long x, long y){
		
		this.u = x;
		this.t = y;
		
	}
	
	public Point( long x, long y, long T)
	{

		x = x/1000000; //convert utilization to milliseconds
		
		if(x == T)
			this.u = 100;
		else
			this.u = (long) (((float)x/(float)T)*100); // percentage
		
		this.t = y; //convert time-stamp to seconds
		
		Log.d("POINT","C : "+ this.u + " T : "+this.t);
	}
	
	public long getU() {
		return u;
	}

	public long getT() {
		return t;
	}
	
}

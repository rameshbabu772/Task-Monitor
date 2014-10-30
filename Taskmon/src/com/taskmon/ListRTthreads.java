package com.taskmon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.util.Log;

public class ListRTthreads {
	
	static private ArrayList<String> rtthreads= new ArrayList<String>();
	private String RtlistPath ="/home/rkuttubo/workspace";
	Thread thread;
	String TAG = "ListRTthreads";
	
	public void getthreadlist()
	{
		
       if(thread==null)
       {
		thread = new Thread() {
			public void run()
			{
				while (true) 
				{
					try {
						Thread.sleep(10000);
						if(rtthreads.isEmpty()==false)
						{
							rtthreads.clear();
						}
						
						UpdateRTthreadList();
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d(TAG,"Size" + rtthreads.size());		
					
				}
			}
		};
		thread.start();	

       }
		
	}
	
	
	public int UpdateRTthreadList()
	{
		
		String Threadid = null;
		BufferedReader reader;
		
		try {
			
			reader = new BufferedReader(new FileReader(this.RtlistPath));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Log.d(TAG,"FileNotFound Exception");
			return -1;
		}
		
		//read
		try {
			
			while((Threadid = reader.readLine()) != null){
				
				rtthreads.add(Threadid);
			}
		
			}catch (IOException e) {
		    // TODO Auto-generated catch block
		     Log.d(TAG,"IO Exception : readLine");
		    //e.printStackTrace();
		   }
		
		//close
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.d(TAG,"IO Exception :reader close");
					//e.printStackTrace();
				}
	
		return 0;
	

}

}
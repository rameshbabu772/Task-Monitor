package com.taskmon;
import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;

import com.taskmon.*;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Monitor extends Activity {

	private static GraphicalView view;
	private  static LineGraph line = new LineGraph("util");
	private  Thread thread = null;
	private static final String TAG = "ProcessData";

	static private ArrayList<ProcessData> plist= new ArrayList<ProcessData>();	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.util_display);
		Log.d(TAG,"In SetUtil");		

		        	if(thread == null){
						thread = new Thread() {
						 public void run()
						 {
							while (true) 
							{
							  try {
							  Thread.sleep(1000);
								} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								}
								Log.d(TAG,"Size" + plist.size());		

								for(int j = 0; j < plist.size();j++){

								if(plist.get(j).updateUtilDataSet(view) == -1){
								line.removeRenderer(plist.get(j).getUtilRenderer());
								line.removeDataset(plist.get(j).getUtilDataset());
								plist.remove(j);
								j--;
								}	

										}
															
										
									}
								}
							};
							thread.start();	

		

		       } else {
		            // The toggle is disabled
		        	thread.interrupt();
		        }

	}
	
	
	public static void add(int pid,long C,long T,int CPUID){
		ProcessData p;
		if((p = getProcessFromPid(pid)) == null){
			
			Log.d(TAG,"New PID :"+pid+"C :"+C+"T :"+T);		
			
			p = new ProcessData(pid,C,T,CPUID);
			plist.add(p);

			line.addDataset(p.getUtilDataset());
			line.addRenderer(p.getUtilRenderer());
		}else
		{
			p.setC(C);
			p.setT(T);

		}
	}

	public static ProcessData getProcessFromPid(int pid){
		for(int i = 0; i < plist.size();i++){
			if(plist.get(i).getPid() == pid){
				return plist.get(i);
			}
		}
		return null;
	}

	public static boolean exist(int pid){
		boolean exist = false;
		for(int i = 0; i < plist.size();i++){
			if(plist.get(i).getPid() == pid){
				exist = true;
				break;		
			}
		}
		return exist;
	}

	public static LineGraph getLine(){
		return line;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		view = line.getView(this);
		setContentView(view);
	}

}

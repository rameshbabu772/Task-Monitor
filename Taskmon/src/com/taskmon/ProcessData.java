package com.taskmon;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.util.Log;

public class ProcessData {
	
	private static final String TAG = "ProcessData";
	private int pid;
	private long C;
	private long T;
	private int CPUID;
	private TimeSeries utilDataset;
	private XYSeriesRenderer utilRenderer;
	private String utilFilePath;
	
	static int x=0;
	static int y=0;
	
	public ProcessData(int pid,long c,long t,int CPUID) {
		super();
		this.pid = pid;
		this.C = c ; // in ms
		this.T = t ; // in ms
		this.CPUID=CPUID;
		
		Log.d(TAG,"got pid :"+pid+"C :"+C+"T :"+T);	
		
	
	    this.utilFilePath = String.format("/sys/rtes/taskmon/util/%d",this.pid);
		//this.utilFilePath = String.format("/home/rkuttubo/workspace",this.pid);
		
	    Log.d(TAG, "File path 0: " + this.utilFilePath);
	
		this.utilDataset = new TimeSeries(Integer.toString(pid));
		
		this.utilRenderer = new XYSeriesRenderer();
		
		Random n = new Random();

		utilRenderer.setColor(Color.rgb((pid%(n.nextInt(200) + 1)+n.nextInt()),((pid%(n.nextInt(150) + 1)+n.nextInt(100))), pid%(n.nextInt(50) + 1)));
		utilRenderer.setPointStyle(PointStyle.SQUARE);
		utilRenderer.setFillPoints(true);
		
	}
	

	public void addNewUtilPoints(Point p)
	{
		this.utilDataset.add(p.getT(), p.getU());
	}
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}

	
	
	public XYSeries getUtilDataset() {
		return utilDataset;
	}
	
	public void setUtilDataset(TimeSeries dataset) {
		this.utilDataset = dataset;
	}
	
	public XYSeriesRenderer getUtilRenderer() {
		return utilRenderer;
	}
	
	public void setUtilRenderer(XYSeriesRenderer renderer) {
		this.utilRenderer = renderer;
	}
	
	public int updateUtilDataSet(GraphicalView view) {
	
		String line = null;
		BufferedReader reader;	
		//open
		try {
			
			reader = new BufferedReader(new FileReader(this.utilFilePath));
			Log.d(TAG, "File path: " + this.utilFilePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG,"FileNotFound Exception");
			return -1;
		}
		
		//read
		try {
			int i=0;
			while((line = reader.readLine()) != null){
				Scanner S= new Scanner(line);
				
				String time=S.next();
				String Util=S.next();
				
				int t =Integer.parseInt(time);
				float u=Float.parseFloat(Util);
				
				Log.d(TAG,"U :"+u+"T :"+t);
				
			    u = u*100; // scaling to 100
				
				/*try {
		 		Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				Log.d(TAG,"1");
		
				//Log.d(TAG,"C :"+c+"T :"+t);
				this.utilDataset.add(t, u);
	
				
				Log.d(TAG,"3");
	     		
			
				
	     		Log.d(TAG,"u :"+u+"T :"+t);
				Log.d(TAG, "Data : " + line);	
				Log.d(TAG,"i count:"+i);
				i++;
			}
			view.repaint();
			
			Log.d(TAG,"Broken out of while");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"IO Exception : readLine");
			e.printStackTrace();
		}
		
		//close
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"IO Exception :reader close");
			//e.printStackTrace();
		}
		
		
		return 1;
	}
	
		
	
	public long getC() {
		return C;
	}
	public void setC(long c) {
		C = c;
	}
	public long getT() {
		return T;
	}
	public void setT(long t) {
		T = t;
	}
	
	public long getcpuid() {
		return T;
	}
	public void setcpuid(int cpuid) {
		CPUID = cpuid;
	}

}

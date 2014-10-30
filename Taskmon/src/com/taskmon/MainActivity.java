package com.taskmon;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	private int pid;
	private long C;
	private long T;
	private int CPUID;
	private Button Set_button;
	private Button Cancel_button;
	private Button Monitor_button;
	private Button Display_button;
	private EditText Pid_text;
	private EditText C_text;
	private EditText T_text;
	private EditText CPUid_text;
	private Spinner list_spinner;
	private ToggleButton toggle_mon;
	//private ListView rtthreads; 
	final ArrayList<String> list = new ArrayList<String>();
	//ArrayAdapter<String> adapter;
	String TAG ="MAIN ACTIVITY"; 
	public String EnablePath= "/sys/rtes/taskmon/enabled";
	
	public MainActivity(){
		
	}
	public MainActivity(int pid, long c, long t,int cpuid) {
		this.pid = pid;
		C = c;
		T = t;
		CPUID=cpuid;
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//assign their respective id's [Start]
		Pid_text=(EditText)findViewById(R.id.pid_text);
		C_text=(EditText)findViewById(R.id.c_text);
		T_text=(EditText)findViewById(R.id.t_text);
		CPUid_text=(EditText)findViewById(R.id.cpuid_text);
		
		Set_button=(Button)findViewById(R.id.set_button);
		Cancel_button=(Button)findViewById(R.id.cancel_button);
		//assign their respective id's [End]
		
		//Attempt to add spinner list of threads to main Activity
	//	spinner1 = (Spinner) findViewById(R.id.listthreads_spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list ); 
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ArrayList<String> list = new ArrayList<String>();
		list_spinner = (Spinner)findViewById(R.id.spinner1);
		list_spinner.setAdapter(adapter);
	   // list_spinner.setTextFilterEnabled(true);
		toggle_mon = (ToggleButton) findViewById(R.id.toggle_monitor);
		
		list_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				
				//image.setImageResource(imgs.getResourceId(list_spinner.getSelectedItemPosition(), -1));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		
		
	// Set reservation button listener and setting the values in kernel through JNI	
		Set_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			
				if((Pid_text.getText().toString()).equals(""))
				{
				Toast.makeText(getApplicationContext(), "Please enter Thread ID ", Toast.LENGTH_SHORT).show();
				return;
				}
				else if((C_text.getText().toString()).equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please Enter Budget [c]", Toast.LENGTH_SHORT).show();
					return;
				}
				else if((T_text.getText().toString()).equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter Time Period [T]", Toast.LENGTH_SHORT).show();
					return;
				}
				else if((CPUid_text.getText().toString()).equals(""))
				{
					Toast.makeText(getApplicationContext(), "Please enter CPU core ID", Toast.LENGTH_SHORT).show();
					return;
				}
				
				
			    pid=Integer.parseInt(Pid_text.getText().toString());
			    C=Long.parseLong(C_text.getText().toString());
			    T=Long.parseLong(T_text.getText().toString());
			    CPUID=Integer.parseInt(CPUid_text.getText().toString());
			    
			    if(CPUID>3)
			    {
			    	Toast.makeText(getApplicationContext(), "CPU ID should be between 0-3", Toast.LENGTH_SHORT).show();
			    	return;
			    }
				setPid(pid);
				setC(C);
				setT(T);
				setcpuid(CPUID);
				
				Toast.makeText(getApplicationContext(), "PID:"+pid, Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), ""+C,Toast.LENGTH_LONG).show();
				
				MainActivity data= new MainActivity(pid,C,T,CPUID);
			    int x= JniSetReserve(data);
			    
				if(pid > 0){
					//SetContext.add(pid, C,T,CPUID);
					Monitor.add(pid, C, T,CPUID);
				//	adapter.add(Pid_text.getText().toString());
				}else{
					Toast.makeText(getApplicationContext(),"Error !",Toast.LENGTH_LONG).show();
				}
				
			}
		} );
		
       
		Cancel_button.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			
			Bundle b = new Bundle();
			if((Pid_text.getText().toString()).equals(""))
			{
			Toast.makeText(getApplicationContext(), "Please enter PID for cancellation", Toast.LENGTH_SHORT).show();
			return;
			}
	
			pid=Integer.parseInt(Pid_text.getText().toString());
			MainActivity data= new MainActivity(pid,0,0,0);
			
			int x= JniCancelReserve(data);
			System.out.println("The value is"+x);
			
			if(x==1){
		//	DisplayTable.remove(pid);
			Toast.makeText(getApplicationContext(), "PID:"+pid+ "cancelled!!", Toast.LENGTH_LONG).show();
			
			}
			else
				Toast.makeText(getApplicationContext(), "PID:"+pid+"not found", Toast.LENGTH_LONG).show();	
		}
	}); 
		
		
	//Getting a list of threads and display on right side of selecting thread id	
	
	/*rtthreads.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			    // When clicked, show a toast with the TextView text
			   // Toast.makeText(getApplicationContext(),((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});*/
		
	
		
		
		
		//final int fail=0;
		
		
	//Toggle button for starting and Stoping Task Monitor
		
		toggle_mon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    	  
		    	  Log.d(TAG, "whats the cause for null pointer exception" );
		    	 /* BufferedWriter bw = null;
		    	  try {
					bw= new BufferedWriter(new FileWriter(EnablePath));
					Log.d(TAG, "File path: " + EnablePath);
				} catch (IOException e1) {
					
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return ;
				} */
		    if (isChecked) {  
		    	
		    	 /*try {	
		    		if(bw!=null)
		    		{
					//bw.write("1");
					Log.d(TAG, "Written one success " );
					bw.close();
		    		}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	*/
		    
		    } else {
		    	/*try {
		    		if(bw!=null)
		    		{
				//	bw.write("0");
					Log.d(TAG, "Written zero success " );
					bw.close();
		    		}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
		    	
		    	Intent monitor = new Intent(getApplicationContext(),Monitor.class);
				startActivity(monitor);
		    }
		      }
		  });
		
		
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
	
	//jni interface related starts
	 private static String[] StringBuf;
     private native String checkjni(); //testpublic  
     public native int  JniSetReserve(MainActivity data); //calls set_reserve system call	
   	 public native int  JniCancelReserve(MainActivity data); // calls cancel_reserve system call
   	 public native int  JNICountRtThreads(); //calls set_reserve system call	
   	 public native String[]  JNIlistRtThreads(int count); // calls cancel_reserve system call
	
	 static {
		 System.loadLibrary("taskmon-jni");
	    } 
    //Jni interface related ends
	 
	 //Methods for parameters set and get [Starts]
	 public int getPid() {
			return pid;
		}


		public void setPid(int pid) {
			this.pid = pid;
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

		public int getcpuid() {
			return CPUID;
		}

		public void setcpuid(int cpuid) {
			CPUID = cpuid;
		}
		
	//Methods for parameters set and get [Ends]
	 
}

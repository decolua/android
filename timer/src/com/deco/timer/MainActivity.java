package com.deco.timer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



import android.widget.TextView;
import android.os.SystemClock;


public class MainActivity extends Activity {

	TextView hTextView;
	final Handler myHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hTextView = (TextView)findViewById(R.id.mytext);
		
		Timer myTimer = new Timer();
		MyTimerTask myTask = new MyTimerTask();
		myTimer.schedule(myTask, 3000, 1500);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class Watcher implements Observer { 
		public void update(Observable obj, Object arg) {
			String aa = (String)arg;
			TextView _mytext = (TextView)findViewById(R.id.mytext);
			_mytext.setText(aa);
		} 
	}	
	
	class MyTimerTask extends TimerTask {
		 public void run() {
			myHandler.post(myRunnable);
		 }
	}		
	
	final Runnable myRunnable = new Runnable() {
	      public void run() {
	        long timeInMilliseconds = SystemClock.uptimeMillis();
	  		BeingWatched observed = new BeingWatched(); 
			Watcher observing = new Watcher(); 
			observed.addObserver(observing); 
			observed.Request(Long.toString(timeInMilliseconds));
			observing = null;
			observed = null;
	      }
	};	
	
}

class BeingWatched extends Observable { 
	void Request(String URL){
		setChanged();
		notifyObservers(URL);
	}
}

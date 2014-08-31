package com.deco.parsetime;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import android.app.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        String strDate = "2014-08-26 03:00:00 +08:00";
        try {
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        	Date date = df.parse(strDate);
        	String formattedDate = df.format(date);
        	
        	df = new SimpleDateFormat("HH:mm");
        	String formattedDate1 = df.format(date);
        	
            int a = 10;
            int b = a;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

package com.deco.dbsql;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import com.deco.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		

		HashMap<String, String> lsData = new HashMap<String, String>();
		lsData.put("team_id","1");
		lsData.put("league_id","2");
		lsData.put("team_name","MU");
		lsData.put("team_short_name","MU");
		lsData.put("team_city","Manchester");
		lsData.put("team_stadium","Old");
		lsData.put("team_avatar","");
		lsData.put("team_fans_num","111");
		lsData.put("team_visible","1");
	    
		
        DatabaseHandler db = new DatabaseHandler(this);
        //db.add(lsData);
        
        /**
         * CRUD Operations
         * */
        // Inserting Contacts
        Log.d("Insert: ", "Inserting .."); 
        db.addContact(new Contact("Ravi", "9100000000"));        
        db.addContact(new Contact("Srinivas", "9199999999"));
       // db.addContact(new Contact("Tommy", "9522222222"));
       // db.addContact(new Contact("Karthik", "9533333333"));
         
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts.."); 
        List<Contact> contacts = db.getAllContacts();       
         
        for (Contact cn : contacts) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            Log.d("Name: ", log);		
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

package com.deco.football;

import java.util.ArrayList;

import com.deco.adapter.MatchAdapter;
import com.deco.adapter.MatchInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        setContentView(R.layout.activity_main);
        
        ArrayList<MatchInfo> lsMatchItem;
        lsMatchItem = new ArrayList<MatchInfo>();
        MatchInfo item = new MatchInfo();
        item.league_id = 1;
        item.league_name = "Friends";
        lsMatchItem.add(item); 
        item = new MatchInfo();
        item.match_id = 2;
        item.time = "2:30";
        item.home = "MU";
        item.away = "MC";
        item.homegoals = "1";
        item.awaygoals = "1";
        lsMatchItem.add(item);
        item = new MatchInfo();
        item.match_id = 3;
        item.time = "3:30";
        item.home = "Asernal";
        item.away = "Newcastle";
        item.homegoals = "1";
        item.awaygoals = "1";
        lsMatchItem.add(item);
        item = new MatchInfo();
        item.league_id = 1;
        item.league_name = "Primier";
        lsMatchItem.add(item);
        item = new MatchInfo();
        item.match_id = 3;
        item.time = "3:30";
        item.home = "Asernal";
        item.away = "Newcastle";
        item.homegoals = "1";
        item.awaygoals = "1";
        lsMatchItem.add(item);
        
        
        MatchAdapter adapter = new MatchAdapter(this, 0, lsMatchItem);
        ListView listView = (ListView)findViewById(R.id.matchlist);
        listView.setAdapter(adapter); 		
        
        MatchAdapter adapter1 = (MatchAdapter)listView.getAdapter();
        ArrayList<MatchInfo> match = adapter1.lsMatchItem;
        MatchInfo matchinfo = match.get(0);
        int a = matchinfo.league_id;
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

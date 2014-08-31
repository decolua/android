package com.deco.football;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import com.deco.adapter.MatchAdapter;
import com.deco.model.MatchModel;
import com.deco.model.UserModel;
import com.deco.service.MatchService;
import com.deco.sql.MATCH;
import com.deco.sql.USER;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class LivingActivity extends Activity {

	final Handler myHandler = new Handler();
	MatchAdapter _adapter;
	HashMap<String, String> _pUser = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_living);
		
		// Get Current User
		UserModel mdlUser = new UserModel(this);
		_pUser = mdlUser.getLastUser();
		if (_pUser.size() == 0){
			_pUser.put(USER.id, "17");
			_pUser.put(USER.token, "0d5c0616c1cf21465fd406bbe6aa7846");
		}
		
		
		// ListView Match
		ArrayList<HashMap<String, String>> lsMatch = new ArrayList<HashMap<String, String>>();
		_adapter = new MatchAdapter(this, 0, lsMatch);
		ListView listView = (ListView)findViewById(R.id.matchlist);
		listView.setAdapter(_adapter); 
		updateListView();
        
        // Init Timer
		Timer myTimer = new Timer();
		LivingMatchTimer getLivingMatchTimer = new LivingMatchTimer();
		myTimer.schedule(getLivingMatchTimer, 0, 10000);
		
		myTimer = new Timer();
		ComingMatchTimer getComingMatchTimer = new ComingMatchTimer();
		myTimer.schedule(getComingMatchTimer, 2000, 60000);		
	}
	
	public void updateListView(){
		MatchModel mdlMatch = new MatchModel(this);
		
		ArrayList<HashMap<String, String>>lsMatch = mdlMatch.getLiving();
		int nLeague = 0;
		for (int i=0; i<lsMatch.size(); i++){
			int tmp = Integer.parseInt(lsMatch.get(i).get(MATCH.league_id));
			if (nLeague != tmp){
				nLeague = tmp;
				HashMap<String, String> item = new HashMap<String, String>();
				item.put(MATCH.league_id, Integer.toString(nLeague));
				item.put(MATCH.id, "");
				lsMatch.add(i, item);
				i++;
			}
		}
		
		if (_adapter.getCount() > lsMatch.size())
			_adapter.clear();
		
		for (int i=_adapter.getCount(); i<lsMatch.size(); i++)
			_adapter.add(null);
		
		_adapter.lsMatch = lsMatch;
		_adapter.notifyDataSetChanged();
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
	
	class LivingMatchTimer extends TimerTask {
		 public void run() {
			myHandler.post(getLivingMatch);
		 }
	}
	
	class ComingMatchTimer extends TimerTask {
		 public void run() {
			myHandler.post(getComingMatch);
		 }
	}	
	
	final Runnable getLivingMatch = new Runnable() {
	      public void run() {
	    	  MatchService svMatch = new MatchService(getApplicationContext());
	    	  LivingMatchWatcher wtcMatch = new LivingMatchWatcher();
	    	  svMatch.addObserver(wtcMatch);
	    	  svMatch.getLivingMatch();		
	      }
	};
	
	final Runnable getComingMatch = new Runnable() {
	      public void run() {
	    	  MatchService svMatch = new MatchService(getApplicationContext());
	    	  LivingMatchWatcher wtcMatch = new LivingMatchWatcher();
	    	  svMatch.addObserver(wtcMatch);
	    	  svMatch.getComingMatch();		
	      }
	};		
	
	class LivingMatchWatcher implements Observer { 
		public void update(Observable obj, Object arg) {
			String data = (String)arg;
			if (data == "d"){
				updateListView();
				return;
			}			
		} 
	}		
}

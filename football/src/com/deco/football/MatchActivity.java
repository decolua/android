package com.deco.football;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.deco.model.MatchModel;
import com.deco.service.TeamImgService;
import com.deco.sql.MATCH;

public class MatchActivity extends Activity {
	private String _szHomeId = "";
	private String _szAwayId = "";
	private Context _context = this; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match);
		
		
	    Intent intent = getIntent();
	    String szMatchId = intent.getStringExtra("matchid");
	    
	    // Get Match Info From Database
	    MatchModel mdlModel = new MatchModel(this);
	    HashMap<String, String> matchInfo = mdlModel.getMatchById(szMatchId);
		_szHomeId = matchInfo.get(MATCH.team_home_id);
		_szAwayId = matchInfo.get(MATCH.team_away_id);
		String szHomeName = matchInfo.get(MATCH.home_name);
		String szAwayName = matchInfo.get(MATCH.away_name);
		String szFirstTime = matchInfo.get(MATCH.first_time);
		
		// Set Team Name 
		TextView homename = (TextView) findViewById(R.id.homename);
		TextView awayname = (TextView) findViewById(R.id.awayname);
		homename.setText(szHomeName);
		awayname.setText(szAwayName);
		
		// Set Time
        try {
	    	String szDate = "";
	    	String szTime = "";         	
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        	Date date = df.parse(szFirstTime);
        	df = new SimpleDateFormat("MMM dd yyyy");
        	szDate = df.format(date);
        	df = new SimpleDateFormat("HH:mm");
        	szTime = df.format(date);
    		TextView dateview = (TextView) findViewById(R.id.matchdate);
    		dateview.setText(szDate);
    		TextView timeview = (TextView) findViewById(R.id.matchtime);
    		timeview.setText(szTime);
        } catch (ParseException e) {
        }
	    
		// Set Image Avatar
		TeamImgService svImage = new TeamImgService(this);
		ImageWatcher wtcImage = new ImageWatcher();
		svImage.addObserver(wtcImage);

		if (!setTeamImage(_szHomeId, R.id.homeimg))
			svImage.getImageFromId(_szHomeId);
		
		if (!setTeamImage(_szAwayId, R.id.awayimg))
			svImage.getImageFromId(_szAwayId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.match, menu);
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
	
	public boolean setTeamImage(String szTeamId, int nViewId){
		String szFilePath = "team" + szTeamId + ".png";
		File imgFile = _context.getFileStreamPath(szFilePath);
		if(imgFile.exists()){
		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    if (myBitmap == null)
		    	return false;
		    
		    ImageView myImage = (ImageView) findViewById(nViewId);
		    myImage.setImageBitmap(myBitmap);
		    return true;
		}		
		return false;
	}
	
	class ImageWatcher implements Observer { 
		public void update(Observable obj, Object arg) {
			HashMap<String, String> result =  (HashMap<String, String>)arg;
			if (result.get("result")=="true"){
				String szTeamId = result.get("teamid");
				String szFilePath = "team" + szTeamId + ".png";
				File imgFile = _context.getFileStreamPath(szFilePath);
				if(imgFile.exists()){
				    if (szTeamId.equals(_szHomeId))
				    	setTeamImage(_szHomeId, R.id.homeimg);
				    else
				    	setTeamImage(_szAwayId, R.id.awayimg);
				}	
			}
		} 
	}		
}
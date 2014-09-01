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
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		
		TextView matchresult = (TextView) findViewById(R.id.matchresult);
		matchresult.setBackgroundColor(Color.RED);
		
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
	    
        // Set Odds List
        setOddsList(1);
        
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
		getMenuInflater().inflate(R.menu.match, menu);
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
	
    public void clickFunc(View view){
        int a = 1;
        int b = 2;
        b = a;
        a = b;
    }

	
	public void setOddsList(int nBetType){
		LinearLayout column1 = (LinearLayout)this.findViewById(R.id.oddscolumn1);
		LinearLayout column2 = (LinearLayout)this.findViewById(R.id.oddscolumn2);
		LinearLayout column3 = (LinearLayout)this.findViewById(R.id.oddscolumn3);
		
		// Insert Column 1
		for (int i=0; i<4; i++){
			for (int j=0; j<3; j++){
				View btnOdds = LayoutInflater.from(this).inflate(R.layout.odds_button, null);
				TextView oddsresult = (TextView)btnOdds.findViewById(R.id.oddsresult);
				String tmp = String.format("%d-%d", i+1, j);
				oddsresult.setText(tmp);
				TextView winback = (TextView)btnOdds.findViewById(R.id.winback);
				winback.setText("1/20");
				column1.addView(btnOdds);
				if (j >= i)
					break;
			}
		}
		
		// Insert Column 1
		for (int i=0; i<3; i++){
				View btnOdds = LayoutInflater.from(this).inflate(R.layout.odds_button, null);
				TextView oddsresult = (TextView)btnOdds.findViewById(R.id.oddsresult);
				String tmp = String.format("%d-%d", i, i);
				oddsresult.setText(tmp);
				TextView winback = (TextView)btnOdds.findViewById(R.id.winback);
				winback.setText("1/20");
				column2.addView(btnOdds);
		}	
		
		// Insert Column 1
		for (int i=0; i<4; i++){
			for (int j=0; j<3; j++){
				View btnOdds = LayoutInflater.from(this).inflate(R.layout.odds_button, null);
				TextView oddsresult = (TextView)btnOdds.findViewById(R.id.oddsresult);
				String tmp = String.format("%d-%d", j, i+1);
				oddsresult.setText(tmp);
				TextView winback = (TextView)btnOdds.findViewById(R.id.winback);
				winback.setText("1/20");
				column3.addView(btnOdds);
				if (j >= i)
					break;
			}
		}		
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

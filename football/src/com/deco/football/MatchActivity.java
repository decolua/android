package com.deco.football;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deco.helper.Helper;
import com.deco.model.MatchModel;
import com.deco.service.TeamImgService;
import com.deco.sql.MATCH;

public class MatchActivity extends Activity {
	private String _szHomeId = "";
	private String _szAwayId = "";
	private Context _context = this; 
	private int _handicap = 4;
	private int _homeback = 90;
	private int _awayback = 90;	
	
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
	    
        // Set Odds List
        setMatchResultOdds();
        
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
	
    public void onOddsBtnClick(View view){
    	LinearLayout panel = (LinearLayout)findViewById(R.id.betpanel);
    	panel.setVisibility(View.VISIBLE);
    	
    	Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
    	panel.startAnimation(slide);
    }		
	
	public void onTabClick(View view){
		LinearLayout tabs = (LinearLayout)findViewById(R.id.typetab);
		for (int i=0; i<tabs.getChildCount(); i++){
			TextView tab = (TextView)tabs.getChildAt(i);
			tab.setTextColor(Color.parseColor("#666666"));
			tab.setTypeface(null, Typeface.NORMAL);
		}
		
		TextView curTab = (TextView)view;
		curTab.setTextColor(Color.parseColor("#000000"));
		curTab.setTypeface(null, Typeface.BOLD);
		String tag = (String)curTab.getTag();
		int nIndex = Integer.parseInt(tag);
		switch (nIndex){
		case 0:
			setMatchResultOdds();
			break;
		case 1:
			setCorrectScoreOdds();
			break;
		case 2:
			setHandicapOdds();
			break;			
		}
	}	
	
    public void setMatchResultOdds()
    {
		LinearLayout column1 = (LinearLayout)this.findViewById(R.id.oddscolumn1);
		LinearLayout column2 = (LinearLayout)this.findViewById(R.id.oddscolumn2);
		LinearLayout column3 = (LinearLayout)this.findViewById(R.id.oddscolumn3);
		column1.removeAllViews();
		column2.removeAllViews();
		column3.removeAllViews();
		
		// Set Home Odds
		View btnOdds = LayoutInflater.from(this).inflate(R.layout.match_odds_button, null);
		TextView oddsresult = (TextView)btnOdds.findViewById(R.id.oddsresult);
		oddsresult.setText("Home");
		TextView winback = (TextView)btnOdds.findViewById(R.id.winback);
		double dbOdds = Helper.genMatchResult(_handicap, _homeback, _awayback, 0);
		winback.setText("1/" + String.valueOf(dbOdds));
		column1.addView(btnOdds);	
		
		// Set Home Odds
		btnOdds = LayoutInflater.from(this).inflate(R.layout.match_odds_button, null);
		oddsresult = (TextView)btnOdds.findViewById(R.id.oddsresult);
		oddsresult.setText("Draw");
		winback = (TextView)btnOdds.findViewById(R.id.winback);
		dbOdds = Helper.genMatchResult(_handicap, _homeback, _awayback, 2);
		winback.setText("1/" + String.valueOf(dbOdds));
		column2.addView(btnOdds);			
		
		// Set Away Odds
		btnOdds = LayoutInflater.from(this).inflate(R.layout.match_odds_button, null);
		oddsresult = (TextView)btnOdds.findViewById(R.id.oddsresult);
		oddsresult.setText("Away");
		winback = (TextView)btnOdds.findViewById(R.id.winback);
		dbOdds = Helper.genMatchResult(_handicap, _homeback, _awayback, 1);
		winback.setText("1/" + String.valueOf(dbOdds));
		column3.addView(btnOdds);				
    }    
    
    public void setHandicapOdds()
    {
		LinearLayout column1 = (LinearLayout)this.findViewById(R.id.oddscolumn1);
		LinearLayout column2 = (LinearLayout)this.findViewById(R.id.oddscolumn2);
		LinearLayout column3 = (LinearLayout)this.findViewById(R.id.oddscolumn3);
		column1.removeAllViews();
		column2.removeAllViews();
		column3.removeAllViews();

		// Set Home Odds
		View btnOdds = LayoutInflater.from(this).inflate(R.layout.match_odds_button, null);
		TextView oddsresult = (TextView)btnOdds.findViewById(R.id.oddsresult);
		oddsresult.setText("Home");
		TextView winback = (TextView)btnOdds.findViewById(R.id.winback);
		double tmp = 1 + (double)_homeback / 100;
		winback.setText("1/" + String.valueOf(tmp));
		column1.addView(btnOdds);	
		
		// Set Away Odds
		btnOdds = LayoutInflater.from(this).inflate(R.layout.match_odds_button, null);
		oddsresult = (TextView)btnOdds.findViewById(R.id.oddsresult);
		oddsresult.setText("Away");
		winback = (TextView)btnOdds.findViewById(R.id.winback);
		tmp = 1 + (double)_awayback / 100;
		winback.setText("1/" + String.valueOf(tmp));
		column3.addView(btnOdds);				
    }
    
	public void setCorrectScoreOdds(){
		LinearLayout column1 = (LinearLayout)this.findViewById(R.id.oddscolumn1);
		LinearLayout column2 = (LinearLayout)this.findViewById(R.id.oddscolumn2);
		LinearLayout column3 = (LinearLayout)this.findViewById(R.id.oddscolumn3);
		column1.removeAllViews();
		column2.removeAllViews();
		column3.removeAllViews();
		new LoadOdds().execute();
	}	
	
	class LoadOdds extends AsyncTask<Integer, Integer, Integer>{
	    @Override
	    protected Integer doInBackground(Integer... input) {
			for (int i=0; i<5; i++){
				for (int j=0; j<5; j++){
					Integer[] values = new Integer[] {i, j};
					publishProgress(values);
				}
			}	    	
	        return 0;
	    }
	    
        @Override
        protected void onProgressUpdate(Integer... values) {
    		LinearLayout column1 = (LinearLayout)findViewById(R.id.oddscolumn1);
    		LinearLayout column2 = (LinearLayout)findViewById(R.id.oddscolumn2);
    		LinearLayout column3 = (LinearLayout)findViewById(R.id.oddscolumn3);      
    		int i = values[0];
    		int j = values[1];
    		
			View btnOdds = LayoutInflater.from(_context).inflate(R.layout.match_odds_button, null);
			TextView oddsresult = (TextView)btnOdds.findViewById(R.id.oddsresult);
			String tmp = String.format("%d-%d", i, j);
			oddsresult.setText(tmp);
			TextView winback = (TextView)btnOdds.findViewById(R.id.winback);
			
			if (i > j && j < 3){
				double dbOdds = Helper.genCorrectScore(_handicap, _homeback, _awayback, i, j);
				winback.setText("1/" + String.valueOf((int)dbOdds));					
				column1.addView(btnOdds);
			}
			
			if (i < j && i < 3){
				double dbOdds = Helper.genCorrectScore(_handicap, _homeback, _awayback, i, j);
				winback.setText("1/" + String.valueOf((int)dbOdds));
				column3.addView(btnOdds);
			}
				
			if (i==j && i < 3){
				double dbOdds = Helper.genCorrectScore(_handicap, _homeback, _awayback, i, j);
				winback.setText("1/" + String.valueOf((int)dbOdds));
				column2.addView(btnOdds);					
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

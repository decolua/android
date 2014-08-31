package com.deco.adapter;

import com.deco.football.R;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MatchAdapter extends ArrayAdapter<MatchInfo> {
	public ArrayList<MatchInfo> lsMatchItem;
	private LayoutInflater mInflater;
	
	public MatchAdapter(Context context, int textViewResourceId, ArrayList<MatchInfo> objects) {
		super(context, textViewResourceId, objects);
		this.lsMatchItem = objects;
		mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View v, ViewGroup parent){
			

		MatchInfo matchinfo = lsMatchItem.get(position);
		if (matchinfo != null) {
			if (matchinfo.league_name == ""){
				v = mInflater.inflate(R.layout.match_item, null);
				MatchHolder holder;
				holder = new MatchHolder();
				holder.time = (TextView)v.findViewById(R.id.time);
		        holder.home = (TextView)v.findViewById(R.id.home);
		        holder.homegoals = (TextView)v.findViewById(R.id.homegoals);
		        holder.awaygoals = (TextView)v.findViewById(R.id.awaygoals);
		        holder.away = (TextView)v.findViewById(R.id.away);
		        v.setTag(holder);
		        holder.time.setText(matchinfo.time);
		    	holder.home.setText(matchinfo.home); 
		    	holder.homegoals.setText(matchinfo.homegoals);
		    	holder.awaygoals.setText(matchinfo.awaygoals);
		    	holder.away.setText(matchinfo.away);
			}
			else{
				v = mInflater.inflate(R.layout.league_item, null);
				LeagueHolder holder;
				holder = new LeagueHolder();
				holder.league_name = (TextView)v.findViewById(R.id.league_name);
		        v.setTag(holder);
		        holder.league_name.setText(matchinfo.league_name);
			}
		}			
			
		return v;
	}
	
	private static class MatchHolder {
		TextView type;
		TextView match_id;
		TextView time;
		TextView home;
		TextView homegoals;
		TextView awaygoals;
		TextView away;
	}
	private static class DateHolder {
		TextView type;
		TextView date;
	}
	private static class LeagueHolder {
		TextView league_id;
		TextView league_name;
	}
}
package com.example.ultimatestats;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

public class TeamListView extends ArrayAdapter<String> { //<ExistingTeams> {
	//private List<ExistingTeams> teams;
	//private int layoutResourceId;
	private final Context context;
	private final String[] values; //
	
/*	public TeamListView (Context context, int layoutResourceId, List<ExistingTeams> teams) {
		super(context, layoutResourceId, teams);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.teams = teams;
	}
*/
	public TeamListView (Context context, String[] values) {
		super(context, R.layout.team_list_view, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
/*		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //((Activity) context).getLayoutInflater();
		
		View rowView = inflater.inflate(R.layout.team_list_view, parent, false); //View row = convertView;
		//row = inflater.inflate(layoutResourceId, parent, false);
*/

		View rowView = convertView;
		if (rowView == null) {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater(); //(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = inflater.inflate(R.layout.team_list_view, parent, false);
		}
		
		
		Button button = (Button) rowView.findViewById(R.id.selectTeam);
		button.setText(values[position]);		
		
		return rowView;

/*		TeamHolder holder = null;
		
		
		holder = new TeamHolder();
		holder.existingTeams = items.get(position);
		holder.teamButton = (Button)row.findViewById(R.id.selectTeam);
		holder.teamButton.setTag(holder.existingTeams);
		
		row.setTag(holder);
		
		//setupItem(holder);
		return row;
*/
	}
	
/*	public static class TeamHolder {
		ExistingTeams existingTeams;
		Button teamButton;
	}
*/	

}
	
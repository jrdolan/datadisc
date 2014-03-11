package com.example.ultimatestats;

import sqlite.helper.Team;
import sqlite.model.DatabaseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TeamPage extends Activity {
	final DatabaseHandler db = new DatabaseHandler(this);
	ListView listView; //
	EditText display;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] teamList = db.getTeamNames();
		setContentView(R.layout.team_page);
		final Button backButton = (Button) findViewById(R.id.go_back);
		
		backButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFAA330F));		//94C369));		//C3A54C));
		backButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(TeamPage.this, MainActivity.class);
				finish();
		        startActivity(i);		
			}
		});
		
		final Button startButton = (Button) findViewById(R.id.create_team);
		
		listView = (ListView) findViewById(R.id.existing_teams);//
		
		startButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
		startButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(TeamPage.this, CreateTeam.class);
				finish();
				startActivity(i);		
			}
		});
	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, teamList); //(String[]) teams); //
	 
		listView.setAdapter(adapter);
		registerForContextMenu(listView); //attachs context menu to listview	
	}
	
	//Okay, this currently gives the index of the listitem
	public boolean onContextItemSelected(MenuItem item) {
		//super.onContextItemSelected(item);
		//info pertains to the list item
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int index = info.position;

		String[] teamList = db.getTeamNames();

		String teamName = teamList[index];
		
		Team team = db.getTeam(teamName);
		int teamID = team.getID(); //wait is this even necessary? - Nope
		//item ID is the ID of the Menu button
		switch (item.getItemId()) {
			case R.id.edit_roster:
				//Now I need to refresh the view
				//This works, but is apparently not ideal. Fine for now I think
				Intent intent =new Intent(this, PlayerPage.class);		
				intent.putExtra("Team ID", teamID);
				finish();
				startActivity(intent);
			    break;
		    
			case R.id.edit_team:
				//Go back to CreateTeam and edit stuff			
				Intent editTeam =new Intent(this, CreateTeam.class);
				editTeam.putExtra("Team ID", teamID);
				finish();
				startActivity(editTeam);
			    break;
			   
			case R.id.delete:
				db.deleteTeam(team); //Works! Success!
				//Now I need to refresh the view
				//This works, but is apparently not ideal. Fine for now I think
				Intent refresh =new Intent(this, TeamPage.class);
				startActivity(refresh);
				this.finish();
			  
			   break;

		   case R.id.cancel:
			   
			   Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show();
			   break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	//This creates a menu on a long click
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//menu.setHeaderIcon(R.drawable.icon);
		menu.setHeaderTitle("Edit Team");
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.team_menu, menu);
	}
}

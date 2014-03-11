package com.example.ultimatestats;

import sqlite.helper.Player;
import sqlite.helper.Team;
import sqlite.model.DatabaseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerPage extends Activity {
	final DatabaseHandler db = new DatabaseHandler(this);
	ListView listView; //
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_page);
		
		Bundle extras = getIntent().getExtras();
		final int teamId = extras.getInt("Team ID");
		Team team = db.getTeam(teamId);
		String teamName = team.getTeamName();

		TextView display = (TextView)findViewById(R.id.selected_team);
		display.setText(teamName);
		
		String[] teamRoster = getRoster(teamId); //db.getPlayerNames();
		
		final Button backButton = (Button) findViewById(R.id.go_back);
		backButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFAA330F));		
		
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(PlayerPage.this, TeamPage.class);
				finish();
		        startActivity(i);		
			}
		});
		
		final Button startButton = (Button) findViewById(R.id.create_player);
		startButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
		
		listView = (ListView) findViewById(R.id.existing_players);//
		
		startButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Check", "to intent");
				Intent i = new Intent(PlayerPage.this, CreatePlayer.class);
				i.putExtra("Team ID", teamId);
				finish();
		        startActivity(i);		
			}
		});
	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, teamRoster); //(String[]) players); //
	 
		listView.setAdapter(adapter);
		registerForContextMenu(listView); //attachs context menu to listview	
	
	}
	
	
	//Okay, this currently gives the index of the listitem
	public boolean onContextItemSelected(MenuItem item) {
		//super.onContextItemSelected(item);
		//info pertains to the list item
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int index = info.position;
		
		
		Bundle extras = getIntent().getExtras();
		final int teamId = extras.getInt("Team ID");
		
		String[] teamRoster = getRoster(teamId);

		String specificPlayer = teamRoster[index];
		
		String[] separatedString = specificPlayer.split(" ");
		String firstName = separatedString[0];
		String lastName = separatedString[1];
		
		int number = Integer.parseInt(separatedString[2]);
		
		String nickName = separatedString[3];
		
		Player player = db.getPlayer(firstName, lastName, number, nickName);
		int playerID = player.getID(); //wait is this even necessary? - Nope
		
		//item ID is the ID of the Menu button
		switch (item.getItemId()) {
			case R.id.edit_player:
				//Go back to CreateTeam and edit stuff			
				Intent editPlayer =new Intent(this, CreatePlayer.class);
				editPlayer.putExtra("Team ID", teamId);
				editPlayer.putExtra("Player ID", playerID);
				finish();
				startActivity(editPlayer);
			    break;
			    
			case R.id.delete_player:
				//Read player
				
				db.deletePlayer(player); //Works! Success!
			   
				//Now I need to refresh the view
				//This works, but is apparently not ideal. Fine for now I think
				Intent refresh =new Intent(this, PlayerPage.class);
				refresh.putExtra("Team ID", teamId);
				startActivity(refresh);
				this.finish();
			  
				break;

			case R.id.cancel_player:
				Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	//This creates a menu on a long click
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//menu.setHeaderIcon(R.drawable.icon);
		menu.setHeaderTitle("Edit Player");
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.player_menu, menu);		
	}

	    
    public String[] getRoster(int teamId) {
		int playerCount = db.getTeamPlayerFirstNames(teamId).length; //Here it is. I need to set playerCount not to the total number of players, but to how many are on that particular team!
		String[] teamRoster = new String[playerCount]; //Creates an empty string array based on how many players exist

		for (int index = 0; index < teamRoster.length; index++) { 
	    	String[] allFirstNames = db.getTeamPlayerFirstNames(teamId); //db.getAllTeamNames().get(0);
	    	String[] allLastNames = db.getTeamPlayerLastNames(teamId); //db.getAllTeamNames().get(1);
	    	String[] allNumbers = db.getTeamPlayerNumbers(teamId);
	    	String[] allNickNames = db.getTeamPlayerNickNames(teamId); 
	    	teamRoster[index] = allFirstNames[index] + " " + allLastNames[index] + " " + allNumbers[index] + " " + allNickNames[index];
	    }
	    return teamRoster;
	}
	
}

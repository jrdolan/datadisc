package com.recording;

import java.util.ArrayList;

import com.example.ultimatestats.R;

import sqlite.helper.Game;
import sqlite.helper.Player;
import sqlite.helper.Possession;
import sqlite.model.DatabaseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class LineUpPage extends Activity implements OnClickListener {
	final DatabaseHandler db = new DatabaseHandler(this);
	ListView listView; //
	ArrayAdapter<String> adapter;
	int[] playersID = new int[8];
	
	String[] playersNickName = new String[8];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lineup_page);
		
		Bundle extras = getIntent().getExtras();
		
		final int gameID = extras.getInt("Game ID");
//		int pointID = extras.getInt("Point ID");
//		int possessionID = extras.getInt("Possesion ID");
		
		Game game = db.getGame(gameID);
		int mainTeamID = db.getTeam(game.getMainTeam()).getID();
		
				
		String[] teamRoster = getRoster(mainTeamID); //db.getPlayerNames();
		
		final Button backButton = (Button) findViewById(R.id.go_back);
		backButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFAA330F));
		
		backButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(LineUpPage.this, FieldPage.class);
				i.putExtra("Game ID", gameID);
				finish();
		        startActivity(i);		
			}
		});
		
		listView = (ListView) findViewById(R.id.existing_players);//
	
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, teamRoster); //(String[]) players); //
	 
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listView.setAdapter(adapter);
		//registerForContextMenu(listView); //attachs context menu to listview
		final Button startButton = (Button) findViewById(R.id.set_line);
		startButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
		
		startButton.setOnClickListener(this);
	}
	   
    public String[] getRoster(int teamId) {
		int playerCount = db.getTeamPlayerFirstNames(teamId).length; //Here it is. I need to set playerCount not to the total number of players, but to how many are on that particular team!
		String[] teamRoster = new String[playerCount]; //Creates an empty string array based on how many players exist

		for (int index = 0; index < teamRoster.length; index++) { 
	    	String[] allFirstNames = db.getTeamPlayerFirstNames(teamId); //db.getAllTeamNames().get(0);
	    	String[] allLastNames = db.getTeamPlayerLastNames(teamId); //db.getAllTeamNames().get(1);
	    	String[] allNumbers = db.getTeamPlayerNumbers(teamId); 
	    	String[] allNickNames = db.getTeamPlayerNickNames(teamId); 
	    	teamRoster[index] = allFirstNames[index] + " " + allLastNames[index] + " (" + allNickNames[index] + ") #" + allNumbers[index];
	    }
	    return teamRoster;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Bundle extras = getIntent().getExtras();
		final int gameID = extras.getInt("Game ID");
		
		SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add sport if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
                selectedItems.add(adapter.getItem(position));
        }
 
        for (int i = 0; i < 7; i++) { //selectedItems.size(); i++) { 
        	
        	if (i+1 > selectedItems.size()) {
        		playersID[i] = 0;
        	}
        	else {
        		String[] separatedString = selectedItems.get(i).split(" ");
	        	String firstName = separatedString[0];
	    		String lastName = separatedString[1];
	    		String nickName = separatedString[2].replace("(", "").replace(")", "");
	    		int number = Integer.parseInt(separatedString[3].replace("#", ""));

	    		Player player = db.getPlayer(firstName, lastName, number, nickName);
	    		int playerID = player.getID();
	    		playersID[i] = playerID;
        	}
        }
        
        Possession possession = db.getPossession(db.getLastPossession());  //db.getPossession(possessionID);
		possession.setPlayer1ID(playersID[0]);
		possession.setPlayer2ID(playersID[1]);
		possession.setPlayer3ID(playersID[2]);
		possession.setPlayer4ID(playersID[3]);
		possession.setPlayer5ID(playersID[4]);
		possession.setPlayer6ID(playersID[5]);
		possession.setPlayer7ID(playersID[6]);

		db.updatePossession(possession);

        Intent intent = new Intent(LineUpPage.this, FieldPage.class);
        intent.putExtra("lineUp", playersID);
        intent.putExtra("Game ID", gameID);
        finish();
        startActivity(intent);
	}
	
}

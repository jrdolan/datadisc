package com.example.ultimatestats;

import sqlite.helper.Action;
import sqlite.helper.Game;
import sqlite.helper.Point;
import sqlite.helper.Possession;
import sqlite.model.DatabaseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CreateGame extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
				
		final Game game = new Game();
		final DatabaseHandler db = new DatabaseHandler(this); 
		
		//final EditText enterdata;
		Button submit;
		Button mainTeam;
		Button opponent;
		final TextView mainTeamDisplay;
		final TextView opponentDisplay;
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_game);
		
		//enterdata = (EditText)findViewById(R.id.editText1);  Using input from the buttons instead
        mainTeam = (Button)findViewById(R.id.main_team);
        mainTeam.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
        
        opponent = (Button)findViewById(R.id.opponent);
        opponent.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFC3A54C));
        
        submit = (Button)findViewById(R.id.create_game);
        submit.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF464F77));
        
        mainTeamDisplay = (TextView)findViewById(R.id.TextView_MainTeam);
        opponentDisplay = (TextView)findViewById(R.id.TextView_Opponent);
        
        mainTeam.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Display an alert box with a list of teams
				
				//Main Team Selector
				final String[] items = db.getTeamNames();//{"Foo", "Bar", "Baz"};

				AlertDialog.Builder builder = new AlertDialog.Builder(CreateGame.this);
				builder.setTitle("Make your selection");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	
				    	mainTeamDisplay.setText(items[item]);
				     //add team to created game here
				        game.setMainTeam(items[item]);
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		opponent.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Display an alert box with a list of teams
				
				//Opponent Selector
				final String[] items = db.getTeamNames(); //{"Foo", "Bar", "Baz"};

				AlertDialog.Builder builder = new AlertDialog.Builder(CreateGame.this);
				builder.setTitle("Make your selection");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	
				    	opponentDisplay.setText(items[item]);
				      //add team to created game here
				        game.setOpponent(items[item]);
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
			
			}
		});
		
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Insert teams/game into database and return to GamePage
				// Create and add game to DB			
				
				if (game.getMainTeam().equalsIgnoreCase("")) {
					//If teamName is empty, bring up a toast alert
					Toast t =Toast.makeText(CreateGame.this, "Please select a main team", Toast.LENGTH_SHORT);
				    t.show();   
				}
				else if (game.getOpponent().equalsIgnoreCase("")) {
					//If teamName is empty, bring up a toast alert
					Toast t =Toast.makeText(CreateGame.this, "Please select an opponent", Toast.LENGTH_SHORT);
				    t.show();   
				}
				else {
					db.addGame(game);
					
					//Create and add the first Point, Possession, and Action for the game.
					Point point = new Point(db.getGame(db.getLastGame()).getID(), 0, 0, "Left", 0, 0.0, 0.0, 0.0, "Clear"); //game id, mainTeam score, opponent score, starting side, wind direction (deg), wind strength
					db.addPoint(point);
					
					Possession possession = new Possession(db.getLastPoint(), "Offense", 0, 0, 0, 0, 0, 0, 0); //point id, possession type, player IDs (all set to 0, the Unknown ID)
					db.addPossession(possession);
					
					Action action = new Action(db.getLastPossession(), 0, "Pick Up", 0, 0); //possession ID, player ID (Unknown), action, X location, Y location
					db.addAction(action);
					
					Intent i = new Intent(CreateGame.this, GamePage.class);
					finish();
			        startActivity(i);
				}
			}
		});
	} 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}
	
	

}

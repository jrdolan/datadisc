package com.example.ultimatestats;

import sqlite.helper.Player;
import sqlite.model.DatabaseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreatePlayer extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		final DatabaseHandler db = new DatabaseHandler(this); 
		
		final EditText firstName;
		final EditText lastName;
		final EditText number;
		final EditText nickName;
		Button submit;
		final TextView hiddenFirst;
		final TextView hiddenLast;
		final TextView hiddenNumber;
		final TextView hiddenNick;
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_player);
		
		Bundle extras = getIntent().getExtras();
		final int teamId = extras.getInt("Team ID");
		
		firstName = (EditText)findViewById(R.id.player_firstname);
		lastName = (EditText)findViewById(R.id.player_lastname);
		number = (EditText)findViewById(R.id.player_number);
		nickName = (EditText)findViewById(R.id.player_nickname);
        //display = (TextView)findViewById(R.id.display);
        hiddenFirst = (TextView)findViewById(R.id.hidden_first_name);
        hiddenLast = (TextView)findViewById(R.id.hidden_last_name);
        hiddenNumber = (TextView)findViewById(R.id.hidden_number);
        hiddenNick = (TextView)findViewById(R.id.hidden_nick_name);
        
        submit = (Button)findViewById(R.id.create_player);
        
        try {
        	//Look for teamID value and preset strings?
        	int playerID = extras.getInt("Player ID");
        	Player playerToEdit = db.getPlayer(playerID); 
        	firstName.setText(playerToEdit.getPlayerFirstName());
        	hiddenFirst.setText(playerToEdit.getPlayerFirstName());
        	lastName.setText(playerToEdit.getPlayerLastName());
        	hiddenLast.setText(playerToEdit.getPlayerLastName());
        	number.setText(Integer.toString(playerToEdit.getPlayerNumber()));
        	hiddenNumber.setText(Integer.toString(playerToEdit.getPlayerNumber()));
        	nickName.setText(playerToEdit.getPlayerNickName());
        	hiddenNick.setText(playerToEdit.getPlayerNickName());
        	
        	submit.setText("Save Player");
        } 
        catch(Exception e) {
        	firstName.setText("");
        	lastName.setText("");
        	number.setText("");
        	nickName.setText("");
        	
        	submit.setText("Add Player");
        }

        submit.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent i = new Intent(MainActivity.this, CreatePlayer.class);
		          // startActivity(i);
				
				String playerFirstName = firstName.getText().toString().trim(); //replace(" ", "");
				String playerLastName = lastName.getText().toString().trim(); //replace(" ", "");
				String stringPlayerNumber = number.getText().toString().trim(); //replace(" ", "");
				int playerNumber = Integer.parseInt(stringPlayerNumber);	
				String playerNickName = nickName.getText().toString().trim(); //replace(" ", "");
			    
				if (playerFirstName.equalsIgnoreCase("")) {
					//If teamName is empty, bring up a toast alert
					Toast t =Toast.makeText(CreatePlayer.this, "Please enter a first name", Toast.LENGTH_SHORT);
				    t.show();   
				}
				else if (playerLastName.equalsIgnoreCase("")) {
					//If teamName is empty, bring up a toast alert
					Toast t =Toast.makeText(CreatePlayer.this, "Please enter a last name", Toast.LENGTH_SHORT);
				    t.show();   
				}
/*				else if (stringPlayerNumber.compareTo("") == 0) {
					//If teamName is empty, bring up a toast alert
					Toast t =Toast.makeText(CreatePlayer.this, "Please enter a number", 2500);
				    t.show();   
				}
*/
				else if (playerNickName.equalsIgnoreCase("")) {
					//If teamName is empty, bring up a toast alert
					Toast t =Toast.makeText(CreatePlayer.this, "Please enter a displayed name (nickname)", Toast.LENGTH_SHORT);
				    t.show();   
				}
				else {
					try {
						String originalFirst = (String) hiddenFirst.getText();
						String originalLast = (String) hiddenLast.getText();
						int originalNumber = Integer.parseInt((String) hiddenNumber.getText());
						String originalNick = (String) hiddenNick.getText();
						
						Player findPlayer = db.getPlayer(originalFirst, originalLast, originalNumber, originalNick); //If the original team Name exists
						findPlayer.setPlayerFirstName(playerFirstName);
						findPlayer.setPlayerLastName(playerLastName);
						findPlayer.setPlayerNumber(playerNumber);
						findPlayer.setPlayerNickName(playerNickName);

						db.updatePlayer(findPlayer);
					}
					catch(Exception e) {
						Player	player = new Player(playerFirstName, playerLastName, playerNumber, playerNickName, teamId);
						db.addPlayer(player);
					}
 
					//REMEMBER, IF YOU'RE GOING TO PLAYERPAGE YOU NEED TO PASS IT Team ID
				     Intent i = new Intent(CreatePlayer.this, PlayerPage.class);
				     i.putExtra("Team ID", teamId);
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

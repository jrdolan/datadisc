package com.example.ultimatestats;

import sqlite.helper.Team;
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

public class CreateTeam extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		final DatabaseHandler db = new DatabaseHandler(this); 
		
		final EditText enterdata;
		Button submit;
		final TextView hiddenName;
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_team);
		enterdata = (EditText)findViewById(R.id.edit_team_text);
		//display = (TextView)findViewById(R.id.display);
        submit = (Button)findViewById(R.id.create_team);
        hiddenName = (TextView)findViewById(R.id.hidden_team_name);
        
        try {
        	//Look for teamID value and preset strings?
        	Bundle extras = getIntent().getExtras();
        	int teamID = extras.getInt("Team ID");
        	String teamNameToEdit = db.getTeam(teamID).getTeamName(); 
        	enterdata.setText(teamNameToEdit);
        	hiddenName.setText(teamNameToEdit);
    		
        	//submit.setText("Save Team");
        } 
        catch(Exception e) {
        	//int teamID = 0;
        	enterdata.setText("");
        	submit.setText("Add Team");
        }
        
        submit.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String teamName = enterdata.getText().toString().trim(); //replace(" ", "");
				
				if (teamName.equalsIgnoreCase("")) {
					//If teamName is empty, bring up a toast alert
					Toast t =Toast.makeText(CreateTeam.this, "Please enter a team name", Toast.LENGTH_SHORT);
				    t.show();   
				}
				else {
					try {
						String originalName = (String) hiddenName.getText();
						Team findTeam = db.getTeam(originalName); //If the original team Name exists
						findTeam.setTeamName(teamName);
						db.updateTeam(findTeam);
					}
					catch(Exception e) {
						//display.setText("Entered name is : "+teamName);
						Team team = new Team(teamName);
						db.addTeam(team);
					}
				    Intent i = new Intent(CreateTeam.this, TeamPage.class);
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

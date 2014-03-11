package com.example.ultimatestats;

import sqlite.model.DatabaseHandler;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.LightingColorFilter;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final DatabaseHandler db = new DatabaseHandler(this);
		
		setContentView(R.layout.activity_main);
		
		final Button viewTeamButton = (Button) findViewById(R.id.team_page);
		final Button viewGameButton = (Button) findViewById(R.id.game_page);

		viewTeamButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));		//0BACDD));
		viewGameButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF464F77));		//0BACDD));
		
		viewTeamButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent i = new Intent(MainActivity.this, TeamPage.class);
		           startActivity(i);
			}
		});
		
		viewGameButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (db.getTeamNames().length < 2) {
					customToast(v);
				}
				else {
				 Intent i = new Intent(MainActivity.this, GamePage.class);
		           startActivity(i);
				}
			}
		});
		
	}
	
	public void customToast(View v) {
        //Create the instance of a new toast
        Toast newToast = Toast.makeText(getApplicationContext(), "You need to create at least two teams (Your team and an Opponent)", Toast.LENGTH_SHORT);
        //Generate the layout from the tasty_toast.xml
        LayoutInflater inflater = getLayoutInflater();
        View customToast = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toastLayout));
        //Get the textview in our toast layout
        TextView toastText = (TextView) customToast.findViewById(R.id.textView1);
        //Set the message in the textview
        toastText.setText("You need to create at least two teams (your team and an opponent)");
        //Set the custom view in the toast
        newToast.setView(customToast);
        //Show the toast
        newToast.show();    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}

}

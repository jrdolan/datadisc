package com.example.ultimatestats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.recording.FieldPage;

import sqlite.helper.Game;
import sqlite.model.DatabaseHandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class GamePage extends Activity { 
	final DatabaseHandler db = new DatabaseHandler(this);
	
	ListView listView; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String[] gameList = getGameListings();	
		setContentView(R.layout.game_page);
		
		final Button backButton = (Button) findViewById(R.id.go_back);
		backButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFAA330F));
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(GamePage.this, MainActivity.class);
		        startActivity(i);		
			}
		});
		
		listView = (ListView) findViewById(R.id.existing_games);
		
		final Button startButton = (Button) findViewById(R.id.create_game);
		startButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(GamePage.this, CreateGame.class);
		        startActivity(i);		
			}
		});
	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, gameList); //(String[]) games); //
	 
		listView.setAdapter(adapter);
		registerForContextMenu(listView); //attachs context menu to listview	
	}
	
	
	//Okay, this currently gives the index of the listitem
	public boolean onContextItemSelected(MenuItem item) {
		//info pertains to the list item
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int index = info.position;	

		String[]gameList = getGameListings();
		
		String specificGame = gameList[index];
		
		String[] separatedString = specificGame.split(" vs. ");
		String mainTeam = separatedString[0];
		String opponent = separatedString[1];
		Game game = db.getGame(mainTeam, opponent);
		int gameID = game.getID(); //wait is this even necessary? - Nope
		
		//item ID is the ID of the Menu button
		switch (item.getItemId()) {
			case R.id.start_game:
				//I think I only need the game ID
				Intent intent =new Intent(this, FieldPage.class);
				//intent.putExtra("lineUp", playersID);
				intent.putExtra("Game ID", gameID);
				startActivity(intent);
			  
			   break;
		   case R.id.delete_game:
			 //Read game
				
				db.deleteGame(game); //Works - kind of.  
				//RIGHT NOW THIS DELETES THE FIRST GAME IN THE LIST THAT MATCHES THE NAME.  
				//I.E. IF THERE ARE TWO GAMES THAT ARE WILSON VS WALLS, ATTEMPTS TO DELETE EITHER GAME
				//WILL *ALWAYS* RESULT IN DELETING THE FIRST GAME.
			   
				//Now I need to refresh the view
				//This works, but is apparently not ideal. Fine for now I think
				Intent refresh =new Intent(this, GamePage.class);
				startActivity(refresh);
				this.finish();
			  
			   break;

		   case R.id.cancel_game:
			   Log.d("Cancel: ", "canceling");
			   Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show();
			   break;
			   
		   case R.id.export_game:
			   Log.d("Export: ", "exporting stats");
			   try {                   
				   new ExportGame().execute("");
			   }
			   catch(Exception ex) {
				   Log.e("Error in MainActivity",ex.toString());
			   }
		   
			   break;
		}
		return super.onOptionsItemSelected(item);
	}

	//This creates a menu on a long click
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//menu.setHeaderIcon(R.drawable.icon);
		menu.setHeaderTitle("Edit Game");
		
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.game_menu, menu);
	}

	//This creates the menu when the 'Menu' button is pressed
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_menu, menu);
		return true;	
	}
	

	//These are buttons for a menu that comes up from pressing the 'Menu' button.  Not what I want to do
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.delete:
	        //newGame();
	    	Log.d("Delete:", "Deleting");
	        return true;
	    case R.id.cancel:
	        //showHelp();
	    	Log.d("Cancel:","Returning");
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public String[] getGameListings() {
		int gameCount = db.getAllMainTeams().length;
		String[] gameList = new String[gameCount]; //db.getGameCount()]; //Creates an empty string array based on how many games exist
	    for (int number = 0; number < gameList.length; number++) { 
	        
	    	String[] allMainTeams = db.getAllMainTeams(); //db.getAllTeamNames().get(0);
	    	String[] allOpponents = db.getAllOpponents(); //db.getAllTeamNames().get(1);
	    	
	    	gameList[number] = allMainTeams[number] + " vs. " + allOpponents[number]; //" vs. ";// + db.getAllOpponents()[number];// 
	    }
	    
	    return gameList;
	}
	
	class ExportGame extends AsyncTask<String, Void, Boolean> {
		
			Context ctx = GamePage.this;
		    private final ProgressDialog dialog = new ProgressDialog(ctx);

		    // can use UI thread here
		    @Override
		    protected void onPreExecute() {
		        this.dialog.setMessage("Exporting database...");
		        this.dialog.show();
		    }   

		    // automatically done on worker thread (separate from UI thread)
		    protected Boolean doInBackground(final String... args) {
		    	
		        File dbFile=getDatabasePath(db.getDatabaseName()+".csv");  //("mydb.db");
		        //File dbFile = new File(Environment.getDataDirectory() + "/data/org.test.xyz/databases/excerDB.db");
		        File exportDir = new File(Environment.getExternalStorageDirectory(), "");        

		        if (!exportDir.exists()) {
		            exportDir.mkdirs();
		        }

		        File rosterFile = new File(exportDir, "roster" + dbFile.getName());
		        
		        File pointFile = new File(exportDir, "point" + dbFile.getName());
		        //int mainTeamID = db.getTeam(db.getGame(XXXXXX).getMainTeam()).getID();
		        File possessionFile = new File(exportDir, "possession" + dbFile.getName());
		        
		        File actionFile = new File(exportDir, "action" + dbFile.getName());

		        try {
		        	db.exportRoster(rosterFile); //, mainTeam);
		        	db.exportPointTable(pointFile);
		        	db.exportPossessionTable(possessionFile);
		        	db.exportActionTable(actionFile);
		        	
		        	return true;
		        }
		        finally {
		        }
		    }

		    // can use UI thread here
		    @Override
		    protected void onPostExecute(final Boolean success) {
		        if (this.dialog.isShowing()) {
		            this.dialog.dismiss();
		        }

		        if (success) {
		            Toast.makeText(ctx, "Export successful!", Toast.LENGTH_SHORT).show();
		        } 
		        else {
		            Toast.makeText(ctx, "Export failed", Toast.LENGTH_SHORT).show();
		        }
		    }

		    void copyFile(File src, File dst) throws IOException {
		        FileChannel inChannel = new FileInputStream(src).getChannel();
		        FileChannel outChannel = new FileOutputStream(dst).getChannel();
		        try 
		        {
		            inChannel.transferTo(0, inChannel.size(), outChannel);
		        } 
		        finally 
		        {
		            if (inChannel != null)
		                inChannel.close();
		            if (outChannel != null)
		                outChannel.close();
		        }
		    }
		}
}

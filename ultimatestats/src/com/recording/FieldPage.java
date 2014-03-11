package com.recording;

import it.restrung.rest.cache.RequestCache;
import it.restrung.rest.client.ContextAwareAPIDelegate;

import sqlite.helper.Action;
import sqlite.helper.Possession;
import sqlite.helper.Point;
import sqlite.model.DatabaseHandler;

import com.example.ultimatestats.GamePage;
import com.example.ultimatestats.R;
import com.fortysevendeg.android.wunderground.api.service.WundergroundApiProvider;
import com.fortysevendeg.android.wunderground.api.service.request.Feature;
import com.fortysevendeg.android.wunderground.api.service.request.Query;
import com.fortysevendeg.android.wunderground.api.service.response.WundergroundResponse;

import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

 public class FieldPage extends Activity {
	final DatabaseHandler db = new DatabaseHandler(this);
	final Context context = this;

	EditText wind_direction;
	//RadioGroup wind_strength;
	//RadioButton selected_strength;
	TextView wind_strength;
	TextView gust_strength;
	TextView weather_type;
	TextView temperature;
	
	TextView wind_strength_tv;
	TextView gust_strength_tv;
	TextView weather_tv;
	TextView temperature_tv;
	TextView wind_direction_tv;
	
	RadioGroup possType;
	RadioButton possTypeSelection;
	RadioGroup startingSide;
	RadioButton startingSideSelection;
	
	TextView mainTeamScore;
	TextView opponentScore;
	String newSide;
	String newType;
	
	TextView actionDisplay;
	
	String playerDisplay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.field_page);
		
		//Get Game
		Bundle extras = getIntent().getExtras();
		final int gameID = extras.getInt("Game ID");

		//Get Latest Point, Possession, and Action IDs
		final int lastPointID = db.getLastPoint();
		final int lastPossessionID = db.getLastPossession();
	
		mainTeamScore = (TextView)findViewById(R.id.main_team_score_number);
        opponentScore = (TextView)findViewById(R.id.opponent_score_number);
        //This sets the score
		mainTeamScore.setText(Integer.toString(db.getPoint(db.getLastPointInGame(gameID)).getMainTeamScore()));
		opponentScore.setText(Integer.toString(db.getPoint(db.getLastPointInGame(gameID)).getOpponentScore()));
		actionDisplay = (TextView)findViewById(R.id.action_display);

		Button windButton = (Button) findViewById(R.id.set_wind_btn);
		windButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
		windButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Generate a popup that sets the wind speed and direction	
				// check if GPS enabled
				
				GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
				
		        if (gpsTracker.canGetLocation())
		        {
		        	//double latitude = gpsTracker.latitude;
		            //double longitude = gpsTracker.longitude;
		        }
		        else
		        {
		            // can't get location
		            // GPS or Network is not enabled
		            // Ask user to enable GPS/network in settings
		            //gpsTracker.showSettingsAlert();
		        	showSettingsAlert();
		        }
				
		        WundergroundApiProvider.getClient().query(new ContextAwareAPIDelegate<WundergroundResponse> (FieldPage.this, WundergroundResponse.class, RequestCache.LoadPolicy.NEVER) {

		        	@Override
		            public void onResults(WundergroundResponse wundergroundResponse) {
		                double wMag = wundergroundResponse.getCurrentObservation().getWindMph();
						double wMax = wundergroundResponse.getCurrentObservation().getWindGustMph();
						String weatherCondition = wundergroundResponse.getCurrentObservation().getWeather();
						double temperatureValue = wundergroundResponse.getCurrentObservation().getTempF();
						
						windPopup(FieldPage.this, wMag, wMax, weatherCondition, temperatureValue); 
						
						//CURRENTLY WMAG IS BEING SET AS THE ANGLE, WHICH IS AN INTEGER.  THIS OBVIOUSLY NEEDS TO BE CORRECTED
						
		            }
		            @Override
		            public void onError(Throwable e) {
		                Toast.makeText(FieldPage.this, "No Internet Connection", Toast.LENGTH_LONG).show();
		            }
		        	

			    }, "e18263a98e7cf981", Query.latLng(gpsTracker.latitude, gpsTracker.longitude), Feature.conditions);
			    
			}
		});
		
		Button conditionsButton = (Button) findViewById(R.id.startup_conditions_btn);
		conditionsButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
		conditionsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Generate a popup that sets the wind speed and direction	
				startUpConditionsPopup(FieldPage.this);
			}
		});
		
		Button lineUpButton = (Button) findViewById(R.id.lineup_btn);
		lineUpButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
		lineUpButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(FieldPage.this, LineUpPage.class);
				i.putExtra("Game ID", gameID);
		        i.putExtra("Point ID", lastPointID);
				i.putExtra("Possession ID", lastPossessionID);
				startActivity(i);	
			}
		});
		
		Button backButton = (Button) findViewById(R.id.back_btn);
		backButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFAA330F));
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				Intent i = new Intent(FieldPage.this, GamePage.class);
				startActivity(i);	
			}
		});
		
		Button statsButton = (Button) findViewById(R.id.stats_btn);
		statsButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
		statsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				Intent i = new Intent(FieldPage.this, StatsPage.class);				
				i.putExtra("Game ID", gameID);
				Log.d("Check", Integer.toString(gameID) + " Game ID");
				startActivity(i);	
			}
		});
		
		Button goalButton = (Button) findViewById(R.id.goal_btn);
		goalButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF464F77));
		goalButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Get Last Point of Game -> Get Starting Side -> Get Current Possession Type -> Based on Side and Possession, determine Goal or Callahan ...
				// TODO -> Popup dialog asking verification -> Create new point, new possession, new action -> redirect to Lineup Page?
				
				if (db.getAction(db.getLastAction()-1).getXPosition() >= 95) {
					//We scored
					if (db.getAction(db.getLastAction()-1).getActionType().compareTo("PickUp") == 0) {
						//Callahan
						didMainTeamScore("Callahan", gameID);
					}
					else {
						//Goal
						didMainTeamScore("Goal", gameID);
					}
				}
				else if (db.getAction(db.getLastAction()-1).getXPosition() <= 25) {
					//We got scored on
					if (db.getAction(db.getLastAction()-1).getActionType().compareTo("PickUp") == 0) {
						//Callahan
						didOpponentScore("Callahan", gameID);
					}
					else {
						//Goal
						didOpponentScore("Goal", gameID);
					}
				}
				else {
					//Not in the endzone!
				}
			}
		});
		
		Button dropButton = (Button) findViewById(R.id.drop_btn);
		dropButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFC3A54C));
		dropButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO On drop, change last action to drop -> New (opposite) Possession
				forcedTurnover("Drop");
				
				Possession oldPossession = db.getPossession(db.getLastPossession());
				
				Possession possession = new Possession(db.getLastPoint(), 
												   switchPossessionType(oldPossession.getPossessionType()),
												   oldPossession.getPlayer1ID(),
												   oldPossession.getPlayer2ID(),
												   oldPossession.getPlayer3ID(),
												   oldPossession.getPlayer4ID(),
												   oldPossession.getPlayer5ID(),
												   oldPossession.getPlayer6ID(),
												   oldPossession.getPlayer7ID());
				db.addPossession(possession);
	
				Action action = new Action(db.getLastPossession(), 0, "Pick Up", 0, 0); //possession ID, player ID (Unknown), action, X location, Y location
				db.addAction(action);
			
			}
		});

		Button throwawayButton = (Button) findViewById(R.id.throwaway_btn);
		throwawayButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFC3A54C));
		throwawayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO On drop, change last action to drop -> New (opposite) Possession
				forcedTurnover("Throwaway");
				
				Possession oldPossession = db.getPossession(db.getLastPossession());
				Possession possession = new Possession(db.getLastPoint(), 
						   switchPossessionType(oldPossession.getPossessionType()),
						   oldPossession.getPlayer1ID(),
						   oldPossession.getPlayer2ID(),
						   oldPossession.getPlayer3ID(),
						   oldPossession.getPlayer4ID(),
						   oldPossession.getPlayer5ID(),
						   oldPossession.getPlayer6ID(),
						   oldPossession.getPlayer7ID());
				db.addPossession(possession);

				Action action = new Action(db.getLastPossession(), 0, "Pick Up", 0, 0); //possession ID, player ID (Unknown), action, X location, Y location
				db.addAction(action);					  
			}
		});
		
		Button stallButton = (Button) findViewById(R.id.stall_btn);
		stallButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFC3A54C));
		stallButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO On drop, change last action to drop -> New (opposite) Possession
				forcedTurnover("Stall");
				
				Possession oldPossession = db.getPossession(db.getLastPossession());
				Possession possession = new Possession(db.getLastPoint(), 
						   switchPossessionType(oldPossession.getPossessionType()),
						   oldPossession.getPlayer1ID(),
						   oldPossession.getPlayer2ID(),
						   oldPossession.getPlayer3ID(),
						   oldPossession.getPlayer4ID(),
						   oldPossession.getPlayer5ID(),
						   oldPossession.getPlayer6ID(),
						   oldPossession.getPlayer7ID());
				db.addPossession(possession);
				
				Action action = new Action(db.getLastPossession(), 0, "Pick Up", 0, 0); //possession ID, player ID (Unknown), action, X location, Y location
				db.addAction(action);						  
			}
		});
		
		Button outOfBoundsButton = (Button) findViewById(R.id.out_of_bounds_btn);
		outOfBoundsButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFC3A54C));
		outOfBoundsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO On drop, change last action to drop -> New (opposite) Possession
				Action action = db.getAction(db.getLastAction()-1);
				action.setActionType("Targeted");
				//Now adjust location to nearest edge
				int xPos = action.getXPosition();
				int yPos = action.getYPosition();
				int[] edges = {xPos, 120-xPos, yPos, 40-yPos};
				int minimum = edges[0];
				int index = 0;
				for (int i = 1; i < edges.length; i++) {
					 if (edges[i] < minimum) {
						 minimum = edges[i];
						 index = i;
					 }
				}
				switch (index) {
				case 0:
					action.setXPosition(0);
					break;
				case 1:
					action.setXPosition(120);
					break;
				case 2:
					action.setYPosition(0);
					break;
				case 3:
					action.setYPosition(40);
					break;
				}
				
				db.updateAction(action);
				
				Action actionTwo = db.getAction(db.getLastAction()-2);
				actionTwo.setActionType("ThrowOB");
				db.updateAction(actionTwo);
				
				db.deleteAction(db.getAction(db.getLastAction()));
				
				Possession oldPossession = db.getPossession(db.getLastPossession());
				Possession newPossession = new Possession(oldPossession.getPointsID(),
														  switchPossessionType(oldPossession.getPossessionType()),
														  oldPossession.getPlayer1ID(),
														  oldPossession.getPlayer2ID(),
														  oldPossession.getPlayer3ID(),
														  oldPossession.getPlayer4ID(),
														  oldPossession.getPlayer5ID(),
														  oldPossession.getPlayer6ID(),
														  oldPossession.getPlayer7ID());
				db.addPossession(newPossession);
				
				Action newAction = new Action(db.getLastPossession(), 0, "PickUp", 0, 0);
				db.addAction(newAction);						  
			}
		});
		
		Button blockButton = (Button) findViewById(R.id.block_btn);
		blockButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFC3A54C));
		blockButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO On drop, change last action to drop -> New (opposite) Possession
				Action action = db.getAction(db.getLastAction()-1);
				action.setActionType("Targeted");
				db.updateAction(action);
				
  				Action actionTwo = db.getAction(db.getLastAction()-2);
				actionTwo.setActionType("Blocked");
				db.updateAction(actionTwo);
				
				db.deleteAction(db.getAction(db.getLastAction()));
				
				Possession oldPossession = db.getPossession(db.getLastPossession());
				Possession newPossession = new Possession(oldPossession.getPointsID(),
														  switchPossessionType(oldPossession.getPossessionType()),
														  oldPossession.getPlayer1ID(),
														  oldPossession.getPlayer2ID(),
														  oldPossession.getPlayer3ID(),
														  oldPossession.getPlayer4ID(),
														  oldPossession.getPlayer5ID(),
														  oldPossession.getPlayer6ID(),
														  oldPossession.getPlayer7ID());
				db.addPossession(newPossession);
				
				Action newAction = new Action(db.getLastPossession(), 0, "PickUp", 0, 0);
				db.addAction(newAction);						  
			}
		});
		
		Button undoButton = (Button) findViewById(R.id.undo_btn);
		undoButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFAA330F));
		undoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (db.getAction(db.getLastAction()).getPlayerID() == 0) {
					playerDisplay = "Unknown";
				}
				else {
					playerDisplay = db.getPlayer(db.getAction(db.getLastAction()).getPlayerID()).getPlayerNickName();
				}
				
				if (db.getPoint(db.getLastPoint()).getGameID() == gameID) { 
					//If the Game ID of the last point matches that of the current game, undo as desired
					//This should prevent undoing actions from other games
					
					if ((db.getActionsInPossession(db.getLastPossessionInPoint(db.getLastPoint())).length == 1) && (db.getPossessionsInPoint(db.getLastPoint()).length == 1)) {
						//If only 1 Action and 1 Possession in Point
						Log.d("Undo", "Undoing Goal");
						for (int i = 0; i < db.getActionsInPossession(db.getLastPossessionInPoint(db.getLastPoint())).length; i++) {
							//Deletes all actions in Poss
							db.deleteAction(db.getAction(db.getLastAction()));
						}
						
						//Delete Last Poss
						db.deletePossession(db.getPossession(db.getLastPossession()));
						
						//Delete Last Point
						db.deletePoint(db.getPoint(db.getLastPoint()));
						
						//Delete last 2 actions (goal, throw) and add one empty
						for (int i = 0; i < 2; i++) {
							db.deleteAction(db.getAction(db.getLastAction()));
						}
						Action action = new Action(db.getLastPossession(), db.getAction(db.getLastAction()).getPlayerID(), "Throw", 0, 0);
						db.addAction(action);
						
						//Edit score
						mainTeamScore.setText(Integer.toString(db.getPoint(db.getLastPoint()-1).getMainTeamScore()));
						opponentScore.setText(Integer.toString(db.getPoint(db.getLastPoint()-1).getOpponentScore()));
						
						actionDisplay.setText(playerDisplay + "  " + db.getAction(db.getLastAction()-1).getActionType());
						
					}
					else if (db.getActionsInPossession(db.getLastPossessionInPoint(db.getLastPoint())).length == 1) {
						//If only 1 action in last Possession
						Log.d("Undo", "Undoing Turnover");
						for (int i = 0; i < db.getActionsInPossession(db.getLastPossessionInPoint(db.getLastPoint())).length; i++) {
							//Deletes all actions in Poss
							db.deleteAction(db.getAction(db.getLastAction()));
						}
						//Delete Last Poss
						db.deletePossession(db.getPossession(db.getLastPossession()));
						
						if (db.getAction(db.getLastAction()).getActionType().contains("Stall")) {
							//If last action is stall, delete last 3 (throw, catch, stall) and add one empty
							for (int i = 0; i < 3; i++) {
								db.deleteAction(db.getAction(db.getLastAction()));
							}
							Action action = new Action(db.getLastPossession(), db.getAction(db.getLastAction()).getPlayerID(), "Throw", 0, 0);
							db.addAction(action);
						}
						else {
							//Else delete last 2 (throwaway/target, throw/drop, etc) and add one empty
							for (int i = 0; i < 2; i++) {
								db.deleteAction(db.getAction(db.getLastAction()));
							}
							Action action = new Action(db.getLastPossession(), db.getAction(db.getLastAction()).getPlayerID(), "Throw", 0, 0);
							db.addAction(action);
						}
						actionDisplay.setText("Resetting back to... " + playerDisplay + "  " + db.getAction(db.getLastAction()-1).getActionType());
	
					}
					else {
						//Multiple actions and multiple Possessions
						//Need to delete last 3 Actions (empty, catch, throw) and create new empty one
						Log.d("Undo", "Undoing Action");
						for (int i = 0; i < 3; i++) {
							db.deleteAction(db.getAction(db.getLastAction()));
						}
						Action action = new Action(db.getLastPossession(), db.getAction(db.getLastAction()).getPlayerID(), "Throw", 0, 0);
						db.addAction(action);
						
						actionDisplay.setText("Resetting back to... " + playerDisplay + "  " + db.getAction(db.getLastAction()-1).getActionType());
					}
				}	
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	// The method that displays the Wind popup. windPopup(FieldPage.this, wMag, wMax, weatherCondition, temperature
	void windPopup(final Activity context, final double wMag, final double wMax, final String weatherCondition, final double temperatureValue) {  
	   int popupWidth = 800;
	   int popupHeight = 550;
	   
	   // Inflate the popup_layout.xml
	   LinearLayout windViewGroup = (LinearLayout) context.findViewById(R.id.wind_popup);
	   LayoutInflater windLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   final View windLayout = windLayoutInflater.inflate(R.layout.wind_popup_layout, windViewGroup);
	 
	   wind_direction = (EditText)windLayout.findViewById(R.id.wind_direction_text);
	   wind_strength = (TextView)windLayout.findViewById(R.id.wind_strength_value);
	   gust_strength = (TextView)windLayout.findViewById(R.id.gust_value);
	   temperature = (TextView)windLayout.findViewById(R.id.temperature_value);
	   weather_type = (TextView)windLayout.findViewById(R.id.weather_value); //(RadioGroup)windLayout.findViewById(R.id.wind_strength);
			   
	   wind_strength_tv = (TextView)windLayout.findViewById(R.id.wind_strength_TextView);
	   gust_strength_tv = (TextView)windLayout.findViewById(R.id.gust_strength_TextView);
	   temperature_tv = (TextView)windLayout.findViewById(R.id.temperature_TextView);
	   weather_tv = (TextView)windLayout.findViewById(R.id.weather_TextView);
	   wind_direction_tv = (TextView)windLayout.findViewById(R.id.wind_direction_TextView);
	   
	   wind_strength.setText(Double.toString(wMag) + " mph");
	   gust_strength.setText(Double.toString(wMax) + " mph");
	   temperature.setText(Double.toString(temperatureValue) + " F");
	   weather_type.setText(weatherCondition);
	   
	   wind_strength_tv.setPaintFlags(wind_strength_tv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
	   gust_strength_tv.setPaintFlags(gust_strength_tv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
	   temperature_tv.setPaintFlags(temperature_tv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
	   weather_tv.setPaintFlags(weather_tv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
	   wind_direction_tv.setPaintFlags(wind_direction_tv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
	   
	   // Creating the PopupWindow
	   final PopupWindow popup = new PopupWindow(context);
	   popup.setContentView(windLayout);
	   popup.setWidth(popupWidth);
	   popup.setHeight(popupHeight);
	   popup.setFocusable(true);
	 
	   popup.setBackgroundDrawable(new BitmapDrawable());
	   
	   // Clear the default translucent background
	   //popup.setBackgroundDrawable(new BitmapDrawable());
	 
	   // Displaying the popup at the specified location, + offsets.
	   popup.showAtLocation(windLayout, Gravity.NO_GRAVITY, (1180-popupWidth)/2, 100); //xPos + OFFSET_X, yPos + OFFSET_Y);
	 
	   // Getting a reference to Save button, and close the popup when clicked.
	   Button saveWindButton = (Button) windLayout.findViewById(R.id.save_wind_btn);
	   saveWindButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
	   saveWindButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				int windDirection = 0;
			    Point point = db.getPoint(db.getLastPoint());  //Gets the last Point ID in the table, then gets the corresponding Point
			    
			    //AT SOME POINT I NEED TO DECIDE WHAT HAPPENS IF NO VALUE IS ENTERED.  FOR NOW, IT'S NOT IMPORTANT
			    String windDirectionString = wind_direction.getText().toString().replace(" ", "");
		    	windDirection = Integer.parseInt(windDirectionString);
			    point.setWindDirection(windDirection);
			    
                point.setWindStrength(wMag);
                point.setGustStrength(wMax);
                point.setTemperatureF(temperatureValue);
                point.setWeatherCondition(weatherCondition);
                
                db.updatePoint(point);
                
	            popup.dismiss();
			}
		});
	   
	   Button closeWindButton = (Button) windLayout.findViewById(R.id.close_btn);
	   closeWindButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFAA330F));
	   closeWindButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	// The method that displays the Startup Conditions popup.
	void startUpConditionsPopup(final Activity context) { //, int xPos, int yPos) { 
	   int popupWidth = 600;
	   int popupHeight = 500;
	   
	   // Inflate the popup_layout.xml
	   LinearLayout startupViewGroup = (LinearLayout) context.findViewById(R.id.startup_conditions_popup);
	   LayoutInflater startupLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	   final View startupLayout = startupLayoutInflater.inflate(R.layout.startup_popup_layout, startupViewGroup);
	   
	   possType = (RadioGroup) startupLayout.findViewById(R.id.poss_type);
	   startingSide = (RadioGroup) startupLayout.findViewById(R.id.starting_side);
	   	   
	   // Creating the PopupWindow
	   final PopupWindow popup = new PopupWindow(context);
	   popup.setContentView(startupLayout);
	   popup.setWidth(popupWidth);
	   popup.setHeight(popupHeight);
	   popup.setFocusable(true);
	   	 
	   // Clear the default translucent background
	   popup.setBackgroundDrawable(new BitmapDrawable());
	 
	   // Displaying the popup at the specified location, + offsets.
	   popup.showAtLocation(startupLayout, Gravity.NO_GRAVITY, (1180-popupWidth)/2, 100); //xPos + OFFSET_X, yPos + OFFSET_Y);
	 
	   // Getting a reference to Save button, and close the popup when clicked.
	   Button saveConditionsButton = (Button) startupLayout.findViewById(R.id.save_conditions_btn);
	   saveConditionsButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFF29ABDD));
	   saveConditionsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			    Point point = db.getPoint(db.getLastPoint());  //Gets the last Point ID in the table, then gets the corresponding Point
			    Possession possession = db.getPossession(db.getLastPossessionInPoint(point.getID()));
				RadioButton possTypeSelection;
				RadioButton startingSideSelection;

	            int selectedType = possType.getCheckedRadioButtonId();
	            possTypeSelection = (RadioButton) startupLayout.findViewById(selectedType);
	            String selectedTypeString = possTypeSelection.getText().toString();
	            possession.setPossessionType(selectedTypeString);
	            db.updatePossession(possession);
	            
	            int selectedSide = startingSide.getCheckedRadioButtonId();
	            startingSideSelection = (RadioButton) startupLayout.findViewById(selectedSide);
	            String selectedSideString = startingSideSelection.getText().toString();
	            point.setStartingSide(selectedSideString);
	            db.updatePoint(point);
	            popup.dismiss();
			}
		});
	   
	   Button closeConditionsButton = (Button) startupLayout.findViewById(R.id.close_btn);
	   closeConditionsButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFAA330F));
	   closeConditionsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
	            popup.dismiss();
			}
		});
	}

	public void didMainTeamScore(final String type, final int gameID) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			// set title
		alertDialogBuilder.setTitle(type + "?");
			// set dialog message
		alertDialogBuilder
			.setMessage("Did We Score?")
			.setCancelable(false)
			.setPositiveButton("Of course!", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					//FieldPage.this.finish();
					if (type.compareToIgnoreCase("Callahan") == 0) {
						db.deleteAction(db.getAction(db.getLastAction()));
						Action action = db.getAction(db.getLastAction());
						action.setActionType("Callahan");
						db.updateAction(action);
					}
					else if (type.compareToIgnoreCase("Goal") == 0) {
						db.deleteAction(db.getAction(db.getLastAction()));
						Action action = db.getAction(db.getLastAction());
						action.setActionType("Goal");
						db.updateAction(action);
					}
					//Update Score -> Create New Point (switch Side)/Poss (switch Type)/Action
					Point point = db.getPoint(db.getLastPointInGame(gameID));
					point.setMainTeamScore(point.getMainTeamScore()+1);
					db.updatePoint(point);
					
					switchDirection(point.getStartingSide());
					
					Point newPoint = new Point(point.getGameID(), point.getMainTeamScore(), point.getOpponentScore(), newSide, point.getWindDirection(), point.getWindStrength(), point.getGustStrength(), point.getTemperatureF(), point.getWeatherCondition());
					db.addPoint(newPoint);

					Possession possession = new Possession(db.getLastPoint(), "Defense", 0, 0, 0, 0, 0, 0, 0); //point id, possession type, player IDs (all set to 0, the Unknown ID)
					db.addPossession(possession);

					Action action = new Action(db.getLastPossession(), 0, "Pick Up", 0, 0); //possession ID, player ID (Unknown), action, X location, Y location
					db.addAction(action);

					mainTeamScore.setText(Integer.toString(db.getPoint(db.getLastPoint()).getMainTeamScore()));
					//This is less ideal because it doesn't reset the oldX and oldestX drawing values
					dialog.cancel();
					
					//redirect to lineup page
					Intent i = new Intent(FieldPage.this, LineUpPage.class);
					i.putExtra("Game ID", point.getGameID());
			        startActivity(i);
				}
			  })
			.setNegativeButton("Not yet",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close the dialog box and do nothing
					dialog.cancel();
				}
			});
				// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
			alertDialog.show();
	}

	public void didOpponentScore(final String type, final int gameID) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			// set title
		alertDialogBuilder.setTitle(type +"?");
			// set dialog message
		alertDialogBuilder
			.setMessage("Did the Opponent Score?")
			.setCancelable(false)
			.setPositiveButton("Yeah",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close current activity
					if (type.compareToIgnoreCase("Callahan") == 0) {
						//Update actions, then update score, then change side/Possession 
						db.deleteAction(db.getAction(db.getLastAction()));
						Action action = db.getAction(db.getLastAction());
						action.setActionType("Callahan");
						db.updateAction(action);
					}
					else if (type.compareToIgnoreCase("Goal") == 0) {
						db.deleteAction(db.getAction(db.getLastAction()));
						Action action = db.getAction(db.getLastAction());
						action.setActionType("Goal");
						db.updateAction(action);
					}
					//Update Score -> Create New Point (switch Side)/Poss (switch Type)/Action
					Point point = db.getPoint(db.getLastPointInGame(gameID));
					point.setOpponentScore(point.getOpponentScore()+1);
					db.updatePoint(point);
					
					switchDirection(point.getStartingSide());
					
					Point newPoint = new Point(point.getGameID(), point.getMainTeamScore(), point.getOpponentScore(), newSide, point.getWindDirection(), point.getWindStrength(), point.getGustStrength(), point.getTemperatureF(), point.getWeatherCondition());
					db.addPoint(newPoint);
					
					Possession possession = new Possession(db.getLastPoint(), "Offense", 0, 0, 0, 0, 0, 0, 0); //point id, possession type, player IDs (all set to 0, the Unknown ID)
					db.addPossession(possession);
					
					Action action = new Action(db.getLastPossession(), 0, "Pick Up", 0, 0); //possession ID, player ID (Unknown), action, X location, Y location
					db.addAction(action);
					
					opponentScore.setText(Integer.toString(db.getPoint(db.getLastPoint()).getOpponentScore()));
					//This is less ideal because it doesn't reset the oldX and oldestX drawing values
					dialog.cancel();
					
					//redirect to lineup page
					Intent i = new Intent(FieldPage.this, LineUpPage.class);
					i.putExtra("Game ID", point.getGameID());
			        startActivity(i);	
				}
			  })
			.setNegativeButton("Hell No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});
				// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
			alertDialog.show();
	}
	
	public void forcedTurnover(final String turnover) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			// set title
		alertDialogBuilder.setTitle("Forced Turnover?");
			// set dialog message
		alertDialogBuilder
			.setMessage("Was the " + turnover + " forced?")
			.setCancelable(false)
			.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					if (turnover.compareToIgnoreCase("Drop") == 0) {
						Action action = db.getAction(db.getLastAction()-2);
						action.setActionType("F" + turnover);
						db.updateAction(action);	
						
						db.deleteAction(db.getAction(db.getLastAction()-1));
					} 
					else if(turnover.compareToIgnoreCase("Throwaway") == 0) {
						Action action = db.getAction(db.getLastAction()-2);
						action.setActionType("Targeted");
						db.updateAction(action);
						
						Action actionTwo = db.getAction(db.getLastAction()-3);
						actionTwo.setActionType("F" + turnover);
						db.updateAction(actionTwo);
						
						db.deleteAction(db.getAction(db.getLastAction()-1));
					}
					else if(turnover.compareToIgnoreCase("Stall") == 0) {
						Action action = db.getAction(db.getLastAction()-1);
						action.setActionType("F" + turnover);
						db.updateAction(action);
					}					
					dialog.cancel();
				}
			  })
			.setNegativeButton("Nope",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					if (turnover.compareToIgnoreCase("Drop") == 0) {
						Action action = db.getAction(db.getLastAction()-2);
						action.setActionType(turnover);
						db.updateAction(action);
						
						db.deleteAction(db.getAction(db.getLastAction()-1));
					} 
					else if(turnover.compareToIgnoreCase("Throwaway") == 0) {
						Action action = db.getAction(db.getLastAction()-2);
						action.setActionType("Targeted");
						db.updateAction(action);
						
						Action actionTwo = db.getAction(db.getLastAction()-3);
						actionTwo.setActionType(turnover);
						db.updateAction(actionTwo);
						
						db.deleteAction(db.getAction(db.getLastAction()-1));
					}
					else if(turnover.compareToIgnoreCase("Stall") == 0) {
						Action action = db.getAction(db.getLastAction()-1);
						action.setActionType(turnover);
						db.updateAction(action);
					}
					dialog.cancel();
				}
			});
				// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
			alertDialog.show();
	}
	
	public String switchPossessionType(String offense) {
		if (offense.compareTo("Offense") == 0) {
			newType = "Defense";
		}
		else {
			newType = "Offense";
		}
		return newType;
	}
	
	public String switchDirection(String direction) {
		if (direction.compareTo("Left") == 0) {
			newSide = "Right";
		}
		else {
			newSide = "Left";
		}
		return newSide;
	}
	
	/**
     * Function to show settings alert dialog
     */
    public void showSettingsAlert() {
    	
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        
        //Setting Dialog Title
        alertDialog.setTitle("GPS Alert");
        //Setting Dialog Message
        alertDialog.setMessage("Turn on GPS?");
        //On Pressing Setting button
        alertDialog.setPositiveButton("Turn On", new DialogInterface.OnClickListener() 
        {   
            @Override
            public void onClick(DialogInterface dialog, int which) 
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        //On pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
        {   
            @Override
            public void onClick(DialogInterface dialog, int which) 
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}

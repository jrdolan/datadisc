package com.recording;

import java.util.List;

import com.example.ultimatestats.R;

import sqlite.helper.Point;
import sqlite.model.DatabaseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatsPage extends Activity {
	final DatabaseHandler db = new DatabaseHandler(this);
	TextView offPPP;
	TextView defPPP;
	TextView totalPPP;
	
	TextView offTOCount;
	TextView offTOReason;
	TextView defTOCount;
	TextView defTOReason;
	
	TextView under10C;
	TextView under20C;
	TextView under30C;
	TextView under40C;
	TextView under50C;
	TextView under60C;
	TextView under70C;
	TextView over70C;
	
	TextView under10A;
	TextView under20A;
	TextView under30A;
	TextView under40A;
	TextView under50A;
	TextView under60A;
	TextView under70A;
	TextView over70A;
	
	TextView under45C;
	TextView under90C;
	TextView under135C;
	TextView under180C;
	TextView under225C;
	TextView under270C;
	TextView under315C;
	TextView under360C;
	
	TextView under45A;
	TextView under90A;
	TextView under135A;
	TextView under180A;
	TextView under225A;
	TextView under270A;
	TextView under315A;
	TextView under360A;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_page);
		
		offPPP = (TextView)findViewById(R.id.offense_ppp);
		defPPP = (TextView)findViewById(R.id.defense_ppp);
		totalPPP = (TextView)findViewById(R.id.total_ppp);
		
		offTOCount= (TextView)findViewById(R.id.offensive_to_count);
		defTOCount= (TextView)findViewById(R.id.defensive_to_count);
		offTOReason= (TextView)findViewById(R.id.offensive_to_cause);
		defTOReason= (TextView)findViewById(R.id.defensive_to_cause);
		
		under10C = (TextView)findViewById(R.id.under_10_comp);
		under20C = (TextView)findViewById(R.id.under_20_comp);
		under30C = (TextView)findViewById(R.id.under_30_comp);
		under40C = (TextView)findViewById(R.id.under_40_comp);
		under50C = (TextView)findViewById(R.id.under_50_comp);
		under60C = (TextView)findViewById(R.id.under_60_comp);
		under70C = (TextView)findViewById(R.id.under_70_comp);
		over70C = (TextView)findViewById(R.id.over_70_comp);
		
		under10A = (TextView)findViewById(R.id.under_10_att);
		under20A = (TextView)findViewById(R.id.under_20_att);
		under30A = (TextView)findViewById(R.id.under_30_att);
		under40A = (TextView)findViewById(R.id.under_40_att);
		under50A = (TextView)findViewById(R.id.under_50_att);
		under60A = (TextView)findViewById(R.id.under_60_att);
		under70A = (TextView)findViewById(R.id.under_70_att);
		over70A = (TextView)findViewById(R.id.over_70_att);
		
		under45C = (TextView)findViewById(R.id.under_45_comp);
		under90C = (TextView)findViewById(R.id.under_90_comp);
		under135C = (TextView)findViewById(R.id.under_135_comp);
		under180C = (TextView)findViewById(R.id.under_180_comp);
		under225C = (TextView)findViewById(R.id.under_225_comp);
		under270C = (TextView)findViewById(R.id.under_270_comp);
		under315C = (TextView)findViewById(R.id.under_315_comp);
		under360C = (TextView)findViewById(R.id.under_360_comp);
		
		under45A = (TextView)findViewById(R.id.under_45_att);
		under90A = (TextView)findViewById(R.id.under_90_att);
		under135A = (TextView)findViewById(R.id.under_135_att);
		under180A = (TextView)findViewById(R.id.under_180_att);
		under225A = (TextView)findViewById(R.id.under_225_att);
		under270A = (TextView)findViewById(R.id.under_270_att);
		under315A = (TextView)findViewById(R.id.under_315_att);
		under360A = (TextView)findViewById(R.id.under_360_att);
		
		Bundle extras = getIntent().getExtras();
		final int gameID = extras.getInt("Game ID");
		
		//Let's go in order
		//Points Per Possession.  
		setPointsPerPoss(gameID);
		//Turnover counts and causes
		setOffTurnoverStats(gameID);
		setDefTurnoverStats(gameID);
		//Completion Percentages
		setCompletions(gameID);
		
		final Button backButton = (Button) findViewById(R.id.go_back);
		backButton.getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xFFAA330F));
		backButton.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(StatsPage.this, FieldPage.class);
				i.putExtra("Game ID", gameID);
				finish();
		        startActivity(i);		
			}
		});
		
	}
	
	public void setPointsPerPoss(int gameID) {
		List<Point> pointList = db.getAllPointsInGame(gameID);
		double offPossCount = 0;
		double defPossCount = 0;
		double offPointCount = 0;
		double defPointCount = 0;
		
		for (int i = 0; i < pointList.size()-1; i++) { //get all points in game, minus 1 (because it auto creates a new one)
			String[] possList = db.getPossessionsInPoint(pointList.get(i).getID()); //gets all possessions in a point
			String startingPoss = db.getPossession(Integer.parseInt(possList[0])).getPossessionType(); //first one the starting Poss
			if (startingPoss.compareToIgnoreCase("Offense") == 0) {
				//We're on O
				for (int j = 0; j < possList.length; j++ ) {
					if (db.getPossession(Integer.parseInt(possList[j])).getPossessionType().compareToIgnoreCase("Offense") == 0) {
						offPossCount++; //This should give me every offensive possession when we start on O
					}
				}
				if ((pointList.get(i+1).getMainTeamScore() - pointList.get(i).getMainTeamScore()) == 1) {
					//We scored
					offPointCount++;
				}
			}
			else if (startingPoss.compareToIgnoreCase("Defense") == 0) {
				//We're on D
				for (int j = 0; j < possList.length; j++ ) {
					if (db.getPossession(Integer.parseInt(possList[j])).getPossessionType().compareToIgnoreCase("Defense") == 0) {
						defPossCount++; //This should give me every offensive possession when we start on D
					}
				}
				if ((pointList.get(i+1).getMainTeamScore() - pointList.get(i).getMainTeamScore()) == 1) {
					//We scored
					defPointCount++;
				}
			}
		}
		
		offPPP.setText(String.format("%.2f", offPointCount/offPossCount)); //Double.toString(offPointCount/offPossCount)));
		defPPP.setText(String.format("%.2f", defPointCount/defPossCount));
	}
	
	public void setOffTurnoverStats(int gameID) {
		List<Point> pointList = db.getAllPointsInGame(gameID);
		int throwawayCount = 0;
		int fThrowawayCount = 0;
		int dropCount = 0;
		int fDropCount = 0;
		int stallCount = 0;
		int fStallCount = 0;
		int blockCount = 0;
		int throwOBCount = 0;
		
		for (int i = 0; i < pointList.size(); i++) { //get all points in game, minus 1 (because it auto creates a new one)

			List<Integer> throwawayIDs = db.getActionsInOffense(pointList.get(i).getID(), "Throwaway");
			List<Integer> fThrowawayIDs = db.getActionsInOffense(pointList.get(i).getID(), "FThrowaway");
			List<Integer> dropIDs = db.getActionsInOffense(pointList.get(i).getID(), "Drop");
			List<Integer> fDropIDs = db.getActionsInOffense(pointList.get(i).getID(), "FDrop");
			List<Integer> stallIDs = db.getActionsInOffense(pointList.get(i).getID(), "Stall");
			List<Integer> fStallIDs = db.getActionsInOffense(pointList.get(i).getID(), "FStall");
			List<Integer> blockedIDs = db.getActionsInOffense(pointList.get(i).getID(), "Blocked");
			List<Integer> throwOBIDs = db.getActionsInOffense(pointList.get(i).getID(), "ThrowOB");

			throwawayCount = throwawayIDs.size();
			fThrowawayCount = fThrowawayIDs.size();
			dropCount = dropIDs.size();
			fDropCount = fDropIDs.size();
			stallCount = stallIDs.size();
			fStallCount = fStallIDs.size();
			blockCount = blockedIDs.size();
			throwOBCount = throwOBIDs.size();
			
			Integer[] toArray = {throwawayCount, fThrowawayCount, dropCount, fDropCount, stallCount, fStallCount, blockCount, throwOBCount};
			String[] toNames = {"Throw Away", "FThrow Away", "Drop", "FDrop", "Stall", "FStall", "Blocked", "ThrowOB"};
			int maxTO = 0; //throwawayCount;
			int toType = 0;
			int toSum = 0;
			for (int k = 0; k < toArray.length; k++ ) {
				if (toArray[k] > maxTO ) {
					maxTO = toArray[k];
					toType = k;
				}
				toSum = toSum + toArray[k];
			}
			
			offTOCount.setText(Integer.toString(toSum));
			offTOReason.setText(toNames[toType]);
		}
	}
	
	public void setDefTurnoverStats(int gameID) {
		List<Point> pointList = db.getAllPointsInGame(gameID);
		int throwawayCount = 0;
		int fThrowawayCount = 0;
		int dropCount = 0;
		int fDropCount = 0;
		int stallCount = 0;
		int fStallCount = 0;
		int blockCount = 0;
		int throwOBCount = 0;
		
		for (int i = 0; i < pointList.size(); i++) { //get all points in game, minus 1 (because it auto creates a new one)

			List<Integer> throwawayIDs = db.getActionsInDefense(pointList.get(i).getID(), "Throwaway");
			List<Integer> fThrowawayIDs = db.getActionsInDefense(pointList.get(i).getID(), "FThrowaway");
			List<Integer> dropIDs = db.getActionsInDefense(pointList.get(i).getID(), "Drop");
			List<Integer> fDropIDs = db.getActionsInDefense(pointList.get(i).getID(), "FDrop");
			List<Integer> stallIDs = db.getActionsInDefense(pointList.get(i).getID(), "Stall");
			List<Integer> fStallIDs = db.getActionsInDefense(pointList.get(i).getID(), "FStall");
			List<Integer> blockedIDs = db.getActionsInDefense(pointList.get(i).getID(), "Blocked");
			List<Integer> throwOBIDs = db.getActionsInDefense(pointList.get(i).getID(), "ThrowOB");

			throwawayCount = throwawayIDs.size();
			fThrowawayCount = fThrowawayIDs.size();
			dropCount = dropIDs.size();
			fDropCount = fDropIDs.size();
			stallCount = stallIDs.size();
			fStallCount = fStallIDs.size();
			blockCount = blockedIDs.size();
			throwOBCount = throwOBIDs.size();
			
			Integer[] toArray = {throwawayCount, fThrowawayCount, dropCount, fDropCount, stallCount, fStallCount, blockCount, throwOBCount};
			String[] toNames = {"Throw Away", "FThrow Away", "Drop", "FDrop", "Stall", "FStall", "Blocked", "ThrowOB"};
			int maxTO = 0; //throwawayCount;
			int toType = 0;
			int toSum = 0;
			for (int k = 0; k < toArray.length; k++ ) {
				if (toArray[k] > maxTO ) {
					maxTO = toArray[k];
					toType = k;
				}
				toSum = toSum + toArray[k];
			}
			
			defTOCount.setText(Integer.toString(toSum));
			defTOReason.setText(toNames[toType]);
		}
	}
	
	public void setCompletions(int gameID) {
		List<Point> pointList = db.getAllPointsInGame(gameID);
		
		int under10Comp = 0, under20Comp = 0, under30Comp = 0, under40Comp = 0, under50Comp = 0, under60Comp = 0, under70Comp = 0, over70Comp = 0;
		int under10Att = 0, under20Att = 0, under30Att = 0, under40Att = 0, under50Att = 0, under60Att = 0, under70Att = 0, over70Att = 0;
		int under45Comp = 0, under90Comp = 0, under135Comp = 0, under180Comp = 0, under225Comp = 0, under270Comp = 0, under315Comp = 0, under360Comp = 0;
		int under45Att = 0, under90Att = 0, under135Att = 0, under180Att = 0, under225Att = 0, under270Att = 0, under315Att = 0, under360Att = 0;
		
		for (int i = 0; i < pointList.size(); i++) { //get all points in game
			//Don't subtract 1, because i starts at 0 and ids start at 1.  
			//This is dumb.  I'm measuring total distance, not just X distance. I should adjust the breakdown
			under10Comp += db.getCompletions(pointList.get(i).getID(), 0, 10);
			under20Comp += db.getCompletions(pointList.get(i).getID(), 10, 20);
			under30Comp += db.getCompletions(pointList.get(i).getID(), 20, 30);
			under40Comp += db.getCompletions(pointList.get(i).getID(), 30, 40);
			under50Comp += db.getCompletions(pointList.get(i).getID(), 40, 50);
			under60Comp += db.getCompletions(pointList.get(i).getID(), 50, 60);
			under70Comp += db.getCompletions(pointList.get(i).getID(), 60, 70);
			over70Comp += db.getCompletions(pointList.get(i).getID(), 70, 121);
			
			under10Att += db.getAttempts(pointList.get(i).getID(), 0, 10);
			under20Att += db.getAttempts(pointList.get(i).getID(), 10, 20);
			under30Att += db.getAttempts(pointList.get(i).getID(), 20, 30);
			under40Att += db.getAttempts(pointList.get(i).getID(), 30, 40);
			under50Att += db.getAttempts(pointList.get(i).getID(), 40, 50);
			under60Att += db.getAttempts(pointList.get(i).getID(), 50, 60);
			under70Att += db.getAttempts(pointList.get(i).getID(), 60, 70);
			over70Att += db.getAttempts(pointList.get(i).getID(), 70, 121);
			
			under45Comp += db.getCompletionsByDirection(pointList.get(i).getID(), 0, 45);
			under90Comp += db.getCompletionsByDirection(pointList.get(i).getID(), 45, 90);
			under135Comp += db.getCompletionsByDirection(pointList.get(i).getID(), 90, 135);
			under180Comp += db.getCompletionsByDirection(pointList.get(i).getID(), 135, 180);
			under225Comp += db.getCompletionsByDirection(pointList.get(i).getID(), 180, 225);
			under270Comp += db.getCompletionsByDirection(pointList.get(i).getID(), 225, 270);
			under315Comp += db.getCompletionsByDirection(pointList.get(i).getID(), 270, 315);
			under360Comp += db.getCompletionsByDirection(pointList.get(i).getID(), 315, 360);
			
			under45Att += db.getAttemptsByDirection(pointList.get(i).getID(), 0, 45);
			under90Att += db.getAttemptsByDirection(pointList.get(i).getID(), 45, 90);
			under135Att += db.getAttemptsByDirection(pointList.get(i).getID(), 90, 135);
			under180Att += db.getAttemptsByDirection(pointList.get(i).getID(), 135, 180);
			under225Att += db.getAttemptsByDirection(pointList.get(i).getID(), 180, 225);
			under270Att += db.getAttemptsByDirection(pointList.get(i).getID(), 225, 270);
			under315Att += db.getAttemptsByDirection(pointList.get(i).getID(), 270, 315);
			under360Att += db.getAttemptsByDirection(pointList.get(i).getID(), 315, 360);
		}
		
		under10C.setText(Integer.toString(under10Comp));
		under20C.setText(Integer.toString(under20Comp));
		under30C.setText(Integer.toString(under30Comp));
		under40C.setText(Integer.toString(under40Comp));
		under50C.setText(Integer.toString(under50Comp));
		under60C.setText(Integer.toString(under60Comp));
		under70C.setText(Integer.toString(under70Comp));
		over70C.setText(Integer.toString(over70Comp));
		
		under10A.setText(Integer.toString(under10Att));	
		under20A.setText(Integer.toString(under20Att));
		under30A.setText(Integer.toString(under30Att));
		under40A.setText(Integer.toString(under40Att));
		under50A.setText(Integer.toString(under50Att));
		under60A.setText(Integer.toString(under60Att));
		under70A.setText(Integer.toString(under70Att));
		over70A.setText(Integer.toString(over70Att));
		
		under45C.setText(Integer.toString(under45Comp));
		under90C.setText(Integer.toString(under90Comp));
		under135C.setText(Integer.toString(under135Comp));
		under180C.setText(Integer.toString(under180Comp));
		under225C.setText(Integer.toString(under225Comp));
		under270C.setText(Integer.toString(under270Comp));
		under315C.setText(Integer.toString(under315Comp));
		under360C.setText(Integer.toString(under360Comp));
		
		under45A.setText(Integer.toString(under45Att));	
		under90A.setText(Integer.toString(under90Att));
		under135A.setText(Integer.toString(under135Att));
		under180A.setText(Integer.toString(under180Att));
		under225A.setText(Integer.toString(under225Att));
		under270A.setText(Integer.toString(under270Att));
		under315A.setText(Integer.toString(under315Att));
		under360A.setText(Integer.toString(under360Att));
	}
	
    public String[] getActions() {
    	int actionCount = db.getLastAction();
    	String[] displayedActions = new String[actionCount]; 

    	String[] allActionIDs = db.getAllActionIDs(); 
    	String[] allActionTypes = db.getAllActionTypes();
    	String[] allActionPlayers = db.getAllActionPlayers();
    	String[] allActionPossessions = db.getAllActionPossessions(); 
    	
		for (int index = 0; index < displayedActions.length; index++) { 
			if (Integer.parseInt(allActionPlayers[index]) == 0) {
				String playerName = "Unknown";
		    	displayedActions[index] = allActionIDs[index] + "     " + allActionTypes[index] + "     " + playerName + "     " + allActionPossessions[index] + " (" + db.getPossession(Integer.parseInt(allActionPossessions[index])).getPossessionType() + ")";
			}
			else {
				String playerName = db.getPlayer(Integer.parseInt(allActionPlayers[index])).getPlayerNickName();
		    	displayedActions[index] = allActionIDs[index] + "     " + allActionTypes[index] + "     " + playerName + "     " + allActionPossessions[index] + " (" + db.getPossession(Integer.parseInt(allActionPossessions[index])).getPossessionType() + ")";
			}

	    }
	    return displayedActions;
	
	}

}

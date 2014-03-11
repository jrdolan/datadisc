package sqlite.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sqlite.helper.Game;
import sqlite.helper.Player;
import sqlite.helper.Team;
import sqlite.helper.Point;
import sqlite.helper.Possession;
import sqlite.helper.Action;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import au.com.bytecode.opencsv.CSVWriter;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	//Logcat Tag
	private static final String LOG = "DatabaseHelper";
	
	//Database Version
	public static final int DB_VERSION = 1;
	
	//Database Name
	public static final String DB_NAME = "statisticsManager";
	
	//Team table name - There are tables for individual things, as well as linking tables.
	public static final String TABLE_TEAM = "TeamTable";
	public static final String TABLE_PLAYER = "PlayerTable";
	public static final String TABLE_GAME = "GameTable";
	public static final String TABLE_POINT = "PointTable";
	public static final String TABLE_POSSESSION = "PossessionTable";
	public static final String TABLE_ACTION = "ActionTable";
	//Missing Action, Game_Team, Game_Action, Action_Player at minimum
		//Do I want to break it down further using Possessions? 
	
	//Common column names
	public static final String KEY_ID = "id";
	
	//Team table column names
	public static final String KEY_TEAM_NAME = "teamName";
	
	//Player table column names
	public static final String KEY_PLAYER_FIRST_NAME = "playerFirstName";
	public static final String KEY_PLAYER_LAST_NAME = "playerLastName";
	public static final String KEY_PLAYER_NUMBER = "playerNumber";
	public static final String KEY_PLAYER_NICK_NAME = "playerNickName";
	public static final String KEY_TEAM_ID = "team_id";
	
	//Game table column names
	public static final String KEY_GAME_MAIN_TEAM = "mainTeam";
	public static final String KEY_GAME_OPPONENT = "opponent";
	//public static final String KEY_GAME_DATE = "gameDate";
	//public static final String KEY_GAME_START_TIME = "startTime";
	
	//POINT table column names
	public static final String KEY_POINT_GAME_ID = "game_id";
	public static final String KEY_POINT_MAIN_TEAM_SCORE = "mainTeamScore";
	public static final String KEY_POINT_OPPONENT_SCORE = "opponentScore";
	public static final String KEY_POINT_STARTING_SIDE = "startingSide";
	public static final String KEY_POINT_WIND_DIRECTION = "windDirection";
	public static final String KEY_POINT_WIND_STRENGTH = "windStrength";
	public static final String KEY_POINT_GUST_STRENGTH = "gustStrength";
	public static final String KEY_POINT_TEMPERATURE = "temperatureF";
	public static final String KEY_POINT_WEATHER = "weatherCondition";

	//POSSESSION table column names
	public static final String KEY_POSSESSION_POINT_ID = "point_id";
	public static final String KEY_POSSESSION_TYPE = "possessionType";
	public static final String KEY_POSSESSION_PLAYER1_ID = "player1ID";
	public static final String KEY_POSSESSION_PLAYER2_ID = "player2ID";
	public static final String KEY_POSSESSION_PLAYER3_ID = "player3ID";
	public static final String KEY_POSSESSION_PLAYER4_ID = "player4ID";
	public static final String KEY_POSSESSION_PLAYER5_ID = "player5ID";
	public static final String KEY_POSSESSION_PLAYER6_ID = "player6ID";
	public static final String KEY_POSSESSION_PLAYER7_ID = "player7ID";
	public static final String KEY_POSSESSION_PLAYER_UNKNOWN_ID = "playerUnknownID";

	//Actions table column names
	public static final String KEY_ACTION_POSSESSION_ID = "possessionID";
	public static final String KEY_ACTION_PLAYER_ID = "playerID";
	public static final String KEY_ACTION_TYPE = "actionType";
	public static final String KEY_ACTION_X_POSITION = "xPosition";
	public static final String KEY_ACTION_Y_POSITION = "yPosition";
	public static final String KEY_ACTION_DISTANCE = "distance";
	public static final String KEY_ACTION_ANGLE = "angle";
	
	//Table creation statements - format of (KEY VALUE, data type, repeat)
		//Team table
	private static final String CREATE_TEAM_TABLE = "CREATE TABLE " + TABLE_TEAM + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TEAM_NAME + " TEXT" + ")";

		//Player table
	private static final String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYER + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PLAYER_FIRST_NAME + " TEXT," + KEY_PLAYER_LAST_NAME + " TEXT," + KEY_PLAYER_NUMBER + " INTEGER," + KEY_PLAYER_NICK_NAME + " TEXT," + KEY_TEAM_ID + " INTEGER" + ")";
	
		//Game table
	//private static final String CREATE_GAME_TABLE = "CREATE TABLE " + TABLE_GAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_GAME_MAIN_TEAM + " TEXT" + ")";
	private static final String CREATE_GAME_TABLE = "CREATE TABLE " + TABLE_GAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_GAME_MAIN_TEAM + " TEXT," + KEY_GAME_OPPONENT + " TEXT" + ")"; /*," + KEY_GAME_DATE + " INTEGER " + KEY_GAME_START_TIME + " INTEGER" + ")"; */

		//POINT table
	private static final String CREATE_POINT_TABLE = "CREATE TABLE " + TABLE_POINT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_POINT_GAME_ID + " INTEGER," + KEY_POINT_MAIN_TEAM_SCORE + " INTEGER," + KEY_POINT_OPPONENT_SCORE + " INTEGER," + 
													  KEY_POINT_STARTING_SIDE + " TEXT," + KEY_POINT_WIND_DIRECTION + " INTEGER," + KEY_POINT_WIND_STRENGTH + " INTEGER," + KEY_POINT_GUST_STRENGTH + " INTEGER," + KEY_POINT_TEMPERATURE + " INTEGER," +
													  KEY_POINT_WEATHER + " STRING)";
	
		//POSSESSION table
	private static final String CREATE_POSSESSION_TABLE = "CREATE TABLE " + TABLE_POSSESSION + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_POSSESSION_POINT_ID + " INTEGER," + KEY_POSSESSION_TYPE + " TEXT," + KEY_POSSESSION_PLAYER1_ID + " INTEGER," + KEY_POSSESSION_PLAYER2_ID + " INTEGER," + 
		KEY_POSSESSION_PLAYER3_ID + " INTEGER," + KEY_POSSESSION_PLAYER4_ID + " INTEGER," + KEY_POSSESSION_PLAYER5_ID + " INTEGER," + KEY_POSSESSION_PLAYER6_ID + " INTEGER," + KEY_POSSESSION_PLAYER7_ID + " INTEGER," + KEY_POSSESSION_PLAYER_UNKNOWN_ID + " INTEGER)"; 
	
		//ACTION table
	private static final String CREATE_ACTION_TABLE = "CREATE TABLE " + TABLE_ACTION + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ACTION_POSSESSION_ID + " INTEGER," + KEY_ACTION_PLAYER_ID + " INTEGER," + KEY_ACTION_TYPE + " TEXT," + KEY_ACTION_X_POSITION + " INTEGER," + 
		KEY_ACTION_Y_POSITION + " INTEGER," + KEY_ACTION_DISTANCE + " DOUBLE," + KEY_ACTION_ANGLE + " DOUBLE)";
	
	
	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION); //context(?), Database name, no idea, and Version #
		
	}

	//Creating the tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		//String CREATE_TEAM_TABLE = "CREATE TABLE " + TABLE_TEAM + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TEAM_NAME + " TEXT" + ")";
        db.execSQL(CREATE_TEAM_TABLE);
        db.execSQL(CREATE_PLAYER_TABLE);
        db.execSQL(CREATE_GAME_TABLE);
        db.execSQL(CREATE_POINT_TABLE);
        db.execSQL(CREATE_POSSESSION_TABLE);
        db.execSQL(CREATE_ACTION_TABLE);
	    
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSSESSION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTION);
		 
        // Create tables again
        onCreate(db);
	}
	
	
//TEAM SPECIFIC FUNCTIONS
	//Add new team
	public void addTeam(Team team) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_TEAM_NAME, team.getTeamName()); //Puts team name in the values box.  By using multiple values.put, I can insert multiple columns at once
		
		db.insert(TABLE_TEAM, null, values); //Inserts the values into the table
		db.close();
	}
	
	//Get single team by ID
	public Team getTeam(int team_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		//Cursor cursor = db.query(TABLE_TEAM, new String[] { KEY_ID, KEY_TEAM_NAME }, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
		String selectQuery = "SELECT * FROM " + TABLE_TEAM + " WHERE " + KEY_ID + " = " + team_id;
		
		Log.e(LOG, selectQuery);
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor != null)
			cursor.moveToFirst();
		
		Team team = new Team(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
		
		return team;
	}
	
	//Get all teams
	public List<Team> getAllTeams() {
		List<Team> teamList = new ArrayList<Team>();
		
		//Select All query
		String selectAllQuery = "SELECT * FROM " + TABLE_TEAM;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAllQuery, null);
		
		//Loop through rows and add to list
		if (cursor.moveToFirst()) {
			do {
				Team team = new Team();
				//Set values
				team.setID(Integer.parseInt(cursor.getString(0)));
				team.setTeamName(cursor.getString(1)); //Not sure why this only uses cursor and the above use Integer and cursor.  Must be a type thing
				//Add team to list
				teamList.add(team);				
			} while (cursor.moveToNext());
		}
		
		return teamList;
	}
	
	//Get team count
	public int getTeamCount() {
		String countQuery = "SELECT * FROM " + TABLE_TEAM;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		
		return cursor.getCount();
	}
	
	//Get all team names *
	public String[] getTeamNames() {
		//List<Team> teamNames = new ArrayList<Team>();
		Cursor cursor = getReadableDatabase().rawQuery("SELECT teamName FROM " + TABLE_TEAM, new String[] {});
		
		String[] teamNames = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
		    String uname = cursor.getString(cursor.getColumnIndex(KEY_TEAM_NAME));
		    teamNames[i] = uname;
		    i++;
		}
		
		return teamNames;
	}
	
	//Update single team
	public int updateTeam(Team team) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_TEAM_NAME, team.getTeamName());
		
		//Update row
		return db.update(TABLE_TEAM, values, KEY_ID + " = ?", new String [] { String.valueOf(team.getID()) });
	}
	
	//Delete team
	public void deleteTeam(Team team) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TEAM, KEY_ID + " = ?", new String [] { String.valueOf(team.getID()) });
		db.close();
	}
	
	//Get single team by NAME
	public Team getTeam(String teamName) {
		SQLiteDatabase db = this.getReadableDatabase();
			
		Cursor cursor = db.query(TABLE_TEAM, new String[] { KEY_ID, KEY_TEAM_NAME }, KEY_TEAM_NAME + "=?", new String[] { teamName }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Team team = new Team(Integer.parseInt(cursor.getString(0)),cursor.getString(1));
			
		return team;
	}
	
	
	
//PLAYER SPECIFIC FUNCTIONS
	//Add new player
	public void addPlayer(Player player) {
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(KEY_PLAYER_FIRST_NAME, player.getPlayerFirstName()); 
		values.put(KEY_PLAYER_LAST_NAME, player.getPlayerLastName()); 
		values.put(KEY_PLAYER_NUMBER, player.getPlayerNumber()); 
		values.put(KEY_PLAYER_NICK_NAME, player.getPlayerNickName()); 
		values.put(KEY_TEAM_ID, player.getTeamId()); 
		
		db.insert(TABLE_PLAYER, null, values); //Inserts the values into the table
		db.close();
	}
		
	//Get single player by ID
	public Player getPlayer(int player_id) {
		SQLiteDatabase db = this.getReadableDatabase();
			
		String selectQuery = "SELECT * FROM " + TABLE_PLAYER + " WHERE " + KEY_ID + " = " + player_id;
			
		Log.e(LOG, selectQuery);
			
		Cursor cursor = db.rawQuery(selectQuery, null);
			
		if (cursor != null)
			cursor.moveToFirst();
			
		Player player = new Player(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),Integer.parseInt(cursor.getString(3)), cursor.getString(4), Integer.parseInt(cursor.getString(5)));
			return player;
		}
		
	//Get all players
	public List<Player> getAllPlayers() {
		List<Player> playerList = new ArrayList<Player>();
			
		//Select All query
		String selectAllQuery = "SELECT * FROM " + TABLE_PLAYER;
			
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAllQuery, null);
			
		//Loop through rows and add to list
		if (cursor.moveToFirst()) {
			do {
				Player player = new Player();
				//Set values
				player.setID(Integer.parseInt(cursor.getString(0)));
				player.setPlayerFirstName(cursor.getString(1)); 
				player.setPlayerLastName(cursor.getString(2)); 
				player.setPlayerNumber(Integer.parseInt(cursor.getString(3)));
				player.setPlayerLastName(cursor.getString(4)); 
				player.setTeamId(Integer.parseInt(cursor.getString(5)));
				//Add player to list
				playerList.add(player);				
			} while (cursor.moveToNext());
		}
			
		return playerList;
	}
		
	//Get player count
	public int getPlayerCount() {
		String countQuery = "SELECT * FROM " + TABLE_PLAYER;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
			
		return cursor.getCount();
	}
		
	//Get all player first names *
	public String[] getPlayerFirstNames() {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT playerFirstName FROM " + TABLE_PLAYER, new String[] {});
			
		String[] playerFirstNames = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String uname = cursor.getString(cursor.getColumnIndex(KEY_PLAYER_FIRST_NAME));
			playerFirstNames[i] = uname;
			i++;
		}	
		return playerFirstNames;
	}
	
	//Get all player last names *
	public String[] getPlayerLastNames() {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT playerLastName FROM " + TABLE_PLAYER, new String[] {});
			
		String[] playerLastNames = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String uname = cursor.getString(cursor.getColumnIndex(KEY_PLAYER_LAST_NAME));
			playerLastNames[i] = uname;
			i++;
		}	
		return playerLastNames;
	}
	
	//Get all player numbers
	public String[] getPlayerNumbers() {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT playerNumber FROM " + TABLE_PLAYER, new String[] {});
				
		String[] playerNumbers = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String uname = cursor.getString(cursor.getColumnIndex(KEY_PLAYER_NUMBER));
			playerNumbers[i] = uname;
			i++;
		}	
		return playerNumbers;
	}
		
	//Get all player nick names *
		public String[] getPlayerNickNames() {
			Cursor cursor = getReadableDatabase().rawQuery("SELECT playerNickName FROM " + TABLE_PLAYER, new String[] {});
				
			String[] playerNickNames = new String[cursor.getCount()];
			int i = 0;
			while (cursor.moveToNext()){
				String uname = cursor.getString(cursor.getColumnIndex(KEY_PLAYER_NICK_NAME));
				playerNickNames[i] = uname;
				i++;
			}	
			return playerNickNames;
		}
		
	//Update single player - Not 100% sure this one works...
	public int updatePlayer(Player player) {
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(KEY_PLAYER_FIRST_NAME, player.getPlayerFirstName());
		values.put(KEY_PLAYER_LAST_NAME, player.getPlayerLastName());
		values.put(KEY_PLAYER_NUMBER, player.getPlayerNumber());
		values.put(KEY_PLAYER_NICK_NAME, player.getPlayerNickName());
		values.put(KEY_TEAM_ID, player.getTeamId());
			
		//Update row
		return db.update(TABLE_PLAYER, values, KEY_ID + " = ?", new String [] { String.valueOf(player.getID()) });
	}
		
	//Delete player
	public void deletePlayer(Player player) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PLAYER, KEY_ID + " = ?", new String [] { String.valueOf(player.getID()) });
		db.close();
	}
		
	
	//Get player by first name, last name, number, nickname
	public Player getPlayer(String firstName, String lastName, int number, String nickName) {
		SQLiteDatabase db = this.getReadableDatabase();
				
		Cursor cursor = db.query(TABLE_PLAYER, new String[] { KEY_ID, KEY_PLAYER_FIRST_NAME, KEY_PLAYER_LAST_NAME, KEY_PLAYER_NUMBER, KEY_PLAYER_NICK_NAME, KEY_TEAM_ID}, KEY_PLAYER_FIRST_NAME + "=? AND " + KEY_PLAYER_LAST_NAME + "=? AND " + KEY_PLAYER_NUMBER + "=? AND " + KEY_PLAYER_NICK_NAME + "=?", new String[] { firstName, lastName, String.valueOf(number), nickName }, null, null, null, null);
		//Cursor cursor = db.query(TABLE_PLAYER, new String[] { KEY_ID, KEY_PLAYER_FIRST_NAME, KEY_PLAYER_LAST_NAME, KEY_PLAYER_NUMBER, KEY_TEAM_ID}, KEY_PLAYER_FIRST_NAME + "=? AND " + KEY_PLAYER_LAST_NAME + "=?", new String[] { firstName+" ", lastName+" " }, null, null, null, null);
		
		if (cursor != null)
		
			cursor.moveToFirst();
		
		Log.d("Cursor", "Player ID: " + cursor.getString(0)); //player id
		//Log.d("Cursor", cursor.getString(1)); //first name
		//Log.d("Cursor", cursor.getString(2)); //last name
		//Log.d("Cursor", cursor.getString(3)); //number
		//Log.d("Cursor", cursor.getString(4)); //team id

		Player player = new Player(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4), Integer.parseInt(cursor.getString(5))); //, Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
				
		return player;
	}
	
	
	//Get all player first names on a single team
	public String[] getTeamPlayerFirstNames(int teamId) {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT playerFirstName FROM " + TABLE_PLAYER + " WHERE " + KEY_TEAM_ID + " = " + teamId, new String[] {});
			
		String[] playerFirstNames = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String uname = cursor.getString(cursor.getColumnIndex(KEY_PLAYER_FIRST_NAME));
			playerFirstNames[i] = uname;
			i++;
		}	
		return playerFirstNames;
	}
	
	//Get all player last names on a single team*
	public String[] getTeamPlayerLastNames(int teamId) {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT playerLastName FROM " + TABLE_PLAYER + " WHERE " + KEY_TEAM_ID + " = " + teamId, new String[] {});
			
		String[] playerLastNames = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String uname = cursor.getString(cursor.getColumnIndex(KEY_PLAYER_LAST_NAME));
			playerLastNames[i] = uname;
			i++;
		}	
		return playerLastNames;
	}
		
	//Get all player numbers on a single team
	public String[] getTeamPlayerNumbers(int teamId) {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT playerNumber FROM " + TABLE_PLAYER + " WHERE " + KEY_TEAM_ID + " = " + teamId, new String[] {});
					
		String[] playerNumbers = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String uname = cursor.getString(cursor.getColumnIndex(KEY_PLAYER_NUMBER));
			playerNumbers[i] = uname;
			i++;
		}	
		return playerNumbers;
	}
		
	//Get all player nick names on a single team
	public String[] getTeamPlayerNickNames(int teamId) {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT playerNickName FROM " + TABLE_PLAYER + " WHERE " + KEY_TEAM_ID + " = " + teamId, new String[] {});
			
		String[] playerNickNames = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String uname = cursor.getString(cursor.getColumnIndex(KEY_PLAYER_NICK_NAME));
			playerNickNames[i] = uname;
			i++;
		}	
		return playerNickNames;
	}
		
		
	
	//GAME SPECIFIC FUNCTIONS
		//Add new game
	public void addGame(Game game) {
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(KEY_GAME_MAIN_TEAM, game.getMainTeam()); 
		values.put(KEY_GAME_OPPONENT, game.getOpponent()); 
		//values.put(KEY_GAME_DATE, game.getGameDate()); 
		//values.put(KEY_GAME_START_TIME, game.getStartTime()); 
			
		db.insert(TABLE_GAME, null, values); //Inserts the values into the table
		db.close();
	}
		
		//Get single game by ID
	public Game getGame(int game_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		//String selectQuery = "SELECT * FROM " + TABLE_TEAM + " WHERE " + KEY_ID + " = " + game_id;
		//Log.e(LOG, selectQuery);	
		//Cursor cursor = db.rawQuery(selectQuery, null);
		Cursor cursor = db.query(TABLE_GAME, new String[] { KEY_ID, KEY_GAME_MAIN_TEAM, KEY_GAME_OPPONENT}, KEY_ID + "=?", new String[] { Integer.toString(game_id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		
		Game game = new Game(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2)); //, Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
		
		return game;
	}
	
	//Get single game by NAMES
	public Game getGame(String mainTeam, String opponent) {
		SQLiteDatabase db = this.getReadableDatabase();
				
		Cursor cursor = db.query(TABLE_GAME, new String[] { KEY_ID, KEY_GAME_MAIN_TEAM, KEY_GAME_OPPONENT}, KEY_GAME_MAIN_TEAM + "=? AND " + KEY_GAME_OPPONENT + "=?", new String[] { mainTeam, opponent }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Game game = new Game(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2)); //, Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
				
		return game;
	}
	
		
		//Get all games
	public List<Game> getAllGames() {
		List<Game> gameList = new ArrayList<Game>();
			
		//Select All query
		String selectAllQuery = "SELECT * FROM " + TABLE_GAME;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAllQuery, null);
		
		//Loop through rows and add to list
		if (cursor.moveToFirst()) {
			do {
				Game game = new Game();
				//Set values
				game.setID(Integer.parseInt(cursor.getString(0)));
				game.setMainTeam(cursor.getString(1));
				game.setOpponent(cursor.getString(2));
				//game.setGameDate(Integer.parseInt(cursor.getString(3)));
				//game.setStartTime(Integer.parseInt(cursor.getString(4)));
				
				//Add game to list
				gameList.add(game);				
			} while (cursor.moveToNext());
		}
		
		return gameList;
	}

	//Get all main teams
	public String[] getAllMainTeams() {
		//List<Team> teamNames = new ArrayList<Team>();
		Cursor cursor = getReadableDatabase().rawQuery("SELECT mainTeam FROM " + TABLE_GAME, new String[] {});
			
		String[] teamNames = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
		    String uname = cursor.getString(cursor.getColumnIndex(KEY_GAME_MAIN_TEAM));
		    teamNames[i] = uname;
		    i++;
		}
			
			return teamNames;
	}
		
	//Get all opponents
		public String[] getAllOpponents() {
			//List<Team> teamNames = new ArrayList<Team>();
			Cursor cursor = getReadableDatabase().rawQuery("SELECT opponent FROM " + TABLE_GAME, new String[] {});
			
			String[] teamNames = new String[cursor.getCount()];
			int i = 0;
			while (cursor.moveToNext()){
			    String uname = cursor.getString(cursor.getColumnIndex(KEY_GAME_OPPONENT));
			    teamNames[i] = uname;
			    i++;
			}
				
			return teamNames;
		}		
			
	
		//Get team count
	public int getGameCount() {
		String countQuery = "SELECT * FROM " + TABLE_GAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
			
		return cursor.getCount();
	}
	
		
		//update games
	public int updateGame(Game game) {
		SQLiteDatabase db = this.getWritableDatabase();
			
		ContentValues values = new ContentValues();
		values.put(KEY_GAME_MAIN_TEAM, game.getMainTeam());
		values.put(KEY_GAME_OPPONENT, game.getOpponent());
		//values.put(KEY_GAME_DATE, game.getGameDate());
		//values.put(KEY_GAME_START_TIME, game.getStartTime());
			
		//Update row
		return db.update(TABLE_TEAM, values, KEY_ID + " = ?", new String [] { String.valueOf(game.getID()) });
	}
		
		//Delete game by game name
	public void deleteGame(Game game) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_GAME, KEY_ID + " = ?", new String [] { String.valueOf(game.getID()) });
		db.close();
	}
	
	public int getLastGame() {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_GAME + " ORDER BY " + KEY_ID + " DESC LIMIT 1";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor != null)
			cursor.moveToFirst();
		
		int lastGameID = Integer.parseInt(cursor.getString(0)); 		
		return lastGameID;
	}
			
	
	
//Point functions
	//Add new team
	public void addPoint(Point point) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_POINT_GAME_ID, point.getGameID());
		values.put(KEY_POINT_MAIN_TEAM_SCORE, point.getMainTeamScore());
		values.put(KEY_POINT_OPPONENT_SCORE, point.getOpponentScore());
		values.put(KEY_POINT_STARTING_SIDE, point.getStartingSide());
		values.put(KEY_POINT_WIND_DIRECTION, point.getWindDirection());
		values.put(KEY_POINT_WIND_STRENGTH, point.getWindStrength());
		values.put(KEY_POINT_GUST_STRENGTH, point.getGustStrength());
		values.put(KEY_POINT_TEMPERATURE, point.getTemperatureF());
		values.put(KEY_POINT_WEATHER, point.getWeatherCondition());
		
		db.insert(TABLE_POINT, null, values); //Inserts the values into the table
		db.close();
	}
	
	//Get single point by ID
	public Point getPoint(int point_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_POINT + " WHERE " + KEY_ID + " = " + point_id;
		Log.e(LOG, selectQuery);
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			cursor.moveToFirst();
		//point id, game id, main score, oppo score, starting side, wind dir, wind str
		Point point = new Point(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)),
							    cursor.getString(4), Integer.parseInt(cursor.getString(5)), Double.parseDouble(cursor.getString(6)), Double.parseDouble(cursor.getString(7)),
						    	Double.parseDouble(cursor.getString(8)), cursor.getString(9));
		
		return point;
	}
	
	//Get all points
	public List<Point> getAllPoints() {
		List<Point> pointList = new ArrayList<Point>();
		
		//Select All query
		String selectAllQuery = "SELECT * FROM " + TABLE_POINT;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAllQuery, null);
		
		//Loop through rows and add to list
		if (cursor.moveToFirst()) {
			do {
				Point point = new Point();
				//Set values
				point.setID(Integer.parseInt(cursor.getString(0)));
				point.setGameID(Integer.parseInt(cursor.getString(1)));
				point.setMainTeamScore(Integer.parseInt(cursor.getString(2)));
				point.setOpponentScore(Integer.parseInt(cursor.getString(3)));
				point.setStartingSide(cursor.getString(4));
				point.setWindDirection(Integer.parseInt(cursor.getString(5)));
				point.setWindStrength(Integer.parseInt(cursor.getString(6)));
				point.setGustStrength(Integer.parseInt(cursor.getString(7)));
				point.setTemperatureF(Integer.parseInt(cursor.getString(8)));
				point.setWeatherCondition(cursor.getString(9));

				//Add point to list
				pointList.add(point);				
			} while (cursor.moveToNext());
		}
		return pointList;
	}
	
	//Get point count
	public int getPointCount() {
		String countQuery = "SELECT * FROM " + TABLE_POINT;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		
		return cursor.getCount();
	}

	//Get Last Point ID
	public int getLastPoint() {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_POINT + " ORDER BY " + KEY_ID + " DESC LIMIT 1";
		Log.e(LOG, selectQuery);
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor != null)
			cursor.moveToFirst();
		
		int lastPointID = Integer.parseInt(cursor.getString(0)); 
//		Point point = new Point(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), cursor.getString(4), Integer.parseInt(cursor.getString(5)), cursor.getString(6));
		
		return lastPointID;
	}
	
	//Update single point
	public int updatePoint(Point point) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();

		values.put(KEY_POINT_GAME_ID, point.getGameID());
		values.put(KEY_POINT_MAIN_TEAM_SCORE, point.getMainTeamScore());
		values.put(KEY_POINT_OPPONENT_SCORE, point.getOpponentScore());
		values.put(KEY_POINT_STARTING_SIDE, point.getStartingSide());
		values.put(KEY_POINT_WIND_DIRECTION, point.getWindDirection());
		values.put(KEY_POINT_WIND_STRENGTH, point.getWindStrength());
		values.put(KEY_POINT_GUST_STRENGTH, point.getGustStrength());
		values.put(KEY_POINT_TEMPERATURE, point.getTemperatureF());
		values.put(KEY_POINT_WEATHER, point.getWeatherCondition());

		//Update row
		return db.update(TABLE_POINT, values, KEY_ID + " = ?", new String [] { String.valueOf(point.getID()) });
	}
	
	//Get all points in a game
	public List<Point> getAllPointsInGame(int gameID) {
		List<Point> pointList = new ArrayList<Point>();
		
		//Select All query
		String selectAllQuery = "SELECT * FROM " + TABLE_POINT + " WHERE " + KEY_POINT_GAME_ID + " = " + gameID;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAllQuery, null);
		
		//Loop through rows and add to list
		if (cursor.moveToFirst()) {
			do {
				Point point = new Point();
				//Set values
				point.setID(Integer.parseInt(cursor.getString(0)));
				point.setGameID(Integer.parseInt(cursor.getString(1)));
				point.setMainTeamScore(Integer.parseInt(cursor.getString(2)));
				point.setOpponentScore(Integer.parseInt(cursor.getString(3)));
				point.setStartingSide(cursor.getString(4));
				point.setWindDirection(Integer.parseInt(cursor.getString(5)));
				point.setWindStrength(Double.parseDouble(cursor.getString(6)));
				point.setGustStrength(Double.parseDouble(cursor.getString(7)));
				point.setTemperatureF(Double.parseDouble(cursor.getString(8)));
				point.setWeatherCondition(cursor.getString(9));

				//Add point to list
				pointList.add(point);				
			} while (cursor.moveToNext());
		}
		return pointList;
	}
	
	//Delete point
	public void deletePoint(Point point) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_POINT, KEY_ID + " = ?", new String [] { String.valueOf(point.getID()) });
		db.close();
	}
	
	//Get Last Point ID in a Game
		public int getLastPointInGame(int gameID) {
			SQLiteDatabase db = this.getReadableDatabase();
			String selectQuery = "SELECT * FROM " + TABLE_POINT + " WHERE " + KEY_POINT_GAME_ID + " = '" + gameID + "' ORDER BY " + KEY_ID + " DESC LIMIT 1";
			Log.e(LOG, selectQuery);
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor != null)
				cursor.moveToFirst();
			
			int lastPointID = Integer.parseInt(cursor.getString(0)); //*/
//			Point point = new Point(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), cursor.getString(4), Integer.parseInt(cursor.getString(5)), cursor.getString(6));
			
			return lastPointID;
		}
	
	
	
	
	
	
//Possession functions
	//Add new possession
	public void addPossession(Possession possession) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_POSSESSION_POINT_ID, possession.getPointsID()); //Puts possession name in the values box.  By using multiple values.put, I can insert multiple columns at once
		values.put(KEY_POSSESSION_TYPE, possession.getPossessionType());
		values.put(KEY_POSSESSION_PLAYER1_ID, possession.getPlayer1ID());
		values.put(KEY_POSSESSION_PLAYER2_ID, possession.getPlayer2ID());
		values.put(KEY_POSSESSION_PLAYER3_ID, possession.getPlayer3ID());
		values.put(KEY_POSSESSION_PLAYER4_ID, possession.getPlayer4ID());
		values.put(KEY_POSSESSION_PLAYER5_ID, possession.getPlayer5ID());
		values.put(KEY_POSSESSION_PLAYER6_ID, possession.getPlayer6ID());
		values.put(KEY_POSSESSION_PLAYER7_ID, possession.getPlayer7ID());
		
//		values.put(KEY_POSSESSION_PLAYER_UNKNOWN_ID, possession.getPlayerUnknownID());
		
		db.insert(TABLE_POSSESSION, null, values); //Inserts the values into the table
		db.close();
	}
	
	//Get single possession by ID
	public Possession getPossession(int possession_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_POSSESSION + " WHERE " + KEY_ID + " = " + possession_id;
		
		Log.e(LOG, selectQuery);
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor != null)
			cursor.moveToFirst();
		
		Possession possession = new Possession(Integer.parseInt(cursor.getString(0)),
											   Integer.parseInt(cursor.getString(1)),
											   cursor.getString(2),
											   Integer.parseInt(cursor.getString(3)),
											   Integer.parseInt(cursor.getString(4)),
											   Integer.parseInt(cursor.getString(5)),
											   Integer.parseInt(cursor.getString(6)),
											   Integer.parseInt(cursor.getString(7)),
											   Integer.parseInt(cursor.getString(8)),
											   Integer.parseInt(cursor.getString(9)));
		
		return possession;
	}
	
	//Get all possessions
	public List<Possession> getAllPossessions() {
		List<Possession> possessionList = new ArrayList<Possession>();
		
		//Select All query
		String selectAllQuery = "SELECT * FROM " + TABLE_POSSESSION;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAllQuery, null);
		
		//Loop through rows and add to list
		if (cursor.moveToFirst()) {
			do {
				Possession possession = new Possession();
				//Set values
				possession.setID(Integer.parseInt(cursor.getString(0)));
				possession.setPointsID(Integer.parseInt(cursor.getString(1)));
				possession.setPossessionType(cursor.getString(2));
				possession.setPlayer1ID(Integer.parseInt(cursor.getString(3)));
				possession.setPlayer2ID(Integer.parseInt(cursor.getString(4)));
				possession.setPlayer3ID(Integer.parseInt(cursor.getString(5)));
				possession.setPlayer4ID(Integer.parseInt(cursor.getString(6)));
				possession.setPlayer5ID(Integer.parseInt(cursor.getString(7)));
				possession.setPlayer6ID(Integer.parseInt(cursor.getString(8)));
				possession.setPlayer7ID(Integer.parseInt(cursor.getString(9)));
				

				//Add possession to list
				possessionList.add(possession);				
			} while (cursor.moveToNext());
		}		
		return possessionList;
	}
	
	//Get possession count
	public int getPossessionCount() {
		String countQuery = "SELECT * FROM " + TABLE_POSSESSION;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		
		return cursor.getCount();
	}
	
	//Get all possession IDs in a point
	public String[] getPossessionsInPoint(int pointID) {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT id FROM " + TABLE_POSSESSION + " WHERE " + KEY_POSSESSION_POINT_ID + " = " + pointID, new String[] {});
					
		String[] possessionIDs = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String possID = cursor.getString(cursor.getColumnIndex(KEY_ID));
			possessionIDs[i] = possID;
			i++;
		}	
		return possessionIDs;
	}
	
	//Update single possession
	public int updatePossession(Possession possession) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		//values.put(KEY_TEAM_NAME, possession.getPossessionName());

		values.put(KEY_POSSESSION_POINT_ID, possession.getPointsID());
		values.put(KEY_POSSESSION_TYPE, possession.getPossessionType());
		values.put(KEY_POSSESSION_PLAYER1_ID, possession.getPlayer1ID());
		values.put(KEY_POSSESSION_PLAYER2_ID, possession.getPlayer2ID());
		values.put(KEY_POSSESSION_PLAYER3_ID, possession.getPlayer3ID());
		values.put(KEY_POSSESSION_PLAYER4_ID, possession.getPlayer4ID());
		values.put(KEY_POSSESSION_PLAYER5_ID, possession.getPlayer5ID());
		values.put(KEY_POSSESSION_PLAYER6_ID, possession.getPlayer6ID());
		values.put(KEY_POSSESSION_PLAYER7_ID, possession.getPlayer7ID());
		
		//Update row
		return db.update(TABLE_POSSESSION, values, KEY_ID + " = ?", new String [] { String.valueOf(possession.getID()) });
	}
	
	//Delete possession
	public void deletePossession(Possession possession) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_POSSESSION, KEY_ID + " = ?", new String [] { String.valueOf(possession.getID()) });
		db.close();
	}
	
	public int getLastPossession() {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_POSSESSION + " ORDER BY " + KEY_ID + " DESC LIMIT 1";
		Log.e(LOG, selectQuery);
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor != null)
			cursor.moveToFirst();
		
		int lastPossessionID = Integer.parseInt(cursor.getString(0)); 
//		Point point = new Point(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), cursor.getString(4), Integer.parseInt(cursor.getString(5)), cursor.getString(6));
		
		return lastPossessionID;
	}
	
	public int getLastPossessionInPoint(int pointID) {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_POSSESSION + " WHERE " + KEY_POSSESSION_POINT_ID + " = " + pointID;
//		Cursor cursor = getReadableDatabase().rawQuery("SELECT id FROM " + TABLE_POSSESSION + " WHERE " + KEY_POSSESSION_POINT_ID + " = " + pointID, new String[] {});
		Log.e(LOG, selectQuery);
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null)
			cursor.moveToLast();
		int lastPossessionID = Integer.parseInt(cursor.getString(0)); 
//		Point point = new Point(Integer.parseInt(cursor.getString(0)),Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3)), cursor.getString(4), Integer.parseInt(cursor.getString(5)), cursor.getString(6));
		return lastPossessionID;
	}
	
	//Get all offensive possession IDs in a point
	public List<Integer> getOffPossessionsInPoint(int pointID) {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT id FROM " + TABLE_POSSESSION + " WHERE " + KEY_POSSESSION_POINT_ID + " = " + pointID + " AND " + KEY_POSSESSION_TYPE + " = 'Offense'", new String[] {});
					
		List<Integer> pointIDList = new ArrayList<Integer>();  //String[] possessionIDs = new String[cursor.getCount()];
		//int i = 0;
		while (cursor.moveToNext()){
			int possID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID)));
			pointIDList.add(possID);
			//i++;
		}	
		return pointIDList;
	}
	
	
	
//Action functions
	//Add new action
	public void addAction(Action action) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_ACTION_POSSESSION_ID, action.getPossessionID()); 
		values.put(KEY_ACTION_PLAYER_ID, action.getPlayerID()); 
		values.put(KEY_ACTION_TYPE, action.getActionType()); 
		values.put(KEY_ACTION_X_POSITION, action.getXPosition());
		values.put(KEY_ACTION_Y_POSITION, action.getYPosition());
		values.put(KEY_ACTION_DISTANCE, action.getDistance());
		values.put(KEY_ACTION_ANGLE, action.getAngle());
			
		db.insert(TABLE_ACTION, null, values); //Inserts the values into the table
		db.close();
	}
	
	//Get single action by ID
	public Action getAction(int action_id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_ACTION + " WHERE " + KEY_ID + " = " + action_id;
		Log.e(LOG, selectQuery);
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor != null)
			cursor.moveToFirst();
		
		Action action = new Action(Integer.parseInt(cursor.getString(0)),
								   Integer.parseInt(cursor.getString(1)),
								   Integer.parseInt(cursor.getString(2)),
								   cursor.getString(3),
								   Integer.parseInt(cursor.getString(4)),
								   Integer.parseInt(cursor.getString(5)),
								   Double.parseDouble(cursor.getString(6)),
								   Double.parseDouble(cursor.getString(7)));
				//action id, possessionID, playerID, actionType, xPosition, yPosition)
		
		return action;
	}
	
	//Get all actions
	public List<Action> getAllActions() {
		List<Action> actionList = new ArrayList<Action>();
		
		//Select All query
		String selectAllQuery = "SELECT * FROM " + TABLE_ACTION;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectAllQuery, null);
		
		//Loop through rows and add to list
		if (cursor.moveToFirst()) {
			do {
				Action action = new Action();
				//Set values
				action.setID(Integer.parseInt(cursor.getString(0)));
				action.setPossessionID(Integer.parseInt(cursor.getString(1)));
				action.setPlayerID(Integer.parseInt(cursor.getString(2)));
				action.setActionType(cursor.getString(3));
				action.setXPosition(Integer.parseInt(cursor.getString(4)));
				action.setYPosition(Integer.parseInt(cursor.getString(5)));
				action.setDistance(Double.parseDouble(cursor.getString(6)));
				action.setAngle(Double.parseDouble(cursor.getString(7)));

				//Add possession to list
				actionList.add(action);				
			} while (cursor.moveToNext());
		}		
		return actionList;
	}
	
	//Get action count
	public int getActionCount() {
		String countQuery = "SELECT * FROM " + TABLE_ACTION;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		
		return cursor.getCount();
	}
	
	//Get all action IDs in a possession
	public String[] getActionsInPossession(int possessionID) {
		
		Cursor cursor = getReadableDatabase().rawQuery("SELECT id FROM " + TABLE_ACTION + " WHERE " + KEY_ACTION_POSSESSION_ID + " = " + possessionID, new String[] {});
		String[] actionIDs = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String actionID = cursor.getString(cursor.getColumnIndex(KEY_ID));
			actionIDs[i] = actionID;
			i++;
		}	
		return actionIDs;
	}
	
	//Update single action
	public int updateAction(Action action) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		//values.put(KEY_TEAM_NAME, possession.getPossessionName());

		values.put(KEY_ACTION_POSSESSION_ID, action.getPossessionID());
		values.put(KEY_ACTION_PLAYER_ID, action.getPlayerID());
		values.put(KEY_ACTION_TYPE, action.getActionType());
		values.put(KEY_ACTION_X_POSITION, action.getXPosition());
		values.put(KEY_ACTION_Y_POSITION, action.getYPosition());
		values.put(KEY_ACTION_DISTANCE, action.getDistance());
		values.put(KEY_ACTION_ANGLE, action.getAngle());

		//Update row
		return db.update(TABLE_ACTION, values, KEY_ID + " = ?", new String [] { String.valueOf(action.getID()) });			
	}
	
	//Delete action
	public void deleteAction(Action action) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_ACTION, KEY_ID + " = ?", new String [] { String.valueOf(action.getID()) });
		db.close();
	}	
	
	public int getLastAction() {
		SQLiteDatabase db = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM " + TABLE_ACTION + " ORDER BY " + KEY_ID + " DESC LIMIT 1";
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor != null)
			cursor.moveToFirst();
		
		int lastActionID = Integer.parseInt(cursor.getString(0)); 		
		return lastActionID;
	}
	
	//Get Action IDs, types, players, and Poss ID
	
	//Get all action IDs
	public String[] getAllActionIDs() {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT id FROM " + TABLE_ACTION, new String[] {});
		String[] actionIDs = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String actionID = cursor.getString(cursor.getColumnIndex(KEY_ID));
			actionIDs[i] = actionID;
			i++;
		}	
		return actionIDs;
	}
	
	//Get all action types
	public String[] getAllActionTypes() {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT actionType FROM " + TABLE_ACTION, new String[] {});
		String[] actionTypes = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String actionType = cursor.getString(cursor.getColumnIndex(KEY_ACTION_TYPE));
			actionTypes[i] = actionType;
			i++;
		}	
		return actionTypes;
	}
	
	//Get all action player IDs
	public String[] getAllActionPlayers() {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT playerID FROM " + TABLE_ACTION, new String[] {});
		String[] actionPlayers = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String actionPlayer = cursor.getString(cursor.getColumnIndex(KEY_ACTION_PLAYER_ID));
			actionPlayers[i] = actionPlayer;
			i++;
		}	
		return actionPlayers;
	}
		
	//Get all action possession IDs
	public String[] getAllActionPossessions() {
		Cursor cursor = getReadableDatabase().rawQuery("SELECT possessionID FROM " + TABLE_ACTION, new String[] {});
		String[] actionPossessions = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()){
			String actionPossession = cursor.getString(cursor.getColumnIndex(KEY_ACTION_POSSESSION_ID));
			actionPossessions[i] = actionPossession;
			i++;
		}	
		return actionPossessions;
	}
		
	//Get the turnover action ID in a possession
	public List<Integer> getActionsInDefense(int pointID, String actionType) {
		
		Cursor cursor = getReadableDatabase().rawQuery("SELECT a.possessionID FROM ActionTable a INNER JOIN PossessionTable p ON a.possessionID = p.id WHERE possessionType = 'Defense' AND actionType = '" + actionType + "' AND p.point_id = '" + pointID + "'", new String [] {}); // String.valueOf(possessionID), actionType });
		List<Integer> possessionIDs = new ArrayList<Integer>();
		while (cursor.moveToNext()){
			int possID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ACTION_POSSESSION_ID)));
			possessionIDs.add(possID);
		}
		return possessionIDs;
	}	
	
	public List<Integer> getActionsInOffense(int pointID, String actionType) {
		
		Cursor cursor = getReadableDatabase().rawQuery("SELECT a.possessionID FROM ActionTable a INNER JOIN PossessionTable p ON a.possessionID = p.id WHERE possessionType = 'Offense' AND actionType = '" + actionType + "' AND p.point_id = '" + pointID + "'", new String [] {}); // String.valueOf(possessionID), actionType });
		List<Integer> possessionIDs = new ArrayList<Integer>();
		while (cursor.moveToNext()){
			int possID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ACTION_POSSESSION_ID)));
			possessionIDs.add(possID);
		}
		return possessionIDs;
	}	
	
	public int getCompletions(int possessionID, int minValue, int maxValue) {
		Cursor cursor = getReadableDatabase().rawQuery("select * from ActionTable A inner join ActionTable AA on A.id = AA.id+1 where (A.actionType = 'Goal' or A.actionType = 'Catch') and AA.actionType = 'Throw' and A.possessionID = '"+ possessionID + "' and A.distance >= '" + minValue + "' and A.distance < '" + maxValue + "'", new String [] {}); // String.valueOf(possessionID), actionType });
		int compCount = cursor.getCount();	//String(0));  //HOW IS THIS CURSOR OUT OF BOUNDS???
		return compCount;
	}

	public int getAttempts(int possessionID, int minValue, int maxValue) {
		
		Cursor cursor = getReadableDatabase().rawQuery("select * from ActionTable A inner join ActionTable AA on A.id = AA.id+1 where A.actionType like '%' and AA.actionType like '%hrow%' and A.possessionID = '"+ possessionID + "' and A.distance >= '" + minValue + "' and A.distance < '" + maxValue + "'", new String [] {}); // String.valueOf(possessionID), actionType });
		
		int attCount = cursor.getCount(); //Integer.parseInt(cursor.getString(0));
		return attCount;
	}
	
	public int getCompletionsByDirection(int possessionID, int minValue, int maxValue) {
		Cursor cursor = getReadableDatabase().rawQuery("select * from ActionTable A inner join ActionTable AA on A.id = AA.id+1 where (A.actionType = 'Goal' or A.actionType = 'Catch') and AA.actionType = 'Throw' and A.possessionID = '"+ possessionID + "' and A.angle >= '" + minValue + "' and A.angle < '" + maxValue + "'", new String [] {}); // String.valueOf(possessionID), actionType });
		int compCount = cursor.getCount();	//String(0));  
		return compCount;
	}

	public int getAttemptsByDirection(int possessionID, int minValue, int maxValue) {
		
		Cursor cursor = getReadableDatabase().rawQuery("select * from ActionTable A inner join ActionTable AA on A.id = AA.id+1 where A.actionType like '%' and AA.actionType like '%hrow%' and A.possessionID = '"+ possessionID + "' and A.angle >= '" + minValue + "' and A.angle < '" + maxValue + "'", new String [] {}); // String.valueOf(possessionID), actionType });
		int attCount = cursor.getCount(); //Integer.parseInt(cursor.getString(0));
		return attCount;
	}
	
	
	public void exportPointTable(File file) { //, int gameID) {
		try {
			file.createNewFile();

	        //this.copyFile(dbFile, file);
			SQLiteDatabase db = this.getWritableDatabase();

	        CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

	        //String selectAllQuery = "SELECT * FROM " + TABLE_GAME;
			//Games need 3 getStrings - but I don't actually need this
	        
	        //Points need 7
			String selectAllPointsQuery = "SELECT * FROM " + TABLE_POINT; // + " WHERE " + KEY_POINT_GAME_ID + " = " + gameID;
	        
			Cursor cursorPoint = db.rawQuery(selectAllPointsQuery, null);
	        csvWrite.writeNext(cursorPoint.getColumnNames());

	        while(cursorPoint.moveToNext()) {
	
	            String arrStr[] ={cursorPoint.getString(0),cursorPoint.getString(1), cursorPoint.getString(2), cursorPoint.getString(3), cursorPoint.getString(4), cursorPoint.getString(5), cursorPoint.getString(6)};
    		    csvWrite.writeNext(arrStr);
	        }
	        csvWrite.close();
	        cursorPoint.close();
		} 
        catch (IOException e) {
	            Log.e("mypck", e.getMessage(), e);
        }
	}
	
	public void exportPossessionTable(File file) {
		try {
			file.createNewFile();

	        //this.copyFile(dbFile, file);
			SQLiteDatabase db = this.getWritableDatabase();

	        CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

	        //Possessions need 11
			String selectAllPossessionsQuery = "SELECT * FROM " + TABLE_POSSESSION;
	        
			Cursor cursorPossession = db.rawQuery(selectAllPossessionsQuery, null);
	        csvWrite.writeNext(cursorPossession.getColumnNames());

	        while(cursorPossession.moveToNext()) {
	
	            String arrStr[] ={cursorPossession.getString(0),cursorPossession.getString(1), cursorPossession.getString(2), cursorPossession.getString(3),cursorPossession.getString(4), cursorPossession.getString(5),
	            				  cursorPossession.getString(6),cursorPossession.getString(7), cursorPossession.getString(8), cursorPossession.getString(9),cursorPossession.getString(10)};
    		    csvWrite.writeNext(arrStr);
	        }
	        csvWrite.close();
	        cursorPossession.close();
		} 
        catch (IOException e) {
	            Log.e("mypck", e.getMessage(), e);
        }
	}
	
	public void exportActionTable(File file) {
		try {
			file.createNewFile();
	        //this.copyFile(dbFile, file);
			SQLiteDatabase db = this.getWritableDatabase();
	        CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
	        //String selectAllQuery = "SELECT * FROM " + TABLE_GAME;
			//Games need 3 getStrings - but I don't actually need this
	        
	        //Actions need 6
			String selectAllActionsQuery = "SELECT * FROM " + TABLE_ACTION;
			Cursor cursorAction = db.rawQuery(selectAllActionsQuery, null);
	        csvWrite.writeNext(cursorAction.getColumnNames());

	        while(cursorAction.moveToNext()) {
	
	            String arrStr[] ={cursorAction.getString(0),cursorAction.getString(1), cursorAction.getString(2), cursorAction.getString(3),
	            				  cursorAction.getString(4), cursorAction.getString(5)};
    		    csvWrite.writeNext(arrStr);
	        }
	        csvWrite.close();
	        cursorAction.close();
		} 
        catch (IOException e) {
	            Log.e("mypck", e.getMessage(), e);
        }
	}
	
	public void exportRoster(File file) {
		try {
			file.createNewFile();
	        //this.copyFile(dbFile, file);
			SQLiteDatabase db = this.getWritableDatabase();
	        CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
	        
	        //Players need 6
			String selectAllPlayersQuery = "SELECT * FROM " + TABLE_PLAYER;
			Cursor cursorPlayer = db.rawQuery(selectAllPlayersQuery, null);
	        csvWrite.writeNext(cursorPlayer.getColumnNames());

	        while(cursorPlayer.moveToNext()) {
	
	            String arrStr[] ={cursorPlayer.getString(0),cursorPlayer.getString(1), cursorPlayer.getString(2), cursorPlayer.getString(3),
	            				  cursorPlayer.getString(4), cursorPlayer.getString(5)};
    		    csvWrite.writeNext(arrStr);
	        }
	        csvWrite.close();
	        cursorPlayer.close();
		} 
        catch (IOException e) {
	            Log.e("mypck", e.getMessage(), e);
        }
	}	
}


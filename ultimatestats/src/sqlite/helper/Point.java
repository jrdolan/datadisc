package sqlite.helper;

public class Point {
	//private
	int id;
	int gameID;
	int mainTeamScore;
	int opponentScore;
	String startingSide;  //left or ride side of the field
	int windDirection;
	double windStrength;
	double gustStrength;
	double temperatureF;
	String weatherCondition;
	
	
	//Empty Constructor
	public Point() {
	}
	
	//Complete Constructor 
	public Point(int id, int gameID, int mainTeamScore, int opponentScore, String startingSide, int windDirection, double windStrength, double gustStrength, double temperatureF, String weatherCondition) {
		
		this.id = id;
		this.gameID = gameID;
		this.mainTeamScore = mainTeamScore;
		this.opponentScore = opponentScore;
		this.startingSide = startingSide;
		this.windDirection = windDirection;
		this.windStrength = windStrength;
		this.gustStrength = gustStrength;
		this.temperatureF = temperatureF;
		this.weatherCondition = weatherCondition;
	}
	
	//Constructor without Point ID
		public Point(int gameID, int mainTeamScore, int opponentScore, String startingSide, int windDirection, double windStrength, double gustStrength, double temperatureF, String weatherCondition) {
			this.gameID = gameID;
			this.mainTeamScore = mainTeamScore;
			this.opponentScore = opponentScore;
			this.startingSide = startingSide;
			this.windDirection = windDirection;
			this.windStrength = windStrength;
		}
//Constructor without weather or point id
		public Point(int gameID, int mainTeamScore, int opponentScore, String startingSide){
			this.gameID = gameID;
			this.mainTeamScore = mainTeamScore;
			this.opponentScore = opponentScore;
			this.startingSide = startingSide;
		}
	
	//Get ID
	public int getID(){
		return this.id;
	}
	//Set ID
	public void setID(int id){
		this.id = id;
	}
	
	//Get Game ID
	public int getGameID(){
		return this.gameID;
	}
	//Set Game ID
	public void setGameID(int gameID){
		this.gameID = gameID;
	}
	
	//Get main team score
	public int getMainTeamScore(){
		return this.mainTeamScore;
	}
	//Set main team score
	public void setMainTeamScore(int mainTeamScore){
		this.mainTeamScore = mainTeamScore;
	}
	
	//Get opponent score
	public int getOpponentScore(){
		return this.opponentScore;
	}
	//Set opponent score
	public void setOpponentScore(int opponentScore){
		this.opponentScore = opponentScore;
	}
	
	//Get starting side
	public String getStartingSide() {
		return this.startingSide;
	}
	//Set starting side
	public void setStartingSide(String startingSide) {
		this.startingSide = startingSide;
	}
	
	//Get wind direction
	public int getWindDirection() {
		return this.windDirection;
	}
	//Set wind direction
	public void setWindDirection(int windDirection) {
		this.windDirection = windDirection;
	}
	
	//Get wind strength
	public double getWindStrength() {
		return this.windStrength;
	}
	//Set wind strength
	public void setWindStrength(double windStrength) {
		this.windStrength = windStrength;
	}
	
	//Get gust strength
	public double getGustStrength() {
		return this.gustStrength;
	}
	//Set gust strength
	public void setGustStrength(double gustStrength) {
		this.gustStrength = gustStrength;
	}
	
	//Get temperature
	public double getTemperatureF() {
		return this.temperatureF;
	}
	//Set temperature
	public void setTemperatureF(double temperatureF) {
		this.temperatureF = temperatureF;
	}
	
	//Get weather conditions
	public String getWeatherCondition() {
		return this.weatherCondition;
	}
	//Set weatherCondition
	public void setWeatherCondition(String weatherCondition) {
		this.weatherCondition = weatherCondition;
	}
}
package sqlite.helper;

public class Game {
	//private
	int id;
	String mainTeam;
	String opponent;
	//int gameDate;
	//int startTime;
	
	//Empty Constructor
	public Game() {
	}
	
	//Constructor with ID 
	public Game(int id, String mainTeam, String opponent) { //, int date, int startTime) {
		this.id = id;
		this.mainTeam = mainTeam;
		this.opponent = opponent;
		//this.gameDate = date;
		//this.startTime = startTime;
	}
	
	//Constructor without ID
	public Game(String mainTeam, String opponent) { //, int date, int startTime) {
		this.mainTeam = mainTeam;
		this.opponent = opponent;
		//this.gameDate = date;
		//this.startTime = startTime;
	}
		
	//Get ID
	public int getID(){
		return this.id;
	}
	//Set ID
	public void setID(int id){
		this.id = id;
	}
	
	//Get First Team
	public String getMainTeam(){
		return this.mainTeam;
	}
	//Set First Team
	public void setMainTeam(String mainTeam){
		this.mainTeam = mainTeam;
	}
	
	//Get Second Team
	public String getOpponent(){
		return this.opponent;
	}
	//Set Opponent
	public void setOpponent(String opponent){
		this.opponent = opponent;
	}
	
/*	
    //Get date
		public int getGameDate(){
			return this.gameDate;
		}
	//Set date
	public void setGameDate(int gameDate){
		this.gameDate = gameDate;
	}
	
	//Get game time
	public int getStartTime(){
		return this.startTime;
	}
	//Set game time
	public void setStartTime(int startTime){
		this.startTime = startTime;
	}
*/
	
}
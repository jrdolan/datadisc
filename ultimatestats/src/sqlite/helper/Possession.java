package sqlite.helper;

public class Possession {
	//private
	int id;
	int pointsID;
	String possessionType; //Offense or Defense	
	int player1ID;
	int player2ID;
	int player3ID;
	int player4ID;
	int player5ID;
	int player6ID;
	int player7ID;
	
	int playerUnknownID;
	//Not sure if this is needed.  Maybe I should automatically create an UnknownPlayer every time I create a team?
	//I don't think it is, but I'm keeping it anyway to be safe.
	//What I need to do is initialize every player when creating a lineup to the UnknownPlayerID
	//That way it just defaults to unknown until prescribed
	String[] lineUpNickNames = new String[8];
	
	//Empty Constructor
	public Possession() {
	}
	//Complete Constructor 
	public Possession(int id, int pointsID, String possessionType, int player1ID, int player2ID, int player3ID, int player4ID, int player5ID, int player6ID, int player7ID){
		this.id = id;
		this.pointsID = pointsID;
		this.possessionType = possessionType;
		this.player1ID = player1ID;
		this.player2ID = player2ID;
		this.player3ID = player3ID;
		this.player4ID = player4ID;
		this.player5ID = player5ID;
		this.player6ID = player6ID;
		this.player7ID = player7ID;
	}
	//Constructor minus id
		public Possession(int pointsID, String possessionType, int player1ID, int player2ID, int player3ID, int player4ID, int player5ID, int player6ID, int player7ID){
			this.pointsID = pointsID;
			this.possessionType = possessionType;
			this.player1ID = player1ID;
			this.player2ID = player2ID;
			this.player3ID = player3ID;
			this.player4ID = player4ID;
			this.player5ID = player5ID;
			this.player6ID = player6ID;
			this.player7ID = player7ID;
		}
		
	//Get ID
	public int getID(){
		return this.id;
	}
	//Set ID
	public void setID(int id){
		this.id = id;
	}
	
	//Get points ID
	public int getPointsID() {
		return this.pointsID;
	}
	//Set points ID
	public void setPointsID(int pointsID){
		this.pointsID = pointsID;
	}
	
	//Get possession Type
	public String getPossessionType() {
		return this.possessionType;
	}
	//Set possession Type
	public void setPossessionType(String possessionType) {
		this.possessionType = possessionType;
	}
	
	//Get player1 ID
	public int getPlayer1ID() {
		return this.player1ID;
	}
	//Set player1 ID
	public void setPlayer1ID(int player1ID) {
		this.player1ID = player1ID;
	}
	
	//Get player2 ID
	public int getPlayer2ID() {
		return this.player2ID;
	}
	//Set player2 ID
	public void setPlayer2ID(int player2ID) {
		this.player2ID = player2ID;
	}
	
	//Get player3 ID
	public int getPlayer3ID() {
		return this.player3ID;
	}
	//Set player3 ID
	public void setPlayer3ID(int player3ID) {
		this.player3ID = player3ID;
	}
	
	//Get player4 ID
	public int getPlayer4ID() {
		return this.player4ID;
	}
	//Set player4 ID
	public void setPlayer4ID(int player4ID) {
		this.player4ID = player4ID;
	}
	
	//Get player5 ID
	public int getPlayer5ID() {
		return this.player5ID;
	}
	//Set player5 ID
	public void setPlayer5ID(int player5ID) {
		this.player5ID = player5ID;
	}
	
	//Get player6 ID
	public int getPlayer6ID() {
		return this.player6ID;
	}
	//Set player6 ID
	public void setPlayer6ID(int player6ID) {
		this.player6ID = player6ID;
	}
	
	//Get player7 ID
	public int getPlayer7ID() {
		return this.player7ID;
	}
	//Set player7 ID
	public void setPlayer7ID(int player7ID) {
		this.player7ID = player7ID;
	}
	
	//Get unknown player ID
	public int getPlayerUnknownID() {
		return this.playerUnknownID;
	}
	//Set playerUnknown ID
	public void setPlayerUnknownID(int playerUnknownID) {
		this.playerUnknownID = playerUnknownID;
	}
	
	
	//Get LineUp NickNames
	public String[] getLineUpNickNames() {
		return this.lineUpNickNames;
	}
	
	//Set LineUp
	public void setLineUpNickNames(String[] lineUpNickNames) {
		for (int i = 0; i < lineUpNickNames.length; i++) {
			this.lineUpNickNames[i] = lineUpNickNames[i];
		}
	}
	
}
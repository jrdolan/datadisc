package sqlite.helper;

public class Action {
	//private
	int id;
	int possessionID;
	int playerID;
	String actionType;
	int xPosition;
	int yPosition;
	double distance;
	double angle;
	
	//Empty Constructor
	public Action() {
	}
	//Complete constructor
	public Action(int id, int possessionID, int playerID, String actionType, int xPosition, int yPosition, double distance, double angle) {
		this.id = id;
		this.possessionID = possessionID;
		this.playerID = playerID;
		this.actionType = actionType;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.distance = distance;
		this.angle = angle;
	}
	//Constructor without distance or angle
	public Action(int id, int possessionID, int playerID, String actionType, int xPosition, int yPosition) {
		this.id = id;
		this.possessionID = possessionID;
		this.playerID = playerID;
		this.actionType = actionType;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	//Constructor without id but with distance and angle
	public Action(int possessionID, int playerID, String actionType, int xPosition, int yPosition, double distance, double angle) {
		this.possessionID = possessionID;
		this.playerID = playerID;
		this.actionType = actionType;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.distance = distance;
		this.angle = angle;
	}
	//Constructor without id or distance or angle
	public Action(int possessionID, int playerID, String actionType, int xPosition, int yPosition) {
		this.possessionID = possessionID;
		this.playerID = playerID;
		this.actionType = actionType;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}
	//Constructor without position (for things like halftime and game over?)
	public Action(int id, int possessionID, int playerID, String actionType) {
		this.id = id;
		this.possessionID = possessionID;
		this.playerID = playerID;
		this.actionType = actionType;
	}

	
	//Get ID
	public int getID(){
		return this.id;
	}
	//Set ID
	public void setID(int id){
		this.id = id;
	}
	
	//Get Possession ID
	public int getPossessionID(){
		return this.possessionID;
	}
	//Set Possession ID
	public void setPossessionID(int possessionID){
		this.possessionID = possessionID;
	}
	
	//Get Player ID
	public int getPlayerID(){
		return this.playerID;
	}
	//Set Player ID
	public void setPlayerID(int playerID){
		this.playerID = playerID;
	}
	
	//Get Action Type
	public String getActionType() {
		return this.actionType;
	}
	//Set Action Type
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	//Get X Position
	public int getXPosition() {
		return this.xPosition;
	}
	//Set X Position
	public void setXPosition(int xPosition) {
		this.xPosition = xPosition;
	}
	
	//Get Y Position
	public int getYPosition() {
		return this.yPosition;
	}
	//Set Y Position
	public void setYPosition(int yPosition) {
		this.yPosition = yPosition;
	}
	
	//Get Distance
	public double getDistance() {
		return this.distance;
	}
	//Set Distance
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	//Get Angle
	public double getAngle() {
		return this.angle;
	}
	//Set Distance
	public void setAngle(double angle) {
		this.angle = angle;
	}

}
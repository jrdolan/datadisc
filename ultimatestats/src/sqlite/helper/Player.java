package sqlite.helper;

public class Player {
	//private
	int id;
	String playerFirstName;
	String playerLastName;
	int playerNumber;
	String playerNickName;
	int teamId;
	
	//Empty Constructor
	public Player() {
	}
	//Complete Constructor 
	public Player(int id, String firstName, String lastName, int number, String nickName, int team_id){
		this.id = id;
		this.playerFirstName = firstName;
		this.playerLastName = lastName;
		this.playerNumber = number;
		this.playerNickName = nickName;
		this.teamId = team_id;
	}
	
	//Constructor with just Name, Number, and team id
	public Player(String firstName, String lastName, int number, String nickName, int team_id){
		this.playerFirstName = firstName;
		this.playerLastName = lastName;
		this.playerNumber = number;
		this.playerNickName = nickName;
		this.teamId = team_id;
	}
	
	//Constructor without number
	public Player(String firstName, String lastName, String nickName, int team_id){
		this.playerFirstName = firstName;
		this.playerLastName = lastName;
		this.playerNickName = nickName;
		this.teamId = team_id;
	}
		
	//Constructor with just NickName and Team Id
	public Player(String nickName, int team_id){
		this.playerNickName = nickName;
		this.teamId = team_id;
	}
	
	//Get ID
	public int getID(){
		return this.id;
	}
	//Set ID
	public void setID(int id){
		this.id = id;
	}
	
	//Get Player First Name
	public String getPlayerFirstName(){
		return this.playerFirstName;
	}
	//Set Player First Name
	public void setPlayerFirstName(String firstName){
		this.playerFirstName = firstName;
	}
	
	//Get Player Last Name
	public String getPlayerLastName(){
		return this.playerLastName;
	}
	//Set Player Last Name
	public void setPlayerLastName(String lastName){
		this.playerLastName = lastName;
	}
	
	//Get Number
		public int getPlayerNumber(){
			return this.playerNumber;
		}
	//Set Number
	public void setPlayerNumber(int number){
		this.playerNumber = number;
	}
	
	//Get Player Nick Name
		public String getPlayerNickName(){
			return this.playerNickName;
		}
		//Set Player Last Name
		public void setPlayerNickName(String nickName){
			this.playerNickName = nickName;
		}
	
	//Get TeamId
	public int getTeamId(){
		return this.teamId;
	}
	//Set Number
	public void setTeamId(int team_id){
		this.teamId = team_id;
	}
}
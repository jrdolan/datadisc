package sqlite.helper;

public class Team {
	//private
	int id;
	String teamName;
	
	//Empty Constructor
	public Team() {
	}
	//Constructor with ID and Team Name
	public Team(int id, String name){
		this.id = id;
		this.teamName = name;
	}
	//Constructor with just Team Name
	public Team(String name){
		this.teamName = name;
	}
	
	//Get ID
	public int getID(){
		return this.id;
	}
	//Set ID
	public void setID(int id){
		this.id = id;
	}
	
	//Get Team Name
	public String getTeamName(){
		return this.teamName;
	}
	//Set Team Name
	public void setTeamName(String name){
		this.teamName = name;
	}
	
}
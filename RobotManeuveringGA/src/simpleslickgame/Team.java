package simpleslickgame;

public class Team {
	Bot bots[];
	String team;
	String DNA;
	
	public Team(){
		setTeam("red");
		bots = new Bot[5];
		bots[0] = new Bot(25,25,10, -45, getTeam());
		bots[1] = new Bot(75,25,10, 0, getTeam());
		bots[2] = new Bot(50,50,10, -45, getTeam());
		bots[3] = new Bot(25,75,10, -90,  getTeam());
		bots[4] = new Bot(75,75,10, -45, getTeam());
		
	}
	public Team(String s, int windowWidth, int windowHeight){
		setTeam(s);
		bots = new Bot[5];
		bots[0] = new Bot(windowWidth-25,windowHeight-25,10, 135, getTeam());
		bots[1] = new Bot(windowWidth-75,windowHeight-25,10, 180, getTeam());
		bots[2] = new Bot(windowWidth-50,windowHeight-50,10, 135, getTeam());
		bots[3] = new Bot(windowWidth-25,windowHeight-75,10, 90, getTeam());
		bots[4] = new Bot(windowWidth-75,windowHeight-75,10, 135, getTeam());
	}
	
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public Bot[] getBots() {
		return bots;
	}
	public void setBots(Bot[] bots) {
		this.bots = bots;
	}
	public String getDNA() {
		return DNA;
	}
	public void setDNA(String dNA) {
		DNA = dNA;
	}
	public void reduceCooldown() {
		for(int i = 0; i<getBots().length; i++){
			getBots()[i].reduceCooldown();
		}
	}
	public void setSights() {
		for(int i=0; i<getBots().length; i++){
			getBots()[i].setSights();
		}
	}

	
}

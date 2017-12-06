package game;

public class Player {
	Body body;
	float x, y;
	int health;
	String playerAnimFile = "player.txt";
	
	
	public Player() {
		body = new Body(4, 2, playerAnimFile);
	}
	
	public void draw() {
		body.draw();
	}
	
	public int getHealth() {
		return health;
	}
	
	public void gotHurt(int damageAmnt) {
		if(health>0)
			health-=damageAmnt;
		else
			die();
	}
	
	public void die() {
		
	}

}

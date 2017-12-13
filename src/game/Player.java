package game;

public class Player {
	Body body;
	float x, y;
	int health = 5;
	String playerAnimFile = "player.txt";
	
	
	public Player() {
		body = new Body(playerAnimFile, 4, 2);
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
	
	public boolean isAlive() {
		return health>0;
	}
	
	public void move(char k) {
		switch(k) {
			case 'w':
				body.jump();
				break;
			case 'a':
				body.walk();
				break;
			case 'd':
				body.walk();
				break;
		}
	}
	
	public void stop(char k) {
		switch(k) {
			case 'w':
				body.idle();
				break;
			case 'a':
				body.idle();
				break;
			case 'd':
				body.idle();
				break;
		}
	}
	
	public void update(float dt) {
		body.update(dt);
	}

}

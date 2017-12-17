package game;

import java.util.ArrayList;

import processing.core.PApplet;

public class Player extends GraphicObject{
	Body body;
	float x, y;
	int health = 5;
	private float moveIncrement = 0.05f;
	String playerAnimFile = "player.txt";
	Hitbox hitbox;
	float torsoSize = 8f;

	float hitboxAngle = PApplet.PI/4;
	
	public Player() {
		body = new Body(playerAnimFile, 4, 2, torsoSize);
		float xMin = x-(torsoSize/2);
		float xMax = x+(torsoSize/2);
		float yMin = y-(torsoSize/2)-6.5f;
		float yMax = y+(torsoSize/2);
		hitbox = new Hitbox(xMin, yMin, xMax, yMax, 0);
	}
	
	public void draw() {
		app_.pushMatrix();
		app_.translate(x, y);
		body.draw();
		app_.popMatrix();
		hitbox.draw();
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
				moveUp();
				break;
			case 's':
				moveDown();
				break;
			case 'a':
				body.walk();
				moveLeft();
				break;
			case 'd':
				body.walk();
				moveRight();
				break;
		}
	}
	
	public void move(ArrayList<Character> keys) {
		for(Character c: keys) {
			move(c);
		}
	}
	
	public void stop() {
		body.idle();
	}
	
	public void fall() {
		y-=0.05f;
		hitbox.incY(-0.05f);
	}
	
	public void moveUp() {
		y+=moveIncrement;
		hitbox.incY(moveIncrement);
	}
	
	public void moveDown() {
		y-=moveIncrement;
		hitbox.incY(-moveIncrement);
	}
	
	public void moveLeft() {
		x-=moveIncrement;
		hitbox.incX(-moveIncrement);
	}
	public void moveRight() {
		x+=moveIncrement;
		hitbox.incX(moveIncrement);
	}
	
	public void update(float dt) {
		body.update(dt);
	}
	
	public boolean isColliding(Hitbox other) {
		return hitbox.isColliding(other);
	}
	
	public Hitbox getHitbox() {
		return hitbox;
	}

}

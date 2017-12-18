package game;

public abstract class Entity extends GraphicObject{
	Body body;
	float x, y;
	int hitDamage;
	Hitbox hitbox;
	int health;
	private float moveIncrement = 0.05f;
	public Entity(String animFile, int numLimbs, int numJoints, float torsoSize, int health_) {
		body = new Body(animFile, numLimbs, numJoints, torsoSize);
		health = health_;
		float xMin = x-(torsoSize/2);
		float xMax = x+(torsoSize/2);
		float yMin = y-(torsoSize/2)-(7*torsoSize/8);//was 6.5f for player
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
		if(health>1) {
			health-=damageAmnt;
			body.hurt();
		}else if(health>0) {
			health-=damageAmnt;
		}
	}
	
	public boolean isAlive() {
		return health>0;
	}
	
	public boolean isColliding(Hitbox other) {
		return hitbox.isColliding(other);
	}
	
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	public void update(float dt) {
		body.update(dt);
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
	
	public void walkLeft() {
		body.walk();
		moveLeft();
	}
	
	public void walkRight() {
		body.walk();
		moveRight();
	}
}

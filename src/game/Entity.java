package game;

import processing.core.PApplet;

public abstract class Entity extends GraphicObject{
	float[] nextPos;
	Body body;
	int hitDamage;
	Hitbox hitbox;
	int health;
	private boolean isOnGround;
	private float vy;
	private float moveSpeed;
	public Entity(String animFile, int numLimbs, int numJoints, float torsoSize, int health_, float moveSpeed_) {
		super(0, 0, 0);
		body = new Body(animFile, numLimbs, numJoints, torsoSize);
		moveSpeed = moveSpeed_;
		nextPos = new float[2];
		nextPos[0] = getX();
		nextPos[1] = getY();
		isOnGround = false;
		vy = 0;
		health = health_;
		float xMin = -(torsoSize/2);
		float xMax = (torsoSize/2);
		float yMin = -(11*torsoSize/8);
		float yMax = (torsoSize/2);
		hitbox = new Hitbox(xMin, yMin, xMax, yMax, 0);
	}
	
	public Entity(float x, float y, String animFile, int numLimbs, int numJoints, float torsoSize, int health_, float moveSpeed_) {
		super(x, y, 0);
		body = new Body(animFile, numLimbs, numJoints, torsoSize);
		moveSpeed = moveSpeed_;
		nextPos = new float[2];
		nextPos[0] = getX();
		nextPos[1] = getY();
		health = health_;
		float xMin = -(torsoSize/2);
		float xMax = (torsoSize/2);
		float yMin = -(11*torsoSize/8);
		float yMax = (torsoSize/2);
		hitbox = new Hitbox(xMin, yMin, xMax, yMax, 0);
	}
	
	public void draw() {
		
		app_.pushMatrix();
		app_.translate(getX(), getY());
		body.draw();
		hitbox.draw();
		app_.popMatrix();
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
	
	public boolean isColliding(Entity other) {
		if(		//is other hitbox's bottom left corner inside?
				isInside(other.getXMin(), other.getYMin()) ||
				//is other hitbox's bottom right corner inside?
				isInside(other.getXMin(), other.getYMax()) ||
				//is other hitbox's top left corner inside?
				isInside(other.getXMax(), other.getYMin()) ||
				//is other hitbox's top right corner inside?
				isInside(other.getXMax(), other.getYMax())) {
			return true;
		}else return false;
	}
	
	public boolean isInside(float x, float y)
	{	
		return hitbox.isInside(x - getX(), y - getY());
	}
	
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	public void setGrounded(boolean g) {
		isOnGround = g;
	}
	
	public void doGravity(float dt) {
		if (!isOnGround) {
			vy -= G * dt;
		}
		nextPos[1] += vy * dt;
	}
	
	public void update(float dt) {
		body.update(dt);
	}
	
	public void stop() {
		body.idle();
	}
	
	public void fall() {
		nextPos[1] -= 0.05f;
	}
	
	public void moveUp(float dt) {
		nextPos[1] += moveSpeed * dt;
	}
	
	public void moveDown(float dt) {
		nextPos[1] -= moveSpeed * dt;
	}
	
	public void moveLeft(float dt) {
		nextPos[0] -= moveSpeed * dt;
	}
	public void moveRight(float dt) {
		nextPos[0] += moveSpeed * dt;
		System.out.println(nextPos[0]);
	}
	
	public void moveBy(float[] diff) {
		nextPos[0] += diff[0];
		nextPos[1] += diff[1];
	}
	
	public void moveBy(float x, float y) {
		nextPos[0] += x;
		nextPos[1] += y;
	}
	
	public void doMove() {
		setX(nextPos[0]);
		setY(nextPos[1]);
	}
	
	public float getXMin() {
		return getX() + hitbox.getXMin();
	}
	
	public float getYMin() {
		return getY() + hitbox.getYMin();
	}
	
	public float getXMax() {
		return getX() + hitbox.getXMax();
	}
	
	public float getYMax() {
		return getY() + hitbox.getYMax();
	}
	
	public float nextXMin() {
		return nextPos[0] + hitbox.getXMin();
	}
	
	public float nextYMin() {
		return nextPos[1] + hitbox.getYMin();
	}
	
	public float nextXMax() {
		return nextPos[0] + hitbox.getXMax();
	}
	
	public float nextYMax() {
		return nextPos[1] + hitbox.getYMax();
	}
	
	public float getW() {
		return hitbox.width;
	}
	
	public float getH() {
		return hitbox.height;
	}

	public float getMoveSpd() {
		return moveSpeed;
	}
}

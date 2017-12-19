package game;

/**
 * Deals with the physics, body (which in turn deals with animation), and world position of a player or an enemy in the game.
 * @author Steph and Thomas
 *
 */
public abstract class Entity extends GraphicObject{
	private float[] nextPos;
	protected Body body;
	protected Hitbox hitbox;
	private int health;
	private boolean isOnGround;
	private float vy;
	private float moveSpeed;
	
	/**
	 * 	sets up an entity at 0, 0 in world coordinates.
	 * @param animFile animation file for this entity
	 * @param numLimbs number of limbs for the body
	 * @param numJoints number of joints per limb for the body
	 * @param torsoSize diameter of the torso of the body
	 * @param health_ max/ starting health
	 * @param moveSpeed_ how much to move every time this entity is told to move
	 */
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
		
		//create the hitbox around the torso and lower limbs
		float xMin = -(torsoSize/2);
		float xMax = (torsoSize/2);
		float yMin = -(11*torsoSize/8);
		float yMax = (torsoSize/2);
		hitbox = new Hitbox(xMin, yMin, xMax, yMax);
	}
	
	/**
	 * sets up an entity at x,y in world coordinates
	 * @param x x coord of where to spawn  
	 * @param y y coord of where to spawn
	 * @param animFile animation file for this entity
	 * @param numLimbs number of limbs for the body
	 * @param numJoints number of joints per limb for the body
	 * @param torsoSize diameter of the torso of the body
	 * @param health_ max/ starting health
	 * @param moveSpeed_ how much to move every time this entity is told to move
	 */
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
		hitbox = new Hitbox(xMin, yMin, xMax, yMax);
	}
	
	/**
	 * Draw the entity.
	 */
	public void draw() {
		app_.pushMatrix();
		app_.translate(getX(), getY());
		body.draw();
		hitbox.draw();
		app_.popMatrix();
	}
	
	/**
	 * return whatever the entity currently has for health
	 * @return
	 */
	public int getHealth() {
		return health;
	}
	
	/**
	 * Play the hurt animation (if the body attached to this entity has one) and reduce health by the specified amount (unless health
	 * already 0).
	 * @param damageAmnt
	 */
	public void gotHurt(int damageAmnt) {
		if(health>1) {
			health-=damageAmnt;
			body.hurt();
		}else if(health>0) {
			health-=damageAmnt;
		}
	}
	
	/**
	 * this entity is only alive as long as it's health is above 0.
	 * @return
	 */
	public boolean isAlive() {
		return health>0;
	}
	
	/**
	 * two hitboxes are only colliding if a corner from both is touching the other.
	 * @param other the other hitbox to check with.
	 * @return true if collding, false if not.
	 */
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
	
	/**
	 * is the given point (in world coordinates) inside of the hitbox?
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isInside(float x, float y)
	{	
		return hitbox.isInside(x - getX(), y - getY());
	}
	
	/**
	 * 
	 * @return the hitbox
	 */
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	/**	Get whether the entity is touching the ground or not
	 * 
	 * @return
	 */
	public boolean checkGrounded() {
		return isOnGround;
	}
	
	/**
	 * tell this entity whether or not it's on the ground (based on collisions and physics).
	 * @param g
	 */
	public void setGrounded(boolean g) {
		isOnGround = g;
	}
	
	/**
	 * 	make gravity take effect, and move downward based on how much time has passed since the last update,
	 *  while the entity is not grounded
	 * @param dt
	 */
	public void doGravity(float dt) {
		if (!isOnGround) {
			vy -= G * dt;
		}
		// Only set the y velocity to 0 for grounded entities if it is negative
		else if (vy < 0) {
			vy = 0;
		}
		nextPos[1] += vy * dt;
	}
	
	/**	Sets the y velocity to the given value
	 * 
	 * @param vy_	The new y velocity
	 */
	public void setVY(float vy_) {
		vy = vy_;
	}
	
	/**
	 * update the animation based on how much time has passed since last update.
	 * @param dt
	 */
	public void update(float dt) {
		body.update(dt);
	}
	
	/**
	 * stop playing other animations and go back to idle
	 */
	public void stop() {
		body.idle();
	}
	
	/**
	 * play the walk animation and move to the left
	 * @param dt
	 */
	public void walkLeft(float dt) {
		body.walk();
		moveLeft(dt);
	}
	
	/**
	 * play the walk animation and move to the right
	 * @param dt
	 */
	public void walkRight(float dt) {
		body.walk();
		moveRight(dt);
	}
	
	/**
	 * play the death animation
	 */
	public void die() {
		body.die();
	}
	
	/**
	 * move up at a speed that makes sense with the change in time
	 * @param dt change in time
	 */
	public void moveUp(float dt) {
		nextPos[1] += moveSpeed * dt;
	}
	
	/**
	 * move down at a speed that makes sense with the change in time
	 * @param dt change in time
	 */
	public void moveDown(float dt) {
		nextPos[1] -= moveSpeed * dt;
	}
	
	/**
	 * move left at a speed that makes sense with the change in time
	 * @param dt change in time
	 */
	public void moveLeft(float dt) {
		nextPos[0] -= moveSpeed * dt;
	}
	
	/**
	 * move right at a speed that makes sense with the change in time
	 * @param dt change in time
	 */
	public void moveRight(float dt) {
		nextPos[0] += moveSpeed * dt;
	}
	
	/**
	 * move over by the amount specified
	 * @param diff
	 */
	public void moveBy(float[] diff) {
		nextPos[0] += diff[0];
		nextPos[1] += diff[1];
	}
	
	/**
	 * move over by the amount specified
	 * @param x move over by this much in the x
	 * @param y move over by this much in the y
	 */
	public void moveBy(float x, float y) {
		nextPos[0] += x;
		nextPos[1] += y;
	}
	
	/**
	 * move over according to what the next move has been calculated as.
	 */
	public void doMove() {
		setX(nextPos[0]);
		setY(nextPos[1]);
	}
	
	/**
	 * @return the xMin of the hitbox in world coordinates
	 */
	public float getXMin() {
		return getX() + hitbox.getXMin();
	}
	
	/**
	 * @return the yMin of the hitbox in world coordinates
	 */
	public float getYMin() {
		return getY() + hitbox.getYMin();
	}
	
	/**
	 * @return the xMax of the hitbox in world coordinates
	 */
	public float getXMax() {
		return getX() + hitbox.getXMax();
	}
	
	/**
	 * @return the yMax of the hitbox in world coordinates
	 */
	public float getYMax() {
		return getY() + hitbox.getYMax();
	}
	
	/**
	 * @return where xMin of the hitbox would be next frame, regardless of collisions, in world coordinates
	 */
	public float nextXMin() {
		return nextPos[0] + hitbox.getXMin();
	}
	
	/**
	 * @return where yMin of the hitbox would be next frame, regardless of collisions, in world coordinates
	 */
	public float nextYMin() {
		return nextPos[1] + hitbox.getYMin();
	}
	
	/**
	 * @return where xMax of the hitbox would be next frame, regardless of collisions, in world coordinates
	 */
	public float nextXMax() {
		return nextPos[0] + hitbox.getXMax();
	}
	
	/**
	 * @return where yMax of the hitbox would be next frame, regardless of collisions, in world coordinates
	 */
	public float nextYMax() {
		return nextPos[1] + hitbox.getYMax();
	}
	
	/**
	 * 
	 * @return width of the hitbox;
	 */
	public float getW() {
		return hitbox.getW();
	}
	
	/**
	 * 
	 * @return height of the hitbox
	 */
	public float getH() {
		return hitbox.getH();
	}

	/**
	 * 
	 * @return moveSpeed
	 */
	public float getMoveSpd() {
		return moveSpeed;
	}
}

package game;

/**
 * General class for all the enemies in the game.
 * @author Steph and Thomas
 *
 */
public class Enemy extends Entity{
	private int hitDamage;
	
	/**
	 * 
	 * @param animFile animation file
	 * @param numLimbs number of limbs
	 * @param numJoints number of joints per limb
	 * @param torsoSize diameter of the torso
	 * @param health max/ starting health
	 * @param hitDmg the damage to do to the player everytime this enemy hits them
	 * @param moveSpeed modifier for how fast this enemy moves
	 */
	public Enemy(String animFile, int numLimbs, int numJoints, float torsoSize, int health, int hitDmg, float moveSpeed) {
		super(animFile, numLimbs, numJoints, torsoSize, health, moveSpeed);
		hitDamage = hitDmg;
	}
	
	/**
	 * draw the enemy
	 */
	public void draw() {
		super.draw();
	}
	
	/**
	 * if the player is colliding with this enemy's hitbox, and the player is on top, take health away from this enemy,
	 * otherwise, damage the player.
	 * @param player
	 * @return
	 */
	public boolean hitPlayer(Player player) {
		if(hitbox.isColliding(player.getHitbox())) {
			if(hitbox.hitTop(player.getHitbox())) {
				//damage this enemy
				gotHurt(1);
				return false;
			}else{
				//damage player
				player.gotHurt(hitDamage);
				return true;
			}
		}else {
			return false;
		}
	}
	
	/**
	 * check if the player is viewable by this enemy (are there any obstacles in the way, is the player at the right
	 * y level)
	 * @param player
	 * @param level the level this enemy is on
	 * @return
	 */
	public boolean seePlayer(Player player, Level level) {
		
		// Is this enemy above or at the same y level as the player
		if(getY()>= player.getY()) {
			
			// start going through all the platforms in the level to see if there are any obstacles in the way.
			Platform[] platforms = level.getPlatforms();
			for(Platform p: platforms) {
				// if a platform is in between the player and the enemy, can't see the player.
				return !(getY() >= p.getY() && (player.getX()<p.getX() && getX()>p.getX())||
						(player.getX()>p.getX() && getX()<p.getX()));
			}
			//if it gets through all the platforms without any platform in between the player and the enemy, can see the player
			return true;
		}else return true;
	}
	
	/**
	 * move toward the player if this enemy can see the player
	 * @param player
	 * @param dt
	 */
	public void moveToward(Player player, float dt) {
		if(player.getX()>getX()) {
			moveLeft(dt);
		}else if(player.getX()<getX()) {
			moveRight(dt);
		}else hitPlayer(player);
	}
}

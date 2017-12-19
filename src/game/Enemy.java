package game;

public class Enemy extends Entity{
	int hitDamage;
	public Enemy(String animFile, int numLimbs, int numJoints, float torsoSize, int health, int hitDmg, float moveSpeed) {
		super(animFile, numLimbs, numJoints, torsoSize, health, moveSpeed);
		hitDamage = hitDmg;
	}
	
	public void draw() {
		super.draw();
	}
	
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
	
	public boolean seePlayer(Player player, Level level) {
		
		if(getY()>= player.getY()) {
			Platform[] platforms = level.getPlatforms();
			for(Platform p: platforms) {
				return !(getY() >= p.getY() && (player.getX()<p.getX() && getX()>p.getX())||
						(player.getX()>p.getX() && getX()<p.getX()));
			}
			return true;
		}else return true;
	}
	
	public void moveToward(Player player, float dt) {
		if(player.getX()>getX()) {
			moveLeft(dt);
		}else if(player.getX()<getX()) {
			moveRight(dt);
		}else hitPlayer(player);
	}
}

package game;

public class Enemy extends Entity{
	int hitDamage;
	public Enemy(String animFile, int numLimbs, int numJoints, float torsoSize, int health_, int hitDmg) {
		super(animFile, numLimbs, numJoints, torsoSize, health_);
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
	
	public boolean onScreen(float xMin, float yMin, float xMax, float yMax) {
		if(getX()>xMin && getY()> yMin && getX()< xMax && getY()< yMax) {
			return true;
		}else return false;
	}
}

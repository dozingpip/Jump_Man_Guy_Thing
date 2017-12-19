package game;

/**
 * Stores all the information to do with the current level.
 * @author Steph and Thomas
 *
 */
public class Level {
	private float levelWidth;
	private float startx, starty;
	private Platform[] platforms;
	
	/**
	 * 
	 * @param levelWidth_ full width of the level
	 * @param platforms_ list of all the platforms in the level
	 * @param startx_ starting x coord of the player
	 * @param starty_ starting y coord of the player
	 */
	public Level(float levelWidth_, Platform[] platforms_, float startx_, float starty_) {
		levelWidth = levelWidth_;
		platforms = platforms_;
		startx = startx_;
		starty = starty_;
	}
	
	/**
	 * 
	 * @return list of all the platforms in the level
	 */
	public Platform[] getPlatforms() {
		return platforms;
	}
	
	/**
	 * 
	 * @return start x position of the player
	 */
	public float getStartX() {
		return startx;
	}
	
	/**
	 * 
	 * @return start y position of the player
	 */
	public float getStartY() {
		return starty;
	}
	
	/**
	 * 
	 * @return full width of the level
	 */
	public float getWidth() {
		return levelWidth;
	}
	
	
}

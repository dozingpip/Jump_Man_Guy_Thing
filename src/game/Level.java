package game;

public class Level {
	float levelWidth;
	float startx, starty;
	Platform[] platforms;
	
	public Level(float levelWidth_, Platform[] platforms_, float startx_, float starty_) {
		levelWidth = levelWidth_;
		platforms = platforms_;
		startx = startx_;
		starty = starty_;
	}

	public Platform[] getPlatforms() {
		return platforms;
	}
	
	public float getStartX() {
		return startx;
	}
	
	public float getStartY() {
		return starty;
	}
	
	public float getWidth() {
		return levelWidth;
	}
	
	
}

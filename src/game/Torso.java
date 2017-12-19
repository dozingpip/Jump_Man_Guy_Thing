package game;

public class Torso extends GraphicObject{
	private float size;
	
	//body color
	private final static int COLOR = 0xA0F7BA00;
	
	/**
	 * 
	 * @param size_ diameter of the torso
	 */
	public Torso(float size_) {
		size = size_;
	}
	
	/**
	 * draw the torso
	 */
	public void draw() {
		app_.pushMatrix();
		app_.fill(COLOR);
		app_.ellipse(0, 0, size, size);
		app_.popMatrix();
	}
}
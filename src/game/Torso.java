package game;

public class Torso extends GraphicObject{
	float size;
	
	//body color
	public final static int COLOR = 0xA0F7BA00;
	
	public Torso(float size_) {
		size = size_;
	}
	
	public void draw() {
		app_.pushMatrix();
		app_.fill(COLOR);
		app_.ellipse(0, 0, size, size);
		app_.popMatrix();
	}
}
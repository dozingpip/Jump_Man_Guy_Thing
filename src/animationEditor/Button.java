package animationEditor;

import game.GraphicObject;

public class Button extends GraphicObject{
	String label;
	float w, h;
	Runnable clickFunction;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param w_
	 * @param h_
	 * @param label_
	 * @param func
	 */
	public Button(float x, float y, float w_, float h_, String label_, Runnable func) {
		super(x, y, 0);
		w = w_;
		h = h_;
		label = label_;
		clickFunction = func;
	}
		
	public void draw() {
		app_.pushMatrix();
		setFillingColor();
		app_.noStroke();
		super.sdraw();
		app_.rect(0, 0, w, h);
		app_.scale(1, -1);
		app_.textSize(w/label.length() *1.8f);
		
		// The text color is random but never too close to the box color, 
		// so it can always be read.
		float[] colors = super.getColor();
		app_.fill((colors[0]+100)%255, (colors[1]+100)%255, (colors[2]+100)%255);
		
		app_.text(label, 0, 0);
	
		app_.popMatrix();
	}
	
	/** Tells whether the point whose coordinates are passed is inside this object
	 * 
	 * @param x  x coordinate of the point (in world coordinates)
	 * @param y x coordinate of the point (in world coordinates)
	 * @return true if (x, y) is inside this object
	 */
	public boolean isInside(float x, float y)
	{
		float a_ = getAngle();
		if(a_==0) {
			float myX = getX(), myY = getY();
			return ((x >= myX) && (x <= myX + w) && (y >= myY) && (y <= myY + h));
		}else {
			float dx = x - getX(), dy = y - getY();
			float ca = getCosAngle(), sa = getSinAngle();
			
			float rdx = ca*dx + sa*dy;
			float rdy = -sa*dx + ca*dy;
			
			return ((rdx >= -w/2) && (rdx <= w/2) && (rdy >= -h/2) && (rdy <= h/2));
		}
	}
	
	public void changeText(String newText) {
		label = newText;
	}
	
	public String getText() {
		return label;
	}
	
	public void onClick() {
		clickFunction.run();
	}
	
	public void setFunction(Runnable function) {
		clickFunction = function;
	}
}

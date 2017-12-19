package animationEditor;

import game.GraphicObject;

/**
 * Button class to place a rectangle with words on it somewhere on the screen and have something happen when you click inside it
 * @author Steph and Thomas
 *
 */
public class Button extends GraphicObject{
	private String label;
	private float w, h;
	private Runnable clickFunction;
	
	/**
	 * 
	 * @param x x coord of lower left corner of the button
	 * @param y y coord of lower left corner of the button
	 * @param w_ width of the button
	 * @param h_ height of the button
	 * @param label_ text to display on the button
	 * @param func function that should be called when a button is pressed
	 */
	public Button(float x, float y, float w_, float h_, String label_, Runnable func) {
		super(x, y, 0);
		w = w_;
		h = h_;
		label = label_;
		clickFunction = func;
	}
	
	/**
	 * draw the button
	 */
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
	
	/**
	 * change the text to the specified string
	 * @param newText what the text should change to
	 */
	public void changeText(String newText) {
		label = newText;
	}
	
	/**
	 * @return what the text currently is
	 */
	public String getText() {
		return label;
	}
	
	/**
	 * run the already specified click function when this button gets clicked
	 */
	public void onClick() {
		clickFunction.run();
	}
	
	/**
	 * change the click function to the specified one instead
	 * @param function specific function to change the click to
	 */
	public void setFunction(Runnable function) {
		clickFunction = function;
	}
}

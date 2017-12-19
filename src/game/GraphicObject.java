package game;

import processing.core.PApplet;

/**
 * General class for things that will get drawn.
 * @author Steph and Thomas
 *
 */
public abstract class GraphicObject implements ApplicationConstants{

	protected static PApplet app_;
	private static int appSetCounter_ = 0;
	private float x_, y_, angle_;
	private float cosAngle_;
	private float sinAngle_;
	private float r_, g_, b_;
	
	/**
	 * initialize this object at the specified location in the world with this specific angle of rotation.
	 * also gives a random color to use if need be
	 * @param x x coord in world coordinates
	 * @param y y coord in world coordinates
	 * @param angle angle of rotation to use when drawing.
	 */
	public GraphicObject(float x, float y, float angle) {
		x_ = x;
		y_ = y;

		angle_ = angle;
		cosAngle_ = PApplet.cos(angle_);
		sinAngle_ = PApplet.sin(angle_);
		r_ = app_.random(255);
		g_ = app_.random(255);
		b_ = app_.random(255);
	}
	
	/**
	 * if no x, y, or angle is specified place the object somewhere randomly on the screen with no rotation.
	 */
	public GraphicObject() {
		x_ = (float) (WORLD_X_MIN + (WORLD_X_MAX - WORLD_X_MIN) * Math.random());
		y_ = (float) (WORLD_Y_MIN + (WORLD_Y_MAX - WORLD_Y_MIN) * Math.random());

		angle_ = (float) (Math.random()*Math.PI);
		r_ = app_.random(255);
		g_ = app_.random(255);
		b_ = app_.random(255);
	}
	
	/**
	 * every graphic object needs to be drawn
	 */
	public abstract void draw();
	
	/**
	 * 
	 * @return current x coord of this object
	 */
	public float getX() {
		return x_;
	}
	
	/**
	 * 
	 * @return current y coord of this object
	 */
	public float getY() {
		return y_;
	}
	
	/**
	 * 
	 * @return current angle of this object
	 */
	public float getAngle() {
		return angle_;
	}
	
	/**
	 * 
	 * @return sine of the current angle of this object
	 */
	public float getSinAngle() {
		return sinAngle_;
	}
	
	/**
	 * 
	 * @return cosine of the current angle of this object
	 */
	public float getCosAngle() {
		return cosAngle_;
	}
	
	/**
	 * 
	 * @return a list of the colors this object has
	 */
	public float[] getColor() {
		float[] colorList = {r_,g_,b_};
		return colorList;
	}
	
	/**
	 * 
	 * @param x set the x of the object to this specified value
	 */
	public void setX(float x) {
		x_ = x;
	}
	
	/**
	 * 
	 * @param y set the y of the object to this specified value
	 */
	public void setY(float y) {
		y_ = y;
	}
	
	/**
	 * set the x, y, and angle of this object all at once
	 * @param x
	 * @param y
	 * @param a
	 */
	public void setXYA(float x, float y, float a) {
		setX(x);
		setY(y);
		setAngle(a);
	}
	
	/**
	 * every time the angle is changed, sinAngle and cosAngle have to get set again too.
	 * this is a thing for all redundant information variables like this Gotta weigh pros
	 *  and cons of setting them every time angle changes vs re-calculating cos(angle) and
	 *   sin(angle) every time, for example isInside() gets checked.
	 *   @param angle new angle
	 */
	public void setAngle(float angle) {
		angle_ = angle;
		cosAngle_ = PApplet.cos(angle_);
		sinAngle_ = PApplet.sin(angle_);
	}
	
	/**
	 * just move the object and rotate it to wherever it should in one step.
	 */
	public void sdraw() {
		app_.translate(getX(), getY());
		app_.rotate(getAngle());
	}
	
	/**
	 * set the color to the specified values
	 * @param r red
	 * @param g green
	 * @param b blue
	 */
	public void setColor(float r, float g, float b) {
		r_ = r;
		g_ = g;
		b_ = b;
	}
	
	/**
	 * set the current filling color to this object's colors.
	 */
	public void setFillingColor() {
		app_.fill(r_, g_, b_);
	}

	/**
	 * setup this object so it can be drawn properly to the sketch and know what app_ is.
	 * @param theApp
	 * @return
	 */
	public static int setup(PApplet theApp)
	{
		if (appSetCounter_ == 0) 
		{
			app_ = theApp;
			appSetCounter_ = 1;
		}
		else
			appSetCounter_ = 2;

		return appSetCounter_;
	}
}

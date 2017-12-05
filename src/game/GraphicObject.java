package game;

import processing.core.PApplet;

public abstract class GraphicObject implements ApplicationConstants{

	protected static PApplet app_;
	private static int appSetCounter_ = 0;
	private float x_, y_, angle_;
	private float cosAngle_;
	private float sinAngle_;
	private float r_, g_, b_;
	
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
	
	public GraphicObject() {
		x_ = (float) (WORLD_X_MIN + (WORLD_X_MAX - WORLD_X_MIN) * Math.random());
		y_ = (float) (WORLD_Y_MIN + (WORLD_Y_MAX - WORLD_Y_MIN) * Math.random());

		angle_ = (float) (Math.random()*Math.PI);
		r_ = app_.random(255);
		g_ = app_.random(255);
		b_ = app_.random(255);
	}
	
	public abstract void draw();
	
	public float getX() {
		return x_;
	}
	public float getY() {
		return y_;
	}
	public float getAngle() {
		return angle_;
	}
	
	public float getSinAngle() {
		return sinAngle_;
	}
	public float getCosAngle() {
		return cosAngle_;
	}
	
	public float[] getColor() {
		float[] colorList = {r_,g_,b_};
		return colorList;
	}
	
	public void setX(float x) {
		x_ = x;
	}
	public void setY(float y) {
		y_ = y;
	}
	
	public void setXYA(float x, float y, float a) {
		x_=x;
		y_=y;
		angle_=a;
	}
	
	/*every time the angle is changed, sinAngle and cosAngle have to get set again too.
		this is a thing for all redundant information variables like this
		Gotta weigh pros and cons of setting them every time angle changes vs 
		re-calculating cos(angle) and sin(angle) every time, for example isInside()
		gets checked.
	 */
	public void setAngle(float angle) {
		angle_ = angle;
		cosAngle_ = PApplet.cos(angle_);
		sinAngle_ = PApplet.sin(angle_);
	}
	
	public void sdraw() {
		app_.translate(getX(), getY());
		app_.rotate(getAngle());
	}
	
	public void setColor(float r, float g, float b) {
		r_ = r;
		g_ = g;
		b_ = b;
	}
	
	public void setFillingColor() {
		app_.fill(r_, g_, b_);
	}


	protected static int setup(PApplet theApp)
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

package game;

import processing.core.PImage;

public class Torso extends GraphicObject{
	float size;
	
	//body color
	public final static int COLOR = 0xF0F7BA00;
	private PImage eye, hat;
	private float eyeRotation;
	boolean eyesSpin;
	float timer;
	float hatY;
	
	public Torso(float size_) {
		eye = app_.loadImage("eye.png");
		hat = app_.loadImage("hat.png");
		size = size_;
		eyeRotation = 0;
		timer = 0;
		hatY = 0;
	}
	
	public void draw() {
		app_.pushMatrix();
		app_.fill(COLOR);
		app_.ellipse(0, 0, size, size);
		app_.scale(0.05f, 0.05f);
		if(eyesSpin) {
			if(eyeRotation <= app_.PI*2)
				eyeRotation+=app_.PI/16;
			else {
				eyesSpin = false;
				eyeRotation = 0;
			}
		}
		
		app_.image(eye, -0.5f, 0);
		app_.rotate(app_.PI/2 + eyeRotation);
		app_.image(eye, -0.5f, 0);
		app_.pushMatrix();
		app_.translate(0, hatY);
		app_.rotate(app_.PI/2);
		app_.image(hat, -50f, -120f);
		app_.popMatrix();
		app_.popMatrix();
	}
	
	public void rollEyes() {
		eyesSpin = true;
	}
	
	public void setHatY(float newY) {
		hatY = newY;
	}
}
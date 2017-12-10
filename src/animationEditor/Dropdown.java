package animationEditor;

import java.util.ArrayList;

public class Dropdown extends Button{
	ArrayList<Button> buttons;
	boolean drawChildren = false;
	static Runnable staticy = new Runnable() { public void run() {}};
	public Dropdown(float x, float y, float w, float h, String label, ArrayList<Button> buttons_) {
		super(x, y, w, h, label, staticy);
		Runnable toggle = new Runnable() { public void run() {toggleOpen();}};
		setFunction(toggle);
		buttons = buttons_;
	}
	public void toggleOpen() {
		drawChildren = !drawChildren;
	}
	
	public void draw() {
		super.draw();
		if(drawChildren)
			for(Button b : buttons) {
				b.draw();
			}
	}
	
	public ArrayList<Button> getButtons() {
		return buttons;
	}
}

package animationEditor;

import java.util.ArrayList;

public class Menu {
	ArrayList<Runnable> r;
	ArrayList<String> buttonNames;
	float buttonY, menuWidth, buttonHeight, leftmost, rightmost;
	ArrayList<Button> buttons;
	
	/**
	 * This class is for collection of buttons displayed for one purpose or another.
	 * @param r_ All the functions for the onClick functionality of each button (r_ and buttonNames
	 * 	should be in the same order)
	 * @param buttonNames_
	 * @param buttonY_ The y coordinate all the buttons will be drawn at.
	 * @param menuWidth_ The maximum amount of space all these buttons together can take up.
	 * @param buttonHeight_
	 * @param leftmost_ The x value of where the leftmost button should be drawn, the other buttons'
	 * 	x coordinates are configured dynamically from there.
	 */
	public Menu(ArrayList<Runnable> r_, ArrayList<String> buttonNames_, float buttonY_,
			float menuWidth_, float buttonHeight_, float leftmost_) {
		r = r_;
		buttonNames = buttonNames_;
		buttonY = buttonY_;
		menuWidth = menuWidth_;
		buttonHeight = buttonHeight_;
		leftmost = leftmost_;
		buttons = new ArrayList<Button>();
		float buttonWidth = (menuWidth-1-buttonNames.size())/(buttonNames.size());
		if(buttonNames.size() == r.size())
			for(int i = 0; i<r.size(); i++) {
				float buttonX = (leftmost+1)+(buttonWidth+1)*i;
				buttons.add(new Button(buttonX,
						buttonY, buttonWidth, 2f, buttonNames.get(i), r.get(i)));
				rightmost = buttonX +buttonWidth;
			}
	}
	
	public void draw() {
		for(Button button: buttons)
			button.draw();
	}
	
	/**
	 * Checking whether the mouse is inside a button only happens if the click is within the
	 * limits of the menu itself.
	 * @param x
	 * @param y
	 */
	public void checkIsInside(float x, float y) {
		if(x>=leftmost && x<=rightmost && y>=buttonY)
			for(int i = 0; i<buttons.size(); i++) {
				if(buttons.get(i).isInside(x, y)) {
					buttons.get(i).onClick();
				}
			}
	}
	
	/**
	 * If something with an individual button must be changed, can use this to get the button
	 * and maybe do something with it.
	 * @param index
	 * @return
	 */
	public Button get(int index) {
		return buttons.get(index);
	}
}

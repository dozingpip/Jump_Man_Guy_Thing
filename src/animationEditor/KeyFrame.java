package animationEditor;

import java.util.ArrayList;

public class KeyFrame {
	int t;
	float x, y, a;
	ArrayList<ArrayList<Float>> limbs;
	
	/**
	 * The things every keyframe needs to keep track of.
	 * @param t_
	 * @param x_
	 * @param y_
	 * @param a_
	 * @param limbs_
	 */
	public KeyFrame(int t_, float x_, float y_, float a_,
			ArrayList<ArrayList<Float>> limbs_) {
		t = t_;
		x = x_;
		y = y_;
		a = a_;
		limbs = limbs_;
	}
	
	public KeyFrame(int t_, float x_, float y_, float a_) {
		t = t_;
		x = x_;
		y = y_;
		a = a_;
	}
	
	public int getT() {
		return t;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	

	public float getA() {
		return a;
	}
	
	public ArrayList<ArrayList<Float>> getLimbsJoints(){
		return limbs;
	}
	
	/**
	 * Put all the relevant information about thing keyframe in one long string so it can be
	 * added to an output file.
	 * Different separators for what level in the hierarchy of things it's at.
	 * Hierarchical view:
	 * t
	 * x
	 * y
	 * a
	 * limbs
	 * 		limb0
	 * 			joints
	 * 				joint0
	 * 					.
	 * 					.
	 * 					.
	 * 		.
	 * 		.
	 * 		.
	 * 
	 * @return The string equivalent of this keyframe.
	 */
	public String keyFrameOut() {
		String out = ""+t+"," + x + ","+ y +","+ a;
		out+="\n";
		for(int i = 0; i<limbs.size(); i++) {
			ArrayList<Float> jointAngles = limbs.get(i);
			for(int j = 0; j<jointAngles.size(); j++) {
				out+=""+jointAngles.get(j);
				if(j != jointAngles.size()-1) {
					out+=",";
				}
			}
			out+="\n";
		}
		return out;
	}
}

package animationEditor;

import java.util.ArrayList;

/**
 * 
 * @author Steph and Thomas
 *
 */
public class KeyFrame {
	float t, x, y, a;
	ArrayList<ArrayList<Float>> limbs;
	
	/**
	 * Initialize the keyframe (with limbs)
	 * @param t_ time at which to run this keyframe
	 * @param x_ x coord of the object this keyframe is for
	 * @param y_ y coord of the object this keyframe is for
	 * @param a_ angle of the object this keyframe is for
	 * @param limbs_ arraylist of the angles for all the joints on all the limbs
	 */
	public KeyFrame(float t_, float x_, float y_, float a_,
			ArrayList<ArrayList<Float>> limbs_) {
		t = t_;
		x = x_;
		y = y_;
		a = a_;
		limbs = limbs_;
	}
	
	/**
	 * Can initialize a keyframe for something that has no limbs
	 * @param t_ time at which to run this keyframe
	 * @param x_ x coord of the object this keyframe is for
	 * @param y_ y coord of the object this keyframe is for
	 * @param a_ angle of the object this keyframe is for
	 */
	public KeyFrame(float t_, float x_, float y_, float a_) {
		t = t_;
		x = x_;
		y = y_;
		a = a_;
	}
	
	/**
	 * 
	 * @return time to run this animation at
	 */
	public float getT() {
		return t;
	}
	
	/**
	 * 
	 * @return x coord to put the object at when this keyframe happens
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * 
	 * @return y coord to put the object at when this keyframe happens
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * 
	 * @return angle to put the object at when this keyframe happens
	 */
	public float getA() {
		return a;
	}
	
	/**
	 * 
	 * @return list of all the joints of all the limbs
	 */
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

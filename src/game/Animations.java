package game;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Animations {

	private ArrayList<KeyFrame> idleAnim, walkAnim, jumpAnim, hurtAnim, dyingAnim;
	static int idle_idx = 0, walk_idx = 1, jump_idx = 2, hurt_idx = 3, dying_idx = 4;
	
	
	public Animations(String filename) {
		try {
			getKeyframesFromFile(filename, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Does the heavy lifting of going through each line and getting each keyframe and all the
	 * data associated with them.
	 * @param filename
	 * @param encoding
	 * @return list of keyframes that make up to animation
	 * @throws IOException
	 */
	public void getKeyframesFromFile(String filename, Charset encoding) 
			throws IOException
	{
		System.out.println("Opening "+ filename+".");
		String content = readFile(filename, encoding);
		String[] lines = content.replaceAll("\t","").split("\\r?\\n");
		int numAnimations = Integer.parseInt(lines[0]);
		int numLimbs = Integer.parseInt(lines[1]);
		int numJoints = Integer.parseInt(lines[2]);
		
		//the plus 2 is for: 
		// 1st line has the time, x, y, and angle values
		// the lines for each limb come after that
		// and then there's a new line to separate one frame from another (accounting for
		// 2nd line added to the line count every frame takes up)
		int numLinesPerKeyframe = 2+ numLimbs;
		
		// the first line related to whatever animation we're looking at. (would be the
		// animation length/ number of keyframes)
		int animStartLine = 3;
		ArrayList<ArrayList<KeyFrame>> allAnims = new ArrayList<>();
		for(int i = 0; i<numAnimations; i++) {
			int numFrames = Integer.parseInt(lines[animStartLine]);
			
			// The plus one is for the two extra new lines between animations
			int numLinesForThisAnimation = numLinesPerKeyframe*numFrames+2;
			animStartLine+=numLinesForThisAnimation;
			ArrayList<KeyFrame> anAnimation = new ArrayList<>();
			for(int j = 0; j<numFrames; j++) {
				int keyframeStartLine = animStartLine+1+j*numLinesPerKeyframe;
				String[] items = lines[keyframeStartLine].split(",");
				int t = Integer.parseInt(items[0]);
				float x = Float.parseFloat(items[1]);
				float y = Float.parseFloat(items[2]);
				float a = Float.parseFloat(items[3]);
				if(numLimbs == 0) {
					ArrayList<ArrayList<Float>> limbs = new ArrayList<ArrayList<Float>>();
					for(int k = 0; k<numLimbs; k++) {
						String[] joints = lines[keyframeStartLine+1+k].split(",");
						ArrayList<Float> limbAngles = new ArrayList<Float>();
						for(int l=0; l<numJoints; l++) {
							limbAngles.add(Float.parseFloat(joints[l]));
						}
						limbs.add(limbAngles);
					}
					anAnimation.add(new KeyFrame(t, x, y, a, limbs));
				}else
					anAnimation.add(new KeyFrame(t, x, y, a));
			}
			allAnims.add(anAnimation);
		}
		idleAnim = allAnims.get(idle_idx);
		walkAnim = allAnims.get(walk_idx);
		jumpAnim = allAnims.get(jump_idx);
		hurtAnim = allAnims.get(hurt_idx);
		dyingAnim = allAnims.get(dying_idx);
	}
	
	/**
	 * Output a text file's contents as a string.
	 * @param path
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	static String readFile(String path, Charset encoding) {
		byte[] encoded;
		try {
			System.out.println(path);
			encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, encoding);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return null;
	}
	
	public ArrayList<KeyFrame> getIdle() {
		return idleAnim;
	}
	
	public ArrayList<KeyFrame> getWalk() {
		return walkAnim;
	}
	
	public ArrayList<KeyFrame> getJump() {
		return jumpAnim;
	}
	
	public ArrayList<KeyFrame> getHurt() {
		return hurtAnim;
	}
	
	public ArrayList<KeyFrame> getDying() {
		return dyingAnim;
	}
}

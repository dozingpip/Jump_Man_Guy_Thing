package animationEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class FileInOutMachine {

	/**
	 * output the current state of the keyframes array to a text file with a name of the user's
	 * choosing.
	 */
	public static void saveKeyFramesToFile(ArrayList<KeyFrame> keyframes) {
		String filename = pickFile();
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			writer.println(keyframes.size());
			for(int i = 0; i<keyframes.size(); i++) {
				writer.println(keyframes.get(i).keyFrameOut());
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Your animation has been saved as " + filename);
	}

	/**
	 * Does the heavy lifting of going through each line and getting each keyframe and all the
	 * data associated with them.
	 * @param filename
	 * @param encoding
	 * @return list of keyframes that make up to animation
	 * @throws IOException
	 */
	public static ArrayList<KeyFrame> getKeyframesFromFile(String filename) 
			throws IOException
	{
		System.out.println("Opening "+ filename+".");
		String content = readFile(filename, StandardCharsets.UTF_8);
		String[] lines = content.replaceAll("\t","").split("\\r?\\n");
		
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
		int animStartLine = 4;
		int numFrames = Integer.parseInt(lines[animStartLine]);
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
		return anAnimation;
	}
	
	public static ArrayList<KeyFrame> getKeyframesFromFile() throws IOException{
		return getKeyframesFromFile(pickFile());
	}
	
	/**
	 * @return the chosen file name as a String
	 */
	public static String pickFile() {
		System.out.println("Enter a file location/ name e.g. dance.txt : ");
		Scanner scanner = new Scanner(System.in);
		String filename = scanner.nextLine();
		if(scanner.nextLine().isEmpty())
			scanner.close();
		filename = "Animations/"+ filename.replaceAll(" ", "").replace("\n", "").replace("\r", "");
		return filename;
	}
	
	/**
	 * @return the list of filenames in the folder as an array list
	 */
	public static ArrayList<String> pickFile(String folderName) {
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();
		ArrayList<String> fileNames = new ArrayList<>();
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        fileNames.add(listOfFiles[i].getName());
	      }/*else if (listOfFiles[i].isDirectory()) {
	        System.out.println("Directory " + listOfFiles[i].getName());
	      }*/
	    }
		return fileNames;
	}
	
	/**
	 * Output a text file's contents as a string.
	 * @param path
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	static String readFile(String path, Charset encoding) 
			  throws IOException 
	{
	  byte[] encoded = Files.readAllBytes(Paths.get(path));
	  return new String(encoded, encoding);
	}
}

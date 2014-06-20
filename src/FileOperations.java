import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;


public class FileOperations {

	// read username-highscore pairs from high score file
	public static ArrayList<UsernameScoreTuple> readHighScoreFile(File file) {
		String line = null;
		BufferedReader br = null;
		ArrayList<UsernameScoreTuple> ar = new ArrayList<UsernameScoreTuple>();
		
		try{ // open file
			br = new BufferedReader(new FileReader(file));
		} catch(FileNotFoundException e) {e.printStackTrace();}
		
		while(true) {
			try { // read a line from file
				line = br.readLine();
			} catch (IOException e) {e.printStackTrace();}
			if(line == null) break;
			
			// add line contents to ArrayList
			String username; int score;
			username = line.trim().split(" ")[0];
			score = Integer.parseInt(line.trim().split(" ")[1]);
			ar.add(new UsernameScoreTuple(username, score));
		}
		
		try {br.close();} catch (IOException e) {e.printStackTrace();}
		return ar;
	}
	
	// write username-highscore pairs to high score file
	public static void writeHighScoreFile(File file, ArrayList<UsernameScoreTuple> usernameScoreTuples) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			for(UsernameScoreTuple ust : usernameScoreTuples) {
				writer.write(ust.getUsername() + " " + ust.getScore() + "\n");
			}
			writer.close();
		} catch(IOException e) {e.printStackTrace();}
	}

}

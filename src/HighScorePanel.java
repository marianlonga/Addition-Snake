import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class HighScorePanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private MainFrame mainFrame = null;
	private JLabel labelHighScores = null, labelHighScoreTitle;
	
	public HighScorePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setSize(MainFrame.SCREEN_W, MainFrame.SCREEN_H);
		this.setLayout(null);
		addKeyListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		setupHighScorePanel();
	}
	
	private void setupHighScorePanel() {
		labelHighScoreTitle = new JLabel("High Scores");
		labelHighScoreTitle.setFont(new Font("Georgia", Font.PLAIN, 40));
		labelHighScoreTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelHighScoreTitle.setBounds(MainFrame.SCREEN_W/2-300, 50, 600, 50);
		this.add(labelHighScoreTitle);
		
		labelHighScores = new JLabel();
		labelHighScores.setBounds(MainFrame.SCREEN_W/2-300, 50, 600, 450);
		labelHighScores.setFont(new Font("Georgia", Font.PLAIN, 20));
		labelHighScores.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(labelHighScores);
	}
	
	public void displayCurrentHighScores() {
		ArrayList<UsernameScoreTuple> usernameScoreTuples = FileOperations.readHighScoreFile(new File("highscore.txt"));
		String scores;
		scores = "<html>";
		for(int i = 0; i < usernameScoreTuples.size(); i++) {
			scores += i+1 + ". " + usernameScoreTuples.get(i).getUsername() + ": " + usernameScoreTuples.get(i).getScore() + "pts<br>";
		}
		scores += "</html>";
		labelHighScores.setText(scores);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			mainFrame.highScoreIsOver();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}


}

import java.awt.Color;
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
	private JLabel labelHighScores = null, labelHighScoreTitle, labelESC;
	
	public HighScorePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setSize(MainFrame.SCREEN_W, MainFrame.SCREEN_H);
		this.setLayout(null);
		addKeyListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		displayHighScore();
	}
	
	public void displayHighScore() {
		
		labelHighScoreTitle = new JLabel(Translations.translate("High Score"));
		labelHighScoreTitle.setFont(new Font("Georgia", Font.PLAIN, 40));
		labelHighScoreTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelHighScoreTitle.setBounds(MainFrame.SCREEN_W/2-300, 50, 600, 50);
		this.add(labelHighScoreTitle);
		
		labelHighScores = new JLabel();
		labelHighScores.setBounds(MainFrame.SCREEN_W/2-300, 70, 600, 450);
		labelHighScores.setFont(new Font("Georgia", Font.PLAIN, 20));
		labelHighScores.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(labelHighScores);
		
		labelESC = new JLabel(Translations.translate("Press [ESC] to return to menu"));
		labelESC.setFont(new Font("Georgia", Font.PLAIN, 16));
		labelESC.setHorizontalAlignment(SwingConstants.RIGHT);
		labelESC.setBounds(MainFrame.SCREEN_W-260, MainFrame.SCREEN_H-55, 250, 30);
		labelESC.setForeground(new Color(200, 0, 0));
		this.add(labelESC);
		
		ArrayList<UsernameScoreTuple> usernameScoreTuples = FileOperations.readHighScoreFile(new File("highscore.txt"));
		String scores;
		scores = "<html>";
		for(int i = 0; i < usernameScoreTuples.size(); i++) {
			scores += "<tr><td>" + i+1 + ".</td><td>" + usernameScoreTuples.get(i).getUsername() + ":</td><td>" + usernameScoreTuples.get(i).getScore() + " pts.</td></tr><br>";
		}
		scores += "</html>";
		labelHighScores.setText(scores);
	}
	
	public void refreshHighScore() {
		labelHighScoreTitle.setText(Translations.translate("High Score"));
		labelESC.setText(Translations.translate("Press [ESC] to return to menu"));
		
		ArrayList<UsernameScoreTuple> usernameScoreTuples = FileOperations.readHighScoreFile(new File("highscore.txt"));
		String scores;
		scores = "<html>" +
				 "<tr><td><b>" + Translations.translate("No.") + "</b></td><td><b>" + Translations.translate("Username") + "</b></td><td><b>" + Translations.translate("Score") + "</b></td></tr>";
		for(int i = 0; i < usernameScoreTuples.size(); i++) {
			scores += "<tr><td>" + (i+1) + ".</td><td>" + usernameScoreTuples.get(i).getUsername() + "</td><td>" + usernameScoreTuples.get(i).getScore() + " pts.</td></tr><br>";
		}
		scores += "</html>";
		labelHighScores.setText(scores);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			mainFrame.highScoreToMenu();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}


}

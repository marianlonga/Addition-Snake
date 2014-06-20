import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class MenuPanel extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	static JButton buttonNewGame, buttonHighScore, buttonExit;
	static JLabel labelTitle;
	private MainFrame mainFrame = null;
	
	public MenuPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setSize(MainFrame.SCREEN_W, MainFrame.SCREEN_H);
		this.setLayout(null);
		addKeyListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
		displayThings();
	}
	
	public void displayThings() {
		labelTitle = new JLabel("Welcome to Addition Snake!");
		labelTitle.setFont(new Font("Georgia", Font.PLAIN, 40));
		labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitle.setBounds(MainFrame.SCREEN_W/2-300, 50, 600, 50);
		this.add(labelTitle);
		
		buttonNewGame = new JButton("New Game");
		buttonNewGame.setBounds(MainFrame.SCREEN_W/2-100, 200, 200, 50);
		this.add(buttonNewGame);
		buttonNewGame.addActionListener(this);
		
		buttonHighScore = new JButton("High Score");
		buttonHighScore.setBounds(MainFrame.SCREEN_W/2-100, 300, 200, 50);
		this.add(buttonHighScore);
		buttonHighScore.addActionListener(this);
		
		buttonExit = new JButton("Exit");
		buttonExit.setBounds(MainFrame.SCREEN_W/2-100, 400, 200, 50);
		this.add(buttonExit);
		buttonExit.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonNewGame) {
			mainFrame.startNewGame();
		}
		if(e.getSource() == buttonHighScore) {
			mainFrame.viewHighScores();
		}
		if(e.getSource() == buttonExit) {
			System.exit(0);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

}

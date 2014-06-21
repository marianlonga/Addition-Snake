package com.marianlonga.additionsnake;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class MenuPanel extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	static JButton buttonNewGame, buttonHighScore, buttonExit, buttonEnglish, buttonSlovencina;
	static JLabel labelTitle, labelSubTitle, labelESC, labelLanguage, labelSnakeImage;
	private MainFrame mainFrame = null;
	
	public MenuPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setSize(MainFrame.SCREEN_W, MainFrame.SCREEN_H);
		this.setLayout(null);
		addKeyListener(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
		displayMenu();
	}
	
	public void displayMenu() {
		
		// Title labels
		
		labelTitle = new JLabel(Translations.translate("Welcome to Addition Snake!"));
		labelTitle.setFont(new Font("Georgia", Font.PLAIN, 40));
		labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitle.setBounds(MainFrame.SCREEN_W/2-300, 20, 600, 50);
		this.add(labelTitle);
		
		labelSubTitle = new JLabel(Translations.translate("by Marian Longa"));
		labelSubTitle.setFont(new Font("Georgia", Font.ITALIC, 16));
		labelSubTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		labelSubTitle.setBounds(MainFrame.SCREEN_W/2, 50, 200, 50);
		this.add(labelSubTitle);
		
		// Menu buttons
		
		buttonNewGame = new JButton(Translations.translate("New Game"));
		buttonNewGame.setBounds(MainFrame.SCREEN_W/2-100, 150, 200, 50);
		buttonNewGame.setFont(new Font("Georgia", Font.PLAIN, 20));
		buttonNewGame.setForeground(new Color(0, 150, 0));
		this.add(buttonNewGame);
		buttonNewGame.addActionListener(this);
		
		buttonHighScore = new JButton(Translations.translate("High Score"));
		buttonHighScore.setBounds(MainFrame.SCREEN_W/2-100, 220, 200, 50);
		buttonHighScore.setFont(new Font("Georgia", Font.PLAIN, 20));
		buttonHighScore.setForeground(new Color(200, 150, 0));
		this.add(buttonHighScore);
		buttonHighScore.addActionListener(this);
		
		buttonExit = new JButton(Translations.translate("Exit"));
		buttonExit.setBounds(MainFrame.SCREEN_W/2-100, 290, 200, 50);
		buttonExit.setFont(new Font("Georgia", Font.PLAIN, 16));
		buttonExit.setForeground(new Color(150, 0, 0));
		this.add(buttonExit);
		buttonExit.addActionListener(this);
		
		labelESC = new JLabel(Translations.translate("Press [ESC] to exit"));
		labelESC.setHorizontalAlignment(SwingConstants.RIGHT);
		labelESC.setFont(new Font("Georgia", Font.PLAIN, 16));
		labelESC.setBounds(MainFrame.SCREEN_W-210, MainFrame.SCREEN_H-55, 200, 30);
		labelESC.setForeground(new Color(200, 0, 0));
		this.add(labelESC);
		
		// Language
		
		labelLanguage = new JLabel("Language / Jazyk");
		labelLanguage.setHorizontalAlignment(SwingConstants.CENTER);
		labelLanguage.setFont(new Font("Georgia", Font.PLAIN, 20));
		labelLanguage.setBounds(MainFrame.SCREEN_W/2-200, 400, 400, 40);
		this.add(labelLanguage);
		
		buttonEnglish = new JButton(new ImageIcon("english.png"));
		buttonEnglish.setBounds(MainFrame.SCREEN_W/2-150-5-5, 450-7, 150+10, 90+14);
		//buttonEnglish.setFont(new Font("Georgia", Font.PLAIN, 22));
		buttonEnglish.addActionListener(this);
		this.add(buttonEnglish);
		
		buttonSlovencina = new JButton(new ImageIcon("slovak.png"));
		buttonSlovencina.setBounds(MainFrame.SCREEN_W/2 +5, 450-7, 150+10, 90+14);
		//buttonSlovencina.setFont(new Font("Georgia", Font.PLAIN, 22));
		buttonSlovencina.addActionListener(this);
		this.add(buttonSlovencina);
		
		// Image of snake
		labelSnakeImage = new JLabel(new ImageIcon("snake_screenshot2.png"));
		labelSnakeImage.setBounds(240, 73, 349, 146);
		this.add(labelSnakeImage);
		
		
	}
	
	private void refreshMenu() {
		labelTitle.setText(Translations.translate("Welcome to Addition Snake!"));
		labelSubTitle.setText(Translations.translate("by Marian Longa"));
		buttonNewGame.setText(Translations.translate("New Game"));
		buttonHighScore.setText(Translations.translate("High Score"));
		buttonExit.setText(Translations.translate("Exit"));
		labelESC.setText(Translations.translate("Press [ESC] to exit"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonNewGame) {
			mainFrame.menuToGame();
		}
		if(e.getSource() == buttonHighScore) {
			mainFrame.menuToHighScore();
		}
		if(e.getSource() == buttonExit) {
			System.exit(0);
		}
		if(e.getSource() == buttonEnglish) {
			Translations.setLanguage("English");
			refreshMenu();
		}
		if(e.getSource() == buttonSlovencina) {
			Translations.setLanguage("Slovencina");
			refreshMenu();
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

package exercises;

//Anthony Marquez

import examples.FileHelper;
import java.util.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Hangman extends KeyAdapter {

	Stack<String> puzzles = new Stack<String>();
	ArrayList<JLabel> boxes = new ArrayList<JLabel>();
	int lives = 9;
	JLabel livesLabel = new JLabel("" + lives);
	
	String word;

	public static void main(String[] args) {
		Hangman hangman = new Hangman();
		hangman.addPuzzles();
		hangman.createUI();
	}

	public List<String> loadWords() {
		List<String> wordList = FileHelper.loadFileContentsIntoArrayList("resource/words.txt");
		return wordList;
	}

	public void addPuzzles() { 

		List<String> listWord = loadWords();
		for (int i = 0; i < 10; i++) {
			int index = new Random().nextInt(listWord.size());
			word = listWord.get(index);	
			puzzles.push(word.toLowerCase().trim());
		}
	}

	JPanel panel = new JPanel();
	private String puzzle;

	private void createUI() {
		playDeathKnell();
		JFrame frame = new JFrame("June's Hangman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.add(livesLabel);
		loadNextPuzzle();
		frame.add(panel);
		frame.setVisible(true);
		frame.pack();
		frame.addKeyListener(this);
	}

	public void loadNextPuzzle() {
		removeBoxes();
		lives = 9;
		livesLabel.setText("" + lives);
		puzzle = puzzles.pop();
		System.out.println("puzzle is now " + puzzle);
		createBoxes();
		
		try {
			if (!puzzle.matches("[A-Za-z]+")) {
				throw new Exception("Word " + puzzle + " contains special characters!");
			}

		} 
		catch (Exception b) {
			System.out.println(b.getMessage());
			loadNextPuzzle();
		}
		finally {
			System.out.println("puzzle is now " + puzzle);
			createBoxes();
		}
	}

	public void keyTyped(KeyEvent arg0) {
		System.out.println(arg0.getKeyChar());
		updateBoxesWithUserInput(arg0.getKeyChar());
		if (lives == 0) {
			playDeathKnell();
			loadNextPuzzle();
		}
	}

	public void updateBoxesWithUserInput(char keyChar) {
		boolean gotOne = false;
		for (int i = 0; i < puzzle.length(); i++) {
			if (puzzle.charAt(i) == keyChar) {
				boxes.get(i).setText("" + keyChar);
				gotOne = true;
				
			}
		}
		
		if (!gotOne) {
			livesLabel.setText("" + --lives);
			return;
			
		}
		
		else {
			String currentLetters = "";
			for (int i = 0; i < puzzle.length(); i++) {
				currentLetters = currentLetters + boxes.get(i).getText();
			}
			if (puzzle.equals(currentLetters)) {
				loadNextPuzzle();
			}
		}
		
	}

	void createBoxes() {
		for (int i = 0; i < puzzle.length(); i++) {
			JLabel textField = new JLabel("_");
			boxes.add(textField);
			panel.add(textField);
		}
	}

	void removeBoxes() {
		for (JLabel box : boxes) {
			panel.remove(box);
		}
		boxes.clear();
	}
	
	public void playDeathKnell() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("resource/funeral-march.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
			Thread.sleep(8400);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}

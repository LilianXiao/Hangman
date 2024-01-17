package org.cis1200.hangman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Hangman extends JFrame implements ActionListener {
    // Size of the entire frame
    public static final Dimension FRAME_DIMENSIONS = new Dimension(550, 700);
    // Size of the entire panel holding the interactive keyboard
    public static final Dimension BUTTON_DIMENSIONS = new Dimension(
            FRAME_DIMENSIONS.width - 10,
            (int) (FRAME_DIMENSIONS.height * 0.45) - 40
    );
    // Size of the menu dialog popup
    public static final Dimension DIALOG_DIMENSIONS = new Dimension(
            (int) (FRAME_DIMENSIONS.width / 2),
            (int) (FRAME_DIMENSIONS.height / 2)
    );
    // indicates game state at all times
    private final JLabel STATUS;
    // filepath for image of the gallows, has 6 versions (updated based on player
    // guesses)
    public static final String HANGMAN_IMAGE = "files/0.png";
    // filepath for entire dictionary word bank
    public static final String DICTIONARY_DATA = "files/dictionary.txt";
    // filepath for game data (keyboard data)
    public static final String GAME_DATA = "files/gamestate.txt";
    // letters inputted by the player
    private String inputLetters = "";
    // keeps track of the number of incorrect letter guesses player makes
    private int numIncorrect;
    // tracks current word that has been randomly chosen
    private String word;
    // this field allows access to methods for random word generating
    private WordManager wm;
    // 2D array housing the 4x7 keyboard JButton display
    private JButton[][] letterArray;
    // JLabel for the gallows
    private JLabel hangmanImage;
    // JLabel for the hidden word
    private JLabel hiddenWord;
    // bottom 3 JLabels compose the end of game popup menu box
    private JLabel result;
    private JLabel wordLabel;
    private JDialog dialog;

    public Hangman(String run) {
        super("Hangman");
        setSize(FRAME_DIMENSIONS);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // dynamic resizing not necessary for this game

        getContentPane().setBackground(Color.BLACK);

        // instantiate fields and such
        wm = new WordManager(DICTIONARY_DATA);
        word = wm.getRandomWord();
        letterArray = new JButton[4][7];
        inputLetters += word + "\n";

        // add game running status to bottom of layout
        this.STATUS = new JLabel(run);
        this.STATUS.setForeground(Color.YELLOW);
        this.STATUS.setBounds(0, 620, 500, 20);
        getContentPane().add(STATUS);

        // applies the final pop up menu components
        createResult();
        // applies the hangman GUI components
        hangmanGUI();
    }

    public void openInstructions(JFrame f) {
        String instructions = "HANGMAN \n\n " +
                "You have 6 tries to guess the letters from a \n" +
                "randomly generated word from a dictionary. \n" +
                "You must click the keyboard letters.\n " +
                "The word is hidden by question marks:\n" +
                "Each correct letter reveals a letter, but\n" +
                "each incorrect letter adds to the hangman.\n" +
                "If you want a new word to guess, press \n" +
                "the Reset button, or if you want to quit, use \n" +
                "the Quit button or simply close the window. \n " +
                "Use SAVE to save your progress on a word, and you \n" +
                "can come back to it later by using the LOAD button. \n" +
                "You can only save one game. \n" +
                "You will see your win/loss after the game, and you \n" +
                "can see your game status throughout at the bottom.";
        JOptionPane.showMessageDialog(
                f, instructions,
                "Instructions", JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void hangmanGUI() {
        JButton instructions = new JButton("INSTRUCTIONS");
        instructions.setBounds(100, 600, 160, 40);
        instructions.addActionListener(e -> openInstructions(this));
        getContentPane().add(instructions);

        // Add gallow to top of layout
        hangmanImage = ImageManager.imageLoader(HANGMAN_IMAGE);
        hangmanImage.setBounds(
                0, 0,
                hangmanImage.getPreferredSize().width,
                hangmanImage.getPreferredSize().height
        );
        getContentPane().add(hangmanImage);

        // Add the hidden word to layout right under gallow
        hiddenWord = new JLabel(WordManager.hideWord(word));
        hiddenWord.setForeground(Color.WHITE);
        hiddenWord.setBounds(
                0,
                hangmanImage.getY() + hangmanImage.getPreferredSize().height,
                FRAME_DIMENSIONS.width, hiddenWord.getPreferredSize().height
        );
        getContentPane().add(hiddenWord);

        // Add the keyboard buttons to layout
        GridLayout g = new GridLayout(4, 7);
        JPanel buttons = new JPanel();
        buttons.setBounds(
                -5,
                hiddenWord.getY() + hiddenWord.getPreferredSize().height,
                BUTTON_DIMENSIONS.width, BUTTON_DIMENSIONS.height
        );
        buttons.setLayout(g);
        JButton[] letters = new JButton[26];
        // Fill in the letters of the alphabet
        for (char c = 'A'; c <= 'Z'; c++) {
            JButton j = new JButton(Character.toString(c));
            j.setBackground(Color.BLUE);
            j.setForeground(Color.WHITE);
            j.addActionListener(this);
            int index = c - 'A';
            letters[index] = j;
        }
        int index = 0;
        for (int w = 0; w < letterArray.length; w++) {
            for (int h = 0; h < letterArray[0].length; h++) {
                if (index == letters.length) {
                    break;
                }
                letterArray[w][h] = letters[index];
                buttons.add(letterArray[w][h]);
                index++;
            }
        }
        getContentPane().add(buttons);

        // Button to reset everything
        JButton reset = new JButton("RESET");
        reset.setBackground(Color.BLACK);
        reset.setForeground(Color.YELLOW);
        reset.addActionListener(this);
        buttons.add(reset);

        // Button to quit current game
        JButton quitGame = new JButton("END");
        quitGame.setBackground(Color.BLACK);
        quitGame.setForeground(Color.YELLOW);
        quitGame.addActionListener(this);
        buttons.add(quitGame);

        // Button to save a game data for later
        JButton saveGame = new JButton("SAVE");
        saveGame.setBackground(Color.BLACK);
        saveGame.setForeground(Color.MAGENTA);
        saveGame.setBounds(300, 600, 80, 40);
        saveGame.addActionListener(this);
        getContentPane().add(saveGame);

        // Button to load the previously saved game data
        JButton loadGame = new JButton("LOAD");
        loadGame.setBackground(Color.BLACK);
        loadGame.setForeground(Color.MAGENTA);
        loadGame.setBounds(380, 600, 80, 40);
        loadGame.addActionListener(this);
        getContentPane().add(loadGame);
    }

    /*
     * Resets the game completely
     */
    private void reset() {
        // get new word from dictionary
        word = wm.getRandomWord();
        // reset incorrect guesses
        numIncorrect = 0;
        // reset the gallows
        ImageManager.updateImage(hangmanImage, HANGMAN_IMAGE);
        // reset hidden word
        hiddenWord.setText(WordManager.hideWord(word));
        // reset keyboard data
        inputLetters = word + "\n";
        // reset game status
        this.STATUS.setText("RUNNING...");
        // reset the keyboard
        for (int w = 0; w < letterArray.length; w++) {
            for (int h = 0; h < letterArray[0].length; h++) {
                if (w < 3 || (w == 3 && h <= 4)) {
                    letterArray[w][h].setBackground(Color.BLUE);
                    letterArray[w][h].setEnabled(true);
                }
            }
        }
        /*
         * for (JButton[] jButtons : letterArray) { // resets the interactive keyboard
         * for (int h = 0; h < letterArray[0].length; h++) {
         * if (!(rowCounter == 3 && h <= 4)) {
         * jButtons[h].setEnabled(true); // enable pressing all buttons again
         * jButtons[h].setBackground(Color.BLUE);
         * }
         * }
         * rowCounter++;
         * }
         */
    }

    private void reset(String s) {
        // get new word from dictionary
        word = s;
        // reset incorrect guesses
        numIncorrect = 0;
        // reset the gallows
        ImageManager.updateImage(hangmanImage, HANGMAN_IMAGE);
        // reset hidden word
        hiddenWord.setText(WordManager.hideWord(word));
        // reset keyboard data
        inputLetters = word + "\n";
        // reset game status
        this.STATUS.setText("RUNNING...");
        // reset the keyboard
        for (int w = 0; w < letterArray.length; w++) {
            for (int h = 0; h < letterArray[0].length; h++) {
                if (w < 3 || (w == 3 && h <= 4)) {
                    letterArray[w][h].setBackground(Color.BLUE);
                    letterArray[w][h].setEnabled(true);
                }
            }
        }
        /*
         * for (JButton[] jButtons : letterArray) { // resets the interactive keyboard
         * for (int h = 0; h < letterArray[0].length; h++) {
         * if (!(rowCounter == 3 && h <= 4)) {
         * jButtons[h].setEnabled(true); // enable pressing all buttons again
         * jButtons[h].setBackground(Color.BLUE);
         * }
         * }
         * rowCounter++;
         * }
         */
    }

    /*
     * Constructs the pop-up menu that displays after the player either wins or
     * loses game
     */
    private void createResult() {
        dialog = new JDialog();
        dialog.setTitle("RESULT");
        dialog.setSize(DIALOG_DIMENSIONS);
        dialog.getContentPane().setBackground(Color.BLACK);
        dialog.setLocationRelativeTo(this);
        dialog.setModal(true);
        dialog.setLayout(new GridLayout(3, 1));
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                reset();
            }
        });

        result = new JLabel();
        result.setForeground(Color.YELLOW);
        result.setHorizontalAlignment(SwingConstants.CENTER);

        wordLabel = new JLabel();
        wordLabel.setForeground(Color.YELLOW);
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton restart = new JButton("RESTART?");
        restart.setForeground(Color.RED);
        restart.setBackground(Color.BLACK);
        restart.addActionListener(this);

        // add to menu
        dialog.add(result);
        dialog.add(wordLabel);
        dialog.add(restart);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("SAVE")) {
            save(this);
        } else if (command.equals("LOAD")) {
            load(this);

        } else {
            // reset
            if (command.equals("RESET") || command.equals("RESTART?")) {
                if (command.equals("RESTART?")) {
                    dialog.setVisible(false);
                }
                reset();
            } else if (command.equals("END")) {
                dispose();
            } else {
                JButton j = (JButton) e.getSource();
                // set keyboard button to invalid after clicked
                j.setEnabled(false);
                inputLetters += j.getText() + "\n";
                if (word.contains(command)) { // keeping track of the word based on player's input
                    j.setBackground(Color.GREEN); // hip hip hooray color
                    char[] hWord = hiddenWord.getText().toCharArray();
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == command.charAt(0)) {
                            hWord[i] = command.charAt(0);
                        }
                    }
                    hiddenWord.setText(String.valueOf(hWord));
                    if (!hiddenWord.getText().contains("?")) { // BOZO GOT IT RIGHT!!!
                        result.setText("DEATH IS EVADED!");
                        this.STATUS.setForeground(Color.GREEN);
                        this.STATUS.setText("YOU HAVE WON THE GAME"); // update game status
                        dialog.setVisible(true);
                    }
                } else { // YOU GOT IT WRONG BOZO!!!
                    j.setBackground(Color.RED); // nuh uh color
                    numIncorrect++; // increase incorrect guess counter
                    // update hangman image to include another part of poor John Guilty
                    ImageManager.updateImage(hangmanImage, "files/" + numIncorrect + ".png");
                    if (numIncorrect >= 6) {
                        result.setText("DEATH IS INESCAPABLE!");
                        this.STATUS.setForeground(Color.RED);
                        this.STATUS.setText("YOU HAVE LOST THE GAME"); // update game status
                        dialog.setVisible(true);
                    }
                } // always reveals what the word was regardless of win or loss at end of game
                wordLabel.setText("THE WORD WAS: " + word);
            }
        }
    }

    /*
     * Loads the saved game state
     */
    public void load(JFrame f) {
        Path p = Paths.get("files/gamestate.txt");
        List<String> data = new ArrayList<>();
        try {
            data = Files.readAllLines(p);
            // player loads file without having a game saved
            if (data.isEmpty()) {
                STATUS.setText("NO GAME PROGRESS");
            } else {
                String s = data.get(0); // first one is saved word
                reset(s);

                for (int w = 0; w < letterArray.length; w++) {
                    for (int h = 0; h < letterArray[0].length; h++) {
                        if (w < 3 || (w == 3 && h <= 4)) {
                            if (data.contains(letterArray[w][h].getText())) {
                                if (s.contains(letterArray[w][h].getText())) {
                                    letterArray[w][h].setEnabled(false);
                                    letterArray[w][h].setBackground(Color.GREEN);

                                    char[] hWord = hiddenWord.getText().toCharArray();
                                    for (int i = 0; i < word.length(); i++) {
                                        if (word.charAt(i) == letterArray[w][h].getText()
                                                .charAt(0)) {
                                            hWord[i] = letterArray[w][h].getText().charAt(0);
                                            ActionEvent e = new ActionEvent(
                                                    letterArray[w][h],
                                                    ActionEvent.ACTION_PERFORMED,
                                                    letterArray[w][h].getText().substring(0, 1)
                                            );
                                            actionPerformed(e);
                                        }
                                    }
                                } else {
                                    ActionEvent e = new ActionEvent(
                                            letterArray[w][h],
                                            ActionEvent.ACTION_PERFORMED,
                                            letterArray[w][h].getText().substring(0, 1)
                                    );
                                    actionPerformed(e);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(f, "FILEPATH INVALID");
            System.out.println("FILENAME INVALID");
            STATUS.setText("FILENAME INVALID");
        }
    }

    /*
     * Saves all necessary game data to the text file gamestate.txt game data
     * includes the
     * word for this specific game round as well as all letters the player has
     * clicked
     */
    public void save(JFrame f) {
        try {
            FileWriter fw = new FileWriter("files/gamestate.txt");
            String inputs = inputLetters.substring(0, inputLetters.length() - 1);
            fw.write(inputs);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(f, "FILEPATH INVALID");
            System.out.println("FILEPATH INVALID");
            STATUS.setText("FILEPATH INVALID");
            e.printStackTrace();
        }
    }
}
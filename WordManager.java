package org.cis1200.hangman;

import java.util.ArrayList;
import java.io.*;
import java.util.Random;

public class WordManager {
    public static ArrayList<String> wordsList;
    private BufferedReader br;
    private String line;

    /*
     * Populates ArrayList with words from the given word bank (in this case
     * dictionary)
     */
    public WordManager(String filePath) {
        try {
            if (filePath == null || filePath.equals("")) {
                throw new IllegalArgumentException("FILEPATH DOES NOT EXIST.");
            }
            // attempt to read file and populate word list
            wordsList = new ArrayList();
            // dictionary.txt is a 200 word bank of SAT vocabulary words
            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                wordsList.add(line); // add words from txt file using reader
            }
        } catch (IOException e) {
            // if dictionary unreadable or some error occurs, throw exception
            System.out.println("THERE IS AN ERROR.");
        }
    }

    /*
     * Retrieves a random word from the arraylist of words.
     */
    public String getRandomWord() {
        Random r = new Random();
        return wordsList.get(r.nextInt(wordsList.size())).toUpperCase();
        // all words pulled from the dictionary set to uppercase
    }

    /*
     * Hides word that is passed in behind number of dashes equivalent to
     * number of letters in word
     */
    public static String hideWord(String word) {
        if (word == null || word.equals("")) {
            throw new IllegalArgumentException("WORD INVALID.");
        }
        String hWord = "";
        for (int i = 0; i < word.length(); i++) {
            hWord += "?"; // hides word behind question marks
        }
        return hWord;
    }
}

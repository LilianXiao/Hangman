package org.cis1200.hangman;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class WordManagerTest {
    @Test
    public void wordManagerFilePathNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            String filePath = null;
            WordManager wm = new WordManager(filePath);
        });
    }

    @Test
    public void wordManagerFilePathEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            String filePath = "";
            WordManager wm = new WordManager(filePath);
        });
    }

    @Test
    public void wordManagerFilePathNonexistent() {
        String filePath = "files/bank.txt";
        WordManager wm = new WordManager(filePath);
        assertDoesNotThrow(() -> wm, "THERE IS AN ERROR.");
    }

    @Test
    public void wordManagerWordListCopiesCorrectly() {
        String filePath = "files/dictionary.txt";
        WordManager wm = new WordManager(filePath);
        assertDoesNotThrow(() -> wm);
    }

    @Test
    public void getRandomWordTest() {
        WordManager wm = new WordManager("files/dictionary.txt");
        String s = wm.getRandomWord().toLowerCase();
        assertTrue(WordManager.wordsList.contains(s));
    }

    @Test
    public void hideWordTestNormal() {
        WordManager wm = new WordManager("files/dictionary.txt");
        String s = wm.getRandomWord();
        String hiddenS = WordManager.hideWord(s);
        int i = s.length();
        String expected = "";
        for (int x = 0; x < i; x++) {
            expected += "?";
        }
        assertEquals(expected, hiddenS);
    }

    @Test
    public void hideWordTestWordNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            WordManager wm = new WordManager("files/dictionary.txt");
            String s = null;
            String hiddenS = wm.hideWord(s);
        });
    }

    @Test
    public void hideWordTestWordEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            WordManager wm = new WordManager("files/dictionary.txt");
            String s = "";
            String hiddenS = wm.hideWord(s);
        });
    }
}

package org.cis1200.hangman;

public class RunHangman implements Runnable {
    @Override
    public void run() {
        new Hangman("RUNNING...").setVisible(true);
    }
}

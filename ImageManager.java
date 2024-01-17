package org.cis1200.hangman;

import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;

public class ImageManager {
    private static BufferedImage bi;

    /*
     * Given the filepath of an image, this method creates a JLabel so it can be
     * easily added to the layout
     */
    public static JLabel imageLoader(String filePath) {
        // responsible for loading any images, if image doesn't exist, error handled
        if (filePath == null) {
            throw new IllegalArgumentException("FILEPATH DOES NOT EXIST.");
        }
        try {
            bi = ImageIO.read(new File(filePath));
            return new JLabel(new ImageIcon(bi));
        } catch (IOException e) {
            System.out.println("THERE IS AN ERROR.");
        }
        return null;
    }

    /*
     * Updates image of the gallows (for instance each time an incorrect guess is
     * made
     * or when the game is reset)
     */
    public static void updateImage(JLabel img, String filePath) {
        try {
            if (filePath == null || filePath.equals("")) { // filepath must exist and be nonempty
                throw new IllegalArgumentException("FILEPATH DOES NOT EXIST.");
            }
            bi = ImageIO.read(new File(filePath));
            img.setIcon(new ImageIcon(bi));
        } catch (IOException e) {
            System.out.println("THERE IS AN ERROR.");
        }
    }
}
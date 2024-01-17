package org.cis1200.hangman;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;

public class ImageManagerTest {

    @Test
    public void imageLoaderFilePathNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            String filePath = null;
            JLabel img = ImageManager.imageLoader(filePath);
        });
    }

    @Test
    public void imageLoaderFilePathNormal() {
        String filePath = "files/0.png";
        JLabel img = ImageManager.imageLoader(filePath);
        assertDoesNotThrow(() -> img);
    }

    @Test
    public void imageLoaderIOExceptionFileNonexistent() {
        String filePath = "files/silly.png"; // this file obviously doesn't exist in files
        JLabel img = ImageManager.imageLoader(filePath);
        assertDoesNotThrow(() -> img, "THERE IS AN ERROR.");
    }

    @Test
    public void imageLoaderFilePathEmpty() {
        String filePath = "";
        JLabel img = ImageManager.imageLoader(filePath);
        assertNull(img);
    }

    @Test
    public void updateImageSuccessful() {
        String path = "files/0.png";
        JLabel img = new JLabel();
        assertDoesNotThrow(() -> ImageManager.updateImage(img, path));
    }

    @Test
    public void updateImageFilePathNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            String path = null;
            JLabel img = new JLabel();
            ImageManager.updateImage(img, path);
        });
    }

    @Test
    public void updateImageFilePathEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            String path = "";
            JLabel img = new JLabel();
            ImageManager.updateImage(img, path);
        });
    }

    @Test
    public void updateImageIOExceptionFileNonexistent() {
        String path = "files/silly.png";
        JLabel img = new JLabel();
        assertDoesNotThrow(() -> ImageManager.updateImage(img, path), "THERE IS AN ERROR.");
    }
}

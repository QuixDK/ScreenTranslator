package ru.ssugt.capture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ScreenCapture {

    public void getScreenshot(int x, int y, int width, int height, String path) {
        try {
            Robot r = new Robot();
            Rectangle captureArea = new Rectangle(x, y, width, height);
            BufferedImage Image = r.createScreenCapture(captureArea);
            ImageIO.write(Image, "jpg", new File(path));
        }
        catch ( Exception e ) {
            System.out.println("Ошибка " + e.toString());
        }
    }
}

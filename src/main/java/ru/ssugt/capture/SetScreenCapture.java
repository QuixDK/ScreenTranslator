package ru.ssugt.capture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;

import org.apache.commons.codec.binary.Base64;

public class SetScreenCapture {

    public byte[] getScreenshot(double x, double y, double width, double height, String path) {

        try {
            Robot r = new Robot();
            Rectangle captureArea = new Rectangle((int) x, (int) y, (int) width, (int) height);
            BufferedImage Image = r.createScreenCapture(captureArea);
            File screenshot = new File(path);
            ImageIO.write(Image, "jpg", screenshot);
            return Base64.encodeBase64(Files.readAllBytes(screenshot.toPath()));
        }

        catch ( Exception e ) {
            System.out.println("Ошибка " + e.toString());
            return null;
        }
    }
}

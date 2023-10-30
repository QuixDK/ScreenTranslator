package ru.ssugt.capture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.codec.binary.Base64;

public class SetScreenCapture {

    public byte[] getScreenshot(SetRectangle rectangle, String path) throws IOException, AWTException {
            Robot r = new Robot();
            Rectangle captureArea = new Rectangle(rectangle.x(), rectangle.y(), rectangle.width(), rectangle.height());
            BufferedImage Image = r.createScreenCapture(captureArea);
            File screenshot = new File(path);
            ImageIO.write(Image, "jpg", screenshot);
            return Base64.encodeBase64(Files.readAllBytes(screenshot.toPath()));
    }
}

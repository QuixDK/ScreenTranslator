package ru.ssugt.capture;

import ru.ssugt.integration.easyOCR.OCR.TextRecognize;

import java.awt.*;

public class ScreenCapture {

    TextRecognize textRecognize = new TextRecognize();

    public void getScreenshot(int x, int y, int width, int height, String path) {
        try {
            Robot r = new Robot();
            //Rectangle captureArea = new Rectangle(x, y, width, height);
            //BufferedImage Image = r.createScreenCapture(captureArea);
            //ImageIO.write(Image, "jpg", new File(path));

            //Тут чтонибудь для сравнения скриншотов
            textRecognize.recognize(path);

        }
        catch ( Exception e ) {
            System.out.println("Ошибка " + e.toString());
        }
    }
}

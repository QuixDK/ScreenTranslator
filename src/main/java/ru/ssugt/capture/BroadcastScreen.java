package ru.ssugt.capture;

import ru.ssugt.integration.easyOCR.EasyOCRVision;
import ru.ssugt.integration.tesseractOCR.TesseractOCRVision;
import ru.ssugt.integration.yandex.vision.YandexVisionApi;

import java.util.ArrayList;
import java.util.List;

public class BroadcastScreen  {

    private final double x;
    private final double y;
    private final double width;
    private final double height;


    private byte[] prevPicture;
    private StringBuilder text;


    public BroadcastScreen(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }
    public byte[] getFrame() {
        SetScreenCapture screenCapture = new SetScreenCapture();
        byte[] pictureInBase64 = screenCapture.getScreenshot(x, y, width, height, "testscreen.jpg");
        if ( pictureInBase64 != prevPicture ) {
            prevPicture = pictureInBase64;
        }
        return pictureInBase64;
    }

}

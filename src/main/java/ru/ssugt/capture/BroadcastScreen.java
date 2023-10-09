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
    private final YandexVisionApi yandexVisionApi;

    private byte[] prevPicture;
    private StringBuilder text;


    public BroadcastScreen(double x, double y, double width, double height, YandexVisionApi yandexVisionApi) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.yandexVisionApi = yandexVisionApi;

    }
    public String getFrame() {
        SetScreenCapture screenCapture = new SetScreenCapture();
        byte[] pictureInBase64 = screenCapture.getScreenshot(x, y, width, height, "testscreen.jpg");
        if ( pictureInBase64 != prevPicture ) {
            List<String> languageCodes = new ArrayList<>();
            languageCodes.add("*");

                text = yandexVisionApi.recognizeText("JPEG", languageCodes, "page", pictureInBase64);

            if (text != null) {
                text = new StringBuilder(text.toString());
                return text.toString();
            }
        }
        prevPicture = pictureInBase64;
        if (text != null) {
            return text.toString();
        }
        return null;
    }

}

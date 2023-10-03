package ru.ssugt.capture;

import ru.ssugt.integration.yandex.vision.YandexVisionApi;

import java.util.ArrayList;
import java.util.List;

public class BroadcastScreen implements Runnable{

    double x;
    double y;
    double width;
    double height;

    YandexVisionApi yandexVisionApi;

    public BroadcastScreen(double x, double y, double width, double height, YandexVisionApi yandexVisionApi) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.yandexVisionApi = yandexVisionApi;
    }
    @Override
    public void run() {
        SetScreenCapture screenCapture = new SetScreenCapture();
        byte[] pictureInBase64 = screenCapture.getScreenshot(x, y, width, height, "testscreen.jpg");
        List<String> languageCodes = new ArrayList<>();
        languageCodes.add("*");
        yandexVisionApi.recognizeText("JPEG", languageCodes, "page", pictureInBase64);
    }

}

package ru.ssugt.threads.OCR;

import lombok.Getter;
import ru.ssugt.capture.SetScreenCapture;
import ru.ssugt.threads.DoneSignal;
import ru.ssugt.capture.SetRectangle;
import ru.ssugt.integration.yandex.vision.YandexVisionApi;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ThreadForYandexOCR extends Thread implements Runnable {

    private final SetRectangle setRectangle;
    private final YandexVisionApi yandexVisionApi;
    private final DoneSignal doneSignal;

    @Getter
    private byte[] pictureInBase64;

    @Getter
    private String recognizedText;

    public ThreadForYandexOCR(SetRectangle setRectangle, YandexVisionApi yandexVisionApi, DoneSignal doneSignal) {
        this.yandexVisionApi = yandexVisionApi;
        this.setRectangle = setRectangle;
        this.doneSignal = doneSignal;
    }

    @Override
    public void run() {
        byte[] prevPicture = null;
        List<String> languageCodes = new ArrayList<>();
        SetScreenCapture screenCapture = new SetScreenCapture();
        languageCodes.add("*");
        try {
            while (true) {
                Path currRelativePath = Paths.get("");
                pictureInBase64 = screenCapture.getScreenshot(setRectangle, currRelativePath + "src/main/resources/temp/testscreen.jpg");
                if ( isAnotherPicture(prevPicture, pictureInBase64) ) {
                    recognizedText = yandexVisionApi.recognizeText("JPEG", languageCodes, "page", pictureInBase64);
                    System.out.println("YandexOCR recognized text");
                }
                if ( recognizedText == null ) {
                    continue;
                }
                prevPicture = pictureInBase64;
                try {
                    doneSignal.getDoneSignal().countDown();
                    doneSignal.getDoneSignal().await();
                    Thread.sleep(1);
                } catch ( InterruptedException e ) {
                    break;
                }
            }
        } catch ( AWTException | IOException e ) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAnotherPicture(byte[] prevPicture, byte[] pictureInBase64) {
        boolean flag = true;
        if ( prevPicture == null ) {
            return true;
        }

        if ( prevPicture.length == pictureInBase64.length ) {
            for ( int i = 0; i < pictureInBase64.length; i++ ) {
                if ( prevPicture[i] != pictureInBase64[i] ) {
                    flag = false;
                    break;
                }
            }
        }
        //System.out.println(prevPicture.length);
        //System.out.println(pictureInBase64.length);
        return flag;
    }
}

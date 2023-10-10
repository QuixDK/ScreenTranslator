package ru.ssugt.threads.OCR;

import lombok.Getter;
import ru.ssugt.DoneSignal;
import ru.ssugt.capture.BroadcastScreen;
import ru.ssugt.integration.yandex.vision.YandexVisionApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ThreadForYandexOCR extends Thread implements Runnable{

    private final BroadcastScreen broadcastScreen;
    private final YandexVisionApi yandexVisionApi;
    private DoneSignal doneSignal;

    @Getter
    private String recognizedText;
    public ThreadForYandexOCR(BroadcastScreen broadcastScreen, YandexVisionApi yandexVisionApi, DoneSignal doneSignal) {
        this.yandexVisionApi = yandexVisionApi;
        this.broadcastScreen = broadcastScreen;
        this.doneSignal = doneSignal;
    }
    @Override
    public void run() {
        byte[] prevPicture = null;
        List<String> languageCodes = new ArrayList<>();
        languageCodes.add("*");
        while (true) {

            byte[] pictureInBase64 = broadcastScreen.getFrame();

            boolean flag = true;
            if ( prevPicture != null ) {
                if ( prevPicture.length == pictureInBase64.length ) {
                    for ( int i = 0; i < pictureInBase64.length; i++ ) {
                        if ( prevPicture[i] != pictureInBase64[i] ) {
                            flag = false;
                            break;
                        }
                    }
                }
            }

            if ( flag ) {
                recognizedText = yandexVisionApi.recognizeText("JPEG", languageCodes, "page", pictureInBase64);
            }
            prevPicture = pictureInBase64;

            if ( recognizedText != null && !recognizedText.equals("") ) {
                //textForm.setTranslatedText(recognizedText, yandexTranslateApi, chooseSourceLanguageComboBox, chooseTargetLanguageComboBox);
            }
            System.out.println("YandexOCR recognized text");
            try {
                doneSignal.getDoneSignal().countDown();
                doneSignal.getDoneSignal().await();
                Thread.sleep(3000);
            } catch ( InterruptedException e ) {
                break;
            }
        }
    }
}

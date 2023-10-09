package ru.ssugt.threads;

import ru.ssugt.integration.easyOCR.EasyOCRVision;

import java.util.concurrent.CountDownLatch;

public class ThreadForEasyOCR extends Thread implements Runnable {

    private final EasyOCRVision easyOCRVision;
    private final CountDownLatch doneSignal;

    public ThreadForEasyOCR(EasyOCRVision easyOCRVision, CountDownLatch doneSignal) {
        this.easyOCRVision = easyOCRVision;
        this.doneSignal = doneSignal;

    }

    @Override
    public void run() {
        while (true) {

            String recognizedText = easyOCRVision.recognizeText("D:\\Java Projects\\ScreenTranslator\\testscreen.jpg");

            if ( recognizedText != null && !recognizedText.equals("") ) {
                // textForm.setTranslatedText(recognizedText, yandexTranslateApi, chooseSourceLanguageComboBox, chooseTargetLanguageComboBox);
            }
            System.out.println("EasyOCR recognized text");
            try {
                Thread.sleep(1);
                doneSignal.countDown();
                doneSignal.await();
            } catch ( InterruptedException e ) {
                break;
            }
        }
    }
}

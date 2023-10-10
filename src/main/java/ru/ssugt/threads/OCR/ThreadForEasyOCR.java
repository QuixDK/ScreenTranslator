package ru.ssugt.threads.OCR;

import lombok.Getter;
import ru.ssugt.DoneSignal;
import ru.ssugt.integration.easyOCR.EasyOCRVision;

import java.util.concurrent.CountDownLatch;

public class ThreadForEasyOCR extends Thread implements Runnable {

    private final EasyOCRVision easyOCRVision;
    private final DoneSignal doneSignal;

    @Getter
    private String recognizedText;

    public ThreadForEasyOCR(EasyOCRVision easyOCRVision, DoneSignal doneSignal) {
        this.easyOCRVision = easyOCRVision;
        this.doneSignal = doneSignal;

    }

    @Override
    public void run() {
        while (true) {

            recognizedText = easyOCRVision.recognizeText("D:\\Java Projects\\ScreenTranslator\\testscreen.jpg");

            if ( recognizedText != null && !recognizedText.equals("") ) {
                // textForm.setTranslatedText(recognizedText, yandexTranslateApi, chooseSourceLanguageComboBox, chooseTargetLanguageComboBox);
            }
            System.out.println("EasyOCR recognized text");
            try {
                doneSignal.getDoneSignal().countDown();
                doneSignal.getDoneSignal().await();
                Thread.sleep(100);
            } catch ( InterruptedException e ) {
                break;
            }
        }
    }
}

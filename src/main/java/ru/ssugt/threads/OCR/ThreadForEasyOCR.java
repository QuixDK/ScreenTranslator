package ru.ssugt.threads.OCR;

import lombok.Getter;
import ru.ssugt.threads.DoneSignal;
import ru.ssugt.integration.easyOCR.EasyOCRVision;

public class ThreadForEasyOCR extends Thread implements Runnable {

    private final EasyOCRVision easyOCRVision;
    private final DoneSignal doneSignal;

    @Getter
    private String recognizedText;

    private final String sourceLang;

    public ThreadForEasyOCR(EasyOCRVision easyOCRVision, DoneSignal doneSignal, String sourceLang) {
        this.easyOCRVision = easyOCRVision;
        this.doneSignal = doneSignal;
        this.sourceLang = sourceLang;

    }

    @Override
    public void run() {
        while (true) {

            recognizedText = easyOCRVision.recognizeText("D:\\Java Projects\\ScreenTranslator\\testscreen.jpg", sourceLang);

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

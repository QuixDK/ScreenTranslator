package ru.ssugt.threads;

import ru.ssugt.integration.easyOCR.EasyOCRVision;
import ru.ssugt.integration.tesseractOCR.TesseractOCRVision;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ThreadForTesseractOCR extends Thread implements Runnable {

    private final TesseractOCRVision tesseractOCRVision;
    private final CountDownLatch doneSignal;
    public ThreadForTesseractOCR(TesseractOCRVision tesseractOCRVision, CountDownLatch doneSignal) {
        this.tesseractOCRVision = tesseractOCRVision;
        this.doneSignal = doneSignal;
    }
    @Override
    public void run() {
        while (true) {

            List<String> languageCodes = new ArrayList<>();
            languageCodes.add("rus");
            languageCodes.add("eng");
            String recognizedText = tesseractOCRVision.recognizeText("D:\\Java Projects\\ScreenTranslator\\testscreen.jpg", languageCodes);

            if ( recognizedText != null && !recognizedText.equals("") ) {
                // textForm.setTranslatedText(recognizedText, yandexTranslateApi, chooseSourceLanguageComboBox, chooseTargetLanguageComboBox);
            }
            System.out.println("TesseractOCR recognized text\n");

            try {
                doneSignal.countDown();
                doneSignal.await();
                Thread.sleep(2000);
            } catch ( InterruptedException e ) {
                break;
            }
        }
    }
}

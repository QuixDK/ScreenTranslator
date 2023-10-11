package ru.ssugt.threads.OCR;

import lombok.Getter;
import ru.ssugt.threads.DoneSignal;
import ru.ssugt.integration.tesseractOCR.TesseractOCRVision;

import java.util.ArrayList;
import java.util.List;

public class ThreadForTesseractOCR extends Thread implements Runnable {
    private final TesseractOCRVision tesseractOCRVision;
    private final DoneSignal doneSignal;

    @Getter
    private String recognizedText;
    public ThreadForTesseractOCR(TesseractOCRVision tesseractOCRVision, DoneSignal doneSignal) {
        this.tesseractOCRVision = tesseractOCRVision;
        this.doneSignal = doneSignal;
    }
    @Override
    public void run() {
        while (true) {
            List<String> languageCodes = new ArrayList<>();
            languageCodes.add("rus");
            languageCodes.add("eng");
            recognizedText = tesseractOCRVision.recognizeText("D:\\Java Projects\\ScreenTranslator\\testscreen.jpg", languageCodes);
            System.out.println("TesseractOCR recognized text");
            try {
                doneSignal.getDoneSignal().countDown();
                doneSignal.getDoneSignal().await();
                Thread.sleep(1);
            } catch ( InterruptedException e ) {
                break;
            }
        }
    }
}

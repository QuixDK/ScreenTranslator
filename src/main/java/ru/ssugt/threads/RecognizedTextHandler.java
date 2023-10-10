package ru.ssugt.threads;

import ru.ssugt.DoneSignal;
import ru.ssugt.threads.OCR.ThreadForEasyOCR;
import ru.ssugt.threads.OCR.ThreadForTesseractOCR;
import ru.ssugt.threads.OCR.ThreadForYandexOCR;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class RecognizedTextHandler extends Thread implements Runnable {
    private final DoneSignal doneSignal;
    private final ThreadForTesseractOCR threadForTesseractOCR;
    private final ThreadForEasyOCR threadForEasyOCR;
    private final ThreadForYandexOCR threadForYandexOCR;

    public RecognizedTextHandler(DoneSignal doneSignal, ThreadForTesseractOCR threadForTesseractOCR, ThreadForEasyOCR threadForEasyOCR, ThreadForYandexOCR threadForYandexOCR) {
        this.doneSignal = doneSignal;
        this.threadForTesseractOCR = threadForTesseractOCR;
        this.threadForEasyOCR = threadForEasyOCR;
        this.threadForYandexOCR = threadForYandexOCR;
    }

    @Override
    public void run() {
        while (true) {
            if ( doneSignal.getDoneSignal().getCount() == 1 ) {
                String tesseractRecognizedText = threadForTesseractOCR.getRecognizedText();
                String easyRecognizedText = threadForEasyOCR.getRecognizedText();
                String yandexRecognizedText = threadForYandexOCR.getRecognizedText();
                String s = null;
                StringBuilder result = new StringBuilder();
                try {
//                    Process p = Runtime.getRuntime().exec("python pyScripts\\main2.py");
//
//                    BufferedReader stdInput = new BufferedReader(new
//                            InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
//
//                    BufferedReader stdError = new BufferedReader(new
//                            InputStreamReader(p.getErrorStream()));
//
//                    while ((s = stdInput.readLine()) != null) {
//                        result.append(s);
//                    }
                    System.out.println("ChatGPT choosed the best text");
                } catch ( Exception ex ) {

                }
                try {
                    doneSignal.getDoneSignal().countDown();
                    doneSignal.setDoneSignal(new CountDownLatch(4));
                    Thread.sleep(1);
                } catch ( InterruptedException ex ) {
                    break;
                }
            }

        }

    }
}

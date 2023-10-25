package ru.ssugt.threads;

import ru.ssugt.forms.TranslatedTextForm;
import ru.ssugt.integration.ScriptHandler;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;
import ru.ssugt.threads.OCR.ThreadForEasyOCR;
import ru.ssugt.threads.OCR.ThreadForTesseractOCR;
import ru.ssugt.threads.OCR.ThreadForYandexOCR;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

public class RecognizedTextHandler extends Thread implements Runnable {
    private final DoneSignal doneSignal;
    private final ThreadForTesseractOCR threadForTesseractOCR;
    private final ThreadForEasyOCR threadForEasyOCR;
    private final ThreadForYandexOCR threadForYandexOCR;
    private final YandexTranslateApi yandexTranslateApi;
    private final String sourceLang;
    private final String targetLang;
    private TranslatedTextForm textForm;

    public RecognizedTextHandler(DoneSignal doneSignal, ThreadForTesseractOCR threadForTesseractOCR, ThreadForEasyOCR threadForEasyOCR, ThreadForYandexOCR threadForYandexOCR, YandexTranslateApi yandexTranslateApi,
                                 String sourceLang, String targetLang) {
        this.doneSignal = doneSignal;
        this.threadForTesseractOCR = threadForTesseractOCR;
        this.threadForEasyOCR = threadForEasyOCR;
        this.threadForYandexOCR = threadForYandexOCR;
        this.yandexTranslateApi = yandexTranslateApi;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
    }

    @Override
    public void run() {
        textForm = new TranslatedTextForm();
        SwingUtilities.invokeLater(textForm);
        ScriptHandler scriptHandler = new ScriptHandler();
        while (true) {
            try {
                if ( doneSignal.getDoneSignal().getCount() != 1 ) {
                    continue;
                }

                String tesseractRecognizedText = threadForTesseractOCR.getRecognizedText();
                String easyRecognizedText = threadForEasyOCR.getRecognizedText();
                String yandexRecognizedText = threadForYandexOCR.getRecognizedText();
                System.out.println(yandexRecognizedText);

                String command = "python pyScripts\\selectBestText.py \"Выбери лучший по смыслу текст из приведенных и напиши его в ответе без цифры:\" \"" + tesseractRecognizedText + "\" \""
                        + easyRecognizedText + "\" \"" + yandexRecognizedText + "\"";

                String result = yandexTranslateApi.getTranslatedText(scriptHandler.executeScript(command), sourceLang, targetLang);

                if (!yandexRecognizedText.equals("")) {
                    String translatedText = yandexTranslateApi.getTranslatedText(result, sourceLang, targetLang);
                    textForm.setTranslatedText(translatedText);
                }

                System.out.println("ChatGPT chose the best text");
                doneSignal.getDoneSignal().countDown();
                doneSignal.setDoneSignal(new CountDownLatch(4));
                Thread.sleep(1);
            }
            catch ( InterruptedException ex ) {
                break;
            }

        }
    }
}

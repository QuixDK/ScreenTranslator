package ru.ssugt.threads;

import ru.ssugt.forms.MainForm;
import ru.ssugt.forms.TranslatedTextForm;
import ru.ssugt.i18n.SupportedLanguages;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;
import ru.ssugt.threads.OCR.ThreadForEasyOCR;
import ru.ssugt.threads.OCR.ThreadForTesseractOCR;
import ru.ssugt.threads.OCR.ThreadForYandexOCR;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
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
        while (true) {
            if ( doneSignal.getDoneSignal().getCount() == 1 ) {
                String tesseractRecognizedText = threadForTesseractOCR.getRecognizedText();
                String easyRecognizedText = threadForEasyOCR.getRecognizedText();
                String yandexRecognizedText = threadForYandexOCR.getRecognizedText();


                String s = null;

                StringBuilder result = new StringBuilder();
                try {
                    System.out.println(tesseractRecognizedText);
                    System.out.println(easyRecognizedText);
                    System.out.println(yandexRecognizedText);

                    Process p = Runtime.getRuntime().exec("python pyScripts\\selectBestText.py \"Выбери лучший по смыслу текст из приведенных и напиши его в ответе:\" \"" + tesseractRecognizedText + "\" \""
                            + easyRecognizedText + "\" \"" + yandexRecognizedText + "\"");

                    BufferedReader stdInput = new BufferedReader(new
                            InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));

                    BufferedReader stdError = new BufferedReader(new
                            InputStreamReader(p.getErrorStream()));

                    while ((s = stdInput.readLine()) != null) {
                        result.append(s);
                    }
                    String translatedText = yandexTranslateApi.getTranslatedText(result.toString(), sourceLang, targetLang);
                    textForm.setTranslatedText(translatedText);
                    while ((s = stdError.readLine()) != null) {
                        System.out.println(s);
                    }
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

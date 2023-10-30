package ru.ssugt.threads;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import ru.ssugt.capture.SetRectangle;
import ru.ssugt.config.HibernateUtil;
import ru.ssugt.forms.TranslatedTextForm;
import ru.ssugt.integration.ScriptHandler;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;
import ru.ssugt.repository.RecognizedTextRepository;
import ru.ssugt.repository.RecognizedTextRepositoryImpl;
import ru.ssugt.service.RecognizedTextService;
import ru.ssugt.service.RecognizedTextServiceImpl;
import ru.ssugt.threads.OCR.ThreadForEasyOCR;
import ru.ssugt.threads.OCR.ThreadForTesseractOCR;
import ru.ssugt.threads.OCR.ThreadForYandexOCR;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

@AllArgsConstructor
public class RecognizedTextHandler extends Thread implements Runnable {
    private final DoneSignal doneSignal;
    private final ThreadForTesseractOCR threadForTesseractOCR;
    private final ThreadForEasyOCR threadForEasyOCR;
    private final ThreadForYandexOCR threadForYandexOCR;
    private final YandexTranslateApi yandexTranslateApi;
    private final String sourceLang;
    private final String targetLang;
    private final SetRectangle rectangle;
    private final byte[] base64Picture;

    @Override
    public void run() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        RecognizedTextRepository recognizedTextRepository = new RecognizedTextRepositoryImpl(sessionFactory);
        RecognizedTextService recognizedTextService = new RecognizedTextServiceImpl(recognizedTextRepository);
        TranslatedTextForm textForm = new TranslatedTextForm(rectangle);
        SwingUtilities.invokeLater(textForm);
        while (true) {
            try {
                if ( doneSignal.getDoneSignal().getCount() != 1 ) {
                    continue;
                }

                String tesseractRecognizedText = threadForTesseractOCR.getRecognizedText();
                String easyRecognizedText = threadForEasyOCR.getRecognizedText();
                String yandexRecognizedText = threadForYandexOCR.getRecognizedText();

                if ( !yandexRecognizedText.equals("") ) {
                    String translatedText = yandexTranslateApi.getTranslatedText(yandexRecognizedText, sourceLang, targetLang);
                    textForm.setTranslatedText(translatedText);
                }
                recognizedTextService.saveText(yandexRecognizedText, tesseractRecognizedText, easyRecognizedText, base64Picture, null);

                doneSignal.getDoneSignal().countDown();
                doneSignal.setDoneSignal(new CountDownLatch(4));
                Thread.sleep(1);
            } catch ( InterruptedException ex ) {
                break;
            }

        }
    }
}

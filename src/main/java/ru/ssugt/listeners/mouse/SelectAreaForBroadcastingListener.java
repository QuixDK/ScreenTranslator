package ru.ssugt.listeners.mouse;

import ru.ssugt.capture.SetScreenCapture;
import ru.ssugt.config.YandexConfigProperties;
import ru.ssugt.forms.MainForm;
import ru.ssugt.i18n.SupportedLanguages;
import ru.ssugt.listeners.action.VoiceRecognizeListener;
import ru.ssugt.service.RecognizedTextService;
import ru.ssugt.threads.DoneSignal;
import ru.ssugt.capture.SetRectangle;
import ru.ssugt.integration.easyOCR.EasyOCRVision;
import ru.ssugt.integration.tesseractOCR.TesseractOCRVision;
import ru.ssugt.threads.OCR.ThreadForEasyOCR;
import ru.ssugt.threads.OCR.ThreadForTesseractOCR;
import ru.ssugt.threads.OCR.ThreadForYandexOCR;
import ru.ssugt.threads.RecognizedTextHandler;
import ru.ssugt.threads.voice.ThreadForVoiceRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class SelectAreaForBroadcastingListener implements MouseListener {

    double x = 0;
    double y = 0;
    private SetRectangle rectangle;
    private List<Thread> threadList;
    private final JFrame areaForTranslation;
    private final EasyOCRVision easyOCRVision = new EasyOCRVision();
    private final TesseractOCRVision tesseractOCRVision = new TesseractOCRVision();
    private final YandexConfigProperties yandexConfigProperties;
    private final MainForm mainForm;



    public SelectAreaForBroadcastingListener(List<Thread> threadList, JFrame areaForTranslation, YandexConfigProperties yandexConfigProperties, MainForm mainForm) {
        this.threadList = threadList;
        this.areaForTranslation = areaForTranslation;
        this.yandexConfigProperties = yandexConfigProperties;
        this.mainForm = mainForm;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = MouseInfo.getPointerInfo().getLocation().getX();
        y = MouseInfo.getPointerInfo().getLocation().getY();
        System.out.println("Клик мышкой " + x + " " + y);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        areaForTranslation.setVisible(false);
        double x2 = MouseInfo.getPointerInfo().getLocation().getX();
        double y2 = MouseInfo.getPointerInfo().getLocation().getY();
        double width;
        double height;
        if ( x2 >= x ) {
            width = x2 - x;
        } else {
            width = x - x2;
            x = x2;
        }
        if ( y2 >= y ) {
            height = y2 - y;
        } else {
            height = y - y2;
            y = y2;
        }
        String sourceLang = ((SupportedLanguages) Objects.requireNonNull(mainForm.getChooseSourceLanguageComboBox().getSelectedItem())).code;
        String targetLang = ((SupportedLanguages) Objects.requireNonNull(mainForm.getChooseTargetLanguageComboBox().getSelectedItem())).code;
        System.out.println("Мышка отпущена " + x + " " + y + " " + width + " " + height);
        rectangle = new SetRectangle((int) x, (int) y, (int) width, (int) height);
        DoneSignal doneSignal = new DoneSignal();

        threadList.set(0, new ThreadForYandexOCR(rectangle, yandexConfigProperties.getYandexVisionApi(), doneSignal));
        threadList.set(1, new ThreadForEasyOCR(easyOCRVision, doneSignal, sourceLang));
        threadList.set(2, new ThreadForTesseractOCR(tesseractOCRVision, doneSignal));
        threadList.set(3, new RecognizedTextHandler(doneSignal, (ThreadForTesseractOCR) threadList.get(2),
                (ThreadForEasyOCR) threadList.get(1), (ThreadForYandexOCR) threadList.get(0),
                yandexConfigProperties.getYandexTranslateApi(), sourceLang, targetLang, rectangle, threadList));
        for ( Thread t: threadList ) {
            if (t instanceof ThreadForVoiceRecord ) {
                continue;
            }
            t.start();
        }

    }


    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

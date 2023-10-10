package ru.ssugt.listeners.mouse;

import ru.ssugt.DoneSignal;
import ru.ssugt.capture.BroadcastScreen;
import ru.ssugt.integration.easyOCR.EasyOCRVision;
import ru.ssugt.integration.tesseractOCR.TesseractOCRVision;
import ru.ssugt.integration.yandex.vision.YandexVisionApi;
import ru.ssugt.threads.OCR.ThreadForEasyOCR;
import ru.ssugt.threads.OCR.ThreadForTesseractOCR;
import ru.ssugt.threads.OCR.ThreadForYandexOCR;
import ru.ssugt.threads.RecognizedTextHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SelectAreaForBroadcastingListener implements MouseListener {

    double x = 0;
    double y = 0;

    private BroadcastScreen broadcastScreen;
    private List<Thread> threadList;
    private final JFrame areaForTranslation;
    private final EasyOCRVision easyOCRVision = new EasyOCRVision();
    private final TesseractOCRVision tesseractOCRVision = new TesseractOCRVision();
    private final YandexVisionApi yandexVisionApi;



    public SelectAreaForBroadcastingListener(List<Thread> threadList, JFrame areaForTranslation, YandexVisionApi yandexVisionApi) {
        this.threadList = threadList;
        this.areaForTranslation = areaForTranslation;
        this.yandexVisionApi = yandexVisionApi;
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
        System.out.println("Мышка отпущена " + x + " " + y + " " + width + " " + height);
        broadcastScreen = new BroadcastScreen(x, y, width, height);
        DoneSignal doneSignal = new DoneSignal();
        threadList.set(0, new ThreadForYandexOCR(broadcastScreen, yandexVisionApi, doneSignal));
        threadList.set(1, new ThreadForEasyOCR(easyOCRVision, doneSignal));
        threadList.set(2, new ThreadForTesseractOCR(tesseractOCRVision, doneSignal));
        threadList.set(3, new RecognizedTextHandler(doneSignal, (ThreadForTesseractOCR) threadList.get(2), (ThreadForEasyOCR) threadList.get(1), (ThreadForYandexOCR) threadList.get(0)));
        for ( Thread t: threadList ) {
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

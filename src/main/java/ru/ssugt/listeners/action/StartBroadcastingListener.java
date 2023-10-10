package ru.ssugt.listeners.action;

import ru.ssugt.integration.yandex.vision.YandexVisionApi;
import ru.ssugt.listeners.mouse.SelectAreaForBroadcastingListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StartBroadcastingListener implements ActionListener {

    private final List<Thread> threadList;
    private final YandexVisionApi yandexVisionApi;

    public StartBroadcastingListener(List<Thread> threadList, YandexVisionApi yandexVisionApi) {
        this.threadList = threadList;

        this.yandexVisionApi = yandexVisionApi;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame areaForTranslation = new JFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        areaForTranslation.setSize(screenSize.width, screenSize.height);
        areaForTranslation.setLocation(screenSize.width / 2 - areaForTranslation.getSize().width / 2, screenSize.height / 2 - areaForTranslation.getSize().height / 2);
        areaForTranslation.setUndecorated(true);
        areaForTranslation.setOpacity(0.5f);
        areaForTranslation.setVisible(true);
        areaForTranslation.addMouseListener(new SelectAreaForBroadcastingListener(threadList, areaForTranslation, yandexVisionApi));
    }
}

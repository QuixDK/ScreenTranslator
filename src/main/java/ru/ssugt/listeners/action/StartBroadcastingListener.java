package ru.ssugt.listeners.action;

import lombok.AllArgsConstructor;
import ru.ssugt.config.YandexConfigProperties;
import ru.ssugt.forms.MainForm;
import ru.ssugt.listeners.mouse.SelectAreaForBroadcastingListener;
import ru.ssugt.service.RecognizedTextService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@AllArgsConstructor
public class StartBroadcastingListener implements ActionListener {

    private final List<Thread> threadList;
    private final YandexConfigProperties yandexConfigProperties;
    private final MainForm mainForm;
    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame areaForTranslation = new JFrame();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        areaForTranslation.setSize(screenSize.width, screenSize.height);
        areaForTranslation.setLocation(screenSize.width / 2 - areaForTranslation.getSize().width / 2, screenSize.height / 2 - areaForTranslation.getSize().height / 2);
        areaForTranslation.setUndecorated(true);
        areaForTranslation.setOpacity(0.5f);
        areaForTranslation.setVisible(true);
        areaForTranslation.addMouseListener(new SelectAreaForBroadcastingListener(threadList, areaForTranslation, yandexConfigProperties, mainForm));
    }
}

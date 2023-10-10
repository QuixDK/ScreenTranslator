package ru.ssugt.forms;

import ru.ssugt.i18n.SupportedLanguages;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;

import javax.swing.*;
import java.util.Objects;

public class TranslatedTextForm implements Runnable {
    private JTextArea areaForTranslatedText;
    private JPanel panel1;

    @Override
    public void run() {
        JFrame jFrame = new JFrame();
        jFrame.add(areaForTranslatedText);
        //jFrame.setUndecorated(true);
        //jFrame.setOpacity(0.5f);
        areaForTranslatedText.setWrapStyleWord(true);
        areaForTranslatedText.setLineWrap(true);
        jFrame.setAlwaysOnTop(true);
        jFrame.setSize(300, 300);
        jFrame.setVisible(true);
    }

    public void setTranslatedText(String text) {
        if (text != null) {
            areaForTranslatedText.setText(text);
        }
    }

}

package ru.ssugt.forms;

import ru.ssugt.capture.SetRectangle;
import ru.ssugt.i18n.SupportedLanguages;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;

import javax.swing.*;
import java.util.Objects;

public class TranslatedTextForm implements Runnable {

    private SetRectangle rectangle;

    public TranslatedTextForm(SetRectangle rectangle) {
        this.rectangle = rectangle;
    }
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
        jFrame.setLocation(rectangle.x(), rectangle.y()+rectangle.height());
        jFrame.setSize(rectangle.width(), rectangle.height()+25);
        jFrame.setVisible(true);
    }

    public void setTranslatedText(String text) {
        if (text != null) {
            areaForTranslatedText.setText(text);
        }
    }

}

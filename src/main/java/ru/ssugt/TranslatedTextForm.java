package ru.ssugt;

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

    public void setTranslatedText(String text, YandexTranslateApi yandexTranslateApi, JComboBox<SupportedLanguages> chooseSourceLanguageComboBox, JComboBox<SupportedLanguages> chooseTargetLanguageComboBox) {
        if (text == null) {
            return;
        }
        String sourceLang = ((SupportedLanguages) Objects.requireNonNull(chooseSourceLanguageComboBox.getSelectedItem())).code;
        String targetLang = ((SupportedLanguages) Objects.requireNonNull(chooseTargetLanguageComboBox.getSelectedItem())).code;
        String translatedText = yandexTranslateApi.getTranslatedText(text, sourceLang, targetLang);
        if ( translatedText != null ) {
            areaForTranslatedText.setText(translatedText);
        }
    }
}

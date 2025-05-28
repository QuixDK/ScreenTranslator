package ru.ssugt.listeners.action;


import ru.ssugt.capture.SetRectangle;
import ru.ssugt.forms.MainForm;
import ru.ssugt.forms.TranslatedTextForm;
import ru.ssugt.i18n.SupportedLanguages;
import ru.ssugt.integration.yandex.stt.YandexSTT;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;
import ru.ssugt.threads.voice.ThreadForVoiceRecord;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class VoiceRecognizeListener implements ActionListener {
    private final List<Thread> threadList;
    Path currRelativePath = Paths.get("");
    ThreadForVoiceRecord recorder = null;
    private final YandexTranslateApi yandexTranslateApi;

    private final MainForm mainForm;
    static final long RECORD_TIME = 5000;

    public VoiceRecognizeListener(List<Thread> threadList, YandexTranslateApi yandexTranslateApi, MainForm mainForm) {
        this.threadList = threadList;
        this.yandexTranslateApi =yandexTranslateApi;
        this.mainForm = mainForm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        YandexSTT yandexSTT = new YandexSTT();
        threadList.set(4, new ThreadForVoiceRecord());
//        while (true) {
            try {
                if ( threadList.get(4) instanceof ThreadForVoiceRecord ) {
                    recorder = (ThreadForVoiceRecord) threadList.get(4);
                }

                if ( !recorder.isAlive() ) { // Check if the thread is not running
                    Thread stopper = new Thread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(RECORD_TIME);
                            } catch ( InterruptedException ex ) {
                                // Handle the exception
                            }
                            recorder.finish();
                        }
                    });
                    stopper.start();
                    // Start recording
                    recorder.start();
                }

                try {
                    Thread.sleep(5000);
                } catch ( InterruptedException ex ) {

                }

                TranslatedTextForm textForm = new TranslatedTextForm(new SetRectangle(100, 100, 20, 30));
                String sourceLang = ((SupportedLanguages) Objects.requireNonNull(mainForm.getChooseSourceLanguageComboBox().getSelectedItem())).code;
                String targetLang = ((SupportedLanguages) Objects.requireNonNull(mainForm.getChooseTargetLanguageComboBox().getSelectedItem())).code;
                String yandexRecognizedText = yandexSTT.recognizeText("D:/Java Projects/ScreenTranslator/src/main/resources/temp/test.wav", sourceLang);

                if ( !yandexRecognizedText.equals("") ) {
                    String translatedText = yandexTranslateApi.getTranslatedText(yandexRecognizedText, sourceLang, targetLang);
                    textForm.setTranslatedText(translatedText);
                }
                SwingUtilities.invokeLater(textForm);
            }
            catch ( Exception ex ) {
                ex.printStackTrace();
            }
            //System.out.println(recognizedText);
        //}

    }

}

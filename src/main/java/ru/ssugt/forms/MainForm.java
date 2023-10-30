package ru.ssugt.forms;

import lombok.Getter;

import ru.ssugt.config.YandexConfigProperties;
import ru.ssugt.i18n.SupportedLanguages;
import ru.ssugt.listeners.action.StartBroadcastingListener;
import ru.ssugt.listeners.action.StopBroadcastingListener;
import ru.ssugt.listeners.action.VoiceRecognizeListener;
import ru.ssugt.threads.OCR.ThreadForEasyOCR;
import ru.ssugt.threads.OCR.ThreadForTesseractOCR;
import ru.ssugt.threads.OCR.ThreadForYandexOCR;
import ru.ssugt.threads.RecognizedTextHandler;
import ru.ssugt.threads.voice.ThreadForVoiceRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainForm implements Runnable {
    private JPanel MainPanel;
    @Getter
    private JComboBox<SupportedLanguages> chooseSourceLanguageComboBox;
    private JLabel sourceLanguage;
    private JLabel targetLanguage;
    @Getter
    private JComboBox<SupportedLanguages> chooseTargetLanguageComboBox;
    private JButton translateAreaButton;
    private JButton stopTranslating;
    private JButton startVoiceRecognizeButton;
    private JButton stopVoiceRecognize;
    private final ArrayList<SupportedLanguages> supportedLanguagesList = new ArrayList<>();
    public JFrame mainFrame = new JFrame();
    private ThreadForYandexOCR threadForYandexOCR;
    private ThreadForEasyOCR threadForEasyOCR;
    private ThreadForTesseractOCR threadForTesseractOCR;
    private RecognizedTextHandler recognizedTextHandler;
    private ThreadForVoiceRecord threadForVoiceRecord;

    private final List<Thread> threadList = new ArrayList<>();




    public void run() {
        YandexConfigProperties yandexConfigProperties = new YandexConfigProperties();
        yandexConfigProperties.initYandexConfigs();
        threadList.add(threadForYandexOCR);
        threadList.add(threadForEasyOCR);
        threadList.add(threadForTesseractOCR);
        threadList.add(recognizedTextHandler);
        threadList.add(threadForVoiceRecord);
        startVoiceRecognizeButton.addActionListener(new VoiceRecognizeListener(threadList));
        stopVoiceRecognize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    if (threadList.get(4) != null && threadList.get(4).isAlive()) {
                        threadList.get(4).interrupt();
                    }
                }
        });
        stopTranslating.addActionListener(new StopBroadcastingListener(threadList));
        translateAreaButton.addActionListener(new StartBroadcastingListener(threadList, yandexConfigProperties, this));

        setFrameSize(mainFrame);
        initializeTexts(mainFrame);
        initializeSupportedLanguages();
        sourceLanguage.setVisible(true);
        targetLanguage.setVisible(true);
        mainFrame.add(MainPanel);
        mainFrame.setVisible(true);
        //mainFrame.setAlwaysOnTop(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initializeSupportedLanguages() {
        supportedLanguagesList.add(SupportedLanguages.English);
        supportedLanguagesList.add(SupportedLanguages.French);
        supportedLanguagesList.add(SupportedLanguages.Russian);
        for ( SupportedLanguages language : supportedLanguagesList ) {
            chooseTargetLanguageComboBox.addItem(language);
            chooseSourceLanguageComboBox.addItem(language);
        }
    }

    private void initializeTexts(Frame f) {
        f.setTitle("Screen Translator");
        sourceLanguage.setText("Source language");
        targetLanguage.setText("Target language");
    }

    private void setFrameSize(Frame f) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthPercentage = 30;
        int heightPercentage = 30;
        int frameWidth = (screenSize.width * widthPercentage) / 100;
        int frameHeight = (screenSize.height * heightPercentage) / 100;
        f.setSize(frameWidth, frameHeight);
    }
}



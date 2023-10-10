package ru.ssugt.forms;

import ru.ssugt.i18n.SupportedLanguages;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;
import ru.ssugt.integration.yandex.vision.YandexVisionApi;
import ru.ssugt.listeners.action.StartBroadcastingListener;
import ru.ssugt.listeners.action.StopBroadcastingListener;
import ru.ssugt.logger.Log;
import ru.ssugt.threads.OCR.ThreadForEasyOCR;
import ru.ssugt.threads.OCR.ThreadForTesseractOCR;
import ru.ssugt.threads.OCR.ThreadForYandexOCR;
import ru.ssugt.threads.RecognizedTextHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class MainForm implements Runnable {
    private JPanel MainPanel;
    private JTextArea fieldForTranslatedText;
    private JTextArea fieldForTextToTranslate;
    private JComboBox<SupportedLanguages> chooseSourceLanguageComboBox;
    private JLabel sourceLanguage;
    private JLabel targetLanguage;
    private JComboBox<SupportedLanguages> chooseTargetLanguageComboBox;
    private JButton translateAreaButton;
    private JButton stopTranslating;
    private final ArrayList<SupportedLanguages> supportedLanguagesList = new ArrayList<>();
    private final Log log;
    public JFrame mainFrame = new JFrame();
    private ThreadForYandexOCR threadForYandexOCR;
    private ThreadForEasyOCR threadForEasyOCR;
    private ThreadForTesseractOCR threadForTesseractOCR;
    private RecognizedTextHandler recognizedTextHandler;
    private final List<Thread> threadList = new ArrayList<>();
    private TranslatedTextForm textForm;
    private YandexTranslateApi yandexTranslateApi;
    private YandexVisionApi yandexVisionApi;

    public MainForm(Log log, YandexTranslateApi yandexTranslateApi, YandexVisionApi yandexVisionApi) {
        this.log = log;
        this.yandexVisionApi = yandexVisionApi;
        this.yandexTranslateApi = yandexTranslateApi;
    }

    public void run() {
        threadList.add(threadForYandexOCR);
        threadList.add(threadForEasyOCR);
        threadList.add(threadForTesseractOCR);
        threadList.add(recognizedTextHandler);
        stopTranslating.addActionListener(new StopBroadcastingListener(threadList));
        translateAreaButton.addActionListener(new StartBroadcastingListener(threadList, yandexVisionApi));

        log.GetLogger().info("Logger has been success created");
        setFrameSize(mainFrame);
        initializeTexts(mainFrame);
        initializeSupportedLanguages();
        fieldForTranslatedText.setLineWrap(true);
        fieldForTranslatedText.setWrapStyleWord(true);
        fieldForTextToTranslate.setLineWrap(true);
        fieldForTextToTranslate.setWrapStyleWord(true);
        sourceLanguage.setVisible(true);
        targetLanguage.setVisible(true);
        mainFrame.add(MainPanel);
        mainFrame.setVisible(true);
        //mainFrame.setAlwaysOnTop(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textForm = new TranslatedTextForm();
        SwingUtilities.invokeLater(textForm);
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
        fieldForTextToTranslate.setToolTipText("Write text for translate");
        fieldForTranslatedText.setToolTipText("Translated text");
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



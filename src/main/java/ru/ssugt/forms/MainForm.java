package ru.ssugt.forms;

import ru.ssugt.capture.BroadcastScreen;
import ru.ssugt.i18n.SupportedLanguages;
import ru.ssugt.integration.easyOCR.EasyOCRVision;
import ru.ssugt.integration.tesseractOCR.TesseractOCRVision;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;
import ru.ssugt.integration.yandex.vision.YandexVisionApi;
import ru.ssugt.logger.Log;
import ru.ssugt.threads.ThreadForEasyOCR;
import ru.ssugt.threads.ThreadForTesseractOCR;
import ru.ssugt.threads.ThreadForYandexOCR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;


public class MainForm implements Runnable {

    private JPanel MainPanel;
    private JButton translateButton;
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
    private BroadcastScreen broadcastScreen;
    public JFrame mainFrame = new JFrame();
    private ThreadForYandexOCR threadForYandexOCR;
    private ThreadForEasyOCR threadForEasyOCR;
    private ThreadForTesseractOCR threadForTesseractOCR;
    private TranslatedTextForm textForm;
    private final EasyOCRVision easyOCRVision = new EasyOCRVision();
    private final TesseractOCRVision tesseractOCRVision = new TesseractOCRVision();
    private YandexTranslateApi yandexTranslateApi;
    private YandexVisionApi yandexVisionApi;

    public MainForm(Log log, YandexTranslateApi yandexTranslateApi, YandexVisionApi yandexVisionApi) {
        this.log = log;
        this.yandexVisionApi = yandexVisionApi;
        this.yandexTranslateApi = yandexTranslateApi;
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTranslatedText(fieldForTextToTranslate.getText(), yandexTranslateApi);

            }
        });

        stopTranslating.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( threadForYandexOCR.isAlive() ) {
                    threadForYandexOCR.interrupt();
                }
                if ( threadForEasyOCR.isAlive() ) {
                    threadForEasyOCR.interrupt();
                }
                if ( threadForTesseractOCR.isAlive() ) {
                    threadForTesseractOCR.interrupt();
                }
            }
        });

        translateAreaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame areaForTranslation = new JFrame();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                areaForTranslation.setSize(screenSize.width, screenSize.height);
                areaForTranslation.setLocation(screenSize.width / 2 - areaForTranslation.getSize().width / 2, screenSize.height / 2 - areaForTranslation.getSize().height / 2);
                areaForTranslation.setUndecorated(true);
                areaForTranslation.setOpacity(0.5f);
                areaForTranslation.setVisible(true);

                areaForTranslation.addMouseListener(new MouseListener() {
                    double x = 0;
                    double y = 0;

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
                        CountDownLatch countDownLatch = new CountDownLatch(3);
                        threadForYandexOCR = new ThreadForYandexOCR(broadcastScreen, yandexVisionApi,countDownLatch);
                        threadForEasyOCR = new ThreadForEasyOCR(easyOCRVision, countDownLatch);
                        threadForTesseractOCR = new ThreadForTesseractOCR(tesseractOCRVision, countDownLatch);
                        threadForTesseractOCR.start();
                        threadForYandexOCR.start();
                        threadForEasyOCR.start();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
            }
        });
    }


    public void run() {

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

    private void setTranslatedText(String text, YandexTranslateApi yandexTranslateApi) {
        if ( text == null ) {
            return;
        }
        String sourceLang = ((SupportedLanguages) Objects.requireNonNull(chooseSourceLanguageComboBox.getSelectedItem())).code;
        String targetLang = ((SupportedLanguages) Objects.requireNonNull(chooseTargetLanguageComboBox.getSelectedItem())).code;
        String translatedText = yandexTranslateApi.getTranslatedText(text, sourceLang, targetLang);
        if ( translatedText != null ) {
            fieldForTranslatedText.setText(translatedText);
        }
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
        translateButton.setText("Translate");
        sourceLanguage.setText("Source language");
        targetLanguage.setText("Target language");
        fieldForTextToTranslate.setToolTipText("Write text for translate");
        fieldForTranslatedText.setToolTipText("Translated text");
        setPlaceHolderForTextFields(fieldForTextToTranslate, "Write text for translate");
        setPlaceHolderForTextFields(fieldForTranslatedText, "Translated text");
    }

    private void setFrameSize(Frame f) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int widthPercentage = 30;
        int heightPercentage = 30;
        int frameWidth = (screenSize.width * widthPercentage) / 100;
        int frameHeight = (screenSize.height * heightPercentage) / 100;
        f.setSize(frameWidth, frameHeight);
    }

    private void setPlaceHolderForTextFields(JTextArea textField, String placeHolder) {
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if ( textField.getText().equals(placeHolder) ) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if ( textField.getText().isEmpty() ) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeHolder);
                }
            }
        });
    }
}



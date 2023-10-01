import ru.ssugt.capture.ScreenCapture;
import ru.ssugt.integration.yandex.translator.i18n.SupportedLanguages;
import ru.ssugt.integration.yandex.translator.api.response.TranslateResponse;
import ru.ssugt.logger.Log;

import ru.ssugt.integration.yandex.translator.api.request.PostTranslateRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Objects;


public class MainForm implements Runnable {

    private JPanel MainPanel;
    private JButton translateButton;
    private JTextArea fieldForTranslatedText;
    private JTextArea fieldForTextToTranslate;
    private JComboBox<SupportedLanguages> chooseSourceLanguageComboBox;
    private JLabel sourceLanguage;
    private JLabel targetLanguage;
    private JComboBox<SupportedLanguages> chooseTargetLanguageComboBox;
    private final ArrayList<SupportedLanguages> supportedLanguagesList = new ArrayList<>();
    private final Log log;
    private ScreenCapture screenCapture;
    private final PostTranslateRequest postTranslateRequest = new PostTranslateRequest();
    private final TranslateResponse translateResponse = new TranslateResponse();

    public MainForm(Log log) {
        this.log = log;
        translateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sourceLang = ((SupportedLanguages) Objects.requireNonNull(chooseSourceLanguageComboBox.getSelectedItem())).code;
                String targetLang = ((SupportedLanguages) Objects.requireNonNull(chooseTargetLanguageComboBox.getSelectedItem())).code;
                String translatedText = translateResponse.get(postTranslateRequest.create(fieldForTextToTranslate.getText(),sourceLang, targetLang));
                if (translatedText != null) {
                    fieldForTranslatedText.setText(translatedText);
                }

            }
        });
    }

    public void run() {

        log.GetLogger().info("Logger has been success created");
        JFrame mainFrame = new JFrame();
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
        mainFrame.setAlwaysOnTop(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        screenCapture = new ScreenCapture();
        String currentDirectory = System.getProperty("user.dir");
        //Эта строчка запускает нерабочий скрипт
        //screenCapture.getScreenshot(0, 0, 0,0, currentDirectory + "/test.png");

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



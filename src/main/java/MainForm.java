import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import logger.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class MainForm implements Runnable {

    private JPanel MainPanel;
    private JButton submitButton;
    private JTextArea translatedField;
    private JTextArea fieldForTranslate;
    private JComboBox<SupportedLanguages> chooseSourceLanguageComboBox;
    private JLabel sourceLanguage;
    private JLabel targetLanguage;
    private JComboBox<SupportedLanguages> chooseTargetLanguageComboBox;
    private final ArrayList<SupportedLanguages> supportedLanguagesList = new ArrayList<>();

    private final Log log;

    public MainForm(Log log) {
        this.log = log;
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRequest();
            }
        });
    }

    public void run() {

        log.GetLogger().info("Logger has been success created");
        JFrame f = new JFrame();
        setFrameSize(f);
        initializeTexts(f);
        initializeSupportedLanguages();
        translatedField.setLineWrap(true);
        translatedField.setWrapStyleWord(true);
        fieldForTranslate.setLineWrap(true);
        fieldForTranslate.setWrapStyleWord(true);
        sourceLanguage.setVisible(true);
        targetLanguage.setVisible(true);
        f.add(MainPanel);
        f.setVisible(true);
        f.setAlwaysOnTop(true);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    private void createRequest() {

        HttpResponse response = postRequest();
        try {
            if ( response != null && response.getStatusLine().getStatusCode() == 200 ) {
                // Parse and show the translated text
                String responseBody = EntityUtils.toString(response.getEntity());
                Gson gson = new Gson();
                JsonObject responseEntity = gson.fromJson(responseBody, JsonObject.class);
                JsonArray translationsArr = responseEntity.get("translations").getAsJsonArray();
                JsonObject text = translationsArr.get(0).getAsJsonObject();
                String translatedText = text.get("text").getAsString();
                translatedField.setText(translatedText);
            } else {
                System.err.println("Failed to translate text. HTTP Status Code: " + response.getStatusLine().getStatusCode());
            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }

    }

    private HttpResponse postRequest() {
        HttpResponse response;
        String translateEndpoint = "https://translate.api.cloud.yandex.net/translate/v2/translate";
        String apiKey = "AQVN3BDYAydEHCgIM-NYR4OPN5UvryzXoMC8EXMI";
        String textToTranslate = fieldForTranslate.getText();

        String sourceLang = ((SupportedLanguages) Objects.requireNonNull(chooseSourceLanguageComboBox.getSelectedItem())).code;
        String targetLang = ((SupportedLanguages) Objects.requireNonNull(chooseTargetLanguageComboBox.getSelectedItem())).code;
        try {
            // Create an HttpClient
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost postRequest = new HttpPost(translateEndpoint);
            JsonArray jsonArray = new JsonArray();
            jsonArray.add(textToTranslate);
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("sourceLanguageCode", sourceLang);
            requestBody.addProperty("targetLanguageCode", targetLang);
            requestBody.add("texts", jsonArray);

            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setEntity(new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON));
            postRequest.setHeader("Authorization", "Api-Key " + apiKey);
            response = httpClient.execute(postRequest);
            return response;
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
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
        submitButton.setText("Translate");
        sourceLanguage.setText("Source language");
        targetLanguage.setText("Target language");
        fieldForTranslate.setToolTipText("Write text for translate");
        translatedField.setToolTipText("Translated text");
        setPlaceHolderForTextFields(fieldForTranslate, "Write text for translate");
        setPlaceHolderForTextFields(translatedField, "Translated text");
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



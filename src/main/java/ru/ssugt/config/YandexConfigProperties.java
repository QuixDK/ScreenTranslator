package ru.ssugt.config;

import lombok.Getter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;
import ru.ssugt.integration.yandex.translate.YandexTranslateApiConfig;
import ru.ssugt.integration.yandex.translate.YandexTranslateApiImpl;
import ru.ssugt.integration.yandex.vision.YandexVisionApi;
import ru.ssugt.integration.yandex.vision.YandexVisionApiConfig;
import ru.ssugt.integration.yandex.vision.YandexVisionApiImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@Getter
public class YandexConfigProperties {

    private YandexVisionApi yandexVisionApi;

    private YandexTranslateApi yandexTranslateApi;
    Path currRelativePath = Paths.get("");

    public void initYandexConfigs() {
        yandexVisionApi = initYandexVision();
        yandexTranslateApi = initYandexTranslate();
    }
    private YandexTranslateApi initYandexTranslate() {
        PropertiesConfiguration config = new PropertiesConfiguration();
        try {
            config.load(currRelativePath + "src/main/resources/application.properties");
            YandexTranslateApiConfig yandexTranslateApiConfig = new YandexTranslateApiConfig(
                    config.getString("apiKey"),
                    config.getString("translateHost"));
            return new YandexTranslateApiImpl(yandexTranslateApiConfig);
        } catch ( ConfigurationException e ) {
            throw new RuntimeException(e);
        }
    }

    private YandexVisionApi initYandexVision() {
        PropertiesConfiguration config = new PropertiesConfiguration();
        try {
            config.load(currRelativePath + "src/main/resources/application.properties");
            String command = "curl -d \"{\\\"yandexPassportOauthToken\\\":\\\"" + config.getString("OAuthToken") + "\\\"}\" \"https://iam.api.cloud.yandex.net/iam/v1/tokens\"\n";
            Process process = Runtime.getRuntime().exec(command);
            String IAMToken = "";
            StringBuilder sb = new StringBuilder();
            java.util.List<Integer> index = new ArrayList<>();
            int i = 0;
            for ( int ch; (ch = process.getInputStream().read()) != -1; ) {
                sb.append((char) ch);
                char c = (char) ch;

                if ( c == '\"' ) {
                    index.add(i);
                }
                i++;
            }
            StringBuffer stringBuffer = new StringBuffer(sb.toString());
            IAMToken = stringBuffer.substring(index.get(2) + 1, index.get(3));
            config.setProperty("IAMToken", IAMToken);
            config.save("application.properties");
            YandexVisionApiConfig yandexVisionApiConfig = new YandexVisionApiConfig(
                    config.getString("IAMToken"),
                    config.getString("visionHost"),
                    config.getString("folderId"));
            return new YandexVisionApiImpl(yandexVisionApiConfig);
        } catch ( ConfigurationException e ) {
            throw new RuntimeException(e);
        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }
    }
}

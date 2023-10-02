package ru.ssugt.config;

import org.apache.commons.configuration.PropertiesConfiguration;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;
import ru.ssugt.integration.yandex.translate.YandexTranslateApiConfig;
import ru.ssugt.integration.yandex.translate.YandexTranslateApiImpl;
import ru.ssugt.integration.yandex.vision.YandexVisionApi;
import ru.ssugt.integration.yandex.vision.YandexVisionApiConfig;
import ru.ssugt.integration.yandex.vision.YandexVisionApiImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ConfigProperties {



    public YandexTranslateApi initYandexTranslate() {
        PropertiesConfiguration config = new PropertiesConfiguration();
        try {
            Path currRelativePath = Paths.get("");
            config.load(currRelativePath + "main//resources//application.properties");
            YandexTranslateApiConfig yandexTranslateApiConfig = new YandexTranslateApiConfig(
                    config.getString("apiKey"),
                    config.getString("translateHost"));
            return new YandexTranslateApiImpl(yandexTranslateApiConfig);
        }
        catch ( Exception e ) {
            System.out.println("Config file is not found");
            return null;
        }
    }
    public YandexVisionApi initYandexVision() {
        PropertiesConfiguration config = new PropertiesConfiguration();
        try {
            Path currRelativePath = Paths.get("");
            config.load(currRelativePath + "main//resources//application.properties");
            String command = "curl -d \"{\\\"yandexPassportOauthToken\\\":\\\"" + config.getString("OAuthToken") + "\\\"}\" \"https://iam.api.cloud.yandex.net/iam/v1/tokens\"\n";
            Process process = Runtime.getRuntime().exec(command);
            String IAMToken = "";
            StringBuilder sb = new StringBuilder();
            java.util.List<Integer> index = new ArrayList<>();
            int i = 0;
            for (int ch; (ch = process.getInputStream().read()) != -1; ) {
                sb.append((char) ch);
                char c = (char) ch;

                if (c == '\"') {
                    index.add(i);
                }
                i++;
            }
            StringBuffer stringBuffer = new StringBuffer(sb.toString());
            IAMToken = stringBuffer.substring(index.get(2)+1, index.get(3));
            config.setProperty("IAMToken", IAMToken);
            YandexVisionApiConfig yandexVisionApiConfig = new YandexVisionApiConfig(
                    config.getString("IAMToken"),
                    config.getString("visionHost"),
                    config.getString("folderId"));
            return new YandexVisionApiImpl(yandexVisionApiConfig);
        }
        catch ( Exception e ) {
            System.out.println("Config file is not found");
            return null;
        }
    }
}

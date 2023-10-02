import com.formdev.flatlaf.FlatDarkLaf;
import org.apache.commons.configuration.PropertiesConfiguration;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;
import ru.ssugt.integration.yandex.translate.YandexTranslateApiConfig;
import ru.ssugt.integration.yandex.translate.YandexTranslateApiImpl;
import ru.ssugt.integration.yandex.vision.YandexVisionApi;
import ru.ssugt.integration.yandex.vision.YandexVisionApiConfig;
import ru.ssugt.integration.yandex.vision.YandexVisionApiImpl;
import ru.ssugt.logger.Log;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final Log log = new Log();

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        for (Window window : JFrame.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
        }

        PropertiesConfiguration config = new PropertiesConfiguration();
        try {
            Path currRelativePath = Paths.get("");
            config.load(currRelativePath + "main//resources//application.properties");
            YandexTranslateApiConfig yandexTranslateApiConfig = new YandexTranslateApiConfig(
                    config.getString("apiKey"),
                    config.getString("translateHost"));
            YandexTranslateApi yandexTranslateApi = new YandexTranslateApiImpl(yandexTranslateApiConfig);

            YandexVisionApiConfig yandexVisionApiConfig = new YandexVisionApiConfig(
                    config.getString("IAMToken"),
                    config.getString("visionHost"),
                    config.getString("folderId"));
            YandexVisionApi yandexVisionApi = new YandexVisionApiImpl(yandexVisionApiConfig);

            SwingUtilities.invokeLater(new MainForm(log, yandexTranslateApi, yandexVisionApi));
        }
        catch ( Exception e ) {
            System.out.println("Config file is not found");
        }


    }
}
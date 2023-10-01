import com.formdev.flatlaf.FlatDarkLaf;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;
import ru.ssugt.integration.yandex.translate.YandexTranslateApiConfig;
import ru.ssugt.integration.yandex.translate.YandexTranslateApiImpl;
import ru.ssugt.logger.Log;

import javax.swing.*;
import java.awt.*;

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

        // в конфиг типа .yaml или .properties унести надо
        YandexTranslateApiConfig yandexTranslateApiConfig = new YandexTranslateApiConfig(
                "AQVN3BDYAydEHCgIM-NYR4OPN5UvryzXoMC8EXMI",
                "https://translate.api.cloud.yandex.net/translate/v2/translate");
        YandexTranslateApi yandexTranslateApi = new YandexTranslateApiImpl(yandexTranslateApiConfig);
        SwingUtilities.invokeLater(new MainForm(log, yandexTranslateApi));
    }
}
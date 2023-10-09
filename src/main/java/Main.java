import com.formdev.flatlaf.FlatDarkLaf;
import ru.ssugt.config.ConfigProperties;
import ru.ssugt.forms.MainForm;
import ru.ssugt.integration.easyOCR.EasyOCRVision;
import ru.ssugt.integration.tesseractOCR.TesseractOCRVision;
import ru.ssugt.integration.yandex.translate.YandexTranslateApi;
import ru.ssugt.integration.yandex.vision.YandexVisionApi;
import ru.ssugt.logger.Log;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Main {

    private static final Log log = new Log();

    private static final ConfigProperties configProperties = new ConfigProperties();

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch ( Exception ex ) {
            System.err.println("Failed to initialize LaF");
        }
        for ( Window window : JFrame.getWindows() ) {
            SwingUtilities.updateComponentTreeUI(window);
        }
        YandexVisionApi yandexVisionApi = configProperties.initYandexVision();
        YandexTranslateApi yandexTranslateApi = configProperties.initYandexTranslate();
        SwingUtilities.invokeLater(new MainForm(log, yandexTranslateApi, yandexVisionApi));

    }
}
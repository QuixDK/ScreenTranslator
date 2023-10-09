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
        EasyOCRVision easyOCRVision = new EasyOCRVision();
        easyOCRVision.init("D:\\Java Projects\\ScreenTranslator\\testscreen.jpg");
        TesseractOCRVision tesseractOCRVision = new TesseractOCRVision();
        List<String> langs = new ArrayList<>();
        langs.add("rus");
        langs.add("eng");
        tesseractOCRVision.init("D:\\Java Projects\\ScreenTranslator\\testscreen.jpg", langs);
        //System.exit(0);
        YandexVisionApi yandexVisionApi = configProperties.initYandexVision();
        YandexTranslateApi yandexTranslateApi = configProperties.initYandexTranslate();
        SwingUtilities.invokeLater(new MainForm(log, yandexTranslateApi, yandexVisionApi));

    }
}
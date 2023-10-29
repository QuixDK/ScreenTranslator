package ru.ssugt.integration.yandex.stt;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import ru.ssugt.integration.ScriptHandler;

import java.nio.file.Path;
import java.nio.file.Paths;

public class YandexSTT {
    public String recognizeText(String pathToFile, String sourceLang) {
        ScriptHandler scriptHandler = new ScriptHandler();
        PropertiesConfiguration config = new PropertiesConfiguration();
        try {
            Path currRelativePath = Paths.get("");
            config.load(currRelativePath + "src/main/resources/application.properties");
            String command = "python \"D:/Java Projects/cloudapi/output/test.py\" --token \"" + config.getString("IAMToken") + "\" --folder_id \"" + config.getString("folderTTSID") + "\" --path \"" +pathToFile + "\"" ;
            return scriptHandler.executeScript(command);
        } catch ( ConfigurationException e ) {
            throw new RuntimeException(e);
        }

    }
}

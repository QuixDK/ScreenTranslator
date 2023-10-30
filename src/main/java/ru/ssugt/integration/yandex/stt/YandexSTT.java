package ru.ssugt.integration.yandex.stt;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import ru.ssugt.integration.ScriptHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class YandexSTT {
    public String recognizeText(String pathToFile, String sourceLang) {
        PropertiesConfiguration config = new PropertiesConfiguration();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", "export IAM_TOKEN=" + config.getString("IAMToken"));
            Process process = processBuilder.start();
            processBuilder.command("bash", "-c", "export FOLDER_ID=" + config.getString("folderTTSID"));

            process = processBuilder.start();
            Path currRelativePath = Paths.get("");
            config.load(currRelativePath + "src/main/resources/application.properties");

            processBuilder.command("bash", "-c", "python \"D:/Java Projects/cloudapi/output/test.py\" --token ${IAM_TOKEN} --folder_id ${FOLDER_ID} --path \"" + pathToFile + "\"");
            process = processBuilder.start();

            int exitCode = process.waitFor();

            byte[] input = process.getErrorStream().readAllBytes();


            //String command = "python \"D:/Java Projects/cloudapi/output/test.py\" --token ${IAM_TOKEN} --folder_id ${FOLDER_ID} --path \"" + pathToFile + "\"" ;
            return new String(input);//scriptHandler.executeScript(command);
        } catch ( ConfigurationException | IOException | InterruptedException e ) {
            throw new RuntimeException(e);
        }

    }
}

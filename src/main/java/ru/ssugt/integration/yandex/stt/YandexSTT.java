package ru.ssugt.integration.yandex.stt;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.hibernate.SessionFactory;
import ru.ssugt.config.HibernateUtil;
import ru.ssugt.integration.ScriptHandler;
import ru.ssugt.repository.RecognizedVoiceRepository;
import ru.ssugt.repository.RecognizedVoiceRepositoryImpl;
import ru.ssugt.service.RecognizedVoiceService;
import ru.ssugt.service.RecognizedVoiceServiceImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class YandexSTT {


    public String recognizeText(String pathToFile, String sourceLang) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        RecognizedVoiceRepository recognizedVoiceRepository = new RecognizedVoiceRepositoryImpl(sessionFactory);
        RecognizedVoiceService recognizedVoiceService = new RecognizedVoiceServiceImpl(recognizedVoiceRepository);
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

            byte[] input = process.getInputStream().readAllBytes();

            recognizedVoiceService.saveText(new String(input), null, null);
            //String command = "python \"D:/Java Projects/cloudapi/output/test.py\" --token ${IAM_TOKEN} --folder_id ${FOLDER_ID} --path \"" + pathToFile + "\"" ;
            return new String(input);//scriptHandler.executeScript(command);
        } catch ( ConfigurationException | IOException | InterruptedException e ) {
            throw new RuntimeException(e);
        }

    }
}

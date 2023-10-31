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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.module.Configuration;
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
            Path currRelativePath = Paths.get("");
            config.load(currRelativePath + "src/main/resources/application.properties");
            ScriptHandler scriptHandler = new ScriptHandler();
            String command = "python \"D:/Java Projects/cloudapi/output/test.py\" --token \"" + config.getString("IAMToken") + "\" --folder_id \"" + config.getString("folderTTSID") + "\" --path \"" + pathToFile + "\"";
            String response = scriptHandler.executeScript(command);
            File file = new File(pathToFile);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, read);
            }

            byte[] byteArray = out.toByteArray();
            out.close();
            audioInputStream.close();
            recognizedVoiceService.saveText(response, byteArray, null);

            return response;
        } catch ( ConfigurationException | UnsupportedAudioFileException | IOException e ) {
            throw new RuntimeException(e);
        }

    }
}

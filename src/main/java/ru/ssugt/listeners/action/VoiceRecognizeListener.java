package ru.ssugt.listeners.action;


import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VoiceRecognizeListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            AudioFormat format = new AudioFormat(44100, 16, 2, true, true);

            // Get a target data line
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);

            // Open the target data line
            targetLine.open(format);

            // Start capturing audio
            targetLine.start();
            Path currRelativePath = Paths.get("");

            // Write captured audio data to a file
            File outputFile = new File(currRelativePath + "src/main/resources/temp/file.wav");
            AudioSystem.write(new AudioInputStream(targetLine), AudioFileFormat.Type.WAVE, outputFile);

            // Record audio for 5 seconds
            Thread.sleep(5000);

            // Stop capturing audio and close resources
            targetLine.stop();
            targetLine.close();
        } catch ( LineUnavailableException | IOException | InterruptedException ex ) {
            ex.printStackTrace();
        }
    }
}

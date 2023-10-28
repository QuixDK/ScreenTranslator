package ru.ssugt.threads.voice;

import lombok.Getter;
import ru.ssugt.integration.tesseractOCR.TesseractOCRVision;
import ru.ssugt.integration.yandex.stt.YandexSTT;
import ru.ssugt.threads.DoneSignal;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ThreadForVoiceRecord extends Thread implements Runnable {
    Path currRelativePath = Paths.get("");
    File outputFile = new File(currRelativePath + "src/main/resources/temp/test.wav");
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    TargetDataLine line;

    @Getter
    private String recognizedText;

    @Override
    public void run() {
        displayMixerInfo();
        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0f, 16, 1, 2, 2, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing

            System.out.println("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Start recording...");

            // start recording
            AudioSystem.write(ais, fileType, outputFile);


        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
    public void finish() {
        line.stop();
        line.close();
        System.out.println("Finished");
    }

    public static void displayMixerInfo() {
        Mixer.Info[] mixersInfo = AudioSystem.getMixerInfo();

        for ( Mixer.Info mixerInfo : mixersInfo ) {
            System.out.println("Mixer: " + mixerInfo.getName());

            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            Line.Info[] sourceLineInfo = mixer.getSourceLineInfo();
            for ( Line.Info info : sourceLineInfo ) {
                showLineInfo(info);
            }

            Line.Info[] targetLineInfo = mixer.getTargetLineInfo();
            for ( Line.Info info : targetLineInfo ) {
                showLineInfo(info);
            }
        }
    }


    private static void showLineInfo(Line.Info lineInfo) {
        System.out.println("  " + lineInfo.toString());

        if ( lineInfo instanceof DataLine.Info ) {
            DataLine.Info dataLineInfo = (DataLine.Info) lineInfo;

            AudioFormat[] formats = dataLineInfo.getFormats();
            for ( AudioFormat format : formats ) {
                System.out.println("    " + format.toString());
            }
        }
    }
}

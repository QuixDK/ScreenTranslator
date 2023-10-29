package ru.ssugt.listeners.action;


import ru.ssugt.integration.yandex.stt.YandexSTT;
import ru.ssugt.threads.voice.ThreadForVoiceRecord;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class VoiceRecognizeListener implements ActionListener {
    private final List<Thread> threadList;
    Path currRelativePath = Paths.get("");
    ThreadForVoiceRecord recorder = null;
    static final long RECORD_TIME = 2000;

    public VoiceRecognizeListener(List<Thread> threadList) {
        this.threadList = threadList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        YandexSTT yandexSTT = new YandexSTT();
        threadList.set(4, new ThreadForVoiceRecord());

        if ( threadList.get(4) instanceof ThreadForVoiceRecord ) {
            recorder = (ThreadForVoiceRecord) threadList.get(4);
        }
        while (true) {
            Thread stopper = new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(RECORD_TIME);
                    } catch ( InterruptedException ex ) {

                    }
                    recorder.finish();
                }
            });
            stopper.start();
            // start recording
            recorder.start();
            String recognizedText = yandexSTT.recognizeText(currRelativePath + "src/main/resources/temp/file.wav", "ru");
            System.out.println(recognizedText);
        }
    }

}

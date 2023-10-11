package ru.ssugt.listeners.action;

import ru.ssugt.integration.CMUSphinx.Voice;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class VoiceRecognizeListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Voice voice = new Voice();
        try {
            voice.startRecognize();
        } catch ( IOException ex ) {
            throw new RuntimeException(ex);
        }
    }
}

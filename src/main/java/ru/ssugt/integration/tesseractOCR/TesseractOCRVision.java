package ru.ssugt.integration.tesseractOCR;

import ru.ssugt.integration.ScriptHandler;

import java.util.List;

public class TesseractOCRVision {

    public String recognizeText(String path, List<String> langs) {
        ScriptHandler scriptHandler = new ScriptHandler();
        StringBuilder languages = new StringBuilder();
        for ( int i = 0; i < langs.size(); i++ ) {
            languages.append(langs.get(i));
            if ( i != langs.size() - 1 ) {
                languages.append("+");
            }
        }
        String command = "tesseract \"" + path + "\" - -l " + languages;
        return scriptHandler.executeScript(command);
    }
}

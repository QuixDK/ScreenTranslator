package ru.ssugt.integration.easyOCR;

import org.graalvm.polyglot.Source;
import ru.ssugt.integration.ScriptHandler;

public class EasyOCRVision {

    public String recognizeText(String pathToFile, String sourceLang) {

        ScriptHandler scriptHandler = new ScriptHandler();
        String command = "python pyScripts\\easyOCR.py \"" + pathToFile + "\" + \"" + sourceLang + "\"";
        return scriptHandler.executeScript(command);
    }
}

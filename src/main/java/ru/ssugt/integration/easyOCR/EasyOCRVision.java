package ru.ssugt.integration.easyOCR;

import ru.ssugt.integration.ScriptHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EasyOCRVision {

    public String recognizeText(String pathToFile, String sourceLang) {

        ScriptHandler scriptHandler = new ScriptHandler();
        String command = "python pyScripts\\easyOCR.py \"" + pathToFile + "\" + \"" + sourceLang + "\"";
        return scriptHandler.executeScript(command);
    }
}

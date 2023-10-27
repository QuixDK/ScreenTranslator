package ru.ssugt.integration.yandex.stt;


import ru.ssugt.integration.ScriptHandler;

public class YandexSTT {
    public String recognizeText(String pathToFile, String sourceLang) {
        ScriptHandler scriptHandler = new ScriptHandler();
        //String command = "python pyScripts\\easyOCR.py \"" + pathToFile + "\" + \"" + sourceLang + "\"";
        //return scriptHandler.executeScript(command);
        return null;
    }
}

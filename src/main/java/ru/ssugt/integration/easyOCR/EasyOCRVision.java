package ru.ssugt.integration.easyOCR;

import org.graalvm.polyglot.Source;
import ru.ssugt.integration.ScriptHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public class EasyOCRVision {

    public String recognizeText(String pathToFile, String sourceLang) {


//        try (Context context = Context.create()) {
//            String pythonScript =
//                    "import easyocr\n" +
//                            "reader = easyocr.Reader([\"" + sourceLang + "\"])\n" +
//                            "result = reader.readtext(\"" + pathToFile + "\")\n" +
//                            "result";
//
//            Value bindings = context.eval("python", pythonScript);
//
//            System.out.println(bindings.toString());
//        }

        ScriptHandler scriptHandler = new ScriptHandler();
        String command = "python pyScripts\\easyOCR.py \"" + pathToFile + "\" + \"" + sourceLang + "\"";
        return scriptHandler.executeScript(command);
    }
}

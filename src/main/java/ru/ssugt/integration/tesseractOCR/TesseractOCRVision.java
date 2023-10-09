package ru.ssugt.integration.tesseractOCR;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TesseractOCRVision {

    public String recognizeText(String path, List<String> langs) {
        String s = null;
        StringBuilder result =  new StringBuilder();
        try {
            StringBuilder languages = new StringBuilder();
            for ( int i = 0; i < langs.size(); i++ ) {
                languages.append(langs.get(i));
                if ( i != langs.size() - 1 ) {
                    languages.append("+");
                }
            }
            String command = "tesseract \"" + path + "\" - -l " + languages;
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));

            while ((s = stdInput.readLine()) != null) {
                result.append(s);
            }

//            while ((s = stdError.readLine()) != null) {
//                System.out.println(s);
//            }


        } catch ( Exception ex ) {

        }
        return result.toString();
    }
}

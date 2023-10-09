package ru.ssugt.integration.easyOCR;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EasyOCRVision {

    public String recognizeText(String pathToFile) {

        String s = null;
        StringBuilder result = new StringBuilder();
        try {
            Process p = Runtime.getRuntime().exec("python pyScripts\\main.py \"" + pathToFile + "\"");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

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

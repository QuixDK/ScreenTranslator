package ru.ssugt.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ScriptHandler {
    public String executeScript(String command) {
        String s;
        StringBuilder result =  new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));

            while ((s = stdInput.readLine()) != null) {
                result.append(s);
            }

            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            return result.toString();

        } catch ( IOException e ) {
            throw new RuntimeException(e);
        }

    }
}

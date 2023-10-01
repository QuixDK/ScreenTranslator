package ru.ssugt.integration.easyOCR.OCR;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextRecognize {

    public String recognize(String imageFilePath) {

        String currentDirectory = System.getProperty("user.dir");

        String pythonScriptName = "OCRscript.py";
        String pythonScriptPath = currentDirectory + "\\" + pythonScriptName;
        System.out.println(pythonScriptPath);
        try {
            String command = "python \"" + pythonScriptPath + "\" \"" + imageFilePath + "\"";
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode);

            // Получаем результат работы OCR как JSON-строку
            String ocrResult = result.toString();
            System.out.println("OCR Result: " + ocrResult);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}









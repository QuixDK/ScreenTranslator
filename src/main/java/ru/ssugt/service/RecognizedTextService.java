package ru.ssugt.service;

public interface RecognizedTextService {
    void saveText(String yandexText, String tesseractText, String easyText, byte[] base64Picture, String correctAnswer);

}
